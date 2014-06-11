/**
 * DISCLAIMER
 * 
 * The quality of the code is such that you should not copy any of it as best
 * practice how to build Vaadin applications.
 * 
 * @author jouni@vaadin.com
 * 
 */

package bs.cadastre.dashboard;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Set;

import bs.cadastre.dashboard.data.FacilityContainer;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.Action;
import com.vaadin.event.Action.Handler;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.event.ShortcutListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.TableDragMode;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class DemoView extends VerticalLayout implements View {

    private static final long serialVersionUID = 1L;

    Table t;

    FacilityContainer data;

    @Override
    public void enter(ViewChangeEvent event) {
        data = ((DashboardUI) getUI()).dataProvider.getFacilitiesContainer();

        setSizeFull();
        addStyleName("facilities");

        t = new Table() {
            @Override
            protected String formatPropertyValue(Object rowId, Object colId,
                    Property<?> property) {
                if (colId.equals("Time")) {
                    SimpleDateFormat df = new SimpleDateFormat();
                    df.applyPattern("MM/dd/yyyy hh:mm:ss a");
                    return df
                            .format(((Calendar) property.getValue()).getTime());
                } else if (colId.equals("Price")) {
                    if (property != null && property.getValue() != null) {
                        String ret = new DecimalFormat("#.##").format(property
                                .getValue());
                        return "$" + ret;
                    } else {
                        return "";
                    }
                }
                return super.formatPropertyValue(rowId, colId, property);
            }
        };
        t.setPageLength(10);
        t.setSizeFull();
        t.addStyleName("borderless");
        t.setSelectable(true);
        t.setColumnCollapsingAllowed(true);
        t.setColumnReorderingAllowed(true);
        data.removeAllContainerFilters();
        t.setContainerDataSource(data);
        t.setVisibleColumns(new Object[]{"CadastrNumber", "Area", "Destination", "Location", "PermittedUsage", "Address"});

        sortTable();

        // Allow dragging items to the reports menu
        t.setDragMode(TableDragMode.MULTIROW);
        t.setMultiSelect(true);

        HorizontalLayout toolbar = new HorizontalLayout();
        toolbar.setWidth("100%");
        toolbar.setSpacing(true);
        toolbar.setMargin(true);
        toolbar.addStyleName("toolbar");
        addComponent(toolbar);

        Label title = new Label("Объекты недвижимости");
        title.addStyleName("h1");
        title.setSizeUndefined();
        toolbar.addComponent(title);
        toolbar.setComponentAlignment(title, Alignment.MIDDLE_LEFT);

        final TextField filter = new TextField();
        /*filter.addTextChangeListener(new TextChangeListener() {
            @Override
            public void textChange(final TextChangeEvent event) {
                data.removeAllContainerFilters();
                data.addContainerFilter(new Filter() {
                    @Override
                    public boolean passesFilter(Object itemId, Item item)
                            throws UnsupportedOperationException {

                        if (event.getText() == null
                                || event.getText().equals("")) {
                            return true;
                        }

                        return filterByProperty("Country", item,
                                event.getText())
                                || filterByProperty("City", item,
                                        event.getText())
                                || filterByProperty("Title", item,
                                        event.getText());

                    }

                    @Override
                    public boolean appliesToProperty(Object propertyId) {
                        if (propertyId.equals("Country")
                                || propertyId.equals("City")
                                || propertyId.equals("Title"))
                            return true;
                        return false;
                    }
                });
            }
        });*/
        // final ComboBox filter = new ComboBox();
        // filter.setNewItemsAllowed(true);
        // filter.setNewItemHandler(new NewItemHandler() {
        // @Override
        // public void addNewItem(String newItemCaption) {
        // filter.addItem(newItemCaption);
        // }
        // });
        // filter.addItem("test");
        // filter.addItem("finland");
        // filter.addItem("paranorman");

        // filter.addStyleName("small");
        filter.setInputPrompt("Фильтр");
        filter.addShortcutListener(new ShortcutListener("Clear",
                KeyCode.ESCAPE, null) {
            @Override
            public void handleAction(Object sender, Object target) {
                filter.setValue("");
                data.removeAllContainerFilters();
            }
        });
        toolbar.addComponent(filter);
        toolbar.setExpandRatio(filter, 1);
        toolbar.setComponentAlignment(filter, Alignment.MIDDLE_LEFT);

        t.addActionHandler(new Handler() {

            private Action details = new Action("Детали");

            @Override
            public void handleAction(Action action, Object sender, Object target) {
                if (action == details) {
                    Item item = ((Table) sender).getItem(target);
                    if (item != null) {
                        Window w = new FacilityView();
                        UI.getCurrent().addWindow(w);
                        w.focus();
                    }
                }
            }

            @Override
            public Action[] getActions(Object target, Object sender) {
                return new Action[] { details };
            }
        });

        t.addValueChangeListener(new ValueChangeListener() {
            @Override
            public void valueChange(ValueChangeEvent event) {
                if (t.getValue() instanceof Set) {
                    Set<Object> val = (Set<Object>) t.getValue();
                }
            }
        });
        t.setImmediate(true);

        addComponent(t);
        setExpandRatio(t, 1);

        /*t.addGeneratedColumn("Title", new ColumnGenerator() {
            @Override
            public Object generateCell(Table source, Object itemId,
                    Object columnId) {
                final String title = source.getItem(itemId)
                        .getItemProperty(columnId).getValue().toString();
                Button titleLink = new Button(title);
                titleLink.addStyleName("link");
                titleLink.addClickListener(new ClickListener() {
                    @Override
                    public void buttonClick(ClickEvent event) {
                        Window w = new FacilityView();
                        UI.getCurrent().addWindow(w);
                    }
                });
                return title;
            }
        });*/
    }

    private void sortTable() {
        t.sort(new Object[] { "CadastrNumber" }, new boolean[] { false });
    }

    private boolean filterByProperty(String prop, Item item, String text) {
        if (item == null || item.getItemProperty(prop) == null
                || item.getItemProperty(prop).getValue() == null)
            return false;
        String val = item.getItemProperty(prop).getValue().toString().trim()
                .toLowerCase();
        if (val.startsWith(text.toLowerCase().trim()))
            return true;
        return false;
    }
}

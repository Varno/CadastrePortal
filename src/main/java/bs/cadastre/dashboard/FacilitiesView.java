package bs.cadastre.dashboard;

import bs.cadastre.dashboard.domain.*;
import bs.cadastre.dashboard.FacilityEditor.EditorSavedEvent;
import bs.cadastre.dashboard.FacilityEditor.EditorSavedListener;
import bs.cadastre.dashboard.providers.FacilityProvider;
import com.vaadin.addon.jpacontainer.EntityProvider;
import com.vaadin.data.util.BeanItem;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import org.springframework.context.annotation.Scope;
import org.springframework.context.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.transaction.annotation.Transactional;
import ru.xpoft.vaadin.VaadinView;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@org.springframework.stereotype.Component
@Scope("prototype")
@VaadinView(FacilitiesView.NAME)
@Transactional
public class FacilitiesView extends VerticalLayout implements ComponentContainer, View {

    public static final String NAME = "facilities";

    @Resource(name = "facilityProvider")
    private EntityProvider<Facility> facilityProvider;

    private Table facilityTable;

    private TextField searchField;

    private Button newButton;
    private Button deleteButton;
    private Button editButton;

    private JPAContainer<Facility> facilities;

    private String textFilter;

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

        this.facilities = new JPAContainer(Facility.class);
        this.facilities.setEntityProvider(this.facilityProvider);
        this.buildMainArea();
    }

    private void buildMainArea() {
        VerticalLayout verticalLayout = new VerticalLayout();
        addComponent(verticalLayout);

        facilityTable = new Table(null, this.facilities);
        facilityTable.setSelectable(true);
        facilityTable.setImmediate(true);
        facilityTable.addListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(ValueChangeEvent event) {
                setModificationsEnabled(event.getProperty().getValue() != null);
            }

            private void setModificationsEnabled(boolean b) {
                deleteButton.setEnabled(b);
                editButton.setEnabled(b);
            }
        });

        facilityTable.setSizeFull();
        facilityTable.setSelectable(true);
        facilityTable.addListener(new ItemClickListener() {
            @Override
            public void itemClick(ItemClickEvent event) {
                if (event.isDoubleClick()) {
                    facilityTable.select(event.getItemId());
                }
            }
        });

        facilityTable.setColumnHeader("cadastrNumber", "Кадастровый номер");
        facilityTable.setColumnHeader("area", "Площадь");
        facilityTable.setColumnHeader("destination", "Назначение");
        facilityTable.setColumnHeader("location", "Местоположение");
        facilityTable.setColumnHeader("permittedUsage", "Разрешенное использование");
        facilityTable.setColumnHeader("address", "Адрес");

        facilityTable.setVisibleColumns(new Object[]{"cadastrNumber", "area",
                "destination", "location", "permittedUsage", "address"});

        HorizontalLayout toolbar = new HorizontalLayout();
        newButton = new Button("Добавить");
        newButton.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                final BeanItem<Facility> newPersonItem = new BeanItem<Facility>(
                        new Facility());
                FacilityEditor facilityEditor = new FacilityEditor(newPersonItem);
                facilityEditor.addListener(new EditorSavedListener() {
                    @Override
                    public void editorSaved(EditorSavedEvent event) {
                        Facility entity = newPersonItem.getBean();
                        facilities.addEntity(entity);
                    }
                });
                UI.getCurrent().addWindow(facilityEditor);
            }
        });

        deleteButton = new Button("Удалить");
        deleteButton.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                facilities.removeItem(facilityTable.getValue());
            }
        });
        deleteButton.setEnabled(false);

        editButton = new Button("Изменить");
        editButton.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                UI.getCurrent().addWindow(
                        new FacilityEditor(facilityTable.getItem(facilityTable
                                .getValue())));
            }
        });
        editButton.setEnabled(false);

        searchField = new TextField();
        searchField.setInputPrompt("Поиск");
        searchField.addTextChangeListener(new TextChangeListener() {

            @Override
            public void textChange(TextChangeEvent event) {
/*
                textFilter = event.getText();
                updateFilters();
*/
            }
        });

        toolbar.addComponent(newButton);
        toolbar.addComponent(deleteButton);
        toolbar.addComponent(editButton);
        toolbar.addComponent(searchField);
        toolbar.setWidth("100%");
        toolbar.setExpandRatio(searchField, 1);
        toolbar.setComponentAlignment(searchField, Alignment.TOP_RIGHT);

        verticalLayout.addComponent(toolbar);
        verticalLayout.addComponent(facilityTable);
        verticalLayout.setExpandRatio(facilityTable, 1);
        verticalLayout.setSizeFull();

    }



    /*private void updateFilters() {
        facilities.setApplyFiltersImmediately(false);
        facilities.removeAllContainerFilters();
        if (departmentFilter != null) {
            // two level hierarchy at max in our demo
            if (departmentFilter.getParent() == null) {
                facilities.addContainerFilter(new Equal("department.parent",
                        departmentFilter));
            } else {
                facilities.addContainerFilter(new Equal("department",
                        departmentFilter));
            }
        }
        if (textFilter != null && !textFilter.equals("")) {
            Or or = new Or(new Like("firstName", textFilter + "%", false),
                    new Like("lastName", textFilter + "%", false));
            facilities.addContainerFilter(or);
        }
        facilities.applyFilters();
    }*/
}

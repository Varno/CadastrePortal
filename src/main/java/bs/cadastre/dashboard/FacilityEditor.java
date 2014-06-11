package bs.cadastre.dashboard;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Arrays;

import bs.cadastre.dashboard.domain.Facility;
import com.vaadin.data.Item;
import com.vaadin.data.validator.BeanValidator;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.DefaultFieldFactory;
import com.vaadin.ui.Field;
import com.vaadin.ui.Form;
import com.vaadin.ui.FormFieldFactory;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;

@SuppressWarnings("serial")
public class FacilityEditor extends Window implements Button.ClickListener,
        FormFieldFactory {

    private final Item facilityItem;
    private Form editorForm;
    private Button saveButton;
    private Button cancelButton;

    public FacilityEditor(Item facilityItem) {
        this.facilityItem = facilityItem;
        editorForm = new Form();
        editorForm.setFormFieldFactory(this);
        editorForm.setBuffered(true);
        editorForm.setImmediate(true);
        editorForm.setItemDataSource(facilityItem, Arrays.asList("cadastrNumber", "area",
                "destination", "location", "permittedUsage", "address"));
        editorForm.getField("cadastrNumber").setCaption("Кадастровый номер");
        editorForm.getField("area").setCaption("Площадь");
        editorForm.getField("destination").setCaption("Назначение");
        editorForm.getField("location").setCaption("Местоположение");
        editorForm.getField("permittedUsage").setCaption("Разрешенное использование");
        editorForm.getField("address").setCaption("Адрес");

        saveButton = new Button("Сохранить", this);
        cancelButton = new Button("Отменить", this);

        editorForm.getFooter().addComponent(saveButton);
        editorForm.getFooter().addComponent(cancelButton);
        setSizeUndefined();
        setContent(editorForm);
    }

    @Override
    public void buttonClick(ClickEvent event) {
        if (event.getButton() == saveButton) {
            editorForm.commit();
            fireEvent(new EditorSavedEvent(this, facilityItem));
        } else if (event.getButton() == cancelButton) {
            editorForm.discard();
        }
        close();
    }

    @Override
    public Field createField(Item item, Object propertyId, Component uiContext) {
        Field field = DefaultFieldFactory.get().createField(item, propertyId,
                uiContext);
        if (field instanceof TextField) {
            ((TextField) field).setNullRepresentation("");
        }

        field.addValidator(new BeanValidator(Facility.class, propertyId
                .toString()));

        return field;
    }

    public void addListener(EditorSavedListener listener) {
        try {
            Method method = EditorSavedListener.class.getDeclaredMethod(
                    "editorSaved", new Class[] { EditorSavedEvent.class });
            addListener(EditorSavedEvent.class, listener, method);
        } catch (final java.lang.NoSuchMethodException e) {
            // This should never happen
            throw new java.lang.RuntimeException(
                    "Ошибка, не найден метод для сохранения");
        }
    }

    public void removeListener(EditorSavedListener listener) {
        removeListener(EditorSavedEvent.class, listener);
    }

    public static class EditorSavedEvent extends Component.Event {

        private Item savedItem;

        public EditorSavedEvent(Component source, Item savedItem) {
            super(source);
            this.savedItem = savedItem;
        }

        public Item getSavedItem() {
            return savedItem;
        }
    }

    public interface EditorSavedListener extends Serializable {
        public void editorSaved(EditorSavedEvent event);
    }
}

/**
 * DISCLAIMER
 * 
 * The quality of the code is such that you should not copy any of it as best
 * practice how to build Vaadin applications.
 * 
 * @author jouni@vaadin.com
 * 
 */

package bs.cadastre.dashboard.data;

import java.util.Calendar;
import java.util.Date;
import java.lang.Math;

import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;

public class FacilityContainer extends IndexedContainer {
    public FacilityContainer() {
        addContainerProperty("CadastrNumber", Integer.class, 0);
        addContainerProperty("Area", Double.class, 1.0);
        addContainerProperty("Destination", String.class, "destination");
        addContainerProperty("Location", String.class, "location");
        addContainerProperty("PermittedUsage", Integer.class, 12);
        addContainerProperty("Address", String.class, "russia, izh city");
    }

    public void addTransaction() {
        Object id = this.addItem();
        Item item = getItem(id);
        if (item != null) {
            item.getItemProperty("CadastrNumber").setValue((int)Math.random());
/*
            item.getItemProperty("Area").setValue(time.getTime());
            item.getItemProperty("Destination").setValue(time.getTime());
            item.getItemProperty("Location").setValue(time.getTime());
            item.getItemProperty("PermittedUsage").setValue(time.getTime());
            item.getItemProperty("Address").setValue(time.getTime());
*/
        }
    }

}
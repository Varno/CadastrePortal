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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;

public class DataProvider {

    public static Random rand = new Random();

    public DataProvider() {
        loadFacilityData();
    }

    private FacilityContainer facilitiesContainer;

    public FacilityContainer getFacilitiesContainer() {
        return facilitiesContainer;
    }

    /** Create a list of dummy facilitiesContainer */
    private void loadFacilityData() {
        facilitiesContainer = new FacilityContainer();

        /* TODO load from repositary*/
        for (int i = 1000; i > 0; i--)
            buildFacilityModel();
        facilitiesContainer.sort(new String[]{"timestamp"}, new boolean[]{true});
    }

    private void buildFacilityModel() {
        facilitiesContainer.addTransaction();
    }
}

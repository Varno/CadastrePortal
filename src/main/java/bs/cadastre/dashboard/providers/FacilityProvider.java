package bs.cadastre.dashboard.providers;

import bs.cadastre.dashboard.domain.Facility;
import org.springframework.stereotype.Repository;

@Repository(value = "facilityProvider")
public class FacilityProvider extends LocalEntityProviderBean<Facility> {

    public FacilityProvider() {
        super(Facility.class);
    }
}

package ru.highloadcup.check;

import org.springframework.stereotype.Component;
import ru.highloadcup.api.LocationEO;

@Component
public class LocationEOCustomChecker implements CustomChecker<LocationEO> {
    @Override
    public void checkFullyNull(LocationEO object) {
        if (object.getCity() == null
                && object.getCountry() == null
                && object.getDistance() == null
                && object.getPlace() == null) {
            throw new IllegalArgumentException("object is fully null");
        }
    }
}

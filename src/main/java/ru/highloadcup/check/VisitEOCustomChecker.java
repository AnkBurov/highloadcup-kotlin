package ru.highloadcup.check;

import org.springframework.stereotype.Component;
import ru.highloadcup.api.VisitEO;

@Component
public class VisitEOCustomChecker implements CustomChecker<VisitEO> {
    @Override
    public void checkFullyNull(VisitEO object) {
        if (object.getLocationId() == null
                && object.getUserId() == null
                && object.getVisitedAt() == null
                && object.getMark() == null) {
            throw new IllegalArgumentException("object is fully null");
        }
    }
}

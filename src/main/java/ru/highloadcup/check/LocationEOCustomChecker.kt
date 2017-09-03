package ru.highloadcup.check

import org.springframework.stereotype.Component
import ru.highloadcup.AllOpen
import ru.highloadcup.api.LocationEO

@AllOpen
@Component
class LocationEOCustomChecker : CustomChecker<LocationEO> {
    override fun checkFullyNull(checkingObject: LocationEO) {
        if (checkingObject.city == null
                && checkingObject.country == null
                && checkingObject.distance == null
                && checkingObject.place == null) {
            throw IllegalArgumentException("checkingObject is fully null")
        }
    }
}

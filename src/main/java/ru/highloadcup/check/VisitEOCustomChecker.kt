package ru.highloadcup.check

import org.springframework.stereotype.Component
import ru.highloadcup.AllOpen
import ru.highloadcup.api.VisitEO

@AllOpen
@Component
class VisitEOCustomChecker : CustomChecker<VisitEO> {
    override fun checkFullyNull(checkingObject: VisitEO) {
        if (checkingObject.locationId == null
                && checkingObject.userId == null
                && checkingObject.visitedAt == null
                && checkingObject.mark == null) {
            throw IllegalArgumentException("checkingObject is fully null")
        }
    }
}

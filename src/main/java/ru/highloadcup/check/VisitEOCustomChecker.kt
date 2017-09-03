package ru.highloadcup.check

import org.springframework.stereotype.Component
import ru.highloadcup.AllOpen
import ru.highloadcup.api.VisitEO

@AllOpen
@Component
class VisitEOCustomChecker : CustomChecker<VisitEO> {
    override fun checkFullyNull(checkingObject: VisitEO) {
        if (checkingObject.location == null
                && checkingObject.user == null
                && checkingObject.visited_at == null
                && checkingObject.mark == null) {
            throw IllegalArgumentException("checkingObject is fully null")
        }
    }
}

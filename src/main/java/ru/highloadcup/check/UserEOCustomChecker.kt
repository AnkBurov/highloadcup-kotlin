package ru.highloadcup.check

import org.springframework.stereotype.Component
import ru.highloadcup.AllOpen
import ru.highloadcup.api.UserEO

@AllOpen
@Component
class UserEOCustomChecker : CustomChecker<UserEO> {
    override fun checkFullyNull(checkingObject: UserEO) {
        if (checkingObject.email == null
                && checkingObject.birthDate == null
                && checkingObject.firstName == null
                && checkingObject.lastName == null
                && checkingObject.gender == null) {
            throw IllegalArgumentException("checkingObject is fully null")
        }
    }
}

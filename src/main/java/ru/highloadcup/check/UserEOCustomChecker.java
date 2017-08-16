package ru.highloadcup.check;

import org.springframework.stereotype.Component;
import ru.highloadcup.api.UserEO;

@Component
public class UserEOCustomChecker implements CustomChecker<UserEO> {
    @Override
    public void checkFullyNull(UserEO object) {
        if (object.getEmail() == null
                && object.getBirthDate() == null
                && object.getFirstName() == null
                && object.getLastName() == null
                && object.getGender() == null) {
            throw new IllegalArgumentException("object is fully null");
        }
    }
}

package ru.highloadcup.dao;

import org.jooq.DSLContext;
import org.jooq.Query;
import org.jooq.Record;
import org.jooq.RecordMapper;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.highloadcup.api.User;
import ru.highloadcup.generated.tables.records.UserRecord;

import javax.transaction.Transactional;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static ru.highloadcup.generated.Tables.USER;

@Component
public class UserDao {

    @Autowired
    private DSLContext dsl;

    @Transactional
    public int createUser(User user) {
        return dsl.insertInto(USER)
                .set(USER.ID, user.getId())
                .set(USER.EMAIL, user.getEmail())
                .set(USER.FIRST_NAME, user.getFirstName())
                .set(USER.LAST_NAME, user.getLastName())
                .set(USER.GENDER, user.getGender().name())
                .set(DSL.field("BIRTH_DATE", Long.class), user.getBirthDate())
                .execute();
    }

    @Transactional
    public void createUsers(Collection<User> users) {
        List<Query> queries = new ArrayList<>();
        for (User user : users) {
            queries.add(dsl.insertInto(USER)
                    .set(USER.ID, user.getId())
                    .set(USER.EMAIL, user.getEmail())
                    .set(USER.FIRST_NAME, user.getFirstName())
                    .set(USER.LAST_NAME, user.getLastName())
                    .set(USER.GENDER, user.getGender().name())
                    .set(DSL.field("BIRTH_DATE", Long.class), user.getBirthDate()));
        }
        dsl.batch(queries).execute();
    }

    @Transactional
    public int updateUser(Integer userId, User user) {
        UserRecord userRecord = dsl.fetchOne(USER, USER.ID.equal(userId));
        if (userRecord == null) {
            return 0;
        }
        if (user.getEmail() != null) {
            userRecord.setEmail(user.getEmail());
        }
        if (user.getFirstName() != null) {
            userRecord.setFirstName(user.getFirstName());
        }
        if (user.getLastName() != null) {
            userRecord.setLastName(user.getLastName());
        }
        if (user.getGender() != null) {
            userRecord.setGender(user.getGender().name());
        }
        if (user.getBirthDate() != null) {
            userRecord.set(DSL.field("BIRTH_DATE", Long.class), user.getBirthDate());
        }
        return userRecord.store();
    }

    public User getUser(Integer userId) {
        return dsl.select(USER.ID,
                USER.EMAIL,
                USER.FIRST_NAME,
                USER.LAST_NAME,
                USER.GENDER,
                USER.BIRTH_DATE)
                .from(USER)
                .where(USER.ID.equal(userId))
                .fetchOne(UserMapper.INSTANCE);
    }

    private static class UserMapper implements RecordMapper<Record, User> {
        private static final UserMapper INSTANCE = new UserMapper();

        @Override
        public User map(Record record) {
            User user = new User();
            user.setId(record.getValue(USER.ID));
            user.setEmail(record.getValue(USER.EMAIL));
            user.setFirstName(record.getValue(USER.FIRST_NAME));
            user.setLastName(record.getValue(USER.LAST_NAME));
            user.setGender(User.Gender.valueOf(record.getValue(USER.GENDER)));
            user.setBirthDate(record.getValue(USER.BIRTH_DATE, Long.class));
            return user;
        }
    }
}

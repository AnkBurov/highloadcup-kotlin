package ru.highloadcup.dao

import org.jooq.DSLContext
import org.jooq.Query
import org.jooq.Record
import org.jooq.RecordMapper
import org.jooq.impl.DSL
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import ru.highloadcup.AllOpen
import ru.highloadcup.api.User
import ru.highloadcup.api.UserEO

import javax.transaction.Transactional


import java.util.ArrayList

import ru.highloadcup.generated.Tables.USER

@AllOpen
@Component
class UserDao {

    @Autowired
    private lateinit var dsl: DSLContext

    @Transactional
    fun createUser(user: User) = dsl.insertInto(USER)
            .set(USER.ID, user.id)
            .set(USER.EMAIL, user.email)
            .set(USER.FIRST_NAME, user.first_name)
            .set(USER.LAST_NAME, user.last_name)
            .set(USER.GENDER, user.gender.name)
            .set(DSL.field("BIRTH_DATE", Long::class.java), user.birthDate)
            .execute()

    @Transactional
    fun createUsers(users: Collection<User>) {
        val queries = users.mapTo(ArrayList<Query>()) { dsl.insertInto(USER)
                    .set(USER.ID, it.id)
                    .set(USER.EMAIL, it.email)
                    .set(USER.FIRST_NAME, it.first_name)
                    .set(USER.LAST_NAME, it.last_name)
                    .set(USER.GENDER, it.gender.name)
                    .set(DSL.field("BIRTH_DATE", Long::class.java), it.birthDate)
        }
        dsl.batch(queries).execute()
    }

    @Transactional
    fun updateUser(userId: Int, user: UserEO): Int {
        val userRecord = dsl.fetchOne(USER, USER.ID.equal(userId)) ?: return 0
        user.email?.let { userRecord.email = it }
        user.firstName?.let { userRecord.firstName = it }
        user.lastName?.let { userRecord.lastName = it }
        user.gender?.let { userRecord.gender = it.name }
        user.birthDate?.let { userRecord.set(DSL.field("BIRTH_DATE", Long::class.java), it) }
        return userRecord.store()
    }

    fun getUser(userId: Int) = dsl.select(USER.ID,
            USER.EMAIL,
            USER.FIRST_NAME,
            USER.LAST_NAME,
            USER.GENDER,
            USER.BIRTH_DATE)
            .from(USER)
            .where(USER.ID.equal(userId))
            .fetchOne(UserMapper)

    private object UserMapper : RecordMapper<Record, User> {
        override fun map(record: Record): User {
            return User(id = record.getValue(USER.ID),
                    email = record.getValue(USER.EMAIL),
                    first_name = record.getValue(USER.FIRST_NAME),
                    last_name = record.getValue(USER.LAST_NAME),
                    gender = User.Gender.valueOf(record.getValue(USER.GENDER)),
                    birthDate = record.getValue(USER.BIRTH_DATE, Long::class.java))
        }
    }
}

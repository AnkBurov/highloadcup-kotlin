package ru.highloadcup.dao

import org.jooq.Condition
import org.jooq.DSLContext
import org.jooq.Query
import org.jooq.Record
import org.jooq.RecordMapper
import org.jooq.impl.DSL
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import ru.highloadcup.AllOpen
import ru.highloadcup.api.UserVisit
import ru.highloadcup.api.Visit
import ru.highloadcup.api.VisitEO

import javax.transaction.Transactional

import java.util.ArrayList

import ru.highloadcup.generated.Tables.LOCATION
import ru.highloadcup.generated.Tables.VISIT

@AllOpen
@Component
class VisitDao {

    @Autowired
    private lateinit var dsl: DSLContext

    @Transactional
    fun createVisit(visit: Visit) = dsl.insertInto(VISIT)
            .set(VISIT.ID, visit.id)
            .set(VISIT.LOCATION_ID, visit.locationId)
            .set(VISIT.USER_ID, visit.userId)
            .set(DSL.field("VISITED_AT", Long::class.java), visit.visitedAt)
            .set(VISIT.MARK, visit.mark)
            .execute()

    @Transactional
    fun createVisits(visits: Collection<Visit>) {
        val queries = visits.mapTo(ArrayList<Query>()) {
            dsl.insertInto(VISIT)
                    .set(VISIT.ID, it.id)
                    .set(VISIT.LOCATION_ID, it.locationId)
                    .set(VISIT.USER_ID, it.userId)
                    .set(DSL.field("VISITED_AT", Long::class.java), it.visitedAt)
                    .set(VISIT.MARK, it.mark)
        }
        dsl.batch(queries).execute()
    }

    @Transactional
    fun updateVisit(visitId: Int, visit: VisitEO): Int {
        val visitRecord = dsl.fetchOne(VISIT, VISIT.ID.equal(visitId)) ?: return 0
        visit.visitedAt?.let { visitRecord.set(DSL.field("VISITED_AT", Long::class.java), it) }
        visit.mark?.let { visitRecord.mark = it }
        visit.locationId?.let { visitRecord.locationId = it }
        visit.userId?.let { visitRecord.userId = it }
        return visitRecord.store()
    }

    fun getVisit(visitId: Int) = dsl.select(VISIT.ID,
            VISIT.LOCATION_ID,
            VISIT.USER_ID,
            VISIT.VISITED_AT,
            VISIT.MARK)
            .from(VISIT)
            .where(VISIT.ID.equal(visitId))
            .fetchOne(VisitMapper)

    fun getUserVisits(userId: Int, fromDate: String?, toDate: String?, country: String?, toDistance: String?): List<UserVisit> {
        val conditions = arrayListOf<Condition>()
        conditions.add(VISIT.USER_ID.equal(userId))
        fromDate?.let { conditions.add(VISIT.VISITED_AT.greaterThan(Integer.valueOf(it))) }
        toDate?.let { conditions.add(VISIT.VISITED_AT.lessThan(Integer.valueOf(it))) }
        country?.let { conditions.add(LOCATION.COUNTRY.equal(it)) }
        toDistance?.let { LOCATION.DISTANCE.lessThan(Integer.valueOf(toDistance)) }
        return getUserVisits(conditions)
    }

    private fun getUserVisits(conditions: Collection<Condition>) = dsl.select(VISIT.MARK,
            VISIT.VISITED_AT,
            LOCATION.PLACE)
            .from(VISIT)
            .join(LOCATION).on(VISIT.LOCATION_ID.equal(LOCATION.ID))
            .where(conditions)
            .orderBy(VISIT.VISITED_AT.asc())
            .fetch(UserVisitMapper)

    private object VisitMapper: RecordMapper<Record, Visit> {
        override fun map(record: Record): Visit {
            val visit = Visit()
            visit.id = record.getValue(VISIT.ID)
            visit.locationId = record.getValue(VISIT.LOCATION_ID)
            visit.userId = record.getValue(VISIT.USER_ID)
            visit.visitedAt = record.getValue(VISIT.VISITED_AT, Long::class.java)
            visit.mark = record.getValue(VISIT.MARK)
            return visit
        }
    }

    private object UserVisitMapper : RecordMapper<Record, UserVisit> {
        override fun map(record: Record): UserVisit {
            val userVisit = UserVisit()
            userVisit.mark = record.getValue(VISIT.MARK)
            userVisit.visitedAt = record.getValue(VISIT.VISITED_AT, Long::class.java)
            userVisit.place = record.getValue(LOCATION.PLACE)
            return userVisit
        }
    }
}

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
import ru.highloadcup.api.Location
import ru.highloadcup.api.LocationEO
import ru.highloadcup.api.User
import ru.highloadcup.generated.tables.records.LocationRecord

import javax.transaction.Transactional

import java.math.BigDecimal
import java.util.ArrayList

import ru.highloadcup.generated.Tables.LOCATION
import ru.highloadcup.generated.Tables.USER
import ru.highloadcup.generated.Tables.VISIT

@AllOpen
@Component
class LocationDao {

    @Autowired
    private lateinit var dsl: DSLContext

    @Transactional
    fun createLocation(location: Location) = dsl
            .insertInto(LOCATION)
            .set(LOCATION.ID, location.id)
            .set(LOCATION.PLACE, location.place)
            .set(LOCATION.COUNTRY, location.country)
            .set(LOCATION.CITY, location.city)
            .set(LOCATION.DISTANCE, location.distance)
            .execute()

    @Transactional
    fun createLocations(locations: Collection<Location>) {
        val queries = locations.mapTo(ArrayList<Query>()) {
            dsl.insertInto(LOCATION)
                    .set(LOCATION.ID, it.id)
                    .set(LOCATION.PLACE, it.place)
                    .set(LOCATION.COUNTRY, it.country)
                    .set(LOCATION.CITY, it.city)
                    .set(LOCATION.DISTANCE, it.distance)
        }
        dsl.batch(queries).execute()
    }

    @Transactional
    fun updateLocation(locationId: Int, location: LocationEO): Int {
        val locationRecord = dsl.fetchOne(LOCATION, LOCATION.ID.equal(locationId)) ?: return 0
        location.place?.let { locationRecord.place = it }
        location.country?.let { locationRecord.country = it }
        location.city?.let { locationRecord.city = it }
        location.distance?.let { locationRecord.distance = it }
        return locationRecord.store()
    }

    fun getLocation(locationId: Int) = dsl
            .select(LOCATION.ID,
                    LOCATION.PLACE,
                    LOCATION.COUNTRY,
                    LOCATION.CITY,
                    LOCATION.DISTANCE)
            .from(LOCATION)
            .where(LOCATION.ID.equal(locationId))
            .fetchOne(LocationMapper)

    fun getAverageVisitMark(locationId: Int, fromDate: String?, toDate: String?,
                            fromAge: String?, toAge: String?, gender: String?): BigDecimal {
        val conditions = arrayListOf<Condition>()
        conditions.add(VISIT.LOCATION_ID.equal(locationId))
        fromDate?.let { conditions.add(VISIT.VISITED_AT.greaterThan(Integer.valueOf(it))) }
        toDate?.let { conditions.add(VISIT.VISITED_AT.lessThan(Integer.valueOf(it))) }
        gender?.let { conditions.add(USER.GENDER.equal(User.Gender.valueOf(gender).name)) }

        var joinCondition = DSL.trueCondition()
        if (fromAge != null) {
            joinCondition = joinCondition.and(
                    "SELECT (JULIANDAY('NOW') - (JULIANDAY(USER.BIRTH_DATE, 'unixepoch'))) / 365.25 > $fromAge")
        }
        if (toAge != null) {
            joinCondition = joinCondition.and(
                    "SELECT (JULIANDAY('NOW') - (JULIANDAY(USER.BIRTH_DATE, 'unixepoch'))) / 365.25 < $toAge")
        }

        return getAverageVisitMark(conditions, joinCondition)
    }

    private fun getAverageVisitMark(conditions: Collection<Condition>, joinCondition: Condition) = dsl
            .select(DSL.avg(VISIT.MARK))
            .from(VISIT)
            .join(USER).on(VISIT.USER_ID.equal(USER.ID).and(joinCondition))
            .where(conditions)
            .fetchOneInto(BigDecimal::class.java)

    private object LocationMapper : RecordMapper<Record, Location> {
        override fun map(record: Record) = Location(
                id = record.getValue(LOCATION.ID),
                place = record.getValue(LOCATION.PLACE),
                country = record.getValue(LOCATION.COUNTRY),
                city = record.getValue(LOCATION.CITY),
                distance = record.getValue(LOCATION.DISTANCE))
    }
}

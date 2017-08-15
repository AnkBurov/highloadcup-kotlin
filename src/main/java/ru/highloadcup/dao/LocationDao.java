package ru.highloadcup.dao;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Query;
import org.jooq.Record;
import org.jooq.RecordMapper;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.highloadcup.api.Location;
import ru.highloadcup.api.User;
import ru.highloadcup.generated.tables.records.LocationRecord;

import javax.transaction.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static ru.highloadcup.generated.Tables.LOCATION;
import static ru.highloadcup.generated.Tables.USER;
import static ru.highloadcup.generated.Tables.VISIT;

@Component
public class LocationDao {

    @Autowired
    private DSLContext dsl;

    @Transactional
    public int createLocation(Location location) {
        return dsl.insertInto(LOCATION)
                .set(LOCATION.ID, location.getId())
                .set(LOCATION.PLACE, location.getPlace())
                .set(LOCATION.COUNTRY, location.getCountry())
                .set(LOCATION.CITY, location.getCity())
                .set(LOCATION.DISTANCE, location.getDistance())
                .execute();
    }

    @Transactional
    public void createLocations(Collection<Location> locations) {
        List<Query> queries = new ArrayList<>();
        for (Location location : locations) {
            queries.add(dsl.insertInto(LOCATION)
                    .set(LOCATION.ID, location.getId())
                    .set(LOCATION.PLACE, location.getPlace())
                    .set(LOCATION.COUNTRY, location.getCountry())
                    .set(LOCATION.CITY, location.getCity())
                    .set(LOCATION.DISTANCE, location.getDistance()));
        }
        dsl.batch(queries).execute();
    }

    @Transactional
    public int updateLocation(Integer locationId, Location location) {
        LocationRecord locationRecord = dsl.fetchOne(LOCATION, LOCATION.ID.equal(locationId));
        if (locationRecord == null) {
            return 0;
        }
        if (location.getPlace() != null) {
            locationRecord.setPlace(location.getPlace());
        }
        if (location.getCountry() != null) {
            locationRecord.setCountry(location.getCountry());
        }
        if (location.getCity() != null) {
            locationRecord.setCity(location.getCity());
        }
        if (location.getDistance() != null) {
            locationRecord.setDistance(location.getDistance());
        }
        return locationRecord.store();
    }

    public Location getLocation(Integer locationId) {
        return dsl.select(LOCATION.ID,
                LOCATION.PLACE,
                LOCATION.COUNTRY,
                LOCATION.CITY,
                LOCATION.DISTANCE)
                .from(LOCATION)
                .where(LOCATION.ID.equal(locationId))
                .fetchOne(LocationMapper.INSTANCE);
    }

    public BigDecimal getAverageVisitMark(Integer locationId, String fromDate, String toDate,
                                          String fromAge, String toAge, String gender) {
        Condition condition = VISIT.LOCATION_ID.equal(locationId);
        if (fromDate != null) {
            condition = condition.and(VISIT.VISITED_AT.greaterThan(new Timestamp(Long.valueOf(fromDate))));
        }
        if (toDate != null) {
            condition = condition.and(VISIT.VISITED_AT.lessThan(new Timestamp(Long.valueOf(toDate))));
        }
        if (gender != null) {
            condition = condition.and(USER.GENDER.equal(User.Gender.valueOf(gender).name()));
        }

        Condition joinCondition = DSL.trueCondition();
        if (fromAge != null) {
            joinCondition = joinCondition.and(
                    "SELECT (JULIANDAY('NOW') - (JULIANDAY(USER.BIRTH_DATE))) / 365.25 > " + fromAge);
        }
        if (toAge != null) {
            joinCondition = joinCondition.and(
                    "SELECT (JULIANDAY('NOW') - (JULIANDAY(USER.BIRTH_DATE))) / 365.25 < " + toAge);
        }

        return getAverageVisitMark(condition, joinCondition);
    }

    private BigDecimal getAverageVisitMark(Condition condition, Condition joinCondition) {
        return dsl.select(DSL.avg(VISIT.MARK))
                .from(VISIT)
                .join(USER).on(VISIT.USER_ID.equal(USER.ID).and(joinCondition))
                .where(condition)
                .fetchOneInto(BigDecimal.class);
    }

    private static class LocationMapper implements RecordMapper<Record, Location> {
        private static final LocationMapper INSTANCE = new LocationMapper();

        @Override
        public Location map(Record record) {
            Location location = new Location();
            location.setId(record.getValue(LOCATION.ID));
            location.setPlace(record.getValue(LOCATION.PLACE));
            location.setCountry(record.getValue(LOCATION.COUNTRY));
            location.setCity(record.getValue(LOCATION.CITY));
            location.setDistance(record.getValue(LOCATION.DISTANCE));
            return location;
        }
    }
}

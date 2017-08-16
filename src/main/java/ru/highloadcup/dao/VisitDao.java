package ru.highloadcup.dao;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Query;
import org.jooq.Record;
import org.jooq.RecordMapper;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.highloadcup.api.UserVisit;
import ru.highloadcup.api.Visit;
import ru.highloadcup.api.VisitEO;
import ru.highloadcup.generated.tables.records.VisitRecord;

import javax.transaction.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static ru.highloadcup.generated.Tables.LOCATION;
import static ru.highloadcup.generated.Tables.VISIT;

@Component
public class VisitDao {

    @Autowired
    private DSLContext dsl;

    @Transactional
    public int createVisit(Visit visit) {
        return dsl.insertInto(VISIT)
                .set(VISIT.ID, visit.getId())
                .set(VISIT.LOCATION_ID, visit.getLocationId())
                .set(VISIT.USER_ID, visit.getUserId())
                .set(DSL.field("VISITED_AT", Long.class), visit.getVisitedAt())
                .set(VISIT.MARK, visit.getMark())
                .execute();
    }

    @Transactional
    public void createVisits(Collection<Visit> visits) {
        List<Query> queries = new ArrayList<>();
        for (Visit visit : visits) {
            queries.add(dsl.insertInto(VISIT)
                    .set(VISIT.ID, visit.getId())
                    .set(VISIT.LOCATION_ID, visit.getLocationId())
                    .set(VISIT.USER_ID, visit.getUserId())
                    .set(DSL.field("VISITED_AT", Long.class), visit.getVisitedAt())
                    .set(VISIT.MARK, visit.getMark()));
        }
        dsl.batch(queries).execute();
    }

    @Transactional
    public int updateVisit(Integer visitId, VisitEO visit) {
        VisitRecord visitRecord = dsl.fetchOne(VISIT, VISIT.ID.equal(visitId));
        if (visitRecord == null) {
            return 0;
        }
        if (visit.getVisitedAt() != null) {
            visitRecord.set(DSL.field("VISITED_AT", Long.class), visit.getVisitedAt());
        }
        if (visit.getMark() != null) {
            visitRecord.setMark(visit.getMark());
        }
        if (visit.getLocationId() != null) {
            visitRecord.setLocationId(visit.getLocationId());
        }
        if (visit.getUserId() != null) {
            visitRecord.setUserId(visit.getUserId());
        }
        return visitRecord.store();
    }

    public Visit getVisit(Integer visitId) {
        return dsl.select(VISIT.ID,
                VISIT.LOCATION_ID,
                VISIT.USER_ID,
                VISIT.VISITED_AT,
                VISIT.MARK)
                .from(VISIT)
                .where(VISIT.ID.equal(visitId))
                .fetchOne(VisitMapper.INSTANCE);
    }

    public List<UserVisit> getUserVisits(Integer userId, String fromDate, String toDate, String country, String toDistance) {
        Condition condition = VISIT.USER_ID.equal(userId);
        if (fromDate != null) {
            condition = condition.and(VISIT.VISITED_AT.greaterThan(Integer.valueOf(fromDate)));
        }
        if (toDate != null) {
            condition = condition.and(VISIT.VISITED_AT.lessThan(Integer.valueOf(toDate)));
        }
        if (country != null) {
            condition = condition.and(LOCATION.COUNTRY.equal(country));
        }
        if (toDistance != null) {
            condition = condition.and(LOCATION.DISTANCE.lessThan(Integer.valueOf(toDistance)));
        }
        return getUserVisits(condition);
    }

    private List<UserVisit> getUserVisits(Condition condition) {
        return dsl.select(VISIT.MARK,
                VISIT.VISITED_AT,
                LOCATION.PLACE)
                .from(VISIT)
                .join(LOCATION).on(VISIT.LOCATION_ID.equal(LOCATION.ID))
                .where(condition)
                .orderBy(VISIT.VISITED_AT.asc())
                .fetch(UserVisitMapper.INSTANCE);
    }

    private static class VisitMapper implements RecordMapper<Record, Visit> {

        private static final VisitMapper INSTANCE = new VisitMapper();

        @Override
        public Visit map(Record record) {
            Visit visit = new Visit();
            visit.setId(record.getValue(VISIT.ID));
            visit.setLocationId(record.getValue(VISIT.LOCATION_ID));
            visit.setUserId(record.getValue(VISIT.USER_ID));
            visit.setVisitedAt(record.getValue(VISIT.VISITED_AT, Long.class));
            visit.setMark(record.getValue(VISIT.MARK));
            return visit;
        }
    }

    private static class UserVisitMapper implements RecordMapper<Record, UserVisit> {

        private static final UserVisitMapper INSTANCE = new UserVisitMapper();

        @Override
        public UserVisit map(Record record) {
            UserVisit userVisit = new UserVisit();
            userVisit.setMark(record.getValue(VISIT.MARK));
            userVisit.setVisitedAt(record.getValue(VISIT.VISITED_AT, Long.class));
            userVisit.setPlace(record.getValue(LOCATION.PLACE));
            return userVisit;
        }
    }
}

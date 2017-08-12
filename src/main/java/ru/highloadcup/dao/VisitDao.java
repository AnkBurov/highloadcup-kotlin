package ru.highloadcup.dao;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.RecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.highloadcup.api.Visit;
import ru.highloadcup.generated.tables.records.VisitRecord;

import javax.transaction.Transactional;

import java.sql.Timestamp;
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
                .set(VISIT.VISITED_AT, visit.getVisitedAt())
                .set(VISIT.MARK, visit.getMark())
                .execute();
    }

    @Transactional
    public int updateVisit(Integer visitId, Visit visit) {
        VisitRecord visitRecord = dsl.fetchOne(VISIT, VISIT.ID.equal(visitId));
        if (visitRecord == null) {
            return 0;
        }
        if (visit.getVisitedAt() != null) {
            visitRecord.setVisitedAt(visit.getVisitedAt());
        }
        if (visit.getMark() != null) {
            visitRecord.setMark(visit.getMark());
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

    public List<Visit> getVisits(Integer userId, Timestamp fromDate, Timestamp toDate, String country, Integer toDistance) {
        Condition condition = VISIT.USER_ID.equal(userId);
        if (fromDate != null) {
            condition = condition.and(VISIT.VISITED_AT.greaterThan(fromDate));
        }
        if (toDate != null) {
            condition = condition.and(VISIT.VISITED_AT.lessThan(toDate));
        }
        if (country != null) {
            condition = condition.and(LOCATION.COUNTRY.equal(country));
        }
        if (toDistance != null) {
            condition = condition.and(LOCATION.DISTANCE.lessThan(toDistance));
        }
        return getVisits(condition);
    }

    private List<Visit> getVisits(Condition condition) {
        return dsl.select(VISIT.ID,
                VISIT.LOCATION_ID,
                VISIT.USER_ID,
                VISIT.VISITED_AT,
                VISIT.MARK)
                .from(VISIT)
                .join(LOCATION).on(VISIT.LOCATION_ID.equal(LOCATION.ID))
                .where(condition)
                .orderBy(VISIT.VISITED_AT.asc())
                .fetch(VisitMapper.INSTANCE);
    }

    private static class VisitMapper implements RecordMapper<Record, Visit> {

        private static final VisitMapper INSTANCE = new VisitMapper();

        @Override
        public Visit map(Record record) {
            Visit visit = new Visit();
            visit.setId(record.getValue(VISIT.ID));
            visit.setLocationId(record.getValue(VISIT.LOCATION_ID));
            visit.setUserId(record.getValue(VISIT.USER_ID));
            visit.setVisitedAt(record.getValue(VISIT.VISITED_AT));
            visit.setMark(record.getValue(VISIT.MARK));
            return visit;
        }
    }

}

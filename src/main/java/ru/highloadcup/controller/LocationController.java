package ru.highloadcup.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.highloadcup.api.AverageVisitMarkDto;
import ru.highloadcup.api.EmptyJson;
import ru.highloadcup.api.Location;
import ru.highloadcup.api.User;
import ru.highloadcup.dao.LocationDao;

import java.math.BigDecimal;
import java.sql.Timestamp;

import static ru.highloadcup.controller.LocationController.REST_PATH;
import static ru.highloadcup.util.HttpUtil.BY_ID;
import static ru.highloadcup.util.HttpUtil.JSON_HEADERS;
import static ru.highloadcup.util.HttpUtil.NEW;
import static ru.highloadcup.util.HttpUtil.NOT_FOUND;
import static ru.highloadcup.util.HttpUtil.OK;

@RestController
@RequestMapping(REST_PATH)
public class LocationController {

    public static final String REST_PATH = "/locations";
    public static final String AVG = "/avg";

    @Autowired
    private LocationDao locationDao;

    @RequestMapping(value = NEW, method = RequestMethod.POST)
    public ResponseEntity<EmptyJson> createLocation(@RequestBody Location location) {
        locationDao.createLocation(location);
        return OK;
    }

    @RequestMapping(value = BY_ID, method = RequestMethod.POST)
    public ResponseEntity<EmptyJson> updateLocation(@PathVariable Integer id, @RequestBody Location location) {
        int numberOfUpdatedRecords = locationDao.updateLocation(id, location);
        if (numberOfUpdatedRecords == 0) {
            return NOT_FOUND;
        }
        return OK;
    }

    @RequestMapping(value = BY_ID, method = RequestMethod.GET)
    public ResponseEntity<Location> getLocation(@PathVariable Integer id) {
        Location location = locationDao.getLocation(id);
        if (location == null) {
            return NOT_FOUND;
        }
        return new ResponseEntity<>(location, JSON_HEADERS, HttpStatus.OK);
    }

    @RequestMapping(value = BY_ID + AVG, method = RequestMethod.GET)
    public ResponseEntity<AverageVisitMarkDto> getAverageVisitMark(@PathVariable Integer id,
                                                                   @RequestParam(required = false) Timestamp fromDate,
                                                                   @RequestParam(required = false) Timestamp toDate,
                                                                   @RequestParam(required = false) Integer fromAge,
                                                                   @RequestParam(required = false) Integer toAge,
                                                                   @RequestParam(required = false) User.Gender gender) {
        Location location = locationDao.getLocation(id);
        if (location == null) {
            return NOT_FOUND;
        }
        BigDecimal averageVisitMark = locationDao.getAverageVisitMark(id, fromDate, toDate, fromAge, toAge, gender);
        if (averageVisitMark == null) {
            averageVisitMark = BigDecimal.ZERO;
        }
        return new ResponseEntity<>(new AverageVisitMarkDto(averageVisitMark), JSON_HEADERS, HttpStatus.OK);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity handleAllExceptions(Exception e) {
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }
}

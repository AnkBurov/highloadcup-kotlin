package ru.highloadcup.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.highloadcup.api.AverageVisitMarkDto;
import ru.highloadcup.api.EmptyJson;
import ru.highloadcup.api.Location;
import ru.highloadcup.dao.LocationDao;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import static ru.highloadcup.controller.LocationController.REST_PATH;
import static ru.highloadcup.util.HttpUtil.BY_ID;
import static ru.highloadcup.util.HttpUtil.NEW;
import static ru.highloadcup.util.HttpUtil.createResponse;

@RestController
@RequestMapping(REST_PATH)
public class LocationController {

    public static final String REST_PATH = "/locations";
    public static final String AVG = "/avg";

    @Autowired
    private LocationDao locationDao;

    @RequestMapping(value = NEW, method = RequestMethod.POST)
    public void createLocation(@RequestBody Location location, HttpServletResponse response) throws IOException {
        locationDao.createLocation(location);
        createResponse(EmptyJson.INSTANCE, HttpStatus.OK, response);
    }

    @RequestMapping(value = BY_ID, method = RequestMethod.POST)
    public void updateLocation(@PathVariable Integer id, @RequestBody Location location, HttpServletResponse response) throws IOException {
        int numberOfUpdatedRecords = locationDao.updateLocation(id, location);
        if (numberOfUpdatedRecords == 0) {
            createResponse(HttpStatus.NOT_FOUND, response);
        }
        createResponse(EmptyJson.INSTANCE, HttpStatus.OK, response);
    }

    @RequestMapping(value = BY_ID, method = RequestMethod.GET)
    public void getLocation(@PathVariable Integer id, HttpServletResponse response) throws IOException {
        Location location = locationDao.getLocation(id);
        if (location == null) {
            createResponse(HttpStatus.NOT_FOUND, response);
        }
        createResponse(location, HttpStatus.OK, response);
    }

    @RequestMapping(value = BY_ID + AVG, method = RequestMethod.GET)
    public void getAverageVisitMark(@PathVariable Integer id,
                                    HttpServletResponse response,
                                    @RequestParam(required = false) String fromDate,
                                    @RequestParam(required = false) String toDate,
                                    @RequestParam(required = false) String fromAge,
                                    @RequestParam(required = false) String toAge,
                                    @RequestParam(required = false) String gender) throws IOException {
        Location location = locationDao.getLocation(id);
        if (location == null) {
            createResponse(HttpStatus.NOT_FOUND, response);
        }
        BigDecimal averageVisitMark = locationDao.getAverageVisitMark(id, fromDate, toDate, fromAge, toAge, gender);
        if (averageVisitMark == null) {
            averageVisitMark = BigDecimal.ZERO;
        }
        BigDecimal rounded = averageVisitMark.round(new MathContext(6, RoundingMode.HALF_EVEN));
        createResponse(new AverageVisitMarkDto(rounded), HttpStatus.OK, response);
    }

    @ExceptionHandler(Exception.class)
    public void handleAllExceptions(Exception e, HttpServletResponse response) throws IOException {
        if (e instanceof MethodArgumentTypeMismatchException) {
            createResponse(HttpStatus.NOT_FOUND, response);
        }
        createResponse(HttpStatus.BAD_REQUEST, response);
    }
}

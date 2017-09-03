package ru.highloadcup.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import ru.highloadcup.api.AverageVisitMarkDto
import ru.highloadcup.api.EmptyJson
import ru.highloadcup.api.Location
import ru.highloadcup.api.LocationEO
import ru.highloadcup.check.CustomChecker
import ru.highloadcup.controller.LocationController.Companion.REST_PATH
import ru.highloadcup.dao.LocationDao

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.validation.Valid
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

import ru.highloadcup.util.HttpUtil.BY_ID
import ru.highloadcup.util.HttpUtil.NEW
import ru.highloadcup.util.HttpUtil.createResponse
import ru.highloadcup.util.doAsync

@RestController
@RequestMapping(REST_PATH)
class LocationController {

    @Autowired
    private lateinit var locationDao: LocationDao

    @Autowired
    private lateinit var customChecker: CustomChecker<LocationEO>

    @RequestMapping(value = NEW, method = arrayOf(RequestMethod.POST))
    fun createLocation(@RequestBody @Valid location: Location, request: HttpServletRequest) {
        request.startAsync().doAsync { response ->
            locationDao.createLocation(location)
            createResponse(EmptyJson, response)
        }
    }

    @RequestMapping(value = BY_ID, method = arrayOf(RequestMethod.POST))
    fun updateLocation(@PathVariable id: Int, @RequestBody @Valid location: LocationEO,
                       request: HttpServletRequest) {
        request.startAsync().doAsync { response ->
            customChecker.checkFullyNull(location)
            val numberOfUpdatedRecords = locationDao.updateLocation(id, location)
            if (numberOfUpdatedRecords == 0) createResponse(HttpStatus.NOT_FOUND, response)
            else createResponse(EmptyJson, response)
        }
    }

    @RequestMapping(value = BY_ID, method = arrayOf(RequestMethod.GET))
    fun getLocation(@PathVariable id: Int, request: HttpServletRequest) {
        request.startAsync().doAsync { response ->
            val location = locationDao.getLocation(id)
            createResponse(location, response)
        }
    }

    @RequestMapping(value = BY_ID + AVG, method = arrayOf(RequestMethod.GET))
    fun getAverageVisitMark(@PathVariable id: Int,
                            request: HttpServletRequest,
                            @RequestParam(required = false) fromDate: String?,
                            @RequestParam(required = false) toDate: String?,
                            @RequestParam(required = false) fromAge: String?,
                            @RequestParam(required = false) toAge: String?,
                            @RequestParam(required = false) gender: String?) {
        request.startAsync().doAsync { response ->
            val location = locationDao.getLocation(id)
            if (location == null) {
                createResponse(HttpStatus.NOT_FOUND, response)
            } else {
                val averageVisitMark: BigDecimal = locationDao.getAverageVisitMark(id, fromDate, toDate, fromAge, toAge, gender)
                val rounded = averageVisitMark.round(MathContext(6, RoundingMode.HALF_EVEN))
                createResponse(AverageVisitMarkDto(rounded), response)
            }
        }
    }

    @ExceptionHandler(Exception::class)
    fun handleAllExceptions(e: Exception, response: HttpServletResponse) {
        e.printStackTrace()
        if (e is MethodArgumentTypeMismatchException) createResponse(HttpStatus.NOT_FOUND, response)
        else createResponse(HttpStatus.BAD_REQUEST, response)
    }

    companion object {
        const val REST_PATH = "/locations"
        const val AVG = "/avg"
    }
}

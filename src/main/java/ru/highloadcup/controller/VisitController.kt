package ru.highloadcup.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import ru.highloadcup.api.EmptyJson
import ru.highloadcup.api.Visit
import ru.highloadcup.api.VisitEO
import ru.highloadcup.check.CustomChecker
import ru.highloadcup.controller.VisitController.Companion.REST_PATH
import ru.highloadcup.dao.VisitDao

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.validation.Valid

import ru.highloadcup.util.HttpUtil.BY_ID
import ru.highloadcup.util.HttpUtil.NEW
import ru.highloadcup.util.HttpUtil.createResponse
import ru.highloadcup.util.doAsync

@RestController
@RequestMapping(REST_PATH)
class VisitController {

    @Autowired
    private lateinit var visitDao: VisitDao

    @Autowired
    private lateinit var customChecker: CustomChecker<VisitEO>

    @RequestMapping(value = NEW, method = arrayOf(RequestMethod.POST))
    fun createVisit(@RequestBody @Valid visit: Visit, request: HttpServletRequest) {
        request.startAsync().doAsync { response ->
            visitDao.createVisit(visit)
            createResponse(EmptyJson, response)
        }
    }

    @RequestMapping(value = BY_ID, method = arrayOf(RequestMethod.POST))
    fun updateVisit(@PathVariable id: Int, @RequestBody @Valid visit: VisitEO, request: HttpServletRequest) {
        request.startAsync().doAsync { response ->
            customChecker.checkFullyNull(visit)
            val numberOfUpdatedRecords = visitDao.updateVisit(id, visit)
            if (numberOfUpdatedRecords == 0) createResponse(HttpStatus.NOT_FOUND, response)
            else createResponse(EmptyJson, response)
        }
    }

    @RequestMapping(value = BY_ID, method = arrayOf(RequestMethod.GET))
    fun getVisit(@PathVariable id: Int, request: HttpServletRequest) {
        request.startAsync().doAsync { response ->
            val visit = visitDao.getVisit(id)
            createResponse(visit, response)
        }
    }

    @ExceptionHandler(Exception::class)
    fun handleAllExceptions(e: Exception, response: HttpServletResponse) {
        e.printStackTrace()
        if (e is MethodArgumentTypeMismatchException) createResponse(HttpStatus.NOT_FOUND, response)
        else createResponse(HttpStatus.BAD_REQUEST, response)
    }

    companion object {
        const val REST_PATH = "/visits"
    }
}

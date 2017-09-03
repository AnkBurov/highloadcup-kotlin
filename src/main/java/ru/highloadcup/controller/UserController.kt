package ru.highloadcup.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import ru.highloadcup.api.*
import ru.highloadcup.check.CustomChecker
import ru.highloadcup.controller.UserController.Companion.REST_PATH
import ru.highloadcup.dao.UserDao
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
class UserController {

    @Autowired
    private lateinit var userDao: UserDao

    @Autowired
    private lateinit var visitDao: VisitDao

    @Autowired
    @Qualifier("userEOCustomChecker")
    private lateinit var customChecker: CustomChecker<UserEO>

    @RequestMapping(value = NEW, method = arrayOf(RequestMethod.POST))
    fun createUser(@RequestBody @Valid user: User, request: HttpServletRequest) {
        request.startAsync().doAsync { response ->
            userDao.createUser(user)
            createResponse(EmptyJson, response)
        }
    }

    @RequestMapping(value = BY_ID, method = arrayOf(RequestMethod.POST))
    fun updateUser(@PathVariable id: Int, @RequestBody @Valid user: UserEO, request: HttpServletRequest) {
        request.startAsync().doAsync { response ->
            customChecker.checkFullyNull(user)
            val numberOfUpdatedRecords = userDao.updateUser(id, user)
            if (numberOfUpdatedRecords == 0) createResponse(HttpStatus.NOT_FOUND, response)
            else createResponse(EmptyJson, response)
        }
    }

    @RequestMapping(value = BY_ID, method = arrayOf(RequestMethod.GET))
    fun getUser(@PathVariable id: Int, request: HttpServletRequest) {
        request.startAsync().doAsync { response ->
            val user = userDao.getUser(id)
            createResponse(user, response)
        }
    }

    @RequestMapping(value = BY_ID + VisitController.REST_PATH, method = arrayOf(RequestMethod.GET))
    fun getUserVisits(@PathVariable id: Int,
                      request: HttpServletRequest,
                      @RequestParam(required = false) fromDate: String?,
                      @RequestParam(required = false) toDate: String?,
                      @RequestParam(required = false) country: String?,
                      @RequestParam(required = false) toDistance: String?) {
        request.startAsync().doAsync { response ->
            val user = userDao.getUser(id)
            if (user == null) {
                createResponse(HttpStatus.NOT_FOUND, response)
            } else {
                val userVisits = visitDao.getUserVisits(id, fromDate, toDate, country, toDistance)
                createResponse(UserVisitsDto(userVisits), response)
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
        const val REST_PATH = "/users"
    }
}

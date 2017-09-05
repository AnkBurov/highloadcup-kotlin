package ru.highloadcup

import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus
import org.springframework.test.context.junit4.SpringRunner
import ru.highloadcup.api.EmptyJson
import ru.highloadcup.api.Visit

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import ru.highloadcup.controller.VisitController.Companion.REST_PATH
import ru.highloadcup.util.HttpUtil.BY_ID
import ru.highloadcup.util.HttpUtil.NEW

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class VisitIntegrationTest {

    @Autowired
    private lateinit var restTemplate: TestRestTemplate

    @Test
    fun createVisit() {
        val (userId) = UserIntegrationTest.createUserInternal(restTemplate, 1000000, "1236@gmail.com")
        val (locationId) = LocationIntegrationTest.createLocationInternal(restTemplate, 1000000)
        createVisitInternal(restTemplate, 1000000, userId, locationId)
    }

    @Test
    fun updateVisit() {
        val (userId) = UserIntegrationTest.createUserInternal(restTemplate, 1000001, "127@gmail.com")
        val (locationId) = LocationIntegrationTest.createLocationInternal(restTemplate, 1000001)
        val visit = createVisitInternal(restTemplate, 1000001, userId, locationId)
        visit.mark = 4

        updateVisitInternal(restTemplate, visit)
    }

    @Test
    fun getVisit() {
        val (userId) = UserIntegrationTest.createUserInternal(restTemplate, 1000002, "1239@gmail.com")
        val (locationId) = LocationIntegrationTest.createLocationInternal(restTemplate, 1000002)
        val visit = createVisitInternal(restTemplate, 1000002, userId, locationId)

        val response = restTemplate.getForEntity(REST_PATH + BY_ID, Visit::class.java, visit.id)
        assertEquals(HttpStatus.OK, response.statusCode)
        assertNotNull(response.body)
        assertEquals(visit, response.body)
    }

    companion object {

        fun createVisitInternal(restTemplate: TestRestTemplate, id: Int, locationId: Int, userId: Int): Visit {
            val visit = Visit(id, locationId, userId, 946684900L, 5)

            val response = restTemplate.postForEntity(REST_PATH + NEW, visit, EmptyJson::class.java)
            assertEquals(HttpStatus.OK, response.statusCode)
            assertNotNull(response.body)
            return visit
        }

        fun updateVisitInternal(restTemplate: TestRestTemplate, visit: Visit) {
            val response = restTemplate.postForEntity(REST_PATH + BY_ID, visit, EmptyJson::class.java, visit.id)
            assertEquals(HttpStatus.OK, response.statusCode)
            assertNotNull(response.body)
        }
    }
}

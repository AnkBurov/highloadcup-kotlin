package ru.highloadcup

import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus
import org.springframework.test.context.junit4.SpringRunner
import ru.highloadcup.api.AverageVisitMarkDto
import ru.highloadcup.api.EmptyJson
import ru.highloadcup.api.Location

import java.util.HashMap

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.springframework.beans.factory.annotation.Autowired
import ru.highloadcup.controller.LocationController.Companion.AVG
import ru.highloadcup.controller.LocationController.Companion.REST_PATH
import ru.highloadcup.util.HttpUtil.BY_ID
import ru.highloadcup.util.HttpUtil.NEW

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LocationIntegrationTest {

    @Autowired
    private lateinit var restTemplate: TestRestTemplate

    @Test
    fun createLocation() {
        createLocationInternal(restTemplate, 600000)
    }

    @Test
    fun updateLocation() {
        val location = createLocationInternal(restTemplate, 600001)
        val copiedLocation = location.copy(location.id, location.place, location.country, "Liverpool", location.distance)
        updateLocationInternal(restTemplate, copiedLocation)
    }

    @Test
    fun getLocation() {
        val location = createLocationInternal(restTemplate, 600002)
        val copiedLocation = location.copy(location.id, location.place, location.country, "Liverpool", location.distance)
        updateLocationInternal(restTemplate, copiedLocation)

        val response = restTemplate.getForEntity(REST_PATH + BY_ID, Location::class.java, copiedLocation.id)
        assertEquals(HttpStatus.OK, response.statusCode)
        assertNotNull(response.body)
        assertEquals(copiedLocation, response.body)
    }

    @Test
    fun getAverageVisitMark() {
        val (userId) = UserIntegrationTest.createUserInternal(restTemplate, 600003, "12399@gmail.com")
        val (locationId) = LocationIntegrationTest.createLocationInternal(restTemplate, 600003)
        VisitIntegrationTest.createVisitInternal(restTemplate, 600003, userId, locationId)

        val urlVariables = HashMap<String, Any>()
        urlVariables.put("id", userId)

        val response = restTemplate.getForEntity(
                REST_PATH + BY_ID + AVG + "?toAge=70", AverageVisitMarkDto::class.java, urlVariables)
        assertEquals(HttpStatus.OK, response.statusCode)
        assertNotNull(response.body)
    }

    companion object {

        fun createLocationInternal(restTemplate: TestRestTemplate, id: Int): Location {
            val location = Location(id, "London", "England", "Some garden", 3)

            val response = restTemplate.postForEntity(REST_PATH + NEW, location, EmptyJson::class.java)
            assertEquals(HttpStatus.OK, response.statusCode)
            assertNotNull(response.body)
            return location
        }

         fun updateLocationInternal(restTemplate: TestRestTemplate, location: Location) {
            val response = restTemplate.postForEntity(REST_PATH + BY_ID, location, EmptyJson::class.java, location.id)
            assertEquals(HttpStatus.OK, response.statusCode)
            assertNotNull(response.body)
        }
    }
}

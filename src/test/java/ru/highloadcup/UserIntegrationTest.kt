package ru.highloadcup

import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus
import org.springframework.test.context.junit4.SpringRunner
import ru.highloadcup.api.EmptyJson
import ru.highloadcup.api.User
import ru.highloadcup.api.VisitsDto
import ru.highloadcup.controller.VisitController

import java.util.HashMap

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import ru.highloadcup.controller.UserController.Companion.REST_PATH
import ru.highloadcup.util.HttpUtil.BY_ID
import ru.highloadcup.util.HttpUtil.NEW

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserIntegrationTest {

    @Autowired
    private lateinit var restTemplate: TestRestTemplate

    @Test
    fun createUser() {
        createUserInternal(restTemplate, 700000, "123@gmail.com")
    }

    @Test
    fun updateUser() {
        val user = createUserInternal(restTemplate, 700002, "1234@gmail.com")

        user.email = "321@gmail.com"
        updateUserInternal(restTemplate, user)
    }

    @Test
    fun getUser() {
        val user = createUserInternal(restTemplate, 700003, "1235@gmail.com")

        user.email = "322@gmail.com"
        updateUserInternal(restTemplate, user)

        val response = restTemplate.getForEntity(REST_PATH + BY_ID, User::class.java, user.id)
        assertEquals(HttpStatus.OK, response.statusCode)
        assertNotNull(response.body)
        assertEquals(user, response.body)
    }

    @Test
    fun getVisits() {
        val (userId) = UserIntegrationTest.createUserInternal(restTemplate, 1000003, "1238@gmail.com")
        val (locationId, _, country) = LocationIntegrationTest.createLocationInternal(restTemplate, 1000003)
        VisitIntegrationTest.createVisitInternal(restTemplate, 1000003, userId, locationId)

        val urlVariables = HashMap<String, Any>()
        urlVariables.put("id", userId)
        val response = restTemplate.getForEntity(
                REST_PATH + BY_ID + VisitController.REST_PATH + "/?country=$country", VisitsDto::class.java, urlVariables)
        assertEquals(HttpStatus.OK, response.statusCode)
        assertNotNull(response.body)
    }

    companion object {

        fun createUserInternal(restTemplate: TestRestTemplate, id: Int, email: String): User {
            val user = User(id, email, "first", "second", User.Gender.m, 0L)

            val response = restTemplate.postForEntity(REST_PATH + NEW, user, EmptyJson::class.java)
            assertEquals(HttpStatus.OK, response.statusCode)
            assertNotNull(response.body)
            return user
        }

        fun updateUserInternal(restTemplate: TestRestTemplate, user: User) {
            val response = restTemplate.postForEntity(REST_PATH + BY_ID, user, EmptyJson::class.java, user.id)
            assertEquals(HttpStatus.OK, response.statusCode)
            assertNotNull(response.body)
        }
    }
}

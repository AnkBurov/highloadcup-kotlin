package ru.highloadcup.warmer

import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import ru.highloadcup.AllOpen
import ru.highloadcup.api.Location
import ru.highloadcup.api.User
import ru.highloadcup.api.Visit

@AllOpen
@Component
class Warmer {

    fun warmControllers() {
        println("Warm-up started")
        val restTemplate = RestTemplate()
        for (i in 0..999) {
            restTemplate.getForEntity("http://localhost/locations/1", Location::class.java)
            restTemplate.getForEntity("http://localhost/users/1", User::class.java)
            restTemplate.getForEntity("http://localhost/visits/1", Visit::class.java)
        }
        println("Warm-up ended")
    }
}

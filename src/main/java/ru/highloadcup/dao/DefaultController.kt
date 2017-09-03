package ru.highloadcup.dao

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.highloadcup.util.doAsync

import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/hello")
class DefaultController {

    @GetMapping
    fun getDefault(request: HttpServletRequest) {
        request.startAsync().doAsync { response ->
            response.writer.write("it works")
        }
    }
}

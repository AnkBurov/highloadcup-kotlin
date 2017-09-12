package ru.highloadcup.dao

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.highloadcup.util.doAsync

import javax.servlet.http.HttpServletRequest
import net.sf.jasperreports.engine.JasperCompileManager
import net.sf.jasperreports.engine.JasperExportManager
import net.sf.jasperreports.engine.JasperFillManager
import net.sf.jasperreports.engine.JasperReport
import java.nio.file.Files
import java.nio.file.Paths


@RestController
@RequestMapping("/hello")
class DefaultController {

    /*@GetMapping
    fun getDefault() = "it works"*/

    /*@GetMapping
    fun getDefault(request: HttpServletRequest) {
        request.startAsync().doAsync { response ->
            response.writer.write("it works")
        }
    }*/

    @GetMapping
    fun jasper(): ByteArray? {
        val jasperReport = JasperCompileManager.compileReport("report.jrxml")
        val parameters = hashMapOf("name" to "fieldValue")

        val filledReport = JasperFillManager.fillReport(jasperReport, parameters as Map<String, Any>?)

        return JasperExportManager.exportReportToPdf(filledReport)
    }
}

package ru.highloadcup

import kotlinx.coroutines.experimental.*
import org.jooq.SQLDialect
import org.jooq.impl.DefaultDSLContext
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.boot.web.support.SpringBootServletInitializer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Lazy
import org.springframework.scheduling.annotation.EnableAsync
import ru.highloadcup.parser.DataParser
import ru.highloadcup.warmer.Warmer

import javax.sql.DataSource
import java.nio.file.Paths
import java.util.concurrent.TimeUnit

@AllOpen
@EnableAsync
@SpringBootApplication(scanBasePackages = arrayOf("ru.highloadcup.controller", "ru.highloadcup.dao", "ru.highloadcup.warmer", "ru.highloadcup.parser", "ru.highloadcup.check"))
class Application(
        @Lazy
        @Autowired
        val dataParser: DataParser,

        @Lazy
        @Autowired
        val warmer: Warmer
) : SpringBootServletInitializer(), InitializingBean {

    @Bean
    fun dsl(dataSource: DataSource) = DefaultDSLContext(dataSource, SQLDialect.SQLITE)

    override fun configure(builder: SpringApplicationBuilder) = builder.sources(Application::class.java)

    override fun afterPropertiesSet() {
        launch(CommonPool) {
            val dataFile: String = System.getProperty("dataFile") ?: "data.zip"
            val asyncParserTask = async(CommonPool) { dataParser.parse(Paths.get(dataFile)) }
            if (!asyncParserTask.isCompleted) {
                delay(10, TimeUnit.SECONDS)
            }
            async(CommonPool) { warmer.warmControllers() }
        }
    }

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(Application::class.java, *args)
        }
    }
}

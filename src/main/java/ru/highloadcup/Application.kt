package ru.highloadcup

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
import org.springframework.core.task.SimpleAsyncTaskExecutor
import org.springframework.scheduling.annotation.EnableAsync
import ru.highloadcup.parser.DataParser
import ru.highloadcup.warmer.Warmer

import javax.sql.DataSource
import java.nio.file.Paths

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

    @Bean
    fun taskExecutor() = SimpleAsyncTaskExecutor()

    override fun configure(builder: SpringApplicationBuilder) = builder.sources(Application::class.java)

    override fun afterPropertiesSet() {
        val dataFile: String = System.getProperty("dataFile") ?: "data.zip"
        dataParser.parse(Paths.get(dataFile))
        warmer.warmControllers()
    }

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(Application::class.java, *args)
        }
    }
}

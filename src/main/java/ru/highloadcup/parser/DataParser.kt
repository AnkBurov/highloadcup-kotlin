package ru.highloadcup.parser

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import ru.highloadcup.api.LocationsDto
import ru.highloadcup.api.UsersDto
import ru.highloadcup.api.VisitsDto
import ru.highloadcup.dao.LocationDao
import ru.highloadcup.dao.UserDao
import ru.highloadcup.dao.VisitDao
import java.io.*

import java.nio.file.Files
import java.nio.file.Path
import java.util.stream.Collectors
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream

@Component
open class DataParser(private @Autowired val userDao: UserDao,
                      private @Autowired val locationDao: LocationDao,
                      private @Autowired val visitDao: VisitDao) {

    private val mapper = ObjectMapper()

    @Async("taskExecutor")
    open fun parse(path: Path) {
        if (!Files.exists(path)) throw IllegalArgumentException("Path $path doesn't exist")

        val usersDtos = arrayListOf<UsersDto>()
        val locationsDtos = arrayListOf<LocationsDto>()
        val visitsDtos = arrayListOf<VisitsDto>()

        FileInputStream(path.toFile()).buffered().zipStream().use { zip ->
            while (true) {
                val nextEntry: ZipEntry? = zip.nextEntry
                if (nextEntry == null) break
                println(nextEntry)
                val fileName = nextEntry.name

                val bufferedReader: BufferedReader = InputStreamReader(zip).buffered()
                when {
                    fileName.contains("users") -> usersDtos.add(bufferedReader.readJsonDtos(mapper))
                    fileName.contains("locations") -> locationsDtos.add(bufferedReader.readJsonDtos(mapper))
                    fileName.contains("visits") -> visitsDtos.add(bufferedReader.readJsonDtos(mapper))
                    else -> println("Unsupported file $fileName in archive")
                }
            }
        }

        //insert to database
        usersDtos.parallelStream().forEach { userDao.createUsers(it.users) }
        locationsDtos.parallelStream().forEach { locationDao.createLocations(it.locations) }
        visitsDtos.parallelStream().forEach { visitDao.createVisits(it.visits) }
        println("Entities uploaded")
    }

    private inline fun <reified T : Serializable> BufferedReader.readJsonDtos(mapper: ObjectMapper): T {
        val json = this.lines().collect(Collectors.joining())
        return mapper.readValue(json, T::class.java)
    }

    private fun BufferedInputStream.zipStream() = ZipInputStream(this)
}

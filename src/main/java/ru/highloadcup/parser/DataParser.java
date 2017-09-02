package ru.highloadcup.parser;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import ru.highloadcup.api.LocationsDto;
import ru.highloadcup.api.UsersDto;
import ru.highloadcup.api.VisitsDto;
import ru.highloadcup.dao.LocationDao;
import ru.highloadcup.dao.UserDao;
import ru.highloadcup.dao.VisitDao;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Component
public class DataParser {

    @Autowired
    private UserDao userDao;

    @Autowired
    private LocationDao locationDao;

    @Autowired
    private VisitDao visitDao;

    @Async("taskExecutor")
    public void parse(Path path) {
        try {
            if (!Files.exists(path)) {
                throw new IllegalArgumentException("Path " + path + "doesn't exist");
            }

            List<UsersDto> usersDtos = new ArrayList<>();
            List<LocationsDto> locationsDtos = new ArrayList<>();
            List<VisitsDto> visitsDtos = new ArrayList<>();
            try (ZipInputStream zip = new ZipInputStream(new BufferedInputStream(new FileInputStream(path.toFile())))) {
                ZipEntry nextEntry = null;
                while ((nextEntry = zip.getNextEntry()) != null) {
                    System.out.println(nextEntry);
                    String fileName = nextEntry.getName();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(zip, Charset.forName("UTF-8")));
                    ObjectMapper mapper = new ObjectMapper();
                    if (fileName.contains("users")) {
                        String json = bufferedReader.lines().collect(Collectors.joining());
                        UsersDto usersDto = mapper.readValue(json, UsersDto.class);
                        usersDtos.add(usersDto);
                    } else if (fileName.contains("locations")) {
                        String json = bufferedReader.lines().collect(Collectors.joining());
                        LocationsDto locationsDto = mapper.readValue(json, LocationsDto.class);
                        locationsDtos.add(locationsDto);
                    } else if (fileName.contains("visits")) {
                        String json = bufferedReader.lines().collect(Collectors.joining());
                        VisitsDto visitsDto = mapper.readValue(json, VisitsDto.class);
                        visitsDtos.add(visitsDto);
                    } else {
                        System.err.println("Unsupported file " + fileName + " in archive");
                    }
                }
            }

            //insert to database
            usersDtos.parallelStream().forEach(usersDto -> userDao.createUsers(usersDto.getUsers()));
            locationsDtos.parallelStream().forEach(locationsDto -> locationDao.createLocations(locationsDto.getLocations()));
            visitsDtos.parallelStream().forEach(visitsDto -> visitDao.createVisits(visitsDto.getVisits()));
            System.out.println("Entities uploaded");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

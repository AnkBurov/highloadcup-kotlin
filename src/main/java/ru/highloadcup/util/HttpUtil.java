package ru.highloadcup.util;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.highloadcup.api.EmptyJson;

public class HttpUtil {

    public static final String BY_ID = "/{id}";
    public static final String NEW = "/new";

    public static final HttpHeaders JSON_HEADERS = new HttpHeaders();

    static {
        JSON_HEADERS.add("Content-Type", "application/json");
    }

    public static final ResponseEntity NOT_FOUND = new ResponseEntity<>(HttpStatus.NOT_FOUND);
    public static final ResponseEntity BAD_REQUEST = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    public static final ResponseEntity OK = new ResponseEntity<>(EmptyJson.INSTANCE, JSON_HEADERS, HttpStatus.OK);

}

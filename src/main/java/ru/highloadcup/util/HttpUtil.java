package ru.highloadcup.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;

public class HttpUtil {

    public static final String BY_ID = "/{id}";
    public static final String NEW = "/new";
    private static final String UTF8 = "UTF-8";
    private static final String CONTENT_TYPE = "application/json;charset=" + UTF8;
    private static final byte[] EMPTY_BYTES = new byte[0];

    private static final ObjectMapper MAPPER = new ObjectMapper().findAndRegisterModules();

    static {
        MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    public static void createResponse(HttpStatus httpStatus, HttpServletResponse response) throws IOException {
        response.setStatus(httpStatus.value());
        response.getOutputStream().write(EMPTY_BYTES);
    }

    public static <T extends Serializable> void createResponse(T object, HttpServletResponse response) throws IOException {
        response.setCharacterEncoding(UTF8);
        response.setContentType(CONTENT_TYPE);
        if (object == null) {
            response.setStatus(HttpStatus.NOT_FOUND.value());
            response.getOutputStream().write(EMPTY_BYTES);
        } else {
            response.setStatus(HttpStatus.OK.value());
            response.getOutputStream().write(MAPPER.writeValueAsBytes(object));
        }
    }
}

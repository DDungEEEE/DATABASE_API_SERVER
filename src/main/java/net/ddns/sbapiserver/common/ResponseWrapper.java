package net.ddns.sbapiserver.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class ResponseWrapper {

    private final ObjectMapper objectMapper;

    public void convertObjectToResponse(HttpServletResponse res, Object object) throws IOException {
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");

        String result = objectMapper.writeValueAsString(object);

        res.getWriter().write(result);
    }
}

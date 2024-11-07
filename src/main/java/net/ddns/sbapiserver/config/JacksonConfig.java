package net.ddns.sbapiserver.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class JacksonConfig implements WebMvcConfigurer {

    // 요청을 처리할 ObjectMapper
    @Bean
    @Primary
    public ObjectMapper requestObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        // 요청 시 스네이크 케이스를 사용
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        return objectMapper;
    }

    // 응답을 처리할 ObjectMapper
    @Bean
    public ObjectMapper responseObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        // 응답 시 카멜 케이스를 사용
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.LOWER_CAMEL_CASE);
        return objectMapper;
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        // 요청 처리용 ObjectMapper 추가
        converters.add(new MappingJackson2HttpMessageConverter(requestObjectMapper()));
        // 응답 처리용 ObjectMapper 추가
        converters.add(new MappingJackson2HttpMessageConverter(responseObjectMapper()));
    }
}

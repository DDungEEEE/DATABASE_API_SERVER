package net.ddns.sbapiserver.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class JacksonConfig implements WebMvcConfigurer {

    private ObjectMapper createObjectMapper(PropertyNamingStrategy namingStrategy) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setPropertyNamingStrategy(namingStrategy);
        objectMapper.registerModule(new JavaTimeModule()); // 날짜/시간 처리
        return objectMapper;
    }

    @Bean
    @Primary
    public ObjectMapper requestObjectMapper() {
        return createObjectMapper(PropertyNamingStrategies.SNAKE_CASE); // 요청 시 스네이크 케이스
    }

    // 응답을 처리할 ObjectMapper
    @Bean
    public ObjectMapper responseObjectMapper() {
        return createObjectMapper(PropertyNamingStrategies.LOWER_CAMEL_CASE); // 응답 시 카멜 케이스
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        // 요청 처리용 ObjectMapper 추가
        converters.add(new MappingJackson2HttpMessageConverter(requestObjectMapper()));
        // 응답 처리용 ObjectMapper 추가
        converters.add(new MappingJackson2HttpMessageConverter(responseObjectMapper()));
    }
}

package net.ddns.sbapiserver.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import net.ddns.sbapiserver.common.code.ErrorCode;
import net.ddns.sbapiserver.common.swagger.ApiErrorCodeExample;
import net.ddns.sbapiserver.common.swagger.ApiErrorCodeExamples;
import net.ddns.sbapiserver.exception.error.ErrorResponse;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Configuration
public class SwaggerConfig{

    private static final String AUTHORIZATION_HEADER = "Authorization";

    @Bean
    public GroupedOpenApi jwtApi(){
        return GroupedOpenApi.builder()
                .group("public")
                .pathsToMatch("/**")
                .addOpenApiCustomizer(openApi -> {
                    openApi.getComponents().addParameters(AUTHORIZATION_HEADER, createHeader(AUTHORIZATION_HEADER));
                })
                .addOperationCustomizer(customize())
                .build();
    }

    @Bean
    public OperationCustomizer customize() {
        return (Operation operation, HandlerMethod handlerMethod) -> {
            ApiErrorCodeExamples apiErrorCodeExamples = handlerMethod.getMethodAnnotation(
                    ApiErrorCodeExamples.class);

            // @ApiErrorCodeExamples annotation
            if (apiErrorCodeExamples != null) {
                generateErrorCodeResponseExample(operation, apiErrorCodeExamples.value());
            } else {
                ApiErrorCodeExample apiErrorCodeExample = handlerMethod.getMethodAnnotation(
                        ApiErrorCodeExample.class);

                // @ApiErrorCodeExample annotation
                if (apiErrorCodeExample != null) {
                    generateErrorCodeResponseExample(operation, apiErrorCodeExample.value());
                }
            }

            return operation;
        };
    }

    // Generate examples for multiple error codes
    private void generateErrorCodeResponseExample(Operation operation, ErrorCode[] errorCodes) {
        ApiResponses responses = operation.getResponses();

        // Group ExampleHolders by HTTP status code
        Map<Integer, List<ExampleHolder>> statusWithExampleHolders = Arrays.stream(errorCodes)
                .map(
                        errorCode -> ExampleHolder.builder()
                                .holder(getSwaggerExample(errorCode))
                                .code(errorCode.getStatus())
                                .name(errorCode.name())
                                .build()
                )
                .collect(Collectors.groupingBy(ExampleHolder::getCode));

        // Add examples to responses
        addExamplesToResponses(responses, statusWithExampleHolders);
    }

private void generateErrorCodeResponseExample(Operation operation, ErrorCode errorCode) {
    ApiResponses responses = operation.getResponses();

    ExampleHolder exampleHolder = ExampleHolder.builder()
            .holder(getSwaggerExample(errorCode))
            .name(errorCode.name())
            .code(errorCode.getStatus())
            .build();
    addExamplesToResponses(responses, exampleHolder);
}

    private Example getSwaggerExample(ErrorCode errorCode){
        ErrorResponse errorResponse = ErrorResponse.from(errorCode);
        Example example = new Example();
        example.setValue(errorResponse);

        return example;
    }

    // Add ExampleHolders to ApiResponses
    private void addExamplesToResponses(ApiResponses responses,
                                        Map<Integer, List<ExampleHolder>> statusWithExampleHolders) {
        statusWithExampleHolders.forEach(
                (status, holders) -> {
                    Content content = new Content();
                    MediaType mediaType = new MediaType();
                    ApiResponse apiResponse = new ApiResponse();

                    holders.forEach(
                            exampleHolder -> mediaType.addExamples(
                                    exampleHolder.getName(),
                                    exampleHolder.getHolder()
                            )
                    );
                    content.addMediaType("application/json", mediaType);
                    apiResponse.setContent(content);
                    responses.addApiResponse(String.valueOf(status), apiResponse);
                }
        );
    }

    // Add single ExampleHolder to ApiResponses
    private void addExamplesToResponses(ApiResponses responses, ExampleHolder exampleHolder) {
        Content content = new Content();
        MediaType mediaType = new MediaType();
        ApiResponse apiResponse = new ApiResponse();

        mediaType.addExamples(exampleHolder.getName(), exampleHolder.getHolder());
        content.addMediaType("application/json", mediaType);
        apiResponse.setContent(content);
        responses.addApiResponse(String.valueOf(exampleHolder.getCode()), apiResponse);
    }



    @Bean
    public OpenAPI openAPI(){
        return new OpenAPI()
                .info(new Info().title("JWT API")
                        .description("API Documentation"))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")))
                .servers(List.of(
                        new Server().url("https://batteryfriendserverapi.com"),
                        new Server().url("https://sbtechapiserver.ddns.net"),
                        new Server().url("http://localhost:33080")));
    }

    private Parameter createHeader(String name){
        return new Parameter()
                .in("header")
                .schema(new io.swagger.v3.oas.models.media.StringSchema())
                .name(name)
                .description(name)
                .required(false);
    }
}

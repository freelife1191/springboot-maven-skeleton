package com.project.config;

import com.fasterxml.classmate.TypeResolver;
import com.google.common.collect.Lists;
import com.project.common.constant.ResCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.Ordered;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import springfox.documentation.builders.*;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Response;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.function.Predicate;

import static springfox.documentation.schema.AlternateTypeRules.newRule;

/**
 * Swagger Api Docs 설정
 * https://springfox.github.io/springfox/docs/current/
 * Created by KMS on 2019-08-19.
 */
@Configuration
@Profile("!prod")
/** Swagger 2.10.5 사용시 주석해제 */
// @EnableSwagger2
/** Swagger 3.0.0 전용 설정 2.10.5 사용시 주석처리 */
@EnableOpenApi
@RequiredArgsConstructor
public class SwaagerConfig {

    @Value("${service.name}")
    private String serviceName;
    @Value("${service.version}")
    private String serviceVersion;
    @Value("${spring.profiles.active}")
    private String springProfile;

    /** Swagger 3.0.0 전용 설정 2.9.2 사용시 주석처리 */
    private final TypeResolver typeResolver;
    /** Swagger 3.0.0 전용 설정 2.9.2 사용시 주석처리 */
    private final RepositoryRestConfiguration restConfiguration;

    @Bean
    public Docket api(){
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
               //.apis(RequestHandlerSelectors.any()) //모든 패키지 OPEN
               //.apis(Predicates.not(RequestHandlerSelectors.basePackage("org.springframework.boot"))) // org.springframework.boot 패키지 제외
                .apis(RequestHandlerSelectors.basePackage("com.project.api") // api 패키지에 해당되는 컨트롤러만
                        .or(RequestHandlerSelectors.basePackage( "com.project.component.code.controller")) // 공통컴포넌트 공통코드 패키지 추가
                )// 패키지에 해당되는 컨트롤러만
                .paths(PathSelectors.any()
                        // .and(PathSelectors.regex("/common/code/*").negate()) // 해당 패턴 PATH 제외
                ) //모든 API 추가
               //.paths(regex("/product.*")) //특정 정규식 패턴에 해당되는 API 추가
               //.paths(PathSelectors.ant("/v2/file/*")) //특정 정규식 패턴에 해당되는 API 추가
               //.paths(PathSelectors.ant("/api/v2/**/*")) //특정 정규식 패턴에 해당되는 API 추가
                .build()
                /** Swagger Pageable 관련설정 */
                .alternateTypeRules(newRule(typeResolver.resolve(Pageable.class), pageableMixin(restConfiguration), Ordered.HIGHEST_PRECEDENCE))
                .apiInfo(apiInfo())
                //ErrorHandler에 RequestBody 값을 전달하기 위한 WebRequest Ignore 설정
                .ignoredParameterTypes(WebRequest.class, File.class, InputStream.class, Resource.class, URI.class, URL.class, View.class, ModelAndView.class)
                .useDefaultResponseMessages(false)
                /*
                .globalResponseMessage(RequestMethod.GET,
                        newArrayList(new ResponseMessageBuilder()
                                        .code(500)
                                        .message("Internal Server Error")
                                        // .responseModel(new ModelRef("Error"))
                                        .build(),
                                new ResponseMessageBuilder()
                                        .code(400)
                                        .message("Bad Request")
                                        .build(),
                                new ResponseMessageBuilder()
                                        .code(404)
                                        .message("Not Found")
                                        .build()))
                */
                .globalResponses(HttpMethod.DELETE, getResponseMessage())
                .globalResponses(HttpMethod.GET, getResponseMessage())
                .globalResponses(HttpMethod.HEAD, getResponseMessage())
                .globalResponses(HttpMethod.OPTIONS, getResponseMessage())
                .globalResponses(HttpMethod.PATCH, getResponseMessage())
                .globalResponses(HttpMethod.POST, getResponseMessage())
                .globalResponses(HttpMethod.PUT, getResponseMessage())
                ;

    }

    /**
     * Path 다중 설정 Sample
     * https://github.com/springfox/springfox/blob/master/springfox-petstore/src/main/java/springfox/petstore/PetStoreConfiguration.java
     * @return
     */
    private Predicate<String> paths() {
        return PathSelectors.regex("/api/v1/models.*")
                .and(PathSelectors.regex("/api/v1/search.*")).negate()
                .or(PathSelectors.regex("/api/v1/generators.*"))
                .or(PathSelectors.regex("/api/v1/attachments.*"));
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(serviceName+"-"+springProfile)
                .description(serviceName+"-"+springProfile)
                .version(serviceVersion)
                .build();
        /*
        return new ApiInfo(
                "springboot-maven-skeleton",
                "springboot-maven-skeleton 입니다",
                "V0.1",
                "약관",
                new Contact("company","test.co.kr","anonymous@mail.co.kr"),
                "License of API", "API license URL", Collections.emptyList());
        */
    }

    /**
     * 에러메시지 셋팅
     * @return
     */
    private List<Response> getResponseMessage() {
        return Lists.newArrayList(
                new ResponseBuilder()
                        .code(String.valueOf(ResCode.SUCCESS.getCode()))
                        .description(ResCode.SUCCESS.getMessage())
                        .build(),
                new ResponseBuilder()
                        .code(String.valueOf(HttpStatus.BAD_REQUEST.value()))
                        .description(HttpStatus.BAD_REQUEST.getReasonPhrase())
                        .build(),
            /*

            new ResponseMessageBuilder()
                    .code(HttpStatus.UNAUTHORIZED.value())
                    .message(HttpStatus.UNAUTHORIZED.getReasonPhrase())
                    .build(),
            new ResponseMessageBuilder()
                    .code(HttpStatus.FORBIDDEN.value())
                    .message(HttpStatus.FORBIDDEN.getReasonPhrase())
                    .build(),
            */
                new ResponseBuilder()
                        .code(String.valueOf(HttpStatus.NOT_FOUND.value()))
                        .description(HttpStatus.NOT_FOUND.getReasonPhrase())
                        .build(),
                new ResponseBuilder()
                        .code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                        .description(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                        .build()
            /*
            new ResponseMessageBuilder()
                    .code(HttpStatus.BAD_GATEWAY.value())
                    .message(HttpStatus.BAD_GATEWAY.getReasonPhrase())
                    .build()
            */
        );
    }

    /**
     * Pageable Mix in
     * https://github.com/springfox/springfox/blob/106f0cbd260f1a39c693555d6c06527a2b90ade2/springfox-data-rest/src/main/java/springfox/documentation/spring/data/rest/configuration/SpringDataRestConfiguration.java
     * @param restConfiguration
     * @return
     */
    private Type pageableMixin(RepositoryRestConfiguration restConfiguration) {
        return new AlternateTypeBuilder()
                .fullyQualifiedClassName(
                        String.format("%s.generated.%s",
                                Pageable.class.getPackage().getName(),
                                Pageable.class.getSimpleName()))
                .property(p -> p.name(restConfiguration.getPageParamName())
                        .type(Integer.class)
                        .canRead(true)
                        .canWrite(true))
                .property(p -> p.name(restConfiguration.getLimitParamName())
                        .type(Integer.class)
                        .canRead(true)
                        .canWrite(true))
                .property(p -> p.name(restConfiguration.getSortParamName())
                        .type(String.class)
                        .canRead(true)
                        .canWrite(true))
                .build();
    }

}

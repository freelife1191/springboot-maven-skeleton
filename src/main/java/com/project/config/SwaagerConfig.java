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
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import springfox.documentation.builders.*;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.schema.AlternateTypeRules;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
// import springfox.documentation.swagger2.annotations.EnableSwagger2;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;


/**
 * Swagger Api Docs 설정
 * Created by KMS on 2019-08-19.
 */
@Configuration
@Profile("!prod")
/** Swagger 2.10.5 사용시 주석해제 */
@EnableSwagger2
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

    /** Swagger 3.0.0 SNAPSHOT 전용 설정 2.9.2 사용시 주석처리 */
    private final TypeResolver typeResolver;
    /** Swagger 3.0.0 SNAPSHOT 전용 설정 2.9.2 사용시 주석처리 */
    private final RepositoryRestConfiguration restConfiguration;

    @Bean
    public Docket api(){
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
               //.apis(RequestHandlerSelectors.any()) //모든 패키지 OPEN
               //.apis(Predicates.not(RequestHandlerSelectors.basePackage("org.springframework.boot"))) // org.springframework.boot 패키지 제외
                .apis(RequestHandlerSelectors.basePackage("com.project.api"))// com.project.api 패키지에 해당되는 컨트롤러만
               //.apis(Predicates.or(
               //        RequestHandlerSelectors.basePackage("com.project.api.contoller"),
               //        RequestHandlerSelectors.basePackage("com.project.common")
               //        RequestHandlerSelectors.basePackage("com.project.component.excel.tutorial")
               //        )
               //    ) // 특정 패키지들의 컨트롤러만
                .paths(PathSelectors.any()) //모든 API 추가
               //.paths(regex("/product.*")) //특정 정규식 패턴에 해당되는 API 추가
               //.paths(PathSelectors.ant("/v2/file/*")) //특정 정규식 패턴에 해당되는 API 추가
               //.paths(PathSelectors.ant("/api/v2/**/*")) //특정 정규식 패턴에 해당되는 API 추가
                .build()
                /** Swagger 3.0.0 SNAPSHOT 전용 설정 2.9.2 사용시 주석처리 */
                .alternateTypeRules(AlternateTypeRules.newRule(typeResolver.resolve(Pageable.class), pageableMixin(restConfiguration), Ordered.HIGHEST_PRECEDENCE))
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
                .globalResponseMessage(RequestMethod.DELETE, getResponseMessage())
                .globalResponseMessage(RequestMethod.GET, getResponseMessage())
                .globalResponseMessage(RequestMethod.HEAD, getResponseMessage())
                .globalResponseMessage(RequestMethod.OPTIONS, getResponseMessage())
                .globalResponseMessage(RequestMethod.PATCH, getResponseMessage())
                .globalResponseMessage(RequestMethod.POST, getResponseMessage())
                .globalResponseMessage(RequestMethod.PUT, getResponseMessage())
                ;

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
    private List<ResponseMessage> getResponseMessage() {
        return Lists.newArrayList(
                new ResponseMessageBuilder()
                        .code(ResCode.SUCCESS.getCode())
                        .message(ResCode.SUCCESS.getMessage())
                        .build(),
                new ResponseMessageBuilder()
                        .code(HttpStatus.BAD_REQUEST.value())
                        .message(HttpStatus.BAD_REQUEST.getReasonPhrase())
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
                new ResponseMessageBuilder()
                        .code(HttpStatus.NOT_FOUND.value())
                        .message(HttpStatus.NOT_FOUND.getReasonPhrase())
                        .build(),
                new ResponseMessageBuilder()
                        .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .message(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                        .build()
            /*
            new ResponseMessageBuilder()
                    .code(HttpStatus.BAD_GATEWAY.value())
                    .message(HttpStatus.BAD_GATEWAY.getReasonPhrase())
                    .build()
            */
        );
    }

    /** Swagger 3.0.0 SNAPSHOT 전용 메서드 2.9.2 사용시 주석처리 */
    private Type pageableMixin(RepositoryRestConfiguration restConfiguration) {
        return new AlternateTypeBuilder()
                .fullyQualifiedClassName(
                        String.format("%s.generated.%s",
                                Pageable.class.getPackage().getName(),
                                Pageable.class.getSimpleName()))
                .withProperties(Arrays.asList(
                        property(Integer.class,
                                restConfiguration.getPageParamName()),
                        property(Integer.class,
                                restConfiguration.getLimitParamName()),
                        property(String.class,
                                restConfiguration.getSortParamName())
                ))
                .build();
    }

    /** Swagger 3.0.0 SNAPSHOT 전용 메서드 2.9.2 사용시 주석처리 */
    private AlternateTypePropertyBuilder property(Class<?> type, String name) {
        return new AlternateTypePropertyBuilder()
                .withName(name)
                .withType(type)
                .withCanRead(true)
                .withCanWrite(true);
    }

}

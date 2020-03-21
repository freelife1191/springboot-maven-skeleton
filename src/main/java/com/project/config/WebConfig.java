package com.project.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.Duration;

/**
 * Created by KMS on 2019-08-19.
 * 웹 공통 설정
 */
@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final RestTemplateBuilder restTemplateBuilder;

    @Value("${properties.file.max-upload-size}")
    private long MAX_UPLOAD_SIZE;

    /**
     * CORS 설정 모두허용
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods(HttpMethod.POST.name(), HttpMethod.GET.name(), HttpMethod.PUT.name(), HttpMethod.PATCH.name(), HttpMethod.DELETE.name()
                        , HttpMethod.OPTIONS.name(), HttpMethod.HEAD.name(), HttpMethod.TRACE.name())
                .allowCredentials(false)
                .allowedHeaders("*")
                .maxAge(3600);
    }

    @Bean
    public RestTemplate restTemplateCustomizer() {
        return restTemplateBuilder
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                //.additionalInterceptors(new RestTemplateClientHttpRequestInterceptor)
                //.errorHandler(new RestTemplateErrorHandler())
                .setConnectTimeout(Duration.ofSeconds(30))
                .setReadTimeout(Duration.ofSeconds(300))
                .build();
    }

    @Bean(name = "multipartResolver")
    public CommonsMultipartResolver multipartResolver() {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setMaxUploadSize(MAX_UPLOAD_SIZE * 1024 * 1024);

        return multipartResolver;
    }
}

package com.project.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.Connector;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.Duration;

/**
 * Created by KMS on 2019-08-19.
 * 웹 공통 설정
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    // private final RestTemplateBuilder restTemplateBuilder;

    @Value("${properties.file.max-upload-size}")
    private long MAX_UPLOAD_SIZE;
    @Value("${spring.servlet.multipart.location}")
    private String location;
    @Value("${spring.servlet.multipart.max-file-size}")
    private String maxFileSize;

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
                .maxAge(60 * 60); // 1시간
    }

    /*
    @Bean
    public RestTemplate restTemplate() {
        return restTemplateBuilder
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                //.additionalInterceptors(new RestTemplateClientHttpRequestInterceptor)
                //.errorHandler(new RestTemplateErrorHandler())
                .setConnectTimeout(Duration.ofSeconds(30)) //접속유지 30초
                .setReadTimeout(Duration.ofSeconds(300)) //응답대기 300초
                .build();
    }
    */

    @Bean
    public TomcatServletWebServerFactory tomcatServletWebServerFactory() {
        return new TomcatServletWebServerFactory() {
            protected void customizeConnector(Connector connector) {
                super.customizeConnector(connector);
                connector.setParseBodyMethods("POST,PUT,PATCH,DELETE");
            }
        };
    }

    /*
    @Bean(name = "multipartResolver")
    public CommonsMultipartResolver multipartResolver() throws IOException {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        // 요청을 파싱할 때 사용할 캐릭터 인코딩
        // 지정하지 않을 경우, HttpServletRequest.setCharacterEncoding() 메서드로 지정한 캐릭터 셋이 사용
        // 아무 값도 없을 경우 ISO-8859-1을 사용
        multipartResolver.setDefaultEncoding("UTF-8");
        // 디스크에 임시 파일을 생성하기 전에 메모리에 보관할수있는 최대 바이트크기 default: 10240 바이트
        multipartResolver.setMaxInMemorySize(10240);
        // 최대 업로드 가능한 바이트 크기, -1은 제한이 없음을 의미, 기본값은 -1
        DataSize parse = DataSize.parse(maxFileSize);
        log.info("## maxFileSize = {}",parse.toBytes());
        multipartResolver.setMaxUploadSize(parse.toBytes());
        // 임시디렉터리 지정
        Path tmpDirPath = Paths.get(location);
        log.info("tmpDirPath ={}, tmpfilesPath = {}",tmpDirPath, location);
        if (Files.notExists(tmpDirPath)) Files.createDirectories(tmpDirPath);
        multipartResolver.setUploadTempDir(new FileSystemResource(tmpDirPath));
        // multipartResolver.setUploadTempDir(new ClassPathResource(filePath.toString()));
        // multipartResolver.setUploadTempDir(resolver.getResource(filePath.toString()));

        // 한 파일당 업로드가 허용되는 최대 용량을 바이트 단위로 설정
        // -1 은 제한이 없다는 뜻으로 이 프로퍼티를 지정하지 않을때 기본값
        //multipartResolver.setMaxUploadSizePerFile(-1);
        // CommonsMultipartFile.getOriginalFilename()에서 경로 정보를 제거하지 않고 클라이언트가 보낸 대로 파일 이름을 보존할지 여부를 설정
        // 기본값은 "false", Opera에서와 같이 실제 파일 이름 앞에 붙을 수 있는 경로 정보를 제거
        // 잠재적 경로 구분자를 포함하여 클라이언트 지정 파일 이름을 그대로 유지하려면 이 값을 "true"로 설정
        //multipartResolver.setPreserveFilename(false);
        //파일 또는 매개 변수 액세스시 다중 파트 요청을 느리게 해결할지 여부를 설정
        // 기본값은 "false"이며 멀티 파트 요소를 즉시 해결하여 resolveMultipart(HttpServletRequest)호출 시 해당 예외를 발생시킴
        // 애플리케이션이 멀티 파트 파일 또는 매개 변수를 얻으려고 시도하면 구문 분석 예외가 발생하여 지연된 멀티 파트 구문 분석의 경우 이를 "true"로 전환
        //multipartResolver.setResolveLazily(false);
        return multipartResolver;
    }
    */

    /**
     * Swagger UI 리소스 설정
     * https://github.com/springfox/springfox-demos/blob/master/boot-swagger/src/main/java/springfoxdemo/boot/swagger/SwaggerUiWebMvcConfigurer.java
     * https://github.com/springfox/springfox-demos/blob/master/spring-java-swagger/src/main/java/springfoxdemo/java/swagger/SpringConfig.java
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // String baseUrl = StringUtils.trimTrailingCharacter(this.baseUrl, '/');
        registry.
                addResourceHandler("/swagger-ui/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/springfox-swagger-ui/")
                .resourceChain(false);
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/swagger-ui/")
                .setViewName("forward:" + "/swagger-ui/index.html");
        registry.addRedirectViewController("/swagger-ui.html","/swagger-ui/index.html");
    }

    /*
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");

        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
    */
}

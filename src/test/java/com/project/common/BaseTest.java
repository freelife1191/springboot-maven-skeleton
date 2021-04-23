package com.project.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;


/**
 * BASE 컨트롤러 테스트 클래스
 * Created by KMS on 06/03/2020.
 */
@SpringBootTest
@AutoConfigureMockMvc
// @RequiredArgsConstructor
@ActiveProfiles("h2")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@Transactional
public class BaseTest {
    protected final MockMvc mockMvc;
    protected final ObjectMapper mapper;
    protected final WebApplicationContext ctx;
    protected StopWatch stopWatch; //개별 테스트 성능 측정용
    protected static StopWatch totalStopWatch; //종합 테스트 성능 측정용

    // 테스트 JWT 데이터
    protected static String jwtData = "";

    public BaseTest(ObjectMapper mapper, WebApplicationContext ctx) {
        this.mapper = mapper;
        this.ctx = ctx;
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.ctx)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))  // 필터 추가
                .alwaysDo(print())
                .build();
    }

    /**
     * 파일 업로드 데이터 생성
     * @param originalFileName 원본파일명
     * @param reqFileName 요청파일명(API에서 받는 이름)
     * @return
     */
    protected static MockMultipartFile getMultipartFile(String originalFileName, String reqFileName) {
        return getMultipartFile(null, originalFileName, reqFileName);
    }

    /**
     * 파일 업로드 데이터 생성
     * @param originalFileName 원본파일명
     * @param reqFileName 요청파일명(API에서 받는 이름)
     * @return
     */
    protected static MockMultipartFile getMultipartFile(String fileMiddlePath, String originalFileName, String reqFileName) {
        Path filePath = Paths.get("src", "test", "resources");
        if(StringUtils.isNotEmpty(fileMiddlePath))
            filePath = Path.of(filePath.toString(), fileMiddlePath);

        filePath = Paths.get(filePath.toString() ,originalFileName);
        // File file = new File(String.valueOf(filePath));

        //        String contentType = "";
        //        String contentType = "image/png";
        //        String contentType = "application/zip";
        byte[] content = null;

        try {
            //            File file = new File(String.valueOf(path));
            content = Files.readAllBytes(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new MockMultipartFile(reqFileName, originalFileName, null, content);
    }
}
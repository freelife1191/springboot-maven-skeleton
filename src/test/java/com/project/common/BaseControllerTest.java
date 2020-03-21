package com.project.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
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
@ActiveProfiles("local")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
// @Transactional
public class BaseControllerTest {
    protected final MockMvc mockMvc;
    protected final ObjectMapper mapper;
    protected final WebApplicationContext ctx;
    protected StopWatch stopWatch; //개별 테스트 성능 측정용
    protected static StopWatch totalStopWatch; //종합 테스트 성능 측정용

    // 테스트 JWT 데이터
    protected static String jwtData = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ0b2tlbktleSI6IjIxMEJGMDY1LUFDRjMtNEUxQS04QTM4LThFRjQ0Mzg1NTM5QSIsImlkIjoieXNraW0iLCJuYW1lIjoiXHVhZTQwXHVjNzIwXHVjMTFkIiwiZGVwYXJ0bWVudF9jb2RlIjoiMTUwMTIiLCJkZXBhcnRtZW50X25hbWUiOiJcdWM2ZjlcdWQ1MDRcdWI4NjBcdWQyYjhcdWM1ZDRcdWI0ZGNcdWQzMGNcdWQyYjgiLCJlbXBsb3llZV9udW1iZXIiOiIyMDE3MDAyNCIsImVtYWlsIjoieXNraW1AaXBhcmtpbmcuY28ua3IiLCJwb3NpdGlvbl9jb2RlIjoiSjEiLCJwb3NpdGlvbl9uYW1lIjoiXHViOWU0XHViMmM4XHVjODAwIiwiZHV0eV9jb2RlIjoiTDIiLCJkdXR5X25hbWUiOiJcdWQzMDBcdWM2ZDAiLCJ0ZWFtX25hbWUiOm51bGwsInBhdGgiOiIxMDAwfDEwMDAwfDE1MDExfDE1MDEyIiwicGF0aF9uYW1lIjoiXHVkMzBjXHVkMGI5XHVkMDc0XHViNzdjXHVjNmIwXHViNGRjfFImRHxcdWQ1MDRcdWI4NjBcdWQyYjhcdWM1ZDRcdWI0ZGNcdWQzMDB8XHVjNmY5XHVkNTA0XHViODYwXHVkMmI4XHVjNWQ0XHViNGRjXHVkMzBjXHVkMmI4In0.bxBABM7tSoHyxMyfp2P89rQSGS_cWpAqc-1bcGDrbrY";

    public BaseControllerTest(ObjectMapper mapper, MockMvc mockMvc, WebApplicationContext ctx) {
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
        Path filePath = Paths.get("src", "test", "resources", File.separatorChar + originalFileName);
        File file = new File(String.valueOf(filePath));

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
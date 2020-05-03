package com.project.api.sample.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.common.BaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Sample 파일 컨트롤러 테스트
 * Created by KMS on 03/05/2020.
 */
class SampleFileControllerTest extends BaseTest {

    public SampleFileControllerTest(ObjectMapper mapper, WebApplicationContext ctx) {
        super(mapper, ctx);
    }

    @Test
    @DisplayName("Sample 파일 업로드 테스트")
    void upload() throws Exception {
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart("/sample/file")
                .file(getMultipartFile("img", "1-1.png", "file"))
                .header("jwt",jwtData);

        mockMvc.perform(builder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("fileName").value("1-1.png"))
                .andDo(print());
    }

    @Test
    @DisplayName("Sample 파일 다운로드 테스트")
    void download() throws Exception {
        mockMvc.perform(get("/sample/file")
                .param("fileName", "1-1.png"))
                .andExpect(status().isOk());
    }
}
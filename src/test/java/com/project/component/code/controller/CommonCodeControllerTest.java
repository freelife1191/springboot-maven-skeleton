package com.project.component.code.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.common.BaseTest;
import com.project.component.code.domain.CommonCode;
import com.project.component.code.packet.ReqCommonCodeGET;
import com.project.component.code.packet.ReqCommonCodeRegist;
import com.project.utils.common.TestUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.web.context.WebApplicationContext;

import static com.project.common.constant.ResCode.SUCCESS;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 공통 메인 코드 테스트 코드
 * Created by KMS on 27/03/2020.
 */
// @ActiveProfiles("h2")
class CommonCodeControllerTest extends BaseTest {

    public CommonCodeControllerTest(ObjectMapper mapper, WebApplicationContext ctx) {
        super(mapper, ctx);
    }

    @Test
    @DisplayName("공통 메인 코드 조회")
    void getCommonCode() throws Exception {
        ReqCommonCodeGET reqCommonCodeGET = ReqCommonCodeGET.builder()
                .code(1)
                .build();

        mockMvc.perform(get("/common/code")
                .queryParams(TestUtils.objectToMultiValueMap(reqCommonCodeGET)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("code").value(SUCCESS.getCode()))
                .andDo(print());
    }

    @Test
    @DisplayName("공통 메인 코드 등록")
    void commonCodeRegistration() throws Exception {
        ReqCommonCodeRegist regist = ReqCommonCodeRegist.builder()
                .groupCode("TEST")
                .codeNm("메인 테스트")
                .codeEngNm("MAIN TEST")
                .codeDc("메인 테스트 설명")
                .build();

        mockMvc.perform(post("/common/code")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(regist)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("code").value(SUCCESS.getCode()))
                .andDo(print());
    }

    @Test
    @DisplayName("공통 메인 코드 수정")
    void commonCodeModification() throws Exception {
        // 공통코드ID
        Integer id = 1;

        CommonCode commonCode = CommonCode.builder()
                .build();

        mockMvc.perform(patch("/common/code/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(commonCode)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("code").value(SUCCESS.getCode()))
                .andDo(print());
    }

    @Test
    @DisplayName("공통 메인 코드 삭제")
    void commonCodeDeletion() throws Exception {
        int deleteId = 100;
        mockMvc.perform(delete("/common/code/{id}", deleteId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("code").value(SUCCESS.getCode()))
                .andDo(print());
    }
}
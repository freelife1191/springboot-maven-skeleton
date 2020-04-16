package com.project.component.code.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.common.BaseTest;
import com.project.component.code.packet.ReqCommonDetailCodeMod;
import com.project.component.code.packet.ReqCommonDetailCodeRegistOne;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 공통 상세 코드 테스트 코드
 * Created by KMS on 27/03/2020.
 */
class CommonDetailCodeControllerTest extends BaseTest {

    public CommonDetailCodeControllerTest(ObjectMapper mapper, WebApplicationContext ctx) {
        super(mapper, ctx);
    }

    @Test
    @DisplayName("공통 상세 코드 조회")
    void getCommonDetailCode() throws Exception {
        mockMvc.perform(get("/common/code/detail"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("code").value(100))
                .andDo(print());
    }

    @Test
    @DisplayName("공통 상세 코드 등록")
    void commonDetailCodeRegistration() throws Exception {
        ReqCommonDetailCodeRegistOne regist = ReqCommonDetailCodeRegistOne.builder()
                .build();

        mockMvc.perform(post("/common/code/detail")
                .content(mapper.writeValueAsString(regist)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("code").value(100))
                .andDo(print());
    }

    @Test
    @DisplayName("공통 상세 코드 수정")
    void commonDetailCodeModification() throws Exception {
        ReqCommonDetailCodeMod mod = ReqCommonDetailCodeMod.builder()
                .build();

        mockMvc.perform(patch("/common/code/detail")
                .content(mapper.writeValueAsString(mod)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("code").value(100))
                .andDo(print());
    }

    @Test
    @DisplayName("공통 상세 코드 삭제")
    void commonCodeDeletion() throws Exception {
        int id = 100;
        int detailCode = 1;

        mockMvc.perform(delete("/common/code/{id}/detail/{code}", id, detailCode))
                .andExpect(status().isOk())
                .andExpect(jsonPath("code").value(100))
                .andDo(print());
    }
}
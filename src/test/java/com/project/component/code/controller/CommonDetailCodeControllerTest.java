package com.project.component.code.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.project.common.BaseTest;
import com.project.component.code.packet.ReqCommonDetailCodeGET;
import com.project.component.code.packet.ReqCommonDetailCodeMod;
import com.project.component.code.packet.ReqCommonDetailCodeRegistMulti;
import com.project.component.code.packet.ReqCommonDetailCodeRegistOne;
import com.project.utils.common.TestUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static com.project.common.constant.ResCode.SUCCESS;
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
        ReqCommonDetailCodeGET reqCommonDetailCodeGET = ReqCommonDetailCodeGET.builder()
                .commonCodeId(1)
                .detailCode(1)
                .build();
        mockMvc.perform(get("/common/code/detail")
                .queryParams(TestUtils.objectToMultiValueMap(reqCommonDetailCodeGET)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("code").value(SUCCESS.getCode()))
                .andDo(print());
    }

    @Test
    @DisplayName("공통 상세 코드 단건 등록")
    void commonDetailCodeRegistration() throws Exception {
        Integer commonCodeId = 1;

        ReqCommonDetailCodeRegistOne regist = ReqCommonDetailCodeRegistOne.builder()
                .detailCodeNm("상세 테스트 단건")
                .detailCodeEngNm("DETAIL TEST ONE")
                .detailCodeDc("상세 테스트 단건 설명")
                .build();

        mockMvc.perform(post("/common/code/{common_code_id}/detail", commonCodeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(regist)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("code").value(SUCCESS.getCode()))
                .andDo(print());
    }

    @Test
    @DisplayName("공통 상세 코드 다중 등록")
    void commonDetailCodeMultiRegistration() throws Exception {
        Integer commonCodeId = 1;

        List<ReqCommonDetailCodeRegistMulti> registList = Lists.newArrayList(
                ReqCommonDetailCodeRegistMulti.builder()
                        .detailCodeNm("상세 테스트 다중")
                        .detailCodeEngNm("DETAIL TEST MULTI")
                        .detailCodeDc("상세 테스트 다중 설명")
                        .build()
        );

        mockMvc.perform(post("/common/code/{common_code_id}/details", commonCodeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(registList)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("code").value(SUCCESS.getCode()))
                .andDo(print());
    }

    @Test
    @DisplayName("공통 상세 코드 수정")
    void commonDetailCodeModification() throws Exception {
        Integer commonCodeId = 1;

        ReqCommonDetailCodeMod mod = ReqCommonDetailCodeMod.builder()
                .build();

        mockMvc.perform(patch("/common/code/{common_code_id}/detail", commonCodeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(mod)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("code").value(SUCCESS.getCode()))
                .andDo(print());
    }

    @Test
    @DisplayName("공통 상세 코드 삭제")
    void commonCodeDeletion() throws Exception {
        int commonCodeId = 1;
        int detailCode = 1;

        mockMvc.perform(delete("/common/code/{common_code_id}/detail/{code}", commonCodeId, detailCode))
                .andExpect(status().isOk())
                .andExpect(jsonPath("code").value(SUCCESS.getCode()))
                .andDo(print());
    }
}
package com.project.api.sample.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.api.sample.constant.Hobby;
import com.project.api.sample.model.Address;
import com.project.api.sample.packet.ReqGetSample;
import com.project.api.sample.packet.ReqJsonSample;
import com.project.common.BaseControllerTest;
import com.project.utils.common.TestUtils;
import org.junit.jupiter.api.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.StopWatch;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasLength;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 샘플 테스트 코드
 * BaseControllerTest를 상속 받으면 미리 설정해둔 어노테이션들이 자동으로 셋팅됨
 * Created by KMS on 11/03/2020.
 */
class SampleApiControllerTest extends BaseControllerTest {

    /**
     * 상속 클래스의 생성자 생성
     * @param mapper
     * @param mockMvc
     * @param ctx
     */
    public SampleApiControllerTest(ObjectMapper mapper, MockMvc mockMvc, WebApplicationContext ctx) {
        super(mapper, mockMvc, ctx);
    }

    private ReqGetSample reqGetSample;
    private ReqJsonSample reqJsonSample;

    /**
     * 테스트 시작전 최초 1회 수행
     */
    @BeforeAll
    static void start() {
        // 종합 테스트 성능 측정 상속 클래스에 protected 로 지정한 totalStopWatch 사용
        totalStopWatch = new StopWatch("API Controller Total Tests");
    }

    /**
     * 모든 테스트 수행 시작시마다 반복적으로 수행됨
     */
    @BeforeEach
    void setData() {
        // 개별 테스트 성능 측정
        stopWatch = new StopWatch("API Controller Tests");
        stopWatch.start();

        Address address = Address.builder()
                .country("ameria")
                .state("si")
                .city("seoul")
                .zipCode("12345")
                .build();

        reqGetSample = ReqGetSample.builder()
                .myId("id")
                .myName("name")
                .myAge(18)
                .myEmail("abc@choco.com")
                .myJob("Developer")
                .hobby(Hobby.MOVIE)
                .myAddress(address)
                .build();

        reqJsonSample = ReqJsonSample.builder()
                .myId("id")
                .myName("name")
                .myAge(18)
                .myEmail("abc@choco.com")
                .myJob("Developer")
                .myAddress(address)
                .build();

    }

    @Test
    @Order(1)
    @DisplayName("GET - CALL 테스트")
    void call() throws Exception {
        totalStopWatch.start("GET - CALL 테스트");
        MvcResult result = mockMvc.perform(get("/call"))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
        String content = result.getResponse().getContentAsString();
        assertThat(content).isEqualTo("ok");
    }

    @Test
    @Order(2)
    @DisplayName("GET - 서버 접속 테스트")
    void test1() throws Exception {
        totalStopWatch.start("GET - 서버 접속 테스트");
        mockMvc.perform(get("/test")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("code").value(100))
                .andExpect(jsonPath("result").isArray())
                .andExpect(jsonPath("result[0]").value(1))
                .andExpect(jsonPath("result[1]").value(2))
                .andExpect(jsonPath("result[2]").value(3))
                .andExpect(jsonPath("result[0]").exists())
                .andDo(print());
    }

    @Test
    @Order(3)
    @DisplayName("GET - 에러 테스트")
    void error() throws Exception {
        totalStopWatch.start("GET - 에러 테스트");
        mockMvc.perform(get("/err"))
                .andExpect(status().isOk())
                .andExpect(header().exists(HttpHeaders.VARY)) // HEADER에 Location 있는지 확인
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE)) //Content-Type 값 확인
                .andExpect(jsonPath("code").value(500))
                .andDo(print());
    }

    @Test
    @Order(4)
    @DisplayName("GET - Object 파라메터 요청 테스트")
    void sampleGet() throws Exception {
        totalStopWatch.start("GET - Object 파라메터 요청 테스트");
        mockMvc.perform(get("/sample")
                .params(TestUtils.objectToMultiValueMap(reqGetSample)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("code").value(100))
                .andExpect(jsonPath("result").isMap())
                .andExpect(jsonPath("result").isNotEmpty())
                .andExpect(jsonPath("result.seq").isEmpty())
                .andExpect(jsonPath("result.myId").exists())
                .andExpect(jsonPath("result.myId").isString())
                .andExpect(jsonPath("result.myId").value("id"))
                .andExpect(jsonPath("result.myName").exists())
                .andExpect(jsonPath("result.myName", is("name")))
                .andExpect(jsonPath("result.myName", hasLength(4)))
                .andExpect(jsonPath("result.myName").value("name"))
                .andExpect(jsonPath("result.myAge").exists())
                .andExpect(jsonPath("result.myAge").isNumber())
                .andExpect(jsonPath("result.myEmail").exists())
                .andExpect(jsonPath("result.myEmail", hasLength(13)))
                .andExpect(jsonPath("result.myEmail").value("abc@choco.com"))
                .andExpect(jsonPath("result.myJob").exists())
                .andExpect(jsonPath("result.myJob").value("Developer"))
                .andExpect(jsonPath("result.hobby").exists())
                .andExpect(jsonPath("result.hobby").value(Hobby.MOVIE.name()))
                .andExpect(jsonPath("result.myAddress.country").exists())
                .andExpect(jsonPath("result.myAddress.country").value("ameria"))
                .andExpect(jsonPath("result.myAddress.state").exists())
                .andExpect(jsonPath("result.myAddress.state").value("si"))
                .andExpect(jsonPath("result.myAddress.city").exists())
                .andExpect(jsonPath("result.myAddress.city").value("seoul"))
                .andExpect(jsonPath("result.myAddress.zip_code").exists())
                .andExpect(jsonPath("result.myAddress.zip_code").value(12345))
                .andDo(print());
    }

    @Test
    @Order(5)
    @DisplayName("GET - Object PathVariable 파라메터 요청 테스트")
    void sampleGetPathVariable() throws Exception {
        totalStopWatch.start("GET - Object PathVariable 파라메터 요청 테스트");
        mockMvc.perform(get("/sample/{seq}", 1)
                .params(TestUtils.objectToMultiValueMap(reqGetSample)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("code").value(100))
                .andExpect(jsonPath("result").isMap())
                .andExpect(jsonPath("result").isNotEmpty())
                .andExpect(jsonPath("result.seq").exists())
                .andExpect(jsonPath("result.seq").isNumber())
                .andExpect(jsonPath("result.seq").value(1))
                .andDo(print());
    }

    @Test
    @Order(6)
    @DisplayName("GET - Category Object PathVariable 파라메터 요청 테스트")
    void sampleGetCategoryPathVariable() throws Exception {
        totalStopWatch.start("GET - Category Object PathVariable 파라메터 요청 테스트");
        mockMvc.perform(get("/sample/hobby/{hobby}", Hobby.GAME.name())
                .params(TestUtils.objectToMultiValueMap(reqGetSample)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("code").value(100))
                .andExpect(jsonPath("result").isMap())
                .andExpect(jsonPath("result").isNotEmpty())
                .andExpect(jsonPath("result.hobby").exists())
                .andExpect(jsonPath("result.hobby").isString())
                .andExpect(jsonPath("result.hobby").value(Hobby.GAME.name()))
                .andExpect(jsonPath("result.hobby", hasLength(4)))
                .andDo(print());
    }

    @Test
    @Order(7)
    @DisplayName("POST - @RequestBody String 파라메터 테스트")
    void bodyString() throws Exception {
        totalStopWatch.start("POST - @RequestBody String 파라메터 테스트");
        mockMvc.perform(post("/sample/body-string")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(reqJsonSample)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @Order(8)
    @DisplayName("POST - @RequestBody Object 파라메터 테스트")
    void bodyObject() throws Exception {
        totalStopWatch.start("POST - @RequestBody Object 파라메터 테스트");
        mockMvc.perform(post("/sample/body-object")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(reqJsonSample)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @Order(9)
    @DisplayName("PATCH - @RequestBody Object PathVariable 파라메터 테스트")
    void patchBodyObjectPathvariable() throws Exception {
        totalStopWatch.start("PATCH - @RequestBody Object PathVariable 파라메터 테스트");
        mockMvc.perform(patch("/sample/pathvariable-object/{seq}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(reqJsonSample)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @Order(10)
    @DisplayName("DELETE - @RequestBody Object PathVariable 파라메터 테스트")
    void deleteObjectPathVariable() throws Exception {
        totalStopWatch.start("DELETE - @RequestBody Object PathVariable 파라메터 테스트");
        mockMvc.perform(delete("/sample/pathvariable-object/{seq}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .params(TestUtils.objectToMultiValueMap(reqGetSample)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    /**
     * 모든 테스트 수행 종료시마다 반복적으로 수행됨
     */
    @AfterEach
    void stop() {
        stopWatch.stop();
        System.out.println("===================================================");
        System.out.println("개별 테스트 성능 측정 결과");
        System.out.println("===================================================");
        System.out.println("Time Seconds = "+stopWatch.getTotalTimeSeconds()+"s");
        System.out.println("Time Millis = "+stopWatch.getTotalTimeMillis()+"ms");
        System.out.println("Time Nanos = "+stopWatch.getTotalTimeNanos()+"ns");
        //System.out.println(stopWatch.shortSummary());
        System.out.println(stopWatch.prettyPrint());
        System.out.println("===================================================");

        totalStopWatch.stop();
    }

    /**
     * 모든 테스트 종료시 1회 수행
     */
    @AfterAll
    static void end() {
        System.out.println("===================================================");
        System.out.println("종합 테스트 성능 측정 결과");
        System.out.println("===================================================");
        System.out.println("Total Time Seconds = "+totalStopWatch.getTotalTimeSeconds()+"s");
        System.out.println("Total Time Millis = "+totalStopWatch.getTotalTimeMillis()+"ms");
        System.out.println("Total Time Nanos = "+totalStopWatch.getTotalTimeNanos()+"ns");
        //System.out.println(stopWatch.shortSummary());
        System.out.println(totalStopWatch.prettyPrint());
        System.out.println("===================================================");
    }

}
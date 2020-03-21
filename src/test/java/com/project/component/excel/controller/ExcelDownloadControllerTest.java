package com.project.component.excel.controller;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Created by KMS on 05/09/2019.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("dev")
public class ExcelDownloadControllerTest {
/*

    @Autowired
    JooqTestRepository dao;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    ExcelConverter mapper;

    @Autowired
    MockMvc mockMvc;

    private StopWatch stopWatch;

    @Before
    public void setUp() {
        stopWatch = new StopWatch("excelDownload");
    }

    @Test
    public void 엑셀_다운로드_오브젝트컨버팅_테스트() {
        List<SerialNumberHistory> list = dao.getAllSerialNumber();
//        List<Settle> list = dao.getAllSettle();
        Map<String, Object> resultMap = mapper.convertList(list);
        System.out.println(resultMap);

    }

    @Test
    public void 엑셀_다운로드_맵컨버팅_테스트() {
        List<Map<String, Object>> list = dao.getAllSerialNumberMap();
        list.forEach(i -> System.out.println(i));

//        Map<String, Object> resultMap = mapper.makeExcelData(list);
//        System.out.println(list);

    }

    @Test
    public void 엑셀_다운로드_서비스_테스트() throws Exception {

        List<SerialNumberHistory> list = dao.getAllSerialNumber();

        Map<String, Object> dataList = mapper.convertList(list);
        System.out.println(dataList);

    }

    @Test
    @Ignore
    public void 엑셀_다운로드_컨트롤러_성능테스트() throws Exception {
        stopWatch.start();
//        mockMvc.perform(get("/v2/tutorial/exceldownload/settle"))
        mockMvc.perform(get("/v2/tutorial/exceldownload/serial-number-history"))
                .andExpect(status().isOk());
//                .andDo(print());
        stopWatch.stop();
        System.out.println(stopWatch.shortSummary());
        System.out.println(stopWatch.getTotalTimeMillis());
        System.out.println(stopWatch.prettyPrint());
    }
*/

}
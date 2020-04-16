package com.project.component.excel.tutorial.controller;

/**
 * Created by KMS on 30/08/2019.
 * 공통 엑셀 다운로드 테스트 컨트롤러
 */

// @Slf4j
// @RequiredArgsConstructor
// @RestController
// @RequestMapping("/v2/tutorial/exceldownload")
public class PoiDownloadTutorialController {
    /*
    @Autowired
    FooService fooService;

    @Autowired
    JooqTestRepository repository;

    @Autowired
    ExcelConverter excelMapper;
    */

    /**
     * ResponseEntity 리턴 방식 엑셀 다운로드 컨트롤러 튜토리얼
     * @param fileName
     * @return
     */
    /*
    @GetMapping("/export")
    public ResponseEntity<Resource> export(@RequestParam("fileName") String fileName) {

        ByteArrayResource resource = null;
        ByteArrayInputStream in = null;
        try {
            in = fooService.getExportInputStream(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok()
//                .contentLength(resource.contentLength())
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .cacheControl(CacheControl.noCache())
                .header("Content-Disposition", "attachment; filename=users.xlsx")
                .body(new InputStreamResource(in));
    }
    */

    /**
     * XLS 다운로드 튜토리얼 컨트롤러
     * @return
     */
    /*
    @GetMapping("excel-xls")
    public ModelAndView xlsView() {
        return new ModelAndView("excelXlsView", initExcelData());
    }
    */

    /**
     * XLSX 다운로드 튜토리얼 컨트롤러
     * @return
     */
    /*
    @GetMapping("excel-xlsx")
    public ModelAndView xlsxView() {
        return new ModelAndView("excelXlsxView", initExcelData());
    }
    */

    /**
     * XLSX-STREAMING 다운로드 튜토리얼 컨트롤러
     * @return
     */
    /*
    @GetMapping("excel-xlsx-streaming")
    public ModelAndView xlsxStreamingView() {
        return new ModelAndView("excelXlsxStreamingView", initExcelData());
    }
    */

    /**
     * 테스트 DB 조회 테스트
     * @param req
     * @return
     */
    /*
    @GetMapping("serial-number-history")
    public ModelAndView serialNumberHistory(@ModelAttribute ReqParam req) {
        return new ModelAndView("excelXlsxStreamingView", excelMapper.convertList(repository.getAllSerialNumber(), req) );
    }
    */

    /**
     * 테스트 DB 조회 테스트
     * @param req
     * @return
     */
    /*
    @GetMapping(value = "serial-number-history-body")
    public ModelAndView serialNumberHistoryBody(@RequestBody ReqParam req) {
        return new ModelAndView("excelXlsxStreamingView", excelMapper.convertList(repository.getAllSerialNumber(), req) );
    }
    */

    /**
     * calculate_v2 settle 대용량 다운로드 테스트 컨트롤러
     * @return
     */
    /*
    @GetMapping("settle")
    public ModelAndView settle() {
        return new ModelAndView("excelXlsxStreamingView", excelMapper.convertList(repository.getAllSettle()) );
    }

    private Map<String, Object> initExcelData() {
        Map<String, Object> map = new HashMap<>();
        map.put(ExcelConstant.FILE_NAME, "default_excel");
        map.put(ExcelConstant.TITLE, "TITLE");
        map.put(ExcelConstant.HEADER, Arrays.asList("ID", "NAME", "COMMENT"));
        map.put(ExcelConstant.BODY,
                Arrays.asList(
                        Arrays.asList("A", "a", "가"),
                        Arrays.asList("B", "b", "나"),
                        Arrays.asList("C", "c", "다")
                ));
        return map;
    }
    */

}

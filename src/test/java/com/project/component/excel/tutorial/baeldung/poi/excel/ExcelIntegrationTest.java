package com.project.component.excel.tutorial.baeldung.poi.excel;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created by KMS on 29/08/2019.
 * 컨텐츠를 temp.xlsx 파일에 쓰는 JUnit 테스트에서 위의 메소드를 테스트 한 다음
 * 동일한 파일을 읽고 작성된 테스트가 포함되어 있는지 확인
 */
public class ExcelIntegrationTest {

    private ExcelPOIHelper excelPOIHelper;
    private static String FILE_NAME = "temp.xlsx";
    private String fileLocation;

    @BeforeEach
    public void generateExcelFile() throws IOException {

        // Path dirpath = Paths.get("src", "test", "resources",".");
        // File currDir = new File(dirpath.toString());
        File currDir = new File(".");


        String path = currDir.getAbsolutePath();
        System.out.println("path = "+path);
        fileLocation = path.substring(0, path.length() - 1) + FILE_NAME;
        System.out.println("fileLocation = "+fileLocation);
        excelPOIHelper = new ExcelPOIHelper();
        excelPOIHelper.writeExcel();

    }

    @Test
    public void whenParsingPOIExcelFile_thenCorrect() throws IOException {
        Map<Integer, List<String>> data = excelPOIHelper.readExcel(fileLocation);

        assertEquals("Name", data.get(0)
                .get(0));
        assertEquals("Age", data.get(0)
                .get(1));

        assertEquals("John Smith", data.get(1)
                .get(0));
        assertEquals("20", data.get(1)
                .get(1));
    }

    @AfterEach
    public void cleanup(){
        File testFile = new File(fileLocation);
        if (testFile.exists()) {
            testFile.delete();
        }
    }
}
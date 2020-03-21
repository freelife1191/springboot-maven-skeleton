package com.project.component.excel;

import com.project.api.sample.dto.SampleExcelDto;
import com.project.component.excel.service.ExcelReader;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Created by KMS on 02/09/2019.
 */
public class ExcelReaderTest {

    @Test
    @DisplayName("엑셀 데이터 읽어서 오브젝트에 담기 테스트")
    public void getObjectListTest() throws IOException, InvalidFormatException, IllegalAccessException {
        String fileName = "dev_doc.xlsx";
        Path filePath = Paths.get("src", "test", "resources",File.separatorChar + fileName);
        System.out.println("## filePath = "+filePath);

        String originalFileName = fileName;
        String contentType = "";

        byte[] content = Files.readAllBytes(filePath);

        MockMultipartFile multipartFile = new MockMultipartFile(fileName, originalFileName, contentType, content);

        List<SampleExcelDto> products = ExcelReader.getObjectList(multipartFile, SampleExcelDto::from, 2);
        products.forEach(data -> System.out.println("## data = "+data));
    }

}
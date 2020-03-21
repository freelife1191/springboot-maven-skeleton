package com.project.component.excel.tutorial.baeldung.jexcel;

import jxl.read.biff.BiffException;
import jxl.write.WriteException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Created by KMS on 29/08/2019.
 */
public class JExcelIntegrationTest {

    private JExcelHelper jExcelHelper;
    private static String FILE_NAME = "temp.xls";
    private String fileLocation;

    @Before
    public void generateExcelFile() throws IOException, WriteException {

        /**
         * File 함수
         * getPath(): File 객체를 생성할 때 넣어준 경로를 그대로 반환
         * getAbsolutePath(): 현재 경로의 뒤에 내가 넣은 경로가 붙은 형태
         * getCanonicalPath: 실제로 그 상위 경로 기호("..")가 없어진 경로가 반환
         */
        Path path = Paths.get("src", "test", "resources");
//        Path path = Paths.get("./");

//        File currDir = new File("/Users/freelife/dev_file/download/.");
//        File file = new File("./");
//        System.out.println("# getPath: " + file.getPath());
//        System.out.println("# getAbsolutePath: " + file.getAbsolutePath());
//        System.out.println("# getCanonicalPath: " + file.getCanonicalPath());
//        String path = currDir.getAbsolutePath();
//        fileLocation = path.substring(0, path.length() - 1) + FILE_NAME;
        fileLocation = path + File.separator + FILE_NAME;
        System.out.println("# fileLocation = "+fileLocation);

        jExcelHelper = new JExcelHelper();
        jExcelHelper.writeJExcel(fileLocation);

    }

    @Test
    public void whenParsingJExcelFile_thenCorrect() throws IOException, BiffException {
        Map<Integer, List<String>> data = jExcelHelper.readJExcel(fileLocation);

        assertEquals("Name", data.get(0)
                .get(0));
        assertEquals("Age", data.get(0)
                .get(1));

        assertEquals("John Smith", data.get(2)
                .get(0));
        assertEquals("20", data.get(2)
                .get(1));
    }
    @After
    public void cleanup(){
        File testFile = new File(fileLocation);
        if (testFile.exists()) {
            testFile.delete();
        }
    }
}
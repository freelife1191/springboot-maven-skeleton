package com.project.component.excel.utils;


import com.github.drapostolos.typeparser.TypeParser;
import com.github.drapostolos.typeparser.TypeParserException;
import com.project.component.excel.constant.ExcelReaderFieldError;
import com.project.component.excel.model.ExcelReaderErrorField;
import com.project.component.excel.service.ExcelReader;
import eu.bitwalker.useragentutils.Browser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.poi.ss.formula.eval.ErrorEval;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

/**
 * 공통 엑셀 컴포넌트 유틸
 * Created by KMS on 26/09/2019.
 */
@Slf4j
public class ExcelUtils {

    public static String getBrowser(HttpServletRequest request) {
        String header = request.getHeader("User-Agent");
        log.info("User-Agent = {}",header);
        if(header != null) {
            if (header.contains("MSIE") || header.contains("Trident")) {
                return "MSIE";
            } else if (header.contains("Chrome")) {
                return "Chrome";
            } else if (header.contains("Opera")) {
                return "Opera";
            } else if (header.contains("Trident/7.0")) { //IE 11 이상 //IE 버전 별 체크 >> Trident/6.0(IE 10) , Trident/5.0(IE 9) , Trident/4.0(IE 8)
                return "MSIE";
            } else {
                return "Firefox";
            }
        } else {
            return "MSIE";
        }
    }

    public static String getDisposition(String filename, String browser) throws Exception {
        String encodedFilename = null;
        if (browser.equals("MSIE")) {
            encodedFilename = URLEncoder.encode(filename, StandardCharsets.UTF_8);
            //encodedFilename = URLEncoder.encode(filename, "UTF-8").replaceAll("\\+", "%20");
        }
        else if (browser.equals("Firefox")) {
            //encodedFilename = "\"" + new String(filename.getBytes("UTF-8"), "8859_1") + "\"";
            encodedFilename = new String(filename.getBytes(StandardCharsets.UTF_8), "8859_1");
        } else if (browser.equals("Opera")) {
            //encodedFilename = "\"" + new String(filename.getBytes("UTF-8"), "8859_1") + "\"";
            encodedFilename = new String(filename.getBytes(StandardCharsets.UTF_8), "8859_1");
        }
        else if (browser.equals("Chrome")) {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < filename.length(); i++) {
                char c = filename.charAt(i);
                if (c > '~') {
                    sb.append(URLEncoder.encode("" + c, "UTF-8"));
                } else {
                    sb.append(c); } } encodedFilename = sb.toString();
        }
        else {
            encodedFilename = new String(filename.getBytes(StandardCharsets.UTF_8), "8859_1");
            //throw new RuntimeException("Not supported browser");
        }
        return encodedFilename;
    }

    /**
     * 파일명 Encoder
     */
    private enum FileNameEncoder {
        IE(Browser.IE, it -> {
            try {
                return URLEncoder.encode(it, StandardCharsets.UTF_8.name()).replaceAll("\\+", "%20");
            } catch (UnsupportedEncodingException e) {
                return it;
            }
        }),
        FIREFOX(Browser.FIREFOX, getDefaultEncodeOperator()),
        OPERA(Browser.OPERA, getDefaultEncodeOperator()),
        CHROME(Browser.CHROME, getDefaultEncodeOperator()),
        UNKNOWN(Browser.UNKNOWN, UnaryOperator.identity());

        private final Browser browser;
        private UnaryOperator<String> encodeOperator;

        private static final Map<Browser, Function<String, String>> FILE_NAME_ENCODER_MAP;

        static {
            FILE_NAME_ENCODER_MAP = EnumSet.allOf(FileNameEncoder.class).stream()
                    .collect(Collectors.toMap(FileNameEncoder::getBrowser, FileNameEncoder::getEncodeOperator));
        }

        FileNameEncoder(Browser browser, UnaryOperator<String> encodeOperator) {
            this.browser = browser;
            this.encodeOperator = encodeOperator;
        }

        protected Browser getBrowser() {
            return browser;
        }

        protected UnaryOperator<String> getEncodeOperator() {
            return encodeOperator;
        }

        private static UnaryOperator<String> getDefaultEncodeOperator() {
            return it -> new String(it.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
        }

        public static String encode(Browser browser, String fileName) {
            return FILE_NAME_ENCODER_MAP.get(browser).apply(fileName);
        }
    }

    /**
     * Cell 데이터를 Type 별로 체크 하여 String 데이터로 변환함
     * String 데이터로 우선 변환해야 함
     * @param cell 요청 엑셀 파일의 cell 데이터
     * @return String 형으로 변환된 cell 데이터
     */
    public static String getValue(Cell cell) {

        if(Objects.isNull(cell) || Objects.isNull(cell.getCellType()))
            return "";

        String value;

        // 셀 내용의 유형 판별
        switch (cell.getCellType()) {
            case STRING: // getRichStringCellValue() 메소드를 사용하여 컨텐츠를 읽음
                value = cell.getRichStringCellValue().getString();
                break;
            case NUMERIC: // 날짜 또는 숫자를 포함 할 수 있으며 아래와 같이 읽음
                if (DateUtil.isCellDateFormatted(cell))
                    value = cell.getLocalDateTimeCellValue().toString();
                else
                    value = String.valueOf(cell.getNumericCellValue());
                if (value.endsWith(".0"))
                    value = value.substring(0, value.length() - 2);
                break;
            case BOOLEAN:
                value = String.valueOf(cell.getBooleanCellValue());
                break;
            case FORMULA:
                value = String.valueOf(cell.getCellFormula());
                break;
            case ERROR:
                value = ErrorEval.getText(cell.getErrorCellValue());
                break;
            case BLANK:
            case _NONE:
            default:
                value = "";
        }
        //log.debug("## cellType = {}, value = {}",cell.getCellType(),value);
        return value;
    }

    /**
     * TypeParser 로 String으로 변환된 Cell 데이터를 객체 필드 타입에 맞게 변환하여 셋팅해줌
     * @param object 요청 객체
     * @param <T>
     * @param row 엑셀 ROW 데이터
     * @return Cell 데이터를 맵핑한 오브젝트
     */
    public static<T> T setObjectMapping(T object, Row row) {

        int i = 0;

        if(Objects.isNull(object)) return null;

        for (Field field : object.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            String cellValue = null;
            TypeParser typeParser = TypeParser.newBuilder().build();

            try {
                if( i  < row.getPhysicalNumberOfCells() ) { //유효한 Cell 영역 까지만
                    cellValue = ExcelUtils.getValue(row.getCell(i));
                    Object setData = null;
                    if(!StringUtils.isEmpty(cellValue))
                        setData = typeParser.parseType(cellValue, field.getType());
                    field.set(object, setData);
                    checkValidation(object, row, i, cellValue, field.getName());
                }
            } catch (TypeParserException e) {
                ExcelReaderFieldError error = ExcelReaderFieldError.TYPE;
                ExcelReader.errorFieldList.add(ExcelReaderErrorField.builder()
                        .type(error.name())
                        .row(row.getRowNum()+1)
                        .field(field.getName())
                        .fieldHeader(ExcelReader.headerList.get(i))
                        .inputData(cellValue)
                        .message(error.getMessage()+
                                "데이터 필드타입 - "+field.getType().getSimpleName()+
                                ", 입력값 필드타입 - "+cellValue.getClass().getSimpleName())
                        .exceptionMessage(ExceptionUtils.getRootCauseMessage(e))
                        .build());
            } catch (Exception e) {
                ExcelReaderFieldError error = ExcelReaderFieldError.UNKNOWN;
                ExcelReader.errorFieldList.add(ExcelReaderErrorField.builder()
                        .type(error.name())
                        .row(row.getRowNum()+1)
                        .field(field.getName())
                        .fieldHeader(ExcelReader.headerList.get(i))
                        .inputData(cellValue)
                        .message(error.getMessage())
                        .exceptionMessage(ExceptionUtils.getRootCauseMessage(e))
                        .build());
            }
            i++;

        }

        return object;
    }

    /**
     * 객체에 대한 Validation 을 검증해서 검증을 통과 하지 못한 내역이 있을 경우 에러 리스트에 담는다
     * @param object
     * @param row
     * @param i
     * @param <T>
     */
    private static<T> void checkValidation(T object, Row row, int i, String cellValue, String fieldName) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<T>> constraintValidations = validator.validate(object);
        ConstraintViolation<T> validData = constraintValidations.stream()
                .filter(data -> data.getPropertyPath().toString().equals(fieldName))
                .findFirst().orElse(null);

        if(Objects.isNull(validData)) return;

        String fieldHeader = ExcelReader.headerList.get(i);
        ExcelReaderFieldError error = ExcelReaderFieldError.VALID;
        String exceptionMessage = validData.getMessage();

        if(validData.getMessageTemplate().contains("NotEmpty") || validData.getMessageTemplate().contains("NotNull")) {
            error = ExcelReaderFieldError.EMPTY;
            exceptionMessage = fieldHeader+"은 필수 입력값입니다";
        }

        ExcelReader.errorFieldList.add(ExcelReaderErrorField.builder()
                .type(error.name())
                .row(row.getRowNum()+1)
                .field(validData.getPropertyPath().toString())
                .fieldHeader(fieldHeader)
                .inputData(cellValue)
                .message(error.getMessage())
                .exceptionMessage(exceptionMessage)
                .build());
    }
}
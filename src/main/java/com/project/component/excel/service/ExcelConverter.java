package com.project.component.excel.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.component.excel.constant.ExcelConstant;
import com.project.component.excel.domain.ReqExcelDownload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by KMS on 04/09/2019.
 * DB조회 데이터 및 요청 데이터 엑셀 데이터 형식 Converter
 */
@Slf4j
@Component
public class ExcelConverter {

    @Autowired
    ObjectMapper objectMapper;

    /**
     * 엑셀 데이터 생성 헤더 요청 값이 없을 떄
     * @param list
     * @param <E>
     * @return
     */
    public<E> Map<String, Object> convertList(List<E> list) {
        return convertList(list, null);
    }

    /**
     * 엑셀 데이터 생성 헤더 요청 값이 있을 때
     * @param list
     * @param reqData
     * @param <E>
     * @param <T>
     * @return
     */
    public<E, T extends ReqExcelDownload> Map<String, Object> convertList(List<E> list, T reqData) {

        Map<String, Object> resultMap = new HashMap<>();
        List<Map<String, Object>> dataMapList = new ArrayList();

        if (list == null || list.size() <= 0) {
            return null;
        }

        /* Body 셋팅 */
        //Map이면 Map 데이터 그대로 진행 오브젝트면 Map으로 변환
        if(list.get(0) instanceof Map){
            dataMapList = (List<Map<String, Object>>) list;
        } else {
            for (E obj : list) {
                // 객체 리스트 Map으로 컨버팅
                Map<String, Object> map = objectMapper.convertValue(obj, LinkedHashMap.class);
                dataMapList.add(map);
            }
        }
        List<List<String>> body = getBody(dataMapList);

        /* Header 셋팅 */
        List<String> header = new ArrayList<>();
        if(reqData != null && reqData.getHeader() != null && reqData.getHeader().length > 0){
            for(String str : reqData.getHeader()){
                log.debug("header : "+str);
                header.add(str);
            }
        }else {
            header = getDefaultHeader(dataMapList);
        }

        if(reqData == null) {
            reqData = (T) new ReqExcelDownload();
        }

        resultMap.put(ExcelConstant.TITLE, reqData.getTitle());
        resultMap.put(ExcelConstant.FILE_NAME, reqData.getFileName() != null ? reqData.getFileName() : "export");
        resultMap.put(ExcelConstant.HEADER, header);
        resultMap.put(ExcelConstant.BODY, body);
        resultMap.put(ExcelConstant.COLOMN_WIDTH, reqData.getColumnWidth());
        resultMap.put(ExcelConstant.STYLE, reqData.isStyle());
        resultMap.put(ExcelConstant.AUTO_SIZING, reqData.isAutoSize());

        return resultMap;
    }

    /**
     * 조회 결과 값 엑셀 데이터 헤더 형식으로 converting
     * @Method Name : convertMap
     * @param rows
     * @return
     * @throws Exception
     */
    private List<String> getDefaultHeader(List<Map<String, Object>> rows){

        List<String> resultList = new ArrayList<>();
        int[] i = {0};

        if (rows == null || rows.size() <= 0) {
            return null;
        }

        log.debug("ROW SIZE = "+rows.size());

        if (null == rows.get(0) || "NULL".equals((rows.get(0).keySet().iterator().next()))) {
            return null;
        }

        log.debug("ROW.get(0) SIZE = "+rows.get(0).size());
        rows.get(0).forEach((k,v)->{

            if (k != null) {
//                log.debug("## KEYNAME : "+k+" | VALUE : "+rows.get(k)+" | class : "+rows.get(k).getClass().getName());
//                 log.info("NAME : "+rows.get(k).getClass().getName());

                 log.debug("HEADER [{}] KEY : {}, VALUE : {}",i,k,v);
                resultList.add(k);

            }
            i[0]++;

        });

        return resultList;
    }

    /**
     * 조회 결과 값 엑셀 데이터 바디 형식으로 converting
     * @Method Name : convertMap
     * @param rows
     * @return
     * @throws Exception
     */
    private List<List<String>> getBody(List<Map<String, Object>> rows) {
        List<List<String>> resultList = new ArrayList<>();

        if (rows == null || rows.size() <= 0) {
            return null;
        }

        log.debug("ROW SIZE = "+rows.size());

        if (null == rows.get(0) || "NULL".equals((rows.get(0).keySet().iterator().next()))) {
            return null;
        }

        int[] i = {0};
        for (Map<String, Object> row : rows) {
            List<String> dataList = new ArrayList<>();

            row.forEach((k,v)->{

                try {
                    if (k != null) {
//                        log.debug("## VALUE : "+row.get(k)+" | class : "+row.get(k).getClass().getName());
//                        log.debug("## KEYNAME : {}",k);
//                        log.debug("## KEYNAME : {} | VALUE : {} | class : {}",k,row.get(k),row.get(k).getClass().getName());
//                        log.info("NAME : "+row.get(k).getClass().getName());
                        //log.debug("LIST [{}] KEY : {}, VALUE : {}",i,k,v);
                        if(v != null){
                            dataList.add(String.valueOf(v));
                        }else {
                            dataList.add("");
                        }
                    } else
                        dataList.add("");
                } catch (Exception e) {
                    e.printStackTrace();
                }

            });

            resultList.add(dataList);
            i[0]++;
        }

        return resultList;
    }

}

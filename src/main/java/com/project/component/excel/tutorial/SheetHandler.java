package com.project.component.excel.tutorial;

import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.apache.poi.xssf.usermodel.XSSFComment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by KMS on 02/09/2019.
 * 메모리 사용량이 적고 빠르게 읽기 위한 Sax 방식 Handler
 * http://blog.naver.com/PostView.nhn?blogId=tmondev&logNo=221505398958
 */
public class SheetHandler implements XSSFSheetXMLHandler.SheetContentsHandler {

    //header를 제외한 데이터부분
    private List<List<String>> rows = new ArrayList<>();

    //cell 호출시마다 쌓아놓을 1 row List
    private List<String> row = new ArrayList<>();

    //Header 정보를 입력
    private List<String> header = new ArrayList<>();

    //빈 값을 체크하기 위해 사용할 셀번호
    private int currentCol = -1;

    //현재 읽고 있는 Cell의 Col
    private int currRowNum = 0;

    public void startRow(int rowNum) {
        //empty 값을 체크하기 위한 초기 셋팅값
        this.currentCol = -1;
        this.currRowNum = rowNum;
    }

    public void endRow(int rowNum) {
        if(rowNum ==0) {
            header = new ArrayList(row);
        } else {
            //헤더의 길이가 현재 로우보다 더 길다면 Cell의 뒷자리가 빈값임으로 해당값만큼 공백
            if(row.size() < header.size()) {
                for (int i = row.size(); i < header.size(); i++) {
                    row.add("");
                }
            }
            rows.add(new ArrayList(row));
        }
        row.clear();
    }

    public void cell(String columnName, String value, XSSFComment var3) {
        int iCol = (new CellReference(columnName)).getCol();
        int emptyCol = iCol - currentCol - 1;

        //읽은 Cell의 번호를 이용하여 빈Cell 자리에 빈값을 강제로 저장시켜줌
        for(int i = 0 ; i < emptyCol ; i++) {
            row.add("");
        }
        currentCol = iCol;
        row.add(value);
    }

    @Override
    public void headerFooter(String text, boolean isHeader, String tagName) {

    }

    @Override
    public void endSheet() {

    }
}

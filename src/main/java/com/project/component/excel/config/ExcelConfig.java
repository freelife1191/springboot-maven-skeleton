package com.project.component.excel.config;

import com.project.component.excel.view.ExcelXlsView;
import com.project.component.excel.view.ExcelXlsxStreamingView;
import com.project.component.excel.view.ExcelXlsxView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Created by KMS on 02/09/2019.
 * 컨트롤러에서 엑셀다운로드를 간편하게 하기 위한 설정
 */
@Configuration
public class ExcelConfig implements WebMvcConfigurer {

    @Autowired
    private ExcelXlsView excelXlsView;

    @Autowired
    private ExcelXlsxView excelXlsxView;

    @Autowired
    private ExcelXlsxStreamingView excelXlsxStreamingView;

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        registry.enableContentNegotiation(excelXlsxStreamingView, excelXlsxView, excelXlsView);
    }
}
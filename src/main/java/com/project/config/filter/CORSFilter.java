package com.project.config.filter;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by KMS on 27/03/2020.
 */
@Component
public class CORSFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) res;

        response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, PATCH, DELETE, HEAD, TRACE, OPTIONS");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "*");
        /*여기의 *을 내가 허용하고 싶은 특정 도메인으로 바꾸면 설정한 도메인에 한에서만 크로스 도메인을 허용하게된다. 여러 도메인의 경우 여러번 설정하면된다. */
        response.setHeader("Access-Control-Allow-Origin", "*");

        filterChain.doFilter(req, res);
    }

    @Override
    public void destroy() {

    }
}

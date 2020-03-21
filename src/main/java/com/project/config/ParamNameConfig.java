package com.project.config;

import com.project.common.annotation.ParamName;
import com.project.common.processor.ParamNameProcessor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * QueryString -> 객체 맵핑 설정
 */
@Configuration
public class ParamNameConfig {

    /**
     *  {@link ParamName} 설정 적용.
     *
     * @return ParamNameProcessor
     */
    @Bean
    protected ParamNameProcessor paramNameProcessor() {
        return new ParamNameProcessor();
    }

    /**
     * Custom {@link BeanPostProcessor} for adding {@link ParamNameProcessor} into the first of
     * {@link RequestMappingHandlerAdapter}.
     *
     * @return BeanPostProcessor
     */
    @Bean
    public BeanPostProcessor beanPostProcessor() {
        return new BeanPostProcessor() {

            @Override
            public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
                return bean;
            }

            @Override
            public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
                if (bean instanceof RequestMappingHandlerAdapter) {
                    RequestMappingHandlerAdapter adapter = (RequestMappingHandlerAdapter) bean;
                    List<HandlerMethodArgumentResolver> argumentResolvers = new ArrayList<>(adapter.getArgumentResolvers());
                    argumentResolvers.add(0, paramNameProcessor());
                    adapter.setArgumentResolvers(argumentResolvers);
                }
                return bean;
            }
        };
    }
}

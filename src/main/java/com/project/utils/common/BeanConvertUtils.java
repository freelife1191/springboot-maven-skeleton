package com.project.utils.common;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.PropertyUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * Created by KMS on 2019-04-26.
 */
@Slf4j
public class BeanConvertUtils {
    //map의 value을 bean에 넣어주는 메소드
    public static void mapToBean(Map properties, Object bean) {
        if (properties == null) {
            return;
        }

        try {
            BeanUtilsBean.getInstance().getConvertUtils().register(false, false, 0);
            BeanUtils.populate(bean, properties);
        } catch (IllegalAccessException | InvocationTargetException e) {
            log.error("BeanConvertUtils mapToBean error :: {}",e.getMessage());
        }

    }

    //bean의 value을 map에 넣어주는 메소드
    public static void beanToMap(Object bean, Map properties) {

        try {
            Map map = PropertyUtils.describe(bean);

            map.remove("class");
            properties.putAll(map);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            log.error("BeanConvertUtils beanToMap error :: {}",e.getMessage());
        }

    }
}
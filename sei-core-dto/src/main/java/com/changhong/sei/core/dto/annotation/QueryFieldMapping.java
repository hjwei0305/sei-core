package com.changhong.sei.core.dto.annotation;

import java.lang.annotation.*;

/**
 * 实现功能: 数据实体查询属性映射
 *
 * @author 王锦光 wangjg
 * @version 2020-03-26 10:41
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target({ ElementType.PARAMETER, ElementType.FIELD })
public @interface QueryFieldMapping {
    /**
     * 业务实体的全类名，默认为返回业务实体的类型全名
     * @return 业务实体的全类名
     */
    String value() default "";
}

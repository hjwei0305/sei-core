package com.changhong.sei.core.dto.annotation;

import java.lang.annotation.*;

/**
 * 实现功能: 在数据实体上启用数据历史记录
 *
 * @author 王锦光 wangjg
 * @version 2020-03-16 14:31
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target({ ElementType.PARAMETER, ElementType.TYPE })
public @interface EnableDataHistory {
    /**
     * 如果为true，则实体上启用变更日志
     * @return 启用
     */
    boolean enable() default true;

    /**
     * 业务实体名称
     * @return 业务实体名称
     */
    String name();
}

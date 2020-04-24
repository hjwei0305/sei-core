package com.changhong.sei.core.dto.annotation;

import java.lang.annotation.*;

/**
 * 实现功能: 属性需要记录变更日志
 *
 * @author 王锦光 wangjg
 * @version 2020-03-16 14:31
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target({ ElementType.PARAMETER, ElementType.FIELD })
public @interface DataHistory {
    /**
     * 属性描述
     * @return 属性描述
     */
    String name();
}

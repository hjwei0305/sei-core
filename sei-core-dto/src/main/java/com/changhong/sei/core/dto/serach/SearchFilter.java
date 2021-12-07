package com.changhong.sei.core.dto.serach;

import com.changhong.sei.util.ConverterUtils;

import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;

/**
 * *************************************************************************************************
 * <p/>
 * 实现功能：查询字段
 * <p>
 * ------------------------------------------------------------------------------------------------
 * 版本          变更时间             变更人                     变更原因
 * ------------------------------------------------------------------------------------------------
 * 1.0.00      2017/4/5 14:42      陈飞(fly)                  新建
 * <p/>
 * *************************************************************************************************
 */
public class SearchFilter implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final String EMPTY_VALUE = "EMPTY";
    public static final String NO_EMPTY_VALUE = "NOEMPTY";
    public static final String NULL_VALUE = "NULL";
    public static final String NO_NULL_VALUE = "NONULL";

    /**
     * 查询操作枚举
     */
    public enum Operator {
        /**
         * 等于 =
         */
        EQ,

        /**
         * 不等于 !=
         */
        NE,

        /**
         * 模糊查询  LIKE %abc%
         */
        LK,

        /**
         * 不包含 NOT LIKE %abc%
         */
        NC,

        /**
         * 左匹配  LIKE abc%
         */
        LLK,

        /**
         * 不以什么开头  NOT LIKE abc%
         */
        BN,

        /**
         * 右匹配  LIKE %abc
         */
        RLK,

        /**
         * 不以什么结尾  NOT LIKE %abc
         */
        EN,

        /**
         * BETWEEN  BETWEEN 1 AND 2
         */
        BT,

        /**
         * 大于  >
         */
        GT,

        /**
         * 小于  <
         */
        LT,

        /**
         * 大等于  >=
         */
        GE,

        /**
         * 小等于  <=
         */
        LE,

        /**
         * 在其中 IN ( )
         */
        IN,

        /**
         * 为空 IS NULL OR ==''
         */
        BK,

        /**
         * 不为空 IS NOT NULL AND !=''
         */
        NB,

        /**
         * 为NULL IS NULL
         */
        NU,

        /**
         * 不为NULL IS NOT NULL
         */
        NN,

        /**
         * 属性比较 属性1 < 属性2
         */
        PLT,

        /**
         * 属性比较 属性1 <= 属性2
         */
        PLE,

        /**
         * 不包含 NOT IN ()
         */
        NOTIN
    }

    /**
     * 查询字段名
     */
    private String fieldName;
    /**
     * 查询字段对应值
     */
    private Object value;
    /**
     * 查询操作
     */
    private Operator operator;

    /**
     * 字段类型
     */
    private String fieldType;

    public SearchFilter() {
    }

    public SearchFilter(String fieldName, Object value) {
        this(fieldName, value, Operator.EQ, null);
    }

    public SearchFilter(String fieldName, Object value, Operator operator) {
        this(fieldName, value, operator, null);
    }

    public SearchFilter(String fieldName, Object value, Operator operator, String fieldType) {
        this.fieldName = fieldName;
        this.value = value;
        this.operator = operator;
        this.fieldType = fieldType;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public Object getValue() {
        Object val = value;
        if (Objects.nonNull(val)) {
            if (!(val instanceof Collection) && !(val.getClass().isArray())) {
                String type = getFieldType();
                if (val.toString().length() > 0) {
                    val = ConverterUtils.convert(type, val);
                }
            }
        }
        return val;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Operator getOperator() {
        return operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SearchFilter that = (SearchFilter) o;
        return Objects.equals(fieldName, that.fieldName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fieldName);
    }
}

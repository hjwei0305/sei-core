package com.changhong.sei.core.log.support;

import java.io.Serializable;
import java.util.List;

/**
 * 方法信息
 */
public class MethodInfo implements Serializable {
    private static final long serialVersionUID = 1968651211575203564L;
    /**
     * 代表本地方法，不进行代码定位
     */
    public static final int NATIVE_LINE_NUMBER = -2;

    /**
     * 所在类全类名
     */
    private String classAllName;
    /**
     * 所在类简单类名
     */
    private String classSimpleName;
    /**
     * 方法名称
     */
    private String methodName;
    /**
     * 参数列表
     */
    private List<String> paramNames;
    /**
     * 方法行号
     */
    private Integer lineNumber;

    /**
     * 构造
     *
     * @param classAllName    所在类全类名
     * @param classSimpleName 所在类简单类名
     * @param methodName      方法名称
     * @param paramNames      参数列表
     * @param lineNumber      方法行号
     */
    MethodInfo(String classAllName, String classSimpleName, String methodName, List<String> paramNames, Integer lineNumber) {
        this.classAllName = classAllName;
        this.classSimpleName = classSimpleName;
        this.methodName = methodName;
        this.paramNames = paramNames;
        this.lineNumber = lineNumber;
    }

    public String getClassAllName() {
        return classAllName;
    }

    public void setClassAllName(String classAllName) {
        this.classAllName = classAllName;
    }

    public String getClassSimpleName() {
        return classSimpleName;
    }

    public void setClassSimpleName(String classSimpleName) {
        this.classSimpleName = classSimpleName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public List<String> getParamNames() {
        return paramNames;
    }

    public void setParamNames(List<String> paramNames) {
        this.paramNames = paramNames;
    }

    public Integer getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(Integer lineNumber) {
        this.lineNumber = lineNumber;
    }

    /**
     * 是否本地方法
     *
     * @return 返回布尔值
     */
    public boolean isNative() {
        return this.lineNumber == NATIVE_LINE_NUMBER;
    }
}

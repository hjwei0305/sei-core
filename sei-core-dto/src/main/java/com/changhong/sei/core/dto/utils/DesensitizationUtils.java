package com.changhong.sei.core.dto.utils;

import com.changhong.sei.core.dto.annotation.Desensitization;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;

/**
 * 实现功能：数据脱敏的工具类
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-07-14 09:07
 */
public final class DesensitizationUtils {
    /**
     * [中文姓名] 只显示第一个汉字，其他隐藏为2个星号<例子：李**>
     */
    public static String chineseName(final String fullName) {
        if (StringUtils.isBlank(fullName)) {
            return "";
        }
        final String name = StringUtils.left(fullName, 1);
        return StringUtils.rightPad(name, StringUtils.length(fullName), "*");
    }

    /**
     * [中文姓名] 只显示第一个汉字，其他隐藏为2个星号<例子：李**>
     */
    public static String chineseName(final String familyName, final String givenName) {
        if (StringUtils.isBlank(familyName) || StringUtils.isBlank(givenName)) {
            return "";
        }
        return chineseName(familyName + givenName);
    }

    /**
     * [身份证号] 身份证前二后四脱敏。共计18位或者15位。<例子：51***********5762>
     */
    public static String idCardNum(final String idCard) {
//        if (StringUtils.isBlank(idCard)) {
//            return "";
//        }
//
//        return StringUtils.left(idCard, 3).concat(StringUtils
//                .removeStart(StringUtils.leftPad(StringUtils.right(idCard, 3), StringUtils.length(idCard), "*"), "***"));
        if (StringUtils.isEmpty(idCard) || (idCard.length() < 8)) {
            return idCard;
        }
        return idCard.replaceAll("(?<=\\w{2})\\w(?=\\w{4})", "*");
    }

    /**
     * [固定电话] 后四位，其他隐藏<例子：****1234>
     */
    public static String fixedPhone(final String num) {
        if (StringUtils.isBlank(num)) {
            return "";
        }
        return StringUtils.leftPad(StringUtils.right(num, 4), StringUtils.length(num), "*");
    }

    /**
     * [手机号码] 前三位，后四位，其他隐藏<例子:138******1234>
     */
    public static String mobilePhone(final String mobile) {
        if (StringUtils.isBlank(mobile)) {
            return "";
        }
        return StringUtils.left(mobile, 2).concat(StringUtils.removeStart(
                StringUtils.leftPad(StringUtils.right(mobile, 2), StringUtils.length(mobile), "*"), "***"));
//        if (StringUtils.isEmpty(mobile) || (mobile.length() != 11)) {
//            return mobile;
//        }
//        return mobile.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
    }

    /**
     * [地址] 只显示到地区，不显示详细地址；我们要对个人信息增强保护<例子：北京市海淀区****>
     *
     * @param sensitiveSize 敏感信息长度
     */
    public static String address(final String address, final int sensitiveSize) {
        if (StringUtils.isBlank(address)) {
            return "";
        }
        final int length = StringUtils.length(address);
        return StringUtils.rightPad(StringUtils.left(address, length - sensitiveSize), length, "*");
    }

    /**
     * [电子邮箱] 邮箱前缀仅显示第一个字母，前缀其他隐藏，用星号代替，@及后面的地址显示<例子:g**@163.com>
     */
    public static String email(final String email) {
        if (StringUtils.isBlank(email)) {
            return "";
        }
        final int index = StringUtils.indexOf(email, "@");
        if (index <= 1) {
            return email;
        } else {
            return StringUtils.rightPad(StringUtils.left(email, 1), index, "*")
                    .concat(StringUtils.mid(email, index, StringUtils.length(email)));
        }
    }

    /**
     * [银行卡号] 前六位，后四位，其他用星号隐藏每位1个星号<例子:6222**********1234>
     */
    public static String bankCard(final String cardNum) {
        if (StringUtils.isBlank(cardNum)) {
            return "";
        }
        return StringUtils.left(cardNum, 4).concat(StringUtils.removeStart(
                StringUtils.leftPad(StringUtils.right(cardNum, 4), StringUtils.length(cardNum), "*"),
                "******"));
    }

    /**
     * [公司开户银行联号] 公司开户银行联行号,显示前两位，其他用星号隐藏，每位1个星号<例子:12********>
     */
    public static String cnapsCode(final String code) {
        if (StringUtils.isBlank(code)) {
            return "";
        }
        return StringUtils.rightPad(StringUtils.left(code, 2), StringUtils.length(code), "*");
    }

    /**
     * 只显示第一个汉字，其他隐藏为2个星号<例子：李**>
     *
     * @param sensitiveInfo 敏感信息
     * @param index         1 为第index位开始脱敏
     */
    public static String left(String sensitiveInfo, int index) {
        if (StringUtils.isBlank(sensitiveInfo)) {
            return "";
        }

        String name = StringUtils.left(sensitiveInfo, index);
        return StringUtils.rightPad(name, StringUtils.length(sensitiveInfo), "*");
    }

    /**
     * 110****58，前面保留3位明文，后面保留2位明文
     *
     * @param sensitiveInfo 敏感信息
     * @param index         3
     * @param end           2
     */
    public static String around(String sensitiveInfo, int index, int end) {
        if (StringUtils.isBlank(sensitiveInfo)) {
            return "";
        }
        return StringUtils.left(sensitiveInfo, index).concat(StringUtils.removeStart(StringUtils.leftPad(StringUtils.right(sensitiveInfo, end), StringUtils.length(sensitiveInfo), "*"), "***"));
    }

    /**
     * 后四位，其他隐藏<例子：****1234>
     *
     * @param sensitiveInfo 敏感信息
     */
    public static String right(String sensitiveInfo, int end) {
        if (StringUtils.isBlank(sensitiveInfo)) {
            return "";
        }
        return StringUtils.leftPad(StringUtils.right(sensitiveInfo, end), StringUtils.length(sensitiveInfo), "*");
    }

    /**
     * 护照前2后3位脱敏，护照一般为8或9位
     */
    public static String idPassport(String id) {
        if (StringUtils.isEmpty(id) || (id.length() < 8)) {
            return id;
        }
        return id.substring(0, 2) + new String(new char[id.length() - 5]).replace("\0", "*") + id.substring(id.length() - 3);
    }

    /**
     * 证件后几位脱敏
     */
    public static String idPassport(String id, int sensitiveSize) {
        if (StringUtils.isBlank(id)) {
            return "";
        }
        int length = StringUtils.length(id);
        return StringUtils.rightPad(StringUtils.left(id, length - sensitiveSize), length, "*");
    }

    ////////////////////////////////

    /**
     * 脱敏数据
     */
    public static <T> List<T> desensitization(List<T> list) {
        for (T t : list) {
            desensitization(t);
        }
        return list;
    }

    /**
     * 脱敏数据
     */
    public static <T> T desensitization(T o) {
        try {
            Class<? extends Object> c = o.getClass();
            // 获取所有属性值
            Field[] fields = c.getDeclaredFields();
            Object value = null;
            for (Field f : fields) {
                f.setAccessible(true);
                // 获取属性上的注解
                Desensitization annotation = f.getAnnotation(Desensitization.class);
                //取出对象的属性值
                value = f.get(o);
                // 有此类注解 同时 当前遍历的属性值不能为空
                if (annotation != null && String.class.equals(f.getGenericType()) && Objects.nonNull(value)) {
                    //注：经测试只有String类型才可以脱敏其他类型加了脱敏注解也无法脱敏
                    Desensitization.DesensitizationType type = annotation.value();
                    // 判断注解类型
                    switch (type) {
                        case CHINESE_NAME:
                            // 用户名
                            f.set(o, DesensitizationUtils.chineseName(value.toString()));
                            break;
                        case ID_CARD:
                            // 身份证号
                            f.set(o, DesensitizationUtils.idCardNum(value.toString()));
                            break;
                        case ID_PASSPORT:
                            // 护照号
                            f.set(o, DesensitizationUtils.idPassport(value.toString()));
                            break;
                        case FIXED_PHONE:
                            // 座机号
                            f.set(o, DesensitizationUtils.fixedPhone(value.toString()));
                            break;
                        case MOBILE_PHONE:
                            // 手机号
                            f.set(o, DesensitizationUtils.mobilePhone(value.toString()));
                            break;
                        case ADDRESS:
                            // 地址
                            f.set(o, DesensitizationUtils.address(value.toString(), 6));
                            break;
                        case EMAIL:
                            // 电子邮件
                            f.set(o, DesensitizationUtils.email(value.toString()));
                            break;
                        case BANK_CARD:
                            // 银行卡
                            f.set(o, DesensitizationUtils.bankCard(value.toString()));
                            break;
                        case CNAPS_CODE:
                            // 公司开户银行联号
                            f.set(o, DesensitizationUtils.cnapsCode(value.toString()));
                            break;
                        case OTHER:
                        default:
                            break;
                    }
                }
            }
            return o;
        } catch (Exception e) {
            return o;
        }
    }
}

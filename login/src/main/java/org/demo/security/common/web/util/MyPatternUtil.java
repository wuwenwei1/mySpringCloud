package org.demo.security.common.web.util;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MyPatternUtil {
    public static void main(String[] args) throws IOException {
    }

    /**
     * 三级菜单id验证器。
     */
    public static boolean menuId(String yyyyMMdd) {
        String pattern ="([0-9]{3})|([0-9]{6})|([0-9]{9})";
        return Pattern.matches(pattern, yyyyMMdd);
    }



    /**
     * yyyy年份的校验器。
     */
    public static boolean validateyyyy(String yyyyMMdd) {
        String pattern ="^[1-9][0-9]{3}";
        return Pattern.matches(pattern, yyyyMMdd);
    }

    /**
     * yyyy-MM年月日期的校验器。
     */
    public static boolean validateyyyyMM(String yyyyMMdd) {
        String pattern ="^[1-9][0-9]{3}-((0[1-9]{1})|(1[0-2]{1}))";
        return Pattern.matches(pattern, yyyyMMdd);
    }

    /**
     * yyyy-MM-dd日期的校验器。
     */
    public static boolean validateyyyyMMdd(String yyyyMMdd) {
        String pattern ="^^[1-9][0-9]{3}-((0[1-9]{1})|(1[0-2]{1}))-((0[1-9]{1})|([1-2]{1}[0-9]{1})|(3[0-1]{1}))";
        return Pattern.matches(pattern, yyyyMMdd);
    }



    /**
     * 浮点型数据校验器。
     */
    public static boolean validateFloatingPointType(String num) {
        String pattern ="(^0{1}+(\\.[0-9]*[1-9]{1,}$))|(^[1-9][0-9]*+(\\.[0-9]*$))";
        return Pattern.matches(pattern, num);
    }



    /**
     * 大于0的数字校验器。
     */
    public static boolean validateGreaterThanNum(String num) {
        String pattern = "^[1-9]\\d*$";
        return Pattern.matches(pattern, num);
    }



    /**
     * 大于等于0的数字校验器。
     */
    public static boolean validateGreaterThanNum2(String num) {
        String pattern = "^[0-9]\\d*$";
        return Pattern.matches(pattern, num);
    }

    /**
     * ip校验器。
     */
    public static boolean validateIp(String ip) {
        String pattern = "((?:(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d))))";
        return Pattern.matches(pattern, ip);
    }

    /**
     * 昵称校验器。
     * 1到10位的中文、字母、数字、_ 与 -
     */
    public static boolean validateNickName(String nickName) {
        String pattern = "^[\\u4e00-\\u9fa5a-zA-Z0-9_-]{1,10}$";
        return Pattern.matches(pattern, nickName);
    }

    /**
     * 登入名校验器。
     * 4到16位的字母、数字、下划线和短横线组成~_.@#$%&*
     */
    public static boolean validateLoginName(String loginName) {
        String pattern = "^(?![~!@#$%^&*()_+`\\-={}|\\[\\]\\\\:\";'<>?,./]+$)[0-9A-Za-z~!@#$%^&*()_+`\\-={}|\\[\\]\\\\:\";'<>?,./]{4,16}$";
        return Pattern.matches(pattern, loginName);
    }

    /**
     * 密码校验器。
     * 密码由四种元素组成（数字、大写字母、小写字母、特殊字符），且必须包含全部四种元素；密码长度8到16位个字符。
     */
    public static boolean validatePassword(String password) {
        String pattern = "^(?![0-9A-Za-z]+$)(?![A-Za-z\\W]+$)[0-9A-Za-z~!@#$%^&*()_+`\\-={}|\\[\\]\\\\:\";'<>?,./]{8,16}$";
        return Pattern.matches(pattern, password);
    }


    /**
     * 校验手机号码是否合法
     */
    public static boolean validatePhoneNumber(String number) {
        Pattern pattern = Pattern.compile("^1[3-9]\\d{9}$");
        Matcher matcher = pattern.matcher(number);
        return matcher.matches();
    }


    /**
     * 校验email是否合法
     */
    public static boolean validateEmail(String number) {
        Pattern pattern = Pattern.compile("^[A-Za-z0-9\u4e00-\u9fa5]+(\\.[A-Za-z0-9\u4e00-\u9fa5]+)*@[A-Za-z0-9\u4e00-\u9fa5]+(\\.[A-Za-z0-9\u4e00-\u9fa5]+)*(\\.[A-Za-z\u4e00-\u9fa5]{2,})$");
        Matcher matcher = pattern.matcher(number);
        return matcher.matches();
    }
}

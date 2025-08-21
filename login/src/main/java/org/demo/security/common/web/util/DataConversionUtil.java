package org.demo.security.common.web.util;


import com.alibaba.fastjson2.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * Created by 91066 on 2021/8/13.
 */
public class DataConversionUtil {


    public static void main(String[] args) throws Exception {


        System.out.println(getThisWeekMonAndSun());

    }



    //获取本周得周一和周天得日期
    public static JSONObject getThisWeekMonAndSun(){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        int adjustedDayOfWeek = (dayOfWeek == 1) ? 7 : dayOfWeek - 1;

        String monday="";
        if(1==adjustedDayOfWeek){
            calendar.add(Calendar.DATE, -0);
            monday = formatter.format(calendar.getTime());
        }else {
            calendar.add(Calendar.DATE, -1*(adjustedDayOfWeek-1));
            monday = formatter.format(calendar.getTime());
        }

        calendar.add(Calendar.DATE, +6);
        Date sundayDate = calendar.getTime();
        String sunday = formatter.format(sundayDate);
        JSONObject returnJson=new JSONObject();
        returnJson.put("monday",monday);
        returnJson.put("sunday",sunday);
        return returnJson;
    }


    //将字节数组（长度8）转换成有符号的Long
    public static long  byteToSignedLong(byte[] bytes){
        long test;
        long a1=(long)(bytes[0]&0xFF)<<56;
        long a2=(long)(bytes[1]&0xFF)<<48;
        long a3=(long)(bytes[2]&0xFF)<<40;
        long a4=(long)(bytes[3]&0xFF)<<32;
        long a5=(long)(bytes[4]&0xFF)<<24;
        long a6=(long)(bytes[5]&0xFF)<<16;
        long a7=(long)(bytes[6]&0xFF)<<8;
        long a8=(long)(bytes[7]&0xFF);
        test=a1|a2|a3|a4|a5|a6|a7|a8;
        return test;
    }



    //将有符号的Long转换成字节数组（长度8）
    public static byte[] signedLongToByte(long test){
        byte[] bytes = new byte[8];
        bytes[0]=(byte) (test>>56);
        bytes[1]=(byte) (test>>48);
        bytes[2]=(byte) (test>>40);
        bytes[3]=(byte) (test>>32);
        bytes[4]=(byte) (test>>24);
        bytes[5]=(byte) (test>>16);
        bytes[6]=(byte) (test>>8);
        bytes[7]=(byte) (test);
        return bytes;
    }

    //===========








    //将字节数组（长度2）转换成有符号的Short
    public static short  byteToSignedShort(byte[] bytes){
        short test;
        short a1= (short) ((bytes[0]&0xFF)<<8);
        short a2=(short)(bytes[1]&0xFF);
        test= (short) (a1|a2);
        return test;
    }

    //将字节数组（长度2）转换成无符号的Short
    public static Integer byteToSignedUnShort(byte[] bytes){
        short test;
        short a1= (short) ((bytes[0]&0xFF)<<8);
        short a2= (short) (bytes[1]&0xFF);
        test = (short) (a1 | a2);
        Integer unShort = test & 0xFFFF;
        return unShort;
    }


    //将有符号的Short转换成字节数组（长度2）
    public static byte[] signedIntToShort(short test){
        byte[] bytes = new byte[2];
        bytes[0]=(byte) (test>>8);
        bytes[1]=(byte) (test);
        return bytes;
    }





    //将字节数组（长度4）转换成有符号的Int
    public static int  byteToSignedInt(byte[] bytes){
        int test;
        int a1=(int)(bytes[0]&0xFF)<<24;
        int a2=(int)(bytes[1]&0xFF)<<16;
        int a3=(int)(bytes[2]&0xFF)<<8;
        int a4=(int)(bytes[3]&0xFF);
        test=a1|a2|a3|a4;
        return test;
    }

    //将字节数组（长度4）转换成无符号的Int
    public static Long byteToSignedUnInt(byte[] bytes){
        Integer test;
        Integer a1=(int)(bytes[0]&0xFF)<<24;
        Integer a2=(int)(bytes[1]&0xFF)<<16;
        Integer a3=(int)(bytes[2]&0xFF)<<8;
        Integer a4=(int)(bytes[3]&0xFF);
        test=a1|a2|a3|a4;
        Long unInteger = test & 0xFFFFFFFFL;
        return unInteger;
    }

    //将有符号的Int转换成字节数组（长度4）
    public static byte[] signedIntToByte(int test){
        byte[] bytes = new byte[4];
        bytes[0]=(byte) (test>>24);
        bytes[1]=(byte) (test>>16);
        bytes[2]=(byte) (test>>8);
        bytes[3]=(byte) (test);
        return bytes;
    }


    public static Integer convertToUnShort(Short s){
        int unShort = s & 0xFFFF;
       return unShort;
    }

    public static Long convertToUnInteger(Integer s){
        Long unInteger = s & 0xFFFFFFFFL;
        return unInteger;
    }


    //获取两个日期之间相隔多少天/那几天
    public static JSONObject getDataNumAndaLLData(String starDate, String endDate) {
        DateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
        try {
            JSONObject returnJson=new JSONObject();
            List<String> dateStr=new ArrayList<>();
            Date star = dft.parse(starDate);//开始时间
            Date endDay=dft.parse(endDate);//结束时间
            Date nextDay=star;
            int i=0;
            while(nextDay.before(endDay)||nextDay.equals(endDay)){//当明天不在结束时间之前是终止循环
                String format = dft.format(star);
                Calendar cld = Calendar.getInstance();
                cld.setTime(star);
                cld.add(Calendar.DATE, 1);
                star = cld.getTime();
                //获得下一天日期字符串
                nextDay = star;
                i++;
                dateStr.add(format);
            }
            returnJson.put("dataNum",i);
            returnJson.put("aLLData",dateStr);
            return returnJson;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }

    }


    //获取指定的某个时间前几个小时属于哪天,返回yyyy-MM-dd
    public static String getAppointDateAFewHoursAgoDateStr1(Date date,Integer hourAgo)  {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, -hourAgo);
        Date time = calendar.getTime();
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
        String agoDateStr = simpleDateFormat1.format(time);
        return agoDateStr;
    }

    //获取指定的某个时间前几个小时属于哪月,返回yyyy-MM
    public static String getAppointDateAFewHoursAgoDateStr2(Date date,Integer hourAgo)  {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, -hourAgo);
        Date time = calendar.getTime();
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM");
        String agoDateStr = simpleDateFormat1.format(time);
        return agoDateStr;
    }

    //获取指定的某个时间前几个小时属于哪年,返回yyyy
    public static String getAppointDateAFewHoursAgoDateStr3(Date date,Integer hourAgo)  {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, -hourAgo);
        Date time = calendar.getTime();
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy");
        String agoDateStr = simpleDateFormat1.format(time);
        return agoDateStr;
    }

    public static String getCurHour()  {
        Calendar date3 = Calendar.getInstance();
        return date3.get(Calendar.HOUR_OF_DAY)<10?"0"+(date3.get(Calendar.HOUR_OF_DAY)):(date3.get(Calendar.HOUR_OF_DAY))+"";
    }

    public static String getCurDay()  {
        Calendar date3 = Calendar.getInstance();
        return date3.get(Calendar.DAY_OF_MONTH)<10?"0"+(date3.get(Calendar.DAY_OF_MONTH)):(date3.get(Calendar.DAY_OF_MONTH))+"";
    }
    public static String getCurMonth()  {
        Calendar date3 = Calendar.getInstance();
        return date3.get(Calendar.MONTH)<10?"0"+(date3.get(Calendar.MONTH)+1):(date3.get(Calendar.MONTH)+1)+"";
    }


    //把20210922090618445装成日期毫秒数
    public static Long changeTimeSatmpToMillisecond(Long timeSatmp) throws ParseException {
        SimpleDateFormat myFormat=new SimpleDateFormat("yyyyMMddHHmmss");
        Date parse = myFormat.parse(String.valueOf(timeSatmp).substring(0, String.valueOf(timeSatmp).length() - 3));
        return parse.getTime();
    }
    //把20210922090618445装成日期秒数
    public static Long changeTimeSatmpToSecond(Long timeSatmp) throws ParseException {
        SimpleDateFormat myFormat=new SimpleDateFormat("yyyyMMddHHmmss");
        Date parse = myFormat.parse(String.valueOf(timeSatmp).substring(0, String.valueOf(timeSatmp).length() - 3));
        return Long.parseLong(String.valueOf(parse.getTime()).substring(0, String.valueOf(parse.getTime()).length() - 3));
    }
    //把20210922090618445装成日期秒数
    public static Long changeTimeSatmpToSecond1(Long timeSatmp) throws ParseException {
        SimpleDateFormat myFormat=new SimpleDateFormat("yyyyMMddHHmmss");
        Date parse = myFormat.parse(String.valueOf(timeSatmp).substring(0, String.valueOf(timeSatmp).length() - 3));
        return parse.getTime()/1000;
    }
    //获取当年有多少周
    public static Map<String,Object> getWeekInYear(int years) {
        Map<String, Object> result = new HashMap<>();
        String yearLast = getYearLast(years);
        Calendar cal = Calendar.getInstance();
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        Date d=null;
        try {
            d=format.parse(yearLast);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        cal.setTime(d);
        int year = cal.get(Calendar.YEAR);
        int week = cal.get(Calendar.WEEK_OF_YEAR);
        int month = cal.get(Calendar.MONTH)+1;
        int xYearFirstDayOfWeek = getXYearFirstDayOfWeek(years);
        //如果第一天是星期1，2，3，4
        if(xYearFirstDayOfWeek<5){
            //如果是最后一个星期
            if(week==1&&month==12){
                int newxinqi = cal.get(Calendar.DAY_OF_WEEK)-1;
                if(0==newxinqi){
                    newxinqi=7;
                }
                int xYearLastDayOfWeek = getXYearLastDayOfWeek(year);
                if(xYearLastDayOfWeek>=4){
                    cal.add(Calendar.DATE, -(newxinqi));
                    week = cal.get(Calendar.WEEK_OF_YEAR)+1;
                }
                if(xYearLastDayOfWeek<4){
                    cal.add(Calendar.DATE, -(newxinqi));
                    week = cal.get(Calendar.WEEK_OF_YEAR);
                }
            }
        }else {
            //如果第一天是5，6，7
                if(week>1){
                    week=(week-1);
                }
                if(week==1&&month==12){
                    int newxinqi = cal.get(Calendar.DAY_OF_WEEK)-1;
                    if(0==newxinqi){
                        newxinqi=7;
                    }
                    int xYearLastDayOfWeek = getXYearLastDayOfWeek(year);
                    if(xYearLastDayOfWeek>=4){
                        cal.add(Calendar.DATE, -(newxinqi));
                        week = cal.get(Calendar.WEEK_OF_YEAR);
                    }
                    if(xYearLastDayOfWeek<4){
                        cal.add(Calendar.DATE, -(newxinqi));
                        week = cal.get(Calendar.WEEK_OF_YEAR)-1;
                    }
                }
        }
        result.put("week",String.valueOf(week));
        return result;
    }



    //获取当天是这一年的第几周
    public static Map<String,Object> getWeekAndYear(String date) {
        Map<String, Object> result = new HashMap<>();
        Calendar cal = Calendar.getInstance();
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        Date d=null;
        try {
            d=format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        cal.setTime(d);
        int year = cal.get(Calendar.YEAR);
        int week = cal.get(Calendar.WEEK_OF_YEAR);
        int month = cal.get(Calendar.MONTH)+1;
        int xYearFirstDayOfWeek = getXYearFirstDayOfWeek(year);
        //如果第一天是星期1，2，3，4
        if(xYearFirstDayOfWeek<5){
            //如果是最后一个星期
            if(week==1&&month==12){
                int newxinqi = cal.get(Calendar.DAY_OF_WEEK)-1;
                if(0==newxinqi){
                    newxinqi=7;
                }
                int xYearLastDayOfWeek = getXYearLastDayOfWeek(year);
                if(xYearLastDayOfWeek>=4){
                    cal.add(Calendar.DATE, -(newxinqi));
                    week = cal.get(Calendar.WEEK_OF_YEAR)+1;
                }
                if(xYearLastDayOfWeek<4){
                    cal.add(Calendar.DATE, +(4-newxinqi));
                    year = cal.get(Calendar.YEAR);
                    week = cal.get(Calendar.WEEK_OF_YEAR);
                }
            }
        }else {
            //如果第一天时5，6，7
           if(week==1&&month==1){
               int newxinqi = cal.get(Calendar.DAY_OF_WEEK)-1;
               if(0==newxinqi){
                   newxinqi=7;
               }
               cal.add(cal.DATE, -(newxinqi));
              int year1 = cal.get(Calendar.YEAR);
               int xYearFirstDayOfWeek1 = getXYearFirstDayOfWeek(year1);
              if(xYearFirstDayOfWeek1<5){
                  week = cal.get(Calendar.WEEK_OF_YEAR)+1;
                  year = cal.get(Calendar.YEAR);
              }else if(xYearFirstDayOfWeek1>=5){
                  week = cal.get(Calendar.WEEK_OF_YEAR);
                  year = cal.get(Calendar.YEAR);
              }
           }else if(week>=2||(week==1&&month==12)){
               if(week>=2){
                   week=(week-1);
               }
               if(week==1&&month==12){
                   int newxinqi = cal.get(Calendar.DAY_OF_WEEK)-1;
                   if(0==newxinqi){
                       newxinqi=7;
                   }
                   int xYearLastDayOfWeek = getXYearLastDayOfWeek(year);
                   if(xYearLastDayOfWeek>=4){
                       cal.add(Calendar.DATE, -(newxinqi));
                       week = cal.get(Calendar.WEEK_OF_YEAR);
                   }
                   if(xYearLastDayOfWeek<4){
                       cal.add(Calendar.DATE, +(4-newxinqi));
                       year = cal.get(Calendar.YEAR);
                       week = cal.get(Calendar.WEEK_OF_YEAR);
                   }

               }
           }
        }
        if(week<10){
            result.put("week",String.valueOf(year)+"-0"+String.valueOf(week));
        }else {
            result.put("week",String.valueOf(year)+"-"+String.valueOf(week));
        }
        return result;
    }

    //获取指定年的第一天是星期几
    public static int getXYearFirstDayOfWeek(int year) {
        String yearFirst = getYearFirst(year);
        Calendar cal = Calendar.getInstance();
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        Date d=null;
        try {
            d=format.parse(yearFirst);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        cal.setTime(d);
        int week = cal.get(Calendar.DAY_OF_WEEK)-1;
        if(0==week){
            return 7;
        }
        return week;
    }
    //获取指定年的最后一天是星期几
    public static int getXYearLastDayOfWeek(int year) {
        String yearFirst = getYearLast(year);
        Calendar cal = Calendar.getInstance();
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        Date d=null;
        try {
            d=format.parse(yearFirst);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        cal.setTime(d);
        int week = cal.get(Calendar.DAY_OF_WEEK)-1;
        if(0==week){
            return 7;
        }
        return week;
    }



    //获取某一年的第一天的YYYYMMDD
    public static String getYearFirst(int year){
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        Date currYearFirst = calendar.getTime();
        return getTodayStr(currYearFirst);
    }
    //获取某一年的最后一天的YYYYMMDD
    public static String getYearLast(int year){
        Calendar calendar = Calendar.getInstance();

        calendar.clear();

        calendar.set(Calendar.YEAR, year);

        calendar.roll(Calendar.DAY_OF_YEAR, -1);

        Date currYearLast = calendar.getTime();
        //这一年最后一天的日期
        String yearLast = getTodayStr(calendar.getTime());
        //这一年最后一天属于这一年的第几周
        return yearLast;
    }

    //根据指定年获取前一年年份
    public static String getBeforeYear(String strDate) {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
        try {
            date = formatter.parse(strDate);
        }catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.YEAR, -1);
        date = calendar.getTime();
        // 前十天的时间字符串
        String dateBeforString = formatter.format(date);
        return dateBeforString;
    }

    //根据指定月份获取前一个月的日期
    public static String getBeforeMonth(String strDate) {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM");
        try {
            date = formatter.parse(strDate);
        }catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONDAY, -1);
        date = calendar.getTime();
        // 前十天的时间字符串
        String dateBeforString = formatter.format(date);
        return dateBeforString;
    }




    //根据指定日期获取前一天的日期
    public static String getBeforeDay(String strDate) {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            date = formatter.parse(strDate);
        }catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, -1);
        date = calendar.getTime();
        // 前十天的时间字符串
        String dateBeforString = formatter.format(date);
        return dateBeforString;
    }


    //将YYYY-MM-DD HH:mm:ss字符串格式转换成20210709105527格式
    public static String getStrToNumber1(String str) {
        String replace = str.replace(" ", "").replace("-","").replace(":","");
        return null!=replace?replace:null;
    }

    //将YYYY-MM-DD HH:mm:ss字符串格式转换成20210709105527048格式
    public static String getStrToNumber(String str) {
        String replace = str.replace(" ", "").replace("-","").replace(":","");
        return null!=replace?replace+"000":null;
    }
    //将2021-09-08T16:42:12格式转换成YYYY-MM-DD HH:mm:ss这种格式
    public static String getPreTodayAllStr(String tStr) {
        String newStr=tStr.replace("T"," ");
        return null!=newStr?newStr:null;
    }
    //获取当前时间往前推五小时的20210709105527048这种时间戳
    public static Long getPreTodayAllStr() {
        long currentTimeMillis = System.currentTimeMillis();
        //往前推一天的毫秒数
        long preTodayTimeMillis =currentTimeMillis - 5 * 60 * 60 * 1000 ;
        Date date=new Date();
        date.setTime(preTodayTimeMillis);
        String preToday = millisecondsStrConversionDateStr(date);
        return null!=preToday?Long.parseLong(preToday):null;
    }
    //把当前时间"20210709105527048"转换成YYYY-MM-DD HH:mm:ss这种格式
    public static String numberDateConversionDateStr(Long numberDate) {
        String str = String.valueOf(numberDate);
        String y = str.substring(0,4);
        String M = str.substring(4,6);
        String d = str.substring(6,8);
        String h = str.substring(8,10);
        String m = str.substring(10,12);
        String s = str.substring(12,14);
       return y+"-"+M+"-"+d+" "+h+":"+m+":"+s;
    }
    //把当前时间转成转成"20210709105527048"这种格式
    public static String millisecondsStrConversionDateStr(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String format = sdf.format(date);
        format=format+String.valueOf(date.getTime()).substring(String.valueOf(date.getTime()).length()-3);
        return !format.equals("")?format:null;
    }
    //把当前时间转成转成"20210709"这种格式
    public static String getTodayStr(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String format = sdf.format(date);
        return !format.equals("")?format:null;
    }
    public static String getTodayStr2(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhh");
        String format = sdf.format(date);
        return !format.equals("")?format:null;
    }
    //把当前时间转成转成"202107"这种格式
    public static String getCurMonthStr(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        String format = sdf.format(date);
        return !format.equals("")?format:null;
    }
    //把当前时间转成"2021-07-09 05:04:04"这种格式
    public static String getTodayAllStr(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = sdf.format(date);
        return !format.equals("")?format:null;
    }

    //把当前时间转成"2021-07-09-05"这种格式
    public static String getTodayAllStr1(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH");
        String format = sdf.format(date);
        return !format.equals("")?format:null;
    }

    //把当前时间转成"2021-07-09"这种格式
    public static String getTodayAllStr2(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String format = sdf.format(date);
        return !format.equals("")?format:null;
    }

    //把当前时间转成"2021-07"这种格式
    public static String getTodayAllStr3(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        String format = sdf.format(date);
        return !format.equals("")?format:null;
    }

    //把当前时间转成"2021"这种格式
    public static String getTodayAllStr4(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        String format = sdf.format(date);
        return !format.equals("")?format:null;
    }

    //把"2021-07-09 05:04:04"转换成毫秒数
    public static Long getTodayAllStr2(String date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Long format = sdf.parse(date).getTime();
        return format;
    }


    //把"2021-07-09"转换成毫秒数
    public static Long getTodayAllStr3(String date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Long format = sdf.parse(date).getTime();
        return format;
    }
}

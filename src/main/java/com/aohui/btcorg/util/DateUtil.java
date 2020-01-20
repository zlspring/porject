package com.aohui.btcorg.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 当天的开始时间
     * @return
     */
    public static long startOfTodDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date date=calendar.getTime();
        return date.getTime();
    }
    /**
     * 当天的结束时间
     * @return
     */
    public static long endOfTodDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        Date date=calendar.getTime();
        return date.getTime();
    }

    public static boolean jisuan(String date1, String date2) throws Exception {
        SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-M-d HH:mm:ss");
        Date start = sdf.parse(date1);
        Date end = sdf.parse(date2);
        long cha = end.getTime() - start.getTime();
        double result = cha * 1.0 / (1000 * 60 * 60);
        if(result>=24){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 获取当前时间，时间戳
     *
     * @return
     */
    public static String getCurrentTimesTamp() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(new Date());
    }

//    public static void main(String[] args) {
//        String date1="2013-06-24 12:30:30"; //
//        String date2="2013-06-26 12:30:31";
//        try {
//            if(jisuan(date1,date2)){
//                System.out.println("可用");
//
//            }else{
//                System.out.println("已过期");
//            }
//        } catch (Exception e) {
//            // TODO: handle exception
//        }
//    }
}

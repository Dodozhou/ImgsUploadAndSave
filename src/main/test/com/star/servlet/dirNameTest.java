package com.star.servlet;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * Created by hp on 2016/12/25.
 */
public class dirNameTest {
    @Test
    public void dirTest(){
        //设置时间格式
        SimpleDateFormat df=new SimpleDateFormat("/yyyy/MM/dd/HH");
        System.out.println(df.format(new Date()));
        String time=df.format(new Date());
        String[] dates= time.split("/");
        for (String date : dates) {
            System.out.println(date);
        }
    }

}
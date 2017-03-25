package com.example.splider.timer;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2016/7/24.
 */
public class SpliderTimer extends Thread{

    public SpliderTimer(){

    }

    private static final long PERIOD_DAY = 24*60*60*1000;//以毫米为单位，24*60*60*1000是一天

    public void start(){
        RefreshItem ri = new RefreshItem();
    }

    public static class RefreshItem{
        public RefreshItem(){
            // 设置执行时间
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            calendar.set(year,month,day,22,00,00);
            Date date = calendar.getTime();
            Timer timer = new Timer();
            timer.schedule(new RefreshTask(), date, PERIOD_DAY);
        }

        class RefreshTask extends TimerTask{
            public void run(){
                System.out.println("执行定时任务11111111");
            }
        }
    }
}

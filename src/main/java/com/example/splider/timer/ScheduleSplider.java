package com.example.splider.timer;

import java.io.IOException;

/**
 * Created by Administrator on 2016/7/24.
 */
public class ScheduleSplider {
    public ScheduleSplider(){

    }

    public void go() throws IOException{
        SpliderTimer st = new SpliderTimer();
        st.start();//线程启动
        System.out.println("启动定时任务");
    }

    /*public static void main(String[] args){
        ScheduleSplider ss = new ScheduleSplider();
        try {
            ss.go();
        }catch (Exception e){
            e.printStackTrace();
        }
    }*/
}

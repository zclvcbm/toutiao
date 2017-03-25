package com.example.splider.timer;

import com.example.splider.bean.LinkTypeData;
import com.example.splider.core.ExtractService;
import com.example.splider.rule.Rule;

import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2016/7/24.
 */
// 要求程序定时执行某个方法，如：每一分钟爬取一次数据
public class ThreadTimer extends Thread {
    public ThreadTimer(){
    }

    public static int num=1;//分钟

    public void start(){
        RefreshItem ri = new RefreshItem(60*ThreadTimer.num);
    }

    public static class RefreshItem{
        public RefreshItem(int seconds) {
            Timer timer = new Timer();
            timer.scheduleAtFixedRate(new RefreshTask(), new Date(), seconds * 1000);
        }

        class RefreshTask extends TimerTask{
            public void run(){
                //查询
                System.out.println("方法被调用");

                Rule rule  = new Rule("http://ent.qq.com/",
                        new String[]{},new String[]{},
                        "div.Q-tpWrap", Rule.SELECTION, Rule.GET);
                List<LinkTypeData> extracts = ExtractService.extract(rule);
                printf(extracts);
            }

            public void printf(List<LinkTypeData> datas){
                for(LinkTypeData data : datas){
                    System.out.println(data.getLinkText());
                    System.out.println(data.getLinkHref());
                    System.out.println(data.getSrc());
                    System.out.println("===================================");
                    System.out.println("");
                }
            }
        }
    }

    /*public static void main(String[] args){
        ThreadTimer tm = new ThreadTimer();
        tm.start();
    }*/
}

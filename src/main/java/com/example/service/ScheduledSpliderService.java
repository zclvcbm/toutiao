package com.example.service;

import com.example.dao.NewsDAO;
import com.example.model.News;
import com.example.splider.bean.LinkTypeData;
import com.example.splider.core.ExtractService;
import com.example.splider.rule.Rule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by Admin on 2016/7/25.
 */
@Service
public class ScheduledSpliderService {
    private static final Logger logger = LoggerFactory.getLogger(ScheduledSpliderService.class);

    @Autowired
    NewsDAO newsDAO;

/*    @Scheduled(cron="0 0/2 8-20 * * ?")
    public void executeFileDownLoadTask(){
        // 间隔2分钟，执行工作任务
        Thread current = Thread.currentThread();
        System.out.println("定时任务1："+current.getId());
        logger.info("ScheduledTest.executedFileDownLoadTask 定时任务1:"+current.getId()+",name:"+current.getName());
    }

    @Scheduled(cron="0 0/1 8-20 * * ?")
    public void executeFileUploadTask(){
        // 间隔2分钟，执行工作任务
        Thread current = Thread.currentThread();
        System.out.println("定时任务2："+current.getId());
        logger.info("ScheduledTest.executedFileUploadTask 定时任务2:"+current.getId()+",name:"+current.getName());
    }

    @Scheduled(cron="0 0/3 8-20 * * ?")
    public void executeUploadBackTask(){
        // 间隔3分钟，执行工作任务
        Thread current = Thread.currentThread();
        System.out.println("定时任务3："+current.getId());
        logger.info("ScheduledTest.executeUploadBackTask 定时任务3:"+current.getId()+",name:"+current.getName());
    }*/

    @Scheduled(cron="0 48 20 * * ?")
    public void getDatasByCssQueryNewsQQ(){
        Rule rule = new Rule("http://ent.qq.com",
                new String[]{},new String[]{},
                "div.Q-tpWrap", Rule.SELECTION, Rule.GET);
        List<LinkTypeData> extracts = ExtractService.extract(rule);
        printf(extracts);
        saveNewsToDatabase(extracts);
    }

    public void saveNewsToDatabase(List<LinkTypeData> datas){
        for(LinkTypeData data : datas){
            News news = new News();
            news.setUserId(1);
            news.setTitle(data.getLinkText());
            news.setLink(data.getLinkHref());
            news.setImage(data.getSrc());
            news.setCreatedDate(new Date());
            news.setLikeCount(0);
            news.setCommentCount(0);
            newsDAO.addNews(news);
        }
    }

    public void printf(List<LinkTypeData> datas){
        for(LinkTypeData data : datas){
            logger.info(data.getLinkText());
            logger.info(data.getLinkHref());
            logger.info(data.getSrc());
            logger.info("=======================================");
            logger.info("");
        }
    }

}

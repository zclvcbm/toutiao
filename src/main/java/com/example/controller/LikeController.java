package com.example.controller;

import com.example.Util.ToutiaoUtil;
import com.example.async.EventModel;
import com.example.async.EventProducer;
import com.example.async.EventType;
import com.example.model.EntityType;
import com.example.model.HostHolder;
import com.example.model.News;
import com.example.service.LikeService;
import com.example.service.NewsService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Admin on 2016/7/16.
 */
@Controller
public class LikeController {
    @Autowired
    LikeService likeService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    NewsService newsService;

    @Autowired
    EventProducer eventProducer;

    @RequestMapping(path={"/like"}, method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public String like(@Param("newsId") int newsId){
        long likeCount =  likeService.like(hostHolder.getUser().getId(), EntityType.ENTITY_NEWS,newsId);
        // 更新喜欢数
        News news = newsService.getById(newsId);
        newsService.updateLikeCount(newsId,(int)likeCount);
        eventProducer.fireEvent(new EventModel(EventType.LIKE)
                .setEntityOwnerId(news.getUserId())
                .setActorId(hostHolder.getUser().getId())
                .setEntityId(newsId));
        return ToutiaoUtil.getJSONString(0,String.valueOf(likeCount));
    }

    @RequestMapping(path={"dislike"}, method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public String dislike(@Param("newsId") int newsId) {
        long likeCount = likeService.disLike(hostHolder.getUser().getId(),EntityType.ENTITY_NEWS,newsId);
        // 更新喜欢数
        newsService.updateLikeCount(newsId,(int)likeCount);
        return ToutiaoUtil.getJSONString(0,String.valueOf(likeCount));
    }
}

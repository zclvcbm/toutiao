package com.example.controller;

import com.example.model.*;
import com.example.service.LikeService;
import com.example.service.NewsService;
import com.example.service.UserService;
import org.apache.catalina.Host;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.w3c.dom.Entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 2016/7/2.
 */
@Controller
public class HomeController {
    @Autowired
    NewsService newsService;
    @Autowired
    UserService userService;
    @Autowired
    LikeService likeService;
    @Autowired
    HostHolder hostHolder;

    public static Logger logger = LoggerFactory.getLogger(HomeController.class);

    private List<ViewObject> getNews(int userId, int offset, int limit) {
        List<News> newsList = newsService.getLatestNews(userId,offset,limit);
        int localUserId = hostHolder.getUser() != null ? hostHolder.getUser().getId() : 0;
        List<ViewObject> vos = new ArrayList<>();
        for(News news : newsList) {
            ViewObject vo = new ViewObject();
            vo.set("news", news);
            logger.info(news.getCreatedDate().toString());
            vo.set("user",userService.getUser(news.getUserId()));
            if(localUserId != 0) {
                vo.set("like",likeService.getLikeStatus(localUserId, EntityType.ENTITY_NEWS, news.getId()));
            }else {
                vo.set("like",0);
            }
            vos.add(vo);
        }
        return vos;
    }

    @RequestMapping(path={"/", "/index"}, method = {RequestMethod.GET,RequestMethod.POST})
    public String index(Model model, @RequestParam(value = "pop", defaultValue = "0") int pop) {
        model.addAttribute("vos",getNews(0,0,10));
        if(hostHolder.getUser() != null){
            pop = 0;
        }
        model.addAttribute("pop",pop);
        return "home";
    }

    @RequestMapping(path={"/user/{userId}/"}, method={RequestMethod.GET, RequestMethod.POST})
    public String userIndex(Model model, @PathVariable("userId") int userId) {
        model.addAttribute("vos",getNews(userId,0,10));
        return "home";
    }
}

package com.example.controller;

import com.example.Util.ToutiaoUtil;
import com.example.model.News;
import com.example.model.ViewObject;
import com.example.service.NewsService;
import com.example.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Admin on 2016/7/2.
 */
@Controller
public class LoginController {

    @Autowired
    UserService userService;

    public static Logger logger = LoggerFactory.getLogger(LoginController.class);



    @RequestMapping(path={"/reg/"}, method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public String reg(Model model, @RequestParam("username") String username,
                      @RequestParam("password") String password,
                      @RequestParam(value = "remember", defaultValue = "0") int remember,
                      HttpServletResponse response) {

        try{
            Map<String, Object> map = userService.register(username,password);
            if(map.containsKey("ticket")) {
                Cookie cookie = new Cookie("ticket",map.get("ticket").toString());
                if(remember>0) {
                    cookie.setMaxAge(3600*24*5);
                }
                response.addCookie(cookie);
                return ToutiaoUtil.getJSONString(0,"注册成功!");
            } else {
                return ToutiaoUtil.getJSONString(0,map);
            }


// {"code":0, "msg":"xxx"}
        }catch (Exception e) {
            logger.error("注册异常"+e.getMessage());
            return ToutiaoUtil.getJSONString(1,"注册异常");
        }

    }

    @RequestMapping(path={"/login/"}, method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public String login(Model model, @RequestParam("username") String username,
                      @RequestParam("password") String password,
                      @RequestParam(value = "remember", defaultValue = "0") int remember,
                      HttpServletResponse response  ) {
        try{
            Map<String, Object> map = userService.login(username,password);
            if(map.containsKey("ticket")){
                Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
                cookie.setPath("/");
                if(remember>0) {
                    cookie.setMaxAge(3600*24*5);
                }
                response.addCookie(cookie);
                return ToutiaoUtil.getJSONString(0,"登录成功!");
            } else{
                return ToutiaoUtil.getJSONString(1,map);
            }
        }catch (Exception e) {
            logger.error("登录异常" + e.getMessage());
            return ToutiaoUtil.getJSONString(1, "登录异常");
        }
    }

    @RequestMapping(path={"/logout/"},method={RequestMethod.GET, RequestMethod.POST})
    public String logout(@CookieValue("ticket") String ticket) {
        userService.logout(ticket);
        return "redirect:/";
    }

}

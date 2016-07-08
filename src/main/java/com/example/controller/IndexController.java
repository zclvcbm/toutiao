package com.example.controller;

import com.example.model.User;
import com.example.service.ToutiaoService;
import org.aspectj.lang.JoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * Created by Admin on 2016/6/27.
 */
//@Controller
public class IndexController {

    @Autowired
    private ToutiaoService toutiaoService;

    @RequestMapping(path={"/","/index"})
    @ResponseBody
    public String index() {
        return "Hello Nowcoder"+" "+toutiaoService.sayHello();
    }

    @RequestMapping(value={"/profile/{groupId}/{userId}"})
    @ResponseBody
    public String profile(@PathVariable("groupId") String groupId,
                          @PathVariable("userId") String userId,
                          @RequestParam(value="type", defaultValue="1") int type,
                          @RequestParam(value="key", defaultValue="nowcoder") String key) {

        return String.format("GID{%s},UID{%s},TYPE{%d},KEY{%s}",groupId,userId,type,key);
    }

    @RequestMapping(path={"/vm"})
    public String news(Model model) {
        model.addAttribute("value1","val");
        List<String> colors = Arrays.asList(new String[] {"RED","GREEN","BLUE"});

        Map<String,Integer> chars = new HashMap<String,Integer>();
        chars.put("a",97);
        chars.put("b",98);
        chars.put("c",99);

        Map<String,User> users = new HashMap<String,User>();
        users.put("zs",new User("zhangsan"));
        users.put("ls",new User("lisi"));
        users.put("ww",new User("wangwu"));

        model.addAttribute("colors",colors);
        model.addAttribute("chars",chars);
        model.addAttribute("users",users);
        return "news";
    }

    @RequestMapping("/request")
    @ResponseBody
    public String request(HttpServletRequest request,
                        HttpServletResponse response,
                        HttpSession session) {
        StringBuilder sb = new StringBuilder();
        Enumeration<String> headerNames = request.getHeaderNames();
        while(headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            sb.append(name+":"+request.getHeader(name)+"<br/>");
        }

        for(Cookie cookie : request.getCookies()) {
            sb.append("Cookie:");
            sb.append(cookie.getName());
            sb.append(":");
            sb.append(cookie.getValue()+"<br/>");
        }

        sb.append("request.getMethod:"+request.getMethod()+"<br/>");
        sb.append("request.getPathInfo:"+request.getPathInfo()+"<br/>");
        sb.append("request.getContextPath:"+request.getContextPath()+"<br/>");
        sb.append("request.getRequestURI:"+request.getRequestURI()+"<br/>");
        sb.append("request.getQueryString:"+request.getQueryString()+"<br/>");
        return sb.toString();
    }

    @RequestMapping("/response")
    @ResponseBody
    public String response(@CookieValue(value="nowcoderid",defaultValue = "a") String nowcoderId,
                           @RequestParam(value="key", defaultValue = "key") String key,
                           @RequestParam(value="value", defaultValue = "value") String value,
                           HttpServletResponse response) {
        response.addCookie(new Cookie(key,value));
        response.addHeader(key,value);
        return "NowcoderId From Cookie:"+nowcoderId;
    }

    /*
    @RequestMapping("/redirect/{code}")
    public RedirectView redirect(@PathVariable("code") int code) {
        RedirectView red = new RedirectView("/",true);
        if(code == 301) {
            red.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
        }

        return red;
    }
    */

    @RequestMapping("/redirect/{code}")
    public String redirect(@PathVariable("code") int code) {


        return "redirect:/";
    }

    @RequestMapping("/admin")
    @ResponseBody
    public String admin(@RequestParam(value="password", required = false) String password) {
        if("admin".equals(password)) {
            return "hello admin";
        }
        throw new IllegalArgumentException("Password 错误!");
    }

    @ExceptionHandler()
    @ResponseBody
    public String error(Exception e){
        return "error:"+e.getMessage();
    }

}

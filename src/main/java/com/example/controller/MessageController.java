package com.example.controller;

import com.example.Util.ToutiaoUtil;
import com.example.model.HostHolder;
import com.example.model.Message;
import com.example.model.User;
import com.example.model.ViewObject;
import com.example.service.MessageService;
import com.example.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Admin on 2016/7/10.
 */
@Controller
public class MessageController {

    @Autowired
    MessageService messageService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    UserService userService;

    public static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @RequestMapping(path={"/msg/detail"} , method = RequestMethod.GET)
    public String conversationDetail(Model model, @RequestParam("conversationId") String conversationId) {
        try{
            List<ViewObject> messages = new ArrayList<>();
            List<Message> conversationList = messageService.getConversationDetail(conversationId,0,10);
            for(Message msg : conversationList) {
                ViewObject vo = new ViewObject();
                vo.set("message",msg);
                User user = userService.getUser(msg.getFromId());
                if(user == null) {
                    continue;
                }
                vo.set("headUrl",user.getHeadUrl());
                vo.set("userName",user.getName());
                messages.add(vo);
            }
            messageService.updateMessageReaded(conversationId);
            model.addAttribute("messages",messages);
            return "letterDetail";
        }catch (Exception e){
            logger.error("获取站内信列表失败"+e.getMessage());
        }
        return "letterDetail";
    }

    @RequestMapping(path={"/msg/list"}, method={RequestMethod.GET})
    public String conversationList(Model model) {
        try{
            int localUserId = hostHolder.getUser().getId();
            List<ViewObject> conversations = new ArrayList<>();
            List<Message> conversationList = messageService.getConversationList(localUserId,0,10);
            for(Message msg : conversationList) {
                ViewObject vo = new ViewObject();
                vo.set("conversation",msg);
                int targetId = msg.getFromId() == localUserId?msg.getToId():msg.getFromId();
                User user = userService.getUser(targetId);
                vo.set("headUrl",user.getHeadUrl());
                vo.set("userName",user.getName());
                vo.set("targetId",targetId);
                vo.set("totalCount",msg.getId());
                vo.set("unreadCount",messageService.getUnReadCount(localUserId,msg.getConversationId()));
                conversations.add(vo);
            }
            model.addAttribute("conversations",conversations);
            return "letter";
        }catch (Exception e) {
            logger.error("获取站内信列表失败"+e.getMessage());
        }
        return "letter";
    }

    @RequestMapping(path={"/msg/addMessage"}, method={RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public String addMessage(@RequestParam("fromId") int fromId,
                             @RequestParam("toId") int toId,
                             @RequestParam("content") String content) {
        Message message = new Message();
        message.setContent(content);
        message.setCreatedDate(new Date());
        message.setToId(toId);
        message.setFromId(fromId);
        message.setConversationId( fromId < toId ? String.format("%d_%d",fromId,toId) : String.format("%d_%d",toId,fromId));
        messageService.addMessage(message);
        return ToutiaoUtil.getJSONString(message.getId());
    }

    @RequestMapping(path={"/msg/del"}, method={RequestMethod.GET})
    public String delMessage(@RequestParam("id") int id,
                             @RequestParam("conversationId") String conversationId) {
            int res = messageService.deleteMessageById(id);
            logger.info(String.format("%d",res));
            //return "letter";
            return "redirect:/msg/detail?conversationId="+conversationId;
    }

}

package com.example.async.handler;

import com.example.Util.MailSender;
import com.example.async.EventHandler;
import com.example.async.EventModel;
import com.example.async.EventType;
import com.example.model.Message;
import com.example.service.MessageService;
import com.sun.mail.util.logging.MailHandler;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by Administrator on 2016/7/16.
 */
@Component
public class LoginExceptionHandler implements EventHandler{
    @Autowired
    MessageService messageService;

    @Autowired
    MailSender mailSender;

    @Override
    public void doHandle(EventModel model) {
        Message message = new Message();
        message.setToId(model.getActorId());
        message.setContent("你上次的登陆IP异常");
        // SYSTEM ACCOUNT
        message.setFromId(3);
        message.setCreatedDate(new Date());
        String conversationId = model.getActorId()<3 ? model.getActorId()+"_"+3 : 3+"_"+model.getActorId();
        message.setConversationId(conversationId);
        messageService.addMessage(message);

        Map<String, Object> map = new HashMap<>();
        map.put("username",model.getExt("username"));
        System.out.println("hello");
        mailSender.sendWithHTMLTemplate(model.getExt("to"),"登陆异常","mails/welcome.html",map);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LOGIN);
    }
}

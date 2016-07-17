package com.example.async.handler;

import com.example.async.EventHandler;
import com.example.async.EventModel;
import com.example.async.EventType;
import com.example.model.Message;
import com.example.model.User;
import com.example.service.MessageService;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/7/16.
 */
@Component
public class LikeHandler implements EventHandler{

    @Autowired
    MessageService messageService;

    @Autowired
    UserService userService;

    @Override
    public void doHandle(EventModel model) {
        Message message = new Message();
        User user = userService.getUser(model.getActorId());
        message.setToId(model.getEntityOwnerId());
        message.setContent("用户"+user.getName()+" 赞了你的咨询,http://127.0.0.1:8080/news/"
                +String.valueOf(model.getEntityId()));
        message.setFromId(3);
        message.setCreatedDate(new Date());
        String conversation_id = model.getEntityId()<3?model.getEntityId()+"_"+3:3+"_"+model.getEntityId();
        message.setConversationId(conversation_id);
        messageService.addMessage(message);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LIKE);
    }
}

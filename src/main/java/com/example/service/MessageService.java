package com.example.service;

import com.example.dao.MessageDAO;
import com.example.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Admin on 2016/7/10.
 */
@Service
public class MessageService {
    @Autowired
    MessageDAO messageDAO;

    public int addMessage(Message message) {
        return messageDAO.addMessage(message);
    }

    public List<Message> getConversationList(int userId, int offset, int limit) {
        // conversation的总条数存在id里
        return messageDAO.getConversationList(userId,offset,limit);
    }

    public List<Message> getConversationDetail(String conversationId, int offset, int limit) {
        return messageDAO.getConversationDetail(conversationId,offset,limit);
    }

    public int getUnReadCount(int userId, String conversationId) {
        return messageDAO.getConversationUnReadCount(userId,conversationId);
    }

    public int deleteMessageById(int id){
        return messageDAO.deleteMessageById(id);
    }

    public int updateMessageReaded(String conversationId){
        return messageDAO.updateMessageReaded(conversationId);
    }
}

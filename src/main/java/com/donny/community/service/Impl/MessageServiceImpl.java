package com.donny.community.service.Impl;

import com.donny.community.dao.MessageMapper;
import com.donny.community.entity.Message;
import com.donny.community.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    MessageMapper messageMapper;

    @Override
    public List<Message> findConversations(Integer userId, Integer offset, Integer limit) {
        return messageMapper.selectConversationsById(userId, offset, limit);
    }

    @Override
    public Integer findConversationsCount(Integer userId) {
        return messageMapper.selectConversationCount(userId);
    }

    @Override
    public List<Message> findMessages(String conversationId, Integer offset, Integer limit) {
        return messageMapper.selectMessages(conversationId, offset, limit);
    }

    @Override
    public Integer findMessageCount(String conversationId) {
        return messageMapper.selectMessageCount(conversationId);
    }

    @Override
    public Integer findMessageUnreadCount(Integer userId, String conversationId) {
        return messageMapper.selectMessageUnreadCount(userId, conversationId);
    }
}

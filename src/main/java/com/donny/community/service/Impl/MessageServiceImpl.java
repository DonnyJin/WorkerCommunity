package com.donny.community.service.Impl;

import com.donny.community.dao.MessageMapper;
import com.donny.community.entity.Message;
import com.donny.community.service.MessageService;
import com.donny.community.util.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private SensitiveFilter sensitiveFilter;

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

    @Override
    public Integer addMessage(Message message) {
        message.setContent(HtmlUtils.htmlEscape(message.getContent()));
        message.setContent(sensitiveFilter.filter(message.getContent()));
        return messageMapper.insertMessage(message);
    }

    @Override
    public Integer readMessages(List<Integer> ids) {
        return messageMapper.updateStatus(ids, 1);
    }

    @Override
    public Message findLastestNotice(Integer userId, String topic) {
        return messageMapper.selectLastestNotice(userId, topic);
    }

    @Override
    public Integer findNoticeCount(Integer userId, String topic) {
        return messageMapper.selectNoticeCount(userId, topic);
    }

    @Override
    public Integer findUnreadNoticeCount(Integer userId, String topic) {
        return messageMapper.selectUnreadNoticeCount(userId, topic);
    }

}

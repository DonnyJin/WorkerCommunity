package com.donny.community.service;

import com.donny.community.entity.Message;

import java.util.List;

public interface MessageService {

    List<Message> findConversations(Integer userId, Integer offset, Integer limit);

    Integer findConversationsCount(Integer userId);

    List<Message> findMessages(String conversationId, Integer offset, Integer limit);

    Integer findMessageCount(String conversationId);

    Integer findMessageUnreadCount(Integer userId, String conversationId);

    Integer addMessage(Message message);

    Integer readMessages(List<Integer> ids);

    Message findLastestNotice(Integer userId, String topic);

    Integer findNoticeCount(Integer userId, String topic);

    Integer findUnreadNoticeCount(Integer userId, String topic);

}

package com.donny.community.dao;

import com.donny.community.entity.Message;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MessageMapper {

    /**
     * 查询当前用户的会话列表,针对每个会话返回第一条最新的
     * @param userId
     * @param offset
     * @param limit
     * @return
     */
    List<Message> selectConversationsById(Integer userId, Integer offset, Integer limit);

    /**
     * 查询当前用户的会话数量
     * @param userId
     * @return
     */
    Integer selectConversationCount(Integer userId);

    /**
     * 查询某个会话所包含的私信列表
     * @param conversationId
     * @param offset
     * @param limit
     * @return
     */
    List<Message> selectMessages(String conversationId, Integer offset, Integer limit);

    Integer selectMessageCount(String conversationId);

    /**
     * 查询未读私信的数量，如果没有传入conversationId就是查全部
     * @param userId
     * @param conversationId
     * @return
     */
    Integer selectMessageUnreadCount(Integer userId, String conversationId);

    Integer insertMessage(Message message);

    Integer updateStatus(List<Integer> ids, Integer status);

}

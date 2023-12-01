package com.donny.community.controller;


import com.alibaba.fastjson.JSONObject;
import com.donny.community.entity.Message;
import com.donny.community.entity.Page;
import com.donny.community.entity.User;
import com.donny.community.service.MessageService;
import com.donny.community.service.UserService;
import com.donny.community.util.CommunityConstant;
import com.donny.community.util.CommunityUtil;
import com.donny.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import javax.jws.WebParam;
import java.util.*;

@Controller
@RequestMapping("/message")
public class MessageController implements CommunityConstant {

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    @GetMapping("/list")
    public String getMessageList(Model model, Page page) {
        User user = hostHolder.getUser();
        // 分页信息
        page.setPageSize(5);
        page.setPath("/message/list");
        page.setRows(messageService.findConversationsCount(user.getId()));
        // 会话列表
        List<Message> conversationList = messageService.findConversations(user.getId(), page.getOffset(), page.getPageSize());
        List<Map<String, Object>> conversations = new ArrayList<>();
        if (conversationList != null) {
            for (Message message : conversationList) {
                Map<String, Object> map = new HashMap<>();
                map.put("conversation", message);
                map.put("messageCount", messageService.findMessageCount(message.getConversationId()));
                map.put("unreadCount", messageService.findMessageUnreadCount(user.getId(), message.getConversationId()));
                Integer targetId = user.getId().equals(message.getFromId()) ? message.getToId() : message.getFromId();
                map.put("target", userService.getUserById(targetId));

                conversations.add(map);
            }
        }
        model.addAttribute("conversations", conversations);

        // 查询用户所有未读私信数量
        Integer messageUnreadCount = messageService.findMessageUnreadCount(user.getId(), null);
        model.addAttribute("messageUnreadCount", messageUnreadCount);

        return "/site/letter";
    }

    @GetMapping("/detail/{conversationId}")
    public String getConversationDetail(@PathVariable String conversationId, Page page, Model model) {
        // 分页信息
        page.setPageSize(5);
        page.setPath("/message/detail/" + conversationId);
        page.setRows(messageService.findMessageCount(conversationId));

        List<Message> messageList = messageService.findMessages(conversationId, page.getOffset(), page.getPageSize());
        List<Map<String, Object>> messages = new ArrayList<>();
        if (messageList != null) {
            for (Message message : messageList) {
                Map<String, Object> map = new HashMap<>();
                map.put("message", message);
                map.put("fromUser", userService.getUserById(message.getFromId()));
                messages.add(map);
            }
        }
        model.addAttribute("messages", messages);
        // 返回聊天对象
        String[] ids = conversationId.split("_");
        Integer id0 = Integer.parseInt(ids[0]);
        Integer id1 = Integer.parseInt(ids[1]);
        Integer targetID = hostHolder.getUser().getId().equals(id0) ? id1 : id0;

        model.addAttribute("target", userService.getUserById(targetID));

        // 获取未读的私信,并改为已读
        if (messageList != null) {
            List<Integer> unreadIds = new ArrayList<>();
            Integer toId = hostHolder.getUser().getId();
            for (Message message : messageList) {
                if (message.getToId().equals(toId) && message.getStatus() == 0) {
                    unreadIds.add(message.getId());
                }
            }
            if (!unreadIds.isEmpty()) {
                messageService.readMessages(unreadIds);
            }
        }

        return "site/letter-detail";
    }

    @PostMapping("/send")
    @ResponseBody
    public String sendMessage(String toName, String content) {
        int i = 1 / 0;
        User target = userService.findUserByName(toName);
        if (target == null) {
            return CommunityUtil.getJSONString(1, "目标用户不存在");
        }
        Message message = new Message();
        message.setContent(content);
        message.setFromId(hostHolder.getUser().getId());
        message.setToId(target.getId());
        String conversationId = message.getFromId() < message.getToId() ? message.getFromId() + "_" + message.getToId() : message.getToId() + "_" + message.getFromId();
        message.setConversationId(conversationId);
        message.setCreateTime(new Date());

        messageService.addMessage(message);
        return CommunityUtil.getJSONString(0);
    }

    @GetMapping("/notice/list")
    public String getNoticeList(Model model) {
        User user = hostHolder.getUser();

        // 查询评论类通知
        Message notice = messageService.findLastestNotice(user.getId(), TOPIC_COMMENT);
        Map<String, Object> noticeVO = new HashMap<>();
        if (notice != null) {
            noticeVO.put("notice", notice);
            String content = HtmlUtils.htmlUnescape(notice.getContent());
            Map<String, Object> data = JSONObject.parseObject(content, HashMap.class);

        }
        // 查询点赞类通知

        // 查询关注类通知
    }
}

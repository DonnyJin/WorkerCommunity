package com.donny.community.controller;


import com.alibaba.fastjson.JSONObject;
import com.donny.community.annotation.LoginRequired;
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
    @LoginRequired
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

        // 查询用户所有未读私信和系统通知数量
        Integer messageUnreadCount = messageService.findMessageUnreadCount(user.getId(), null);
        model.addAttribute("messageUnreadCount", messageUnreadCount);
        Integer unreadNoticeCount = messageService.findUnreadNoticeCount(user.getId(), null);
        model.addAttribute("noticeUnreadCount", unreadNoticeCount);

        return "/site/letter";
    }

    @GetMapping("/detail/{conversationId}")
    @LoginRequired
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
    @LoginRequired
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
    @LoginRequired
    public String getNoticeList(Model model) {
        User user = hostHolder.getUser();

        // 查询评论类通知
        Message commentNotice = messageService.findLastestNotice(user.getId(), TOPIC_COMMENT);
        Map<String, Object> commentNoticeVO = new HashMap<>();
        if (commentNotice != null) {
            commentNoticeVO.put("notice", commentNotice);
            String content = HtmlUtils.htmlUnescape(commentNotice.getContent());
            Map<String, Object> data = JSONObject.parseObject(content, HashMap.class);

            commentNoticeVO.put("user", userService.getUserById((Integer) data.get("userId")));
            commentNoticeVO.put("entityType", data.get("entityType"));
            commentNoticeVO.put("entityId", (Integer) data.get("entityId"));
            commentNoticeVO.put("postId", (Integer) data.get("postId"));

            Integer count = messageService.findNoticeCount(user.getId(), TOPIC_COMMENT);
            commentNoticeVO.put("count",count);

            Integer unread = messageService.findUnreadNoticeCount(user.getId(), TOPIC_COMMENT);
            commentNoticeVO.put("unread", unread);
        } else {
            commentNoticeVO.put("notice", null);
            commentNoticeVO.put("user", null);
            commentNoticeVO.put("entityType", null);
            commentNoticeVO.put("entityId", null);
            commentNoticeVO.put("postId", null);
            commentNoticeVO.put("count", null);
            commentNoticeVO.put("unread", null);
        }
        model.addAttribute("commentNotice", commentNoticeVO);
        // 查询点赞类通知
        Message likeNotice = messageService.findLastestNotice(user.getId(), TOPIC_LIKE);
        Map<String, Object> likeNoticeVO = new HashMap<>();
        if (likeNotice != null) {
            likeNoticeVO.put("notice", likeNotice);
            String content = HtmlUtils.htmlUnescape(likeNotice.getContent());
            Map<String, Object> data = JSONObject.parseObject(content, HashMap.class);

            likeNoticeVO.put("user", userService.getUserById((Integer) data.get("userId")));
            likeNoticeVO.put("entityType", data.get("entityType"));
            likeNoticeVO.put("entityId", data.get("entityId"));
            likeNoticeVO.put("postId", data.get("postId"));

            Integer count = messageService.findNoticeCount(user.getId(), TOPIC_LIKE);
            likeNoticeVO.put("count",count);

            Integer unread = messageService.findUnreadNoticeCount(user.getId(), TOPIC_LIKE);
            likeNoticeVO.put("unread", unread);
        } else {
            likeNoticeVO.put("notice", null);
            likeNoticeVO.put("user", null);
            likeNoticeVO.put("entityType", null);
            likeNoticeVO.put("entityId", null);
            likeNoticeVO.put("postId", null);
            likeNoticeVO.put("count", null);
            likeNoticeVO.put("unread", null);
        }
        model.addAttribute("likeNotice", likeNoticeVO);
        // 查询关注类通知
        Message followNotice = messageService.findLastestNotice(user.getId(), TOPIC_FOLLOW);
        Map<String, Object> followNoticeVO = new HashMap<>();
        if (followNotice != null) {
            followNoticeVO.put("notice", followNotice);
            String content = HtmlUtils.htmlUnescape(followNotice.getContent());
            Map<String, Object> data = JSONObject.parseObject(content, HashMap.class);

            followNoticeVO.put("user", userService.getUserById((Integer) data.get("userId")));
            followNoticeVO.put("entityType", data.get("entityType"));
            followNoticeVO.put("entityId", data.get("entityId"));

            Integer count = messageService.findNoticeCount(user.getId(), TOPIC_FOLLOW);
            followNoticeVO.put("count",count);

            Integer unread = messageService.findUnreadNoticeCount(user.getId(), TOPIC_FOLLOW);
            followNoticeVO.put("unread", unread);
        } else {
            followNoticeVO.put("notice", null);
            followNoticeVO.put("user", null);
            followNoticeVO.put("entityType", null);
            followNoticeVO.put("entityId", null);
            followNoticeVO.put("count", null);
            followNoticeVO.put("unread", null);
        }
        model.addAttribute("followNotice", followNoticeVO);

        // 查询用户所有未读私信和系统通知数量
        Integer messageUnreadCount = messageService.findMessageUnreadCount(user.getId(), null);
        model.addAttribute("messageUnreadCount", messageUnreadCount);
        Integer unreadNoticeCount = messageService.findUnreadNoticeCount(user.getId(), null);
        model.addAttribute("noticeUnreadCount", unreadNoticeCount);

        return "site/notice";
    }

    @GetMapping("/notice/detail/{topic}")
    @LoginRequired
    public String getNoticeDetail(@PathVariable String topic, Page page, Model model) {
        User user = hostHolder.getUser();
        page.setPageSize(5);
        page.setPath("/message/notice/detail/" + topic);
        page.setRows(messageService.findNoticeCount(user.getId(), topic));

        List<Message> noticeList = messageService.findNoticeList(user.getId(), topic, page.getOffset(), page.getPageSize());
        List<Map<String, Object>> noticeVOList = new ArrayList<>();
        if (noticeList != null) {
            for (Message notice : noticeList) {
                Map<String, Object> map = new HashMap<>();
                // 通知
                map.put("notice", notice);
                // 内容
                String content = HtmlUtils.htmlUnescape(notice.getContent());
                Map<String, Object> data = JSONObject.parseObject(content, Map.class);
                map.put("user", userService.getUserById((Integer) data.get("userId")));
                map.put("entityType", (Integer) data.get("entityType"));
                map.put("entityId", (Integer) data.get("entityId"));
                map.put("postId", (Integer) data.get("postId"));
                // 通知作者
                map.put("fromUser", userService.getUserById(notice.getFromId()));

                noticeVOList.add(map);
            }
        } else {
            model.addAttribute("noticeVOList", null);
        }
        model.addAttribute("noticeVOList", noticeVOList);
        // 设置已读
        if (noticeList != null) {
            List<Integer> unreadIds = new ArrayList<>();
            Integer toId = hostHolder.getUser().getId();
            for (Message notice : noticeList) {
                if (notice.getToId().equals(toId) && notice.getStatus() == 0) {
                    unreadIds.add(notice.getId());
                }
            }
            if (!unreadIds.isEmpty()) {
                messageService.readMessages(unreadIds);
            }
        }


        return "site/notice-detail";
    }
}

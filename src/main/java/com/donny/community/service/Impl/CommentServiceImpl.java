package com.donny.community.service.Impl;


import com.donny.community.dao.CommentMapper;
import com.donny.community.dao.DiscussPostMapper;
import com.donny.community.entity.Comment;
import com.donny.community.service.CommentService;
import com.donny.community.util.CommunityConstant;
import com.donny.community.util.SensitiveFilter;
import org.omg.CORBA.TRANSACTION_REQUIRED;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService, CommunityConstant {

    @Autowired
    CommentMapper commentMapper;

    @Autowired
    DiscussPostMapper discussPostMapper;

    @Autowired
    SensitiveFilter sensitiveFilter;

    @Override
    public List<Comment> getCommentByEntity(Integer entityType, Integer entityId, Integer offset, Integer limit) {
        return commentMapper.selectCommentByEntity(entityType, entityId, offset, limit);
    }

    @Override
    public Integer getCommentCount(Integer entityType, Integer entityId) {
        return commentMapper.selectCountByEntity(entityType, entityId);
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public Integer addComment(Comment comment) {
        if (comment == null) throw new IllegalArgumentException("参数不能为空!");

        comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));
        comment.setContent(sensitiveFilter.filter(comment.getContent()));
        int rows = commentMapper.insertComment(comment);

        // 更新评论数量
        if (comment.getEntityType().equals(ENTITY_TYPE_POST)) {
            Integer count = commentMapper.selectCountByEntity(comment.getEntityType(), comment.getEntityId());
            discussPostMapper.updateCommentCount(comment.getEntityId(), count + 1);

        }
        return rows;
    }
}

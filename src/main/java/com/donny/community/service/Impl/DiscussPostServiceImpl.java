package com.donny.community.service.Impl;

import com.donny.community.dao.DiscussPostMapper;
import com.donny.community.entity.Comment;
import com.donny.community.entity.DiscussPost;
import com.donny.community.entity.Event;
import com.donny.community.service.DiscussPostService;
import com.donny.community.util.CommunityConstant;
import com.donny.community.util.SensitiveFilter;
import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class DiscussPostServiceImpl implements DiscussPostService, CommunityConstant {

    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Autowired
    private SensitiveFilter sensitiveFilter;

    @Value("${caffeine.posts.max-size}")
    private Integer maxSize;
    @Value("${caffeine.posts.expire-seconds}")
    private Integer expireSeconds;

    //Caffeine核心接口: Cache, LoadingCache, AsyncLoadingCache
    /**
     * 帖子列表的缓存
     */
    private LoadingCache<String, List<DiscussPost>> postListCache;
    /**
     * 帖子总数的缓存
     */
    private LoadingCache<Integer, Integer> postRowsCache;

    @PostConstruct
    public void init() {
        // 初始化帖子列表缓存
        postListCache = Caffeine.newBuilder()
                .maximumSize(maxSize)
                .expireAfterWrite(expireSeconds, TimeUnit.SECONDS)
                .build(new CacheLoader<String, List<DiscussPost>>() {
                    @Override
                    public @Nullable List<DiscussPost> load(@NonNull String key) throws Exception {
                        if (key == null || key.length() == 0) throw new IllegalArgumentException("参数错误!");

                        String[] params = key.split(":");
                        if (params == null || params.length != 2) {
                            throw new IllegalArgumentException("参数错误!");
                        }
                        Integer offset = Integer.valueOf(params[0]);
                        Integer limit = Integer.valueOf(params[1]);

                        // 二级缓存 Redis


                        // DataBase
                        log.debug("Load Posts From Database");
                        return discussPostMapper.selectPostList(0, offset, limit, 1);
                    }
                });
        // 初始化帖子总数缓存
        postRowsCache = Caffeine.newBuilder()
                .maximumSize(maxSize)
                .expireAfterWrite(expireSeconds, TimeUnit.SECONDS)
                .build(new CacheLoader<Integer, Integer>() {
                    @Override
                    public @Nullable Integer load(@NonNull Integer key) throws Exception {
                        log.debug("Load Post Rows From Database");
                        return discussPostMapper.selectPostRows(key);
                    }
                });
    }

    @Override
    public List<DiscussPost> getPosts(Integer userId, Integer offset, Integer limit, Integer orderMode) {
        if (userId == 0 && orderMode == 1) {
            return postListCache.get(offset + ":" + limit);
        }
        log.debug("Load Posts From DataBase");
        return discussPostMapper.selectPostList(userId, offset, limit, orderMode);
    }

    @Override
    public Integer getRowsCount(Integer userId) {
        if (userId == 0) {
            return postRowsCache.get(userId);
        }
        log.debug("Load Post Rows From DataBase");
        return discussPostMapper.selectPostRows(userId);
    }

    @Override
    public Integer addDiscussPost(DiscussPost discussPost) {
        if (discussPost == null) throw new IllegalArgumentException("参数不能为空!");
        // 转义html标记
        discussPost.setTitle(HtmlUtils.htmlEscape(discussPost.getTitle()));
        discussPost.setContent(HtmlUtils.htmlEscape(discussPost.getContent()));
        // 过滤敏感词
        discussPost.setTitle(sensitiveFilter.filter(discussPost.getTitle()));
        discussPost.setContent(sensitiveFilter.filter(discussPost.getContent()));

        discussPost.setStatus(0);
        discussPost.setType(0);
        discussPost.setCommentCount(0);

        return discussPostMapper.insertDiscussPost(discussPost);
    }

    @Override
    public DiscussPost findDiscussPost(Integer id) {
        return discussPostMapper.selectDiscussPostById(id);
    }

    @Override
    public Integer updateCommentCount(Integer id, Integer commentCount) {
        return discussPostMapper.updateCommentCount(id, commentCount);
    }

    @Override
    public Comment findCommentById(Integer id) {
        return discussPostMapper.selectCommentById(id);
    }

    @Override
    public Integer updateTypeById(Integer id, Integer type) {
        return discussPostMapper.updateType(id, type);
    }

    @Override
    public Integer updateStatusById(Integer id, Integer status) {
        return discussPostMapper.updateStatus(id, status);
    }

    @Override
    public DiscussPost findPostById(Integer id) {
        return discussPostMapper.selectDiscussPostById(id);
    }

    @Override
    public Integer updateScore(Integer id, double score) {
        return discussPostMapper.updateScore(id, score);
    }


}

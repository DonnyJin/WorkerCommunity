package com.donny.community.quartz;

import com.donny.community.entity.DiscussPost;
import com.donny.community.service.DiscussPostService;
import com.donny.community.service.ElasticSearchService;
import com.donny.community.service.LikeService;
import com.donny.community.util.CommunityConstant;
import com.donny.community.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@Component
public class PostScoreJob implements Job, CommunityConstant {

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private LikeService likeService;

    @Autowired
    private ElasticSearchService elasticSearchService;

    @Autowired
    @Qualifier("redisTemplateConfig")
    private RedisTemplate redisTemplate;

    private static final Date epoch;

    static {
        try {
            epoch = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2022-09-22 00:00:00");
        } catch (ParseException e) {
            throw new RuntimeException("初始化评分初始日期失败");
        }
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        String key = RedisUtil.getPostScoreKey();
        BoundSetOperations op = redisTemplate.boundSetOps(key);
        if (op.size() == 0) {
            log.info("没有需要刷新评分的帖子");
            return;
        }
        log.info("正在刷新分数的帖子:" + op.size());

        while (op.size() > 0) {
            this.refresh((Integer) op.pop());
        }

        log.info("帖子分数刷新完毕!");

    }

    private void refresh(Integer postId) {
        DiscussPost post = discussPostService.findPostById(postId);

        if (post == null) {
            log.error("该帖子不存在:{}", postId);
            return;
        }
        // 是否精华
        boolean wonderful = post.getStatus() == 1;
        // 评论数量
        Integer commentCount = post.getCommentCount();
        // 点赞数量
        Long likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_POST, postId);

        // 计算权重
        double w = (wonderful ? 75 : 0) + commentCount * 10 + likeCount * 2;
        // 分数 = 权重 + 距离天数
        double score = Math.log10(Math.max(1, w))
                + (post.getCreateTime().getTime() - epoch.getTime()) / (1000 * 3600 * 24);

        discussPostService.updateScore(postId, score);
        post.setScore(score);
        // 同步搜索数据
        elasticSearchService.saveDiscussPost(post);

    }
}

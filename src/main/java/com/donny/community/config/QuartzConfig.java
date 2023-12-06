package com.donny.community.config;

import com.donny.community.quartz.PostScoreJob;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;


/**
 *  配置 -> 数据库
 *  只有第一次初始化读取配置
 *  以后读取数据库
 */
@Configuration
public class QuartzConfig {
    // FactoryBean可简化Bean的实例化过程:
    // 1.通过FactoryBean封装了Bean的实例化过程
    // 2.可以将FactoryBean装配到Spring容器里
    // 3.将FactoryBean注入给其他的Bean, 该Bean将得到FactoryBean所管理的对象实例


    // 刷新帖子分数的任务
    @Bean
    public JobDetailFactoryBean scoreJobDetail() {
        JobDetailFactoryBean factoryBean = new JobDetailFactoryBean();
        factoryBean.setJobClass(PostScoreJob.class);
        factoryBean.setName("postScoreJob");
        factoryBean.setGroup("communityJobGroup");
        factoryBean.setDurability(true);
        factoryBean.setRequestsRecovery(true);
        return factoryBean;
    }


    @Bean
    public SimpleTriggerFactoryBean scoreTrigger(JobDetail scoreJobDetail) {
        SimpleTriggerFactoryBean factoryBean = new SimpleTriggerFactoryBean();
        factoryBean.setJobDetail(scoreJobDetail);
        factoryBean.setName("postScoreTrigger");
        factoryBean.setGroup("communityTriggerGroup");
        factoryBean.setRepeatInterval(1000 * 60 * 5);
        factoryBean.setJobDataMap(new JobDataMap());
        return factoryBean;
    }
}

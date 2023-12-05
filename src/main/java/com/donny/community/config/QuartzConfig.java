package com.donny.community.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;


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
}

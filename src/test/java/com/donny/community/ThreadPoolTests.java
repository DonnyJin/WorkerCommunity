package com.donny.community;


import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@SpringBootTest
@Slf4j
public class ThreadPoolTests {


    // JDK普通线程池
    private ExecutorService executorService = Executors.newFixedThreadPool(5);

    // JDK可执行定时任务的线程池
    private ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(5);

    // Spring普通线程池
    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    // Spring可执行定时任务线程池
    @Autowired
    private ThreadPoolTaskScheduler taskScheduler;

    private void sleep(long m) {
        try {
            Thread.sleep(m);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // 1.JDK普通线程池
    @Test
    void testExecutorService() {
        Runnable task = new Runnable() {
            @Override
            public void run() {
                log.debug("Hello ExecutorService");
            }
        };

        for (int i = 0 ; i < 10 ; i++) {
            executorService.submit(task);
        }
        sleep(10000);
    }

    /**
     * Spring的普通线程
     */
    @Test
    void testThreadPoolTaskExecutor() {
        Runnable task = new Runnable() {
            @Override
            public void run() {
                log.debug("Hello ThreadPoolTaskExecutor");
            }
        };
        for (int i = 0 ; i < 10 ; i++) {
            taskExecutor.submit(task);
        }
        sleep(10000);
    }

    /**
     * Spring定时任务线程
     */
    @Test
    void testThreadPoolTaskScheduler() {
        Runnable task = new Runnable() {
            @Override
            public void run() {
                log.debug("Hello ThreadPoolTaskExecutor");
            }
        };
        Date start = new Date(System.currentTimeMillis() + 10000);
        taskScheduler.scheduleAtFixedRate(task, start, 1000);

        sleep(30000);
    }

    @Async
    void execute1() {
        log.debug("execute1");
    }

    @Scheduled(initialDelay = 10000, fixedRate = 1000)
    void execute2() {
        log.debug("execute2");
    }

    @Test
    void testThreadPoolTaskExecutorSimple() {
        for (int i = 0 ; i < 10 ; i++) {
            this.execute1();
        }

        sleep(10000);
    }
    /**
     * 简化版Spring定时任务线程池
     */
    @Test
    void testThreadPoolTaskScheduledrSimple() {
        sleep(30000);
    }
}

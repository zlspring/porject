package com.aohui.btcorg

import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.SchedulingConfigurer
import org.springframework.scheduling.config.ScheduledTaskRegistrar
import java.util.concurrent.Executors

/**
 * 定时任务线程池配置
 *
 */
@Configuration
@EnableScheduling
class SchedulingConfig : SchedulingConfigurer {

    val logger = LoggerFactory.getLogger("SchedulingThreadPool")!!

    override fun configureTasks(taskRegistrar: ScheduledTaskRegistrar) {
        val coreNum = Runtime.getRuntime().availableProcessors()
        logger.info("set thread-pool-schedule to ${coreNum * 25}")
        taskRegistrar.setScheduler(Executors.newScheduledThreadPool(coreNum * 25))
    }
}
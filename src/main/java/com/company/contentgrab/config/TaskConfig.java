package com.company.contentgrab.config;

import com.company.contentgrab.crawl.ContentGrab;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

@Slf4j
@Configuration
public class TaskConfig {

    @Autowired
    private ContentGrab contentGrab;

    @Scheduled(cron = "0/30 * * * * *")
    public void grabTask(){
        log.info("执行抓取任务");
        contentGrab.process();
    }


}

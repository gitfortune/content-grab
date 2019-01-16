package com.company.contentgrab.config;

import com.company.contentgrab.crawl.ContentGrab;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TaskConfig {

    @Autowired
    private ContentGrab contentGrab;

//    @Scheduled(cron = "0 0 8,12 * * ?")
//    @Scheduled(cron = "${scheduled}")
    @Scheduled(cron = "0/5 * * * * *")
    public void grabTask() throws InterruptedException {
        log.info("执行抓取任务");
        Thread.sleep(1000);
        contentGrab.process();
    }


}

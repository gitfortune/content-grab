package com.hnradio.contentgrab;

import com.hnradio.contentgrab.crawl.GrabCNR;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.company.contentgrab.api"})
@EnableHystrix
public class ContentGrabApplication {

    @Autowired
    private GrabCNR grabCNR;

    public static void main(String[] args) {
        SpringApplication.run(ContentGrabApplication.class, args);
    }
}


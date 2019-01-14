package com.company.contentgrab;

import com.company.contentgrab.api.ArticleServiceAPI;
import com.company.contentgrab.crawl.ContentGrab;
import com.company.contentgrab.enmu.ResultEnmu;
import com.company.contentgrab.entity.ArticleDTO;
import com.company.contentgrab.exception.GrabException;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ContentGrabApplicationTests {

    @Test
    public void contextLoads() {
    }

    @Autowired
    ArticleServiceAPI articleServiceAPI;

    @Autowired
    ContentGrab contentGrab;

    @Test
    public void save(){
//        contentGrab.saveNews("");
    }

}


package com.hnradio.contentgrab;

import com.hnradio.contentgrab.api.ArticleServiceAPI;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ContentGrabApplicationTests {

    @Test
    public void contextLoads() {
    }

    @Autowired
    ArticleServiceAPI articleServiceAPI;


    @Test
    public void save(){
//        contentGrab.saveNews("");
    }

}


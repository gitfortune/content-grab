package com.hnradio.contentgrab;

import lombok.extern.slf4j.Slf4j;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class Test {

    @org.junit.Test
    public void test(){
        String str = "祎";
        log.info(str);
    }

}

package com.hnradio.contentgrab.crawl;

import com.hnradio.contentgrab.api.ArticleServiceAPI;
import com.hnradio.contentgrab.common.RestResponse;
import com.hnradio.contentgrab.enmu.ResultEnmu;
import com.hnradio.contentgrab.entity.ArticleDTO;
import com.hnradio.contentgrab.exception.GrabException;
import com.hnradio.contentgrab.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 *抓取大河网新闻
 */
@Component
@Slf4j
public class GrabDaHe {

    @Autowired
    ArticleServiceAPI articleServiceAPI;

    @Value("${grab.dahe.url}")
    String url;

    /**
     * 抓取大河网新闻列表链接
     */
    @Scheduled(cron = "${grab.dahe.cron}")
    public void grabDaHeLinks(){
        try {
            Document doc = Jsoup.connect(url).get();
            //根据页面html的结构，获取该页面的新闻列表里的url
            //获取新闻列表 li标签集合
            Elements lis = doc.select("#content li");
            for (Element li : lis) {
                String linkHref = li.getElementsByTag("a").attr("href");
                //截取新闻标题后面的时间(年月日格式)
                String date = li.getElementsByTag("span").text().substring(0, 10);
                //文章时间的时分秒
                String time = li.getElementsByTag("span").text().substring(11);
                if(DateUtil.isToday(date)){
                    //是当天且符合条件的新闻，继续解析
                    this.parseDaheNewsHtml(linkHref);
                }
            }
        } catch (IOException e) {
            log.error("JSOUP获取大河网新闻列表时发生异常：{}",e.getMessage());
            throw new GrabException(ResultEnmu.JSOUP_FAIL);
        }
    }

    /**
     * 解析大河网新闻文章页面
     */
    private void parseDaheNewsHtml(String url){
        ArticleDTO articleDTO = new ArticleDTO();
        try {
            Document doc = Jsoup.connect(url).get();
            articleDTO.setLinkTo(url);
            //标题
            articleDTO.setArticleTitle(doc.getElementById("4g_title").text());
            articleDTO.setContentTitle(doc.getElementById("4g_title").text());
            //发布时间
            String time = doc.getElementById("pubtime_baidu").text()
                    .replace("年", "-")
                    .replace("月", "-")
                    .replace("日", " ");
            //大河新闻的发布时间缺少 秒，补全
            articleDTO.setPublishTime(time+":00");
            //来源
            articleDTO.setArticleOrigin(doc.getElementById("source_baidu").text().substring(3));
            //内容
            articleDTO.setContentBody(doc.getElementById("mainCon").html());
            //作者,大河网个别文章的编辑后面会加上审核：xxx字样，这里将它去除，只保留编辑姓名
            String editor_baidu = doc.getElementById("editor_baidu").text();
            if(editor_baidu.contains("审核")){
                articleDTO.setArticleAuthor(editor_baidu.substring(3,editor_baidu.indexOf("审")).trim());
            }else {
                articleDTO.setArticleAuthor(editor_baidu.substring(3));
            }
        } catch (IOException e) {
            log.error("JSOUP解析大河网文章时发生异常：{}",e.getMessage());
            throw new GrabException(ResultEnmu.JSOUP_FAIL);
        }
        //Feign调用微服务hnradio-cms的方法 保存数据
        RestResponse<ArticleDTO> articleDTORestResponse = articleServiceAPI.create("-1", "-1", articleDTO);
        log.info("状态码："+articleDTORestResponse.getCode());
    }
}

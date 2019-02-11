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
 * 抓取央广新闻
 */
@Component
@Slf4j
public class GrabCNR {

    @Autowired
    ArticleServiceAPI articleServiceAPI;

    @Value("${grab.cnr.url}")
    String url;

    /**
     * 抓取央广新闻链接
     */
    @Scheduled(cron = "${grab.cnr.cron}")
    public void grabCNRLinks(){
        try {
            Document doc = Jsoup.connect(url).get();
            //根据页面html的结构，获取该页面的新闻列表里的url
            //获取新闻列表 li标签集合
            Elements lis = doc.select(".articleList li");
            for (Element li : lis) {
                String linkHref = li.select(".text a").attr("href");
                //截取新闻标题后面的时间-年月日格式
                String date = li.select(".publishTime").text().substring(0, 10);    //时间
                String time = li.select(".publishTime").text().substring(11);
                if(DateUtil.isToday(date)){
                    log.info("央广时间："+time);
                    //是当天新闻，继续解析
                    this.parseCNRNewsHtml(linkHref);
                }
            }
        } catch (IOException e) {
            log.error("JSOUP获取HTML时发生错误：{}",e);
            throw new GrabException(ResultEnmu.JSOUP_FAIL);
        }
    }


    /**
     * 解析央广新闻文章页面
     * @param url
     */
    private void parseCNRNewsHtml(String url){
        ArticleDTO articleDTO = new ArticleDTO();
        try {
            articleDTO.setLinkTo(url);
            Document doc = Jsoup.connect(url).get();
            //标题
            articleDTO.setArticleTitle(doc.select(".subject h2").text());
            articleDTO.setContentTitle(doc.select(".subject h2").text());
            //发布时间
            articleDTO.setPublishTime(doc.select(".source span").first().text());
            //来源
            articleDTO.setArticleOrigin("央广网");
            //内容
            articleDTO.setContentBody(doc.getElementsByClass("TRS_Editor").html());
            //作者
            String str = doc.getElementsByClass("editor").text();
            String editor = str.substring(4);
            log.info(editor);
            articleDTO.setArticleAuthor(editor);
        } catch (IOException e) {
            //这个异常发生在for循环内部，此处不要throw异常，否则for循环会停止。
            log.error("JSOUP解析央广新闻文章时发生异常：{},异常文章url：{}",e,url);
        }
        //Feign调用微服务hnradio-cms的方法 保存数据
        RestResponse<ArticleDTO> articleDTORestResponse = articleServiceAPI.create("-1", "-1", articleDTO);
        log.info("状态码："+articleDTORestResponse.getCode());
    }
}

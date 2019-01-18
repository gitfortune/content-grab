package com.hnradio.contentgrab.crawl;

import com.hnradio.contentgrab.api.ArticleServiceAPI;
import com.hnradio.contentgrab.common.RestResponse;
import com.hnradio.contentgrab.enmu.ResultEnmu;
import com.hnradio.contentgrab.entity.ArticleDTO;
import com.hnradio.contentgrab.exception.GrabException;
import com.hnradio.contentgrab.utils.DateUtil;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;

/**
 * 抓取新浪河南新闻
 */
@Component
@Slf4j
public class GrabSina {

    @Autowired
    ArticleServiceAPI articleServiceAPI;

    @Value("${grab.sina.url}")
    String url;

    /**
     * 抓取新浪河南新闻链接（动态页面，Jsoup无法解析，需要使用htmlUnit）
     */
    @Scheduled(cron = "${grab.sina.cron}")
    public void grabSinaLinks(){
        int year = LocalDate.now().getYear();
        // HtmlUnit 模拟浏览器
        WebClient webClient = new WebClient(BrowserVersion.CHROME);
        webClient.getOptions().setJavaScriptEnabled(true);              // 启用JS解释器，默认为true
        webClient.getOptions().setCssEnabled(false);                    // 禁用css支持
        webClient.getOptions().setThrowExceptionOnScriptError(false);   // js运行错误时，是否抛出异常
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        webClient.getOptions().setTimeout(30000);                   // 设置连接超时时间30s
        HtmlPage page = null;
        try {
            page = webClient.getPage(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        webClient.waitForBackgroundJavaScript(20000);               // 等待js后台执行20秒

        String pageAsXml = page.asXml();

        Document doc = Jsoup.parse(pageAsXml);
        //根据页面html的结构，获取该页面的新闻列表里的url
        Element element = doc.getElementById("listArticle_page_0");
        //获取li标签集合
        Elements lis = element.getElementsByTag("li");
        for (Element li : lis) {
            String linkHref = li.select("h3 a").attr("href");
            String str = li.getElementsByClass("time").text().substring(0,5);
            String time = li.getElementsByClass("time").text().substring(6);

            //按年月日格式，拼接时间
            String date = year + "-" + str;
            if(DateUtil.isToday(date)){
                log.info("新浪时间："+time);
                //是当天新闻，继续解析
                this.parseSinaNewsHtml(linkHref);
            }
        }
    }

    /**
     * 解析新浪河南新闻文章页面
     * @param url
     */
    private void parseSinaNewsHtml(String url) {
        ArticleDTO articleDTO = new ArticleDTO();
        try {
            Document doc = Jsoup.connect(url).get();
            articleDTO.setLinkTo(url);
            //标题
            articleDTO.setArticleTitle(doc.select("#artibody h1").text());
            articleDTO.setContentTitle(doc.select("#artibody h1").text());
            //发布时间
            articleDTO.setPublishTime(doc.select(".source-time span").first().text()+":00");
            //来源
            articleDTO.setArticleOrigin(doc.getElementById("art_source").text());

            //新浪河南文章内容包含广告，js代码，样式，以及其他无用代码，需要先把这些都清除
            doc.select(".news_weixin_ercode").remove();
            String unsafe = doc.select(".article-body").html();
            //使用Whitelist.relaxed()这个过滤器允许的标签最多
            String safe = Jsoup.clean(unsafe, Whitelist.relaxed());
            //内容
            articleDTO.setContentBody(safe);
        } catch (Exception e) {
            //这个异常发生在for循环内部，此处不要throw异常，否则for循环会停止。
            log.error("JSOUP解析新浪河南文章时发生异常：{},异常文章url：{}",e.getMessage(),url);
        }
        //Feign调用微服务hnradio-cms的方法 保存数据
        RestResponse<ArticleDTO> articleDTORestResponse = articleServiceAPI.create("-1", "-1", articleDTO);
        log.info("状态码："+articleDTORestResponse.getCode());
    }
}

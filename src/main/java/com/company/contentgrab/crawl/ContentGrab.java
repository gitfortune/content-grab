package com.company.contentgrab.crawl;

import com.company.contentgrab.api.ArticleServiceAPI;
import com.company.contentgrab.common.RestResponse;
import com.company.contentgrab.enmu.ResultEnmu;
import com.company.contentgrab.entity.ArticleDTO;
import com.company.contentgrab.exception.GrabException;
import com.company.contentgrab.utils.DateUtil;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;

/**
 * 内容抓取
 */
@Component
@Slf4j
public class ContentGrab {

    //大河网
    @Value("${grab.dahe}")
    String dahe;
    //央广
    @Value("${grab.cnr}")
    String cnr;
    //新浪河南
    @Value("${grab.sina}")
    String sina;

    @Autowired
    ArticleServiceAPI articleServiceAPI;

    LocalDate date = LocalDate.now();

    public void process(){
//        grabCNRLinks(cnr);
//        grabSinaLinks(sina);
//        grabDaHeLinks(dahe);
        LocalDate parse = LocalDate.parse("2019-12-03");
        log.info(parse.toString()+"");
    }

    /**
     * 抓取大河网新闻列表链接
     */
    public void grabDaHeLinks(String url){
        try {
            Document doc = Jsoup.connect(url).get();
            //根据页面html的结构，获取该页面的新闻列表里的url
            //获取新闻列表 li标签集合
            Elements lis = doc.select("#content li");
            for (Element li : lis) {
                String linkHref = li.getElementsByTag("a").attr("href");
                String linkText = li.getElementsByTag("a").text();
                //截取年月日 时间
                String time = li.getElementsByTag("span").text().substring(0, 10);
                if(DateUtil.isToday(time)){
                    //是当天新闻，继续解析
                    this.parseDaheNewsHtml(linkHref);
                }
//                log.info(linkHref+""+linkText+ ""+time);
            }
        } catch (IOException e) {
            log.error("JSOUP获取大河网新闻列表时发生异常：{}",e.getMessage());
            throw new GrabException(ResultEnmu.JSOUP_FAIL);
        }
    }

    /**
     * 解析大河网新闻文章页面
     */
    public void parseDaheNewsHtml(String url){
        try {
            ArticleDTO articleDTO = new ArticleDTO();
            Connection conn = Jsoup.connect(url).timeout(5000);
            conn.header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
            conn.header("Accept-Encoding", "gzip, deflate, sdch");
            conn.header("Accept-Language", "zh-CN,zh;q=0.8");
            conn.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36");

            Document doc = conn.get();
            log.info("标题："+doc.getElementById("4g_title").text());
            Element pubtime = doc.getElementById("pubtime_baidu");
            log.info("发布时间"+pubtime.text());
            Element source = doc.getElementById("source_baidu");
            log.info("来源"+source.text());
            Element body = doc.getElementById("mainCon");
            Elements imgs = body.getElementsByTag("img");
            for (Element img : imgs){
                log.info("图片："+img.attr("src"));
            }
            log.info("内容"+body.html());
            Element editor = doc.getElementById("editor_baidu");
            log.info("编辑"+editor.text());
            //标题
            articleDTO.setArticleTitle(doc.getElementById("4g_title").text());
            articleDTO.setContentTitle(doc.getElementById("4g_title").text());
            //发布时间
            articleDTO.setPublishTime(doc.getElementById("pubtime_baidu").text());
            //来源
            articleDTO.setArticleOrigin(doc.getElementById("source_baidu").text());
            //内容
            articleDTO.setContentBody(doc.getElementById("mainCon").html());
            //作者
            articleDTO.setArticleAuthor(doc.getElementById("editor_baidu").text());
            RestResponse<ArticleDTO> articleDTORestResponse = articleServiceAPI.create("-1", "-1", articleDTO);
            log.info("状态码："+articleDTORestResponse.getCode());
        } catch (IOException e) {
            log.error("JSOUP解析大河网文章时发生异常：{}",e.getMessage());
            throw new GrabException(ResultEnmu.JSOUP_FAIL);
        }
    }


    /**
     * 抓取央广新闻链接
     */
    public void grabCNRLinks(String url){
        int i = 0;
        try {
            Document doc = Jsoup.connect(url).get();
            //根据页面html的结构，获取该页面的新闻列表里的url
            //获取新闻列表 li标签集合
            Elements lis = doc.select(".articleList li");
            for (Element li : lis) {
                String linkHref = li.select(".text a").attr("href");
//                this.saveNews(linkHref);
                String linkText = li.select(".text a").text();
                String time = li.select(".publishTime").text().substring(0, 10);    //时间
                log.info(linkHref+""+linkText+ ""+time);
            }
        } catch (IOException e) {
            log.error("JSOUP获取HTML时发生错误：{}",e.getMessage());
            throw new GrabException(ResultEnmu.JSOUP_FAIL);
        }
    }


    /**
     * 抓取新浪河南新闻链接
     */
    public void grabSinaLinks(String url){

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
        webClient.waitForBackgroundJavaScript(10000);               // 等待js后台执行10秒

        String pageAsXml = page.asXml();

        int i = 0;
        Document doc = Jsoup.parse(pageAsXml);
        //根据页面html的结构，获取该页面的新闻列表里的url
//        Elements links = doc.select("#listArticle_page_0 h3 a");
        Element element = doc.getElementById("listArticle_page_0");
        //获取li标签集合
        Elements lis = element.getElementsByTag("li");
        for (Element li : lis) {
            Elements time = li.getElementsByClass("time");
//            time.text().substring(0,5);   //时间
            String linkHref = li.select("h3 a").attr("href");
//                this.saveNews(linkHref);
            String linkText = li.select("h3 a").text();
            log.info(linkHref+""+linkText+ time.text().substring(0,5));
        }
    }

    /**
     * 保存
     * @param url
     */
    public void saveNews(String url){
        String url1 = "https://news.dahe.cn/2018/09-16/376490.html";

    }

}

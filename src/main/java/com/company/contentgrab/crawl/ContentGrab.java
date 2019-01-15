package com.company.contentgrab.crawl;

import com.company.contentgrab.api.ArticleServiceAPI;
import com.company.contentgrab.common.RestResponse;
import com.company.contentgrab.enmu.ResultEnmu;
import com.company.contentgrab.entity.ArticleDO;
import com.company.contentgrab.entity.ArticleDTO;
import com.company.contentgrab.exception.GrabException;
import com.company.contentgrab.service.HainaService;
import com.company.contentgrab.utils.DateUtil;
import com.company.contentgrab.utils.StringUtil;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

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

    @Autowired
    private HainaService hainaService;

    public void process(){
//        grabCNRLinks(cnr);
//        grabSinaLinks(sina);
//        grabDaHeLinks(dahe);
//        parseDaheNewsHtml("https://news.dahe.cn/2019/01-14/436106.html");
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
                //截取新闻标题后面的时间(年月日格式)
                String time = li.getElementsByTag("span").text().substring(0, 10);
                if(DateUtil.isToday(time)){
                    //是当天新闻，继续解析
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
            Connection conn = Jsoup.connect(url).timeout(5000);
            conn.header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
            conn.header("Accept-Encoding", "gzip, deflate, sdch");
            conn.header("Accept-Language", "zh-CN,zh;q=0.8");
            conn.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36");

            Document doc = conn.get();

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
            articleDTO.setArticleAuthor(doc.getElementById("editor_baidu").text().substring(3,7).trim());
        } catch (IOException e) {
            log.error("JSOUP解析大河网文章时发生异常：{}",e.getMessage());
            throw new GrabException(ResultEnmu.JSOUP_FAIL);
        }
        this.saveNews(articleDTO);
    }


    /**
     * 抓取央广新闻链接
     */
    public void grabCNRLinks(String url){
        try {
            Document doc = Jsoup.connect(url).get();
            //根据页面html的结构，获取该页面的新闻列表里的url
            //获取新闻列表 li标签集合
            Elements lis = doc.select(".articleList li");
            for (Element li : lis) {
                String linkHref = li.select(".text a").attr("href");
                //截取新闻标题后面的时间-年月日格式
                String time = li.select(".publishTime").text().substring(0, 10);    //时间
                if(DateUtil.isToday(time)){
                    //是当天新闻，继续解析
                    this.parseCNRNewsHtml(linkHref);
                }
            }
        } catch (IOException e) {
            log.error("JSOUP获取HTML时发生错误：{}",e.getMessage());
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
            log.error("JSOUP解析央广新闻文章时发生异常：{}",e.getMessage());
            throw new GrabException(ResultEnmu.JSOUP_FAIL);
        }
        this.saveNews(articleDTO);
    }


    /**
     * 抓取新浪河南新闻链接（动态页面，Jsoup无法解析，需要使用htmlUnit）
     */
    public void grabSinaLinks(String url){
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
            String str = li.getElementsByClass("time").text().substring(0,5);
            String linkHref = li.select("h3 a").attr("href");
            //按年月日格式，拼接时间
            String time = year + "-" + str;
            if(DateUtil.isToday(time)){
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
            Document doc = Jsoup.connect("http://henan.sina.com.cn/news/z/2019-01-14/detail-ihqfskcn6862290.shtml").get();
            //标题
            articleDTO.setArticleTitle(doc.select("#artibody h1").text());
            articleDTO.setContentTitle(doc.select("#artibody h1").text());
            //发布时间
            articleDTO.setPublishTime(doc.select(".source-time span").first().text());
            //来源
            articleDTO.setArticleOrigin(doc.getElementById("art_source").text());

            //新浪河南文章内容包含广告，js代码，样式，以及其他无用代码，需要先把这些都清除
            doc.select(".news_weixin_ercode").remove();
            String unsafe = doc.select(".article-body").html();
            //使用Whitelist。relaxed()这个过滤器允许的标签最多
            String safe = Jsoup.clean(unsafe,Whitelist.relaxed());
            //内容
            articleDTO.setContentBody(safe);
        } catch (IOException e) {
            log.error("JSOUP解析新浪河南文章时发生异常：{}",e.getMessage());
            throw new GrabException(ResultEnmu.JSOUP_FAIL);
        }
        this.saveNews(articleDTO);

    }

    /**
     * Feign调用微服务hnradio-cms的方法 保存数据
     */
    public void saveNews(ArticleDTO articleDTO){
        RestResponse<ArticleDTO> articleDTORestResponse = articleServiceAPI.create("-1", "-1", articleDTO);
        log.info("状态码："+articleDTORestResponse.getCode());
    }

    private void hainaProcess(){
        try {
            List<ArticleDO> list = hainaService.listNotPublish();
            if (list != null && list.size() > 0) {
                //首先修改最大id
                log.info("海纳采集合并到CMS开始");
                long maxId = list.stream().max(Comparator.comparing(ArticleDO::getId)).orElse(new ArticleDO()).getId();
                log.info("海纳采集合并到CMS开始编号:" + maxId + "/数量:" + list.size());
                hainaService.updateUsedId(maxId);

                for (ArticleDO bean : list) {
                    ArticleDTO articleDTO = new ArticleDTO();
                    /*if (ArticleBusiness.getInstance().contains(bean)) {
                        continue;
                    }*/
                    articleDTO.setLinkTo(bean.getFromUrl());//采集源地址存储到link_to
                    articleDTO.setChannelId(bean.getChannelId() > 0 ? bean.getChannelId()+"" : "1262");//暂时发布到测试栏目
//                    articleDTO.setId(0);//在cms里存储新闻
                    articleDTO.setContentTitle(StringUtil.processQuotationMarks(bean.getTitle()));
                    articleDTO.setContentTitle(StringUtil.processQuotationMarks(bean.getTitleHome()));
                    articleDTO.setSeoDescription(StringUtil.processQuotationMarks(bean.getDescription()));

//                    ArticleBusiness.getInstance().doArticleBody(bean);
//                    int r = ArticleBusiness.getInstance().save(bean);
                    //资讯类直接发布
                    /*if (r > 0 && bean.getChannel_id() == 102) {
                        TaskPoolExecutor.getInstance().submitArticlePublishAsTask(bean);
                        bean.setPublish_time(Utils.currentTimestamp());
                    }
                    if (r > 0) {
                        LogBean logBean = new LogBean(1206, "海纳批量", " 保存并发布文章", "保存并发布文章:" + bean.getTitle() + "[" + bean.getId() + "]");
                        logBean.setArticle_id(bean.getId());
                        logBean.setChannel_id(bean.getChannel_id());
                        LogBusiness.getInstance().addArticleLog(logBean);
                    }*/
                    this.saveNews(articleDTO);
                }
            }
        } catch (Exception e) {
            log.error("海纳定时异常:"+e.getMessage());
        }

    }

}

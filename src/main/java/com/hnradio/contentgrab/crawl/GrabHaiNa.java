package com.hnradio.contentgrab.crawl;

import com.hnradio.contentgrab.api.ArticleServiceAPI;
import com.hnradio.contentgrab.common.RestResponse;
import com.hnradio.contentgrab.enmu.ResultEnmu;
import com.hnradio.contentgrab.entity.ArticleDO;
import com.hnradio.contentgrab.entity.ArticleDTO;
import com.hnradio.contentgrab.exception.GrabException;
import com.hnradio.contentgrab.service.HainaService;
import com.hnradio.contentgrab.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * 抓取海纳系统数据
 */
@Component
@Slf4j
public class GrabHaiNa {

    @Autowired
    ArticleServiceAPI articleServiceAPI;

    @Autowired
    private HainaService hainaService;

    @Scheduled(cron = "${grab.haina.cron}")
    public void hainaProcess(){
        try {
            List<ArticleDO> list = hainaService.listNotPublish();
            if (list != null && list.size() > 0) {
                //首先修改最大id
                log.info("海纳采集合并到CMS开始");
                long maxId = list.stream().max(Comparator.comparing(ArticleDO::getId)).orElse(new ArticleDO()).getId();
                Optional<ArticleDO> max = list.stream().max(Comparator.comparing(ArticleDO::getId));
                log.info("海纳采集合并到CMS开始编号:" + maxId + "/数量:" + list.size());
                hainaService.updateUsedId(maxId);
                for (ArticleDO bean : list) {
                    ArticleDTO articleDTO = new ArticleDTO();

                    articleDTO.setLinkTo(bean.getFromUrl());//采集源地址存储到link_to
                    articleDTO.setChannelId(bean.getChannelId() > 0 ? bean.getChannelId()+"" : "1262");//暂时发布到测试栏目
//                    articleDTO.setId(0);//在cms里存储新闻
                    articleDTO.setContentTitle(StringUtil.processQuotationMarks(bean.getTitle()));
                    articleDTO.setArticleTitle(StringUtil.processQuotationMarks(bean.getTitleHome()));
                    articleDTO.setSeoDescription(StringUtil.processQuotationMarks(bean.getDescription()));
                    articleDTO.setSeoKeywords(StringUtil.processQuotationMarks(bean.getKeywords()));
                    articleDTO.setContentBody(bean.getBody());
                    articleDTO.setArticleOrigin(bean.getOrigin());

                    //Feign调用微服务hnradio-cms的方法 保存数据
                    RestResponse<ArticleDTO> articleDTORestResponse = articleServiceAPI.create("-1", "-1", articleDTO);
                    log.info("状态码："+articleDTORestResponse.getCode());
                }
            }
        } catch (Exception e) {
            log.error("海纳定时异常:"+e);
            throw new GrabException(ResultEnmu.HAINA_FAIL);
        }
    }
}

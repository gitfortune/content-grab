package com.hnradio.contentgrab.service.impl;

import com.hnradio.contentgrab.entity.ArticleDO;
import com.hnradio.contentgrab.entity.CommonDO;
import com.hnradio.contentgrab.repository.ArticleRepository;
import com.hnradio.contentgrab.repository.CommonRepository;
import com.hnradio.contentgrab.service.HainaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

@Service
public class HainaServiceImpl implements HainaService {

    @Autowired
    private CommonRepository commonRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Override
    public List<ArticleDO> listNotPublish() {
        BigInteger startId = findUsedIdById();
        //海纳数据库中article的id是bigint对应long，common的used_id是bigint(20) unsigned 对应BigInteger，
        //两者不一致，不改字段类型的情况下，先这样处理
        List<ArticleDO> articleList = findArticleNotPub(startId.longValue());
        return articleList;
    }

    /**
     * 根据ID查询usedId
     * @return
     */
    private BigInteger findUsedIdById() {
        CommonDO commonDO = commonRepository.findById(1);
        return commonDO.getUsedId();
    }

    /**
     * 查询未发布
     * @param id
     * @return
     */
    private List<ArticleDO> findArticleNotPub(long id){
        List<ArticleDO> articleList = articleRepository.findByIdGreaterThan(id);
        return articleList;
    }

    @Override
    public int updateUsedId(long id){
        int i = commonRepository.updateUsedId(id);
        return i;
    }

}

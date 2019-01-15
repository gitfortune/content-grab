package com.company.contentgrab.service.impl;

import com.company.contentgrab.entity.ArticleDO;
import com.company.contentgrab.entity.CommonDO;
import com.company.contentgrab.repository.ArticleRepository;
import com.company.contentgrab.repository.CommonRepository;
import com.company.contentgrab.service.HainaService;
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
        //article的id是bigint对应long，common的used_id是bigint(20) unsigned 对应BigInteger，
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

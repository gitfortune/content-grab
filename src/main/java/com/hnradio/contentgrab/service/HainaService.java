package com.hnradio.contentgrab.service;

import com.hnradio.contentgrab.entity.ArticleDO;

import java.util.List;

public interface HainaService {

    /**
     * 查出未发布的文章
     * @return
     */
    List<ArticleDO> listNotPublish();

    /**
     * 更新common的usedId
     * @param id
     */
    int updateUsedId(long id);
}

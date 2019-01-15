package com.company.contentgrab.service;


import com.company.contentgrab.entity.ArticleDO;

import java.util.List;

public interface ArticleService{

    List<ArticleDO> listNotPublish(long startId);
}

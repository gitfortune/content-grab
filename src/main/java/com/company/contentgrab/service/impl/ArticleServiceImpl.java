package com.company.contentgrab.service.impl;

import com.company.contentgrab.entity.ArticleDO;
import com.company.contentgrab.repository.ArticleRepository;
import com.company.contentgrab.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleRepository repository;



    public List<ArticleDO> listNotPublish(long startId){

        ArticleDO articleDO = repository.findById(startId).get();

        return new ArrayList<>();
    }
}

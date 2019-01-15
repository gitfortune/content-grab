package com.company.contentgrab.repository;

import com.company.contentgrab.entity.ArticleDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<ArticleDO, Long>{

    List<ArticleDO> findByIdGreaterThan(long id);
}

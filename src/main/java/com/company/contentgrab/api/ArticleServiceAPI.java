package com.company.contentgrab.api;

import com.company.contentgrab.common.RestResponse;
import com.company.contentgrab.entity.ArticleDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * @Auther: alexgaoyh
 * @Date: 2019/1/4 09:02
 * @Description:
 */
@FeignClient(value = "cms-producer",fallback=ArticleRemoteHystrix.class)
public interface ArticleServiceAPI {

    @PostMapping(value = "/article")
    RestResponse<ArticleDTO> create(@RequestHeader(value = "hnrToken", defaultValue = "-1") String hnrToken,
                                           @RequestHeader(value = "hnrVersion", defaultValue = "1.0") String hnrVersion,
                                           @RequestBody ArticleDTO ArticleDTO);

}

@Component
class ArticleRemoteHystrix implements ArticleServiceAPI {

    @Override
    public RestResponse<ArticleDTO> create(String hnrToken, String hnrVersion, ArticleDTO ArticleDTO) {
        return RestResponse.validfail(ArticleDTO.toString());
    }

}
package com.hnradio.contentgrab.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 海纳-article实体类
 */
@Data
@Entity
@Table(name = "article")
@ApiModel(value = "ArticleDO", description = "海纳系统Article实体类")
public class ArticleDO implements Serializable {

    private static final long serialVersionUID = 6532319362650020783L;
    @Id
    @ApiModelProperty("文档ID")
    private long id;

    @ApiModelProperty("栏目ID")
    private int channelId;

    @ApiModelProperty("用户ID")
    private int managerId;

    @ApiModelProperty("0图文、1图集、2拼条、3引用、4URL、5投票、6调查、7单页")
    private int category;

    @ApiModelProperty("0新稿、1提交审核 2审核未通过 3已撤 4已删 10待发、11已发")
    private int status;

    @ApiModelProperty("内容标题")
    private String titleHome;

    @ApiModelProperty("正文标题")
    private String title;

    @ApiModelProperty("内容，category为34时，body能为空")
    private String body;

    @ApiModelProperty("标题样式，常见属性b,u,i, r(red),g(green),f焦点, h头条, c推荐, p有图, v视频，y热议。对应的样式是 global_title_[b] 样式文件在/resources/css/global.css")
    private String style;

    @ApiModelProperty("作者")
    private String author;

    @ApiModelProperty("来源")
    private String origin;

    @ApiModelProperty("分类1")
    private String categoryFirst;

    @ApiModelProperty("分类2")
    private String categorySecond;

    @ApiModelProperty("seo关键字")
    private String keywords;

    @ApiModelProperty("seo描述")
    private String description;

    @ApiModelProperty("0普通, 1置顶")
    private int top;

    @ApiModelProperty("0普通, 1忽略发布")
    private int hidden;

    @ApiModelProperty("来源网址")
    private String fromUrl;

}

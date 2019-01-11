package com.company.contentgrab.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Id;
import java.io.Serializable;

/**
 * @Auther: alexgaoyh
 * @Date: 2018/12/20 10:12
 * @Description:
 */
@Data
@ApiModel(value = "ArticleDTO", description = "文章")
public class ArticleDTO implements Serializable {

    /**
     *   文档编号
     */
    @Id
    @ApiModelProperty(value = "文档编号")
    private String articleId;

    /**
     *   文档编码
     */
    @ApiModelProperty(value = "文档编码")
    private String articleCode;

    /**
     *   栏目编号
     */
    @ApiModelProperty(value = "栏目编号")
    private String channelId;

    /**
     *   提取码
     */
    @ApiModelProperty(value = "提取码")
    private String extractCode;

    /**
     *   文档类型: 0图文、1图集、2拼条、3引用、4URL、5投票、6调查、7单页
     */
    @ApiModelProperty(value = "文档类型: 0图文、1图集、2拼条、3引用、4URL、5投票、6调查、7单页")
    private Integer articleType;

    /**
     *   文档状态:0新稿、1提交审核 2审核未通过 3已撤 4已删 10待发、11已发
     */
    @ApiModelProperty(value = "文档状态:0新稿、1提交审核 2审核未通过 3已撤 4已删 10待发、11已发")
    private Integer articleStatus;

    /**
     *   排序序号
     */
    @ApiModelProperty(value = "排序序号")
    private Long seqNo;

    /**
     *   文章标题
     */
    @ApiModelProperty(value = "文章标题")
    private String articleTitle;

    /**
     *   正文标题
     */
    @ApiModelProperty(value = "正文标题")
    private String contentTitle;

    /**
     *   内容，category为34时，body能为空
     */
    @ApiModelProperty(value = "内容，category为34时，body能为空")
    private String contentBody;

    /**
     *   文本对应的文件编号
     */
    @ApiModelProperty(value = "文本对应的文件编号")
    private String fileId;

    /**
     *   文本对应的文件路径
     */
    @ApiModelProperty(value = "文本对应的文件路径")
    private String filePath;

    /**
     *   标题样式，常见属性b,u,i, r(red),g(green),f焦点, h头条, c推荐, p有图, v视频，y热议。对应的样式是 global_title_[b] 样式文件在/resources/css/global.css
     */
    @ApiModelProperty(value = "标题样式，常见属性b,u,i, r(red),g(green),f焦点, h头条, c推荐, p有图, v视频，y热议。对应的样式是 global_title_[b] 样式文件在/resources/css/global.css")
    private String titleStyle;

    /**
     *   文档作者
     */
    @ApiModelProperty(value = "文档作者")
    private String articleAuthor;

    /**
     *   文档来源
     */
    @ApiModelProperty(value = "文档来源")
    private String articleOrigin;

    /**
     *   seo关键字
     */
    @ApiModelProperty(value = "seo关键字")
    private String seoKeywords;

    /**
     *   seo描述
     */
    @ApiModelProperty(value = "seo描述")
    private String seoDescription;

    /**
     *   文档类型为引用时,文档ID
     */
    @ApiModelProperty(value = "文档类型为引用时,文档ID")
    private String refersId;

    /**
     *   文档类型为URL时
     */
    @ApiModelProperty(value = "文档类型为URL时")
    private String linkTo;

    /**
     *   相关文档IDs
     */
    @ApiModelProperty(value = "相关文档IDs")
    private String relatedIds;

    /**
     *   文档附件
     */
    @ApiModelProperty(value = "文档附件")
    private String articleAttachments;

    /**
     *   正文图片
     */
    @ApiModelProperty(value = "正文图片")
    private String contentImages;

    /**
     *   正文视频
     */
    @ApiModelProperty(value = "正文视频")
    private String contentVideos;

    /**
     *   额外数据
     */
    @ApiModelProperty(value = "额外数据")
    private String extFields;

    /**
     *   0普通, 1置顶
     */
    @ApiModelProperty(value = "0普通, 1置顶")
    private Integer topFlag;

    /**
     *   0普通, 1忽略发布
     */
    @ApiModelProperty(value = "0普通, 1忽略发布")
    private Integer hiddenFlag;

    /**
     *   删除标识0 未删除, 1已删除
     */
    @ApiModelProperty(value = "删除标识0 未删除, 1已删除")
    private Integer deleteFlag;

    /**
     *   点击次数
     */
    @ApiModelProperty(value = "点击次数")
    private Long clickNum;

    /**
     *   状态改变时间
     */
    @ApiModelProperty(value = "状态改变时间")
    private String statusTime;

    /**
     *   发布日期
     */
    @ApiModelProperty(value = "发布日期")
    private String publishTime;

    /**
     *   创建人
     */
    @ApiModelProperty(value = "创建人")
    private String createUser;

    /**
     *   创建日期
     */
    @ApiModelProperty(value = "创建日期")
    private String createTime;

    /**
     *   创建人IP地址
     */
    @ApiModelProperty(value = "创建人IP地址")
    private String createIp;

    /**
     *   修改人
     */
    @ApiModelProperty(value = "修改人")
    private String modifyUser;

    /**
     *   修改日期
     */
    @ApiModelProperty(value = "修改日期")
    private String modifyTime;

    /**
     *   修改人IP
     */
    @ApiModelProperty(value = "修改人IP")
    private String modifyIp;

    /**
     *   授权标示
     */
    @ApiModelProperty(value = "授权标示")
    private String clientLicenseId;
}

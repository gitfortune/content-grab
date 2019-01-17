package com.hnradio.contentgrab.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigInteger;

@Data
@Entity
@Table(name = "common")
@DynamicUpdate
@ApiModel(value = "CommonDO", description = "海纳系统common实体类")
public class CommonDO implements Serializable {

    private static final long serialVersionUID = 1377668874800903374L;

    @Id
    @ApiModelProperty("主键")
    private int id;

    @ApiModelProperty("记录每次访问的文章id")
    private BigInteger usedId;

    @ApiModelProperty("内容")
    private String content;

}

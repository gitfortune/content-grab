package com.hnradio.contentgrab.enmu;

import lombok.Getter;

@Getter
public enum ResultEnmu {

    JSOUP_FAIL(1,"JSOUP获取HTML时发生异常"),
    HAINA_FAIL(2,"海纳存储到CMS发生异常"),
    ;




    private int code;

    private String msg;

    ResultEnmu(int code, String msg){
        this.code = code;
        this.msg = msg;
    }
}

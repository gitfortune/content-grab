package com.company.contentgrab.enmu;

import lombok.Getter;

@Getter
public enum ResultEnmu {

    JSOUP_FAIL(1,"JSOUP获取HTML时发生错误"),

    ;




    private int code;

    private String msg;

    ResultEnmu(int code, String msg){
        this.code = code;
        this.msg = msg;
    }
}

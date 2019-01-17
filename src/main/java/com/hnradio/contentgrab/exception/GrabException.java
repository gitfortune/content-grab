package com.hnradio.contentgrab.exception;

import com.hnradio.contentgrab.enmu.ResultEnmu;
import lombok.Data;

@Data
public class GrabException extends RuntimeException {

    private int code;

    public GrabException(ResultEnmu resultEnmu){
        super(resultEnmu.getMsg());

        this.code = resultEnmu.getCode();

    }

    public GrabException(int code, String msg){
        super(msg);
        this.code = code;
    }
}

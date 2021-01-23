package com.evan.wj.result;

import lombok.Data;

@Data
public class Result {

    private int code;
    private String message;
    private Object result;

    public Result(int code){
        this.code = code;
    }

    public Result(int resultCode, String message, Object data) {
        this.code = resultCode;
        this.message = message;
        this.result = data;
    }

    public void buildFailResult(int code){this.code = code;}
}

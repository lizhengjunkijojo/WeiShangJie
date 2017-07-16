package com.lzj.vtm.demo.fun.juhe.wnl.model;

/**
 * RxJava模型类
 */
public class WnlBase {

    public String reason;
    public Result result;
    public int error_code;

    public class Result{
        public Wnl data;
    }
}

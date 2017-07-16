package com.lzj.vtm.demo.fun.juhe.news.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * RxJava模型类
 */
public class RetBase implements Serializable {

    public String reason;
    public Result result;
    public int error_code;

    public class Result implements Serializable {
        public String stat;
        public ArrayList<News> data;
    }
}

package com.lzj.vtm.demo.banner;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * RxJava模型类
 */
public class RetBaseBanner implements Serializable {

    public int code; //调用返回代码 ,
    public String language; //语言,暂时没有用到 ,
    public String msg; //服务器端返回消息 ,
    public int status ;//暂时没有用到 ,
    public boolean success; //调用是否成功

    public Result result; //返回数据对象 Object 类型 ,

    public int currentPage;//当前页
    public int totalPage; //总页面,

    public class Result implements Serializable {
        public ArrayList<Banner> banner;
    }
}

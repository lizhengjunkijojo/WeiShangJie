package com.lzj.vtm.demo.fun.juhe.pcd.model;

import java.util.ArrayList;

/**
 * RxJava模型类
 */
public class CodeBase {

    public String reason;
    public Result result;
    public int error_code;

    public class Result{
        public int totalcount;
        public int currentpage;
        public int totalpage;
        public int pagesize;

        public ArrayList<Code> list;
    }
}

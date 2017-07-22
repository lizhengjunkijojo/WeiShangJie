package com.lzj.vtm.demo.home.episode.model;

import java.io.Serializable;

/**
 * Created by lizhengjun on 17/7/18.
 */
public class Episode  implements Serializable {

    public final static int NEWSTYPE_NEWS_FREE = 0; //信息类型  0   web直接加载
    public final static int NEWSTYPE_NEWS_HTML_1 = 1; //信息类型  1    Html页面加载---外部广告页面
    public final static int NEWSTYPE_NEWS_HTML_2 = 2; //信息类型  2   Html页面加载---内部链接页面

    public String title;
    public int type;
    public long addtime;
    public String pic;
    public String content;
    public String html;

    public int rowid;
    public String shortdescription;
    public String id;
    public int ordernum;
    public String released;
    public String srcfrom;
    public int status;

}
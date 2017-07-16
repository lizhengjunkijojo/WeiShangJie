package com.lzj.vtm.demo.banner;

import java.io.Serializable;

/**
 * 轮播图实体对象
 */
public class Banner implements Serializable{

    public final static int NEWSTYPE_BANNER_FREE = 0; //信息类型  0   web直接加载
    public final static int NEWSTYPE_BANNER_HTML_1 = 1; //信息类型  1   Html页面加载---外部广告页面
    public final static int NEWSTYPE_BANNER_HTML_2 = 2; //信息类型  2   Html页面加载---内部链接页面

    public String title;
    public int type;
    public int rowid;
    public long addtime;
    public String shortdescription;
    public String html;
    public String id;
    public String bannerContent;
    public int ordernum;
    public String pic;
    public int status;

}

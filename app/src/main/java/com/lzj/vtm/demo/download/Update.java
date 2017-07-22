package com.lzj.vtm.demo.download;

import com.lzj.vtm.demo.home.news.model.News;

import java.io.Serializable;

/**
 * 更新实体类
 */
public class Update implements Serializable {

	public int code; //调用返回代码
	public String msg; //服务器端返回消息
	public boolean success; //调用是否成功
	public String language; //语言,暂时没有用到
	public int status ;//暂时没有用到
	public int currentPage;//当前页
	public int totalPage; //总页面

	public Result result; //返回数据对象 Object 类型

	public class Result implements Serializable {
		public AndroidBean latestAppInfo;
	}

	public class AndroidBean implements Serializable {
		public int versionCode;  // 新版本的code
		public String versionName; //   新版本的版本名
		public String downloadUrl;  //  新版本的下载地址
		public String updateLog;    //  新版本的更新信息描述
	}

}

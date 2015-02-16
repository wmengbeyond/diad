/**
 * @date 2015-02-12
 * @author wm
 * 合集信息类
 * 成员变量名字与rabbitmq接收的对应上，注释是OTT给的名字说明
 */

package com.hunantv.subplat.diad.utils;

import com.alibaba.fastjson.*;

public class ClipInfo {
	
	//合集id OTT: Sndlvl_Id
	private int assetId;
	
	//合集名   OTT: Sndlvl_Desc
	private String clipName;
	
	//一级分类名   OTT: Fstlvl_Desc
	private String fstName;

	//合集关键字   OTT: Sndlvl_Tag
	private String keyword;
	
	//合集标签   OTT: Sndlvl_Ex_Tag
	private String tags;
	
	public int getAssetId() {
		return assetId;
	}

	public void setAssetId(int id) {
		this.assetId = id;
	}
	
	public String getClipName() {
		return clipName;
	}

	public void setClipName(String name) {
		clipName = name;
	}
	
	public String getFstName() {
		return fstName;
	}

	public void setFstName(String name) {
		fstName = name;
	}
	
	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String name) {
		keyword = name;
	}
	
	public String getTags() {
		return tags;
	}

	public void setTags(String name) {
		tags = name;
	}
	
	/**
	 * 解析json字符串
	 */
	public String parse(){
		return JSON.toJSONString(this);
	}
}

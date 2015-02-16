/**
 * @date 2015-02-12
 * @author wm
 * 分集信息类
 * 成员变量名字与rabbitmq接收的对应上，注释是OTT给的名字说明
 */

package com.hunantv.subplat.diad.utils;

import com.alibaba.fastjson.JSON;

//成员变量名字与rabbitmq接收的对应上，注释是OTT给的名字说明
public class VideoInfo {
	
	//视频ID OTT: Clip_Id
	private String partId;
	
	//视频名   OTT: Primary_Name
	private String partName;
	
	//发布日期   OTT: Release_Date
	private String releaseTime;
	
	//视频时长   OTT: File_Duration
	private String duration;
	
	//视频广告时间点
	//private String adtime1;
	
	//合集id OTT: Sndlvl_Id
	private String assetId;
	
//	//合集名   OTT: Sndlvl_Desc
//	private String clipName;
//	
//	//一级分类名   OTT: Fstlvl_Desc
//	private String fstName;
//
//	//合集关键字   OTT: Sndlvl_Tag
//	private String keyword;
//	
//	//合集标签   OTT: Sndlvl_Ex_Tag
//	private String tags;

	public String getPartId() {
		return partId;
	}

	public void setPartId(String id) {
		this.partId = id;
	}
	
	public String getPartName() {
		return partName;
	}

	public void setPartName(String name) {
		this.partName = name;
	}

	public String getReleaseTime() {
		return releaseTime;
	}

	public void setReleaseTime(String times) {
		releaseTime = times;
	}
	
	public String getDuration() {
		return duration;
	}

	public void setDuration(String dur) {
		duration = dur;
	}
	
	public String getAssetId() {
		return assetId;
	}

	public void setAssetId(String id) {
		this.assetId = id;
	}
	
//	public String getClipName() {
//		return clipName;
//	}
//
//	public void setClipName(String name) {
//		clipName = name;
//	}
//	
//	public String getFstName() {
//		return fstName;
//	}
//
//	public void setFstName(String name) {
//		fstName = name;
//	}
//	
//	public String getKeyword() {
//		return keyword;
//	}
//
//	public void setKeyword(String name) {
//		keyword = name;
//	}
//	
//	public String getTags() {
//		return tags;
//	}
//
//	public void setTags(String name) {
//		tags = name;
//	}
	
	public String parse(){
		return JSON.toJSONString(this);
	}
}

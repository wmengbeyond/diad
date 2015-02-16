/**
 * @date 2015-02-12
 * @author wm
 * VIP信息类
 * 成员变量名字与rabbitmq接收的对应上，注释是OTT给的名字说明
 */

package com.hunantv.subplat.diad.utils;

import com.alibaba.fastjson.JSON;

public class VipInfo {
	
	private String uid;
	
	private String name;
	
	public VipInfo(String vipUid, String vipName) {
		super();
		
		uid = vipUid;
		name = vipName;
	}
	
	public String getUid() {
		return uid;
	}

	public void setUid(String id) {
		this.uid = id;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String parse(){
		return JSON.toJSONString(this);
	}
}

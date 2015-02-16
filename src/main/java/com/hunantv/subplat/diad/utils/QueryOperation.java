/**
 * @date 2015-02-12
 * @author wm
 * 操作redis，响应客户端请求，返回数据hdf格式
 */

package com.hunantv.subplat.diad.utils;

import java.util.Map;

import com.google.clearsilver.jsilver.JSilver;
import com.google.clearsilver.jsilver.data.Data;
import com.google.clearsilver.jsilver.resourceloader.ClassResourceLoader;

import redis.clients.jedis.Jedis;


public class QueryOperation {
	private static QueryOperation instance = null;
	
	public static final String VIDEO_PREFIX = "VV_V_";
	
	public static final String CLIP_PREFIX = "VV_C_";
	
	public static synchronized QueryOperation getInstance() {
		if (instance == null) {
			instance = new QueryOperation();
		}
		return instance;
	}
	
	public String getVideoInfo(String id) {
		
		Jedis jedis = JedisUtil.getJedis();
		
		// Load resources (e.g. templates) from classpath, along side this class.
		JSilver jSilver = new JSilver(new ClassResourceLoader(QueryOperation.class));
		
		// Set up some data.
		Data data = jSilver.createData();
		
		String findKey = VIDEO_PREFIX + id;
		Map<String, String> fields = jedis.hgetAll(findKey);
		for(Map.Entry<String, String> entry: fields.entrySet()) { 
			data.setValue("video." + entry.getKey(),  entry.getValue());
			//data.setAttribute("video." + entry.getKey(), "{@code int}");
		}
		
		String assetid = data.getValue("video.assetid");
		findKey = CLIP_PREFIX + assetid;
		Map<String, String> fieldsClip = jedis.hgetAll(findKey);
		for(Map.Entry<String, String> entry: fieldsClip.entrySet()) { 
			data.setValue("video.Clip." + entry.getKey(),  entry.getValue());
		}
		
		JedisUtil.closeJedis(jedis);
		
		return data.toString();
	}
}

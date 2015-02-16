/**
 * @date 2015-02-12
 * @author wm
 * 程序启动时加载合集和分集信息到内存，备选方案
 * 直接通过id返回hdf，备选方案返回json
 */

package com.hunantv.subplat.diad.utils;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import redis.clients.jedis.Jedis;


public class DataPool {
	private static final Logger log = Logger.getLogger(DataPool.class);
	
	private static DataPool instance = null;
	
	public static final String VIDEO_PREFIX = "VV_V_";
	
	public static final String CLIP_PREFIX = "VV_C_";
	
	private Hashtable<String, VideoInfo> video = new Hashtable<String, VideoInfo> ();
	
	private Hashtable<String, ClipInfo> clip = new Hashtable<String, ClipInfo> ();
	
	private Hashtable<String, VipInfo> vip = new Hashtable<String, VipInfo> ();
	
	public void insertVideoTable(String key, VideoInfo value) {
		if(value == null || value.equals(""))
			return;
		
		video.put(key, value);
	}
	
	public VideoInfo getVideoTable(String key) {
		return video.get(key);
	}
	
	public VideoInfo removeVideoTable(String key) {
		return video.remove(key);
	}
	
	public void insertClipTable(String key, ClipInfo value) {
		if(value == null || value.equals(""))
			return;
		
		clip.put(key, value);
	}
	
	public ClipInfo getClipTable(String key) {
		if(key == null || key.equals(""))
			return null;
		
		return clip.get(key);
	}
	
	public ClipInfo removeClipTable(String key) {
		return clip.remove(key);
	}
	
	public void insertVipTable(String key, VipInfo value) {
		if(key == null || key.equals(""))
			return;
		
		if(value == null || value.equals(""))
			return;
		
		vip.put(key, value);
	}
	
	public VipInfo getVipTable(String key) {
		return vip.get(key);
	}
	
	public VipInfo removeVipTable(String key) {
		return vip.remove(key);
	}
	
	public void LoadData() {
		log.info("load redis data success!");
		
		try {
			Jedis jedis = JedisUtil.getJedis();

			Set<String> keyVideos = jedis.keys(VIDEO_PREFIX + "*");
			
			String key;
			
			if (!keyVideos.isEmpty()) {
				Iterator<String> iter = keyVideos.iterator();
				while (iter.hasNext()) {
					key = iter.next();
//					Map<String, String> fields = jedis.hgetAll(key); 
					
//					for(Map.Entry entry: map.entrySet()) { 
//					   System.out.print(entry.getKey() + ":" + entry.getValue() + "\t");
//					} 
				
					//value = JSONObject.fromObject(fields).toString();
					
					// Set up some data.
//					Data data = jSilver.createData();
//					for(Map.Entry<String, String> entry: fields.entrySet()) { 
//						data.setValue(VIDEO_PREFIX + "." + entry.getKey(),  entry.getValue());
//					}
//					value = data.toString();
					
					VideoInfo value = new VideoInfo();
					value.setPartId(jedis.hget(key, "partid"));
					value.setPartName(jedis.hget(key, "partname"));
					value.setDuration(jedis.hget(key, "duration"));
					value.setReleaseTime(jedis.hget(key, "releaseTime"));
					insertVideoTable(jedis.hget(key, "partid"), value);
				}
			}
			
			Set<String> keyClip = jedis.keys(CLIP_PREFIX + "*");
			
			if (!keyClip.isEmpty()) { 
				Iterator<String> iter = keyClip.iterator();
				while (iter.hasNext()) {
					key = iter.next();
					Map<String, String> fields = jedis.hgetAll(key); 
					//value = JSONObject.fromObject(fields).toString();
					
//					Data data = jSilver.createData();
//					for(Map.Entry<String, String> entry: fields.entrySet()) { 
//						data.setValue(entry.getKey(),  entry.getValue());
//					}
//					
//					value = data.toString();
					
					ClipInfo value = new ClipInfo();
					insertClipTable(fields.get("assetid"), value);
				}
			}
			
			//Set<String> keyVip = jedis.hkeys("vips");
			
			JedisUtil.closeJedis(jedis);
			
		} catch (Exception e) {
			log.error("load redis failed!");
		} 
		
		log.info("load redis success!");
	}
	
	public static synchronized DataPool getInstance() {
		if (instance == null) {
			instance = new DataPool();
		}
		return instance;
	}
}

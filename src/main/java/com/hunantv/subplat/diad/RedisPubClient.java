/**
 * @date 2015-02-12
 * @author wm
 * redis pub操作
 */

package com.hunantv.subplat.diad;

import java.io.ObjectOutputStream;

import com.hunantv.subplat.diad.utils.JedisUtil;
import com.hunantv.subplat.diad.utils.ProjectConfig;

import redis.clients.jedis.*;

public class RedisPubClient {
	private static RedisPubClient instance = null;
	
	//private Jedis jedis;
	
	public static synchronized RedisPubClient getInstance() {
		if (instance == null) {
			instance = new RedisPubClient();
		}
		return instance;
	}
	
	public void initPublish()
	{
		//jedis = new Jedis(DiadConfig.getInstance().getRedisHost(), DiadConfig.getInstance().getRedisPort());
	}
	
	
	public void pub(ObjectOutputStream message){
		
//	    try {
//	        Bean bean = new Bean();
//	        bean.setName("test");
//	    //使用ObjectOutputStream和ByteArrayOutputStream将对象转换成字节流
//	        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//	        ObjectOutputStream oos = new ObjectOutputStream(baos);
//	        oos.writeObject(bean);
//	        String msg1 = baos.toString("ISO-8859-1");//指定字符集将字节流解码成字符串，否则在订阅时，转换会有问题。
//	        // msg1 = URLEncoder.encode(msg1, "UTF-8");
//	        jedis.publish("foo", msg1);
//	      } catch (Exception e) {
//
//	      }
	}
	
	public void pub(String message){ 
		Jedis jedis = JedisUtil.getJedis();
        jedis.publish(ProjectConfig.getInstance().getRedisChannel1(), message);
        JedisUtil.closeJedis(jedis);
    } 
	
	public void close(){
		Jedis jedis = JedisUtil.getJedis();
		jedis.publish(ProjectConfig.getInstance().getRedisChannel1(), "quit");  
		jedis.del(ProjectConfig.getInstance().getRedisChannel1()); 
		
		jedis.publish(ProjectConfig.getInstance().getRedisChannel2(), "quit");
		jedis.del(ProjectConfig.getInstance().getRedisChannel2());
		JedisUtil.closeJedis(jedis);
	}
	
//    public PubClient(String host,int port){  
//        jedis = new Jedis(host,port);  
//    }  
      
//    public void pub(String channel,String message){  
//        jedis.publish(channel, message);  
//    }  
      
//    public void close(String channel){  
//        jedis.publish(channel, "quit");  
//        jedis.del(channel);//  
//    }  
}


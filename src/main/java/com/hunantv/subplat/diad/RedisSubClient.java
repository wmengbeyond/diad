/**
 * @date 2015-02-12
 * @author wm
 * redis sub操作
 */

package com.hunantv.subplat.diad;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

import com.hunantv.subplat.diad.utils.ProjectConfig;

public class RedisSubClient {
	
	//private Jedis jedis;// 
	
	protected void finalize() {
		
	}
	
//	public void initRedisConnect() {
//		jedis = new Jedis(DiadConfig.getInstance().getRedisHost(), DiadConfig.getInstance().getRedisPort());
//	}
      
    public void sub(Jedis jedis, JedisPubSub listener){
        //jedis.subscribe(listener, new String[]{DiadConfig.getInstance().getRedisChannel1(), DiadConfig.getInstance().getRedisChannel2()});  
    	//此处将会阻塞，在client代码级别为JedisPubSub在处理消息时，将会“独占”链接  
        //并且采取了while循环的方式，侦听订阅的消息  
        //
    	
    	jedis.subscribe(listener, ProjectConfig.getInstance().getRedisChannel1()); 
    }
}

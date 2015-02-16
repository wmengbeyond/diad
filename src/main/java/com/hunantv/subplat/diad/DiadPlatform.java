/**
 * @date 2015-02-12
 * @author wm
 * 程序入口
 * 
 */

package com.hunantv.subplat.diad;

import java.io.File;

//import redis.clients.jedis.Jedis;
//import redis.clients.jedis.JedisPubSub;





import org.apache.log4j.Logger;

import com.hunantv.subplat.diad.net.NettyServer;
import com.hunantv.subplat.diad.utils.ProjectConfig;

public class DiadPlatform {
	private static final String[] QUEUE_NAMES= {"MgtvDispatchQueue_itv_yys", "hello"};
	
	public static void main(String[] args) throws Exception {
		String path = "properties/log4j.properties";//配置自己配置文件的路径;
		File file = new File(path);
		if(!file.exists())
		{
			System.out.println("log4j.properties no exist! please check cur directory properties folder");
			return;
		}
		
		org.apache.log4j.PropertyConfigurator.configure(path);
		Logger log = Logger.getLogger(DiadPlatform.class);
		log.info("start load properties ...");
		
		if (!ProjectConfig.getInstance().readXml()) {
			System.out.println("load properties failed! exit...");
			return;
		}
		
		//加载redis数据到内存 备用方案	
//		DataPool.getInstance().LoadData();
		
		//String hdf = QueryOperation.getInstance().getVideoInfo("664111");
		//选择线程启动模式  如果该程序做为rabbit客户端接受者，则启用redis的pub方式。否则直接启动redis的sub方式启动 暂不使用pub/sub直接通过redis取数据
		if (!ProjectConfig.getInstance().getRabbitSwitch())
		{
			//暂时不启动该方式 备选方案
//			Thread subRedisThread = new Thread(new Runnable() {  
//	            @Override  
//	            public void run() {  
//	                try{  
//						Jedis jedis = JedisUtil.getJedis();
//	                    RedisSubClient subClient = new RedisSubClient();  
//	                    JedisPubSub listener = new RedisSubListener();  
//	                    subClient.sub(jedis, listener); 
//						JedisUtil.closeJedis(jedis);
//	                }catch(Exception e){  
//	                    Logger.getLogger(DiadPlatform.class).error(e.getMessage());
//	                    return;
//	                }  
//	                  
//	            }  
//	        });  
//			
//			subRedisThread.start(); 
	        
		} else {
			Thread rabbitThread = new Thread(new Runnable() {  
	            @Override  
	            public void run() {  
	                try{  
	                	RabbitSubscriber rabbit = new RabbitSubscriber(QUEUE_NAMES[1]);
	                	rabbit.run();
	                }catch(Exception e){  
	                    Logger.getLogger(DiadPlatform.class).error(e.getMessage());
	                    return;
	                }  
	            }  
	        });  
			
			rabbitThread.start(); 

//			for(int i=0;i<QUEUE_NAMES.length;i++){
//	             
//	            RabbitSubscriber sub = new RabbitSubscriber(QUEUE_NAMES[i]);
//	            Thread t = new Thread(sub);
//	            t.start();    
//	        }
		}
		
		NettyServer server = new NettyServer();  
		server.start(ProjectConfig.getInstance().getServerPort());
	}

}

/**
 * @date 2015-02-12
 * @author wm
 * 程序启动时加载配置文件，系统需要的参数信息
 * 
 */

package com.hunantv.subplat.diad.utils;

import java.io.File;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.hunantv.subplat.diad.net.NettyInboundHandler;

public final class ProjectConfig {
	private static Logger log = Logger.getLogger(NettyInboundHandler.class); 
	
	public  enum RedisType {
		PUBLISH,
		SUBSCRIBE,
	}
	
	private static ProjectConfig instance = null;
	
	private static String serverHost;
	
	private static int serverPort;
	
	private static int serverHeartBeat;
	
	private static String rabbitHost;
	
	private static int rabbitPort;
	
	private static String rabbitUserName;
	
	private static String rabbitPassword;
	
	private static String rabbitQueueName;
	
	private static boolean rabbitSwitch;
	
	private static String redisHost;
	
	private static String redisChannel1;
	
	private static String redisChannel2;
	
	private static int redisPort;
	
	private static int redisType;
	
	private static int redisRetryNum;
	
	//可用连接实例的最大数目，默认值为8；
    //如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)
	private static int redisMaxActive;
	
	//控制一个pool最多有多少个状态为idle(空闲的)的jedis实例，默认值也是8
	private static int redisMaxIdle;
	
	//等待可用连接的最大时间，单位毫秒，默认值为-1，表示永不超时。如果超过等待时间，则直接抛出JedisConnectionException；
	private static int redisMaxWait;
	
	private static int redisTimeout;
	
	//在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
	private static boolean redisTestOnBorrow;
	
	private static String loggerPath;
	
	public String getServerHost() {
		return serverHost;
	}
	
	public int getServerPort() {
		return serverPort;
	}

	public int getServerHeartBeat() {
		return serverHeartBeat;
	}
	
	public String getRabbitHost() {
		return rabbitHost;
	}
	
	public int getRabbitPort() {
		return rabbitPort;
	}
	
	public String getRabbitUserName() {
		return rabbitUserName;
	}
	
	public String getRabbitPassword() {
		return rabbitPassword;
	}

	public String getRabbitQueueName() {
		return rabbitQueueName;
	}
	
	public boolean getRabbitSwitch() {
		return rabbitSwitch;
	}
	
	public String getRedisHost() {
		return redisHost;
	}
	
	public String getRedisChannel1() {
		return redisChannel1;
	}
	
	public String getRedisChannel2() {
		return redisChannel2;
	}
	
	public int getRedisPort() {
		return redisPort;
	}
	
	public int getRedisType() {
		return redisType;
	}
	
	public int getRedisRetryNum() {
		return redisRetryNum;
	}
	
	public int getRedisMaxActive() {
		return redisMaxActive;
	}
	
	public int getRedisMaxIdle() {
		return redisMaxIdle;
	}
	
	public int getRedisMaxWait() {
		return redisMaxWait;
	}
	
	public int getRedisTimeout() {
		return redisTimeout;
	}
	
	public boolean getRedisTestOnBorrow() {
		return redisTestOnBorrow;
	}
	
	public String getLoggerPath() {
		return loggerPath;
	}
	
	public boolean readXml(){
		try {
			SAXReader reader = new SAXReader();
			
			File file = new File("properties/server.xml");
			if(!file.exists())
			{
				System.out.println("server no exist");
				return false;
			}
			
			Document  document = reader.read(file);
			Element root = document.getRootElement();
			
			serverHost = root.element("Server").element("Host").getText().trim();
			serverPort = Integer.parseInt(root.element("Server").element("Port").getText().trim());
			serverHeartBeat = Integer.parseInt(root.element("Server").element("Heartbeat").getText().trim());
			
			rabbitHost = root.element("RabbitMQ").element("Host").getText().trim();
			rabbitPort = Integer.parseInt(root.element("RabbitMQ").element("Port").getText().trim());
			rabbitUserName = root.element("RabbitMQ").element("UserName").getText().trim();
			rabbitPassword = root.element("RabbitMQ").element("Password").getText().trim();
			rabbitQueueName = root.element("RabbitMQ").element("QueueName").getText().trim();
			
			rabbitSwitch = false;
			String RabbitSwitch = root.element("RabbitMQ").element("Switch").getText().trim();
			if (RabbitSwitch.equals("ON"))
			{
				rabbitSwitch = true;
			}
			
			redisHost = root.element("Redis").element("Host").getText().trim();
			redisPort = Integer.parseInt(root.element("Redis").element("Port").getText().trim());
			redisChannel1 = root.element("Redis").element("Queue").element("Channel1").getText().trim();
			redisChannel2 = root.element("Redis").element("Queue").element("Channel2").getText().trim();
			redisType = Integer.parseInt(root.element("Redis").element("Type").getText().trim());
			redisMaxActive = Integer.parseInt(root.element("Redis").element("MaxActive").getText().trim());
			redisMaxIdle = Integer.parseInt(root.element("Redis").element("MaxIdle").getText().trim());
			redisMaxWait = Integer.parseInt(root.element("Redis").element("MaxWait").getText().trim());
			redisTimeout = Integer.parseInt(root.element("Redis").element("Timeout").getText().trim());
			redisTestOnBorrow = Boolean.parseBoolean(root.element("Redis").element("TestOnBorrow").getText().trim());
			
		}catch (Exception e) {
			log.error(e.getMessage());
			return false;
		}

		return true;
	}

	private ProjectConfig() {
	}

	public static synchronized ProjectConfig getInstance() {
		if (instance == null) {
			instance = new ProjectConfig();
		}
		return instance;
	}
}

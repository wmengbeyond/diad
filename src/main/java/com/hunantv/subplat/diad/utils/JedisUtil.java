/**
* @date 2015-02-12
 * @author wm
 * redis pool处理请求，及时归还到pool
 * 
 */

package com.hunantv.subplat.diad.utils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisUtil {
    private static JedisPool pool;
    //private static FwLogger log = new FwLogger(JedisUtil.class);
    private JedisUtil() {
    }

    static {
		JedisPoolConfig config = new JedisPoolConfig();
	    //pool可以分配的Jedis实例
        config.setMaxTotal(ProjectConfig.getInstance().getRedisMaxActive());
        //控制一个pool最多有多少个状态为idle(空闲的)的jedis实例
        config.setMaxIdle(ProjectConfig.getInstance().getRedisMaxIdle());
        //表示当borrow(引入)一个jedis实例时，最大的等待时间，如果超过等待时间，则直接抛出JedisConnectionException
        config.setMaxWaitMillis(ProjectConfig.getInstance().getRedisMaxWait());
        config.setTestOnBorrow(true);
        config.setTestOnReturn(true);
        pool = new JedisPool(config, ProjectConfig.getInstance().getRedisHost(), ProjectConfig.getInstance().getRedisPort());
        //log.info("JedisUtil static init ...");
    }
    
    public static JedisPool getPool(){
    	return pool;
    }
    public static Jedis getJedis() {
    	return pool.getResource();
    }

    public static void closeJedis(Jedis jedis) {
        pool.returnResource(jedis);
    }
}

/**
 * @date 2015-02-12
 * @author wm
 * redis sub 监听操作
 */

package com.hunantv.subplat.diad;

import org.apache.log4j.Logger;

import redis.clients.jedis.*;

public class RedisSubListener extends JedisPubSub {
	private static final Logger log = Logger.getLogger(RedisSubListener.class);

	// 取得订阅的消息后的处理
	@Override
	public void onMessage(String channel, String message) {
		 log.info("Message received. Channel: " + channel + ", Msg: " + message);

		// 此处我们可以取消订阅
		if (message.equalsIgnoreCase("quit")) {
			this.unsubscribe(channel);
			return;
		}

//		JSONObject obj = JSONObject.fromObject(message);
//
//		if (channel.equals(DiadConfig.getInstance().getRedisChannel1())) {
//			if (message.indexOf("partId") > 0) {
//				String key = obj.getString("partId");
//				VideoInfo value = (VideoInfo)JSONObject.toBean(obj,VideoInfo.class);
//				DataPool.getInstance().insertVideoTable(key, value);
//			}
//		} else if (channel.equals(DiadConfig.getInstance().getRedisChannel2())) {
//			if (message.indexOf("assetId") > 0) {
//				String key = obj.getString("assetId");
//				ClipInfo value = (ClipInfo)JSONObject.toBean(obj,ClipInfo.class);
//				DataPool.getInstance().insertClipTable(key, value);
//			}
//		} //else if (channel.equals(DiadConfig.getInstance().getRedisChannel2())) {
//			if (message.indexOf("uid") > 0) {
//				String key = obj.getString("uid");
//				VipInfo value = (VipInfo)JSONObject.toBean(obj,VipInfo.class);
//				DataPool.getInstance().insertVipTable(key, value);
//			}
//		}
	}

	// 取得按表达式的方式订阅的消息后的处理
	@Override
	public void onPMessage(String pattern, String channel, String message) {

	}

	// 初始化订阅时候的处理
	@Override
	public void onSubscribe(String channel, int subscribedChannels) {

	}

	// 取消订阅时候的处理
	@Override
	public void onUnsubscribe(String channel, int subscribedChannels) {

	}

	// 取消按表达式的方式订阅时候的处理
	@Override
	public void onPUnsubscribe(String pattern, int subscribedChannels) {

	}

	// 初始化按表达式的方式订阅时候的处理
	@Override
	public void onPSubscribe(String pattern, int subscribedChannels) {

	}
}

/**
 * @date 2015-02-12
 * @author wm
 * RabbitMQ 客户端阻塞接收消息类
 */

package com.hunantv.subplat.diad;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import redis.clients.jedis.Jedis;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import com.hunantv.subplat.diad.utils.*;

public class RabbitSubscriber implements Runnable {	
	private static final Logger log = Logger.getLogger(RabbitSubscriber.class);
	
	private static final int TYPE_DEMAND = 1;
	
	private static final int TYPE_LIVE = 2;
	
	private static final int DESC_CLIP = 1;
	
	private static final int DESC_CLIPPART = 2;
	
	private static final int OP_ALTER = 1;
	
	private static final int OP_DEL = 2;
	
	public static final String VIDEO_PREFIX = "VV_V_";
	
	public static final String CLIP_PREFIX = "VV_C_";
	
	private Connection connection = null;
	
	private Channel channel = null;
	
	private String queue_name = null;
	
	public RabbitSubscriber(String queue_name) {

		this.queue_name = queue_name;
	}

	@Override
	public void run() {
		try {

			//RedisPubClient.getInstance().initPublish();
				
			ConnectionFactory factory = new ConnectionFactory();
			factory.setHost(ProjectConfig.getInstance().getRabbitHost());
			factory.setPort(ProjectConfig.getInstance().getRabbitPort());
			factory.setUsername(ProjectConfig.getInstance().getRabbitUserName());
			factory.setPassword(ProjectConfig.getInstance().getRabbitPassword());
			factory.setAutomaticRecoveryEnabled(true);
			factory.setNetworkRecoveryInterval(10000);
			connection = factory.newConnection();
			channel = connection.createChannel();
			channel.queueDeclare(queue_name, true, false, false, null);
			// channel.basicQos(1);//告诉RabbitMQ同一时间给一个消息给消费者

			QueueingConsumer consumer = new QueueingConsumer(channel);
			channel.basicConsume(queue_name, true, consumer);
			
			while (true) {
				QueueingConsumer.Delivery delivery = consumer.nextDelivery();
				String message = new String(delivery.getBody());
				
				log.debug(" [x] RabbitMQ Received '" + message + "'");
				
				Document document = DocumentHelper.parseText(message);
				Element root = document.getRootElement();
				  
				// 1点播；2直播；3明星库
				byte type = Byte.parseByte(root.element("assettype").getText());
				// 1 代表是集合(clip); 2是媒资分集(clippart); 3文件内容注入(file)
				byte desc = Byte.parseByte(root.element("assetdesc").getText());
				// 1增加修改; 2 删除;3 取消发布
				byte operation = Byte.parseByte(root.element("assetoperation").getText());
				Element e = root.element("content");
				
				if (type == TYPE_DEMAND || type == TYPE_LIVE) {

					Jedis jedis = JedisUtil.getJedis();

					// 暂时直接从redis中获取数据
					// JSONObject jsonObj = new JSONObject();
					// jsonObj.put("assettype", type);
					// jsonObj.put("assetoperation", operation);
					// jsonObj.put("assetdesc", desc);

					if (desc == DESC_CLIP) {
						if (operation == OP_ALTER) {
							String assetid = e.element("assetid").getText();// 集合媒资id
							String fstname = e.element("fstname").getText();// 一级分类名称(如：电影)
							String fstlvlid = e.element("fstlvlid").getText();// 一级分类ID(如：1)
							String clipname = e.element("clipname").getText();// 媒资集合名称
							String keyword = e.element("keyword").getText();// 搜索关键字
							String kind = e.element("kind").getText();// 类型
																		// (格式：综艺|MV)
							String area = e.element("area").getText();// 地区
							String tags = e.element("tags").getText();// 标签
							String year = e.element("year").getText();// 年份 （整型）
							// String duration =
							// e.element("duration").getText();//时长
							// String relasetime =
							// e.element("relasetime").getText();// 上映时间

							Map<String, String> fields = new HashMap<String, String>();
							fields.put("assetid", assetid.trim());
							fields.put("fstname", fstname.trim());
							fields.put("fstlvlid", fstlvlid.trim());
							fields.put("clipname", clipname.trim());
							fields.put("keyword", keyword.trim());
							fields.put("kind", kind.trim());
							fields.put("area", area.trim());
							fields.put("tags", tags.trim());
							fields.put("year", year.trim());
							jedis.hmset(CLIP_PREFIX + assetid.trim(), fields);

							// String Clip =
							// JSONObject.fromObject(fields).toString();
							// DataPool.getInstance().insertClipTable(assetid,
							// Clip);
							// jsonObj.put("value", Clip);

						} else if (operation == OP_DEL) {
							String assetid = e.element("id").getText();// 集合媒资id
							jedis.del(CLIP_PREFIX + assetid.trim());
							// DataPool.getInstance().removeClipTable(assetid);

							// jsonObj.put("value", assetid);
						}

					} else if (desc == DESC_CLIPPART) {
						if (operation == OP_ALTER) {
							String assetid = e.element("assetid").getText();// 集合媒资id
							String partid = e.element("partid").getText();// 分集id
							String partname = e.element("partname").getText();// 分集名称
//							String keyword = e.element("keyword").getText();// 搜索关键字
//							String kind = e.element("kind").getText();// 类型(格式：综艺|MV)
//							String area = e.element("area").getText();// 地区
//							String extag = e.element("extag").getText();//分集扩展标签 对应二级分类和二级分类ID
//							String tags = e.element("tags").getText(); // 标签
//							String year = e.element("year").getText();// 年份 （整型）
							String isintact = e.element("isintact").getText(); // 1 代表整片；2 短片；3预告片
							String duration = e.element("duration").getText();// 时长
							String relasetime = e.element("relasetime").getText();// 上映时间

							// <seekpoints>
							// <item begin=’起始时间，单位秒’ end=’结束时间,单位秒’ type=”0代表片中
							// 1代表片头 2代表片尾” name=”打点名称” img=”图片地址” imghash=”
							// 7502D9FED3520013F7C3D6124567045F”></item>
							// </seekpoints>

							Map<String, String> fields = new HashMap<String, String>();
							fields.put("assetid", assetid.trim());
							fields.put("partid", partid.trim());
							fields.put("partname", partname.trim());
//							fields.put("keyword", keyword.trim());
//							fields.put("kind", kind.trim());
//							fields.put("area", area.trim());
//							fields.put("tags", tags.trim());
//							fields.put("year", year.trim());
							fields.put("isintact", isintact.trim());
							fields.put("duration", duration.trim());
							fields.put("relasetime", relasetime.trim());
							jedis.hmset(VIDEO_PREFIX + partid.trim(), fields);

							// String Video =
							// JSONObject.fromObject(fields).toString();
							// DataPool.getInstance().insertVideoTable(partid,
							// Video);
							// jsonObj.put("value", Video);

						} else if (operation == OP_DEL) {
							String partid = e.element("id").getText();// 分集id
							jedis.del(VIDEO_PREFIX + partid.trim());
							// DataPool.getInstance().removeVideoTable(partid);
							// jsonObj.put("value", partid);
						}
					}

					// jedis.publish(DiadConfig.getInstance().getRedisChannel1(),
					// jsonObj.toString());

					JedisUtil.closeJedis(jedis);
				}

				// channel.basicAck(delivery.getEnvelope().getDeliveryTag(),
				// false);//下一个消息

				Thread.sleep(1000);
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			System.exit(0);
		}

//		if (channel != null && channel.isOpen()) {
//			try {
//				channel.close();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		
//		if (connection != null && connection.isOpen()) {
//			try {
//				connection.close();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		
//		System.exit(0);
	}
}
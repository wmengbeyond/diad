/**
 * @date 2015-02-12
 * @author wm
 * UDP报文编码类
 * 
 */

package com.hunantv.subplat.diad.net;

import java.net.InetSocketAddress;
import java.util.List;

import org.apache.log4j.Logger;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.util.AttributeKey;

public class UdpEncode extends MessageToMessageEncoder<ByteBuf> {
	private static Logger log = Logger.getLogger(UdpEncode.class); 
	 
    public static final AttributeKey<InetSocketAddress> TARGET_ADDRESS = AttributeKey.valueOf("TARGET_ADDRESS");
 
    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        InetSocketAddress ip = ctx.channel().attr(TARGET_ADDRESS).get();
        if (ip == null) {
            log.error("no server ip");
            return;
        }
 
        DatagramPacket packet = new DatagramPacket(msg, ip);
        
        //目标地址为了能在整个连接上下文中共享，需要保存在channel中，而不是context中
        //发送的bytebuf仍然需要调用retain来防止提前释放抛出异常
        msg.retain();
        out.add(packet);
    }
}

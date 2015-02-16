/**
 * @date 2015-02-12
 * @author wm
 * netty UDP服务器
 */

package com.hunantv.subplat.diad.net;

import org.apache.log4j.Logger;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;  
import io.netty.channel.ChannelInitializer;  
import io.netty.channel.ChannelOption;  
import io.netty.channel.EventLoopGroup;  
import io.netty.channel.nio.NioEventLoopGroup;  
import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.handler.timeout.IdleStateHandler;

import com.hunantv.subplat.diad.utils.*;

public class NettyServer {
	private static final Logger log = Logger.getLogger(NettyServer.class);
	
	public void start(int port) throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        
        try {  
        	Bootstrap b = new Bootstrap();
            b.group(bossGroup).channel(NioDatagramChannel.class)  
            		.handler(new ChannelInitializer<DatagramChannel>() {  
                                @Override  
                                public void initChannel(DatagramChannel ch) throws Exception {
                                    ch.pipeline().addLast(new NettyOutboundHandler());    
                                    ch.pipeline().addLast("heartbeat", new IdleStateHandler(ProjectConfig.getInstance().getServerHeartBeat(), 0, 0));
                                    ch.pipeline().addLast("decoder", new UDPDecoder());
                                	ch.pipeline().addLast(new NettyInboundHandler());  
                                }  
                            }).option(ChannelOption.SO_REUSEADDR, false);

            ChannelFuture f = b.bind(port).sync();   

            f.channel().closeFuture().sync();  
        } catch (InterruptedException e) {
            log.error(e.toString());
        }finally {  
            bossGroup.shutdownGracefully();  
        }
	}
}

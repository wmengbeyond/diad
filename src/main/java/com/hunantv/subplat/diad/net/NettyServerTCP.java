/**
 * @date 2015-02-12
 * @author wm
 * netty netty TCP服务器
 */

package com.hunantv.subplat.diad.net;

import org.apache.log4j.Logger;

import io.netty.bootstrap.ServerBootstrap;  
import io.netty.channel.ChannelFuture;  
import io.netty.channel.ChannelInitializer;  
import io.netty.channel.ChannelOption;  
import io.netty.channel.EventLoopGroup;  
import io.netty.channel.nio.NioEventLoopGroup;  
import io.netty.channel.socket.SocketChannel;  
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

import com.hunantv.subplat.diad.utils.ProjectConfig;

public class NettyServerTCP {
	private static Logger log = Logger.getLogger(NettyServerTCP.class);
	
	public void start(int port) throws Exception {
		// 引导辅助程序
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        // 通过nio方式来接收连接和处理连接
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        
        try {  
            ServerBootstrap b = new ServerBootstrap();   
            b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {     
                                @Override  
                                public void initChannel(SocketChannel ch) throws Exception {  
                                	// 注册两个OutboundHandler，执行顺序为注册顺序的逆序，所以应该是OutboundHandler2 OutboundHandler1 
                                    ch.pipeline().addLast(new NettyOutboundHandler());  
//                                    ch.pipeline().addLast(new OutboundHandler2());  
                                    // 注册两个InboundHandler，执行顺序为注册顺序，所以应该是InboundHandler1 InboundHandler2 
                                	
                                    //LengthFieldBasedFrameDecoder构造函数，第一个参数为信息最大长度，超过这个长度回报异常，第二参数为长度属性的起始（偏移）位，我们的协议中长度是0到第3个字节，所以这里写0，第三个参数为“长度属性”的长度，我们是4个字节，所以写4，第四个参数为长度调节值，在总长被定义为包含包头长度时，修正信息长度，第五个参数为跳过的字节数，根据需要我们跳过前4个字节，以便接收端直接接受到不含“长度属性”的内容。
//                                	ch.pipeline().addLast("frameDecoder", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
//                                	//ch.pipeline().addLast("frameEncoder", new LengthFieldPrepender(4, false)); 
//                                	ch.pipeline().addLast("frameEncoder", new LengthFieldPrepender(4));                              	
                                    
                                    ch.pipeline().addLast("heartbeat", new IdleStateHandler(ProjectConfig.getInstance().getServerHeartBeat(), 0, 0));
                                    ch.pipeline().addLast("decoder", new MessageDecoder());
                                    //ch.pipeline().addLast("encoder", new MessageEncoder());
                                	ch.pipeline().addLast(new NettyInboundHandler());  
                                    
                                	//readerIdleTime为读超时时间（即服务器一定时间内未接受到客户端消息）
                                    //writerIdleTime为写超时时间（即服务器一定时间内向客户端发送消息）
                                    //allIdleTime为全体超时时间（即同时没有读写的时间）
//                                    ch.pipeline().addLast("heartbeat", new IdleStateHandler(30, 0, 0));
//                                    ch.pipeline().addLast("heartHandler", new HeartbeatHandler());
                                    
                                }  
                            }).option(ChannelOption.SO_BACKLOG, 128)   
                    .childOption(ChannelOption.SO_KEEPALIVE, true);   
  
            // Start the server. //在所有的网卡上监听这个端口
            ChannelFuture f = b.bind(port).sync();   
  
            // Wait until the server socket is closed.
            f.channel().closeFuture().sync();  
        } catch (InterruptedException e) {
            e.printStackTrace();
            log.error(e.toString());
        }finally { 
        	// Shut down all event loops to terminate all threads.
            workerGroup.shutdownGracefully();  
            bossGroup.shutdownGracefully();  
        }
	}
}

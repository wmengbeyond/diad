/**
 * @date 2015-02-12
 * @author wm
 * netty上收到的消息就会到这里来处理
 */

package com.hunantv.subplat.diad.net;

import org.apache.log4j.Logger;
import org.clearsilver.HDF;

import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;
import io.netty.channel.ChannelHandlerContext;  
import io.netty.channel.ChannelInboundHandlerAdapter; 
import io.netty.channel.socket.DatagramPacket;


public class NettyInboundHandler extends ChannelInboundHandlerAdapter {
	private static Logger logger = Logger.getLogger(NettyInboundHandler.class); 
	  
    @Override
    public void channelActive(final ChannelHandlerContext ctx) throws InterruptedException {
    	logger.info("DiadInboundHandler.channelActive: ctx :" + ctx);
    }
    
//    @Override
//    public void channelRead0(ChannelHandlerContext ctx, DatagramPacket packet) throws Exception {
//        System.err.println(packet);
//        if ("QOTM?".equals(packet.content().toString(CharsetUtil.UTF_8))) {
//            ctx.write(new DatagramPacket(
//                    Unpooled.copiedBuffer("QOTM: " + nextQuote(), CharsetUtil.UTF_8), packet.sender()));
//        }
//    }
    
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {

		logger.info("DiadInboundHandler.channelRead ctx :" + ctx + " req: "
				+ msg.toString());

		ctx.write(msg);
	} 
  
    @Override  
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {  
        logger.info("DiadInboundHandler.channelReadComplete");  
        ctx.flush();  
        
//        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER)//flush掉所有写回的数据
//        .addListener(ChannelFutureListener.CLOSE);//当flush完成后关闭channel
    } 
    
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt)
            throws Exception {

    	/*心跳处理*/
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {
            	/*读超时*/
                System.out.println("READER_IDLE 读超时");
                ctx.disconnect();
            } else if (event.state() == IdleState.WRITER_IDLE) {
            	/*写超时*/  
                System.out.println("WRITER_IDLE 写超时");
            } else if (event.state() == IdleState.ALL_IDLE) {
            	/*总超时*/
                System.out.println("ALL_IDLE 总超时");
            }
        }
}

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {

    	// 掉线 下线
        //ctx.close();
    	//System.err.printf("Factorial of %,d is: %,d%n", lastMultiplier, factorial);

    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();//捕捉异常信息
        ctx.close();//出现异常时关闭channel
        
//        try {
//            _logger.error(e.getCause(), "ERROR: Unhandled exception: " + e.getCause().getMessage()
//                    + ". Closing channel " + ctx.getChannel().getId());
//            e.getChannel().close();
//        } catch (Exception ex) {
//            _logger.debug(ex, "ERROR trying to close socket because we got an unhandled exception");
//        }
        
//        HttpResponse err = new DefaultHttpResponse(HttpVersion.HTTP_1_1,
//                HttpResponseStatus.INTERNAL_SERVER_ERROR);
//e.getChannel().write(err).addListener(ChannelFutureListener.CLOSE);
    }
}

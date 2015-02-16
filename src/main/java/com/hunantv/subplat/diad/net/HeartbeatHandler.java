/**
 * @date 2015-02-12
 * @author wm
 * tcp 心跳处理 暂未使用
 * 
 */

package com.hunantv.subplat.diad.net;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

import io.netty.channel.ChannelDuplexHandler;

public class HeartbeatHandler extends ChannelDuplexHandler {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String retMsg = (String) msg;
        if (!"Pong".equals(retMsg)) {
            ctx.close();
        }
        
        ctx.fireChannelRead(msg);
    }
 
 
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.isFirst()) { //如果是第一次出现Idle，则发送心跳包进行检测，否则直接关闭连接
                if (event.state() == IdleState.READER_IDLE) {
                    System.out.println("no reader event for 60 seconds");
                    ctx.writeAndFlush("Ping\n");
                    
//                    //log.error("channel:{} is time out.", ctx.channel());
//                    ctx.fireExceptionCaught(new SocketTimeoutException(
//                            "force to close channel("
//                                    + ctx.channel().remoteAddress()
//                                    + "), reason: time out."));
//                    ctx.channel().close();
                    
                } //else if (event.state() == IdleState.WRITER_IDLE) {
//                    System.out.println("no writer event for 60 seconds");
//                    //ctx.writeAndFlush("Ping\n");//出现异常客户端没有收到包 待查
//
//                }
            } else {
            	ctx.close();
            }
        }
        super.userEventTriggered(ctx, evt);
    }
 
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}

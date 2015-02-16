/**
 * @date 2015-02-12
 * @author wm
 * netty上发送消息都会经过这个函数
 */

package com.hunantv.subplat.diad.net;

import org.apache.log4j.Logger;

import io.netty.buffer.ByteBuf;  
import io.netty.channel.ChannelHandlerContext;  
import io.netty.channel.ChannelOutboundHandlerAdapter;  
import io.netty.channel.ChannelPromise; 

public class NettyOutboundHandler extends ChannelOutboundHandlerAdapter {
	private static Logger log = Logger.getLogger(NettyInboundHandler.class);  
    
	@Override  
    // 向client发送消息  
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {  
		log.info("DiadOutboundHandler.write");

		//QueryOperation.getInstance().getVideoInfo(id);
		MessageRequest req = (MessageRequest)msg;
		byte code = req.getCode();
		if (code == MessageHead.CODE_HEART) {
			System.out.println("client heart...");
			return;
		}
		
        MessageResponse resp = new MessageResponse();
        resp.setCode(code);
        String key = req.getMsg();
        
        String body = "wangmeng1111";
//        if (code == MessageHead.CODE_VIDOE) {
//			try {
//				body = DataPool.getInstance().getVideoTable(key).parse();
//			} catch (NumberFormatException e) {
//				log.info(e.getMessage());
//				return;
//			}
//        	
//        } else if (code == MessageHead.CODE_VIP) {
//        	body = DataPool.getInstance().getVipTable(key).parse();
//        }

        resp.setMsg(body);
        
        ByteBuf encoded = ctx.alloc().buffer(/*4 * */resp.getLen());  
        encoded.writeBytes(resp.ResponseByte());  
        ctx.write(encoded);  

        ctx.flush(); 
    }  
}

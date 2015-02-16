/**
 * @date 2015-02-12
 * @author wm
 * tcp报文编码类
 * 
 */

package com.hunantv.subplat.diad.net;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class MessageEncoder extends MessageToByteEncoder<MessageResponse> {
	
	@Override
    protected void encode(ChannelHandlerContext ctx, MessageResponse msg, ByteBuf out) {
		// Convert to a DiadResponseMsg first for easier implementation.
//		DiadResponseMsg v;
//		if (v instanceof DiadResponseMsg) {
//			v = (DiadResponseMsg) msg;
//		} else {
//				v = new DiadResponseMsg(String.valueOf(msg));
//		}
		
//        ByteBuf b = ctx.alloc().buffer();
//        byte[] encoded = new byte[20];
//        encoded = resp.ResponseByte();
//        //b.writeBytes(resp.ResponseByte());
//        b.writeBytes("aaa".getBytes()); 
        
		byte[] value = msg.ResponseByte();
		
//		out.writeByte((byte) 'F'); // magic number
//		out.writeInt(dataLength);  // data length
//		 out.writeBytes(data);      // data
		
		out.writeBytes(value);
    }
}

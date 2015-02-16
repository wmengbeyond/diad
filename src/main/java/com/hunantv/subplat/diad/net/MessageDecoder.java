/**
 * @date 2015-02-12
 * @author wm
 * tcp报文解析类
 * 
 */

package com.hunantv.subplat.diad.net;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import java.util.List;

public class MessageDecoder extends ByteToMessageDecoder {
	 @Override
     protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
//		 byte[] aa = new byte[20];
//		 in.readBytes(aa);
		 
         // Wait until the length prefix is available.
         if (in.readableBytes() < MessageHead.HEAD_LEN) {
             return;
         }
 
         //标记一下当前的readIndex的位置
         in.markReaderIndex();
 
         // Wait until the whole data is available.
         byte datatype = in.readByte();
         if (datatype != MessageHead.REQ) {
        	 ctx.close();
        	 return;
         }
         
         byte dataCode = in.readByte();
         if (dataCode != MessageHead.CODE_VIP && dataCode != MessageHead.CODE_VIDOE && dataCode != MessageHead.CODE_HEART) {
        	 ctx.close();
        	 return;
         }
         
         int dataLength = in.readInt();// 读取传送过来的消息的长度。ByteBuf 的readInt()方法会让他的readIndex增加4
         
         if (dataLength < 0) { //读到的消息体长度为0，这里出现这情况，关闭连接。
             ctx.close();
             return;
         }
         
         if (in.readableBytes() < dataLength - MessageHead.HEAD_LEN) {
        	 //读到的消息体长度如果小于我们传送过来的消息长度，则resetReaderIndex. 
        	 //这个配合markReaderIndex使用的。把readIndex重置到mark的地方
             in.resetReaderIndex();
             return;
         }
 
         byte[] body = new byte[dataLength - MessageHead.HEAD_LEN];
         in.readBytes(body);
         String msg = new String(body);
         
         MessageRequest req = new MessageRequest(datatype, dataCode, dataLength, msg);
         out.add(req);
         
     }
}

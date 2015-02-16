/**
 * @date 2015-02-12
 * @author wm
 * UDP报文解析类
 * 
 */

package com.hunantv.subplat.diad.net;

import java.util.List;

import org.apache.log4j.Logger;
import org.clearsilver.HDF;

import com.google.clearsilver.jsilver.adaptor.JSilverFactory;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageDecoder;

public class UDPDecoder extends  MessageToMessageDecoder<DatagramPacket> {
	private static Logger log = Logger.getLogger(UDPDecoder.class);
	enum Data{
	    DATA_TYPE_EOF,
	    DATA_TYPE_U32,
	    DATA_TYPE_ULONG,
	    DATA_TYPE_STRING,
	    DATA_TYPE_ARRAY,
	    DATA_TYPE_ANY
	};

    @Override
    protected void decode(ChannelHandlerContext ctx, DatagramPacket packet, List<Object> out) throws Exception {
        try {
            ByteBuf content = packet.content();

            //Version RequestID
            int value = content.readInt();
            int version = (value & 0XF0000000) >> 28;
            int requestID = (value & 0X0FFFFFFF); 
            if (version != 1 || requestID != 1) {
            	log.error("DatagrapPacket version error");
            	return;
            }
            
            short requestCommand = content.readShort();
            if (requestCommand != 1001) {
            	return;
            }
            
            short flags = content.readShort();
            int pluginLen = content.readInt();
            byte[] plugin = new byte[pluginLen];
            content.readBytes(plugin);
            String pluginName = new String(plugin,"UTF-8");
            
            if (!pluginName.equals("mqc")) {
            	return;
            }
            
            int varType = content.readInt();
            
            switch (varType) {
            case 1:
            	break;
            case 2:
            	break;
            case 3:
                int varLen = content.readInt();
                
//                HDF hdf = factory.newHdf();
//               try {
//            	   // Copy the Data into the HDF.
//               }
//               hdf.readString(data.toString());
                
                byte[] variable = new byte[varLen];
                content.readBytes(variable);
                String varName = new String(variable,"UTF-8");
                if (varName.equals("root")) {
                    value = content.readInt();
                    byte[] varEnd = new byte[value];
                    content.readBytes(varEnd);
                    String end = new String(varEnd,"UTF-8");
            		JSilverFactory factory = new JSilverFactory();
            		HDF hdf = factory.newHdf();
            		hdf.readString(end.trim());
            		
            		String str = hdf.getValue("p.m.p", null);

            		out.add(hdf);
                }
            	break;
            case 4:
            	break;
            }
            


    		
//            requestData.setEncode(content.readByte());
// 
//            ...
// 
//            requestData.setSender(packet.sender());
// 
//            out.add(requestData);
            
            //在out中添加了msg的bytebuf之后，注意调用msg的retain方法，防止msg中的bytebuf提前释放抛出异常
            //这样写了之后，丢失了DatagramPacket中的sender，这样在后续的处理器中，无法直接向来源发送消息，所以这种方式基本只试用于客户端
            //out.add(msg.content());
            //msg.retain();
            
        } catch (Exception e) {
            log.error(String.format("error decode udp packet from [ %s ], ignore!", packet.sender().toString()));
        }
    }
}

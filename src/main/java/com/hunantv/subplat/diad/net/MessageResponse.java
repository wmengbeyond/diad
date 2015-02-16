/**
 * @date 2015-02-12
 * @author wm
 * 查询成功与否反馈信息
 * 1 2 3 4 5 6 7 8 9 A B C D E F 10 。。。
 * 标识  |类型 |  长度        |   msg   
 *   标识: 0x02固定值
 *   类型: CODE_HEART = 0x01 CODE_VIP 0x02 CODE_VIDOE 0x03
 *   长度: 头长0x06+字符串长度msg.len
 *   msg: 根据类型判断 CODE_VIP vip的反馈信息 CODE_VIDOE video的反馈信息
 */

package com.hunantv.subplat.diad.net;

public class MessageResponse {
	private static final byte headTag = MessageHead.RESP;
	
	private byte code;
    
	private int len;
	
	private String msg;//反馈信息
	
	public byte getHeadTag() {
		return headTag;
	}
	
	public int getLen() {
        return len = MessageHead.HEAD_LEN + msg.length();
    }
	
	public byte getCode() {
        return code;
    }
    public void setCode(byte code) {
        this.code = code;
    }
    
    public String getMsg() {
        return msg;
    }
    public void setMsg(String msg) {
    	this.msg = msg;
    }

    @Override  
    public String toString() {  
        StringBuilder builder = new StringBuilder();  
        builder.append("DiadResponseMsg [headTag=").append(headTag).append(", code=").append(code)
        .append(", len=").append(len).append(", msg=").append(msg).append("]");  
        return builder.toString();  
    }  
  
    public byte[] ResponseByte() {
    	int msgLen = getLen();
    	byte[] bytes = new byte[msgLen];
//    	String str1 = String.format("%02d",headTag);
//    	String str2 = String.format("%02d",code);
//    	String str3 = String.format("%08d",msgLen);
//    	String str4 = str1 + str2 + str3 + msg;
    	String str4 = String.format("%02d%02d%08d%s",headTag, code, msgLen, msg);
    	bytes = str4.getBytes();
    
    	return (bytes);
    }
}

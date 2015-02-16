/**
 * @date 2015-02-12
 * @author wm
 * miad发送查询vip和媒资使用的请求信息
 * 1 2 3 4 5 6 7 8 9 A B C D E F 10 。。。
 * 标识  |类型 |  长度       |   msg   
 *   标识: 0x01 固定值
 *   类型: CODE_HEART = 0x01 CODE_VIP 0x02 CODE_VIDOE 0x03
 *   长度: 头长0x03+ID字符串长度str.len
 *   msg: 根据类型判断 CODE_VIP vip的id CODE_VIDOE video的id
 */

package com.hunantv.subplat.diad.net;

public class MessageRequest {
	
	private byte headTag;
	
	private byte code;
	
	private int len;
	
	private String msg;
	
	public MessageRequest(byte tag, byte code, int len, String msg) {
		this.headTag = tag;
		this.code = code;
		this.len = len;
		this.msg = msg;
	}
	
	public byte getHeadTag() {
		return headTag;
	}
	
	public void setHeadTag(byte tag) {
		headTag = tag;
    }
	
	public byte getCode() {
		return code;
	}
	
	public void setCode(byte code) {
		this.code = code;
    }
	
	public int getLen() {
		return len;
	}
	
	public void setLen(int len) {
		this.len = len;
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
        builder.append("MiadRequestMsg [headTag=").append(headTag).append(", len=").append(len)
        .append(", code=").append(code).append(", msg=").append(msg).append("]"); 
        
        return builder.toString();  
    } 
}

/**
 * @date 2015-02-12
 * @author wm
 * 指令集
 * 自定义tcp报文头
 */

package com.hunantv.subplat.diad.net;

public class MessageHead {
    public static final byte REQ = 0x01;			//��������
    
    public static final byte RESP = 0x02;			//Ӧ������
    
    public static final byte CODE_HEART = 0x01; 	//�����
    
	public static final byte CODE_VIP = 0x02; 		//��ѯVIP��Ϣ
	
    public static final byte CODE_VIDOE = 0x03; 	//��ѯý����Ϣ
    
    public static final byte CODE_NONE = 0xF;		//����ָ�� �޷�����
    
    public static final int HEAD_LEN = 6; 			//ͷ����1 1 4 �ֽ�
}
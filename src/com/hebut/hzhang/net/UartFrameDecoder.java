package com.hebut.hzhang.net;

import java.util.Date;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

import com.hebut.hzhang.model.Frame;
import com.hebut.hzhang.util.AppConstants;
import com.hebut.hzhang.util.ByteUtils;

public class UartFrameDecoder implements ProtocolDecoder
{

	/**
	 * 站机发送数据顺序如下
	 * struct uart_frame{
			头部 6+4+1+1 = 12bytes
			long _time;	//4个字节，以s为单位，传到上位机，有上位机负责转换成ms
			u8 frame_type;	//A0,A1,F0,F1,F2,F3,F4...
			u8 data_length;	//数据场的长度
			u8 mac[6];
			//数据域 52bytes
	
			u8 data[52];
		};
	 */
	@Override
	public void decode(IoSession session, IoBuffer in, ProtocolDecoderOutput out)
			throws Exception
	{
		int startPos = in.position();
		byte[] macB = new byte[6];
		byte[] timeB = new byte[4];
		byte typeB = 0x00;
		byte data_lenB = 0;
		byte[] contentB = new byte[52];
		int cnt = 0;
		int i = 0, j = 0;
		byte[] recvBytes = new byte[in.limit()];
		System.out.println("decode()...");
		System.out.println("recv bytes limit: " + in.limit());
		while(in.hasRemaining())
		{
			//每次循环读取一个字节
			byte b = in.get();
			recvBytes[cnt] = b;
			if(cnt < 4){
				timeB[cnt++] = b;
			}else if(cnt < 5){
				typeB = b;
				cnt++;
			}else if(cnt < 6){
				data_lenB = b;
				cnt++;
			}else if(cnt < 12){
				macB[i++] = b;
				cnt++;
			}else{
				contentB[j++] = b;
				cnt++;
			}
		}
		System.out.println("recv bytes: " + ByteUtils.bytesToHexString(recvBytes));
		
		Frame frame = new Frame();
		frame.setMac(ByteUtils.bytesToHexString(macB));
		long timeL = ByteUtils.bytesToInt(timeB);
		timeL = timeL * 1000;	//毫秒
		frame.setTime(new Date(timeL));
		frame.setOrientation(AppConstants.FRAME_ORIEN_R);
		frame.setType(typeB);
		frame.setContentLen(data_lenB);
		frame.setContent(ByteUtils.bytesToHexString(contentB));
		
		out.write(frame);
	}

	@Override
	public void dispose(IoSession ioSession) throws Exception
	{

	}

	@Override
	public void finishDecode(IoSession ioSession, ProtocolDecoderOutput protocolDecoderOutput)
			throws Exception
	{

	}

}

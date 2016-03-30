package com.hebut.hzhang.net;

import java.util.Date;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

import com.hebut.hzhang.model.Frame;
import com.hebut.hzhang.util.AppConstants;
import com.hebut.hzhang.util.ByteUtils;

public class UartFrameEncoder implements ProtocolEncoder
{

	@Override
	public void dispose(IoSession ioSession) throws Exception
	{
		
	}

	@Override
	public void encode(IoSession ioSession, Object message, //
			ProtocolEncoderOutput out)
			throws Exception
	{
		Frame frame = null;
		if(message instanceof Frame)
		{
			frame = (Frame) message;
		}
		if(frame != null)
		{
			//֡��Ϊ64bytes
			IoBuffer ioBuffer = IoBuffer.allocate(64);
			ioBuffer.setAutoExpand(false);
			//001122334455
			String mac = frame.getMac();
			byte[] macB = ByteUtils.hexStringToBytes(mac);
			Date time = frame.getTime();
			//342434325
			Long timeL = time.getTime();
			int timeI = (int) (timeL / 1000);	//ת����s
			byte[] timeB = ByteUtils.getBytes(timeI);
			//A1
			byte typeB = frame.getType();
			//FF1234
			String content = frame.getContent();

			byte data_lenB = (byte) frame.getContentLen();
			//��֤һ֡Ϊ64bytes
			int left = 52 - data_lenB;
			StringBuilder sb = new StringBuilder("");
			sb.append(content);
			for(int i = 0; i < left; i++){
				if(sb.toString().length() / 2  == 52){
					break;
				}
				sb.append("FF");
			}
			content = sb.toString();
			//����˺ܶ�FF������javabean
			frame.setContent(content);
			byte[] contentB = ByteUtils.hexStringToBytes(content);
			
			
			/**
			 * struct uart_frame{
					ͷ�� 6+4+1+1 = 12bytes
					long _time;	//4���ֽڣ���sΪ��λ��������λ��������λ������ת����ms
					u8 frame_type;	//A0,A1,F0,F1,F2,F3,F4...
					u8 data_length;	//���ݳ��ĳ���
					u8 mac[6];
					//������ 52bytes
			
					u8 data[52];
				};
			 */
			ioBuffer.put(timeB);
			ioBuffer.put(typeB);
			ioBuffer.put(data_lenB);
			ioBuffer.put(macB);
			ioBuffer.put(contentB);
			
			ioBuffer.flip();
			out.write(ioBuffer);
		}
	}

}

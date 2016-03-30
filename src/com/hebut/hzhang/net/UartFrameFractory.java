package com.hebut.hzhang.net;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

public class UartFrameFractory implements ProtocolCodecFactory
{

	private UartFrameEncoder mEncoder;
	private UartFrameDecoder mDecoder;
	
	public UartFrameFractory()
	{
		mEncoder = new UartFrameEncoder();
		mDecoder = new UartFrameDecoder();
	}
	@Override
	public ProtocolDecoder getDecoder(IoSession ioSession) throws Exception
	{
		return mDecoder;
	}

	@Override
	public ProtocolEncoder getEncoder(IoSession ioSession) throws Exception
	{
		return mEncoder;
	}

}

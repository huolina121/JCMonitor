package com.hebut.hzhang;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import com.hebut.hzhang.net.DataIoHandler;
import com.hebut.hzhang.net.UartFrameFractory;
import com.hebut.hzhang.util.Context;
import com.hebut.hzhang.view.MyView;

public class JCMonitor
{
	public static void main(String[] args)
	{
		 MyView myView = new MyView();
		Context.getInstance().setMyView(myView);
		try
		{
			NioSocketAcceptor acceptor = new NioSocketAcceptor();
			acceptor.setHandler(new DataIoHandler());
			acceptor.getFilterChain().addLast("codec", //
					new ProtocolCodecFilter(new UartFrameFractory()));
			acceptor.bind(new InetSocketAddress(5003));
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}

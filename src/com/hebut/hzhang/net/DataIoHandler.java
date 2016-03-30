package com.hebut.hzhang.net;

import java.net.InetSocketAddress;
import java.util.Date;

import javax.swing.JOptionPane;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import com.hebut.hzhang.model.Frame;
import com.hebut.hzhang.util.AppConstants;
import com.hebut.hzhang.util.Context;
import com.hebut.hzhang.view.MyView;

public class DataIoHandler extends IoHandlerAdapter
{

	@Override
	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception
	{
		super.exceptionCaught(session, cause);
		JOptionPane.showMessageDialog(Context.getInstance().getMyView(), "网络连接异常");
	}

	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception
	{
		System.out.println("messageReceived()...");
		if(message instanceof Frame){
			Frame frame_recv = (Frame) message;
			System.out.println("RCVD: " + frame_recv);
			
			FrameHandler handler = (FrameHandler) session.getAttribute("HANDLER");
			handler.process(frame_recv);
			
		}
	}

	@Override
	public void messageSent(IoSession session, Object message) throws Exception
	{
		super.messageSent(session, message);
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception
	{
		super.sessionClosed(session);
		JOptionPane.showMessageDialog(Context.getInstance().getMyView(), "与客户端连接断开");
		FrameHandler frameHandler = (FrameHandler) session.getAttribute("HANDLER");
		frameHandler.setSessionCreated(false);
	}

	@Override
	public void sessionCreated(IoSession session) throws Exception
	{
		super.sessionCreated(session);
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status)
			throws Exception
	{
		super.sessionIdle(session, status);
	}

	@Override
	public void sessionOpened(IoSession session) throws Exception
	{
		System.out.println("sessionOpened()...");
		System.out.println("remoteAddress=" + session.getRemoteAddress());
		String clientIp = ((InetSocketAddress)session.getRemoteAddress()).getAddress().getHostAddress();
		JOptionPane.showMessageDialog(Context.getInstance().getMyView(), clientIp +" 建立连接");
		
		FrameHandler handler = (FrameHandler) session.getAttribute("HANDLER");
		if(handler == null){
			handler = new FrameHandler(session);
		}
		handler.setSessionCreated(true);
		session.setAttribute("HANDLER", handler);
		
		//主动发起向客户端请求MAC地址的操作
		/*Frame frame = new Frame();
		frame.setTime(new Date());
		frame.setType(AppConstants.FRAME_TYPE_REQUEST);
		frame.setMac("000000000000");
		frame.setOrientation(AppConstants.FRAME_ORIEN_T);
		frame.setContent("REQUEST");*/
		
		//session.write(frame);
	}

}

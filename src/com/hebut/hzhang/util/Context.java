package com.hebut.hzhang.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.mina.core.session.IoSession;

import com.hebut.hzhang.view.MyView;

public class Context
{
	private static Context context;
	private MyView myView;
	
	private Map<String, IoSession> sessions = new HashMap<String ,IoSession>();
	private Context()
	{
		//全局变量初始化
	}
	
	public static Context getInstance()
	{
		if(context == null)
		{
			synchronized (Context.class)
			{
				if(context == null)
				{
					context = new Context();
				}
			}
		}
		return context;
	}
	
	public void setMyView(MyView myView)
	{
		this.myView = myView;
	}
	
	public MyView getMyView()
	{
		return this.myView;
	}
	
	public void putSession(String mac, IoSession ioSession)
	{
		this.sessions.put(mac, ioSession);
	}
	
	public IoSession getSession(String mac)
	{
		return sessions.get(mac);
	}
}

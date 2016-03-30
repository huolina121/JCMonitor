package com.hebut.hzhang.model;

import java.util.Date;

import com.hebut.hzhang.util.ByteUtils;

public class Frame
{
	private int id;
	
	private String mac;
	
	private Date time;
	
	private byte type;
	
	private int contentLen;
	
	//	T/R: T��ʾ���ͳ�ȥ֡��R��ʾ�յ���֡
	private String orientation;

	//frame����������16�����ַ���
	private String content;

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}
	
	public String getMac()
	{
		return mac;
	}

	public void setMac(String mac)
	{
		this.mac = mac;
	}

	public String getOrientation()
	{
		return orientation;
	}

	public void setOrientation(String orientation)
	{
		this.orientation = orientation;
	}

	public byte getType()
	{
		return type;
	}

	public void setType(byte type)
	{
		this.type = type;
	}
	
	public int getContentLen()
	{
		return contentLen;
	}

	public void setContentLen(int contentLen)
	{
		this.contentLen = contentLen;
	}

	public String getContent()
	{
		return content;
	}

	public void setContent(String content)
	{
		this.content = content;
	}

	public Date getTime()
	{
		return time;
	}

	public void setTime(Date time)
	{
		this.time = time;
	}

	@Override
	public String toString()
	{
		return "Frame [id=" + id + ", mac=" + mac + ", time=" + time
				+ ", type=" + Integer.toHexString(type & 0xff) + ", contentLen=" + contentLen
				+ ", orientation=" + orientation + ", content=" + content + "]";
	}

}

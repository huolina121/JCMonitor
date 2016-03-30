package com.hebut.hzhang.util;

public class ByteUtils
{
	public static String hexStringToASCC(String s){
		StringBuffer sb=new StringBuffer("");
		
		for(int i=0;(2+i)<=s.length();i+=2){
			String str=s.substring(i, i+2);
			sb.append((char)Integer.parseInt(str, 16));
			
		}
		return sb.toString();
	}

	public static String bytesToHexString(byte[] bytes)
	{
		StringBuilder sb = new StringBuilder("");
		for(int i = 0; i < bytes.length; i++)
		{
			int b =  bytes[i] & 0xff;
			String val = Integer.toHexString(b);
			if(val.length() < 2){
				sb.append("0");
			}
			sb.append(val);
		}
		return sb.toString().toUpperCase();
	}
	
	public static byte[] hexStringToBytes(String hexString)
	{
		if(hexString == null || hexString.equals("")) return null;
		
		hexString = hexString.toUpperCase();
		int length = hexString.length() / 2;
		char[] hexChars = hexString.toCharArray();
		byte[] b = new byte[length];
		for(int i = 0; i < length; i++)
		{
			int pos = i*2;
			b[i] = (byte) ((char2byte(hexChars[pos])) << 4 | char2byte(hexChars[pos+1]));
		}
		return b;
	}

	private static byte char2byte(char c)
	{
		return (byte)"0123456789ABCDEF".indexOf(c);
	}
	
	
	public static byte[] getBytes(int data)
	{
		byte[] bytes= new byte[4];
		bytes[0] = (byte) (data & 0xff);
		bytes[1] = (byte) ((data >> 8) & 0xff);
		bytes[2] = (byte) ((data >> 16) & 0xff);
		bytes[3] = (byte) ((data >> 24) & 0xff);
		return bytes;
	}
	
	public static int bytesToInt(byte[] bytes)
	{
		int ret = 0;
		if(bytes.length != 4){
			return 0;
		}
		for(int i = 0; i < bytes.length; i++)
		{
			ret |= ((bytes[i] & 0xff) << (8*i));
		}
		
		return ret;
	}
	
}

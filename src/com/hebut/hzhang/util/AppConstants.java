package com.hebut.hzhang.util;

public class AppConstants
{
	public final static byte FRAME_TYPE_REQUEST = (byte) 0xFF;
	public final static byte FRAME_TYPE_INIT = 0x00;
	public final static byte FRAME_TYPE_HB = 0x01;
	public final static byte FRAME_TYPE_FILE = 0x0F;
	public final static byte FRAME_TYPE_WR_ARR = (byte) 0xa1;
	public final static byte FRAME_TYPE_RD_ARR = (byte) 0xa2;
	public final static byte FRAME_TYPE_FND_FILE = (byte) 0xf1;
	public final static byte FRAME_TYPE_WR_FILE = (byte) 0xf2;
	public final static byte FRAME_TYPE_RD_FILE = (byte) 0xF3;
	public final static byte FRAME_TYPE_DEL_FILE = (byte) 0xF4;

	public final static String FRAME_ORIEN_T = "T";
	public final static String FRAME_ORIEN_R = "R";
	
	public final static String OK = "OK";
	
}

package com.hebut.hzhang.net;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JOptionPane;

import org.apache.mina.core.session.IoSession;

import com.hebut.hzhang.model.Frame;
import com.hebut.hzhang.util.AppConstants;
import com.hebut.hzhang.util.ByteUtils;
import com.hebut.hzhang.util.Context;
import com.hebut.hzhang.view.MyView;

public class FrameHandler
{
	private MyView myView;
	private IoSession ioSession;
	private boolean isSessionCreated = false;
	private List<Frame> fileFrames = new ArrayList<Frame>();
	private StringBuilder sb = new StringBuilder("");
	
	public boolean isFileAckReceived = false;
	public boolean isFileAckTimeout = false;
	
	public FrameHandler()
	{
		myView = Context.getInstance().getMyView();
	}
	
	public FrameHandler(IoSession ioSession)
	{
		this();
		this.ioSession = ioSession;
	}

	/**
	 * 
	 * @param frame
	 *            "F1FFDDBBSSCC"
	 */
	public void process(Frame frame)
	{
		System.out.println("process()...");
		//初始化帧，不显示
		switch(frame.getType())
		{
		case AppConstants.FRAME_TYPE_INIT:
			//将session放入map
			if(!Context.getInstance().getMyView().getComBoxAllItems().contains(frame.getMac())){
				Context.getInstance().getMyView().addMac2ComBox(frame.getMac());
			}
			Context.getInstance().putSession(frame.getMac(), this.ioSession);
			//返回一帧给客户端
			Frame init_ack = new Frame();
			init_ack.setTime(new Date());
			init_ack.setType(AppConstants.FRAME_TYPE_INIT);
			init_ack.setOrientation(AppConstants.FRAME_ORIEN_T);
			init_ack.setMac(frame.getMac());
			init_ack.setContentLen(AppConstants.OK.length());
			init_ack.setContent(str2hexString(AppConstants.OK));
			ioSession.write(init_ack);
			
			break;
		case AppConstants.FRAME_TYPE_HB:
			//显示
			Context.getInstance().getMyView().insertFrame2Table(frame);
			break;
		case AppConstants.FRAME_TYPE_FILE:	//文件内容帧
			Frame file_ack = new Frame();
			file_ack.setMac(frame.getMac());
			file_ack.setTime(new Date());
			file_ack.setType(AppConstants.FRAME_TYPE_RD_FILE);
			file_ack.setOrientation(AppConstants.FRAME_ORIEN_T);
			file_ack.setContent(str2hexString("OK"));
			file_ack.setContentLen(2);
			ioSession.write(file_ack);
			
			Context.getInstance().getMyView().insertFrame2Table(frame);
			processFileFrame(frame);
			//最后一帧，文件帧收取结束
			if(frame.getContentLen() < 52){
				String fileContent = sb.toString();
				sb.delete(0, fileContent.length());
				int index = 0;
				int begin = index;
				//System.out.println(fileContent);
				String filename = Context.getInstance().getMyView().getRdFileName();
				try {
					FileWriter fileWriter = new FileWriter(".\\can_cfg\\" + filename);
					BufferedWriter bw = new BufferedWriter(fileWriter);
					while((index = fileContent.indexOf('\n', begin)) != -1){
						String line = fileContent.substring(begin, index);
						bw.write(line);
						bw.newLine();
						bw.flush();
						begin = index + 1;
					}
					bw.flush();
					bw.close();
					JOptionPane.showMessageDialog(Context.getInstance().getMyView(), "文件读取完成...");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			break;
		case AppConstants.FRAME_TYPE_RD_ARR:
			//显示
			Context.getInstance().getMyView().insertFrame2Table(frame);
			break;
		case AppConstants.FRAME_TYPE_WR_ARR:
			if(frame.getContent().substring(0, 4).equalsIgnoreCase("4F4B")){
				JOptionPane.showMessageDialog(Context.getInstance().getMyView(), 
						"数组写入成功...");
			}else{
				JOptionPane.showMessageDialog(Context.getInstance().getMyView(), 
						"数组写入失败...");
			}
			break;
		case AppConstants.FRAME_TYPE_FND_FILE:
			//显示
			Context.getInstance().getMyView().insertFrame2Table(frame);
			break;
		case AppConstants.FRAME_TYPE_RD_FILE:
			JOptionPane.showMessageDialog(Context.getInstance().getMyView(), 
					"文件读取成功...");
			break;
		case AppConstants.FRAME_TYPE_WR_FILE:
			//OK的ascci码，4f4b
			if(frame.getContent().substring(0, 4).equalsIgnoreCase("4F4B")){
				this.isFileAckReceived = true;
			}else{
				this.isFileAckReceived = false;
			}
			break;
		case AppConstants.FRAME_TYPE_DEL_FILE:
			if(frame.getContent().equals("SUCCESS")){
				JOptionPane.showMessageDialog(Context.getInstance().getMyView(), 
						"文件删除成功...");
			}else{
				JOptionPane.showMessageDialog(Context.getInstance().getMyView(), 
						"文件删除失败...");
			}
			break;
		}
	}

	private void processFileFrame(Frame frame) 
	{
		int data_len = frame.getContentLen();
		String fileStr = null;
		
		byte[] fileB = ByteUtils.hexStringToBytes(frame.getContent());
		if(data_len < 52){
			byte[] fileLastB = new byte[data_len];
			for(int i = 0; i < data_len; i++)
			{
				fileLastB[i] = fileB[i];
			}
			fileStr = new String(fileLastB);
		}else{
			fileStr = new String(fileB);
		}
		
		sb.append(fileStr);
	}

	public boolean isSessionCreated()
	{
		return isSessionCreated;
	}
	
	public void setSessionCreated(boolean isSessionCreated) {
		this.isSessionCreated = isSessionCreated;
	}

	private String str2hexString(String text)
	{
		//text = "filename"
		byte[] bytes = text.getBytes();
		StringBuilder sb = new StringBuilder("");
		for(int i = 0; i < bytes.length; i++)
		{
			int temp = bytes[i] & 0xff;
			sb.append(Integer.toHexString(temp));
		}
		return sb.toString();
	}
}

package com.hebut.hzhang.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.apache.mina.core.session.IoSession;

import com.hebut.hzhang.model.Frame;
import com.hebut.hzhang.net.FrameHandler;
import com.hebut.hzhang.util.AppConstants;
import com.hebut.hzhang.util.ByteUtils;
import com.hebut.hzhang.util.Context;

public class MyView extends JFrame implements ActionListener
{
	//table组件
	private JTable table;
	//系统时间的JLabel
	private JLabel mSysTimeLabel;
	
	//private JTextField macField;
	private JComboBox macCombox;
	private JButton A1Button;
	private JButton A2Button;
	private JButton F1Button;
	private JButton F2Button;
	private JButton F3Button;
	private JButton F4Button;
	private JTextField A1Field;
	private JTextField A2Field;
	private JTextField F1Field;
	private JTextField F2Field;
	private JTextField F3Field;
	private JTextField F4Field;
	
	//table中的数据模型
	private DefaultTableModel tableModel;
	
	private Date mNow;
	
	private int mFrameID = 1;
	
	private IoSession session;
	private FrameHandler frameHandler;
	
	public MyView()
	{
		super("GPRS监测系统");
		this.setBounds(120, 40, 1200, 800);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		//初始化显示布局
		initLayout();
		this.setVisible(true);
		
		initEvent();
		
		Timer timer = new Timer();
		timer.schedule(new TimerTask(){

			@Override
			public void run()
			{
				setSysTimeForJLabel();
			}
			
		}, 0, 1000);
		
		Frame frame = new Frame();
		frame.setMac("112233445566");
		frame.setOrientation(AppConstants.FRAME_ORIEN_R);
		frame.setType(AppConstants.FRAME_TYPE_HB);
		frame.setTime(new Date());
		frame.setContent("A4A531313334355449414E4A494E5AFFFFFFFFFFFFFFFFFFFFFFFFFF");
		frame.setContentLen(18);
		insertFrame2Table(frame);
	}
	
	/**
	 * 	private JButton A1Button;
	private JButton A2Button;
	private JButton F1Button;
	private JButton F2Button;
	private JButton F3Button;
	private JButton F4Button;
	 */
	private void initEvent()
	{
		A1Button.addActionListener(this);
		A2Button.addActionListener(this);
		F1Button.addActionListener(this);
		F2Button.addActionListener(this);
		F3Button.addActionListener(this);
		F4Button.addActionListener(this);
	}

	public void insertFrame2Table(Frame frame)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date = sdf.format(frame.getTime());
		String type = "";
		if(frame.getType() == AppConstants.FRAME_TYPE_HB){
			type = "心跳帧";
		}else if(frame.getType() == AppConstants.FRAME_TYPE_FILE){
			type = "文件帧";
		}else {
			type = "交互帧";
		}
		String[] rowData = new String[]{mFrameID + "", frame.getOrientation(), type,
				frame.getContent(), date, frame.getMac(), frame.getContentLen() + ""};
		
		tableModel.insertRow((mFrameID-1), rowData);
		
		frame.setId(mFrameID);
		
		mFrameID++;
	}
	
	/**
	 * 使用GridBagLayout对各个组件进行排版
	 */
	private void initLayout()
	{
		GridBagLayout gbLayout = new GridBagLayout();
		
		//最上面组件
		mNow = new Date();
		mSysTimeLabel = new JLabel();
		setSysTimeForJLabel();
		
		//左边组件
		JPanel left = new JPanel();
		left.setBackground(Color.LIGHT_GRAY);  
		setLeftContent(left);
		
		//右边组件
		initTable();
		JScrollPane right = new JScrollPane(table);
		//right.setBackground(Color.LIGHT_GRAY);
		
		this.setLayout(gbLayout);
		this.add(mSysTimeLabel);
		this.add(left);
		this.add(right);
		
		GridBagConstraints gridBagConstraints= new GridBagConstraints();
		/**
		 * @gridx,gridy:
	     * 组件左上角所在的位置，如上图中左侧的面板在1行0列，则gridy=0,gridx=1。读者请注意这里的行对应的是gridy,列对应的是gridx
		 */
		//BOTH：使组件完全填满其显示区域。
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		//占一行两列
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.gridheight = 1;
		//是否拉神，表示X方向拉神，Y方向不拉伸
		gridBagConstraints.weightx = 100;
		gridBagConstraints.weighty = 0;
		//指定组件大小
		gridBagConstraints.ipadx = 0;
		gridBagConstraints.ipady = 50;
		gbLayout.setConstraints(mSysTimeLabel, gridBagConstraints);
		
		//left
		//BOTH：使组件完全填满其显示区域。
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.gridwidth = 1;
		gridBagConstraints.gridheight = 1;
		gridBagConstraints.weightx = 0;
		gridBagConstraints.weighty = 100;
		gridBagConstraints.ipadx = 150;
		gridBagConstraints.ipady = 0;
		gbLayout.setConstraints(left, gridBagConstraints);
		
		//right
		//BOTH：使组件完全填满其显示区域。
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.gridwidth = 1;
		gridBagConstraints.gridheight = 1;
		//gridBagConstraints.weightx = 0;
		//gridBagConstraints.weighty = 1;
		//gridBagConstraints.ipadx = 180;
		//gridBagConstraints.ipady = 100;
		gbLayout.setConstraints(right, gridBagConstraints);
		
	}

	/**
	 * 左侧布局内容
	 * @param left
	 */
	private void setLeftContent(JPanel left)
	{
		GridBagLayout gbLayout = new GridBagLayout();
		left.setLayout(gbLayout);
		//第一行: MAC地址
		JLabel macLabel = new JLabel("站机MAC地址：");
		macLabel.setFont(new java.awt.Font("Dialog", 1, 18));
		macLabel.setHorizontalAlignment(JLabel.CENTER);
		
		//macField = new JTextField();
		//String macStr = "00 00 00 00 00 00";
		//macField.setText(macStr);
		macCombox = new JComboBox();
		
		//第二行：命令 命令参数
		JLabel cmdLabel = new JLabel("命令");
		cmdLabel.setFont(new java.awt.Font("Dialog", 1, 18));
		cmdLabel.setHorizontalAlignment(JLabel.CENTER);
		
		JLabel cmdParamLabel = new JLabel("命令参数");
		cmdParamLabel.setFont(new java.awt.Font("Dialog", 1, 18));
		cmdParamLabel.setHorizontalAlignment(JLabel.CENTER);
		
		//第3行：读数组命令
		A1Button = new JButton("读数组");
		A1Field = new JTextField();
		
		//第4行：写数组命令
		A2Button = new JButton("写数组");
		A2Field = new JTextField();
		
		//第5行：查文件命令
		F1Button = new JButton("查文件");
		F1Field = new JTextField();
		
		//第6行：读文件命令
		F2Button = new JButton("读文件");
		F2Field = new JTextField();
		
		//第7行：读文件命令
		F3Button = new JButton("写文件");
		F3Field = new JTextField();
		
		//第8行：读文件命令
		F4Button = new JButton("删文件");
		F4Field = new JTextField();
		
		left.add(macLabel);
		left.add(macCombox);
		left.add(cmdLabel);
		left.add(cmdParamLabel);
		left.add(A1Button);
		left.add(A1Field);
		left.add(A2Button);
		left.add(A2Field);
		left.add(F1Button);
		left.add(F1Field);
		left.add(F2Button);
		left.add(F2Field);
		left.add(F3Button);
		left.add(F3Field);
		left.add(F4Button);
		left.add(F4Field);
		
		gbLayout.setConstraints(macLabel, generateConstraints(GridBagConstraints.BOTH, 
				0, 0, 1, 1, 0, 0));
		gbLayout.setConstraints(macCombox, generateConstraints(GridBagConstraints.BOTH, 
				1, 0, 1, 1, 0, 0));
		gbLayout.setConstraints(cmdLabel, generateConstraints(GridBagConstraints.BOTH, 
				0, 1, 1, 1, 0, 0));
		gbLayout.setConstraints(cmdParamLabel, generateConstraints(GridBagConstraints.BOTH, 
				1, 1, 1, 1, 180, 0));
		gbLayout.setConstraints(A1Button, generateConstraints(GridBagConstraints.BOTH, 
				0, 2, 1, 1, 0, 0));
		gbLayout.setConstraints(A1Field, generateConstraints(GridBagConstraints.BOTH, 
				1, 2, 1, 1, 0, 0));
		gbLayout.setConstraints(A2Button, generateConstraints(GridBagConstraints.BOTH, 
				0, 3, 1, 1, 0, 0));
		gbLayout.setConstraints(A2Field, generateConstraints(GridBagConstraints.BOTH, 
				1, 3, 1, 1, 0, 0));
		gbLayout.setConstraints(F1Button, generateConstraints(GridBagConstraints.BOTH, 
				0, 4, 1, 1, 0, 0));
		gbLayout.setConstraints(F1Field, generateConstraints(GridBagConstraints.BOTH, 
				1, 4, 1, 1, 0, 0));
		gbLayout.setConstraints(F2Button, generateConstraints(GridBagConstraints.BOTH, 
				0, 5, 1, 1, 0, 0));
		gbLayout.setConstraints(F2Field, generateConstraints(GridBagConstraints.BOTH, 
				1, 5, 1, 1, 0, 0));
		gbLayout.setConstraints(F3Button, generateConstraints(GridBagConstraints.BOTH, 
				0, 6, 1, 1, 0, 0));
		gbLayout.setConstraints(F3Field, generateConstraints(GridBagConstraints.BOTH, 
				1, 6, 1, 1, 0, 0));
		gbLayout.setConstraints(F4Button, generateConstraints(GridBagConstraints.BOTH, 
				0, 7, 1, 1, 0, 0));
		gbLayout.setConstraints(F4Field, generateConstraints(GridBagConstraints.BOTH, 
				1, 7, 1, 1, 0, 0));
	}
	
	private GridBagConstraints generateConstraints(int fill, //
			int gridx, int gridy, int gridwidth, int gridheight, int ipadx, int ipady)
	{
		GridBagConstraints gridBagConstraints= new GridBagConstraints();
		//BOTH：使组件完全填满其显示区域。
		gridBagConstraints.fill = fill;
		
		//单元格索引
		gridBagConstraints.gridx = gridx;
		gridBagConstraints.gridy = gridy;
		//占一行两列
		gridBagConstraints.gridwidth = gridwidth;
		gridBagConstraints.gridheight = gridheight;
		
		//组件大小
		gridBagConstraints.ipadx = ipadx;
		gridBagConstraints.ipady = ipady;
		
		//组件与所处格子边框的距离
		Insets in = new Insets(20, 10, 20, 10);
		gridBagConstraints.insets = in;
		
		return gridBagConstraints;
	}

	private void setSysTimeForJLabel()
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		mNow = new Date();
		String dateStr = sdf.format(mNow);
		mSysTimeLabel.setText("本机系统时间：" + dateStr);
		mSysTimeLabel.setFont(new java.awt.Font("Dialog", 1, 22));
		mSysTimeLabel.setHorizontalAlignment(JLabel.CENTER);
	}

	/**
	 * 初始化表格样式
	 */
	private void initTable()
	{
		String titles[] = {"序号", "T/R", "帧类型", "十六进制显示", "时间", "MAC", "DATALEN"};
		
		tableModel = new DefaultTableModel(titles, 30){
			/**
			 * 设置表格中的行不可编辑
			 */
			@Override
			public boolean isCellEditable(int row, int column)
			{
				return false;
			}
		};
		
		table = new JTable(tableModel);
		
		/**
		 * 设置table的列宽
		 */
		table.getColumnModel().getColumn(0).setPreferredWidth(30);
		table.getColumnModel().getColumn(0).setMaxWidth(30);
		table.getColumnModel().getColumn(0).setMinWidth(30);
		
		table.getColumnModel().getColumn(1).setPreferredWidth(30);
		table.getColumnModel().getColumn(1).setMaxWidth(30);
		table.getColumnModel().getColumn(1).setMinWidth(30);
		
		table.getColumnModel().getColumn(2).setPreferredWidth(80);
		table.getColumnModel().getColumn(2).setMaxWidth(80);
		table.getColumnModel().getColumn(2).setMinWidth(80);
		
		table.getColumnModel().getColumn(4).setPreferredWidth(130);
		table.getColumnModel().getColumn(4).setMaxWidth(130);
		table.getColumnModel().getColumn(4).setMinWidth(130);
		
		//隐藏最后两列
		table.getColumnModel().removeColumn(table.getColumnModel().getColumn(6));
		table.getColumnModel().removeColumn(table.getColumnModel().getColumn(5));
		
		table.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2){
                	int row = table.getSelectedRow();
       
                	
                	String[] rowData = new String[]{row+"", (String)tableModel.getValueAt(row, 1), (String)tableModel.getValueAt(row, 2),
                			(String)tableModel.getValueAt(row, 3), (String)tableModel.getValueAt(row, 4), (String) tableModel.getValueAt(row, 5), (String)tableModel.getValueAt(row, 6)};
                	//HeartView hv=new HeartView(rowData);
                	new PacView(rowData).setVisible(true);
                	
//               	String mac = (String) tableModel.getValueAt(row, 5);
//               	String len = (String) tableModel.getValueAt(row, 6);
//           	String str1=context.substring(0, 2);
//              	String str2=context.substring(2, 4);
//            	
            	//JOptionPane.showMessageDialog(MyView.this, mac + "..." + len);
//                	
//               	String str2=context.substring(4, 9);
                	
                }
            }
        });
	}
	
	private boolean isHexString(String text)
	{
		text = text.toUpperCase();
		char[] array = text.toCharArray();
		for(int i = 0; i < array.length; i++)
		{
			if("0123456789ABCDEF".indexOf(array[i]) < 0){
				return false;
			}
		}
		return true;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if(macCombox.getItemCount() == 0){
			JOptionPane.showMessageDialog(MyView.this, "当前没有客户端连接");
			return;
		}
		Frame frame = new Frame();
		String mac = macCombox.getSelectedItem().toString();
		mac = mac.replace(" ", "");
		if(mac == null || "".equals(mac)){
			JOptionPane.showMessageDialog(MyView.this, "mac地址不能为空");
			return;
		}
		if(mac.length() != 12 || !isHexString(mac)){
			JOptionPane.showMessageDialog(MyView.this, "MAC地址格式不正确");
			return;
		}
		frame.setMac(mac);
		frame.setTime(new Date());
		frame.setOrientation(AppConstants.FRAME_ORIEN_T);
		session = Context.getInstance().getSession(mac);
		frameHandler = (FrameHandler) session.getAttribute("HANDLER");
		if(session == null || !frameHandler.isSessionCreated())
		{
			JOptionPane.showMessageDialog(MyView.this, "无法连接到客户端");
			return;
		}
		
		String text = "";
		if(e.getSource() == A1Button)	//读数组
		{
			text = A1Field.getText().replace(" ", "");
			if(text == null || "".equals(text)){
				JOptionPane.showMessageDialog(MyView.this, "输入的内容不能为空");
				return;
			}
			if(text.length() != 2)
			{
				JOptionPane.showMessageDialog(MyView.this, "输入的内容不合法");
				return;
			}
			frame.setType(AppConstants.FRAME_TYPE_RD_ARR);
			
			frame.setContent(text);
			
			frame.setContentLen(text.length() / 2);
			
			//数据打给客户端
			session.write(frame);
			//在表格中显示,
			//在session.write()中保证了一帧为64个字节
			insertFrame2Table(frame);
			
		}else if(e.getSource() == A2Button)	//写数组
		{
			text = A2Field.getText().replace(" ", "");
			if(text == null || "".equals(text)){
				JOptionPane.showMessageDialog(MyView.this, "输入的内容不能为空");
				return;
			}
			if(text.length() % 2 != 0)
			{
				JOptionPane.showMessageDialog(MyView.this, "输入的内容不合法");
				return;
			}
			
			frame.setType(AppConstants.FRAME_TYPE_WR_ARR);
			
			frame.setContent(text);
			
			frame.setContentLen(text.length() / 2);
			
			//数据打给客户端
			session.write(frame);
			insertFrame2Table(frame);
			
		}else if(e.getSource() == F1Button)	//查文件
		{
			String filename = F1Field.getText().replace(" ", "");
			if(filename == null || "".equals(filename)){
				JOptionPane.showMessageDialog(MyView.this, "输入的内容不能为空");
				return;
			}
			//文件名转成16进制字符串
			text = str2hexString(filename);
			
			frame.setType(AppConstants.FRAME_TYPE_FND_FILE);
			
			frame.setContent(text);
			
			frame.setContentLen(filename.length());
			
			//数据打给客户端
			session.write(frame);
			//在表格中显示,
			//在session.write()中保证了一帧为64个字节
			insertFrame2Table(frame);
			
		}else if(e.getSource() == F2Button)	//读文件
		{
			String filename = F2Field.getText().replace(" ", "");
			if(filename == null || "".equals(filename)){
				JOptionPane.showMessageDialog(MyView.this, "输入的内容不能为空");
				return;
			}
			text = str2hexString(filename);
			
			frame.setType(AppConstants.FRAME_TYPE_RD_FILE);
			
			frame.setContent(text);
			
			frame.setContentLen(filename.length());
			
			//数据打给客户端
			session.write(frame);
			//在表格中显示,
			//在session.write()中保证了一帧为64个字节
			insertFrame2Table(frame);
			
		}else if(e.getSource() == F3Button)	//写文件
		{
			String filename = F3Field.getText().replace(" ", "").toLowerCase();
			if(filename == null || "".equals(filename)){
				JOptionPane.showMessageDialog(MyView.this, "输入的内容不能为空");
				return;
			}
			//文件名合法性检查
			//TODO
			File  file = new File(".\\can_cfg\\" + filename);
			if(!file.exists()){
				JOptionPane.showMessageDialog(MyView.this, "文件不存在");
				return;
			}
			
			text = str2hexString(filename);
			
			frame.setType(AppConstants.FRAME_TYPE_WR_FILE);
			
			frame.setContent(text);
			frame.setContentLen(filename.length());
			//1.先发送写文件命令
			session.write(frame);
			insertFrame2Table(frame);
			
			//2.随后发送文件内容...

			//构造文件帧
			frame.setTime(new Date());
			frame.setType(AppConstants.FRAME_TYPE_FILE);
		
			try {
				BufferedReader br = new BufferedReader(new FileReader(file));
				String line = null;
				StringBuilder sb = new StringBuilder("");
				while((line = br.readLine()) != null){
					sb.append(line);
					//按linux下的换行为准进行传输
					//window从linux读取文件的时候，需要将 '\n'替换成'\r\n'
					sb.append('\n');
				}
				br.close();
				String content = sb.toString();
				System.out.println("send file content: ");
				System.out.println(content);
				
				byte[] contentB = content.getBytes();
				int length = contentB.length;
				byte[] tempB = new byte[52];
				int pos = 0;
				while((length / 52) > 0)
				{
					length -= 52;
					
					for(int i = 0; i < 52; i++)
					{
						tempB[i] = contentB[pos++];
					}
					frame.setContent(ByteUtils.bytesToHexString(tempB));
					frame.setContentLen(52);
					
					session.write(frame);
					//会阻塞UI线程
					waitFileAckFromClient();
					if(frameHandler.isFileAckTimeout){
						System.out.println("send file time out...");
						JOptionPane.showMessageDialog(MyView.this, "文件发送超时");
						frameHandler.isFileAckTimeout = false;
						return;
					}
					
					insertFrame2Table(frame);
				}
				if(contentB.length % 52 == 0){
					//构造一个contentLen = 0的空帧
					for(int i = 0; i < 52; i++){
						tempB[i] = (byte) 0xff;
					}
					frame.setContentLen(0);
					frame.setContent(ByteUtils.bytesToHexString(tempB));
				}else{
					byte[] lastB = new byte[length];
					for(int  i = 0; i < length; i++)
					{
						lastB[i] = contentB[pos++];
					}
					System.out.println("last frame content len: " + lastB.length);
					frame.setContentLen(length);
					frame.setContent(ByteUtils.bytesToHexString(lastB));
				}
				session.write(frame);
				
				//会阻塞UI线程
				waitFileAckFromClient();
				if(frameHandler.isFileAckTimeout){
					System.out.println("send file time out...");
					JOptionPane.showMessageDialog(MyView.this, "文件发送超时");
					frameHandler.isFileAckTimeout = false;
					return;
				}
				insertFrame2Table(frame);
				
				JOptionPane.showMessageDialog(MyView.this, "文件发送成功");
				
			} catch (Exception e1) {
				JOptionPane.showMessageDialog(MyView.this, "文件发送失败");
				e1.printStackTrace();
			}
			
			
		}else if(e.getSource() == F4Button)	//删文件
		{
			String filename = F4Field.getText().replace(" ", "");
			if(filename == null || "".equals(filename)){
				JOptionPane.showMessageDialog(MyView.this, "输入的内容不能为空");
				return;
			}
			text = str2hexString(filename);
			
			frame.setType(AppConstants.FRAME_TYPE_DEL_FILE);
			
			frame.setContent(text);
			
			frame.setContentLen(filename.length());
			
			//数据打给客户端
			session.write(frame);
			//在表格中显示,
			//在session.write()中保证了一帧为64个字节
			insertFrame2Table(frame);
		}
	}

	private void waitFileAckFromClient() {
		int cnt = 0;
		while(!frameHandler.isFileAckReceived){
			try {
				cnt++;
				Thread.sleep(200);
				//20秒之后，如果还没有收到ACK，退出
				if(cnt == 100) {
					frameHandler.isFileAckTimeout = true;
					break;
				}else{
					frameHandler.isFileAckTimeout = false;
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		frameHandler.isFileAckReceived = false;
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
	
	public void addMac2ComBox(String mac)
	{
		this.macCombox.addItem(mac);
	}
	
	public List<String> getComBoxAllItems()
	{
		List<String> allItems = new ArrayList<String>();
		int count = this.macCombox.getItemCount();
		for(int i = 0; i < count; i++)
		{
			allItems.add((String) this.macCombox.getItemAt(i));
		}
		return allItems;
	}

	public String getRdFileName() {
		
		return F2Field.getText().replace(" ", "").toLowerCase();
	}
}

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
	//table���
	private JTable table;
	//ϵͳʱ���JLabel
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
	
	//table�е�����ģ��
	private DefaultTableModel tableModel;
	
	private Date mNow;
	
	private int mFrameID = 1;
	
	private IoSession session;
	private FrameHandler frameHandler;
	
	public MyView()
	{
		super("GPRS���ϵͳ");
		this.setBounds(120, 40, 1200, 800);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		//��ʼ����ʾ����
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
			type = "����֡";
		}else if(frame.getType() == AppConstants.FRAME_TYPE_FILE){
			type = "�ļ�֡";
		}else {
			type = "����֡";
		}
		String[] rowData = new String[]{mFrameID + "", frame.getOrientation(), type,
				frame.getContent(), date, frame.getMac(), frame.getContentLen() + ""};
		
		tableModel.insertRow((mFrameID-1), rowData);
		
		frame.setId(mFrameID);
		
		mFrameID++;
	}
	
	/**
	 * ʹ��GridBagLayout�Ը�����������Ű�
	 */
	private void initLayout()
	{
		GridBagLayout gbLayout = new GridBagLayout();
		
		//���������
		mNow = new Date();
		mSysTimeLabel = new JLabel();
		setSysTimeForJLabel();
		
		//������
		JPanel left = new JPanel();
		left.setBackground(Color.LIGHT_GRAY);  
		setLeftContent(left);
		
		//�ұ����
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
	     * ������Ͻ����ڵ�λ�ã�����ͼ�����������1��0�У���gridy=0,gridx=1��������ע��������ж�Ӧ����gridy,�ж�Ӧ����gridx
		 */
		//BOTH��ʹ�����ȫ��������ʾ����
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		//ռһ������
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.gridheight = 1;
		//�Ƿ����񣬱�ʾX��������Y��������
		gridBagConstraints.weightx = 100;
		gridBagConstraints.weighty = 0;
		//ָ�������С
		gridBagConstraints.ipadx = 0;
		gridBagConstraints.ipady = 50;
		gbLayout.setConstraints(mSysTimeLabel, gridBagConstraints);
		
		//left
		//BOTH��ʹ�����ȫ��������ʾ����
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
		//BOTH��ʹ�����ȫ��������ʾ����
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
	 * ��಼������
	 * @param left
	 */
	private void setLeftContent(JPanel left)
	{
		GridBagLayout gbLayout = new GridBagLayout();
		left.setLayout(gbLayout);
		//��һ��: MAC��ַ
		JLabel macLabel = new JLabel("վ��MAC��ַ��");
		macLabel.setFont(new java.awt.Font("Dialog", 1, 18));
		macLabel.setHorizontalAlignment(JLabel.CENTER);
		
		//macField = new JTextField();
		//String macStr = "00 00 00 00 00 00";
		//macField.setText(macStr);
		macCombox = new JComboBox();
		
		//�ڶ��У����� �������
		JLabel cmdLabel = new JLabel("����");
		cmdLabel.setFont(new java.awt.Font("Dialog", 1, 18));
		cmdLabel.setHorizontalAlignment(JLabel.CENTER);
		
		JLabel cmdParamLabel = new JLabel("�������");
		cmdParamLabel.setFont(new java.awt.Font("Dialog", 1, 18));
		cmdParamLabel.setHorizontalAlignment(JLabel.CENTER);
		
		//��3�У�����������
		A1Button = new JButton("������");
		A1Field = new JTextField();
		
		//��4�У�д��������
		A2Button = new JButton("д����");
		A2Field = new JTextField();
		
		//��5�У����ļ�����
		F1Button = new JButton("���ļ�");
		F1Field = new JTextField();
		
		//��6�У����ļ�����
		F2Button = new JButton("���ļ�");
		F2Field = new JTextField();
		
		//��7�У����ļ�����
		F3Button = new JButton("д�ļ�");
		F3Field = new JTextField();
		
		//��8�У����ļ�����
		F4Button = new JButton("ɾ�ļ�");
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
		//BOTH��ʹ�����ȫ��������ʾ����
		gridBagConstraints.fill = fill;
		
		//��Ԫ������
		gridBagConstraints.gridx = gridx;
		gridBagConstraints.gridy = gridy;
		//ռһ������
		gridBagConstraints.gridwidth = gridwidth;
		gridBagConstraints.gridheight = gridheight;
		
		//�����С
		gridBagConstraints.ipadx = ipadx;
		gridBagConstraints.ipady = ipady;
		
		//������������ӱ߿�ľ���
		Insets in = new Insets(20, 10, 20, 10);
		gridBagConstraints.insets = in;
		
		return gridBagConstraints;
	}

	private void setSysTimeForJLabel()
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		mNow = new Date();
		String dateStr = sdf.format(mNow);
		mSysTimeLabel.setText("����ϵͳʱ�䣺" + dateStr);
		mSysTimeLabel.setFont(new java.awt.Font("Dialog", 1, 22));
		mSysTimeLabel.setHorizontalAlignment(JLabel.CENTER);
	}

	/**
	 * ��ʼ�������ʽ
	 */
	private void initTable()
	{
		String titles[] = {"���", "T/R", "֡����", "ʮ��������ʾ", "ʱ��", "MAC", "DATALEN"};
		
		tableModel = new DefaultTableModel(titles, 30){
			/**
			 * ���ñ���е��в��ɱ༭
			 */
			@Override
			public boolean isCellEditable(int row, int column)
			{
				return false;
			}
		};
		
		table = new JTable(tableModel);
		
		/**
		 * ����table���п�
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
		
		//�����������
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
			JOptionPane.showMessageDialog(MyView.this, "��ǰû�пͻ�������");
			return;
		}
		Frame frame = new Frame();
		String mac = macCombox.getSelectedItem().toString();
		mac = mac.replace(" ", "");
		if(mac == null || "".equals(mac)){
			JOptionPane.showMessageDialog(MyView.this, "mac��ַ����Ϊ��");
			return;
		}
		if(mac.length() != 12 || !isHexString(mac)){
			JOptionPane.showMessageDialog(MyView.this, "MAC��ַ��ʽ����ȷ");
			return;
		}
		frame.setMac(mac);
		frame.setTime(new Date());
		frame.setOrientation(AppConstants.FRAME_ORIEN_T);
		session = Context.getInstance().getSession(mac);
		frameHandler = (FrameHandler) session.getAttribute("HANDLER");
		if(session == null || !frameHandler.isSessionCreated())
		{
			JOptionPane.showMessageDialog(MyView.this, "�޷����ӵ��ͻ���");
			return;
		}
		
		String text = "";
		if(e.getSource() == A1Button)	//������
		{
			text = A1Field.getText().replace(" ", "");
			if(text == null || "".equals(text)){
				JOptionPane.showMessageDialog(MyView.this, "��������ݲ���Ϊ��");
				return;
			}
			if(text.length() != 2)
			{
				JOptionPane.showMessageDialog(MyView.this, "��������ݲ��Ϸ�");
				return;
			}
			frame.setType(AppConstants.FRAME_TYPE_RD_ARR);
			
			frame.setContent(text);
			
			frame.setContentLen(text.length() / 2);
			
			//���ݴ���ͻ���
			session.write(frame);
			//�ڱ������ʾ,
			//��session.write()�б�֤��һ֡Ϊ64���ֽ�
			insertFrame2Table(frame);
			
		}else if(e.getSource() == A2Button)	//д����
		{
			text = A2Field.getText().replace(" ", "");
			if(text == null || "".equals(text)){
				JOptionPane.showMessageDialog(MyView.this, "��������ݲ���Ϊ��");
				return;
			}
			if(text.length() % 2 != 0)
			{
				JOptionPane.showMessageDialog(MyView.this, "��������ݲ��Ϸ�");
				return;
			}
			
			frame.setType(AppConstants.FRAME_TYPE_WR_ARR);
			
			frame.setContent(text);
			
			frame.setContentLen(text.length() / 2);
			
			//���ݴ���ͻ���
			session.write(frame);
			insertFrame2Table(frame);
			
		}else if(e.getSource() == F1Button)	//���ļ�
		{
			String filename = F1Field.getText().replace(" ", "");
			if(filename == null || "".equals(filename)){
				JOptionPane.showMessageDialog(MyView.this, "��������ݲ���Ϊ��");
				return;
			}
			//�ļ���ת��16�����ַ���
			text = str2hexString(filename);
			
			frame.setType(AppConstants.FRAME_TYPE_FND_FILE);
			
			frame.setContent(text);
			
			frame.setContentLen(filename.length());
			
			//���ݴ���ͻ���
			session.write(frame);
			//�ڱ������ʾ,
			//��session.write()�б�֤��һ֡Ϊ64���ֽ�
			insertFrame2Table(frame);
			
		}else if(e.getSource() == F2Button)	//���ļ�
		{
			String filename = F2Field.getText().replace(" ", "");
			if(filename == null || "".equals(filename)){
				JOptionPane.showMessageDialog(MyView.this, "��������ݲ���Ϊ��");
				return;
			}
			text = str2hexString(filename);
			
			frame.setType(AppConstants.FRAME_TYPE_RD_FILE);
			
			frame.setContent(text);
			
			frame.setContentLen(filename.length());
			
			//���ݴ���ͻ���
			session.write(frame);
			//�ڱ������ʾ,
			//��session.write()�б�֤��һ֡Ϊ64���ֽ�
			insertFrame2Table(frame);
			
		}else if(e.getSource() == F3Button)	//д�ļ�
		{
			String filename = F3Field.getText().replace(" ", "").toLowerCase();
			if(filename == null || "".equals(filename)){
				JOptionPane.showMessageDialog(MyView.this, "��������ݲ���Ϊ��");
				return;
			}
			//�ļ����Ϸ��Լ��
			//TODO
			File  file = new File(".\\can_cfg\\" + filename);
			if(!file.exists()){
				JOptionPane.showMessageDialog(MyView.this, "�ļ�������");
				return;
			}
			
			text = str2hexString(filename);
			
			frame.setType(AppConstants.FRAME_TYPE_WR_FILE);
			
			frame.setContent(text);
			frame.setContentLen(filename.length());
			//1.�ȷ���д�ļ�����
			session.write(frame);
			insertFrame2Table(frame);
			
			//2.������ļ�����...

			//�����ļ�֡
			frame.setTime(new Date());
			frame.setType(AppConstants.FRAME_TYPE_FILE);
		
			try {
				BufferedReader br = new BufferedReader(new FileReader(file));
				String line = null;
				StringBuilder sb = new StringBuilder("");
				while((line = br.readLine()) != null){
					sb.append(line);
					//��linux�µĻ���Ϊ׼���д���
					//window��linux��ȡ�ļ���ʱ����Ҫ�� '\n'�滻��'\r\n'
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
					//������UI�߳�
					waitFileAckFromClient();
					if(frameHandler.isFileAckTimeout){
						System.out.println("send file time out...");
						JOptionPane.showMessageDialog(MyView.this, "�ļ����ͳ�ʱ");
						frameHandler.isFileAckTimeout = false;
						return;
					}
					
					insertFrame2Table(frame);
				}
				if(contentB.length % 52 == 0){
					//����һ��contentLen = 0�Ŀ�֡
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
				
				//������UI�߳�
				waitFileAckFromClient();
				if(frameHandler.isFileAckTimeout){
					System.out.println("send file time out...");
					JOptionPane.showMessageDialog(MyView.this, "�ļ����ͳ�ʱ");
					frameHandler.isFileAckTimeout = false;
					return;
				}
				insertFrame2Table(frame);
				
				JOptionPane.showMessageDialog(MyView.this, "�ļ����ͳɹ�");
				
			} catch (Exception e1) {
				JOptionPane.showMessageDialog(MyView.this, "�ļ�����ʧ��");
				e1.printStackTrace();
			}
			
			
		}else if(e.getSource() == F4Button)	//ɾ�ļ�
		{
			String filename = F4Field.getText().replace(" ", "");
			if(filename == null || "".equals(filename)){
				JOptionPane.showMessageDialog(MyView.this, "��������ݲ���Ϊ��");
				return;
			}
			text = str2hexString(filename);
			
			frame.setType(AppConstants.FRAME_TYPE_DEL_FILE);
			
			frame.setContent(text);
			
			frame.setContentLen(filename.length());
			
			//���ݴ���ͻ���
			session.write(frame);
			//�ڱ������ʾ,
			//��session.write()�б�֤��һ֡Ϊ64���ֽ�
			insertFrame2Table(frame);
		}
	}

	private void waitFileAckFromClient() {
		int cnt = 0;
		while(!frameHandler.isFileAckReceived){
			try {
				cnt++;
				Thread.sleep(200);
				//20��֮�������û���յ�ACK���˳�
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

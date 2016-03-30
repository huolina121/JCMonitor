package com.hebut.hzhang.view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRootPane;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;

import com.hebut.hzhang.util.ByteUtils;

public class HeartView extends JFrame implements ActionListener{

	private JLabel time1;
	private MJTextField time;
	private JLabel mac1;
	private MJTextField mac;
	private JLabel as1;
	private MJTextField as;
	private JLabel ss1;
	private MJTextField ss;
	private JLabel id1;
	private MJTextField id;
	private JLabel macName1;
	private MJTextField macName;
	private JButton bt;
	private JLabel jl;
	private JLabel tOrf;
	private JLabel tOrf1;
	private JLabel tOrf2;
	private int xx, yy;
	private boolean isDraging = false;
	public HeartView(String[] rowData) {
		String[] aa = new String[7];
		aa = rowData;

		String context = aa[3];
		int len = Integer.parseInt(aa[6])*2;
		String str1 = context.substring(0, 2);
		String str2 = context.substring(2, 4);
		String str3 = context.substring(4, 14);
		String str4 = context.substring(14,len);
		String str31 = ByteUtils.hexStringToASCC(str3);
		String str41 = ByteUtils.hexStringToASCC(str4);

		GridBagLayout gbLayout = new GridBagLayout();
		this.setLayout(gbLayout);
		this.setUndecorated(true);
		this.setVisible(true);
		this.setSize(600, 600);
		this.setResizable(false);
		setLocationRelativeTo(null);// 窗体居中
		
    	this.getRootPane().setBorder(
 				//BorderFactory.createBevelBorder(BevelBorder.RAISED 
 						BorderFactory.createEtchedBorder());
    
		time = new MJTextField(aa[4]);
		time.setEditable(false);
		mac = new MJTextField(aa[5]);
		mac.setEditable(false);
		as = new MJTextField(str1);
		as.setEditable(false);
		ss = new MJTextField(str2);
		ss.setEditable(false);
		id = new MJTextField(str31);
		id.setEditable(false);
		macName = new MJTextField(str41);
		macName.setEditable(false);

		time1 = new JLabel("Time");
		mac1 = new JLabel("MAC");
		as1 = new JLabel("AS");
		ss1 = new JLabel("SS");
		id1 = new JLabel("ID");
		macName1 = new JLabel("站机名");

		bt = new JButton("确定");
		tOrf1 = getJLabel(str1);
		tOrf2 = getJLabel(str2);
		jl=new JLabel(aa[2]);
		jl.setFont(new java.awt.Font("宋体", 1, 22));
		this.add(jl);
		this.add(time1);
		this.add(time);
		this.add(mac);
		this.add(mac1);
		this.add(as);
		this.add(as1);
		this.add(ss);
		this.add(ss1);
		this.add(id);
		this.add(id1);
		this.add(macName);
		this.add(macName1);
		this.add(tOrf1);
		this.add(tOrf2);
		this.add(bt);
        bt.addActionListener(this);
        gbLayout.setConstraints(jl,
				generateConstraints(GridBagConstraints.CENTER, 0, 0, 3, 1, 0, 0));
		gbLayout.setConstraints(time1,
				generateConstraints(GridBagConstraints.BOTH, 0, 1, 1, 1, 0, 0));
		gbLayout.setConstraints(time,
				generateConstraints(GridBagConstraints.BOTH, 1, 1, 1, 1, 0, 0));
		gbLayout.setConstraints(mac1,
				generateConstraints(GridBagConstraints.BOTH, 0, 2, 1, 1, 0, 0));
		gbLayout.setConstraints(mac,
				generateConstraints(GridBagConstraints.BOTH, 1, 2, 1, 1, 0, 0));
		gbLayout.setConstraints(as1,
				generateConstraints(GridBagConstraints.BOTH, 0, 3, 1, 1, 0, 0));
		gbLayout.setConstraints(as,
				generateConstraints(GridBagConstraints.BOTH, 1, 3, 1, 1, 0, 0));

		gbLayout.setConstraints(tOrf1,
				generateConstraints(GridBagConstraints.BOTH, 2, 3, 1, 1, 0, 0));

		gbLayout.setConstraints(ss1,
				generateConstraints(GridBagConstraints.BOTH, 0, 4, 1, 1, 0, 0));
		gbLayout.setConstraints(ss,
				generateConstraints(GridBagConstraints.BOTH, 1, 4, 1, 1, 0, 0));
		gbLayout.setConstraints(tOrf2,
				generateConstraints(GridBagConstraints.BOTH, 2, 4, 1, 1, 0, 0));

		gbLayout.setConstraints(id1,
				generateConstraints(GridBagConstraints.BOTH, 0, 5, 1, 1, 0, 0));
		gbLayout.setConstraints(id,
				generateConstraints(GridBagConstraints.BOTH, 1, 5, 1, 1, 0, 0));
		gbLayout.setConstraints(macName1,
				generateConstraints(GridBagConstraints.BOTH, 0, 6, 1, 1, 0, 0));
		gbLayout.setConstraints(
				macName,
				generateConstraints(GridBagConstraints.BOTH, 1, 6, 1, 1, 200, 0));
		gbLayout.setConstraints(
				bt,generateConstraints(GridBagConstraints.CENTER, 0, 7, 2, 1, 80,0));
		
		this.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {

				isDraging = true;

				xx = e.getX();

				yy = e.getY();
			}

			public void mouseReleased(MouseEvent e) {

				isDraging = false;
			}
		});
		this.addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent e) {

				if (isDraging) {

					int left = getLocation().x;

					int top = getLocation().y;

					setLocation(left + e.getX() - xx, top + e.getY() - yy);

				}
			}
		});
	}

	private GridBagConstraints generateConstraints(
			int fill, //
			int gridx, int gridy, int gridwidth, int gridheight, int ipadx,
			int ipady) {
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		// BOTH：使组件完全填满其显示区域。
		gridBagConstraints.fill = fill;

		// 单元格索引
		gridBagConstraints.gridx = gridx;
		gridBagConstraints.gridy = gridy;
		// 占一行两列
		gridBagConstraints.gridwidth = gridwidth;
		gridBagConstraints.gridheight = gridheight;

		// 组件大小
		gridBagConstraints.ipadx = ipadx;
		gridBagConstraints.ipady = ipady;

		// 组件与所处格子边框的距离
		Insets in = new Insets(20, 10, 20, 10);
		gridBagConstraints.insets = in;

		return gridBagConstraints;

	}

	private JLabel getJLabel(String str) {
		if (!(str.equals("A5"))) {
			tOrf = new JLabel("异常");
			tOrf.setForeground(Color.red);
			tOrf.setFont(new java.awt.Font("宋体", 1, 18));
			return tOrf;
		} else {
			tOrf = new JLabel("正常");

			return tOrf;
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		this.dispose();
		
	}
}
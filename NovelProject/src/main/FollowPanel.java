package main;
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.*;
import common.*;
import dao.*;
import vo.*;
public class FollowPanel extends JPanel{
	JTextField tf;
	
	ControlPanel cp;
	public FollowPanel(ControlPanel cp) {
		this.cp = cp;
		tf = new JTextField();
		setLayout(null);
		tf.setBounds(100, 100, 100, 30);
		tf.setText("즐겨찾기게시판");
		add(tf);
	}
}

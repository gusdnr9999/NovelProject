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
public class BoardPanel extends JPanel {
	JTextField tf;
	
	ControlPanel cp;
	public BoardPanel(ControlPanel cp) {
		this.cp = cp;
		tf = new JTextField();
		setLayout(null);
		tf.setBounds(100, 100, 100, 30);
		tf.setText("커뮤니티게시판");
		add(tf);
	}
}

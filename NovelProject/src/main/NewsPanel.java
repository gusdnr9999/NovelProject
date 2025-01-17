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
public class NewsPanel extends JPanel {
	JTextField tf;
	
	ControlPanel cp;
	public NewsPanel(ControlPanel cp) {
		this.cp = cp;
		tf = new JTextField();
		setLayout(null);
		tf.setBounds(100, 100, 100, 30);
		tf.setText("뉴스게시판");
		add(tf);
	}
}

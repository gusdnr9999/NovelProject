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
public class NewsPanel extends JPanel 
implements ActionListener
{
	JTextField tf;
	JButton b;
	JTextArea ta;

	ControlPanel cp;
	public NewsPanel(ControlPanel cp) {
		this.cp = cp;
	   	setLayout(null);
	   	 
	   	b=new JButton("뉴스검색");
	   	tf=new JTextField();
	   	ta=new JTextArea();
	   	JScrollPane js=new JScrollPane(ta);
	   	tf.setBounds(20, 20, 650, 30);
	   	b.setBounds(680, 20, 120, 30);
	   	js.setBounds(20, 60, 780, 450);

	   	add(tf); add(b); 
	   	add(js);
	   	
	   	b.addActionListener(this);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
}

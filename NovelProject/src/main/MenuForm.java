package main;

import java.awt.*;
import javax.swing.*;

public class MenuForm extends JPanel {

	public JButton b1 = new JButton("홈");
	public JButton b2 = new JButton("소설");
	public JButton b3 = new JButton("소설 검색");
	public JButton b4 = new JButton("즐겨찾기");
	public JButton b5 = new JButton("커뮤니티");
	public JButton b6 = new JButton("실시간 채팅");
	public JButton b7 = new JButton("실시간 뉴스");

	// 초기화 ==> 배치
	public MenuForm() {
		setLayout(new GridLayout(1, 5, 10, 10));
		add(b1);
		add(b2);
		add(b3);
		add(b4);
		add(b5);
		add(b6);
		add(b7);
	}

}
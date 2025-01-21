package main;

import javax.swing.*;

public class Register extends JFrame {

  JLabel la1, la2, la3, la4;
  JTextField tf1, tf2;
  JPasswordField pf;
  JRadioButton rb1, rb2;
  JButton b1, b2;


  public Register() {
	la1 = new JLabel("ID");
	tf1 = new JTextField();

	la2 = new JLabel("PW");
	pf = new JPasswordField();

	la3 = new JLabel("Name");
	tf2 = new JTextField();

	la4 = new JLabel("Gender");
	rb1 = new JRadioButton("남자");
	rb2 = new JRadioButton("여자");
	ButtonGroup bg = new ButtonGroup();
	bg.add(rb1);
	bg.add(rb2);
	rb1.setSelected(true);

	b1 = new JButton("가입");
	b2 = new JButton("취소");

	// 배치 => 실행과 동시에 실행 명령 => 초기화 => 생성자
	setLayout(null);
	la1.setBounds(10, 15, 80, 30);
	tf1.setBounds(95, 15, 200, 30);
	add(la1);
	add(tf1);

	la2.setBounds(10, 55, 80, 30);
	pf.setBounds(95, 55, 200, 30);
	add(la2);
	add(pf);

	la3.setBounds(10, 95, 80, 30);
	tf2.setBounds(95, 95, 200, 30);
	add(la3);
	add(tf2);

	la4.setBounds(10, 135, 80, 30);
	rb1.setBounds(95, 135, 50, 30);
	rb2.setBounds(150, 135, 50, 30);
	add(la4);
	add(rb1);
	add(rb2);

	JPanel p = new JPanel();
	p.setOpaque(false);
	p.add(b1);
	p.add(b2);
	p.setBounds(10, 170, 310, 35);
	add(p);

	setBounds(400, 300, 330, 250);
	setVisible(false);// 화면 출력
	setDefaultCloseOperation(EXIT_ON_CLOSE);
  }
}
package main;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;

import common.ImageChange;
import dao.*;
import vo.*;
public class NovelDetailPanel extends JPanel implements ActionListener{
	JLabel poster;
	JLabel titleLa, authorLa, ispaLa, genreLa, avgstarLa, serialLa, storyLa;
	JLabel title, author, ispa, genre, avgstar, serial;
	JTextPane storyTa;
	JButton b1, b2, b3;
	int mode = 0;
	int dno = 0;
	ControlPanel cp;
	String[] link = {"","HOME","NOVEL","FIND","FOLLOW","MREVIEW"};
	NovelDAO nDao = NovelDAO.newInstance();
	FollowDAO fDao = FollowDAO.newInstance();
	boolean followCheck = false;
	public NovelDetailPanel(ControlPanel cp) {
		this.cp = cp;
		setLayout(null);
		
		poster = new JLabel("");
		poster.setBounds(20, 20, 300, 500);
		add(poster);
		
		titleLa = new JLabel("제목");
		title = new JLabel();
		
		titleLa.setBounds(330, 20, 80, 30);
		title.setBounds(415, 20, 250, 30);
		add(titleLa); add(title);
		
		authorLa = new JLabel("작가");
		author = new JLabel();

		authorLa.setBounds(330, 55, 80, 30);
		author.setBounds(415, 55, 250, 30);
		add(authorLa); add(author);
		
		ispaLa = new JLabel("완결");
		ispa = new JLabel();
		
		ispaLa.setBounds(330, 90, 80, 30);
		ispa.setBounds(415, 90, 350, 30);
		add(ispaLa); add(ispa);
		
		genreLa = new JLabel("장르");
		genre = new JLabel();
		
		genreLa.setBounds(330, 125, 80, 30);
		genre.setBounds(415, 125, 250, 30);
		add(genreLa); add(genre);
		
		avgstarLa = new JLabel("평균별점");
		avgstar = new JLabel();
		
		avgstarLa.setBounds(330, 160, 80, 30);
		avgstar.setBounds(415, 160, 250, 30);
		add(avgstarLa); add(avgstar);
		
		serialLa = new JLabel("연재처");
		serial = new JLabel();
		
		serialLa.setBounds(330, 195, 80, 30);
		serial.setBounds(415, 195, 250, 30);
		add(serialLa); add(serial);
		
		storyLa = new JLabel("줄거리");
		storyTa = new JTextPane();
		
		storyLa.setBounds(330, 230, 80, 30);
		storyTa.setBounds(415, 230, 250, 90);
		add(storyLa); add(storyTa);
		storyTa.setEnabled(false);
		
		b2 = new JButton("즐겨찾기 해제");
		b1 = new JButton("즐겨찾기");
		b3 = new JButton("목록");
		
		JPanel p = new JPanel();
		p.add(b1);p.add(b2);p.add(b3);
		p.setBounds(330, 525, 430, 35);
		add(p);
		
		b3.addActionListener(this); // 목록으로
		b1.addActionListener(this); // 즐겨찾기 추가
		b2.addActionListener(this); // 즐겨찾기 해제
	}
	public void detailPrint(int mode, NovelVO vo){
		this.mode = mode;
		this.dno = vo.getNo();
		followPrint(vo.getNo());
		try {
			URL url = new URL(vo.getPoster());
			Image img = ImageChange.getImage(new ImageIcon(url), 350, 500);
			poster.setIcon(new ImageIcon(img));
			title.setText(vo.getTitle());
			author.setText(vo.getAuthor());
			ispa.setText(vo.getIscp());
			genre.setText(vo.getGenre());
			avgstar.setText(String.valueOf(vo.getAvgstar()));
			serial.setText(vo.getSerial());
			storyTa.setText(vo.getStory());
			
		} catch (Exception e) {}
	}
	public void followPrint(int no) {
		followCheck = fDao.FollowCheck(no);
		if(followCheck) {
			b1.setVisible(false);
			b2.setVisible(true);
		}else {
			b1.setVisible(true);
			b2.setVisible(false);
		}
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == b3) {
			cp.card.show(cp, link[mode]);
		}else if(e.getSource() == b1) { // 즐겨찾기 추가
			if(fDao.FollowInsert(dno)) { // 추가 성공
				JOptionPane.showMessageDialog(this, "즐겨찾기에 추가했습니다");
			}else { // 추가 실패
				JOptionPane.showMessageDialog(this, "즐겨찾기에 추가하지 못했습니다");
			}
			followPrint(dno);
		}else if(e.getSource() == b2) { // 즐겨찾기 해제
			if(fDao.FollowDelete(dno)) { // 추가 성공
				JOptionPane.showMessageDialog(this, "즐겨찾기에서 삭제했습니다");
			}else { // 추가 실패
				JOptionPane.showMessageDialog(this, "즐겨찾기에서 삭제하지 못했습니다");
			}
			
			followPrint(dno);
		}
	}
}

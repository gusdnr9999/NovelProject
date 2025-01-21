package main;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import common.ImageChange;
import dao.FollowDAO;
import dao.NovelDAO;
import dao.ReviewDAO;
import vo.NovelVO;
import vo.ReviewVO;
public class NovelDetailPanel extends JPanel implements ActionListener,MouseListener{
	JLabel poster;
	JLabel titleLa, authorLa, ispaLa, genreLa, avgstarLa, serialLa, storyLa;
	JLabel title, author, ispa, genre, avgstar, serial;
	JTextPane storyTa;
	JScrollPane reviewPa, review;
	JPanel rePanel1, rePanel2, rePanel3;
	JButton b1, b2, b3;
	JPanel myReview;
	JLabel rStar, rContent, rInsert;
	JPanel rInfo;
	JLabel regLa;
	JButton[] bUp = new JButton[24];
	JButton[] bDown = new JButton[24];
	int[] noUp = new int[24];
	int[] noDown = new int[24];
	int mode = 0;
	int dno = 0;
	ControlPanel cp;
	String[] link = {"","HOME","NOVEL","FIND","FOLLOW","MREVIEW"};
	NovelDAO nDao = NovelDAO.newInstance();
	NovelVO nVo = new NovelVO();
	ReviewDAO rDao = ReviewDAO.newInstance();
	FollowDAO fDao = FollowDAO.newInstance();
	boolean followCheck = false;
	private NovelVO vo;
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
		
		ispaLa = new JLabel("완결");
		ispa = new JLabel();
		
		ispaLa.setBounds(670, 20, 80, 30);
		ispa.setBounds(755, 20, 350, 30);
		add(ispaLa); add(ispa);

		authorLa = new JLabel("작가");
		author = new JLabel();

		authorLa.setBounds(330, 55, 80, 30);
		author.setBounds(415, 55, 250, 30);
		add(authorLa); add(author);
		
		avgstarLa = new JLabel("평균별점");
		avgstar = new JLabel();
		
		avgstarLa.setBounds(670, 55, 80, 30);
		avgstar.setBounds(755, 55, 250, 30);

		genreLa = new JLabel("장르");
		genre = new JLabel();
		
		genreLa.setBounds(330, 90, 80, 30);
		genre.setBounds(415, 90, 250, 30);
		add(genreLa); add(genre);
		
		add(avgstarLa); add(avgstar);
		
		serialLa = new JLabel("연재처");
		serial = new JLabel();
		
		serialLa.setBounds(330, 125, 80, 30);
		serial.setBounds(415, 125, 250, 30);
		add(serialLa); add(serial);
		
		storyLa = new JLabel("줄거리");
		storyTa = new JTextPane();
		
		storyLa.setBounds(330, 160, 80, 30);
		storyTa.setBounds(415, 160, 250, 90);
		add(storyLa); add(storyTa);
		storyTa.setEnabled(false);

		myReview = new JPanel();
		myReview.setLayout(new BorderLayout());
		myReview.setBounds(330, 255, 350, 90);
		myReview.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1)); // 테두리 추가
		myReview.setBackground(Color.WHITE);
		
		reviewPa = new JScrollPane();
		reviewPa.setBounds(330, 350, 350, 170);
		reviewPa.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1)); // 테두리 추가
		reviewPa.setBackground(Color.WHITE);
		
		reviewPirnt(dno);
		add(reviewPa);
		reviewPa.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		setVisible(true);

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
		this.nVo = vo;
		reviewPirnt(dno);
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
		myReview.removeAll();
		
		if(vo.getRVo().getRno() != 0) {
			String star = "";
			int star1 = (int)(vo.getRVo().getStar()/1);
			double star2 = vo.getRVo().getStar() - star1;
			for(int i = 1; i <= star1; i++) {
				star+="★";
			}
			if(star2 == 0.5) {
				star+="☆";
			}
			rStar = new JLabel("별점 : " + star);
			rContent = new JLabel("<html><div style='width:240px;'>" + vo.getRVo().getContent() + "</div></html>");
			myReview.add(rStar, BorderLayout.NORTH);
			myReview.add(rContent, BorderLayout.CENTER);
			add(myReview);
		}else {
			rInsert = new JLabel("<html>등록된 리뷰가 없습니다<br>리뷰를 등록하시겠습니까?</html>");
			myReview.add(rInsert, BorderLayout.CENTER);
			add(myReview);
			rInsert.addMouseListener(this);
		}
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
	public void reviewPirnt(int no) {
		List<ReviewVO> list = rDao.reviewListData(1, no);
		
		JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS)); // 세로 정렬
        contentPanel.setBackground(Color.WHITE);
		/*
		
		for (ReviewVO vo : list) {
		    JPanel box = new JPanel();
		    box.setLayout(new BorderLayout());
		    box.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1)); // 박스 테두리
		    box.setBackground(Color.WHITE);
		
		    // 제목 라벨
		    JLabel rTitle = new JLabel(vo.getNickname());
		    rTitle.setFont(new Font("맑은 고딕", Font.BOLD, 14));
		    rTitle.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0)); // 여백
		
		    // 내용 라벨
		    JLabel rContent = new JLabel("<html><div style='width:240px;'>" + vo.getContent() + "</div></html>"); // 여러 줄 지원
		    rContent.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
		    rContent.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0)); // 여백
		
		    regLa = new JLabel(vo.getRegdate().toString());
		    String up = "공감" + vo.getUp();
		    String down = "비공감" + vo.getDown();
		    bup = new JButton(up);
		    bdown = new JButton(down);
		    
		    
		    //정보 라벨
		    rInfo = new JPanel();
		    rInfo.add(regLa);rInfo.add(bup);rInfo.add(bdown);
		    
		    bup.addActionListener(this);
		    bdown.addActionListener(this);
		    
		    // 레이아웃에 추가
		    box.add(rTitle, BorderLayout.NORTH);
		    box.add(rContent, BorderLayout.CENTER);
		    box.add(rInfo, BorderLayout.SOUTH);
		
		    // 크기 설정
		    box.setMaximumSize(new Dimension(300, Integer.MAX_VALUE)); // 고정 크기
		    contentPanel.add(box);
		}*/
        for (int i = 0; i < list.size(); i++) {
        	JPanel box = new JPanel();
        	box.setLayout(new BorderLayout());
        	box.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1)); // 박스 테두리
        	box.setBackground(Color.WHITE);
        	
        	// 제목 라벨
        	JLabel rTitle = new JLabel(list.get(i).getNickname());
        	rTitle.setFont(new Font("맑은 고딕", Font.BOLD, 14));
        	rTitle.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0)); // 여백
        	
        	// 내용 라벨
        	JLabel rContent = new JLabel("<html><div style='width:240px;'>" + list.get(i).getContent() + "</div></html>"); // 여러 줄 지원
        	rContent.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
        	rContent.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0)); // 여백
        	regLa = new JLabel(list.get(i).getRegdate().toString());
        	
        	noUp[i] = list.get(i).getUp();
        	noDown[i] = list.get(i).getDown();
        	String up = "공감" + noUp[i];
        	String down = "비공감" + noDown[i];
        	
        	bUp[i] = new JButton(up);
        	bDown[i] = new JButton(down);
        	
        	
        	//정보 라벨
        	rInfo = new JPanel();
        	rInfo.add(regLa);rInfo.add(bUp[i]);rInfo.add(bDown[i]);
        	
        	bUp[i].addActionListener(this);
        	bDown[i].addActionListener(this);
        	
        	// 레이아웃에 추가
        	box.add(rTitle, BorderLayout.NORTH);
        	box.add(rContent, BorderLayout.CENTER);
        	box.add(rInfo, BorderLayout.SOUTH);
        	
        	// 크기 설정
        	box.setMaximumSize(new Dimension(300, Integer.MAX_VALUE)); // 고정 크기
        	contentPanel.add(box);
        }

        reviewPa.setViewportView(contentPanel);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		for(int i = 0; i < 24; i++) {
			if(e.getSource() == bUp[i]) {
				if(bUp[i].isEnabled()) { // 공감 / 비공감 선택 안한상태 => 공감
					
				}else { // 비공감 => 공감
					
				}
				bUp[i].setEnabled(false);
				bDown[i].setEnabled(true);
				reviewPirnt(dno);
			}else if(e.getSource() == bDown[i]) {
				if(bDown[i].isEnabled()) { // 공감 / 비공감 선택 안한상태 => 비공감
					
				}else { // 공감 => 비공감
					
				}
				bUp[i].setEnabled(true);
				bDown[i].setEnabled(false);
				reviewPirnt(dno);
			}
		}
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
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
	}
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}

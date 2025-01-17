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
public class NovelPanel extends JPanel implements ActionListener{
	ControlPanel cp;
	JButton[] genre = new JButton[13]; // 한식, 중식, 양식, 일식, 기타
	JButton prev,next; //이전, 다음
	JLabel la = new JLabel("0 page / 0 pages");
	JLabel[] imgs = new JLabel[12];
	
	int curpage = 1;
	int totalpage = 0;
	JPanel pan = new JPanel(); // 이미지
	//데이터베이스 연동
	NovelDAO dao = NovelDAO.newInstance();
	String strGenre = "전체";
	public NovelPanel(ControlPanel cp) {
		this.cp = cp;

		prev = new JButton("이전");
		next = new JButton("다음");
		
		JPanel p1 = new JPanel();
		p1.setLayout(new GridLayout(2,7,5,5));
		String[] temp = {"전체", "로맨스", "로판", "판타지", "현판", "무협", "미스터리", "대체역사", "라이트노벨", "게임", "스포츠", "SF", "BL"};
		for(int i = 0 ; i < genre.length; i++) {
			genre[i] = new JButton(temp[i]);
			p1.add(genre[i]);
			
			genre[i].addActionListener(this);
		}
		
		JPanel p2 = new JPanel();
		p2.add(prev); p2.add(la); p2.add(next);// 코딩 순서로 출력
		
		pan.setLayout(new GridLayout(3,4,5,5));
		
		setLayout(new BorderLayout());
		add("North", p1);
		add("Center", pan);
		add("South", p2);
		print();
		
		prev.addActionListener(this);
		next.addActionListener(this);
		
	}
	// 초기화
		public void init() {
			for(int i = 0; i < imgs.length; i++) {
				imgs[i] = new JLabel("");
			}
			pan.removeAll(); // 전체 삭제
			pan.validate(); // 재배치
		}
		// 이미지 출력
		public void print() {
			// 총페이지
			totalpage = dao.novelGenreTotalPage(strGenre);
			List<NovelVO> list = dao.novelGenreListData(curpage, strGenre);
			for(int i = 0; i < list.size(); i++) {
				NovelVO vo = list.get(i);
				try {
					URL url = new URL(vo.getPoster());
					Image image = ImageChange.getImage(new ImageIcon(url), 200, 180);
					imgs[i] = new JLabel(new ImageIcon(image));
					imgs[i].setToolTipText(vo.getTitle() + "^" + vo.getNo());
					pan.add(imgs[i]);
					// 이벤트 등록
//					imgs[i].addMouseListener(this);
				} catch (Exception e) {}
			}
			la.setText(curpage + " page / " + totalpage + " pages");
		}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

}

package main;//   McWinLookAndFeel

import common.*;
import dao.*;
import vo.*;

import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;

//                         상속 => 재사용 => 변경
/*
 *   사용자 동작 => 로그인 , 채팅 보내기
 *   메소드 => 무조건 종료 => 다른 메소드가 호출
 *   -----------------------------------
 *   서버에서 보내는 값 : 자동화 처리
 *                   ----------- 쓰레드
 *
 */
public class ClientMainFrame extends JFrame implements ActionListener, Runnable, MouseListener {

	/// 네트워크 통신
	Socket s;
	OutputStream out;
	BufferedReader in;
	// 로그인이 되면 서버에 등록
	MenuForm mf = new MenuForm(); // 포함 클래스 => 있는 그대로 사용
	ControlPanel cp = new ControlPanel();
	Login login = new Login();
	// 배치
	// 데이터베이스
	int selectRow=-1; // 테이블에서 선택이 안된 상태
	MemberDAO mDao = MemberDAO.newInstance();

	public ClientMainFrame() {
		setLayout(null); // 사용자 정의 => 직접 배치
		mf.setBounds(10, 15, 800, 50);
		add(mf);
		cp.setBounds(10, 75, 820, 570);
		add(cp);
		setSize(850, 700);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		// 등록
		// 로그인
		login.b1.addActionListener(this);
		login.b2.addActionListener(this);

		mf.b1.addActionListener(this);// 홈
		mf.b2.addActionListener(this);// 소설
		mf.b3.addActionListener(this);// 소설 검색
		mf.b4.addActionListener(this);// 즐겨찾기
		mf.b5.addActionListener(this);// 커뮤니티
		mf.b6.addActionListener(this);// 실시간 채팅
		mf.b7.addActionListener(this);// 실시간 뉴스

		// Chat => Socket
		cp.cp.tf.addActionListener(this);
		cp.cp.table.addMouseListener(this);
		cp.cp.b2.addActionListener(this); // 채팅창 정보보기
		cp.cp.b1.addActionListener(this); // 쪽지보내기
		
		addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				try {
					out.write((Function.EXIT + "|\n").getBytes());
				} catch (Exception ex) {
				}
			}

		});
	}

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel("com.jtattoo.plaf.mcwin.McWinLookAndFeel");
		} catch (Exception ex) {
		}
		new ClientMainFrame();
	}

	// 서버에서 응답 받기 => 쓰레드
	@Override
	public void run() {
		try {
			while (true) {
				String msg = in.readLine();
				// 서버에서 보낸값을 받는다
				StringTokenizer st = new StringTokenizer(msg, "|");
				int protocol = Integer.parseInt(st.nextToken());
				switch (protocol) {
				case Function.LOGIN: {
					String[] data = { st.nextToken(), st.nextToken(), st.nextToken(), };
					cp.cp.model.addRow(data);
				}
					break;
				case Function.MYLOG: {
					String id = st.nextToken();
					setTitle(id);
					login.setVisible(false);
					setVisible(true);
				}
					break;
				case Function.WAITCHAT: {
					cp.cp.ta.append(st.nextToken() + "\n");
				}
					break;
				case Function.MYEXIT: {
					dispose();
					System.exit(0);
				}
					break;
				// 남아 있는 사람 처리
				case Function.EXIT: {
					String yid = st.nextToken();
					for (int i = 0; i < cp.cp.model.getRowCount(); i++) {
						String id = cp.cp.model.getValueAt(i, 0).toString();
						if (yid.equals(id)) {
							cp.cp.model.removeRow(i);
							break;
						}
					}
				}
					break;
				}
			}
		} catch (Exception ex) {
		}
	}

	// 서버에 요청 => 로그인 / 채팅 보내기
	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println("뭐지왜"+selectRow);
		if (e.getSource() == login.b2) {
			dispose();// 윈도우 메모리 해제
			System.exit(0); // 프로그램 종료
		} else if (e.getSource() == login.b1) {
			// 유효성 검사
			String id = login.tf.getText();
			if (id.trim().isEmpty()) {
				JOptionPane.showMessageDialog(this, "아이디를 입력하세요");
				login.tf.requestFocus();
				return;
			}
			String pwd = String.valueOf(login.pf.getPassword());
			if (pwd.trim().isEmpty()) {
				JOptionPane.showMessageDialog(this, "비밀번호를 입력하세요");
				login.pf.requestFocus();
				return;
			}
			// 로그인 검색
			MemberVO vo = mDao.isLogin(id, pwd);
			if (vo.getMsg().equals("NO ID")) {
				JOptionPane.showMessageDialog(this, "아이디가 존재하지 않습니다");
				login.tf.setText("");
				login.pf.setText("");
				login.tf.requestFocus();
			} else if (vo.getMsg().equals("NO PWD")) {
				JOptionPane.showMessageDialog(this, "비밀번호가 틀립니다");
				login.pf.setText("");
				login.pf.requestFocus();
			} else {
				// 서버연결
				connection(vo);
			}
		}else if(e.getSource()==cp.cp.b2) { // 정보보기임
			if(selectRow==-1) { // 선택이 안된 상태
				JOptionPane.showMessageDialog(this, "정보를 확인할 대상을 선택하세요");
				return;
				
			}
			String id =cp.cp.model.getValueAt(selectRow, 0).toString(); // id읽어오는거
			
			MemberVO vo =mDao.memberInfo(id);
			
			
 		  //SELECT nickname,gender,email,address1,phone,birth" 
			String info="이름 : "+vo.getNickname()+"\n"
					+ "성별 : "+vo.getGender()+"\n"
					+ "이메일 : "+vo.getEmail()+"\n"
					+ "주소 : "+vo.getAddress1()+"\n"
					+ "핸드폰 : "+vo.getPhone()+"\n"
					+ "생일 : "+vo.getBirth().toString();
			JOptionPane.showMessageDialog(this, info);
		} 
		// chat처리
		else if (e.getSource() == cp.cp.tf) {
			String msg = cp.cp.tf.getText();
			if (msg.trim().length() < 1) {
				cp.cp.tf.requestFocus();
				return;
			}

			try {
				out.write((Function.WAITCHAT + "|" + msg + "\n").getBytes());
			} catch (Exception ex) {
			}

			cp.cp.tf.setText("");
		} else if (e.getSource() == mf.b6) {
			cp.card.show(cp, "CHAT");
		} else if (e.getSource() == mf.b1) {
			cp.card.show(cp, "HOME");
		} else if (e.getSource() == mf.b2) {
			cp.card.show(cp, "NOVEL");
		} else if (e.getSource() == mf.b3) {
			cp.card.show(cp, "FIND");
		} else if (e.getSource() == mf.b4) {
			cp.card.show(cp, "FOLLOW");
		} else if (e.getSource() == mf.b5) {
			cp.card.show(cp, "BOARD");
		} else if (e.getSource() == mf.b7) {
			cp.card.show(cp, "NEWS");
		}
	}

	public void connection(MemberVO vo) {
		try {
			s = new Socket("localhost", 4321);
			// 서버 연결 => s는 서버
			// 서버로 전송
			out = s.getOutputStream();
			// 서버에서 값 받기
			in = new BufferedReader(new InputStreamReader(s.getInputStream()));

			// 서버로 로그인 요청
			out.write(
					(Function.LOGIN + "|" + vo.getId() + "|" + vo.getName() + "|" + vo.getGender() + "\n").getBytes());
			// => 반드시 => \n을 전송해야 된다
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
		// 서버로부터 값을 받아서 출력
		new Thread(this).start(); // run()메소드 호출
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() == cp.cp.table) {
			selectRow = cp.cp.table.getSelectedRow();
			String myId = getTitle();
			String id = cp.cp.model.getValueAt(selectRow, 0).toString();
			if (myId.equals(id)) {
				cp.cp.b1.setEnabled(false);
				cp.cp.b2.setEnabled(false);
			} else {
				cp.cp.b1.setEnabled(true);
				cp.cp.b2.setEnabled(true);
			}
		}
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
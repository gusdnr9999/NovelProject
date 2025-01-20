package main;

import java.awt.*;
import javax.swing.*;

// 화면 변경
public class ControlPanel extends JPanel {

<<<<<<< HEAD
  HomePanel hp;
  ChatPanel cp;
  NovelPanel np;
  NovelFindPanel nfp;
  NewsPanel nep;
  FollowPanel fp;
  BoardMainPanel bmp;
=======
  HomePanel hp; // 메인홈
  ChatPanel cp; // 실시간 채팅
  NovelPanel np; // 소설장르
  NovelFindPanel nfp; // 소설검색
  BoardPanel bp; // 게시판
  NewsPanel nep; // 뉴스
  FollowPanel fp; // 즐겨찾기
  NovelDetailPanel ndp;
>>>>>>> branch 'main' of https://github.com/gusdnr9999/NovelProject.git
  CardLayout card = new CardLayout();

  public ControlPanel() {
	setLayout(card);
	hp = new HomePanel(this);
	add("HOME", hp);
	cp = new ChatPanel(this);
	add("CHAT", cp);
	np = new NovelPanel(this);
	add("NOVEL", np);
	nfp = new NovelFindPanel(this);
	add("FIND", nfp);
	
	bmp = new BoardMainPanel(this);
	add("BOARD", bmp);

	nep = new NewsPanel(this);
	add("NEWS", nep);
	fp = new FollowPanel(this);
	add("FOLLOW", fp);
	ndp = new NovelDetailPanel(this);
	add("DETAIL", ndp);
  }

}
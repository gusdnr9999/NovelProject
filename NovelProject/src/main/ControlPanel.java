package main;

import java.awt.*;
import javax.swing.*;

// 화면 변경
public class ControlPanel extends JPanel {

  HomePanel hp; // 메인홈
  ChatPanel cp; // 실시간 채팅
  NovelPanel np; // 소설장르
  NovelFindPanel nfp; // 소설검색
  BoardPanel bp; // 게시판
  NewsPanel nep; // 뉴스
  FollowPanel fp; // 즐겨찾기
  NovelDetailPanel ndp;
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
	bp = new BoardPanel(this);
	add("BOARD", bp);
	nep = new NewsPanel(this);
	add("NEWS", nep);
	fp = new FollowPanel(this);
	add("FOLLOW", fp);
	ndp = new NovelDetailPanel(this);
	add("DETAIL", ndp);
  }

}
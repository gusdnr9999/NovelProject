package main;

import java.awt.*;
import javax.swing.*;

// 화면 변경
public class ControlPanel extends JPanel {

  HomePanel hp;
  ChatPanel cp;
  NovelPanel np;
  NovelFindPanel nfp;
  BoardPanel bp;
  NewsPanel nep;
  FollowPanel fp;
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
  }

}
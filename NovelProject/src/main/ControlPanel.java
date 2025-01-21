package main;

import java.awt.*;
import javax.swing.*;

// 화면 변경
public class ControlPanel extends JPanel {

  HomePanel hp;
  ChatPanel cp;
  NovelPanel np;
  NovelFindPanel nfp;
  NewsPanel nep;
  FollowPanel fp;
  BoardList bl;
  BoardInsert bInsert;
  BoardDetail bDetail;
  BoardUpdate bUpdate;
  BoardDelete bDelete;
  BoardReply bReply;
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
	
	bl = new BoardList(this);
	add("BOARD", bl);
	bInsert=new BoardInsert(this);
	add("BINSERT" ,bInsert);
	bDetail = new BoardDetail(this);
	add("BDETAIL" ,bDetail);
	bUpdate = new BoardUpdate(this);
	add("BUPDATE" ,bUpdate);
	bDelete=new BoardDelete(this);
	add("BDELETE",bDelete);
	bReply = new BoardReply(this);
	add("BREPLY" , bReply);

	nep = new NewsPanel(this);
	add("NEWS", nep);
	fp = new FollowPanel(this);
	add("FOLLOW", fp);
	ndp = new NovelDetailPanel(this);
	add("DETAIL", ndp);
  }

}
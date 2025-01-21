package main;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.*;
import javax.swing.table.*;

import dao.BoardDAO;
import vo.BoardVO;
public class BoardList extends JPanel
implements ActionListener,MouseListener
{
	JButton inBtn,prevBtn,nextBtn;
    JLabel pageLa,titleLa;
    JTable table;
    DefaultTableModel model;
    TableColumn column;
    ControlPanel cp;
    // 데이터베이스 연동
    BoardDAO dao=BoardDAO.newInstance();
    int curpage = 1;  // 현재 페이지 (보여주는 페이지)
    int totalpage = 0; // 총페이지
    
       public BoardList(ControlPanel cp)
    {
    	   this.cp = cp;
    	inBtn=new JButton("새글");//<input type=button value="새글">
    	prevBtn=new JButton("이전");
    	nextBtn=new JButton("다음");
    	pageLa=new JLabel("0 page / 0 pages"); //<label>0 page / 0 pages</label>
    	titleLa=new JLabel("게시판",JLabel.CENTER);// <table>
    	titleLa.setFont(new Font("맑은 고딕",Font.BOLD,30)); //<h3></h3>
    	
    	String[] col={"번호","제목","이름","작성일","조회수"};//<tr><th></th>....</tr>
    	String[][] row=new String[0][5];
    	// 한줄에 5개 데이터를 첨부 
    	model=new DefaultTableModel(row,col) // 데이터 관리
    	{
			@Override
			public boolean isCellEditable(int row, int column) {
				// TODO Auto-generated method stub
				return false;
			}
    		 // 익명의 클래스 => 포함 클래스 => 상속없이 오버라이딩 => 클릭 => 편집기 => 편집방지 
    		 
    	};
    	table=new JTable(model); // 테이블 모양 관리 
    	JScrollPane js=new JScrollPane(table);
    	for(int i=0;i<col.length;i++)
    	{
    		column=table.getColumnModel().getColumn(i);
    		if(i==0)
    		{
    			column.setPreferredWidth(50);
    		}
    		else if(i==1)
    		{
    			column.setPreferredWidth(350);
    		}
    		else if(i==2)
    		{
    			column.setPreferredWidth(100);
    		}
    		else if(i==3)
    		{
    			column.setPreferredWidth(150);
    		}
    		else if(i==4)
    		{
    			column.setPreferredWidth(50);
    		}
    	}
    	table.getTableHeader().setReorderingAllowed(false);
    	table.setShowVerticalLines(false);
    	table.setRowHeight(30);
    	table.getTableHeader().setBackground(Color.pink);
    	
    	// 배치 
    	setLayout(null);
    	titleLa.setBounds(10, 15, 830, 50);
    	add(titleLa);
    	inBtn.setBounds(100, 70, 100, 30);
    	add(inBtn);
    	js.setBounds(100, 110, 600, 330);
    	add(js);
    	
    	JPanel p=new JPanel();
    	p.add(prevBtn);
    	p.add(pageLa);
    	p.add(nextBtn);
    	
    	p.setBounds(100, 450, 600, 35);
    	add(p);
    	
    	print();
    	
    	inBtn.addActionListener(this); // 새글
    	prevBtn.addActionListener(this); // 이전
    	nextBtn.addActionListener(this); // 다음 
    	
    	table.addMouseListener(this);

    }
    public void print() {
    for(int i=model.getRowCount()-1;i>=0;i--)
   	{
   		model.removeRow(i);
   	}
   	// 데이터 받기 
   	List<BoardVO> list=dao.boardListData(curpage);
   	totalpage=dao.boardTotalPage();
   	
   	// 출력 => 테이블 
   	for(BoardVO vo:list)
   	{
   		if(vo.getGroup_tab()>0) // 답변인 경우
   		{
   			String s="";
   			for(int i=0;i<vo.getGroup_tab();i++)
   			{
   				s+="&nbsp;&nbsp;";
   				// &nbsp; => " "
   			}
   			String subject="<html><body>"+s+"<font color=red>☞</font>"+vo.getSubject()+"</body></html>";
   			String[] data= {
   				String.valueOf(vo.getNo()),
   				subject,
   				vo.getName(),
   				vo.getDbday(),
   				String.valueOf(vo.getHit())
   			};
   			model.addRow(data);
   		}
   		else // 답변이 아닌 경우 => 새글 
   		{
   			String[] data= {
       				String.valueOf(vo.getNo()),
       				vo.getSubject(),
       				vo.getName(),
       				vo.getDbday(),
       				String.valueOf(vo.getHit())
       			};
   			model.addRow(data);
   		}
   	}
   	pageLa.setText(curpage+" page / "+totalpage+" pages");
   }
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource()==table) {
			if(e.getClickCount()==2) {
				int row=table.getSelectedRow();
				// 클릭 위치 => 게시물 번호 읽기
				String no=model.getValueAt(row, 0).toString();
				
				//ReplyBoardVO vo=dao.boardDetailData(Integer.parseInt(no));
				
				// 윈도우 / 웹 => 정수(X) , 문자열 => 문자열=>정수형으로 변경 
				cp.card.show(cp,"BDETAIL");
				// Database연동 
				cp.bDetail.print(1,Integer.parseInt(no));
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
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
}
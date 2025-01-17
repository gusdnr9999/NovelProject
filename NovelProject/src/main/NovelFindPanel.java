package main;
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import common.*;
import dao.*;
import vo.*;
public class NovelFindPanel extends JPanel implements ActionListener, MouseListener{
	ControlPanel cp; // 상세보기 
    JTable table; // 모양관리 
    DefaultTableModel model; // 데이터 관리
    // MVC구조 
    
    JButton prev,next; //이전, 다음
	JLabel la = new JLabel("0 page / 0 pages");
    int curpage = 1;
	int totalpage = 0;
    JTextField tf; 
    JButton b;// 검색 
    String title = "";
    TableColumn column;
    NovelDAO dao=NovelDAO.newInstance();
    public NovelFindPanel(ControlPanel cp)
    {
   	 this.cp=cp;
   	 
   	prev = new JButton("이전");
	next = new JButton("다음");
	
   	 String[] col={
   		"번호","","제목","장르","작가","평균평점"
   	 };
   	 Object[][] row=new Object[0][6];
   	 model=new DefaultTableModel(row,col)
   	 {
           // 클릭시 => 편집 방지 
			@Override
			public boolean isCellEditable(int row, int column) {
				// TODO Auto-generated method stub
				return false;
			}
           // 이미지 출력 <?> => 와일드카드 : 어떤 클래스든 사용이 가능
			/*
			 *   Class<Integer> => ?
			 *   Class<String>
			 *   Class<ImageIcon>
			 *   Class<Double>
			 */
			@Override
			public Class<?> getColumnClass(int columnIndex) {
				// TODO Auto-generated method stub
				return getValueAt(0, columnIndex).getClass();
			}
   		 // 오버라이딩 사용 => 라이브러리 변경 
			 // 상속없이 오버라이딩이 가능 => 익명의 클래스 (내부 클래스의 일종)
   		 
   	 };
   	 table=new JTable(model);
   	 table.getTableHeader().setReorderingAllowed(false);
   	 table.setRowHeight(40);
   	 JScrollPane js1=new JScrollPane(table);
   	 
   	 for(int i=0;i<col.length;i++)
    	 {
    		column=table.getColumnModel().getColumn(i);
    		if(i==0)
    			column.setPreferredWidth(80);
    		else if(i==1)
    			column.setPreferredWidth(130);
    		else if(i==2)
    			column.setPreferredWidth(200);
    		else if(i==3)
    			column.setPreferredWidth(250);
    		else if(i==4)
    			column.setPreferredWidth(140);
    		else if(i==5)
    			column.setPreferredWidth(80);
    	 }
   	 
   	 b=new JButton("검색");
   	 tf=new JTextField();
   	 
   	 setLayout(null);
   	 
   	 tf.setBounds(20, 20, 200, 30);
   	 b.setBounds(225, 20, 80, 30);
   	 
   	 js1.setBounds(20, 60, 780, 500);
   	 
   	 add(tf); add(b); 
   	 add(js1);
   	 
   	JPanel p2 = new JPanel();
   	la.setText(curpage + " page / " + totalpage + " pages");
	p2.add(prev); p2.add(la); p2.add(next);
   	 b.addActionListener(this);
   	 tf.addActionListener(this);
   	 
   	 table.addMouseListener(this);
   	 
	prev.addActionListener(this);
	next.addActionListener(this);
    }
    public void print(String title)
	{
		// 테이블 데이터 지우기 
		for(int i=model.getRowCount()-1;i>=0;i--)
		{
			model.removeRow(i);
		}
		
		// 총페이지
		totalpage = dao.novelFindTotalPage(title);
		// 데이터 받기 
		List<NovelVO> list=dao.novelFindData(curpage ,title);
		for(NovelVO vo:list)
		{
		  try
		  {
			URL url=new URL(vo.getPoster());
			Image image=ImageChange.getImage(
					new ImageIcon(url), 30, 30);
			Object[] data={
				vo.getNo(),
				new ImageIcon(image),
				vo.getTitle(),
				vo.getGenre(),
				vo.getAuthor(),
				vo.getAvgstar()
			};
			System.out.println(vo.getNo());
			System.out.println(vo.getTitle());
			model.addRow(data);
		  }catch(Exception ex){}
		}
	}
    @Override
    public void actionPerformed(ActionEvent e) {
    	// TODO Auto-generated method stub
    	if(e.getSource()==b || e.getSource()==tf)
		{
			// 검색어 읽기
			String title=tf.getText();
			if(title.trim().length()<1)
			{
				tf.requestFocus();
				return;
			}
			print(title);
		}else if(e.getSource() == prev) {
			if(curpage > 1) {
				curpage--;
				print(title);
			}
		}else if(e.getSource() == next) {
			if(curpage < totalpage) {
				curpage++;
				print(title);
			}
			
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

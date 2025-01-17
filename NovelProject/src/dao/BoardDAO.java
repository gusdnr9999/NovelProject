package dao;
import java.sql.*;
import java.util.*;
import vo.BoardVO;
public class BoardDAO {
		private Connection conn;
	   private PreparedStatement ps;
	   private final String URL="jdbc:oracle:thin:@211.238.142.124:1521:XE";

	   private static BoardDAO dao;
	   
	   public BoardDAO()
	   {
		   try
		   {
			   Class.forName("oracle.jdbc.driver.OracleDriver");
		   }catch(Exception ex) {}
	   }
	   public void getConnection()
	   {
		   try
		   {
			   conn=DriverManager.getConnection(URL,"hr_4","happy");
			   // conn hr/happy
		   }catch(Exception ex)
		   {
			   ex.printStackTrace();
		   }
	   }
	   public void disConnection()
	   {
		   try
		   {
			   if(ps!=null) ps.close();
			   if(conn!=null) conn.close();
		   }catch(Exception ex) {}
	   }
	   public static BoardDAO newInstance()
	   {
		   if(dao==null)
			   dao=new BoardDAO();
		   return dao;
	   }
	   public List<BoardVO> boardListData(int page)
	   {
		   List<BoardVO> list=
				   new ArrayList<BoardVO>();
		   try
		   { 
			   getConnection();
			   String sql="SELECT no,subject,name,regdate,hit,num "
					     +"FROM (SELECT no,subject,name,regdate,hit,rownum as num "
					     +"FROM (SELECT no,subject,name,regdate,hit "
					     +"FROM freeboard ORDER BY no DESC)) "
					     +"WHERE num BETWEEN ? AND ?";
			   
			   ps=conn.prepareStatement(sql); 
			   
			   int rowSize=10;
			   int start=(rowSize*page)-(rowSize-1);
			  
			   int end=rowSize*page;
			  
			   ps.setInt(1, start);
			   ps.setInt(2, end);
			   
			   ResultSet rs=ps.executeQuery();
			   while(rs.next())
			   {
				   BoardVO vo=new BoardVO();
				   vo.setNo(rs.getInt(1));
				   vo.setSubject(rs.getString(2));
				   vo.setName(rs.getString(3));
				   vo.setRegdate(rs.getDate(4));
				   vo.setHit(rs.getInt(5));
				   list.add(vo);
			   }
			   rs.close();
		   }catch(Exception ex)
		   {
			   ex.printStackTrace();
		   }
		   finally
		   {
			   disConnection();
		   }
		   return list;
	   } 
	   public int boardTotalPage()
	   {
		   int total=0;
		   try
		   {
			   getConnection();
			   String sql="SELECT CEIL(COUNT(*)/10.0) FROM freeboard";
			   // 총 페이지 구하기 ------------------------------------
			   ps=conn.prepareStatement(sql);
			   ResultSet rs=ps.executeQuery();
			   rs.next();
			   total=rs.getInt(1);
			   rs.close();
		   }catch(Exception ex)
		   {
			   ex.printStackTrace();
		   }
		   finally
		   {
			   disConnection();
		   }
		   return total;
	   }
	   
}

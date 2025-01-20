package dao;
import java.util.*;
import java.sql.*;
import vo.*;
public class BoardDAO {
   // 오라클 연결 객체 
   private Connection conn;
   // 오라클 송수신 담당 (송신(SQL문장)/수신(실행 결과값))
   private PreparedStatement ps;
   // 오라클 주소 => 고정 (상수) 
   private final String URL="jdbc:oracle:thin:@localhost:1521:XE";
   // 객체를 한번만 생성해서 재사용 (메모리 절약) 
   // Connection객체수를 조절 => 50명 
   // 싱글턴 
   private static BoardDAO dao;
   
   // => 1. 드라이버 등록 
   public BoardDAO()
   {
	   try
	   {
		   Class.forName("oracle.jdbc.driver.OracleDriver");
	   }catch(Exception ex) {}
   }
   // => 2. 오라클 연결 
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
   // 윈도우/명령프롬프트/웹/ react/vue ... 상관없이 
   // ------------------ 오라클연동이 안된다 <==> 자바
   // ------------------ 웹의 85%
   // => 3. 오라클 해제 
   public void disConnection()
   {
	   try
	   {
		   if(ps!=null) ps.close();
		   if(conn!=null) conn.close();
	   }catch(Exception ex) {}
   }
   // => 4. 싱글턴 
   // ================================= 필수 조건 
   /*
    *   Back-End : DB연동 , AI
    */
   public static BoardDAO newInstance()
   {
	   if(dao==null)
		   dao=new BoardDAO();
	   return dao;
   }
   
   // 기능 => 1. 목록 (페이징 기법) => 인라인뷰 사용 
   public List<BoardVO> boardListData(int page)
   {
	   List<BoardVO> list=
			   new ArrayList<BoardVO>();
	   try
	   {
		   // 1. 오라클 연결 
		   getConnection();
		   // 2. SQL문장 제작 
		   String sql="SELECT no,subject,name,regdate,hit,num "
				     +"FROM (SELECT no,subject,name,regdate,hit,rownum as num "
				     +"FROM (SELECT no,subject,name,regdate,hit "
				     +"FROM board ORDER BY no DESC)) "
				     +"WHERE num BETWEEN ? AND ?";
		   // 페이지 나누는 방법 => 인라인뷰 => rownum
		   // => rownum은 중간에 자를 수 없다 
		   ps=conn.prepareStatement(sql); 
		   // 실행전에 ?에 값을 채운다 
		   int rowSize=10;
		   int start=(rowSize*page)-(rowSize-1);
		   // 1page => 1 , 2page => 11 ....
		   // rownum은 1부터 시작한다 
		   int end=rowSize*page;
		   // 1page => 10 , 2page => 20 ...
		   ps.setInt(1, start); // 1
		   ps.setInt(2, end);// 10
		   
		   // 실행후에 결과값을 가지고 와라 
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
		   // 에러 처리 => 확인 
		   ex.printStackTrace();
	   }
	   finally
	   {
		   // 오라클 닫기 
		   disConnection();
	   }
	   return list;
   }
   // 1-1. 총페이지 구하기 
   public int boardTotalPage()
   {
	   int total=0;
	   try
	   {
		   getConnection();
		   String sql="SELECT CEIL(COUNT(*)/10.0) FROM board";
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
   // 2. 상세보기 => WHERE => Primary Key값을 넘겨준다 
   // 게시물 번호 => 자동 증가 
   public BoardVO boardDetailData(int no)
   {
	   BoardVO vo=new BoardVO();
	   try
	   {
		   // 한개의 기능 수행시 => SQL문장 여러개를 한번에 처리가 가능하다 
		   // 연결 
		   getConnection();
		   // 조회수 증가 
		   String sql="UPDATE board SET "
				     +"hit=hit+1 "
				     +"WHERE no="+no;
		   ps=conn.prepareStatement(sql);
		   // 실행 명령 
		   ps.executeUpdate();
		   /*
		    *   executeQuery() => 데이터 검색 (SELECT)
		    *   executeUpdate() => 데이터 변경 
		    *                      INSERT / UPDATE / DELETE
		    *   ---------------------------------------commit()
		    *   ---- 자바 AutoCommit()
		    */
		   // 데이터 읽기 
		   sql="SELECT no,name,subject,content,regdate,hit "
			  +"FROM board "
			  +"WHERE no="+no;
		   
		   ps=conn.prepareStatement(sql);
		   ResultSet rs=ps.executeQuery();
		   rs.next(); // 한번만 수행 
		   // 값을 채운다 
		   vo.setNo(rs.getInt(1));
		   vo.setName(rs.getString(2));
		   vo.setSubject(rs.getString(3));
		   vo.setContent(rs.getString(4));
		   vo.setRegdate(rs.getDate(5));
		   vo.setHit(rs.getInt(6));
		   
		   rs.close();
		   
	   }catch(Exception ex)
	   {
		   ex.printStackTrace();
	   }
	   finally
	   {
		   disConnection();
	   }
	   return vo;
   }
   // 3. 글쓰기 => INSERT 
   public void boardInsert(BoardVO vo)
   {
	   // 리턴형 / 매개변수 
	   try
	   {
		   getConnection();
		   // hit=0 , regdate=SYSDATE
		   // PRIMARY KEY => 자동 증가번호
		   // SELECT MAX()+1
		   // SEQUENCE fb_no_seq.nextval
		   String sql="INSERT INTO board(no,name,subject,content,pwd) "
				     +"VALUES(fb_no_seq.nextval,?,?,?,?)";
		   ps=conn.prepareStatement(sql);
		   ps.setString(1, vo.getName());//'홍길동' 
		   ps.setString(2, vo.getSubject());
		   ps.setString(3, vo.getContent());
		   ps.setString(4, vo.getPwd());
		   
		   // 실행 
		   ps.executeUpdate();
	   }catch(Exception ex)
	   {
		   ex.printStackTrace();
	   }
	   finally
	   {
		   disConnection();
	   }
   }
   // 4. 수정 => UPDATE => 비밀번호 검사  
   //  1) 수정 데이터 읽기 (이전)
   public BoardVO boardUpdateData(int no) {
	   // 한개의 게시물 읽기 / 맛집 / 상품 / 영화 => PRIMARY KEY
	   BoardVO vo=new BoardVO();
	   // vo 는 게시물 한개에 대한 모든 데이터를 가지고 있다 
	   // 한개 찾기 => VO => 중복없는 데이터 
	   // 여러개 찾기 (검색) => List<VO> => 검색어 => LIKE
	   try {
		   getConnection();
		   String sql="SELECT no,name,subject,content "
		   		+ "FROM board "
		   		+ "WHERE no="+no;
		   ps=conn.prepareStatement(sql);
		   ResultSet rs=ps.executeQuery();
		   rs.next();
		   vo.setNo(rs.getInt(1));
		   vo.setName(rs.getString(2));
		   vo.setSubject(rs.getString(3));
		   vo.setContent(rs.getString(4));
		   rs.close();
		   
		   
		   
	   }catch(Exception ex) {
		   ex.printStackTrace();
	   }
	   finally {
		   disConnection();
	   }
	   return vo;
   }
   // 2) 실제 수정 
   public boolean boardUpdate(BoardVO vo) {
	   boolean bCheck = false;
	   try {
		   // 1.연결
		   getConnection();
		   // 2. SQL => 비밀번호 읽기
		   String sql="SELECT pwd "
		   		+ "FROM board "
		   		+ "WHERE no = "+vo.getNo();
		   ps=conn.prepareStatement(sql);
		   ResultSet rs=ps.executeQuery();
		   rs.next();
		   String db_pwd=rs.getString(1);
		   rs.close();
		   
		   if(db_pwd.equals(vo.getPwd())) {
			   bCheck=true;
			   sql="UPDATE board SET "
			   		+ "name=?,subject=?,content=? "
			   		+ "WHERE no=?";
			   ps=conn.prepareStatement(sql);
			   ps.setString(1, vo.getName());
			   ps.setString(2, vo.getSubject());
			   ps.setString(3, vo.getContent());
			   ps.setInt(4, vo.getNo());
			   
			   // 실행
			   ps.executeUpdate();
		   }
	   }
	   catch(Exception ex) {
		   ex.printStackTrace();
	   }finally {
		   disConnection();
	   }
	   return bCheck;
   }
   // 5. 삭제 => DELETE => 비밀번호 검사 
   public boolean boardDelete(int no,String pwd) {
	   boolean bCheck=false;
	   try {
		   // 1. 연결
		   getConnection();
		   // 2. SQL문장 제작
		   String sql="SELECT pwd FROM board "
		   		+ "WHERE no="+no;
		   // SQL문장 전송
		   ps=conn.prepareStatement(sql);
		   // 
		   ResultSet rs=ps.executeQuery();
		   rs.next();
		   String db_pwd=rs.getString(1);
		   rs.close();
		   
		   if(db_pwd.equals(pwd)) {
			   bCheck=true;
			   sql="DELETE FROM board "
			   		+ "WHERE no="+no;
			   ps=conn.prepareStatement(sql);
			   ps.executeUpdate();
		   }
	   }
	   catch(Exception ex) {
		   ex.printStackTrace();
	   }finally {
		   disConnection();
	   }
	   return bCheck;
   }
   // 6. 찾기 => LIKE 문장 사용 
   public List<BoardVO> boardFindData(String col,String fd)
   {
	   // 이름 / 제목 / 내용 
	   List<BoardVO> list=
			   new ArrayList<BoardVO>();
	   try
	   {
		   getConnection();
		   String sql="SELECT no,subject,name,regdate,hit "
				     +"FROM board "
				     +"WHERE "+col+" LIKE '%'||?||'%'";
		   //                              '%홍길동%'
		   // ? => setString(1,'홍길동')
		   //      ---------  '홍길동'
		   ps=conn.prepareStatement(sql);
		   ps.setString(1, fd);
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
   // 6-1 
   public int boardFindCount(String col,String fd)
   {
	   int count=0;
	   try
	   {
		   getConnection();
		   String sql="SELECT COUNT(*) "
				     +"FROM board "
				     +"WHERE "+col+" LIKE '%'||?||'%'";
		   //                              '%홍길동%'
		   // ? => setString(1,'홍길동')
		   //      ---------  '홍길동'
		   ps=conn.prepareStatement(sql);
		   ps.setString(1, fd);
		   ResultSet rs=ps.executeQuery();
		   rs.next();
		   count=rs.getInt(1);
		   rs.close();
	   }catch(Exception ex)
	   {
		   ex.printStackTrace();
	   }
	   finally
	   {
		   disConnection();
	   }
	   return count;
   }
   // -------------------------------- CRUD 
   
}

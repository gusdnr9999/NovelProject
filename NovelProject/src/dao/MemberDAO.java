package dao;

import java.sql.*;

import vo.MemberVO;

public class MemberDAO {

  private Connection conn;
  private PreparedStatement ps;
  private static MemberDAO dao;
  private final String URL = "jdbc:oracle:thin:@211.238.142.124:1521:XE";
  static String id = "";

  private MemberDAO() {
	try {
	  Class.forName("oracle.jdbc.driver.OracleDriver");
	} catch (Exception ex) {
	  throw new RuntimeException(ex);
	}
  }

  public static MemberDAO newInstance() {
	if (dao == null) {
	  dao = new MemberDAO();
	}
	return dao;
  }

  public void getConnection() {
	try {
	  conn = DriverManager.getConnection(URL, "hr_4", "happy");
	} catch (Exception ex) {
	  throw new RuntimeException(ex);
	}
  }

  public void disConnection(Connection conn, PreparedStatement ps) {
	try {
	  if (ps != null) {
		ps.close();
	  }
	  if (conn != null) {
		conn.close();
	  }
	} catch (Exception ex) {
	  throw new RuntimeException(ex);
	}
  }

  public MemberVO isLogin(String id, String pwd) {
	MemberVO vo = new MemberVO();
	this.id = id;
	try {
	  getConnection();
	  String sql = "SELECT COUNT(*) "
		  + "FROM member "
		  + "WHERE id=?";
	  ps = conn.prepareStatement(sql);
	  ps.setString(1, id);

	  ResultSet rs = ps.executeQuery();
	  rs.next();
	  int cnt = rs.getInt(1);
	  System.out.println(cnt);
	  rs.close();

	  if (cnt == 0) {
		vo.setMsg("NO ID");
	  } else {
		sql = "SELECT id, nickname, pw, name, gender "
			+ "FROM member "
			+ "WHERE id=?";
		ps = conn.prepareStatement(sql);
		ps.setString(1, id);
		rs = ps.executeQuery();
		rs.next();
		vo.setId(rs.getString(1));
		vo.setNickname(rs.getString(2));
		vo.setName(rs.getString(4));
		vo.setGender(rs.getString(5));

		String db_pwd = rs.getString(3);
		if (db_pwd.equals(pwd)) {
		  vo.setMsg("OK");
		} else {
		  vo.setMsg("NO PWD");
		}
	  }
	} catch (Exception ex) {
	  throw new RuntimeException(ex);
	} finally {
	  disConnection(conn, ps);
	}
	return vo;
  }

  /*
      우리꺼 member
        private String id, nickname, pw, name, gender, email, address1, address2, phone, is_admin, last_login, msg;
  		private Date reg_date, birth;
   */
  public MemberVO memberInfo(String id) {
	MemberVO vo = new MemberVO();
	try {
	  getConnection();
	  //	String sql="SELECT nickname FROM member WHERE id=?"; 
	  String sql = "SELECT nickname,gender,email,address1,phone,birth "
		  + "FROM member "
		  + "WHERE id=?";
	  ps = conn.prepareStatement(sql);
	  ps.setString(1, id);
	  ResultSet rs = ps.executeQuery();
	  rs.next();
	  vo.setNickname(rs.getString(1));
	  vo.setGender(rs.getString(2));
	  vo.setEmail(rs.getString(3));
	  vo.setAddress1(rs.getString(4));
	  vo.setPhone(rs.getString(5));
	  vo.setBirth(rs.getDate(6));
	  rs.close();
	} catch (Exception e) {
	  e.printStackTrace();
	} finally {
	  disConnection(conn, ps);
	}
	return vo;
  }

  public void memberInsert(MemberVO vo) {
	try {
	  getConnection();
	  String sql = "INSERT INTO member(id,nickname,pw,name,gender,email,address1,phone,birth) "
		  + "VALUES(?,?,?,?,?,?,?,?,?)";
	  ps = conn.prepareStatement(sql);
	  ps.setString(1, vo.getId());
	  ps.setString(2, vo.getNickname());
	  ps.setString(3, vo.getPw());
	  ps.setString(4, vo.getName());
	  ps.setString(5, vo.getGender());
	  ps.setString(6, vo.getEmail());
	  ps.setString(7, vo.getAddress1());
	  ps.setString(8, vo.getPhone());
	  ps.setDate(9, (Date) vo.getBirth());
	  ps.executeUpdate();
	  ps.close();

	} catch (SQLException e) {
	  throw new RuntimeException(e);
	} finally {
	  disConnection(conn, ps);
	}
  }
}

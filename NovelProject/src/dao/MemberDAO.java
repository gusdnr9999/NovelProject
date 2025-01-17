package dao;

import java.sql.*;
import vo.MemberVO;

public class MemberDAO {

  private Connection conn;
  private PreparedStatement ps;
  private static MemberDAO dao;
  private final String URL = "jdbc:oracle:thin:@211.238.142.124:1521:XE";

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
	return new MemberDAO();
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
}

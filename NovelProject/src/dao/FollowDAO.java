package dao;

import java.sql.*;
import java.util.*;
import vo.*;

public class FollowDAO {
	private Connection conn;
	private PreparedStatement ps;
	DataBase db = new DataBase();
	private static FollowDAO fDao;
	private final int FOLLOWROW = 20;

	public static FollowDAO newInstance() {
		if (fDao == null)
			fDao = new FollowDAO();
		return fDao;
	}
	//내가 즐겨찾기한 소설
	public List<NovelVO> MyFollowList(int page){
		List<NovelVO> list = new ArrayList<NovelVO>();
		try {
			conn = db.getConnection();
			String sql = "SELECT no, title, poster, num "
					+ "FROM (SELECT no, title, poster, rownum as num "
					+ "FROM (SELECT /*+ INDEX_ASC(novel_no_pk)*/ novel.no, title, poster "
					+ "FROM novel, follow "
					+ "WHERE novel.no = follow.no "
					+ "AND id = ?)) "
					+ "WHERE num BETWEEN ? AND ?";
			ps = conn.prepareStatement(sql);
			int start = (FOLLOWROW * page) - (FOLLOWROW - 1);
			int end = FOLLOWROW * page;
			ps.setString(1, MemberDAO.id);
			ps.setInt(2, start);
			ps.setInt(3, end);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				NovelVO vo = new NovelVO();
				vo.setNo(rs.getInt(1));
				vo.setTitle(rs.getString(2));
				vo.setPoster(rs.getString(3));
//				list.add(vo);
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.disConnection(conn, ps);
		}
		return list;
	}
	//즐겨찾기 유무 검사
	public boolean FollowCheck(int no){
		boolean bCheck = false;
		try {
			conn = db.getConnection();
			String sql = "SELECT COUNT(*) FROM follow WHERE no = ? AND id = ?";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, no);
			ps.setString(2, MemberDAO.id);
			ResultSet rs = ps.executeQuery();
			rs.next();
			if(rs.getInt(1) == 1) {
				bCheck = true;
			}else {
				bCheck = false;
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.disConnection(conn, ps);
		}
		return bCheck;
	}
	//즐겨찾기 추가
	public boolean FollowInsert(int no) {
		boolean bCheck = false;
		try {
			conn = db.getConnection();
			String sql = "INSERT INTO follow "
					+ "VALUES(?,?)";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, no);
			ps.setString(2, MemberDAO.id);
			ps.executeUpdate();
			bCheck = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.disConnection(conn, ps);
		}
		return bCheck;
	}
	//즐겨찾기 해제
	public boolean FollowDelete(int no) {
		boolean bCheck = false;
		try {
			conn = db.getConnection();
			String sql = "DELETE FROM follow "
					+ "WHERE no = ? AND id = ?";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, no);
			ps.setString(2, MemberDAO.id);
			ps.executeUpdate();
			bCheck = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.disConnection(conn, ps);
		}
		return bCheck;
	}
}

package dao;

import java.sql.*;
import java.util.*;

import vo.ReviewVO;

public class ReviewDAO {

	private Connection conn;
	private PreparedStatement ps;
	DataBase db = new DataBase();
	private static ReviewDAO rDao;
	private final int REVIEWROW = 10;

	public static ReviewDAO newInstance() {
		if (rDao == null) {
			rDao = new ReviewDAO();
		}
		return rDao;
	}

	// 전체목록 출력
	public List<ReviewVO> reviewListData(int page, int no) {
		List<ReviewVO> list = new ArrayList<ReviewVO>();
		try {
			conn = db.getConnection();
			String sql = "SELECT rno,no,id,star,content,regdate,up,down,profile,nickname,num "
					+ "FROM (SELECT rno,no,id,star,content,regdate,up,down,profile,nickname,rownum as num "
					+ "FROM (SELECT rno,no,review.id,star,content,regdate,up,down,profile,nickname "
					+ "FROM review, member "
					+ "WHERE review.id = member.id AND no = ? "
					+ "ORDER BY up DESC)) "
					+ "WHERE num BETWEEN ? AND ?";
			ps = conn.prepareStatement(sql);
			int start = (REVIEWROW * page) - (REVIEWROW - 1);
			int end = (REVIEWROW * page);
			ps.setInt(1, no);
			ps.setInt(2, start);
			ps.setInt(3, end);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				ReviewVO vo = new ReviewVO();
				vo.setRno(rs.getInt(1));
				vo.setNo(rs.getInt(2));
				vo.setId(rs.getString(3));
				vo.setStar(rs.getDouble(4));
				vo.setContent(rs.getString(5));
				vo.setRegdate(rs.getDate(6));
				vo.setUp(rs.getInt(7));
				vo.setDown(rs.getInt(8));
				vo.setProfile(rs.getString(9));
				vo.setNickname(rs.getString(10));
				list.add(vo);
			}
			rs.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			db.disConnection(conn, ps);
		}
		return list;
	}

	public int reviewTotalPage() {
		int count = 0;
		try {
			conn = db.getConnection();
			String sql = "SELECT CEIL(COUNT(*)/?) FROM review";
			ps = conn.prepareStatement(sql);
			ps.setDouble(1, REVIEWROW);
			ResultSet rs = ps.executeQuery();
			rs.next();
			count = rs.getInt(1);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			db.disConnection(conn, ps);
		}

		return count;
	}

}
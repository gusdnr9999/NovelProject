package dao;

import java.sql.*;
import java.util.*;
import vo.*;

public class NovelDAO {

	private Connection conn;
	private PreparedStatement ps;
	DataBase db = new DataBase();
	private static NovelDAO nDao;
	private final int NOVELROW = 12;

	public static NovelDAO newInstance() {
		if (nDao == null)
			nDao = new NovelDAO();
		return nDao;
	}

	// 전체목록 출력
	public List<NovelVO> novelListData(int page) {
		List<NovelVO> list = new ArrayList<NovelVO>();
		try {
			conn = db.getConnection();
			String sql = "SELECT no,genre,title,poster,author,story,avgstar,serial,iscp,num "
					+ "FROM (SELECT no,genre,title,poster,author,story,avgstar,serial,iscp,rownum as num "
					+ "FROM (SELECT /*+ INDEX_ASC(novel novel_no_pk) */ no,genre,title,poster,author,story,avgstar,serial,iscp "
					+ "FROM novel))" + "WHERE num BETWEEN ? AND ?";
			ps = conn.prepareStatement(sql);
			int start = (NOVELROW * page) - (NOVELROW - 1);
			int end = (NOVELROW * page);
			ps.setInt(1, start);
			ps.setInt(2, end);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				NovelVO vo = new NovelVO();
				vo.setNo(rs.getInt(1));
				vo.setGenre(rs.getString(2));
				vo.setTitle(rs.getString(3));
				vo.setPoster(rs.getString(4));
				vo.setAuthor(rs.getString(5));
				vo.setStory(rs.getString(6));
				vo.setAvgstar(rs.getDouble(7));
				vo.setSerial(rs.getString(8));
				vo.setIscp(rs.getString(9));
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

	public int novelTotalPage() {
		int count = 0;
		try {
			conn = db.getConnection();
			String sql = "SELECT CEIL(COUNT(*)/?) FROM novel";
			ps = conn.prepareStatement(sql);
			ps.setDouble(1, NOVELROW);
			ResultSet rs = ps.executeQuery();
			rs.next();
			count = rs.getInt(1);
			rs.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			db.disConnection(conn, ps);
		}
		return count;
	}
}
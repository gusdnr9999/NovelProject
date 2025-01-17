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
			String sql = "SELECT no,title,poster,num "
					+ "FROM (SELECT no,title,poster,rownum as num "
					+ "FROM (SELECT /*+ INDEX_ASC(novel novel_no_pk) */ no,title,poster "
					+ "FROM novel))"
					+ "WHERE num BETWEEN ? AND ?";
			ps = conn.prepareStatement(sql);
			int start = (NOVELROW * page) - (NOVELROW - 1);
			int end = (NOVELROW * page);
			ps.setInt(1, start);
			ps.setInt(2, end);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				NovelVO vo = new NovelVO();
				vo.setNo(rs.getInt(1));
				vo.setTitle(rs.getString(2));
				vo.setPoster(rs.getString(3));
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
	// 최신 평가받은 소설
	public List<NovelVO> novelTrend(){
		List<NovelVO> list = new ArrayList();
		try {
			conn = db.getConnection();
			String sql = "SELECT poster, title, star "
					+ "FROM novel,(SELECT no, star, regdate, num "
					+ "FROM (SELECT no, star, regdate, rownum as num "
					+ "FROM (SELECT no, star, regdate\n"
					+ "FROM review order by regdate DESC)) "
					+ "WHERE num <= 10) r "
					+ "WHERE r.no = novel.no";
			ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				NovelVO vo = new NovelVO();
				vo.setPoster(rs.getString(1));
				vo.setTitle(rs.getString(2));
				vo.getRVo().setStar(rs.getDouble(3));
				list.add(vo);
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.disConnection(conn, ps);
		}
		return list;
	}
	// 장르별 총 페이지
	public int novelGenreTotalPage(String genre){
		int count = 0;
		if(genre.equals("전체")) {
			return novelTotalPage();
		}
		try {
			conn = db.getConnection();
			String sql = "SELECT COUNT(*) "
					+ "FROM novel "
					+ "WHERE genre LIKE '%'||?||'%'";
			if(genre.equals("판타지")) {
				sql += " AND genre NOT LIKE '%'||현대판타지||'%'";
			}
			ps = conn.prepareStatement(sql);
			ps.setString(1, genre);				
			ResultSet rs = ps.executeQuery();
			rs.next();
			count = rs.getInt(1);
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.disConnection(conn, ps);
		}
		return count;
	}
	// 장르별 소설 목록
		public List<NovelVO> novelGenreListData(int page, String genre) {
			List<NovelVO> list = new ArrayList<NovelVO>();
			try {
				conn = db.getConnection();
				String sql = "SELECT no,title,poster,num "
						+ "FROM (SELECT no,title,poster,rownum as num "
						+ "FROM (SELECT /*+ INDEX_ASC(novel novel_no_pk) */ no,title,poster "
						+ "FROM novel";
				if(genre.equals("전체")) {
					
				}else if(genre.equals("판타지")) {
					sql += "WHERE genre LIKE '%'||?||'%' AND genre NOT LIKE '%'||현대판타지||'%'";
				}else {
					sql += "WHERE genre LIKE '%'||?||'%'";
				}
				sql += ")) WHERE num BETWEEN ? AND ?";
				ps = conn.prepareStatement(sql);
				int start = (NOVELROW * page) - (NOVELROW - 1);
				int end = (NOVELROW * page);
				ps.setInt(1, start);
				ps.setInt(2, end);
				ResultSet rs = ps.executeQuery();
				while (rs.next()) {
					NovelVO vo = new NovelVO();
					vo.setNo(rs.getInt(1));
					vo.setTitle(rs.getString(2));
					vo.setPoster(rs.getString(3));
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
}
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
	private final int NOVELFINDROW = 20;

	public static NovelDAO newInstance() {
		if (nDao == null)
			nDao = new NovelDAO();
		return nDao;
	}

	//홈 => 전체목록 출력
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
	
	// 홈 => 전체페이지
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
			String sql = "SELECT CEIL(COUNT(*)/12.0) "
					+ "FROM novel "
					+ "WHERE ' '|| genre ||' ' LIKE '%'||' '||?||' '||'%'";
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
						+ "FROM novel ";
				if(genre.equals("전체")) {
					sql += "WHERE '전체' = ?";
				}else{
					sql += "WHERE ' '|| genre ||' ' LIKE '%'||' '||?||' '||'%'";
				}
				sql += ")) WHERE num BETWEEN ? AND ?";
				ps = conn.prepareStatement(sql);
				int start = (NOVELROW * page) - (NOVELROW - 1);
				int end = (NOVELROW * page);
				ps.setString(1, genre);
				ps.setInt(2, start);
				ps.setInt(3, end);
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
		// 소설 검색 리스트
		public List<NovelVO> novelFindData(int page, String title) {
			List<NovelVO> list = new ArrayList<NovelVO>();
			try {
				conn = db.getConnection();
				String sql = "SELECT no,poster,title,genre,author,avgstar,num "
						+ "FROM (SELECT no,poster,title,genre,author,avgstar,rownum as num "
						+ "FROM (SELECT /*+ INDEX_ASC(novel novel_no_pk) */ no,poster,title,genre,author,avgstar "
						+ "FROM novel "
						+ "WHERE TITLE LIKE '%'||?||'%')) "
						+ "WHERE num BETWEEN ? AND ?";
				ps = conn.prepareStatement(sql);
				int start = (NOVELFINDROW * page) - (NOVELFINDROW - 1);
				int end = (NOVELFINDROW * page);
				ps.setString(1, title);
				ps.setInt(2, start);
				ps.setInt(3, end);
				ResultSet rs = ps.executeQuery();
				while(rs.next()) {
					NovelVO vo = new NovelVO();
					vo.setNo(rs.getInt(1));
					vo.setPoster(rs.getString(2));
					vo.setTitle(rs.getString(3));
					vo.setGenre(rs.getString(4));
					vo.setAuthor(rs.getString(5));
					vo.setAvgstar(rs.getDouble(6));
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
		// 소설 검색 리스트총 페이지수
		public int novelFindTotalPage(String title) {
			int count = 1;
			try {
				conn = db.getConnection();
				String sql = "SELECT CEIL(COUNT(no)/10.0) "
						+ "FROM (SELECT no,title,poster,rownum as num "
						+ "FROM (SELECT /*+ INDEX_ASC(novel novel_no_pk) */ no,title,poster "
						+ "FROM novel "
						+ "WHERE TITLE LIKE '%'||?||'%'))";
				ps = conn.prepareStatement(sql);
				ps.setString(1, title);
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
		public NovelVO novelDetailData(int no) {
			NovelVO vo = new NovelVO();
			try {
				conn = db.getConnection();
				String sql = "select no, genre, title, poster, author, story, avgstar, serial, iscp "
						+ "from novel "
						+ "where no = " + no;
				ps = conn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery();
				rs.next();
				vo.setNo(rs.getInt(1));
				vo.setGenre(rs.getString(2));
				vo.setTitle(rs.getString(3));
				vo.setPoster(rs.getString(4));
				vo.setAuthor(rs.getString(5));
				vo.setStory(rs.getString(6));
				vo.setAvgstar(Double.parseDouble(rs.getString(7)));
				vo.setSerial(rs.getString(8));
				vo.setIscp(rs.getString(9));
				rs.close();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				db.disConnection(conn, ps);
			}
			return vo;
		}
}
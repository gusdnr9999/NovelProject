package dao;
import java.sql.*;
public class DataBase {
	private Connection conn;
	private PreparedStatement ps;
	
	private final String URL="jdbc:oracle:thin:@211.238.142.124:1521:XE";
	
	public DataBase()
	{
		try
		{
			Class.forName("oracle.jdbc.driver.OracleDriver");
		}catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	public void getConnection()
	{
		try
		{
			conn=DriverManager.getConnection(URL,"hr_4","happy");
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
		}catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
}

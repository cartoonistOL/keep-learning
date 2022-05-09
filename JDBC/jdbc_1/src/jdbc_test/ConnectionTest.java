package jdbc_test;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;


public class ConnectionTest {
	public Connection getConnection() throws SQLException, IOException {
		InputStream in = ConnectionTest.class.getClassLoader().getResourceAsStream("db.properties");
		Properties properties = new Properties();
		properties.load(in);
		
		String driver = properties.getProperty("driver");
		String url = properties.getProperty("url");
	    String user =  properties.getProperty("user");
	    String password =  properties.getProperty("password");
		

		try {
	        Class.forName(driver);
	    }catch (Exception e) {
	        System.out.print("¼ÓÔØÇý¶¯Ê§°Ü!");
	        e.printStackTrace();
	    }
		
	    
	    Connection conn = DriverManager.getConnection(url, user, password);
	    return conn;
	}
	
}

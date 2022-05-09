package jdbc_test;


import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class GetMessage {
	public static void main(String[] args) throws SQLException,IOException {
		ConnectionTest connection = new ConnectionTest();
		Connection conn = connection.getConnection();
		System.out.println(conn);
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("select * from user");
	
		while (rs.next()) {
			System.out.print("ÊÖ»úºÅ:" + rs.getString("phone")+"\t");
			System.out.println("ÐÕÃû:" + rs.getString("name")+"\t");
		}
	}
}

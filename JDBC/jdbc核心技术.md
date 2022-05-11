| 知识点 |内容 |
|---| :-----:  |
|  C/S| 客户、服务器  |
|B/S|浏览器、服务器|


| 知识点 | 内容|
|---  | :---: |
|  JDBC概念| 是一组接口规范，各厂商通过规范，提供了一系列驱动（实现类） |

```java
"""连接数据库的基本步骤---5.1版本"""

import java.sql.DriverManager;
import java.sql.SQLException;

import org.junit.Rule;

public class t1{
	public void connection() throws Exception {
		System.out.println("connecting...");
		
		//1.三个连接参数
		String url = "jdbc:mysql://localhost:3306/test";
		String user = "root";
		String password = "123456";
		
		//2.注册驱动
		Class.forName("com.mysql.jdbc.Driver"); //反射
		
		//3.DriverManager（参数）
		DriverManager.getConnection(url,user,password);
		
		System.out.println(conn);
	}
	
}

```

```java
"""	mysql8.0的方法
	jar包：mysql-connector-java-8.0.28.jar
	"""
public class ConnectionTest {
	public Connection getConnection() throws SQLException {
		try {
	        Class.forName("com.mysql.cj.jdbc.Driver");//驱动名称改变了
	    }catch (Exception e) {
	        System.out.print("加载驱动失败!");
	        e.printStackTrace();
	    }
	    String url = "jdbc:mysql://localhost:3306/test";
	    String user = "root";
	    String password = "123456";
	    Connection conn = DriverManager.getConnection(url, user, password);
	    return conn;
	}
}
```

```java
"""最终方法：通过配置文件获取登陆参数"""
/* 文件内容 ：
user=root
password=abc123
url=jdbc:mysql://localhost:3306/test?rewriteBatchedStatements=true
driverClass=com.mysql.jdbc.Driver */

/*  
	 * 此种方式的好处？
	 * 1.实现了数据与代码的分离。实现了解耦
	 * 2.如果需要修改配置文件信息，可以避免程序重新打包。
	 */
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
	        System.out.print("加载驱动失败!");
	        e.printStackTrace();
	    }
		
	    
	    Connection conn = DriverManager.getConnection(url, user, password);
	    return conn;
	}
}
```

| 知识点 | 内容|
|---  | :---: |
| statement的弊端|SQL注入问题，恶意访问数据库 |
```java
"""statement传递SQL语句"""
Statement stmt = conn.createStatement();
sql = "select * from user"
ResultSet rs = stmt.executeQuery(sql);
```

| 知识点 | 内容|
|---  | :---: |
| PreparedStatment|传入带通配符的预编译sql语句<br>|
```java
"""prepareStatement進行增刪改"""
public void update(String sql,Object ...args){//傳入帶通配符的sql語句和通配符位置的參數
	...
	//String sql = "insert into customers(name,email,birth)values(?,?,?)";
	ps = conn.prepareStatement(sql);
	for (int i = 0; i < args.length; i++) { 
		ps.setObject(i + 1, args[i]); //用setObject设置通配符相应位置的值
		}
}

```


---

| 知识点 | 内容|
|---  | :---: |
| | |

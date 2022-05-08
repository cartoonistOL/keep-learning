| 知识点 |内容 |
|---| :-----:  |
|  C/S| 客户、服务器  |
|B/S|浏览器、服务器|


| 知识点 | 内容|
|---  | :---: |
|  JDBC概念| 是一组接口规范，各厂商通过规范，提供了一系列驱动（实现类） |

```java
/* 连接数据库的基本步骤 */

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
		Class.forName("com.mysql.jdbc.Driver");
		
		//3.DriverManager（参数）
		DriverManager.getConnection(url,user,password);
		
		System.out.println(conn);
	}
	
}
```
```java
/* 通过配置文件获取登陆参数 */
/* 文件内容 ：
user=root
password=abc123
url=jdbc:mysql://localhost:3306/test?rewriteBatchedStatements=true
driverClass=com.mysql.jdbc.Driver */
```




---
| 知识点 | 内容|
|---  | :---: |
| | |

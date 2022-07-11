| 知识点 |内容 |
|---| :-----:  |
| C/S | 客户、服务器  |
|B/S|浏览器、服务器|


| 知识点 | 内容|
|---  | :---: |
|  JDBC概念| 是一组接口规范，各厂商通过规范，提供了一系列驱动（实现类） |
| JUnit加载方法 | [JUnit - Eclipse 插件_w3cschool](https://www.w3cschool.cn/junit/q2os1huq.html) |

## 1.JDBC最基本方法

```java
"""连接数据库的基本步骤---5.1版本Driver"""

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

## 2.改进：读取文档获取驱动实现类和三个连接参数

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

## 3.JDBC的增删改查

### conn.createStatement

| 知识点 | 内容|
|---  | :---: |
| statement的弊端|SQL注入问题，恶意访问数据库 |
```java
"""statement传递SQL语句"""
Statement stmt = conn.createStatement();
sql = "select * from user"
ResultSet rs = stmt.executeQuery(sql);
```

### conn.preparedStatment

| 知识点 | 内容|
|---  | :---: |
| PreparedStatment替代statement |传入带通配符的预编译sql语句|
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
//执行
ps.execute();

```


---

## 4.封装：建立连接&资源关闭

### 封装connection和close方法到类utils中

```java
package jdbc.utils;

public class JdbcUtils {
	public static Connection getConnection(String propertiesfile) throws SQLException, IOException {
		Connection conn = null;
		PreparedStatement ps = null;
			InputStream in = ClassLoader.getSystemClassLoader().getResourceAsStream(propertiesfile); //ClassLoader加载文件方法
			Properties properties = new Properties();
			properties.load(in);
			String driver = properties.getProperty("driver");
			String url = properties.getProperty("url");
			String user =  properties.getProperty("user");
			String password =  properties.getProperty("password");
			
			//注册驱动
			try {
			    Class.forName(driver);
			}catch (Exception e) {
			    System.out.print("加载驱动失败!");
			    e.printStackTrace();
			}
			conn = DriverManager.getConnection(url, user, password);
		return conn;
	}
	
	public static void closeResource(Connection conn,Statement ps) {	//prepareStatement是Statement的子接口
		//资源的关闭：conn和ps
		try {
			if (conn != null) {	//保证conn是获取到的情况下才close
				conn.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
```

### 最后在增删改代码中调用connection和close方法

```java

import jdbc.utils.JdbcUtils;

public class PrepareStatmentCRU {
	//数据库增删改
	public static void main(String[] args) throws SQLException, IOException {
		//1、连接
		Connection conn = JdbcUtils.getConnection("db.properties");
		//2.创建sql语句
		String sql = "update customers set name=? where id =?";
		PreparedStatement ps = conn.prepareStatement(sql);
		//3.填充占位符
		ps.setObject(0, "莫扎特");	//索引从1开始
		ps.setObject(1, 18);
		//4.执行
		ps.execute();
		//5.关闭资源
		JdbcUtils.closeResource(conn, ps);
}
}
```

添加exception和finally：

> 1.选中代码段，右键surround with->try catch block
>
> 2.只保留一个Exception
>
> 3.添加finally{close}
>
> 4.添加初始值

```jaVA
"""最终效果"""
import jdbc.utils.JdbcUtils;

public class PrepareStatmentCRU {
	//数据库增删改
	public static void main(String[] args){
         Connection conn = null;
		PreparedStatement ps = null;
		//1、连接
		try {
			Connection conn = JdbcUtils.getConnection("db.properties");
			//2.创建sql语句
			String sql = "update customers set name=? where id =?";
			PreparedStatement ps = conn.prepareStatement(sql);
			//3.填充占位符
			ps.setObject(0, "莫扎特");	//索引从1开始
			ps.setObject(1, 18);
			//4.执行
			ps.execute();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			//5.关闭资源
			JdbcUtils.closeResource(conn, ps);
		}
}
}
```

## 5.增删改

```JAVA
@Test
//通用的增删改操作
public void update(String sql,Object ...args) {
		Connection conn = null;
		PreparedStatement ps = null;
		//1、连接
		try {
			conn = JdbcUtils.getConnection("db.properties");
			//2传递sql语句
			ps = conn.prepareStatement(sql);
			//3.填充占位符
			for(int i = 0;i < args.length;i++){
				ps.setObject(i + 1, args[i]);//序号从1开始
			}
			//4.执行
			ps.execute();
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			//5.关闭资源
			JdbcUtils.closeResource(conn, ps);
		}
}
```

## 6.查询

### 自建order类

根据表的结构，来构建order类，属性变量类型要对应，但是属性名和字段名可以不同

![image-20220512130946943](C:\Users\owl\AppData\Roaming\Typora\typora-user-images\image-20220512130946943.png)

<img src="C:\Users\owl\AppData\Roaming\Typora\typora-user-images\image-20220512131138567.png" alt="image-20220512131138567" style="zoom:80%;" />

<img src="C:\Users\owl\AppData\Roaming\Typora\typora-user-images\image-20220511215133119.png" alt="image-20220511215133119" style="zoom: 50%;" />

> 快捷方式，右键—source：
>
> - 无参构造方法
> - 带参构造方法
> - get、set方法
> - tostring方法

```JAVA
public class Order {
	private int orderId;
	private String orderName;
	private Date orderDate;
	public Order() {
		super();
	}
	public Order(int orderId, String orderName, Date orderDate) {
		super();
		this.orderId = orderId;
		this.orderName = orderName;
		this.orderDate = orderDate;
	}
	@Override
	public String toString() {
		return "order [orderId=" + orderId + ", orderName=" + orderName + ", orderDate=" + orderDate + "]";
	}
	public int getOrderId() {
		return orderId;
	}
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	public String getOrderName() {
		return orderName;
	}
	public void setOrderName(String orderName) {
		this.orderName = orderName;
	}
	public Date getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}
	
}
```

### Date格式的转换

```JAVA
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
java.util.Date date = sdf.parse("1000-01-01");
ps.setDate(3,  new java.sql.Date(date.getTime()));	//传入的date类必须是java.sql.Date类
```



### 通用query方法

```JAVA
import com.atguigu3.bean.Order;

@Test
public void testOrderForQuery(){
		String sql = "select order_id orderId,order_name orderName,order_date orderDate from `order` where order_id = ?";	//使用类的属性名来命名字段的别名
		Order order = orderForQuery(sql,1);
		System.out.println(order);
	}
public Order orderForQuery(String sql,Object...args){
		/*
		* @Description 通用的针对于order表的查询操作,返回order类
         * 针对于表的字段名与类的属性名不相同的情况：
         * 1. 必须声明sql时，使用类的属性名来命名字段的别名
         * 2. 使用ResultSetMetaData时，需要使用getColumnLabel()来替换getColumnName(),获取列的别名。
         * 说明：如果sql中没有给字段其别名，getColumnLabel()获取的就是列名
         */
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = JDBCUtils.getConnection();
			ps = conn.prepareStatement(sql);
			for(int i = 0;i < args.length;i++){
				ps.setObject(i + 1, args[i]);
			}
			//执行，获取结果集
			rs = ps.executeQuery();
			//获取结果集的元数据
			ResultSetMetaData rsmd = rs.getMetaData();
			//获取列数
			int columnCount = rsmd.getColumnCount();
			if(rs.next()){	//遍历记录
				Order order = new Order();
				for(int i = 0;i < columnCount;i++){		//遍历属性
					//获取每个列的列值:通过ResultSet
					Object columnValue = rs.getObject(i + 1);
					//通过ResultSetMetaData
					//获取列的列名：getColumnName() --不推荐使用
					//获取列的别名：getColumnLabel()
					//String columnName = rsmd.getColumnName(i + 1);
					String columnLabel = rsmd.getColumnLabel(i + 1);
					
					//通过反射，将对象指定名columnName的属性赋值为指定的值columnValue
					Field field = Order.class.getDeclaredField(columnLabel);
					field.setAccessible(true);
					field.set(order, columnValue);
				}
				return order;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			JDBCUtils.closeResource(conn, ps, rs);	//这里是重写的closeResource方法，有三个参数
		}
		return null;
		
	}
```

## 7.blob类型

## 8.批处理

## 9.考虑事务

### 具体的增删改操作中，从外部传入Connection，内部不再关闭connection

### 事务的隔离级别：ACID

## 10.DAO

### 父类BaseDao

### 具体接口Dao

### 继承BaseDao、实现接口Dao

## 11.数据库连接池

### 为什么使用连接池

传统数据库链接操作：

1. 建立连接
2. 执行操作
3. 断开连接

产生的问题：

- 连接时间
- 断开问题
- 连接数量控制

连接词的功能

### 开源连接池

- DBCP
- c3p0
- Druid（阿里）

## 12.DbUtils

封装了CRUD操作

### QueryRunner

```JAVA
"""增删改"""
QueryRunner runner = new QueryRunner();
String sql = "insert into customers(name,email,birth)values(?,?,?)";
int update = runner.update(conn, sql ,"菜菜菜","123@qq.com","1998-10-10");#返回受影响的行数update
```

```JAVA
"""查"""
QueryRunner runner = new QueryRunner();
String sql = "select id,name,email,birth from customers where id = ?";
Customer customer = runner.query(conn, sql, handler, 23);
```

QueryRunner方法

<img src="C:\Users\owl\AppData\Roaming\Typora\typora-user-images\image-20220517195244618.png" alt="image-20220517195244618" style="zoom: 67%;" />

### ResultSetHandler\<T>

具体实现类

<img src="C:\Users\owl\AppData\Roaming\Typora\typora-user-images\image-20220517201646803.png" alt="image-20220517201646803" style="zoom:150%;" />

### BeanHandler\<T>

> 封装一条记录

```JAVA
String sql = "select id,name,email,birth from customers where id = ?";
BeanHandler<Customer> handler = new BeanHandler<>(Customer.class);
Customer customer = runner.query(conn, sql, handler, 23);
```

### ScalaHandler

> 获取特殊值，返回值需要强转：

```java
String sql = "select max(birth) from customers";
ScalarHandler handler = new ScalarHandler();
Date maxBirth = (Date) runner.query(conn, sql, handler);
```

## junit测试

| 添加junit测试 | [Eclipse项目如何添加Junit？Eclipse项目添加Junit的方法步骤](https://www.downkr.com/news/190038_1.html) |
| :-----------: | :----------------------------------------------------------: |

## ORM编程思想

```java
package com.atguigu3.bean;
/*
 * ORM编程思想  （object relational mapping）
 * 一个数据表对应一个java类
 * 表中的一条记录对应java类的一个对象
 * 表中的一个字段对应java类的一个属性
 * 
 */
public class Customer {
	
	private int id;
	private String name;
	private String email;
	private Date birth;
	public Customer() {
		super();
	}
	public Customer(int id, String name, String email, Date birth) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.birth = birth;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Date getBirth() {
		return birth;
	}
	public void setBirth(Date birth) {
		this.birth = birth;
	}
	@Override
	public String toString() {
		return "Customer [id=" + id + ", name=" + name + ", email=" + email + ", birth=" + birth + "]";
	}
}
```

## Java与SQL对应数据类型转换

![image-20220511215133119](C:\Users\owl\AppData\Roaming\Typora\typora-user-images\image-20220511215133119.png)

## 快捷方法

### 快速添加get、set、tostring、构造方法

> 快捷键：alt + shift + s

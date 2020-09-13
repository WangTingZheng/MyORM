# MyORM

[![Github Package](https://img.shields.io/badge/Github-Package-brightgreen)](https://github.com/WangTingZheng/MyORM/packages) [![Github Wiki](https://img.shields.io/badge/Github-Wiki-brightgreen)](https://github.com/WangTingZheng/MyORM/wiki)[![BiliBili](https://img.shields.io/badge/BiliBili-Videos-brightgreen)](https://www.bilibili.com/video/BV1Ff4y1R7cN)

A simple ORM(Object Relational Mapping) for Java.
## Quck Start
### Install
Add this text to your `pom.xml`
```xml
<dependency>
    <groupId>com.wangtingzheng</groupId>
    <artifactId>myorm</artifactId>
    <version>1.0.6</version>
</dependency>
```
The latest version can be check [here](https://github.com/WangTingZheng/MyORM/packages).
### Define a table
This code will create a table named `MyTable` which has two item: username and password. The username is a 100 length varchar and it is primary key, the password is same but it is not primary key.
```java
@OrmTable
public class MyTable  {
    @OrmItem(type = ItemTypeEnum.VARCHAR, length = 100, isPrimaryKey = true)
    private String username;

    @OrmItem(type = ItemTypeEnum.VARCHAR, length = 100)
    private String password;

    public MyTable(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
```
### Define a database
This code will define a database which includes one table named MyTable. MyOrm can use SQLITE Driver to connect database in 123.456.789.123:3306 use a account called "myuser". When connect established, MyOrm will use database called test, the server timezone is +8:00.
```java
@OrmDatabase(type = DatabaseTypeEnum.SQLITE,host = "123.456.789.123:3306",username = "myuser",password = "mypassword",openDatabase = "test",serverTimezone = "+8:00")
public class MyDatabase {
    @OrmTable()
    public MyTable myTable;

}
```
### Add a record
This code will add a record to table named "MyTable" in database "Mydatabse".
```java
DatabaseApt myDatabase = new DatabaseApt(MyDatabase.class);  //new database annotation process tool
TableApt myTable = new myDatabase.newInstance(MyTable.class ); //get table annotation process tool
MyTable record = new MyTable("wangtingzheng","iloveMyOrm");  //new a record obejct
myDatabase.create(); //create database if not existed
myTable.create(); //create table if not existed
if(myTable.add(record)){  //if add record successfully
    System.out.println("Record has been add to MyTable in Mydatabase.");
}
else{
    System.out.println("Add failed."); 
}
```
## Wiki
More detail can be saw in [wiki](https://github.com/WangTingZheng/MyORM/wiki):
- [Install MyORM to your project](https://github.com/WangTingZheng/MyORM/wiki/Install)
- [Define database and table with class](https://github.com/WangTingZheng/MyORM/wiki/Define)
- [Execute table operation](https://github.com/WangTingZheng/MyORM/wiki/Operation)
- [Write extend to make MyORM support more databases](https://github.com/WangTingZheng/MyORM/wiki/Extend)

## Todo

- Support more databases
- Support sql language DIY
- Improve performance
- Support more sql data type
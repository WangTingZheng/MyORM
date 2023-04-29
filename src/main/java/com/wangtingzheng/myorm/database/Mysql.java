package com.wangtingzheng.myorm.database;

import com.wangtingzheng.myorm.entity.DatabaseConnectionEntity;
import com.wangtingzheng.myorm.exception.ConnectionGetFailed;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author WangTingZheng
 * @date 2020/7/8 15:06
 * @features
 */
public class Mysql extends DatabaseLayer {
    private String DATABASE_URL = "jdbc:mysql://";
    public Mysql(DatabaseConnectionEntity databaseConnectionEntity) {
        super(databaseConnectionEntity);
    }
    @Override
    public Connection getConnection() throws ConnectionGetFailed {
        DATABASE_URL = DATABASE_URL + databaseConnectionEntity.getHost()+"/" + databaseConnectionEntity.getOpenDatabase()+"?serverTimezone="+ databaseConnectionEntity.getServerTimezone();
        try {
            String DRIVER = "com.mysql.cj.jdbc.Driver";
            Class.forName(DRIVER);
            return DriverManager.getConnection(DATABASE_URL, databaseConnectionEntity.getUsername(), databaseConnectionEntity.getPassword());
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        throw new ConnectionGetFailed("Connection get failed.", this.getClass());
    }
}

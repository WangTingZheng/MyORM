package com.wangtingzheng.myorm;

import com.wangtingzheng.myorm.apt.DatabaseApt;
import com.wangtingzheng.myorm.apt.TableApt;
import com.wangtingzheng.myorm.test.MyDatabase;
import com.wangtingzheng.myorm.test.MyTable;
import com.wangtingzheng.myorm.reflection.ReflectUtils;

import java.security.Key;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * @author WangTingZheng
 * @date 2020/7/8 9:01
 * @features
 */
public class App{
    public static void main(String[] args) throws Exception {
        DatabaseApt databaseApt = new DatabaseApt(MyDatabase.class);
        //databaseApt.create();

        TableApt tableApt = databaseApt.newTableAptInstance("MyTable");
        MyTable table = new MyTable();
        table.setUsername("wtz111");
        table.setPassword("12345");
        System.out.println(tableApt.add(table));
    }

}

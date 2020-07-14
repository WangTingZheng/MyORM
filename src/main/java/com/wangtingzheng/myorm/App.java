package com.wangtingzheng.myorm;

import com.wangtingzheng.myorm.apt.DatabaseApt;
import com.wangtingzheng.myorm.apt.TableApt;
import com.wangtingzheng.myorm.test.MyDatabase;
import com.wangtingzheng.myorm.test.MyTable;

/**
 * @author WangTingZheng
 * @date 2020/7/8 9:01
 * @features
 */
public class App{
    public static void main(String[] args) throws Exception {
        DatabaseApt databaseApt = new DatabaseApt(MyDatabase.class);

        TableApt tableApt = databaseApt.newTableAptInstance(MyTable.class);
        MyTable table = new MyTable();
        table.setUsername("wtz1113");
        table.setPassword("12345");
        System.out.println(tableApt.add(table));
    }

}

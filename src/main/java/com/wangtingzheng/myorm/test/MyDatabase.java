package com.wangtingzheng.myorm.test;

import com.wangtingzheng.myorm.annotation.OrmDatabase;
import com.wangtingzheng.myorm.annotation.OrmTable;
import com.wangtingzheng.myorm.database.MysqlTest;
import com.wangtingzheng.myorm.enums.DatabaseTypeEnum;

/**
 * @author WangTingZheng
 * @date 2020/7/8 9:44
 * @features
 */
@OrmDatabase(type = DatabaseTypeEnum.SQLITE,host = "123.456.789.123:3306",username = "myusername",password = "mypassword",openDatabase = "test",serverTimezone = "+8:00")
public class MyDatabase {
    @OrmTable()
    public MyTable myTable;

}

package com.wangtingzheng.myorm.test;

import com.wangtingzheng.myorm.annotation.OrmItem;
import com.wangtingzheng.myorm.annotation.OrmTable;
import com.wangtingzheng.myorm.enums.ItemTypeEnum;

/**
 * @author WangTingZheng
 * @date 2020/7/8 9:10
 * @features
 */
@OrmTable
public class MyTable  {
    @OrmItem(type = ItemTypeEnum.VARCHAR, length = 100, isPrimaryKey = true)
    private String username;

    @OrmItem(type = ItemTypeEnum.VARCHAR, length = 100)
    private String password;

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

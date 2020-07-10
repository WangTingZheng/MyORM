package com.wangtingzheng.myorm.apt;

import com.wangtingzheng.myorm.database.DatabaseLayer;
import com.wangtingzheng.myorm.database.ExtendSample;
import com.wangtingzheng.myorm.database.Mysql;
import com.wangtingzheng.myorm.database.SQLite;
import com.wangtingzheng.myorm.entity.DatabaseConnectionEntity;
import com.wangtingzheng.myorm.entity.DatabaseEntity;
import com.wangtingzheng.myorm.enums.DatabaseTypeEnum;
import com.wangtingzheng.myorm.exception.*;
import com.wangtingzheng.myorm.reflection.DatabaseReflection;
import com.wangtingzheng.myorm.util.SQL;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;

/**
 * @author WangTingZheng
 * @date 2020/7/8 12:21
 * @features
 */
public class DatabaseApt {
    public DatabaseConnectionEntity databaseConnectionEntity; //数据库连接实体，保存着数据库连接相关的信息，比如说密码
    public Class<com.wangtingzheng.myorm.test.MyDatabase> database; //orm数据库的类
    public Connection connection; //数据库连接对象
    public DatabaseEntity databaseEntity; //数据库实体，保存着数据库中表的信息，通过反射获得

    public DatabaseApt(Class database) {
        this.database = database;
        try {
            this.databaseConnectionEntity = getDatabaseConnection();
            this.connection = getConnection();
            this.databaseEntity = getTable(database);
        } catch (DatabaseConnectionAnnotationNotFound |DatabaseTypeNotFound|TableNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析数据库类中的连接信息
     */
    private DatabaseConnectionEntity getDatabaseConnection() throws DatabaseConnectionAnnotationNotFound {
        try {
            return new DatabaseReflection(database).toDatabaseConnectionEntity();
        } catch (DatabaseNotFoundException e) {
            e.printStackTrace();
        }
        throw new DatabaseConnectionAnnotationNotFound("Database connect annotation not found.", database);
    }

    /**
     * 解析数据库类中的表信息
     */
    private DatabaseEntity getTable(Class database) throws TableNotFoundException {
        try {
            return new DatabaseReflection(database).toDatabaseEntity();
        } catch (TableNotFoundException e) {
            e.printStackTrace();
        }
        throw  new TableNotFoundException("Table not found when new databaseApt", database);
    }

    /**
     * 使用获得的数据库连接信息进行连接，获得连接
     * @return 获得的连接
     */
    public Connection getConnection() throws DatabaseTypeNotFound {
        if (databaseConnectionEntity.type == DatabaseTypeEnum.EXTEND && databaseConnectionEntity.extendType != ExtendSample.class){ //如果选择的是扩展数据库并且用户已经填值了的话
            if (databaseConnectionEntity.extendType.getSuperclass() == DatabaseLayer.class){ //如果改类继承了DatabaseLayer类
                DatabaseLayer databaseLayer = null; //创建数据库驱动对象
                try {
                    databaseLayer = (DatabaseLayer) databaseConnectionEntity.extendType.getConstructor(DatabaseConnectionEntity.class).newInstance(databaseConnectionEntity);
                    return databaseLayer.getConnection();//获得叫getConnection的函数，执行它，获得返回值
                } catch (InstantiationException | IllegalAccessException |ConnectionGetFailed | InvocationTargetException | NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }
        }
        try {
            if (databaseConnectionEntity.type == DatabaseTypeEnum.MYSQL){
                return new Mysql(databaseConnectionEntity).getConnection();
            }else if (databaseConnectionEntity.type == DatabaseTypeEnum.SQLITE){
                return new SQLite(databaseConnectionEntity).getConnection();
            }
        } catch (ConnectionGetFailed connectionGetFailed) {
            connectionGetFailed.printStackTrace();
        }

        throw new DatabaseTypeNotFound("Database not found.", database);
    }

    /**
     * 返回一个table apt对象，主要的过程是：
     * 通过名称查找数据库下的表，得到表所对应的类
     * 准备好数据库连接对象
     * 准备好数据库名称
     * 通过表类、数据库连接对象、数据库名称创建一个tableApt对象
     * @param name 表的名称
     * @return 表所对应的 tableApt对象
     * @throws TableClassNotFoundException 当数据库类中找不到这个表时，会报错
     */
    public TableApt newTableAptInstance(String name) throws TableClassNotFoundException {
        for(Class myClass: databaseEntity.getTableClasses()){
            if (myClass.getSimpleName().equals(name)){
                try {
                    return new TableApt(myClass, getConnection(),database.getSimpleName());
                } catch (DatabaseTypeNotFound e) {
                    e.printStackTrace();
                }
            }
        }
        throw new TableClassNotFoundException("Table class not found in database when get new TableApt", database);
    }

    public TableApt newTableAptInstance() throws TableClassNotFoundException {
        return newTableAptInstance();
    }

    public TableApt newTableAptInstance(Class tableClass) throws TableClassNotFoundException {
       return newTableAptInstance(tableClass.getSimpleName());
    }

    /**
     * 创建数据库，数据库名称就是数据库类的类名称
     * @return 成功为true，不成功为false
     */
    public boolean create(){
        return SQL.createDatabase(connection, database.getSimpleName());
    }

    /**
     * 删除数据库
     * @return 成功为true，不成功为false
     */
    public boolean delete() {return SQL.dropDatabase(connection, database.getSimpleName());}
}

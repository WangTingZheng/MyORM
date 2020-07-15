package com.wangtingzheng.myorm.apt;

import com.wangtingzheng.myorm.annotation.OrmItem;
import com.wangtingzheng.myorm.entity.TableEntity;
import com.wangtingzheng.myorm.exception.DatabaseExcuteNoResult;
import com.wangtingzheng.myorm.exception.TableItemNotFoundException;
import com.wangtingzheng.myorm.reflection.ObjectReflection;
import com.wangtingzheng.myorm.reflection.TableReflection;
import com.wangtingzheng.myorm.util.SQL;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * @author WangTingZheng
 * @date 2020/7/8 9:01
 * @features
 */
public class TableApt {
    private Class table; //表类
    private TableEntity tableEntity;
    private String databaseName; //表所在数据库的名称

    private Connection connection; //数据库连接对象



    /**
     * table apt 初始化，完成的工作有
     * 通过反射获得表名称
     * 通过反射获得表中定义的数据库属性
     * 生成插件数据库sql语句
     */
    public TableApt(Class table, Connection connection, String databaseName) {
        this.table = table;
        this.connection = connection;
        this.databaseName = databaseName;
        try {
            this.tableEntity = getTableEntity();
        } catch (TableItemNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过反射获取表类中的表项的内容，包装成表项实体
     */
    private TableEntity getTableEntity() throws TableItemNotFoundException {
        return new TableReflection(table).toTableEntity();
    }


    /**
     * 使用数据库
     * @return 成功为true，失败为false
     */
    private boolean useDatabase(){
        return SQL.useDatabase(connection, databaseName);
    }

    /**
     * 创建数据库，数据库名称为数据库类的类名
     * @return 成功为true，失败为false
     */
    public boolean create(){
        useDatabase();
        return SQL.createTable(connection, table.getSimpleName(), tableEntity.getTableItems());
    }

    /**
     * 向数据库添加数据
     * @param value 含有orm注解的数据库表对象
     * @return 成功为true，失败为false
     */
    private boolean add(HashMap<String,String> value){
        useDatabase();
        return SQL.insert(connection,table.getSimpleName(),value);
    }


    /**
     * 从含有orm注解的数据库对象中获取表项信息，转换为hashmap
     * @param object 含有orm注解的数据库对象
     * @return 含有表项信息的hashmap，第一个为项目名称，第二个为项目的值
     */
    private HashMap<String, String> getItem(Object object){
        Class clazz = object.getClass();
        HashMap<String,String> value = new HashMap<>();
        for(Field field: clazz.getDeclaredFields()){
            if (field.isAnnotationPresent(OrmItem.class)){
                try {
                    Object fieldValueObject = ObjectReflection.getValue(object,field.getName());
                    String fieldValue = "";
                    if (fieldValueObject != null){
                        fieldValue = fieldValueObject.toString();
                        value.put(field.getName(), fieldValue);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return value;
    }

    /**
     * 向数据库添加数据
     * @param object 含有orm注解的数据库表对象
     * @return 成功为true，失败为false
     * @throws DatabaseExcuteNoResult
     * @throws SQLException
     */
    public boolean add(Object object){
        useDatabase();
        if (!isExisted(object))
            return add(getItem(object));
        return false;
    }


    private boolean delete(HashMap<String, String> value){
        useDatabase();
        return SQL.delete(connection, table.getSimpleName(), value);
    }

    public boolean delete(Object object){
        useDatabase();
        return delete(getItem(object));
    }

    private boolean update(HashMap<String,String> oldValue, HashMap<String,String> newValue){
        return SQL.update(connection,table.getSimpleName(), oldValue,newValue);
    }
    public boolean update(Object oldObject,Object newObject) {
        useDatabase();
        if (isExisted(oldObject))
            return update(getItem(oldObject),getItem(newObject));
        return false;
    }

    public boolean drop(){
        return SQL.dropTable(connection, table.getSimpleName());
    }

    public ResultSet select(HashMap<String,String> value)  {
        return SQL.select(connection,table.getSimpleName(),value);
    }

    public ResultSet select(Object object) {
        useDatabase();
        return select(getItem(object));
    }

    public boolean isExisted(Object object)  {
        boolean flag = false;
        try {
            flag = select(object).next();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return flag;
    }

    public void close(){
        try {
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}

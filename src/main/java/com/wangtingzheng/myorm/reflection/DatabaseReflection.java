package com.wangtingzheng.myorm.reflection;

import com.wangtingzheng.myorm.annotation.OrmDatabase;
import com.wangtingzheng.myorm.annotation.OrmTable;
import com.wangtingzheng.myorm.entity.DatabaseConnectionEntity;
import com.wangtingzheng.myorm.entity.DatabaseEntity;
import com.wangtingzheng.myorm.entity.TableEntity;
import com.wangtingzheng.myorm.exception.DatabaseNotFoundException;
import com.wangtingzheng.myorm.exception.TableClassNotFoundException;
import com.wangtingzheng.myorm.exception.TableItemNotFoundException;
import com.wangtingzheng.myorm.exception.TableNotFoundException;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @author WangTingZheng
 * @date 2020/7/8 15:55
 * @features
 */
public class DatabaseReflection {
    Class database;

    public DatabaseReflection(Class database) {
        this.database = database;
    }

    public DatabaseConnectionEntity toDatabaseConnectionEntity() throws DatabaseNotFoundException {
        if (database.isAnnotationPresent(OrmDatabase.class)){
            OrmDatabase ormDatabase = (OrmDatabase) database.getAnnotation(OrmDatabase.class);
            return new DatabaseConnectionEntity(ormDatabase.type(), ormDatabase.extendType(), ormDatabase.host(),ormDatabase.username(),ormDatabase.password(),ormDatabase.openDatabase(),ormDatabase.serverTimezone());
        }
        else {
            throw new DatabaseNotFoundException("Database annotation not found.", database);
        }
    }

    /**
     * 通过数据库类来获得数据库实体
     * 主要是填充数据库的表数据
     * 数据中包含表的类列表和表的信息列表
     * @return
     * @throws TableNotFoundException
     */
    public DatabaseEntity toDatabaseEntity() throws TableNotFoundException {
        DatabaseEntity  databaseEntity = new DatabaseEntity(database);
        for(Field field: database.getDeclaredFields()){
            field.setAccessible(true);
            if (field.isAnnotationPresent(OrmTable.class)){
                try {
                    databaseEntity.addTableEntity(new TableReflection(field.getType()).toTableEntity());
                    databaseEntity.addTableClass(field.getType());
                } catch (TableItemNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        if (databaseEntity.getSize() == 0){
            throw new TableNotFoundException("Table not found in this database class", database);
        }
        return databaseEntity;
    }

    public List<Class> getTableClass() throws TableClassNotFoundException {
        List<Class> clazz = new ArrayList<>();
        for (Field field:database.getDeclaredFields()){
            field.setAccessible(true);
            if(field.isAnnotationPresent(OrmTable.class)){
                clazz.add(field.getType());
            }
        }
        if (clazz.size() == 0){
            throw new TableClassNotFoundException("Table class not found in this database class", database);
        }
        return clazz;
    }
}

package src.com.ych.whimsy.mysql.operation;

import src.com.ych.whimsy.mysql.Field;
import src.com.ych.whimsy.mysql.LoadDatabase;
import src.com.ych.whimsy.mysql.Restriction;

import java.sql.SQLException;
import java.util.Objects;

/**
 *
 */
public class CreateTable implements Restriction {

    /**
     * 数据库对象
     */
    private LoadDatabase loadDatabase;

    /**
     * 通过传入一个数据库对象,来创建一个数据表对象
     *
     * @param loadDatabase 数据库对象
     * @NullPointerException 数据库对象为空了
     */
    public CreateTable (LoadDatabase loadDatabase) throws NullPointerException {
        if (Objects.isNull(loadDatabase)) {
            throw new NullPointerException("数据库对象为空");
        }
        this.loadDatabase = loadDatabase;
    }

    /**
     * 创建表的列定义
     */
    private Field[] fields;

    /**
     * 设置创建表时需要的字段
     *
     * @param fields
     */
    public void setFields (Field... fields) {
        if (Objects.isNull(fields) || fields.length == 0) {
            throw new NullPointerException("字段为空了");
        }
        this.fields = fields;
    }

    /**
     * 工具方法判断字段名是否为空和判断字段列表是否为空
     *
     * @param fieldName 字段名
     * @throws NullPointerException 字段名为空,或者字段列表为空
     */
    private void isNull (String fieldName) throws NullPointerException {
        // 判断字段名是否为空
        if (Objects.isNull(fieldName) || fieldName.isEmpty()) {
            throw new NullPointerException("字段名为空了");
        }
        // 判断字段列表是否为空
        if (Objects.isNull(fields) || fields.length == 0) {
            throw new NullPointerException("字段列表为空");
        }
    }

    /**
     * 根据传进来的字段名设置对应的数据库约束,采用的是列级约束语法
     *
     * @param fieldName   字段名
     * @param restriction 约束
     * @return 设置成功返回true, 设置失败返回fasle.
     * @throws SQLException 数据库约束异常
     */
    private boolean selectRestriction (String fieldName, String restriction) throws SQLException {
        // 遍历字段列表
        for (Field field : fields) {
            // 在遍历中查找对应的字段对象
            if (field.getName().equals(fieldName)) {
                // 根据传进来的约束设置指定的约束
                switch (restriction) {
                    case AUTO_INCREMENT:
                        field.setRestriction(AUTO_INCREMENT);
                        return true;
                    case NOT_NULL:
                        field.setRestriction(NOT_NULL);
                        return true;
                    case UNIQUE:
                        field.setRestriction(UNIQUE);
                        return true;
                    case PRIMARY_KEY:
                        field.setRestriction(PRIMARY_KEY);
                        return true;
                }
            }
        }
        return false;
    }

    /**
     * 根据字段名设置非空约束, 使用的是列级约束语法
     *
     * @param fieldName 字段名
     * @return 设置成功返回true, 设置失败返回false
     * @throws NullPointerException 字段名为空,或者字段列表为空
     */
    public boolean setNotNull (String fieldName) throws NullPointerException {
        //判断字段名和字段列表
        isNull(fieldName);
        boolean isSet = false;
        try {
            // 调用设置约束的方法
            isSet = selectRestriction(fieldName, NOT_NULL);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return isSet;
    }


    /**
     * 根据字段名设置自动增长约束,使用的是列级约束语法
     *
     * @param fieldName 字段名
     * @return 设置成功返回true, 设置失败返回false
     * @throws NullPointerException 字段名为空,或者字段列表为空
     */
    public boolean setAutoIncrement (String fieldName) throws NullPointerException {
        // 判断字段名和字段列表是否为空
        isNull(fieldName);
        boolean isSet = false;
        try {
            // 调用设置约束的方法
            isSet = selectRestriction(fieldName, AUTO_INCREMENT);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return isSet;
    }

    /**
     * 根据字段名设置唯一约束,使用的是列级约束语法
     *
     * @param fieldName 字段名
     * @return 设置成功返回true, 设置失败返回false
     * @throws NullPointerException 字段名为空,或者字段列表为空
     */
    public boolean setUnique (String fieldName) {
        // 判断字段名和字段列表是否为空
        isNull(fieldName);
        boolean isSet = false;
        try {
            // 调用设置约束的方法
            isSet = selectRestriction(fieldName, UNIQUE);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return isSet;
    }

    /**
     * 根据字段名设置主键约束,使用的是列级约束语法
     *
     * @param fieldName 字段名
     * @return 设置成功返回true, 设置失败返回false
     * @throws NullPointerException 字段名为空,或者字段列表为空
     */
    public boolean setPrimaryKey (String fieldName) {
        // 判断字段名和字段列表是否为空
        isNull(fieldName);
        boolean isSet = false;
        try {
            // 调用设置约束的方法
            isSet = selectRestriction(fieldName, PRIMARY_KEY);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return isSet;
    }


}

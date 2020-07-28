package src.com.ych.whimsy.mysql;

import java.sql.SQLException;
import java.util.Objects;

/**
 * 是该类可以创建一个数据表字段对象,该对象代表一个数据表字段.<br/>
 * 该对象有四个实例变量分别代表字段名,字段类型,字段值,字段默认值.<br/>
 * 我们有两种方式生成字段对象:<br/>
 * 1.我们可以通过该类三个重载的构造器,分别传入不同的参数创建不同的字段对象<br/>
 * 例如:Fileld field=new Field("id",Field.Type.INT.getType(8),"12345678",Field.DefaultValue.XXX);<br/>
 * 2.可以通过选着合适的构造器,在通过一些列set方法来设置字段名等属性值
 */
public class Field implements Restriction {

    /**
     * 字段名,也就是数据库表的列定义名称
     */
    private String name;
    /**
     * 字段类型,也就是数据库表的列定义的类型
     */
    private Type type;

    /**
     * 字段对应的值,也就是数据库表的列定义的值
     */
    private String value;

    /**
     * 约束
     */
    private String restriction;

    /**
     * 默认构造器
     */
    public Field ( ) {
    }

    /**
     * 仅使用 字段名和字段类型创建一个字段
     *
     * @param name 字段名
     * @param type 字段类型
     * @throws NullPointerException 抛出该异常时代表字段名或者字段类型为空了
     */
    public Field (String name, Type type) throws NullPointerException {
        // 设置字段名
        this.setName(name);
        // 设置字段类型
        this.setType(type);
    }

    /**
     * 使用字段名、字段类型、和字段值创建一个字段实例
     *
     * @param name  字段名
     * @param type  字段类型
     * @param value 字段值
     * @throws NullPointerException 抛出该异常时代表字段名或者字段类型为空了
     */
    public Field (String name, Type type, String value) throws NullPointerException {
        // 调用重载构造器来初始化前两的参数对应的实例变量
        this(name, type);
        // 设置字段值
        this.setValue(value);
    }


    /**
     * 给字段对象设置字段名
     *
     * @param name 字段名
     * @throws NullPointerException 抛出该异常标识字段名为空了
     */
    public void setName (String name) throws NullPointerException {
        // 如果字段名为空抛出异常
        if (Objects.isNull(name) || name.isEmpty()) {
            throw new NullPointerException("字段名为空了");
        }
        this.name = name;
    }

    /**
     * 设置字段类型
     *
     * @param type 字段类型
     * @throws NullPointerException 抛出该异常代表字段类型为空了
     */
    public void setType (Type type) throws NullPointerException {
        // 如果字段类型为空抛出异常
        if (Objects.isNull(type)) {
            throw new NullPointerException("l字段类型为空了");
        }
        this.type = type;
    }

    /**
     * 设置字段值
     *
     * @param value 字段值
     */
    public void setValue (String value) {
        // 如果字段值为null将其换成""值
        if (Objects.isNull(value)) {
            value = "";
        }
        this.value = value;
    }

    /**
     * 设置数据库约束,在此设置的邀约束将使用列级约束语法进行约束
     *
     * @param restriction 约束
     * @throws SQLException 错误的数据库约束
     */
    public void setRestriction (String restriction) throws SQLException {
        switch (restriction) {
            case AUTO_INCREMENT:
                this.restriction = AUTO_INCREMENT;
                break;
            case NOT_NULL:
                this.restriction = NOT_NULL;
                break;
            case UNIQUE:
                this.restriction = UNIQUE;
                break;
            case PRIMARY_KEY:
                this.restriction = PRIMARY_KEY;
                break;
            default:
                throw new SQLException("错误的数据库约束");
        }
    }

    /**
     * 获取字段对象的字段名
     *
     * @return 返回字段对象的字段名
     */
    public String getName ( ) {
        return name;
    }

    /**
     * 获取字段对象的字段类型
     *
     * @return 返回字段类型
     */
    public Type getType ( ) {
        return type;
    }

    /**
     * 获取字段对象的字段值
     *
     * @return 返回字段值
     */
    public String getValue ( ) {
        return value;
    }


    /**
     * 获取字段默认值
     *
     * @return 默认值
     */
    public String getDefault ( ) {
        return this.type.getDefaultValue();
    }


    /**
     * 获取 字段名 字段类型 字段默认值的拼接的字符串
     *
     * @return 返回获取 字段名 字段类型 字段默认值的拼接的字符串
     */
    @Override
    public String toString ( ) {
        return Objects.isNull(restriction) ? this.name + " " + type : this.name + " " + type + " " + restriction;
    }
}
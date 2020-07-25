package src.com.ych.whimsy.mysql;

import com.mysql.cj.exceptions.NumberOutOfRange;

import java.util.Objects;

/**
 * 是该类可以创建一个数据表字段对象,该对象代表一个数据表字段.<br/>
 * 该对象有四个实例变量分别代表字段名,字段类型,字段值,字段默认值.<br/>
 * 我们有两种方式生成字段对象:<br/>
 * 1.我们可以通过该类三个重载的构造器,分别传入不同的参数创建不同的字段对象<br/>
 * 例如:Fileld field=new Field("id",Field.Type.INT.getType(8),"12345678",Field.DefaultValue.XXX);<br/>
 * 2.可以通过选着合适的构造器,在通过一些列set方法来设置字段名等属性值
 */
public class Field {

    /**
     * 该类是一个枚举类，每个枚举实例代表一个数据类型，每个数据类型实例可以调用它的<br/>
     * getType(...)方法获取类型并指定该类型的长度或者宽度
     * <p>目前支持类型只有两个:int、varchar<p/>
     */
    public static enum Type {
        /**
         * int 类型<br/>
         * 如果没有使用getType方法获取类型并指定宽度则使用该实例的<br/>
         * 默认int类型,它的宽度为11位
         */
        INT("int") {
            /**
             * 获取INT类型并设置int类型的宽度，不指定宽度或者宽度指定为零会使用默认宽度
             * @param width int类型的宽度
             * @return 返回设置过宽度的int类型
             */
            @Override
            public Type getType (int width) {
                // 返回当前枚举实例
                // 如果宽度为0了说明不指定宽度
                if (width != 0 && !(width < 0) && width <= 11) {
                    INT.type = INT.type + "(" + width + ")";
                }
                return this;
            }
        },
        /**
         * 可变长度的字符串 长度区间:0~65535<br/>
         * 如果该实例没有使用getType获取类型并指定长度,<br/>
         * 使用该实例默认的255长度.
         */
        VARCHAR("varchar(255)") {
            /**
             * 获取VARCHAR类型并指定长度
             * @param length varchar长度
             * @return 返回设置过长度的VARCHAR
             */
            @Override
            public Type getType (int length) throws NumberOutOfRange {
                // 判断length的取值范围,范围超出抛出异常
                if (length <= 0 || length > 65535) {
                    throw new NumberOutOfRange("");
                }
                VARCHAR.type = "varchar(" + length + ")";
                return this;
            }
        };
        /**
         * 数据类型
         */
        private String type;

        /**
         * 初始化数据类型
         *
         * @param type 类型名
         */
        Type (String type) {
            this.type = type;
        }

        /**
         * 该方法用来给String类型指定长度,int类型的该方法没有任何作用
         *
         * @param i 长度或者宽度
         */
        public abstract Type getType (int i);

        /**
         * 返回默认字段类型信息
         *
         * @return 返回默认字段类型
         */
        public String toString ( ) {
            return this.type;
        }
    }

    /**
     * 该类的实例代表每个字段的默认值，暂时未对默认值进行初始化，后续会慢慢完善
     */
    public enum DefaultValeu {

        NULL("null") {
            @Override
            public DefaultValeu get (String value) {
                if (Objects.isNull(value) || value.isEmpty()) {
                    throw new NullPointerException("自定义默认值为空了");
                }
                NULL.defaultValue = value;
                return NULL;
            }
        };

        /**
         * 默认值
         */
        private String defaultValue;

        /**
         * 在创建枚举实例时同时初始化默认值。
         *
         * @param defaultValue 默认值
         */
        DefaultValeu (String defaultValue) {
            this.defaultValue = defaultValue;
        }

        /**
         * 不满足默认的默认值,用来设置自定义默认值
         *
         * @param value 默认值
         * @return 返回设置了自定义默认值的默认值
         */
        public abstract DefaultValeu get (String value);

        /**
         * 在输出或者该对象的实例在与其他字符串拼接时返回该对象的默认值
         *
         * @return 返回默认值
         */
        @Override
        public String toString ( ) {
            return this.defaultValue;
        }
    }

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
     * 默认值
     */
    private DefaultValeu defaultValue;

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
     * 根据字段名、字段类型、字段字、字段默认值来创建一个字段实例
     *
     * @param name         字段名
     * @param type         字段类型
     * @param value        字段值
     * @param defaultValue 字段默认值
     * @throws NullPointerException 抛出该异常时代表字段名、者字段类型或者字段默认值为空了
     */
    public Field (String name, Type type, String value, DefaultValeu defaultValue) throws NullPointerException {
        // 调用重载构造器
        this(name, type, value);
        // 设置默认值
        this.setDefaultValue(defaultValue);
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
     * 设置字段默认值
     *
     * @param defaultValue 默认值
     * @throws NullPointerException 抛出该异常标识默认值对象为空了
     */
    public void setDefaultValue (DefaultValeu defaultValue) throws NullPointerException {
        // 如果默认值为空将抛出异常
        if (Objects.isNull(defaultValue)) {
            throw new NullPointerException("字段默认值为空了");
        }
        this.defaultValue = defaultValue;
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
     * 获取字段对象的默认值
     *
     * @return 返回字段对象的字段值
     */
    public DefaultValeu getDefaultValue ( ) {
        return defaultValue;
    }

    /**
     * 获取 字段名 字段类型 字段默认值的拼接的字符串
     *
     * @return 返回获取 字段名 字段类型 字段默认值的拼接的字符串
     */
    @Override
    public String toString ( ) {
        // 判断默认值对象是否为空,如果为空则不拼接默认值
        if (Objects.isNull(defaultValue)) {
            return this.name + " " + type;
        }
        return this.name + " " + type + defaultValue;
    }
}
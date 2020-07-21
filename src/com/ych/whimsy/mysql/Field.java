package src.com.ych.whimsy.mysql;

/**
 * 字段类,该类的每个实例代表一个MySQL数据的列
 */
public class Field {

    /**
     * type变量的值,int类型标识
     */
    public static final String INT = "int";
    /**
     * type变量的值,String类型标识
     */
    public static final String STRING_255 = "varchar(255)";

    /**
     * 文件类型16MB
     */
    public static final String MEDIUMBLOB = "mediumblob";

    /**
     * 主键约束
     */
    public static final int PRIMARY_KEY = 1;

    /**
     * 字段名
     */
    private String name;
    /**
     * 字段类型
     */
    private String type;

    /**
     * 字段对应的值
     */
    private String vlue;

    /**
     * 数据库约束
     */
    private int key;


    /**
     * 创建一个字段对象
     *
     * @param name 字段名
     * @param type 字段类型,该类为该参数提供了两个默认常量值Field.INT和Field.STRING255
     * @param vlue 字段值
     */
    public Field (String name, String type, String vlue) {
        this(name, type);
        this.vlue = vlue;
    }

    /**
     * 创建一个字段对象
     *
     * @param name 字段名
     * @param type 字段类型,该类为该参数提供了两个默认常量值Field.INT和Field.STRING255
     */
    public Field (String name, String type) {
        this.name = name;
        this.type = type;
    }

    /**
     * 设置 key 属性的值<br/>
     * 你能使用 getKey() 获取 key 的属性值
     *
     * @param key 请描述--
     */
    public void setKey (int key) {
        this.key = key;
    }

    /**
     * 获取字段值
     *
     * @return 返回字段值
     */
    public String getValue ( ) {
        return vlue;
    }

    /**
     * 获取字段类型
     *
     * @return 返回字段类型
     */
    public String getType ( ) {
        return type;
    }

    /**
     * 获取字段名
     *
     * @return 返回字段名
     */
    public String getName ( ) {
        return name;
    }

    /**
     * 获取 key(数据库约束) 的属性值值
     *
     * @return 返回 key(数据库约束) 的值
     */
    public String getKey ( ) {
        switch (key) {
            case PRIMARY_KEY:
                return "primary key";
        }
        return "";
    }

    /**
     * @return 返回字段名和类型的拼接字符串
     */
    @Override
    public String toString ( ) {

        switch (key) {
            case PRIMARY_KEY:
                return name + " " + type + " " + getKey();
        }
        return name + " " + type;
    }
}
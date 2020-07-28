package src.com.ych.whimsy.mysql.temporary;

import src.com.ych.whimsy.mysql.Field;
import src.com.ych.whimsy.mysql.LoadDatabase;

import java.awt.*;
import java.util.Objects;

/**
 * 一些工具内部类的通用属性
 */
public abstract class Property {
    /**
     * 当前类的类名
     */
    private final String propertyName = Property.class.getSimpleName();
    /**
     * 当前类的信息
     */
    private final String propertyLog = LoadDatabase.loadDatabaseName + ":\n" + propertyName + ":\n";
    /**
     * 数据库名
     */
    protected String databaseName;
    /**
     * 表名
     */
    protected String tableName;

    /**
     * 为哪些数据表的列添加数据,删除数据,修改数据
     */
    protected Field[] fields;

    /**
     * 为哪些数据表的列添加数据,删除数据,修改数据
     */
    protected String columns;
    /**
     * 为哪些数据表的列添加数据,删除数据,修改数据
     */
    protected String[] columnsArrays;
    /**
     * 每个数据列对应的值
     */
    protected String values;
    /**
     * 每个数据列对应的值
     */
    protected String[] valuesArrays;

    protected LoadDatabase loadDatabase;

    public Property (LoadDatabase loadDatabase) {
        this.loadDatabase = loadDatabase;
    }

    /**
     * 设置数据库,也就是进入那个数据库
     *
     * @param databaseName 数据库名
     * @return 返回false设置失败, 返回true设置成功
     */
    public boolean setDatabase (String databaseName) {
        if (Objects.isNull(databaseName) || databaseName.isEmpty()) {
            System.err.println(propertyLog + "setDatabaseName (String databaseName): 数据库名不能为空,请重新设置");
            return false;
        }
        this.databaseName = databaseName;
        // 使用哪个数据库
        String sql_user = "use " + databaseName + ";";
        // 执行使用哪个数据库指令
        return loadDatabase.executeUpdate(sql_user, "ddl");
    }

    /**
     * 设置表名,也就是给哪个数据表添加数据,删除数据,修改数据
     *
     * @param tableName 表名
     * @return 返回false设置失败, 返回true设置成功
     */
    public boolean setTable (String tableName) {
        if (Objects.isNull(tableName) || tableName.isEmpty()) {
            System.err.println(propertyLog + "setTableName (String tableName): 表名不能为空, 请重新设置");
            return false;
        }
        this.tableName = tableName;
        return true;
    }

    /**
     * 设置字段集
     *
     * @param fields 需要插入值的列,列的顺序,要和数据表的列顺序对应,如果不想为某列插入值该列传入null值,然后在setFieldValues()中对应顺序的位置也传入null值
     * @return 返回false设置失败, 返回true设置成功
     */
    public boolean setFields (Field... fields) {
        if (Objects.isNull(fields) || fields.length == 0) {
            System.err.println(propertyLog + "setFields (Field... fields): 字段不能为空,请重新设置");
            return false;
        }
        this.fields = fields;
//            StringBuilder columns = new StringBuilder();
//            for (Field field : fields) {
//                columns.append(field.getName() + " ");
//            }
//            this.columns = columns.toString();
        return true;
    }

    /**
     * 要为那些列插入值
     *
     * @param columns 需要插入值的列,列的顺序,要和数据表的列顺序对应,如果不想为某列插入值该列传入null值,然后在setFieldValues()中对应顺序的位置也传入null值
     * @return 返回false设置失败, 返回true设置成功
     */
    public boolean setColumns (String... columns) {
        if (Objects.isNull(columns) || columns.length == 0) {
            System.err.println(propertyLog + "setFields (Field... fields): 字段不能为空,请重新设置");
            return false;
        }
        StringBuilder columns2 = new StringBuilder();
        int num = 1;
        for (String column : columns) {
            columns2.append(column + " ");
            if (num == columns.length) {
                continue;
            }
            columns2.append(",");
            num++;
        }
        this.columns = columns2.toString();
        this.columnsArrays = columns;
        return true;
    }


    /**
     * 给setColumns (String... columns)方法设置每一列插入数据,如果不想为某一列设置插入数据,传入空值;数据插入的顺序和setColumns (String... columns)插入的列的顺序一致
     *
     * @param values 每一列的要插入的值,如果数据表的值是String类型这样添加 " \'xxx\' ",数值类型的 "100"
     * @return 返回false设失败, 返回true设置成功
     */
    public boolean setValues (String... values) {
        if (Objects.isNull(values) || values.length == 0) {
            System.err.println(propertyLog + "setValue (String.. values): 要插入的数据不能为空,请重新输入");
            return false;
        }
        StringBuilder vs = new StringBuilder();
        int num = 1;
        for (String value : values) {
            vs.append(value + " ");
            if (num == values.length) {
                continue;
            }
            vs.append(",");
            num++;
        }
        this.values = vs.toString();
        this.valuesArrays = values;
        return true;
    }


    public abstract boolean commit ( );

    public abstract boolean commitFields ( );
}
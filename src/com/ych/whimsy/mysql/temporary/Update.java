package src.com.ych.whimsy.mysql.temporary;

import src.com.ych.whimsy.mysql.Field;
import src.com.ych.whimsy.mysql.LoadDatabase;

import java.util.Objects;

import static src.com.ych.whimsy.mysql.LoadDatabase.loadDatabaseName;

/**
 * Uptade 类是LoadDatabase类的内部类,该类的作用是修改数据表中的数据
 */
public class Update extends Property {
    /**
     * 当前类的类名
     */
    private final String updateName = Update.class.getSimpleName();
    /**
     * 当前类的信息
     */
    private final String updateLog = loadDatabaseName + ":\n" + updateName + ":\n";

    /**
     * where限定语句
     */
    private String where;

    public Update (LoadDatabase loadDatabase) {
        super(loadDatabase);
    }

    /**
     * 私有化构造器
     */


    /**
     * 设置Where限定用于给指定列更新信息
     *
     * @param column  指定列名
     * @param compare 判断模式 -1小于;0等于;1大于,-10小于等于,10大于等于
     * @param value   列对应的值
     * @return 返回false设置失败, 返回true设置成功
     */
    public boolean setWhere (String column, int compare, String value) {

        if (Objects.isNull(column) || column.isEmpty()) {
            System.err.println(updateLog + "setWhere (String column, int compare, String value) : 列定义不能为空,请重新输入");
            return false;
        }
        if (Objects.isNull(value) || value.isEmpty()) {
            System.err.println(updateLog + "setWhere (String column, int compare, String value) : 列定义的值不能为空,请重新输入");
            return false;
        }
        switch (compare) {
            case -1:
                this.where = " where " + column + " < " + value;
                break;
            case 0:
                this.where = " where " + column + " = " + value;
                break;
            case 1:
                this.where = " where " + column + " > " + value;
                break;
            case -10:
                this.where = " where " + column + " <= " + value;
                break;
            case 10:
                this.where = " where " + column + " >=" + value;
                break;
            default: {
                System.err.println(updateLog + "setWhere (String column, int compare, String value) : 比较模式不能为空");
                return false;
            }
        }
        return true;
    }

    /**
     * 设置Where限定用于给指定列更新信息
     *
     * @param field   指定字段
     * @param compare 判断模式 -1小于;0等于;1大于,-10小于等于,10大于等于
     * @return 返回false设置失败, 返回true设置成功
     */
    public boolean setWhere (Field field, int compare) {
        if (Objects.isNull(field)) {
            System.err.println(updateLog + "setWhere (Field field) : 字段列定义不能为空,请重新输入");
            return false;
        }
        // 取出字段值
        String v = null;
        switch (field.getType()) {
//                case Field.INT:
//                    v = field.getValue();
//                    break;
//                case Field.STRING_255:
//                    v = "\'" + field.getValue() + "\'";
//                    break;
        }
        // 拼接where限定子句
        return setWhere(field.getName(), compare, v);
    }

    /**
     * 提交修改
     *
     * @return 返回false提交失败, 返回true提交成功
     */
    @Override
    public boolean commit ( ) {
        if (Objects.isNull(columnsArrays) || columnsArrays.length == 0) {
            System.err.println(updateLog + "isNull ( ) : 列定义数组不能为空,请重新输入");
            return false;
        }
        if (Objects.isNull(valuesArrays) || valuesArrays.length == 0) {
            System.err.println(updateLog + "isNull ( ) : 列定义的值数组不能为空,请重新输入");
            return false;
        }
        // 拼接更新语句如下格式
        // update table_name set column1=value1[,column2=value2]... [where condition];
        StringBuilder sql_update_set = new StringBuilder();
        for (int i = 0; i < columnsArrays.length; i++) {
            sql_update_set.append(columnsArrays[i] + " = " + valuesArrays[i]);
            if (i == columnsArrays.length - 1) {
                continue;
            }
            sql_update_set.append(" , ");
        }
        String sql_update = "update " + tableName + " set " + sql_update_set + where + ";";
        // 执行更新语句
        return loadDatabase.executeUpdate(sql_update, "dml");
    }

    /**
     * 提交修改
     *
     * @return 返回false提交失败, 返回true提交成功
     */
    @Override
    public boolean commitFields ( ) {
        if (Objects.isNull(fields) || fields.length == 0) {
            System.err.println(updateLog + "commitFields ( ) : 列定义数组不能为空,请重新输入");
            return true;
        }
        // 拼接 insert into 插入语句
        String[] columns = new String[fields.length];
        String[] values = new String[fields.length];
        // 遍历Field集
        int index = 0;
        for (Field field : fields) {
            columns[index] = field.getName();
            switch (field.getType()) {
//                    case Field.Type.INT:
//                        values[index] = field.getValue() + " ";
//                        break;
//                    case Field.Type.VARCHAR:
//                        values[index] = "\'" + field.getValue() + "\'";
//                        break;
            }
            index++;
        }
        // 设置列定义
        setColumns(columns);
        // 设置列定义要修改的值
        setValues(values);
        return commit();
    }
}
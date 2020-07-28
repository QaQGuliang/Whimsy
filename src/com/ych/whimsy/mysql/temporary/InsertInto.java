package src.com.ych.whimsy.mysql.temporary;

import src.com.ych.whimsy.mysql.LoadDatabase;

import java.util.Objects;

import static src.com.ych.whimsy.mysql.LoadDatabase.loadDatabaseName;

/**
 * InsertInto  类是LoadDatabase的内部类,该类的实例用户向指定数据表中添加数据
 */
public class InsertInto extends Property {
    /**
     * 当前类的类名
     */
    private final String insertintoName = InsertInto.class.getSimpleName();
    /**
     * 当前类的信息
     */
    private final String insertintoLog = loadDatabaseName + ":\n" + insertintoName + ":\n";
    /**
     * 插入数据语法
     */
    private static final String SQL_INSER_INTO = "insert into ";

    public InsertInto (LoadDatabase loadDatabase) {
        super(loadDatabase);
    }


    /**
     * 将要添加的数据提交到数据库
     *
     * @return 返回false提交失败, 返回true提交成功.
     */
    @Override
    public boolean commit ( ) {
        // 判断插入的数据是否为空,为空结束插入并提示
        if (Objects.isNull(values) || values.isEmpty()) {
            System.err.println(insertintoLog + "commot ( ): 要插入的数据不能为空,请设置要插入的数据");
            return false;
        }
        // 拼接插入语句
        String sql_insert_into = SQL_INSER_INTO + tableName + " (" + columns + ") values(" + values + ");";
        return loadDatabase.executeUpdate(sql_insert_into, "dml");
    }

    /**
     * 将要添加的数据提交到数据库
     *
     * @return 返回false提交失败, 返回true提交成功.
     */
    @Override
    public boolean commitFields ( ) {
        // 判断插入的数据是否为空,为空结束插入并提示
        if (Objects.isNull(fields) || fields.length == 0) {
            System.err.println(insertintoLog + "commotFields ( ): 要插入的字段集不能为空,请设置字段集");
            return false;
        }
        // 拼接 insert into 插入语句
        StringBuilder columns = new StringBuilder();
        StringBuilder values = new StringBuilder();
        // 遍历Field集
        int num = 1;
//            for (Field field : fields) {
//                columns.append(field.getName());
//                switch (field.getType()) {
//                    case Field.INT:
//                        values.append(field.getValue() + " ");
//                        break;
//                    case Field.STRING_255:
//                        values.append("\'" + field.getValue() + "\'");
//                        break;
//                }
//                if (num == fields.length) {
//                    continue;
//                }
//                columns.append(",");
//                values.append(",");
//                num++;
//            }
        // 拼接插入语句
        String sql_insert_into = SQL_INSER_INTO + tableName + " (" + columns + ") values(" + values + ");";
        return loadDatabase.executeUpdate(sql_insert_into, "dml");
    }
}
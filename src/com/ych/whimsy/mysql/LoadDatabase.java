package src.com.ych.whimsy.mysql;

import src.com.ych.whimsy.log.Log;
import src.com.ych.whimsy.mysql.temporary.Delete;
import src.com.ych.whimsy.mysql.temporary.InsertInto;
import src.com.ych.whimsy.mysql.temporary.Update;

import java.sql.*;
import java.util.Objects;

/**
 * 该类用于加载用户数据库<br/>
 * 1.拿到指定数据库的Connection连接.<br/>
 * 2.通过Connection连接执行DDL语句,创建用户数据表,数据表存在则不创建.<br/>
 * 3.提供操作用户数据表的方法.
 */
public class LoadDatabase {
    /**
     * 本类类名
     */
    public static final String loadDatabaseName = "--> " + LoadDatabase.class.getSimpleName();
    /**
     * 数据库驱动名,默认驱动名"com.mysql.cj.jdbc.Driver"
     */
    private static String driverName = "com.mysql.cj.jdbc.Driver";

    /**
     * 数据库URL
     */
    private String sqlUrl;

    /**
     * 数据库用户名,默认用户名:root
     */
    private String userName;

    /**
     * 数据库用户名密码
     */
    private String password;

    /**
     * 数据库Connection连接对象
     */
    private Connection connection;

    /**
     * 是否准备,变量代表该实例的数据库的Connection是否连接成功<br/>
     * 为true标识连接成功
     */
    private boolean isAlready = false;

    /**
     * Statement对象,用来执行DDL语句或者DML语句,该变量的值由getStatement ( )方法获取
     */
    private Statement statement;


    /**
     * LoadDatabase的默认构造器
     */
    public LoadDatabase ( ) {
    }

    /**
     * 初始化MySQL数据库驱动
     *
     * @param driverName MySQL数据库驱动名
     * @throws ClassNotFoundException 请检查驱动名:<br/>
     *                                1.驱动名无误说明驱动包未加入项目.<br/>
     *                                2.驱动名有误说明未找到该驱动.<br/>
     */
    private LoadDatabase (String driverName) throws ClassNotFoundException {
        // 初始化MySQL数据库驱动
        if (!initDriver(driverName)) {
            // MySQL数据库驱动初始化失败抛出异常
            throw new ClassNotFoundException("请检查驱动名:\n1.驱动名无误说明驱动包未加入项目.\n2.驱动名有误说明未找到该驱动.");
        }
    }

    /**
     * 创建一个LoadDatabase对象,并通过传入一个完整的MySQL数据库URL来进行连接MySQL数据库
     *
     * @param driverName MySQL数据库驱动名
     * @param sqlUrl     完整的MySQL数据库URL,例如:jdbc:mysql://localhost/mysql?user=root&password=ychQAQ&serverTimezone=UTC
     * @throws ClassNotFoundException 请检查驱动名:<br/>
     *                                1.驱动名无误说明驱动包未加入项目.<br/>
     *                                2.驱动名有误说明未找到该驱动.<br/>
     * @throws SQLException           完整的MySQL数据库URL有误
     */
    public LoadDatabase (String driverName, String sqlUrl) throws ClassNotFoundException, SQLException {
        // 初始化MySQL数据驱动
        this(driverName);
        // MySQL数据库驱动初始化,开始创建MySQL数据库连接
        if (!initConnection(sqlUrl)) {
            throw new SQLException("完整的MySQL数据库URL有误");
        }
    }

    /**
     * 创建一个LoadDatabase对象,并通过传入一个提供一个MySQL数据库的URL来进行连接MySQL数据库
     *
     * @param driverName 数据库驱动名
     * @param sqlUrl     MySQL数据库URL,例如:jdbc:mysql://localhost/mysql?serverTimezone=UTC
     *                   * @param userName 数据库用户名
     *                   * @param password 数据库用户名密码
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public LoadDatabase (String driverName, String sqlUrl, String userName, String password) throws SQLException, ClassNotFoundException {
        // 初始化MySQL数据驱动
        this(driverName);
        // 初始化MySQL数据库连接
        if (!initConnection(sqlUrl, userName, password)) {
            throw new SQLException("数据库URL有误或者数据库用户名/密码有误");
        }
        this.sqlUrl = sqlUrl;
        this.userName = userName;
        this.password = password;
    }

    /**
     * 设置MySQL数据库URL
     *
     * @param sqlUrl MySQL数据库URL
     * @throws NullPointerException 传入的数据库连接为null或者为""了
     */
    public void setSqlUrl (String sqlUrl) throws NullPointerException {
        if (Objects.isNull(sqlUrl) || sqlUrl.isEmpty()) {
            throw new NullPointerException("MySQL数据库URL为空了.\n");
        }
        this.sqlUrl = sqlUrl;
    }

    /**
     * 设置数据库用户名
     *
     * @param userName 数据库用户名
     * @throws NullPointerException 传入的数据库用户名为null或者为""了
     */
    public void setUserName (String userName) throws NullPointerException {
        if (Objects.isNull(userName) || userName.isEmpty()) {
            throw new NullPointerException("数据库用户名为空了.\n");
        }
        this.userName = userName;
    }

    /**
     * 设置数据库用户密码
     *
     * @param password 数据库用户密码
     * @throws NullPointerException 传入的数据库用户密码为null或者为""了
     */
    public void setPassword (String password) throws NullPointerException {
        if (Objects.isNull(password) || password.isEmpty()) {
            throw new NullPointerException("数据库密码为空了.\n");
        }
        this.password = password;
    }


    /**
     * 初始化MySQL数据库驱动
     *
     * @param driverName MySQL数据库驱动名
     * @return 初始化成功返回true, 初始化失败返回false.
     */
    public boolean initDriver (String driverName) {
        try {
            // 初始化驱动名
            this.driverName = driverName;
            // 加载驱动
            Class<?> aClass = Class.forName(driverName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.err.println(loadDatabaseName + "\n--> initDriver (String driverName): 驱动名有误 -> MySQL数据库驱动加载失败");
            return false;
        }
        return true;
    }

    /**
     * 通过一个完整的MySQL数据库URL来进行连接MySQL数据库,完整的MySQL数据库URL是拼接了用户名和密码的URL
     *
     * @param sqlUrl 完整的MySQL数据库URL,例如:jdbc:mysql://localhost/mysql?user=root&password=ychQAQ&serverTimezone=UTC
     * @return 连接成功返回true, 连接失败返回false.
     */
    public boolean initConnection (String sqlUrl) {
        try {
            // 创建MySQL数据库Connection连接
            connection = DriverManager.getConnection(sqlUrl);
        } catch (SQLException throwables) {
            // 捕获异常出现异常说明传入的数据有误
            System.err.println(loadDatabaseName + "\n--> initConnection(String sqlUrl): \n完整的MySQL数据库URL有误-> MySQL数据库Connection连接对象初始化失败");
            return false;
        }
        isAlready = true;
        return true;
    }

    /**
     * 提供一个MySQL数据库的URL来进行连接MySQL数据库
     *
     * @param url      MySQL数据库URL,例如:jdbc:mysql://localhost/mysql?serverTimezone=UTC
     * @param userName 数据库用户名
     * @param password 数据库用户名密码
     * @return 连接成功返回true, 连接失败返回false
     */
    public boolean initConnection (String url, String userName, String password) {
        try {
            // 创建MySQL数据库Connection连接
            connection = DriverManager.getConnection(url, userName, password);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            // 捕获异常出现异常说明传入的数据有误
            System.err.println(loadDatabaseName + "\n--> initConnection(String url, String userName, String password): \nMySQL数据库URL有误或者数据库用户名/密码有误 -> MySQL数据库Connection连接对象初始化失败");
            return false;
        }
        isAlready = true;
        return true;
    }


    /**
     * 根据LoadDatabase对象的setSqlUrl (String sqlUrl)、setUserName(String userName)、setPassword(String password)方法设置的<br/>
     * MySQL数据库URL、数据库用户名、数据库用户名密码，来连接数据库
     *
     * @return 连接数据库成功返回true, 连接失败返回false
     */
    public boolean initConnection ( ) {
        try {
            // 创建MySQL数据库Connection连接
            connection = DriverManager.getConnection(sqlUrl, userName, password);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            // 捕获异常出现异常说明传入的数据有误
            System.err.println(loadDatabaseName + "\n--> initConnection():\n MySQL数据库URL有误或者数据库用户名/密码有误 -> MySQL数据库Connection连接对象初始化失败");
            return false;
        }
        isAlready = true;
        return true;
    }

//--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * 使用那个数据库,也就是我们接下来要操作那个数据库里的数据
     *
     * @param dbName 数据库名
     * @return 返回false使用失败, 返回false需要考录两种情况1.Connection未连接或者数据库使用失败; 返回true使用成功
     */
    public boolean useDatabase (String dbName) {
        // 判断是否Connection是否连接
        if (!isAlready) {
            // 没有连接直接返回
            return false;
        }
        // 判断数据库名是否为空
        if (Objects.isNull(dbName) || dbName.isEmpty()) {
            throw new NullPointerException("数据库名为空了");
        }
        // 拼接DDL语句
        String sqlUseDdta = "use " + dbName + ";";
        return executeUpdate(sqlUseDdta, "ddl");
    }

    /**
     * 获取Statement对象
     *
     * @return Statement对象
     * @throws SQLException 1.LoadDatabase实例的Connection未连接数据库<br/>
     *                      2.数据库访问错误,在获取Statement对象时
     */
    public Statement getStatement ( ) throws SQLException {
        // 判断是否Connection是否连接
        if (!isAlready) {
            throw new SQLException("LoadDatabase实例的Connection未连接数据库");
        }
        if (Objects.isNull(statement)) {
            // statement为空通过Connection对象创建一个Statement对象
            statement = connection.createStatement();
        }
        // 不为空直接返回statement
        return statement;
    }

    /**
     * 该方法用户执行DDL语句
     *
     * @param sql 要执行的DDL语句
     * @return 返回false执行失败, 返回true执行成功
     */
    public boolean executeUpdate (String sql, String ddlOrDml) {
        try {
            int i = this.getStatement().executeUpdate(sql);
            if (i == 0 && "ddl".equals(ddlOrDml)) {
                return true;
            }
            if (i == 1 && "dml".equals(ddlOrDml)) {
                return true;
            }
        } catch (SQLException throwables) {
            System.err.println(loadDatabaseName + "--> executeSQL (String ddl): SQL_DDL_Or_DML指令 \"" + sql + "\" 执行过程出现异常,请检查该指令是否有误,或者检查相关的类或者对象是否出现问题");
            return false;
        }
        return false;
    }

    //****************************************************************************************************************************************************************************************************************************************


    /**
     * 创建表的方法
     *
     * @param tableName 表名 .
     * @param fields    列定义,该参数是可变参数说明可以是多个列定义.
     * @return 返回false创建失败, 返回true创建成功.
     */
    public boolean createTable (String tableName, Field... fields) {
        // 拼接SQL语句
        StringBuffer sql = new StringBuffer();
        // 创建表语法
        sql.append("create table ");
        // 判断是否创建过
        sql.append("if not exists ");
        // 表名
        sql.append(tableName);
        //拼接列定义
        sql.append("(");
        for (int i = 0; i < fields.length; i++) {
            // 将每一个列定义拼接上去
            sql.append(fields[i]);
            // 拼接最后一个列定义时不需要拼接 ","
            if (i == fields.length - 1) {
                continue;
            }
            // 每个列定义之间用 "," 隔开
            sql.append(",");
        }
        sql.append(");");
        // 执行sql语句
        return executeUpdate(sql.toString(), "ddl");
    }

    /**
     * 创建表的方法
     *
     * @param tableName 表名 .
     * @param fields    列定义,该参数是可变参数说明可以是多个列定义.
     * @return 返回false创建失败, 返回true创建成功.
     */
    public boolean createTableKey (String tableName, Field... fields) {
        // 拼接SQL语句
        StringBuffer sql = new StringBuffer();
        // 创建表语法
        sql.append("create table ");
        // 判断是否创建过
        sql.append("if not exists ");
        // 表名
        sql.append(tableName);
        //拼接列定义
        sql.append("(");
        for (int i = 0; i < fields.length; i++) {
            // 将每一个列定义拼接上去
            Field field = fields[i];
//            if (Objects.isNull(field.getKey()) || field.getKey().isEmpty()) {
//                sql.append(field.getName() + " " + field.getType());
//            } else {
//                sql.append(field.getName() + " " + field.getType() + " " + field.getKey());
//            }
            // 拼接最后一个列定义时不需要拼接 ","
            if (i == fields.length - 1) {
                continue;
            }
            // 每个列定义之间用 "," 隔开
            sql.append(",");
        }
        sql.append(");");
        // 执行sql语句
        return executeUpdate(sql.toString(), "ddl");
    }

    /**
     * 删除数据表
     *
     * @param tableName 要删除那个表的表名
     * @return 返回false删除失败, 返回true删除成功
     */
    public boolean deleteTable (String tableName) {
        // 拼接SQL语句
        StringBuffer sql = new StringBuffer();
        // 删除表语法
        sql.append("drop table if not exists ");
        // 删除那个表
        sql.append(tableName + ";");
        return executeUpdate(sql.toString(), "ddl");
    }

    /**
     * 遍历表数据
     *
     * @param databaseName 指定数据库
     * @param tableName    指定表
     * @return 返回false查询失败, 返回true查询成功
     */
    public boolean inquireTable (String databaseName, String tableName) {
        String log = loadDatabaseName + "->\n" + "inquireTable (String databaseName, String tableName)->\n";
        if (!useDatabase(databaseName)) {
            Log.outRedLn(log + "数据库: " + databaseName + "使用失败,请检查操作");
            return false;
        }
        // 拼接SQL语句
        // 查询语法
        String sql = "select * from " + tableName + ";";
        // 执行sql查询语句
        try {
            ResultSet resultSet = getStatement().executeQuery(sql);
            resultSet.beforeFirst();
            ResultSetMetaData metaData = resultSet.getMetaData();
            while (resultSet.next()) {
                StringBuilder builder = new StringBuilder();
                builder.append("{ ");
                for (int i = 1; i <= metaData.getColumnCount(); i++) {
                    String columnName = metaData.getColumnName(i);
                    String str = resultSet.getString(columnName);
                    builder.append(columnName + ": " + str + "    ");
                }
                builder.append("}");
                System.out.println(builder.toString());
            }
        } catch (SQLException throwables) {
            System.err.println(loadDatabaseName + "--> inquireTable(String databaseName, String tableName): 查询出现异常,可能找不到对应的 \"" + databaseName + "\" 数据库,或者找不到对应的 \"" + tableName + "\" 数据表找不到,请检查信息");
            return false;
        }
        return true;
    }

    /**
     * 返回Connection连接
     *
     * @return 返回Connection连接
     */
    public Connection getConnection ( ) {
        return connection;
    }

    /**
     * 返回通过查询所有数据返回结果集
     *
     * @param databaseName 数据库名
     * @param tableName    表名
     * @return 返回ResultSet结果集, 返回null没有查询到
     */
    public ResultSet inquireResultSet (String databaseName, String tableName) {
        String log = loadDatabaseName + "->\n" + "inquireResultSet (String databaseName, String tableName)->\n";
        if (!useDatabase(databaseName)) {
            Log.outRedLn(log + "数据库: " + databaseName + "使用失败,请检查操作");
            return null;
        }
        // 拼接SQL语句
        // 查询语法
        String sql = "select * from " + tableName + ";";
        try {
            return getStatement().executeQuery(sql);
        } catch (SQLException throwables) {
            // throwables.printStackTrace();
            return null;
        }

    }


    /**
     * 打印指定数据库的指定表的结构信息
     *
     * @param databaseName 指定数据库
     * @param tableName    指定表
     */
    public void inquireTableMessage (String databaseName, String tableName) {
        String sqlUseDdta = "use " + databaseName + ";";
        // 拼接SQL语句
        // 查询语法
        String sql = "select * from " + tableName + ";";
        // 执行sql查询语句
        try {
            if (!(Objects.isNull(databaseName) || databaseName.isEmpty())) {
                executeUpdate(sqlUseDdta, "ddl");
            }
            ResultSet resultSet = getStatement().executeQuery(sql);
            resultSet.beforeFirst();
            ResultSetMetaData metaData = resultSet.getMetaData();
            StringBuilder builder = new StringBuilder();
            StringBuilder line = null;
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                String columnName = metaData.getColumnName(i);
//                String schemaName = metaData.getSchemaName(i);
                String columnTypeName = metaData.getColumnTypeName(i);
                int precision = metaData.getPrecision(i);
                int num = 1;
                if (Objects.isNull(line)) {
                    line = new StringBuilder();
                    while (num <= -5 + (3 * (columnName.length() + columnTypeName.length() + /*schemaName.length() +*/ (precision + "").length()))) {
                        line.append("—");
                        num++;
                    }
                }
                builder.append(line);
                builder.append("\n" + i + ".列名: " + columnName + "     类型: " + columnTypeName + /*"     模式: " + schemaName +*/ "     数据长度: " + precision + "\n");
            }
            builder.append(line);
            System.out.println(builder.toString());
        } catch (SQLException throwables) {
            System.err.println(loadDatabaseName + "--> inquireTableMessage(String databaseName, String tableName): 查询出现异常,可能找不到对应的 \"" + databaseName + "\" 数据库,或者找不到对应的 \"" + tableName + "\" 数据表找不到,请检查信息");
        }
    }

    /**
     * 需要优化
     * 创建LoadDatabase的ODT的实例
     *
     * @return 返回InsertInto对象
     */
    public InsertInto createInsertInto ( ) {
        return new InsertInto(this);
    }


    /**
     * 需要优化
     *
     * @return
     */
    public Delete createDelete ( ) {
        return new Delete(this);
    }


    /**
     * 需要优化
     *
     * @return
     */
    public Update createUpdate ( ) {
        return new Update(this);
    }


    /**
     * 关闭MySQL数据库Connection连接
     */
    public void close ( ) {
        try {
            if (!Objects.isNull(this.statement)) {
                this.statement.close();
            }
            if (!Objects.isNull(this.connection)) {
                this.connection.close();
            }
            isAlready = false;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}

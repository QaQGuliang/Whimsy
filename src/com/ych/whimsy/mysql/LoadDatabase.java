package src.com.ych.whimsy.mysql;

import src.com.ych.whimsy.log.Log;

import javax.sql.RowSet;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;
import java.net.MalformedURLException;
import java.net.URL;
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
    private static final String loadDatabaseName = "--> " + LoadDatabase.class.getSimpleName();
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
        return true;
    }


//****************************************************************************************************************************************************************************************************************************************

    /**
     * 使用那个数据库
     *
     * @param databaseName 数据库名
     * @return 返回false使用失败, 返回true使用成功
     */
    public boolean userDatabase (String databaseName) {
        // 判断数据库名是否为空
        if (Objects.isNull(databaseName) || databaseName.isEmpty()) {
            System.err.println(loadDatabaseName + "\n--> userDatabase (String databaseName): 数据库\" " + databaseName + "\" 不能为空,请检查信息");
            return false;
        }
        // 拼接DDL语句
        String sqlUseDdta = "use " + databaseName + ";";
        return executeUpdate(sqlUseDdta, "ddl");
    }

    /**
     * 关闭MySQL数据库Connection连接
     */
    public void close ( ) {
        try {
            if (!Objects.isNull(this.cachedRowSet)) {
                this.cachedRowSet.close();
            }
            if (!Objects.isNull(this.resultSet)) {
                this.resultSet.close();
            }
            if (!Objects.isNull(this.statement)) {
                this.statement.close();
            }
            if (!Objects.isNull(this.connection)) {
                this.connection.close();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    /**
     * Statement对象
     */
    private Statement statement;
    /**
     * PreparedStatement对象
     */
    private PreparedStatement preparedStatement;
    /**
     * ResultSet结果集对象
     */
    private ResultSet resultSet;

    /**
     * CachedRowSet结果集
     */
    private CachedRowSet cachedRowSet;


    /**
     * 获取Statement对象
     *
     * @return Statement对象
     * @throws SQLException
     */
    public Statement getStatement ( ) throws SQLException {
        if (Objects.isNull(statement)) {
            // statement为空通过Connection对象创建一个Statement对象
            statement = connection.createStatement();
        }
        // 不为空直接返回statement
        return statement;
    }


    /**
     * 获取Statement对象
     *
     * @return Statement对象
     * @throws SQLException
     */
    public PreparedStatement getPreparedStatement (String databaseName, String tablename) throws SQLException {
        userDatabase(databaseName);
        if (Objects.isNull(preparedStatement)) {
            // statement为空通过Connection对象创建一个Statement对象
            preparedStatement = connection.prepareStatement("select * from " + tablename);
        }
        // 不为空直接返回statement
        return preparedStatement;
    }

    /**
     * 获取ResultSet结果
     *
     * @return ResultSet结果
     * @throws SQLException
     */
    public ResultSet getResultSet ( ) throws SQLException {
        if (Objects.isNull(resultSet)) {
            // 如果resultSet通过Statement拿到一个ResultSet对象
            resultSet = getStatement().getResultSet();
        }
        // 不为空返回resultSet
        return resultSet;
    }

    /**
     * 获取CachedRowSet对象
     *
     * @return CachedRowSet对象
     * @throws SQLException
     */
    public CachedRowSet getCachedRowSet (String tableName) throws SQLException {
        if (Objects.isNull(cachedRowSet)) {
            cachedRowSet = RowSetProvider.newFactory().createCachedRowSet();
            cachedRowSet.populate(getStatement().executeQuery("select * from " + tableName + ";"));
        }
        return cachedRowSet;
    }

    /**
     * 获取RowSet对象
     *
     * @return RowSet对象
     * @throws SQLException
     */
    public RowSet getRowSet ( ) throws SQLException {
        return RowSetProvider.newFactory().createCachedRowSet();
    }

    /**
     * 该方法用户执行DDL语句
     *
     * @param sql 要执行的DDL语句
     * @return 返回false执行失败, 返回true执行成功
     */
    private boolean executeUpdate (String sql, String ddlOrDml) {
        try {
            int i = getStatement().executeUpdate(sql);
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
            if (Objects.isNull(field.getKey()) || field.getKey().isEmpty()) {
                sql.append(field.getName() + " " + field.getType());
            } else {
                sql.append(field.getName() + " " + field.getType() + " " + field.getKey());
            }
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
        if (!userDatabase(databaseName)) {
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
        if (!userDatabase(databaseName)) {
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
     * 创建LoadDatabase的ODT的实例
     *
     * @return 返回InsertInto对象
     */
    public InsertInto createInsertInto ( ) {
        return new InsertInto();
    }

    /**
     * 一些工具内部类的通用属性
     */
    private abstract class Property {
        /**
         * 当前类的类名
         */
        private final String propertyName = Property.class.getSimpleName();
        /**
         * 当前类的信息
         */
        private final String propertyLog = loadDatabaseName + ":\n" + propertyName + ":\n";
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
            return executeUpdate(sql_user, "ddl");
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

        private InsertInto ( ) {
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
            return executeUpdate(sql_insert_into, "dml");
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
            for (Field field : fields) {
                columns.append(field.getName());
                switch (field.getType()) {
                    case Field.INT:
                        values.append(field.getValue() + " ");
                        break;
                    case Field.STRING_255:
                        values.append("\'" + field.getValue() + "\'");
                        break;
                }
                if (num == fields.length) {
                    continue;
                }
                columns.append(",");
                values.append(",");
                num++;
            }
            // 拼接插入语句
            String sql_insert_into = SQL_INSER_INTO + tableName + " (" + columns + ") values(" + values + ");";
            return executeUpdate(sql_insert_into, "dml");
        }
    }

    public Delete createDelete ( ) {
        return new Delete();
    }

    /**
     * Delete 类是LoadDatabase类的内部类,该类的作用是删除数据表中的数据
     */
    public class Delete extends Property {
        /**
         * 当前类的类名
         */
        private final String deleteName = Delete.class.getSimpleName();
        /**
         * 当前类的信息
         */
        private final String deleteLog = loadDatabaseName + ":\n" + deleteName + ":\n";

        private Delete ( ) {
        }

        /**
         * 设置删除值,给setColumns (String... columns)方法设置的列定义,设置值,将根据设置的这个值删除数据
         *
         * @param values 列定义值
         * @return 返回false设置失败, 返回true设置成功
         */
        @Override
        public boolean setValues (String... values) {
            if (Objects.isNull(values) || values.length == 0) {
                System.err.println(deleteLog + "setValue (String.. values): 要删除的数据不能为空,请重新输入");
                return false;
            }
            if (values.length > 1) {
                System.err.println(deleteLog + "setValue (String.. values): 要删除的数据不能大于一个值,请重新输入");
                return false;
            }
            this.values = values[0];
            return true;
        }

        /**
         * 提交删除使用setColumns (String... columns) , setValues (String... values)设置的,列定义以及以及根据对应的值删除指定数据
         *
         * @return 返回false删除失败, 返回true删除成功
         */
        @Override
        public boolean commit ( ) {
            // delete from teacher2 where id>2;
            if (Objects.isNull(columns) || columns.isEmpty()) {
                System.err.println(deleteLog + "commot ( ) : 列定义不能为空,请重新输入");
                return false;
            }
            if (Objects.isNull(values) || values.isEmpty()) {
                System.err.println(deleteLog + "commot ( ) : 列定义的值不能为空,请重新输入");
                return false;
            }
            // 拼接删除语句
            String sql_delete = "delete from " + tableName + " where " + columns + "=" + values + ";";
            // 执行删除语句
            return executeUpdate(sql_delete, "dml");
        }

        /**
         * 提交删除,使用setFields(Field... fields)设置的字段进行删除
         *
         * @return 返回false删除失败, 返回true删除成功
         */
        @Override
        public boolean commitFields ( ) {
            if (Objects.isNull(fields) || fields.length == 0) {
                System.err.println(deleteLog + "commitFields ( ): 要删除的字段不能为空,请重新输入");
                return false;
            }
            if (fields.length > 1) {
                System.err.println(deleteLog + "setValue (String.. values): 要删除的字段不能大于一个,请重新输入");
                return false;
            }
            String v = null;
            switch (fields[0].getType()) {
                case Field.INT:
                    v = fields[0].getValue();
                    break;
                case Field.STRING_255:
                    v = "\'" + fields[0].getValue() + "\'";
                    break;
            }

            // 拼接删除语句
            String sql_delete = "delete from " + tableName + " where " + fields[0].getName() + "=" + v + ";";
            // 执行删除语句
            return executeUpdate(sql_delete, "dml");
        }
    }

    public Update createUpdate ( ) {
        return new Update();
    }

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

        /**
         * 私有化构造器
         */
        private Update ( ) {
        }

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
                case Field.INT:
                    v = field.getValue();
                    break;
                case Field.STRING_255:
                    v = "\'" + field.getValue() + "\'";
                    break;
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
            return executeUpdate(sql_update, "dml");
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
                    case Field.INT:
                        values[index] = field.getValue() + " ";
                        break;
                    case Field.STRING_255:
                        values[index] = "\'" + field.getValue() + "\'";
                        break;
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
}

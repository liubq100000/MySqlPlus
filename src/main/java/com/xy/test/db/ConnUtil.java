package com.xy.test.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class ConnUtil {

    // 数据源
    public static String driver = "com.mysql.cj.jdbc.Driver";

    public static String url = "jdbc:mysql://192.168.0.244:3308/erp_v3?useUnicode=true&characterEncoding=utf-8&autoReconnect=true&useSSL=false&serverTimezone=Asia/Shanghai&rewriteBatchedStatements=true&nullCatalogMeansCurrent=true&zeroDateTimeBehavior=CONVERT_TO_NULL";

    public static String username ="root";

    public static String password ="root";


    //当前线程缓存
    private static ThreadLocal<Connection> connCache = new ThreadLocal<>();

    /**
     * @throws Exception
     * @description 初始化
     */
    public static synchronized Connection getConn() throws Exception {
        Connection conn = connCache.get();
        if (conn == null || conn.isClosed()) {
            Class.forName(driver).newInstance();
            Properties jdbcProperties = new Properties();
            jdbcProperties.put("user", username);
            jdbcProperties.put("password", password);
            conn = DriverManager.getConnection(url, jdbcProperties);
            connCache.set(conn);
        }
        return conn;
    }
}

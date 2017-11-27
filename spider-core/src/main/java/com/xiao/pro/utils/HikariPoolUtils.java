package com.xiao.pro.utils;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public final class HikariPoolUtils {
    private static final Logger logger = LoggerFactory.getLogger(HikariPoolUtils.class);
    public static final String JDBC_URL = "jdbcUrl";
    public static final String JDBC_USERNAME = "username";
    public static final String JDBC_PASSWORD = "password";
    public static final String JDBC_DRIVERCLASSNAME = "driverClassName";

    public static final String JDBC_CONNECTIONTIMEOUT = "connectionTimeout";
    public static final String JDBC_IDLETIMEOUT = "idleTimeout";
    public static final String JDBC_MAXLIFETIME = "maxLifetime";
    public static final String JDBC_AUTOCOMMIT = "autoCommit";
    public static final String JDBC_MAXIMUMPOOLSIZE = "maximumPoolSize";

    private static DataSource dataSource = null;//

    public static synchronized void initialPool(Properties properties) {
        if (dataSource == null) {
            dataSource = new HikariDataSource();
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(properties.getProperty(JDBC_URL));
            config.setUsername(properties.getProperty(JDBC_USERNAME));
            config.setPassword(properties.getProperty(JDBC_PASSWORD));
            config.setDriverClassName(properties.getProperty(JDBC_DRIVERCLASSNAME));

            if(StringUtils.isNotBlank(properties.getProperty(JDBC_CONNECTIONTIMEOUT))){
                config.addDataSourceProperty(JDBC_CONNECTIONTIMEOUT, properties.getProperty(JDBC_CONNECTIONTIMEOUT));
            }
            if(StringUtils.isNotBlank(properties.getProperty(JDBC_IDLETIMEOUT))){
                config.addDataSourceProperty(JDBC_IDLETIMEOUT,  properties.getProperty(JDBC_IDLETIMEOUT));
            }
            if(StringUtils.isNotBlank(properties.getProperty(JDBC_MAXLIFETIME))){
                config.addDataSourceProperty(JDBC_MAXLIFETIME,  properties.getProperty(JDBC_MAXLIFETIME));
            }
            if(StringUtils.isNotBlank(properties.getProperty(JDBC_AUTOCOMMIT))){
                config.addDataSourceProperty(JDBC_AUTOCOMMIT,  properties.getProperty(JDBC_AUTOCOMMIT));
            }
            if(StringUtils.isNotBlank(properties.getProperty(JDBC_MAXIMUMPOOLSIZE))){
                config.addDataSourceProperty(JDBC_MAXIMUMPOOLSIZE,  properties.getProperty(JDBC_MAXIMUMPOOLSIZE));
            }

            dataSource = new HikariDataSource(config);
            logger.info("pool construct success !");
        }
    }

    /**
     * 获取数据库连接对象
     * @return 数据连接对象
     * @throws SQLException
     */
    public static synchronized Connection getConnection() throws SQLException {
        final Connection conn = dataSource.getConnection();
//        conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
        return conn;
    }

    public static void closeStatement(Statement pre){
        if(pre!=null){
            try {
                pre.close();
            } catch (SQLException e) {
                logger.error("close PreparedStatement error : ",e);
            }
        }
    }

    public static void closeResultSet(ResultSet res){
        if(res!=null){
            try {
                res.close();
            } catch (SQLException e) {
                logger.error("close PreparedStatement error : ",e);
            }
        }
    }

    public static void closeConnection(Connection conn){
        if(conn!=null){
            try {
                conn.close();
            } catch (SQLException e) {
                logger.error("close connection error : ",e);
            }
        }
    }
}
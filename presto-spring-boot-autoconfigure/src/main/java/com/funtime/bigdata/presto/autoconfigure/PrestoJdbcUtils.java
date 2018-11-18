package com.funtime.bigdata.presto.autoconfigure;

import com.facebook.presto.jdbc.internal.guava.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class PrestoJdbcUtils {

    private static final Logger logger = LoggerFactory.getLogger(PrestoJdbcUtils.class);

    private final String driver;
    private final String username;
    private final String password;
    private final String url;

    public PrestoJdbcUtils(String driver, String username, String password, String url) {
        this.driver = driver;
        this.username = username;
        this.password = password;
        this.url = url;
        try {
            Class.forName(this.driver);
        } catch (ClassNotFoundException e) {
            logger.error("presto driver load error!", e);
        }
    }

    /**
     * Get Presto Connection
     *
     * @return
     * @throws SQLException
     */
    public Connection getConnection() throws SQLException {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            logger.error("presto connect failed:" + url, e);
            throw e;
        }
        return conn;
    }

    /**
     * Close Presto Connection
     *
     * @param conn
     */
    public void closeConnection(Connection conn) {
        if (conn == null) {
            return;
        }
        try {
            if (!conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
            logger.error("presto connection close failed!", e);
        }
    }

    /**
     * Query List<Map>
     *
     * @param sql
     * @return
     * @throws SQLException
     */
    public List<Map<String, Object>> querySqlForKeyValue(String sql) throws SQLException {
        Connection conn = getConnection();
        Statement stmt = null;
        ResultSet rs = null;
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            if (logger.isDebugEnabled()) {
                logger.info("query sql:" + sql);
            }
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            ResultSetMetaData md = rs.getMetaData();
            int columnCount = md.getColumnCount();
            while (rs.next()) {
                Map<String, Object> map = Maps.newHashMap();
                for (int i = 1; i <= columnCount; i++) {
                    map.put(md.getColumnLabel(i), rs.getObject(i));
                }
                list.add(map);
            }
        } catch (SQLException e) {
            logger.error("query sql failed:" + sql, e);
            throw e;
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    logger.error("ResultSet close fail:", e);
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    logger.error("Statement close fail:", e);
                }
            }
            closeConnection(conn);
        }
        return list;
    }

    public List<Object[]> queryRows(String sql) throws SQLException {
        Connection conn = getConnection();
        Statement stmt = null;
        ResultSet rs = null;
        List<Object[]> list = new ArrayList<Object[]>();
        try {
            if (logger.isDebugEnabled()) {
                logger.info("query sql:" + sql);
            }
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            ResultSetMetaData md = rs.getMetaData();
            int columnCount = md.getColumnCount();
            Object[] row = null;
            while (rs.next()) {
                row = new Object[columnCount];
                for (int i = 0; i < columnCount; i++) {
                    row[i] = rs.getObject(i + 1);
                }
                list.add(row);
            }
        } catch (SQLException e) {
            logger.error("query sql failed:" + sql, e);
            throw e;
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    logger.error("ResultSet close fail:",e);
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    logger.error("Statement close fail:",e);
                }
            }
            if (conn != null) {
                closeConnection(conn);
            }
        }
        return list;
    }

    public boolean excuteSql(String sql) throws SQLException {
        Connection con = getConnection();
        boolean flag = true;
        Statement stmt = null;
        try {
            if (logger.isDebugEnabled()) {
                logger.info("query sql:" + sql);
            }
            stmt = con.createStatement();
            stmt.execute(sql);
        } catch (SQLException e) {
            logger.error("query sql failed:" + sql, e);
            throw e;
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    logger.error("Statement close fail:", e);
                }
            }
            closeConnection(con);
        }
        return flag;
    }


    public long countQuery(String sql) throws SQLException {
        Connection con = getConnection();
        Statement stmt = null;
        ResultSet rs = null;
        long count = 0l;
        try {
            if (logger.isDebugEnabled()) {
                logger.info("query sql:" + sql);
            }
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                count = rs.getLong(1);
            }
        } catch (SQLException e) {
            logger.error("query sql failed:" + sql, e);
            throw e;
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    logger.error("ResultSet close fail:",e);
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    logger.error("Statement close fail:",e);
                }
            }
            if (con != null) {
                closeConnection(con);
            }
        }
        return count;
    }

}

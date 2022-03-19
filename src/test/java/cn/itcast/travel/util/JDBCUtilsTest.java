package cn.itcast.travel.util;

import junit.framework.TestCase;

import java.sql.Connection;
import java.sql.SQLException;

public class JDBCUtilsTest extends TestCase {

    public void testGetDataSource() {

    }

    public void testGetConnection() throws SQLException {
        Connection connection= JDBCUtils.getConnection();
        System.out.println(connection);
    }

    public void testClose() {
    }

    public void testTestClose() {
    }
}
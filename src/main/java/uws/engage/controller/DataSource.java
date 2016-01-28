package uws.engage.controller;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbcp.BasicDataSource;
import uws.engage.controller.General;
import java.sql.DriverManager;
public class DataSource {

    private static DataSource     datasource;
    private BasicDataSource ds;
    private General g;

    private DataSource() throws IOException, SQLException, PropertyVetoException {
        g = new General();
        ds = new BasicDataSource();
        ds.setDriverClassName("com.mysql.jdbc.Driver");
        ds.setUsername(g.DB_USERNAME);
        ds.setPassword(g.DB_PASSWD);
        ds.setUrl(g.DB_NAME);
       
     // the settings below are optional -- dbcp can work with defaults
        ds.setMaxActive(-1);
        // ds.setMinIdle(5);
        // ds.setMaxIdle(20);
        // ds.setMaxOpenPreparedStatements(180);

    }

    public static DataSource getInstance() throws IOException, SQLException, PropertyVetoException {
        if (datasource == null) {
            datasource = new DataSource();
            return datasource;
        } else {
            return datasource;
        }
    }

    public Connection getConnection() throws SQLException {
        return this.ds.getConnection();
    }

}
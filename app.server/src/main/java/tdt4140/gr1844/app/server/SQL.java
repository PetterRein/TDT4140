package tdt4140.gr1844.app.server;

import org.json.JSONArray;
import org.json.JSONObject;

import java.nio.file.Paths;
import java.sql.*;

public class SQL {
    // init database constants
    private static final String DATABASE_URL = "jdbc:sqlite:sample.db"; //Denne er feil se print fra Working Directory
    // init connection object
    // connect database
    private Connection connection = null;
    // disconnect database
    void disconnect() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    Connection connect() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
            Class.forName("org.sqlite.JDBC").newInstance();
            //DriverManager.registerDriver(new JDBC());
            java.nio.file.Path currentRelativePath = Paths.get("");
            String s = currentRelativePath.toAbsolutePath().toString();
            //System.out.println("Current relative path is: " + s);
            //System.out.println("Working Directory = " + System.getProperty("user.dir"));
            if (connection == null) {
                try {
                    connection = DriverManager.getConnection(DATABASE_URL);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            return connection;

        }

    static JSONObject SQLToJSONArray(ResultSet rs, String listName) throws SQLException {
        JSONObject json = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        ResultSetMetaData rsmd = rs.getMetaData();
        while(rs.next()) {
            int numColumns = rsmd.getColumnCount();
            JSONObject jsonObject = new JSONObject();
            for (int i=1; i<=numColumns; i++) {
                String column_name = rsmd.getColumnName(i);
                jsonObject.put(column_name, rs.getObject(column_name));
            }
            jsonArray.put(jsonObject);
        }
        json.put(listName, jsonArray);
        return json;
    }


    static JSONObject SQLToJSON (ResultSet rs) throws SQLException {
        JSONObject json = new JSONObject();
        ResultSetMetaData rsmd = rs.getMetaData();
        while(rs.next()) {
            int numColumns = rsmd.getColumnCount();
            for (int i=1; i<=numColumns; i++) {
                String column_name = rsmd.getColumnName(i);
                json.put(column_name, rs.getObject(column_name));
            }
        }
        return json;
    }
}

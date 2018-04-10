package tdt4140.gr1844.app.server;

import org.json.JSONArray;
import org.json.JSONObject;

import java.nio.file.Paths;
import java.sql.*;

import static tdt4140.gr1844.app.server.Create.createPatient;

class SQL {

    static void initDatabase() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
        // create a database connection
        SQL conn = new SQL();

        // drop tables if they exist
        System.out.println("Dropping all tables...");
        PreparedStatement usersTable = conn.connect().prepareStatement("DROP TABLE IF EXISTS users");
        PreparedStatement patientDataTable = conn.connect().prepareStatement("DROP TABLE IF EXISTS ratings");
        PreparedStatement feedback = conn.connect().prepareStatement("DROP TABLE IF EXISTS feedbacks");
        usersTable.execute();
        patientDataTable.execute();
        feedback.execute();

        // create table users
        PreparedStatement statement1 = conn.connect().prepareStatement("CREATE TABLE IF NOT EXISTS users" +
                "(id INTEGER PRIMARY KEY, role varchar(64), name varchar(64), email varchar(64), passwordHash varchar(2000), salt varchar(256)," +
                "cookie varchar(256), doctorID int, FOREIGN KEY (doctorID) REFERENCES users(id))");
        // create table patientData
        PreparedStatement statement2 = conn.connect().prepareStatement("CREATE TABLE IF NOT EXISTS patientData" +
                "(id INTEGER PRIMARY KEY, patientID int not null, rating int, message text," +
                "timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP not null," +
                "FOREIGN KEY (patientID) REFERENCES users(id))");
        PreparedStatement statement3 = conn.connect().prepareStatement("CREATE TABLE IF NOT EXISTS feedback" +
                "(id INTEGER PRIMARY KEY, message VARCHAR (20000000))");


        statement1.execute();
        statement2.execute();
        statement3.execute();


        // TODO: Fix test
        createPatient("Haavard", "haavard@email.com", "password", 2);
        createPatient("Balazs", "balazs@email.com", "password", 3);
        createPatient("Mats", "mats@email.com", "password", 1);
        PreparedStatement statement4 = conn.connect().prepareStatement("update users set cookie='a' where email = 'email'");
        statement4.executeUpdate();
        conn.disconnect();
    }

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

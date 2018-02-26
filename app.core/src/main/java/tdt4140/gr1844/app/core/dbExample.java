package tdt4140.gr1844.app.core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class dbExample {

        public static void main(String[] args)
        {
            Connection connection = null;
            try
            {
                // create a database connection
                connection = DriverManager.getConnection("jdbc:sqlite:sample.db");
                Statement statement = connection.createStatement();
                statement.setQueryTimeout(30);  // set timeout to 30 sec.

                statement.executeUpdate("drop table if exists person");
                statement.executeUpdate("create table if not EXISTS users (id integer, role varchar, name varchar, email varchar, password_hash varchar," +
                                                                                " salt varchar, PRIMARY KEY(id))");
                statement.executeUpdate("create table if not EXISTS patientdata (id integer, patientID integer, doctorID integer, rating integer, extrainfo varchar," +
                                                                        " timestamp integer, PRIMARY KEY(id), FOREIGN KEY(patientID) references users(id), FOREIGN KEY(doctorID) references users(id) )");
/*
                ResultSet rs = statement.executeQuery("select * from person");
                while(rs.next())
                {
                    // read the result set
                    System.out.println("name = " + rs.getString("name"));
                    System.out.println("id = " + rs.getInt("id"));
                }*/
            }
            catch(SQLException e)
            {
                // if the error message is "out of memory",
                // it probably means no database file is found
                System.err.println(e.getMessage());
            }
            finally
            {
                try
                {
                    if(connection != null)
                        connection.close();
                }
                catch(SQLException e)
                {
                    // connection close failed.
                    System.err.println(e);
                }
            }
        }
    }

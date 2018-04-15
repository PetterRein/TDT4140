package tdt4140.gr1844.app.server;

import org.json.JSONObject;
import org.mindrot.jbcrypt.BCrypt;
import java.sql.PreparedStatement;
import java.sql.SQLException;


class Create {


    static JSONObject createFeeling(int patientID, int rating, String message, String cookie) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        JSONObject response = new JSONObject();
        if (Authentication.isValid("rating", rating)){
            if (Authentication.isDataOwner(patientID, cookie)) {
                SQL sql = new SQL();
                PreparedStatement createFeelingQuery = sql.connect()
                        .prepareStatement(
                        "INSERT INTO patientData(patientID, rating, message)  VALUES(?,?,?)"
                        );
                createFeelingQuery.setInt(1,patientID);
                createFeelingQuery.setInt(2, rating);
                createFeelingQuery.setString(3, message);
                Boolean successfulCreation = createFeelingQuery.executeUpdate() > 0;
                if (successfulCreation) {
                    response.put("status", "OK");
                } else {
                    response.put("status", "ERROR");
                    response.put("message", "Feeling was not added to the database.");
                }
                sql.disconnect();
            } else {
                response.put("status", "ERROR");
                response.put("message", "You are not authorized to do that action.");
            }
        } else {
            response.put("status", "ERROR");
            response.put("message", "Rating is a value in the range of 1-5.");
        }
        return response;
    }


    static JSONObject createAdminOrDoctor(String name, String email, String password, String role, String cookie) throws SQLException, IllegalAccessException, ClassNotFoundException, InstantiationException {
        SQL sql = new SQL();
        JSONObject response = new JSONObject();
        if (Authentication.isAuthenticated(cookie, "admin")) {

            PreparedStatement userExistsQuery = sql.connect()
                    .prepareStatement(
                    "SELECT * FROM users WHERE email = ?"
                    );
            userExistsQuery.setString(1, email);
            userExistsQuery.execute();
            Boolean userExists = userExistsQuery.getResultSet().isBeforeFirst();
            if (userExists) {
                response.put("status", "ERROR");
                response.put("message", "E-mail address (" + email + ") is taken.");
                userExistsQuery.close();
            } else {
                String salt = BCrypt.gensalt();
                String passwordHash = BCrypt.hashpw(password, salt);
                PreparedStatement createPatientQuery = sql.connect()
                        .prepareStatement(
                        "INSERT INTO users(role, name, email, passwordHash, salt)" +
                            " VALUES (?, ?, ?, ?, ?)"
                        );
                createPatientQuery.setString(1, role);
                createPatientQuery.setString(2, name);
                createPatientQuery.setString(3, email);
                createPatientQuery.setString(4, passwordHash);
                createPatientQuery.setString(5, salt);
                Boolean userCreated = createPatientQuery.executeUpdate() > 0;
                if (userCreated) {
                    response.put("status", "OK");
                } else {
                    response.put("status", "ERROR");
                    response.put("message", "User could not be created.");
                }
                createPatientQuery.close();
            }

        } else {
            response.put("status", "ERROR");
            response.put("message", "You are not authorized for that action.");
        }
        sql.disconnect();
        return response;
    }


    static JSONObject createPatient(String name, String email, String password, int doctorID) throws SQLException, IllegalAccessException, InstantiationException, ClassNotFoundException {
        return create("patient", name,email,password,doctorID);
    }


    // Create admin user for testing
    static void createAdminTestPurpose() throws SQLException, IllegalAccessException, InstantiationException, ClassNotFoundException{
        create("admin", "Admin", "admin@email.com", "password", -1);
        SQL sql = new SQL();
        PreparedStatement setCookie = sql.connect()
                .prepareStatement(
                        "UPDATE users SET cookie = ? WHERE email = ?"
                );
        setCookie.setString(1, "1");
        setCookie.setString(2, "admin@email.com");
        setCookie.execute();
        setCookie.close();
        sql.disconnect();
    }

    private static JSONObject create(String role, String name, String email, String password, int doctorID) throws SQLException, IllegalAccessException, InstantiationException, ClassNotFoundException{
        SQL sql = new SQL();
        JSONObject response = new JSONObject();
        PreparedStatement userExistsQuery = sql.connect()
                .prepareStatement(
                        "SELECT * FROM users WHERE email = ?"
                );
        userExistsQuery.setString(1, email);
        userExistsQuery.execute();
        Boolean userExists = userExistsQuery.getResultSet().isBeforeFirst();
        if (userExists) {
            response.put("status", "ERROR");
            response.put("message", "E-mail address (" + email + ") is taken.");
            userExistsQuery.close();
        } else {
            String salt = BCrypt.gensalt();
            String passwordHash = BCrypt.hashpw(password, salt);
            PreparedStatement createPatientQuery = sql.connect()
                    .prepareStatement(
                            "INSERT INTO users(role, name, email, passwordHash, salt, doctorID) " +
                                    "VALUES (?, ?, ?, ?, ?, ?)"
                    );
            createPatientQuery.setString(1, role);
            createPatientQuery.setString(2, name);
            createPatientQuery.setString(3, email);
            createPatientQuery.setString(4, passwordHash);
            createPatientQuery.setString(5, salt);
            createPatientQuery.setInt(6, doctorID);
            Boolean userCreated = createPatientQuery.executeUpdate() > 0;
            if (userCreated) {
                response.put("status", "OK");
            } else {
                response.put("status", "ERROR");
                response.put("message", "User could not be created.");
            }
            createPatientQuery.close();
        }
        sql.disconnect();
        return response;
    }



    static JSONObject createFeedback(String message, String cookie) throws SQLException, IllegalAccessException, InstantiationException, ClassNotFoundException {
        JSONObject response = new JSONObject();
        if (Authentication.isAuthenticated(cookie, "doctor")) {
            SQL sql = new SQL();
            PreparedStatement createFeedbackQuery = sql.connect()
                    .prepareStatement(
                    "INSERT INTO feedback(message) VALUES (?)"
                    );
            createFeedbackQuery.setString(1, message);
            boolean isFeedbackCreated = createFeedbackQuery.executeUpdate() > 0;
            if (isFeedbackCreated) {
                response.put("status", "OK");
            } else {
                response.put("status", "ERROR");
                response.put("message", "Feedback creation was unsuccessful.");

            }
            sql.disconnect();
        } else {
            response.put("status", "ERROR");
            response.put("message", "You are not authorized for that action.");
        }
        return response;
    }

    static JSONObject markFeedbackRead(int feedbackID, String cookie) throws SQLException, IllegalAccessException, InstantiationException, ClassNotFoundException {
        JSONObject response = new JSONObject();
        if (Authentication.isAuthenticated(cookie, "admin")) {
            SQL sql = new SQL();
            PreparedStatement createFeedbackQuery = sql.connect()
                    .prepareStatement(
                    "UPDATE feedbacks SET isRead = TRUE WHERE id = ?"
                    );
            createFeedbackQuery.setInt(1, feedbackID);
            boolean isFeedbackSetToRead = createFeedbackQuery.executeUpdate() > 0;
            if (isFeedbackSetToRead) {
                response.put("status", "OK");
            } else {
                response.put("status", "ERROR");
                response.put("message", "Feedback could not be set to read.");

            }
            sql.disconnect();
        } else {
            response.put("status", "ERROR");
            response.put("message", "You are not authorized for that action.");
        }
        return response;
    }

}

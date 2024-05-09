package com.minkostplan.eksamensprojekt.Repository;

import com.minkostplan.eksamensprojekt.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class DBRepository {

    @Autowired
    private DataSource dataSource;

    public void createUser(User user) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            // Get connection from DataSource
            conn = dataSource.getConnection();

            // Create SQL query without the subscriber field
            String sql = "INSERT INTO User (userId, firstName, lastName, email, password, age, gender, weight, height, activityLevel, isEmployed) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            // Create PreparedStatement
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, user.getUserId());
            pstmt.setString(2, user.getFirstName());
            pstmt.setString(3, user.getLastName());
            pstmt.setString(4, user.getEmail());
            pstmt.setString(5, user.getPassword());
            pstmt.setInt(6, user.getAge());
            pstmt.setString(7, String.valueOf(user.getGender()));
            pstmt.setDouble(8, user.getWeight());
            pstmt.setDouble(9, user.getHeight());
            pstmt.setString(10, user.getActivityLevel());
            pstmt.setBoolean(11, user.isEmployed());

            // Execute update
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error connecting to the database or executing the query.");
            e.printStackTrace();
        } finally {
            // Clean up and close connections
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println("Error closing the connection.");
                e.printStackTrace();
            }
        }
    }

    // Method to verify user login
    public User login(String email, String password) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        User user = null;

        try {
            conn = dataSource.getConnection();
            String sql = "SELECT * FROM User WHERE email = ? AND password = ?";

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, email);
            pstmt.setString(2, password);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                user = new User(
                        rs.getInt("userId"),
                        rs.getString("firstName"),
                        rs.getString("lastName"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getInt("age"),
                        rs.getString("gender").charAt(0),
                        rs.getDouble("weight"),
                        rs.getDouble("height"),
                        rs.getString("activityLevel"),
                        rs.getBoolean("isEmployed"),
                        rs.getBoolean("subscriber")
                );
            }
        } catch (SQLException e) {
            System.out.println("Error during database operation: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println("Error closing database resources: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return user;
    }


}

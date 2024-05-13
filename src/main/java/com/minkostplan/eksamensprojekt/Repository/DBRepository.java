package com.minkostplan.eksamensprojekt.Repository;

import com.minkostplan.eksamensprojekt.Model.Ingredients;
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

    public void updateUser(User user) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = dataSource.getConnection();
            String sql = "UPDATE User SET firstName=?, lastName=?, age=?, gender=?, weight=?, height=?, activityLevel=?, isEmployed=?, subscriber=? WHERE userId=?";

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, user.getFirstName());
            pstmt.setString(2, user.getLastName());
            pstmt.setInt(3, user.getAge());
            pstmt.setString(4, String.valueOf(user.getGender()));
            pstmt.setDouble(5, user.getWeight());
            pstmt.setDouble(6, user.getHeight());
            pstmt.setString(7, user.getActivityLevel());
            pstmt.setBoolean(8, user.isEmployed());
            pstmt.setBoolean(9, user.isSubscriber());
            pstmt.setInt(10, user.getUserId());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error connecting to the database or executing the query.");
            e.printStackTrace();
        } finally {
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

    public boolean deleteUser(int userId) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = dataSource.getConnection();
            String sql = "DELETE FROM User WHERE userId = ?";

            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.out.println("Error connecting to the database or executing the query: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println("Error closing the connection: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public User getUserById(int userId) {
        User user = null;
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM User WHERE userId = ?")) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
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
                        rs.getString("status"),
                        rs.getBoolean("isActive"),
                        rs.getBoolean("isAdmin")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public void createIngredients(Ingredients ingredients) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = dataSource.getConnection();
            String sql = "INSERT INTO Ingredients (ingredientsId, name, fat, carbohydrate, protein) VALUES (?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, ingredients.getIngredientsId());
            pstmt.setString(2, ingredients.getName());
            pstmt.setDouble(3, ingredients.getFat());
            pstmt.setDouble(4, ingredients.getCarbohydrate());
            pstmt.setDouble(5, ingredients.getProtein());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error connecting to the database or executing the query.");
            e.printStackTrace();
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println("Error closing the connection or statement.");
                e.printStackTrace();
            }
        }

    }



}
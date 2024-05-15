package com.minkostplan.eksamensprojekt.Repository;

import com.minkostplan.eksamensprojekt.Model.Ingredients;
import com.minkostplan.eksamensprojekt.Model.Recipe;
import com.minkostplan.eksamensprojekt.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class DBRepository {

    @Autowired
    private DataSource dataSource;

    private Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    private void closeResources(Connection conn, PreparedStatement pstmt, ResultSet rs) {
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
            System.out.println("Error closing the connection or statement.");
            e.printStackTrace();
        }
    }

    private void closeResources(Connection conn, PreparedStatement pstmt) {
        closeResources(conn, pstmt, null);
    }

    public void createUser(User user) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = getConnection();
            String sql = "INSERT INTO User (userId, firstName, lastName, email, password, age, gender, weight, height, activityLevel, isEmployed) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
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

            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error connecting to the database or executing the query.");
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt);
        }
    }

    public User login(String email, String password) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        User user = null;

        try {
            conn = getConnection();
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
            closeResources(conn, pstmt, rs);
        }
        return user;
    }

    public void updateUser(User user) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = getConnection();
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
            closeResources(conn, pstmt);
        }
    }

    public boolean deleteUser(int userId) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = getConnection();
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
            closeResources(conn, pstmt);
        }
    }

    public User getUserById(int userId) {
        User user = null;
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM User WHERE userId = ?")) {
            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
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
            conn = getConnection();
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
            closeResources(conn, pstmt);
        }
    }

    public void createRecipeWithIngredients(Recipe recipe, List<Ingredients> ingredientsList) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            conn.setAutoCommit(false); // Start a transaction

            // Insert data into Recipe table
            String recipeSql = "INSERT INTO Recipe (title, description, method, cookingTime, imageUrl) VALUES (?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(recipeSql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, recipe.getTitle());
            pstmt.setString(2, recipe.getDescription());
            pstmt.setString(3, recipe.getMethod());
            pstmt.setString(4, recipe.getCookingTime());
            pstmt.setString(5, recipe.getImageUrl());
            pstmt.executeUpdate();

            // Get the generated recipe ID
            rs = pstmt.getGeneratedKeys();
            int recipeId = 0;
            if (rs.next()) {
                recipeId = rs.getInt(1);
            }

            // Insert data into Recipe_Ingredients table
            String recipeIngredientsSql = "INSERT INTO Recipe_Ingredients (recipe_id, ingredient_id) VALUES (?, ?)";
            pstmt = conn.prepareStatement(recipeIngredientsSql);
            for (Ingredients ingredient : ingredientsList) {
                pstmt.setInt(1, recipeId);
                pstmt.setInt(2, ingredient.getIngredientsId());
                pstmt.executeUpdate();
            }

            conn.commit(); // Commit the transaction
        } catch (SQLException e) {
            try {
                if (conn != null) {
                    conn.rollback(); // Rollback the transaction in case of failure
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            System.out.println("Error connecting to the database or executing the query.");
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }
    }

    public List<Ingredients> getAllIngredients() {
        List<Ingredients> ingredientsList = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            String sql = "SELECT * FROM Ingredients";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Ingredients ingredient = new Ingredients();
                ingredient.setIngredientsId(rs.getInt("ingredientsId"));
                ingredient.setName(rs.getString("name"));
                ingredient.setFat(rs.getDouble("fat"));
                ingredient.setCarbohydrate(rs.getDouble("carbohydrate"));
                ingredient.setProtein(rs.getDouble("protein"));
                ingredientsList.add(ingredient);
            }
        } catch (SQLException e) {
            System.out.println("Error connecting to the database or executing the query.");
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }
        return ingredientsList;
    }
}

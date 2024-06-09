package com.minkostplan.eksamensprojekt.Repository;

import com.minkostplan.eksamensprojekt.Model.Ingredient;
import com.minkostplan.eksamensprojekt.Model.Recipe;
import com.minkostplan.eksamensprojekt.Model.Subscription;
import com.minkostplan.eksamensprojekt.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository klasse, der håndterer databaseoperationer for applikationen.
 */
@Repository
public class DBRepository {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Henter en forbindelse til databasen.
     *
     * @return en {@link Connection} til databasen.
     * @throws SQLException hvis der opstår en SQL-fejl under hentning af forbindelsen.
     */
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    /**
     * Lukker ressourcerne til databaseforbindelse, PreparedStatement og ResultSet.
     *
     * @param conn   {@link Connection} til databasen.
     * @param pstmt  {@link PreparedStatement} der skal lukkes.
     * @param rs     {@link ResultSet} der skal lukkes.
     */
    public void closeResources(Connection conn, PreparedStatement pstmt, ResultSet rs) {
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

    /**
     * Lukker ressourcerne til databaseforbindelse og PreparedStatement.
     *
     * @param conn  {@link Connection} til databasen.
     * @param pstmt {@link PreparedStatement} der skal lukkes.
     */
    public void closeResources(Connection conn, PreparedStatement pstmt) {
        closeResources(conn, pstmt, null);
    }

    /**
     * Henter en liste af opskrifter baseret på uge.
     *
     * @param week ugen opskrifterne skal hentes for.
     * @return en liste af {@link Recipe} objekter.
     */
    public List<Recipe> getRecipesByWeek(String week) {
        List<Recipe> recipes = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            String sql = "SELECT * FROM Recipe WHERE week = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, week);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Recipe recipe = new Recipe();
                recipe.setRecipeId(rs.getInt("recipeId"));
                recipe.setTitle(rs.getString("title"));
                recipe.setDescription(rs.getString("description"));
                recipe.setMethod(rs.getString("method"));
                recipe.setCookingTime(rs.getString("cookingTime"));
                recipe.setImageUrl(rs.getString("imageUrl"));
                recipe.setMealTime(rs.getString("meal_time"));
                recipe.setTotalCalories(rs.getInt("total_calories"));
                recipe.setTotalProtein(rs.getInt("total_protein"));
                recipe.setTotalFat(rs.getInt("total_fat"));
                recipe.setTotalCarbohydrates(rs.getInt("total_carbohydrates"));
                recipe.setWeek(rs.getString("week"));
                recipes.add(recipe);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }
        return recipes;
    }

    /**
     * Opdaterer ansættelsesstatus for en bruger baseret på deres email.
     *
     * @param email            brugerens email.
     * @param employmentStatus den nye ansættelsesstatus.
     */
    public void updateEmploymentStatus(String email, int employmentStatus) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = getConnection();
            String sql = "UPDATE User SET employed = ? WHERE email = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, employmentStatus);
            pstmt.setString(2, email);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt);
        }
    }

    /**
     * Opretter en ny bruger i databasen.
     *
     * @param user brugeren der skal oprettes.
     */
    public void createUser(User user) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = getConnection();
            String sql = "INSERT INTO User (firstName, lastName, email, password, birthday, gender, weight, height, activityLevel, goal, employed, subscriber) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, user.getFirstName());
            pstmt.setString(2, user.getLastName());
            pstmt.setString(3, user.getEmail());
            pstmt.setString(4, user.getPassword());
            pstmt.setDate(5, java.sql.Date.valueOf(user.getBirthday()));
            pstmt.setString(6, String.valueOf(user.getGender()));
            pstmt.setDouble(7, user.getWeight());
            pstmt.setDouble(8, user.getHeight());
            pstmt.setInt(9, user.getActivityLevel());
            pstmt.setInt(10, user.getGoal());
            pstmt.setInt(11, user.getEmployed());
            pstmt.setBoolean(12, user.isSubscriber());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt);
        }
    }

    /**
     * Finder en bruger baseret på deres email.
     *
     * @param email brugerens email.
     * @return den fundne bruger eller null hvis ingen bruger blev fundet.
     */
    public User findByEmail(String email) {
        Connection conn = null;     //initialiserer variabel til at holde databaseforbindelsen
        PreparedStatement pstmt = null;  // Initialiserer variabel til at holde resultatet af SQL-forespørgslen
        ResultSet rs = null; // vi opretter de her variabler inden vi går ind til vores try her får de så værdier.
        User user = null; // Initialiserer variabel til at holde det fundne User-objekt

        try {
            conn = getConnection();
            String sql = "SELECT * FROM User WHERE email = ?"; // SQL-forespørgsel
            pstmt = conn.prepareStatement(sql);  // Forbereder SQL-forespørgslen
            pstmt.setString(1, email);// Indsætter e-mailparameteren i SQL-forespørgslen
            rs = pstmt.executeQuery();

            if (rs.next()) {
                user = new User();
                user.setUserId(rs.getInt("userId"));
                user.setFirstName(rs.getString("firstName"));
                user.setLastName(rs.getString("lastName"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setBirthday(rs.getDate("birthday").toLocalDate());
                user.setGender(rs.getString("gender").charAt(0));
                user.setWeight(rs.getDouble("weight"));
                user.setHeight(rs.getDouble("height"));
                user.setActivityLevel(rs.getInt("activityLevel"));
                user.setGoal(rs.getInt("goal"));
                user.setEmployed(rs.getInt("employed"));
                user.setSubscriber(rs.getBoolean("subscriber"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }

        return user;
    }

    /**
     * Opdaterer en bruger i databasen.
     *
     * @param user den bruger der skal opdateres.
     */
    public void updateUser(User user) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = getConnection();
            String sql = "UPDATE User SET firstName=?, lastName=?, birthday=?, gender=?, weight=?, height=?, activityLevel=?, goal=?, employed=?, subscriber=? WHERE userId=?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, user.getFirstName());
            pstmt.setString(2, user.getLastName());
            pstmt.setDate(3, java.sql.Date.valueOf(user.getBirthday()));
            pstmt.setString(4, String.valueOf(user.getGender()));
            pstmt.setDouble(5, user.getWeight());
            pstmt.setDouble(6, user.getHeight());
            pstmt.setInt(7, user.getActivityLevel());
            pstmt.setInt(8, user.getGoal());
            pstmt.setInt(9, user.getEmployed());
            pstmt.setBoolean(10, user.isSubscriber());
            pstmt.setInt(11, user.getUserId());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt);
        }
    }

    /**
     * Sletter en bruger fra databasen.
     *
     * @param userId brugerens ID.
     * @return true hvis brugeren blev slettet, ellers false.
     */
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

    /**
     * Henter en bruger baseret på deres ID.
     *
     * @param userId brugerens ID.
     * @return den fundne bruger eller null hvis ingen bruger blev fundet.
     */
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
                            rs.getDate("birthday").toLocalDate(),
                            rs.getString("gender").charAt(0),
                            rs.getDouble("weight"),
                            rs.getDouble("height"),
                            rs.getInt("activityLevel"),
                            rs.getInt("goal"),
                            rs.getInt("employed"),
                            rs.getBoolean("subscriber")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    /**
     * Opretter en ingrediens i databasen.
     *
     * @param ingredient den ingrediens der skal oprettes.
     */
    public void createIngredients(Ingredient ingredient) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = getConnection();
            String sql = "INSERT INTO Ingredient (name, fat, carbohydrate, protein, calories) VALUES (?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, ingredient.getName());
            pstmt.setDouble(2, ingredient.getFat());
            pstmt.setDouble(3, ingredient.getCarbohydrate());
            pstmt.setDouble(4, ingredient.getProtein());
            pstmt.setInt(5, ingredient.getCalories());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error connecting to the database or executing the query.");
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt);
        }
    }

    /**
     * Henter en ingrediens baseret på dens ID.
     *
     * @param ingredientId ingrediensens ID.
     * @return den fundne ingrediens eller null hvis ingen ingrediens blev fundet.
     */
    public Ingredient getIngredientById(int ingredientId) {
        Ingredient ingredient = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            String sql = "SELECT * FROM Ingredient WHERE ingredientId = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, ingredientId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                ingredient = new Ingredient();
                ingredient.setIngredientId(rs.getInt("ingredientId"));
                ingredient.setName(rs.getString("name"));
                ingredient.setFat(rs.getDouble("fat"));
                ingredient.setCarbohydrate(rs.getDouble("carbohydrate"));
                ingredient.setProtein(rs.getDouble("protein"));
                ingredient.setCalories(rs.getInt("calories"));
                // Add this if you have a quantity field in your database for ingredients
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }
        return ingredient;
    }


    /**
     * Opretter en opskrift med tilhørende ingredienser i databasen.
     *
     * @param recipe         opskriften der skal oprettes.
     * @param ingredientIds  liste over ingrediens IDs.
     * @param quantities     liste over mængder af hver ingrediens.
     */
    public void createRecipeWithIngredients(Recipe recipe, List<Integer> ingredientIds, List<Double> quantities) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            conn.setAutoCommit(false);

            String recipeSql = "INSERT INTO Recipe (title, description, method, cookingTime, imageUrl, meal_time, total_calories, total_protein, total_fat, total_carbohydrates, week) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(recipeSql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, recipe.getTitle());
            pstmt.setString(2, recipe.getDescription());
            pstmt.setString(3, recipe.getMethod());
            pstmt.setString(4, recipe.getCookingTime());
            pstmt.setString(5, recipe.getImageUrl());
            pstmt.setString(6, recipe.getMealTime());
            pstmt.setInt(7, recipe.getTotalCalories());
            pstmt.setInt(8, recipe.getTotalProtein());
            pstmt.setInt(9, recipe.getTotalFat());
            pstmt.setInt(10, recipe.getTotalCarbohydrates());
            pstmt.setString(11, recipe.getWeek());
            pstmt.executeUpdate();

            rs = pstmt.getGeneratedKeys();
            int recipeId = 0;
            if (rs.next()) {
                recipeId = rs.getInt(1);
            }

            String recipeIngredientsSql = "INSERT INTO Recipe_Ingredients (recipe_id, ingredient_id, quantity) VALUES (?, ?, ?)";
            pstmt = conn.prepareStatement(recipeIngredientsSql);
            for (int i = 0; i < ingredientIds.size(); i++) {
                pstmt.setInt(1, recipeId);
                pstmt.setInt(2, ingredientIds.get(i));
                pstmt.setDouble(3, quantities.get(i));
                pstmt.executeUpdate();
            }

            conn.commit();
        } catch (SQLException e) {
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }
    }

    /**
     * Henter alle ingredienser fra databasen.
     *
     * @return en liste over alle ingredienser.
     */
    public List<Ingredient> getAllIngredients() {
        List<Ingredient> ingredientList = new ArrayList<>(); //laver en ny arraylist
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null; //vi initialesere vores resultatsæt med null så der ikke er noget til at starte med

        try {
            conn = getConnection();
            String sql = "SELECT * FROM Ingredient"; //henter alt fra databasen
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery(); // Det var der vi kører vores query. alt vi får fra db tilføjer vi
            while (rs.next()) { //while der stadig er ingredienter i vores database kører den laver nye ingredient objekter fra vores eksisterende,
                Ingredient ingredient = new Ingredient();
                ingredient.setIngredientId(rs.getInt("ingredientId"));
                ingredient.setName(rs.getString("name"));
                ingredient.setFat(rs.getDouble("fat"));
                ingredient.setCarbohydrate(rs.getDouble("carbohydrate"));
                ingredient.setProtein(rs.getDouble("protein"));
                ingredient.setCalories(rs.getInt("calories"));
                ingredientList.add(ingredient);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }
        return ingredientList; //bliver tilføjet til vores liste og den bliver returnet til vores controller, nu kender html til vores ingredienser
    }


    /**
     * Opretter et nyt abonnement i databasen.
     *
     * @param subscription det abonnement der skal oprettes.
     */
    public void createSubscription(Subscription subscription) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = getConnection();
            String sql = "INSERT INTO Subscription (subscriptionId, userId, startDate, endDate, price, status) VALUES (?, ?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, subscription.getSubscriptionId());
            pstmt.setInt(2, subscription.getUserId());
            pstmt.setDate(3, new java.sql.Date(subscription.getStartDate().getTime()));
            pstmt.setDate(4, new java.sql.Date(subscription.getEndDate().getTime()));
            pstmt.setDouble(5, subscription.getPrice());
            pstmt.setString(6, subscription.getStatus());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt);
        }
    }

    /**
     * Opdaterer abonnementsstatus for en bruger baseret på deres ID.
     *
     * @param userId       brugerens ID.
     * @param isSubscriber den nye abonnementsstatus.
     */
    public void updateUserSubscriptionStatus(int userId, boolean isSubscriber) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = getConnection();
            String sql = "UPDATE User SET subscriber = ? WHERE userId = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setBoolean(1, isSubscriber);
            pstmt.setInt(2, userId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error connecting to the database or executing the query.");
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt);
        }
    }

    /**
     * Opdaterer status for et abonnement baseret på dets ID.
     *
     * @param subscriptionId abonnementets ID.
     * @param status         den nye status.
     */
    public void updateSubscriptionStatus(String subscriptionId, String status) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = getConnection();
            String sql = "UPDATE Subscription SET status = ? WHERE subscriptionId = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, status);
            pstmt.setString(2, subscriptionId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error connecting to the database or executing the query: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt);
        }
    }

    /**
     * Henter alle opskrifter fra databasen.
     *
     * @return en liste over alle opskrifter.
     */
    public List<Recipe> getAllRecipes() {
        List<Recipe> recipes = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            String sql = "SELECT * FROM Recipe";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Recipe recipe = new Recipe();
                recipe.setRecipeId(rs.getInt("recipeId"));
                recipe.setTitle(rs.getString("title"));
                recipe.setDescription(rs.getString("description"));
                recipe.setMethod(rs.getString("method"));
                recipe.setCookingTime(rs.getString("cookingTime"));
                recipe.setImageUrl(rs.getString("imageUrl"));
                recipe.setMealTime(rs.getString("meal_time"));
                recipe.setTotalCalories(rs.getInt("total_calories"));
                recipe.setTotalProtein(rs.getInt("total_protein"));
                recipe.setTotalFat(rs.getInt("total_fat"));
                recipe.setTotalCarbohydrates(rs.getInt("total_carbohydrates"));
                recipe.setWeek(rs.getString("week"));
                recipes.add(recipe);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }
        return recipes;
    }

    /**
     * Henter en opskrift baseret på dens ID.
     *
     * @param id opskriftens ID.
     * @return den fundne opskrift eller null hvis ingen opskrift blev fundet.
     */
    public Recipe getRecipeById(int id) {
        Recipe recipe = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            String sql = "SELECT * FROM Recipe WHERE recipeId = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                recipe = new Recipe();
                recipe.setRecipeId(rs.getInt("recipeId"));
                recipe.setTitle(rs.getString("title"));
                recipe.setDescription(rs.getString("description"));
                recipe.setMethod(rs.getString("method"));
                recipe.setCookingTime(rs.getString("cookingTime"));
                recipe.setImageUrl(rs.getString("imageUrl"));
                recipe.setMealTime(rs.getString("meal_time"));
                recipe.setTotalCalories(rs.getInt("total_calories"));
                recipe.setTotalProtein(rs.getInt("total_protein"));
                recipe.setTotalFat(rs.getInt("total_fat"));
                recipe.setTotalCarbohydrates(rs.getInt("total_carbohydrates"));
                recipe.setWeek(rs.getString("week"));

                // Initialiser ingredienslisten
                recipe.setIngredients(new ArrayList<>());

                // Hent og tilføj ingredienser til opskriften
                String ingredientSql = "SELECT i.*, ri.quantity FROM Ingredient i " +
                        "JOIN Recipe_Ingredients ri ON i.ingredientId = ri.ingredient_id " +
                        "WHERE ri.recipe_id = ?";
                try (PreparedStatement ingredientPstmt = conn.prepareStatement(ingredientSql)) {
                    ingredientPstmt.setInt(1, id);
                    try (ResultSet ingredientRs = ingredientPstmt.executeQuery()) {
                        while (ingredientRs.next()) {
                            Ingredient ingredient = new Ingredient();
                            ingredient.setIngredientId(ingredientRs.getInt("ingredientId"));
                            ingredient.setName(ingredientRs.getString("name"));
                            ingredient.setCalories(ingredientRs.getInt("calories"));
                            ingredient.setFat(ingredientRs.getDouble("fat"));
                            ingredient.setProtein(ingredientRs.getDouble("protein"));
                            ingredient.setCarbohydrate(ingredientRs.getDouble("carbohydrate"));
                            ingredient.setQuantity(ingredientRs.getDouble("quantity"));
                            recipe.getIngredients().add(ingredient);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }
        return recipe;
    }

    /**
     * Henter det seneste abonnement baseret på brugerens ID.
     *
     * @param userId brugerens ID.
     * @return det seneste abonnement eller null hvis ingen abonnement blev fundet.
     */
    public Subscription getLatestSubscriptionByUserId(int userId) {
        Subscription subscription = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            String sql = "SELECT * FROM Subscription WHERE userId = ? ORDER BY endDate DESC LIMIT 1";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                subscription = new Subscription();
                subscription.setSubscriptionId(rs.getString("subscriptionId"));
                subscription.setUserId(rs.getInt("userId"));
                subscription.setStartDate(rs.getDate("startDate"));
                subscription.setEndDate(rs.getDate("endDate"));
                subscription.setPrice(rs.getDouble("price"));
                subscription.setStatus(rs.getString("status"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }

        return subscription;
    }

    /**
     * Sletter inaktive abonnementer baseret på brugerens ID.
     *
     * @param userId brugerens ID.
     */
    @Deprecated
    public void deleteInactiveSubscriptionsByUserId(int userId) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = getConnection();
            String sql = "DELETE FROM Subscription WHERE userId = ? AND status = 'inactive'";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt);
        }
    }

    /**
     * Opdaterer en opskrift og dens ingredienser i databasen.
     *
     * @param recipe        opskriften der skal opdateres.
     * @param ingredientIds liste over ingrediens IDs.
     * @param quantities    liste over mængder for hver ingrediens.
     */
    public void updateRecipeWithIngredients(Recipe recipe, List<Integer> ingredientIds, List<Double> quantities) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            conn.setAutoCommit(false);

            String recipeSql = "UPDATE Recipe SET title=?, description=?, method=?, cookingTime=?, imageUrl=?, meal_time=?, total_calories=?, total_protein=?, total_fat=?, total_carbohydrates=?, week=? WHERE recipeId=?";
            pstmt = conn.prepareStatement(recipeSql);
            pstmt.setString(1, recipe.getTitle());
            pstmt.setString(2, recipe.getDescription());
            pstmt.setString(3, recipe.getMethod());
            pstmt.setString(4, recipe.getCookingTime());
            pstmt.setString(5, recipe.getImageUrl());
            pstmt.setString(6, recipe.getMealTime());
            pstmt.setInt(7, recipe.getTotalCalories());
            pstmt.setInt(8, recipe.getTotalProtein());
            pstmt.setInt(9, recipe.getTotalFat());
            pstmt.setInt(10, recipe.getTotalCarbohydrates());
            pstmt.setString(11, recipe.getWeek());
            pstmt.setInt(12, recipe.getRecipeId());
            pstmt.executeUpdate();

            String deleteOldIngredientsSql = "DELETE FROM Recipe_Ingredients WHERE recipe_id=?";
            pstmt = conn.prepareStatement(deleteOldIngredientsSql);
            pstmt.setInt(1, recipe.getRecipeId());
            pstmt.executeUpdate();

            String recipeIngredientsSql = "INSERT INTO Recipe_Ingredients (recipe_id, ingredient_id, quantity) VALUES (?, ?, ?)";
            pstmt = conn.prepareStatement(recipeIngredientsSql);
            for (int i = 0; i < ingredientIds.size(); i++) {
                pstmt.setInt(1, recipe.getRecipeId());
                pstmt.setInt(2, ingredientIds.get(i));
                pstmt.setDouble(3, quantities.get(i));
                pstmt.executeUpdate();
            }

            conn.commit();
        } catch (SQLException e) {
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }
    }

    /**
     * Sletter en opskrift baseret på dens ID.
     *
     * @param id opskriftens ID.
     * @return true hvis opskriften blev slettet, ellers false.
     */
    public boolean deleteRecipeById(int id) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = getConnection();
            conn.setAutoCommit(false);

            // Først, slet fra Recipe_Ingredients
            String deleteRecipeIngredientsSql = "DELETE FROM Recipe_Ingredients WHERE recipe_id = ?";
            pstmt = conn.prepareStatement(deleteRecipeIngredientsSql);
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            pstmt.close();

            // Derefter, slet fra Recipe
            String deleteRecipeSql = "DELETE FROM Recipe WHERE recipeId = ?";
            pstmt = conn.prepareStatement(deleteRecipeSql);
            pstmt.setInt(1, id);
            int affectedRows = pstmt.executeUpdate();

            conn.commit();
            return affectedRows > 0;
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
            return false;
        } finally {
            closeResources(conn, pstmt);
        }
    }

    /**
     * Opdaterer en ingrediens i databasen.
     *
     * @param ingredient den ingrediens der skal opdateres.
     */
    public void updateIngredient(Ingredient ingredient) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = getConnection();
            String sql = "UPDATE Ingredient SET name = ?, fat = ?, carbohydrate = ?, protein = ?, calories = ? WHERE ingredientId = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, ingredient.getName());
            pstmt.setDouble(2, ingredient.getFat());
            pstmt.setDouble(3, ingredient.getCarbohydrate());
            pstmt.setDouble(4, ingredient.getProtein());
            pstmt.setInt(5, ingredient.getCalories());
            pstmt.setInt(6, ingredient.getIngredientId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt);
        }
    }

    /**
     * Sletter en ingrediens baseret på dens ID.
     *
     * @param id ingrediensens ID.
     * @return true hvis ingrediensen blev slettet, ellers false.
     */
    public boolean deleteIngredientById(int id) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = getConnection();
            conn.setAutoCommit(false);

            // Først, slet fra Recipe_Ingredients hvor denne ingrediens bruges
            String deleteRecipeIngredientsSql = "DELETE FROM Recipe_Ingredients WHERE ingredient_id = ?";
            pstmt = conn.prepareStatement(deleteRecipeIngredientsSql);
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            pstmt.close();

            // Derefter, slet fra Ingredient
            String deleteIngredientSql = "DELETE FROM Ingredient WHERE ingredientId = ?";
            pstmt = conn.prepareStatement(deleteIngredientSql);
            pstmt.setInt(1, id);
            int affectedRows = pstmt.executeUpdate();

            conn.commit();
            return affectedRows > 0;
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
            return false;
        } finally {
            closeResources(conn, pstmt);
        }
    }

    /**
     * Tilføjer en ingrediens til indkøbslisten for en bruger.
     *
     * @param userId     brugerens ID.
     * @param ingredient ingrediensen der skal tilføjes.
     */
    public void addIngredientToShoppingList(int userId, Ingredient ingredient) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();

            // Check if the ingredient already exists in the shopping list
            String checkSql = "SELECT quantity FROM ShoppingList WHERE user_id = ? AND ingredient_id = ?";
            pstmt = conn.prepareStatement(checkSql);
            pstmt.setInt(1, userId);
            pstmt.setInt(2, ingredient.getIngredientId());
            rs = pstmt.executeQuery();

            if (rs.next()) {
                // If the ingredient exists, update the quantity
                double existingQuantity = rs.getDouble("quantity");
                double newQuantity = existingQuantity + ingredient.getQuantity();

                String updateSql = "UPDATE ShoppingList SET quantity = ? WHERE user_id = ? AND ingredient_id = ?";
                pstmt = conn.prepareStatement(updateSql);
                pstmt.setDouble(1, newQuantity);
                pstmt.setInt(2, userId);
                pstmt.setInt(3, ingredient.getIngredientId());
                pstmt.executeUpdate();
            } else {
                // If the ingredient does not exist, insert a new record
                String insertSql = "INSERT INTO ShoppingList (user_id, ingredient_id, quantity) VALUES (?, ?, ?)";
                pstmt = conn.prepareStatement(insertSql);
                pstmt.setInt(1, userId);
                pstmt.setInt(2, ingredient.getIngredientId());
                pstmt.setDouble(3, ingredient.getQuantity());
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }
    }

    /**
     * Henter indkøbslisten for en bruger baseret på deres ID.
     *
     * @param userId brugerens ID.
     * @return en liste af ingredienser på brugerens indkøbsliste.
     */
    public List<Ingredient> getShoppingListByUserId(int userId) {
        List<Ingredient> shoppingList = new ArrayList<>();
        String sql = "SELECT i.ingredientId, i.name, sl.quantity, sl.bought FROM ShoppingList sl " +
                "JOIN Ingredient i ON sl.ingredient_id = i.ingredientId WHERE sl.user_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Ingredient ingredient = new Ingredient();
                    ingredient.setIngredientId(rs.getInt("ingredientId"));
                    ingredient.setName(rs.getString("name"));
                    ingredient.setQuantity(rs.getDouble("quantity"));
                    ingredient.setBought(rs.getBoolean("bought")); // Add this line
                    shoppingList.add(ingredient);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return shoppingList;
    }

    /**
     * Opdaterer købstatus for en ingrediens i en brugers indkøbsliste.
     *
     * @param userId       brugerens ID.
     * @param ingredientId ingrediensens ID.
     * @param bought       den nye købstatus.
     */
    public void updateBoughtStatus(int userId, int ingredientId, boolean bought) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = getConnection();
            String sql = "UPDATE ShoppingList SET bought = ? WHERE user_id = ? AND ingredient_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setBoolean(1, bought);
            pstmt.setInt(2, userId);
            pstmt.setInt(3, ingredientId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt);
        }
    }

    /**
     * Sletter alle ingredienser i en brugers indkøbsliste.
     *
     * @param userId brugerens ID.
     */
    public void clearAll(int userId) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = getConnection();
            String sql = "DELETE FROM ShoppingList WHERE user_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt);
        }
    }

    /**
     * Sletter alle markerede (købte) ingredienser i en brugers indkøbsliste.
     *
     * @param userId brugerens ID.
     */
    public void clearMarked(int userId) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = getConnection();
            String sql = "DELETE FROM ShoppingList WHERE user_id = ? AND bought = true";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt);
        }
    }

    /**
     * Tilføjer en opskrift til en brugers favoritliste.
     *
     * @param userId   brugerens ID.
     * @param recipeId opskriftens ID.
     */
    public void addFavoriteRecipe(int userId, int recipeId) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = getConnection();
            String sql = "INSERT INTO UserFavoriteRecipe (UserID, RecipeID) VALUES (?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            pstmt.setInt(2, recipeId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt);
        }
    }

    /**
     * Henter alle favoritopskrifter for en bruger baseret på deres ID.
     *
     * @param userId brugerens ID.
     * @return en liste af favoritopskrifter IDs.
     */
    public List<Integer> getFavoriteRecipeIdsByUserId(int userId) {
        List<Integer> favoriteRecipeIds = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            String sql = "SELECT recipeId FROM UserFavoriteRecipe WHERE userId = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                favoriteRecipeIds.add(rs.getInt("recipeId"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }
        return favoriteRecipeIds;
    }

    /**
     * Fjerner en opskrift fra en brugers favoritliste.
     *
     * @param userId   brugerens ID.
     * @param recipeId opskriftens ID.
     */
    public void removeFavoriteRecipe(int userId, int recipeId) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = getConnection();
            String sql = "DELETE FROM UserFavoriteRecipe WHERE UserID = ? AND RecipeID = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            pstmt.setInt(2, recipeId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt);
        }
    }

    /**
     * Opdaterer mængden af en ingrediens i en brugers indkøbsliste.
     *
     * @param userId       brugerens ID.
     * @param ingredientId ingrediensens ID.
     * @param quantity     den nye mængde.
     */
    public void updateQuantity(int userId, int ingredientId, int quantity) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = getConnection();
            String sql = "UPDATE ShoppingList SET quantity = ? WHERE user_id = ? AND ingredient_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, quantity);
            pstmt.setInt(2, userId);
            pstmt.setInt(3, ingredientId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt);
        }
    }

}

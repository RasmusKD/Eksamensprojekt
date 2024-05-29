package com.minkostplan.eksamensprojekt.Repository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * DatabaseSetup er ansvarlig for initial opsætning af databasen og dens tabeller.
 */
@Component
public class DatabaseSetup {

    private static final String DATABASE_NAME = "wentzelevent_dk_db_min_kostplan_lauge";

    @Value("${spring.datasource.url}")
    private String jdbcUrl;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    /**
     * Metoden kører automatisk efter objektets initialisering og opsætter databasen og dens tabeller.
     */
    @PostConstruct
    public void setupDatabase() {
        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
            createDatabase(connection);
            createIngredientTable(connection);
            createRecipeTable(connection);
            createRecipeIngredientsTable(connection);
            createSubscriptionTable(connection);
            createUserTable(connection);

            System.out.println("Database setup completed successfully. Everything is in order.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void createDatabase(Connection connection) throws SQLException {
        if (databaseExists(connection)) {
            System.out.println("Database already exists. No issues.");
            return;
        }

        String createDatabaseSQL = "CREATE DATABASE IF NOT EXISTS `" + DATABASE_NAME + "` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;";
        try (Statement statement = connection.createStatement()) {
            statement.execute(createDatabaseSQL);
            System.out.println("Database created successfully.");
        }
    }

    private static boolean databaseExists(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute("SHOW DATABASES LIKE '" + DATABASE_NAME + "'");
            return statement.getResultSet().next();
        }
    }

    private static void createIngredientTable(Connection connection) throws SQLException {
        String createTableSQL = """
                CREATE TABLE IF NOT EXISTS `Ingredient` (
                  `ingredientId` int NOT NULL AUTO_INCREMENT,
                  `name` varchar(255) DEFAULT NULL,
                  `fat` double DEFAULT NULL,
                  `carbohydrate` double DEFAULT NULL,
                  `protein` double DEFAULT NULL,
                  `calories` int DEFAULT NULL,
                  PRIMARY KEY (`ingredientId`)
                ) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=latin1;
                """;
        try (Statement statement = connection.createStatement()) {
            statement.execute(createTableSQL);
        }
    }

    private static void createRecipeTable(Connection connection) throws SQLException {
        String createTableSQL = """
                CREATE TABLE IF NOT EXISTS `Recipe` (
                  `recipeId` int NOT NULL AUTO_INCREMENT,
                  `title` varchar(255) DEFAULT NULL,
                  `description` text,
                  `method` text,
                  `cookingTime` varchar(255) DEFAULT NULL,
                  `imageUrl` varchar(255) DEFAULT NULL,
                  `meal_time` enum('Breakfast','Lunch','Dinner') DEFAULT NULL,
                  `total_calories` int DEFAULT NULL,
                  `total_protein` int DEFAULT NULL,
                  `total_fat` int DEFAULT NULL,
                  `total_carbohydrates` int DEFAULT NULL,
                  `day` varchar(255) DEFAULT NULL,
                  PRIMARY KEY (`recipeId`)
                ) ENGINE=InnoDB AUTO_INCREMENT=145 DEFAULT CHARSET=latin1;
                """;
        try (Statement statement = connection.createStatement()) {
            statement.execute(createTableSQL);
        }
    }

    private static void createRecipeIngredientsTable(Connection connection) throws SQLException {
        String createTableSQL = """
                CREATE TABLE IF NOT EXISTS `Recipe_Ingredients` (
                  `recipe_id` int NOT NULL,
                  `ingredient_id` int NOT NULL,
                  `quantity` double DEFAULT NULL,
                  PRIMARY KEY (`recipe_id`,`ingredient_id`),
                  KEY `ingredient_id` (`ingredient_id`)
                ) ENGINE=InnoDB DEFAULT CHARSET=latin1;
                """;
        try (Statement statement = connection.createStatement()) {
            statement.execute(createTableSQL);
        }
    }

    private static void createSubscriptionTable(Connection connection) throws SQLException {
        String createTableSQL = """
                CREATE TABLE IF NOT EXISTS `Subscription` (
                  `subscriptionId` varchar(255) NOT NULL,
                  `userId` int DEFAULT NULL,
                  `startDate` date DEFAULT NULL,
                  `endDate` date DEFAULT NULL,
                  `price` double DEFAULT NULL,
                  `status` varchar(255) DEFAULT NULL,
                  PRIMARY KEY (`subscriptionId`),
                  KEY `userId` (`userId`)
                ) ENGINE=InnoDB DEFAULT CHARSET=latin1;
                """;
        try (Statement statement = connection.createStatement()) {
            statement.execute(createTableSQL);
        }
    }

    private static void createUserTable(Connection connection) throws SQLException {
        String createTableSQL = """
                CREATE TABLE IF NOT EXISTS `User` (
                  `userId` int NOT NULL AUTO_INCREMENT,
                  `firstName` varchar(255) DEFAULT NULL,
                  `lastName` varchar(255) DEFAULT NULL,
                  `email` varchar(255) DEFAULT NULL,
                  `password` varchar(255) DEFAULT NULL,
                  `age` int DEFAULT NULL,
                  `gender` char(1) DEFAULT NULL,
                  `weight` double DEFAULT NULL,
                  `height` double DEFAULT NULL,
                  `activityLevel` int DEFAULT '0',
                  `goal` int DEFAULT '0',
                  `employed` int DEFAULT NULL,
                  `subscriber` tinyint(1) NOT NULL DEFAULT '0',
                  PRIMARY KEY (`userId`),
                  UNIQUE KEY `email` (`email`)
                ) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=latin1;
                """;
        try (Statement statement = connection.createStatement()) {
            statement.execute(createTableSQL);
        }
    }
}

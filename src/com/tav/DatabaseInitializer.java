package com.tav;

import com.tav.model.enums.UserType;
import com.tav.util.JDBCUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;

public class DatabaseInitializer {

    public static void main(String[] args) {
        try {
            setupDatabase();
            injectData();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            dropTables();
        }
    }

    private static void dropTables() {
        try {
            String query = "DROP TABLE `example_db_1`.`groups`, `example_db_1`.`user_to_group`, `example_db_1`.`users`";
            JDBCUtil.getConnection().prepareStatement(query).execute();
        } catch (SQLException e) {
            System.err.println("Failed to drop schema");
        }
    }

    // Using DDL (Data Definition Language) to create a db
     public static void setupDatabase() throws SQLException {
        JDBCUtil.getConnection().prepareStatement("CREATE TABLE `example_db_1`.`users` (\n" +
                "  `id` BIGINT NOT NULL AUTO_INCREMENT,\n" +
                "  `email` VARCHAR(45) NOT NULL,\n" +
                "  `password` INT NOT NULL,\n" +
                "  `type` ENUM('ADMIN', 'REGULAR') NOT NULL,\n" +
                "  PRIMARY KEY (`id`),\n" +
                "  UNIQUE INDEX `idusers_UNIQUE` (`id` ASC) VISIBLE,\n" +
                "  UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE);\n"
        ).execute();

         JDBCUtil.getConnection().prepareStatement("CREATE TABLE `example_db_1`.`groups` (\n" +
                 "  `id` BIGINT NOT NULL AUTO_INCREMENT,\n" +
                 "  `name` VARCHAR(45) NOT NULL,\n" +
                 "  PRIMARY KEY (`id`),\n" +
                 "  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE,\n" +
                 "  UNIQUE INDEX `name_UNIQUE` (`name` ASC) VISIBLE);\n"
         ).execute();

         JDBCUtil.getConnection().prepareStatement("CREATE TABLE `example_db_1`.`user_to_group` (\n" +
                 "  `group_id` BIGINT NOT NULL,\n" +
                 "  `user_id` BIGINT NOT NULL);\n"
         ).execute();
    }

    // Using DML (Data manipulation language) to inject data into the database
    public static void injectData() throws SQLException {
        createUsers();
        createGroups();
    }

    private static void createGroups() throws SQLException {
        String[] groupsNames = { "Java Lovers", "Harry Potter Fans" };
        for (String groupName : groupsNames) {
            String insertQuery = "INSERT INTO example_db_1.groups (name)" + " VALUES ('" + groupName + "')";
            JDBCUtil.getConnection().prepareStatement(insertQuery).executeUpdate();
        }
    }

    private static void createUsers() throws SQLException {
        String[] names = { "yair", "oz", "tanya", "meital", "or", "emil", "idan", "yvgeni", "dor", "omer",
                           "david1", "david2", "daniel", "olga", "noam", "amit", "lior", "nitay" };

        for (String name : names) {
            String email = name + "@jb.co.il";
            int hashedPassword = "abc123".hashCode();
            String insertUserQuery = "INSERT INTO example_db_1.users (email, password, type) VALUES (?, ?, ?)";
            PreparedStatement insertUserStatement = JDBCUtil.getConnection().prepareStatement(insertUserQuery, Statement.RETURN_GENERATED_KEYS);
            insertUserStatement.setString(1, email);
            insertUserStatement.setInt(2, hashedPassword);
            insertUserStatement.setString(3, String.valueOf(UserType.REGULAR));
            insertUserStatement.executeUpdate();
            ResultSet generatedKeysResult = insertUserStatement.getGeneratedKeys();
            if (!generatedKeysResult.next()) {
                throw new RuntimeException("Failed to retrieve auto-incremented id");
            }
            long id = generatedKeysResult.getLong(1);

            if (id % 2 == 0) {
                String registerUserToGroupQuery = "INSERT INTO user_to_group (group_id, user_id) VALUES (?, ?)";
                PreparedStatement registerUserToGroupStatement = JDBCUtil.getConnection().prepareStatement(
                        registerUserToGroupQuery, Statement.RETURN_GENERATED_KEYS
                );
                registerUserToGroupStatement.setLong(1, new Random().nextInt(2) + 1);
                registerUserToGroupStatement.setLong(2, id);
                registerUserToGroupStatement.executeUpdate();
            }
        }

        String insertQuery = "INSERT INTO example_db_1.users (email, password, type) VALUES (?, ?, ?)";
        PreparedStatement preparedStatement = JDBCUtil.getConnection().prepareStatement(insertQuery);
        preparedStatement.setString(1, "tav@jb.co.il");
        preparedStatement.setInt(2, "admin".hashCode());
        preparedStatement.setString(3, String.valueOf(UserType.ADMIN));
        preparedStatement.executeUpdate();
    }

}

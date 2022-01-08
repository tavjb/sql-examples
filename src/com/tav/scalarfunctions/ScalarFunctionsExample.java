package com.tav.scalarfunctions;

import com.tav.util.JDBCUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ScalarFunctionsExample {
    public static void main(String[] args) throws SQLException {
        System.out.println("---------------------------------------------");
        printAllFirstEmailLetters();
        System.out.println("---------------------------------------------");
        printUpperCaseEmail();
    }

    public static void printAllFirstEmailLetters() throws SQLException {
        String query = "SELECT LEFT(email, 1) AS `name` FROM users";
        PreparedStatement preparedStatement = JDBCUtil.getConnection().prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            System.out.println(resultSet.getString("name"));
        }
    }

    public static void printUpperCaseEmail() throws SQLException {
        String query = "SELECT UPPER(email) AS `uppercase_email` FROM users";
        PreparedStatement preparedStatement = JDBCUtil.getConnection().prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            System.out.println(resultSet.getString("uppercase_email"));
        }
    }
}

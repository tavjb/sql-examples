package com.tav.joinstatements;

import com.tav.util.JDBCUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JoinStatementExample {
    public static void main(String[] args) throws SQLException {
        System.out.println("---------------------------------");
        joinUsersAndUserToGroup();
        System.out.println("---------------------------------");
        leftJoinUsersAndUserToGroup();
    }

    private static void leftJoinUsersAndUserToGroup() throws SQLException {
        String query = "SELECT email, `password`, `type`, group_id AS `group` FROM users LEFT JOIN user_to_group ON id=user_id";

        PreparedStatement preparedStatement = JDBCUtil.getConnection().prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            System.out.println("email: " + resultSet.getString("email") + ", group: " + resultSet.getString("group"));
        }
    }

    private static void joinUsersAndUserToGroup() throws SQLException {
        String query = "SELECT email, `password`, `type`, group_id AS `group` FROM users JOIN user_to_group ON id=user_id";

        PreparedStatement preparedStatement = JDBCUtil.getConnection().prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            System.out.println("email: " + resultSet.getString("email") + ", group: " + resultSet.getString("group"));
        }
    }
}

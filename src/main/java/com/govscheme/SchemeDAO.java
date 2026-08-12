package com.govscheme;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

// Contains all database queries used by the project.
public class SchemeDAO {

    private static final String URL = "jdbc:mysql://localhost:3306/government_scheme_db";
    private static final String USER = "root";
    private static final String PASSWORD = "password";

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // Overloading: this version accepts a User object.
    public ArrayList<Scheme> getEligibleSchemes(User user) throws SQLException {
        return getEligibleSchemes(
                user.getAge(),
                user.getIncome(),
                user.getGender(),
                user.getCategory(),
                user.getOccupation(),
                user.getState()
        );
    }

    // Overloading: this version accepts the eligibility values directly.
    public ArrayList<Scheme> getEligibleSchemes(
            int age,
            double income,
            String gender,
            String category,
            String occupation,
            String stateName) throws SQLException {

        ArrayList<Scheme> schemes = new ArrayList<>();

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        // Find schemes that match age, income, gender, category,
        // occupation, and state.
        String sql =
                "SELECT * FROM Schemes " +
                "WHERE ? BETWEEN min_age AND max_age " +
                "AND ? BETWEEN min_income AND max_income " +
                "AND (gender = 'ALL' OR gender = ?) " +
                "AND (category = 'ANY' OR category = ?) " +
                "AND (occupation = 'ANY' OR occupation = ?) " +
                "AND state IN ('All India (Central)', ?)";

        try {
            connection = getConnection();
            statement = connection.prepareStatement(sql);

            statement.setInt(1, age);
            statement.setDouble(2, income);
            statement.setString(3, gender);
            statement.setString(4, category);
            statement.setString(5, occupation);
            statement.setString(6, stateName);

            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                schemes.add(createScheme(resultSet));
            }

        } catch (SQLException exception) {
            throw exception;
        } finally {
            closeResources(connection, statement, resultSet);
        }

        return schemes;
    }

    // Add a new scheme.
    public boolean addScheme(Scheme scheme) throws SQLException {

        Connection connection = null;
        PreparedStatement statement = null;

        String sql =
                "INSERT INTO Schemes " +
                "(scheme_name, min_age, max_age, min_income, max_income, " +
                "gender, occupation, category, state, description) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            connection = getConnection();
            statement = connection.prepareStatement(sql);

            statement.setString(1, scheme.getSchemeName());
            statement.setInt(2, scheme.getMinAge());
            statement.setInt(3, scheme.getMaxAge());
            statement.setDouble(4, scheme.getMinIncome());
            statement.setDouble(5, scheme.getMaxIncome());
            statement.setString(6, scheme.getGender());
            statement.setString(7, scheme.getOccupation());
            statement.setString(8, scheme.getCategory());
            statement.setString(9, scheme.getState());
            statement.setString(10, scheme.getDescription());

            return statement.executeUpdate() > 0;

        } catch (SQLException exception) {
            throw exception;
        } finally {
            closeResources(connection, statement, null);
        }
    }

    // Update an existing scheme.
    public boolean updateScheme(Scheme scheme) throws SQLException {

        Connection connection = null;
        PreparedStatement statement = null;

        String sql =
                "UPDATE Schemes SET " +
                "scheme_name = ?, " +
                "min_age = ?, " +
                "max_age = ?, " +
                "min_income = ?, " +
                "max_income = ?, " +
                "gender = ?, " +
                "occupation = ?, " +
                "category = ?, " +
                "state = ?, " +
                "description = ? " +
                "WHERE scheme_id = ?";

        try {
            connection = getConnection();
            statement = connection.prepareStatement(sql);

            statement.setString(1, scheme.getSchemeName());
            statement.setInt(2, scheme.getMinAge());
            statement.setInt(3, scheme.getMaxAge());
            statement.setDouble(4, scheme.getMinIncome());
            statement.setDouble(5, scheme.getMaxIncome());
            statement.setString(6, scheme.getGender());
            statement.setString(7, scheme.getOccupation());
            statement.setString(8, scheme.getCategory());
            statement.setString(9, scheme.getState());
            statement.setString(10, scheme.getDescription());
            statement.setInt(11, scheme.getSchemeId());

            return statement.executeUpdate() > 0;

        } catch (SQLException exception) {
            throw exception;
        } finally {
            closeResources(connection, statement, null);
        }
    }

    // Delete one scheme by ID.
    public boolean deleteScheme(int schemeId) throws SQLException {

        Connection connection = null;
        PreparedStatement statement = null;

        String sql = "DELETE FROM Schemes WHERE scheme_id = ?";

        try {
            connection = getConnection();
            statement = connection.prepareStatement(sql);

            statement.setInt(1, schemeId);

            return statement.executeUpdate() > 0;

        } catch (SQLException exception) {
            throw exception;
        } finally {
            closeResources(connection, statement, null);
        }
    }

    // Show all schemes in the database.
    public ArrayList<Scheme> getAllSchemes() throws SQLException {

        ArrayList<Scheme> schemes = new ArrayList<>();

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        String sql = "SELECT * FROM Schemes";

        try {
            connection = getConnection();
            statement = connection.prepareStatement(sql);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                schemes.add(createScheme(resultSet));
            }

        } catch (SQLException exception) {
            throw exception;
        } finally {
            closeResources(connection, statement, resultSet);
        }

        return schemes;
    }

    // Fetch dynamic options from the database.
    public ArrayList<String> getGenders() throws SQLException {
        return getStringList("SELECT gender_name FROM Genders");
    }

    public ArrayList<String> getOccupations() throws SQLException {
        return getStringList("SELECT occupation_name FROM Occupations");
    }

    public ArrayList<String> getCategories() throws SQLException {
        return getStringList("SELECT category_name FROM Categories");
    }

    public ArrayList<String> getStates() throws SQLException {
        return getStringList("SELECT state_name FROM States");
    }

    // Generic method for fetching a single-column String list.
    private ArrayList<String> getStringList(String sql) throws SQLException {

        ArrayList<String> list = new ArrayList<>();

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = getConnection();
            statement = connection.prepareStatement(sql);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                list.add(resultSet.getString(1));
            }

        } catch (SQLException exception) {
            throw exception;
        } finally {
            closeResources(connection, statement, resultSet);
        }

        return list;
    }

    // Convert the current ResultSet row into a Scheme object.
    private Scheme createScheme(ResultSet resultSet) throws SQLException {

        return new Scheme(
                resultSet.getInt("scheme_id"),
                resultSet.getString("scheme_name"),
                resultSet.getInt("min_age"),
                resultSet.getInt("max_age"),
                resultSet.getDouble("min_income"),
                resultSet.getDouble("max_income"),
                resultSet.getString("gender"),
                resultSet.getString("occupation"),
                resultSet.getString("category"),
                resultSet.getString("state"),
                resultSet.getString("description")
        );
    }

    // Close ResultSet first, then PreparedStatement, then Connection.
    private void closeResources(
            Connection connection,
            PreparedStatement statement,
            ResultSet resultSet) {

        try {
            if (resultSet != null) {
                resultSet.close();
            }
        } catch (SQLException exception) {
            System.out.println("Could not close ResultSet.");
        }

        try {
            if (statement != null) {
                statement.close();
            }
        } catch (SQLException exception) {
            System.out.println("Could not close PreparedStatement.");
        }

        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException exception) {
            System.out.println("Could not close Connection.");
        }
    }
}

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
        return getEligibleSchemes(user.getAge(), user.getIncome(), user.getGender(),
                user.getCategory(), user.getOccupation(), user.getState());
    }

    // Overloading: this version accepts the eligibility values directly.
    public ArrayList<Scheme> getEligibleSchemes(int age, double income, String gender,
            String category, String occupation, String stateName) throws SQLException {
        ArrayList<Scheme> schemes = new ArrayList<Scheme>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        // Find schemes that match age, income, gender, category, occupation, and state.
        String sql = "SELECT * FROM Schemes WHERE ? BETWEEN min_age AND max_age "
                + "AND ? <= max_income AND (gender = 'ALL' OR gender = ?) "
                + "AND (category = 'ANY' OR category = ?) "
                + "AND (occupation = 'ANY' OR occupation = ?) "
                + "AND state_id IN (SELECT state_id FROM States "
                + "WHERE state_name = 'All India (Central)' OR state_name = ?)";
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

    public boolean addScheme(Scheme scheme) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        // Insert a new scheme.
        String sql = "INSERT INTO Schemes (scheme_name, min_age, max_age, max_income, gender, occupation, category, state_id, description) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            connection = getConnection();
            statement = connection.prepareStatement(sql);
            statement.setString(1, scheme.getSchemeName());
            statement.setInt(2, scheme.getMinAge());
            statement.setInt(3, scheme.getMaxAge());
            statement.setDouble(4, scheme.getMaxIncome());
            statement.setString(5, scheme.getGender());
            statement.setString(6, scheme.getOccupation());
            statement.setString(7, scheme.getCategory());
            statement.setInt(8, scheme.getStateId());
            statement.setString(9, scheme.getDescription());
            return statement.executeUpdate() > 0;
        } catch (SQLException exception) {
            throw exception;
        } finally {
            closeResources(connection, statement, null);
        }
    }

    public Scheme getSchemeById(int schemeId) throws SchemeNotFoundException, SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        // Find one scheme by ID.
        String sql = "SELECT * FROM Schemes WHERE scheme_id = ?";
        try {
            connection = getConnection();
            statement = connection.prepareStatement(sql);
            statement.setInt(1, schemeId);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return createScheme(resultSet);
            }
            throw new SchemeNotFoundException("Scheme ID " + schemeId + " was not found.");
        } catch (SQLException exception) {
            throw exception;
        } finally {
            closeResources(connection, statement, resultSet);
        }
    }

    public boolean updateScheme(int schemeId, double newMaxIncome) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        // Update the income limit for one scheme.
        String sql = "UPDATE Schemes SET max_income = ? WHERE scheme_id = ?";
        try {
            connection = getConnection();
            statement = connection.prepareStatement(sql);
            statement.setDouble(1, newMaxIncome);
            statement.setInt(2, schemeId);
            return statement.executeUpdate() > 0;
        } catch (SQLException exception) {
            throw exception;
        } finally {
            closeResources(connection, statement, null);
        }
    }

    public boolean deleteScheme(int schemeId) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        // Delete one scheme by ID.
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

    public ArrayList<String> listSchemes() throws SQLException {
        ArrayList<String> schemes = new ArrayList<String>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        // Join Schemes and States to show a scheme with its state name.
        String sql = "SELECT s.scheme_name, st.state_name FROM Schemes s INNER JOIN States st ON s.state_id = st.state_id";
        try {
            connection = getConnection();
            statement = connection.prepareStatement(sql);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                schemes.add(resultSet.getString("scheme_name") + " - "
                        + resultSet.getString("state_name"));
            }
        } catch (SQLException exception) {
            throw exception;
        } finally {
            closeResources(connection, statement, resultSet);
        }
        return schemes;
    }

    public void countSchemesByState() throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        // Count schemes in each state using GROUP BY.
        String sql = "SELECT st.state_name, COUNT(s.scheme_id) AS total FROM States st LEFT JOIN Schemes s ON st.state_id = s.state_id GROUP BY st.state_name";
        try {
            connection = getConnection();
            statement = connection.prepareStatement(sql);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                System.out.println(resultSet.getString("state_name") + ": "
                        + resultSet.getInt("total"));
            }
        } catch (SQLException exception) {
            throw exception;
        } finally {
            closeResources(connection, statement, resultSet);
        }
    }

    public void statesHavingMoreThan(int number) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        // Find states that have more than the supplied number of schemes.
        String sql = "SELECT st.state_name, COUNT(s.scheme_id) AS total FROM States st INNER JOIN Schemes s ON st.state_id = s.state_id GROUP BY st.state_name HAVING COUNT(s.scheme_id) > ?";
        try {
            connection = getConnection();
            statement = connection.prepareStatement(sql);
            statement.setInt(1, number);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                System.out.println(resultSet.getString("state_name") + ": "
                        + resultSet.getInt("total"));
            }
        } catch (SQLException exception) {
            throw exception;
        } finally {
            closeResources(connection, statement, resultSet);
        }
    }

    public double secondHighestIncomeLimit() throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        // Find the second highest income limit using a subquery.
        String sql = "SELECT MAX(max_income) AS second_highest FROM Schemes WHERE max_income < (SELECT MAX(max_income) FROM Schemes)";
        try {
            connection = getConnection();
            statement = connection.prepareStatement(sql);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getDouble("second_highest");
            }
            return 0;
        } catch (SQLException exception) {
            throw exception;
        } finally {
            closeResources(connection, statement, resultSet);
        }
    }

    // Convert the current ResultSet row into a Scheme object.
    private Scheme createScheme(ResultSet resultSet) throws SQLException {
        return new Scheme(resultSet.getInt("scheme_id"), resultSet.getString("scheme_name"),
                resultSet.getInt("min_age"), resultSet.getInt("max_age"),
                resultSet.getDouble("max_income"), resultSet.getString("gender"),
                resultSet.getString("occupation"), resultSet.getString("category"),
                resultSet.getInt("state_id"), resultSet.getString("description"));
    }

    // Close ResultSet first, then PreparedStatement, then Connection.
    private void closeResources(Connection connection, PreparedStatement statement, ResultSet resultSet) {
        try { if (resultSet != null) resultSet.close(); }
        catch (SQLException exception) { System.out.println("Could not close ResultSet."); }
        try { if (statement != null) statement.close(); }
        catch (SQLException exception) { System.out.println("Could not close PreparedStatement."); }
        try { if (connection != null) connection.close(); }
        catch (SQLException exception) { System.out.println("Could not close Connection."); }
    }
}

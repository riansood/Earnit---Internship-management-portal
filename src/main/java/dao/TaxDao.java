/**
 * The {@code TaxDao} enum provides methods for managing tax data in the database.
 * <p>
 * This singleton class connects to the PostgreSQL database and performs CRUD operations on tax records.
 * It also provides methods for comparing dates to determine if they fall within the same quarter.
 * </p>
 */

package dao;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import model.Tax;
import model.VATform;
import util.VATformPDFgenerator;

public enum TaxDao {
    INSTANCE;
    private Connection connection;

    /**
     * Private constructor for the singleton instance.
     * <p>
     * This constructor loads the PostgreSQL JDBC driver and establishes a connection to the database.
     * </p>
     */
    private TaxDao() {
        DatabaseConnection.loadDriver();
        this.connection = DatabaseConnection.getConnection();
    }
    /**
     * Retrieves the maximum ID from the tax table.
     *
     * @return the maximum ID; {@code 0} if no records are found
     * @throws SQLException if a database access error occurs
     */
    public int getMaxId() throws SQLException {

        String query = "SELECT id FROM tax ORDER BY id DESC LIMIT 1";

        try (PreparedStatement statement = connection.prepareStatement(query)){
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("id");
            } else {
                return 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }
    /**
     * Creates a new tax record in the database.
     *
     * @param tax the tax object to be created
     * @return the created tax object
     */
    public Tax createTax(Tax tax){

        String sql = "INSERT INTO tax (id, taxAmount, isPaid, email, date, earning) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1,getMaxId() + 1 );
            statement.setDouble(2, 0.21f * tax.getEarning());
            statement.setBoolean(3, tax.isPaid());
            statement.setString(4, tax.getEmail());
            statement.setString(5, tax.getDate());
            statement.setDouble(6, tax.getEarning());
            statement.executeUpdate();
            return getTaxByEmail(tax.getEmail());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * Updates an existing tax record in the database.
     *
     * @param tax the tax object to be updated
     * @return the updated tax object
     */
    public Tax updateTax(Tax tax){

        String sql = "UPDATE tax SET taxAmount = ?, earning = ?, date = ? WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            if(!compareDates(tax.getDate(), getTaxByEmail(tax.getEmail()).getDate())) {
                statement.setDouble(1, getTaxByEmail(tax.getEmail()).getTaxAmount() + 0.21f * tax.getEarning());
                statement.setDouble(2, tax.getEarning() + getTaxByEmail(tax.getEmail()).getEarning());

            }
            else {
                statement.setDouble(1,  0.21f * tax.getEarning());
                statement.setDouble(2, tax.getEarning());
            }
            statement.setString(3, tax.getDate());
            statement.setInt(4, getTaxByEmail(tax.getEmail()).getId());
            statement.executeUpdate();
            return getTaxByEmail(tax.getEmail());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * Retrieves a tax record by email from the database.
     *
     * @param email the email of the tax record to be retrieved
     * @return the retrieved tax object; {@code null} if not found
     */
    public Tax getTaxByEmail (String email) {

        String sql = "SELECT * FROM tax WHERE email = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return new Tax(
                        resultSet.getInt("id"),
                        resultSet.getDouble("taxAmount"),
                        resultSet.getBoolean("isPaid"),
                        resultSet.getString("email"),
                        resultSet.getString("date"),
                        resultSet.getDouble("earning")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * Creates or updates a tax record in the database.
     * <p>
     * If a tax record with the same email exists, it is updated; otherwise, a new record is created.
     * </p>
     *
     * @param tax the tax object to be created or updated
     * @return the created or updated tax object
     */
    public Tax tax(Tax tax) {
        String sql = "SELECT * FROM tax WHERE email = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, tax.getEmail());
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return updateTax(tax);
            } else {
                return createTax(tax);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Compares two dates to determine if they fall within the same quarter of the year.
     *
     * @param dateStr1 the first date string in "dd/MM/yyyy" format
     * @param dateStr2 the second date string in "dd/MM/yyyy" format
     * @return {@code true} if the dates are not in the same quarter; {@code false} otherwise
     */
    // returns true if date1(new date) is not in the same quarter as date2
    public static boolean compareDates(String dateStr1, String dateStr2) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        try {
            java.util.Date date1 = dateFormat.parse(dateStr1);
            java.util.Date date2 = dateFormat.parse(dateStr2);

            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(date1);
            int quarter1 = (calendar1.get(Calendar.MONTH) / 3) + 1;
            int year1 = calendar1.get(Calendar.YEAR);

            Calendar calendar2 = Calendar.getInstance();
            calendar2.setTime(date2);
            int quarter2 = (calendar2.get(Calendar.MONTH) / 3) + 1;
            int year2 = calendar2.get(Calendar.YEAR);

            return quarter1 != quarter2 || year1 != year2;

        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }

    }
}

/**
 * The {@code VATformDao} enum provides methods for managing VAT form data in the database.
 * <p>
 * This singleton class connects to the PostgreSQL database and performs CRUD operations on VAT form records.
 * It also provides methods for generating PDFs of the VAT forms.
 * </p>
 */

package dao;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;

import java.util.List;
import model.Student;
import model.VATform;
import dao.DatabaseConnection;
import org.apache.pdfbox.pdmodel.PDDocument;
import util.VATformPDFgenerator;

public enum VATformDao {
    INSTANCE;

    private Connection connection;

    /**
     * Private constructor for the singleton instance.
     * <p>
     * This constructor loads the PostgreSQL JDBC driver and establishes a connection to the database.
     * </p>
     */
    private VATformDao() {
        DatabaseConnection.loadDriver();
        this.connection = DatabaseConnection.getConnection();
    }

    /**
     * Creates a new VAT form record in the database.
     *
     * @param form the VAT form object to be created
     * @return the created VAT form object
     * @throws SQLException if a database access error occurs
     * @throws IOException if an I/O error occurs
     */
    public VATform createVat(VATform form) throws SQLException, IOException {


        String sql = "INSERT INTO employment_form (form_id, student_id, companyName, companyAddress, businessStartDate, businessPhoneNumber, businessWebsite, " + "studentName, studentAddress, dateOfBirth, BSN, natureOfBusiness, earningsFirstYear, companySpendings, " + "noOfEmployers, initialsAndSurname, dateOfSignature, studentPhoneNumber, signature) " + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            int newFormId = getMaxId() + 1;
            statement.setInt(1, newFormId);
            statement.setString(2, form.getStudent_id());
            statement.setString(3, form.getCompanyName());
            statement.setString(4, form.getCompanyAddress());
            statement.setString(5, form.getBusinessStartDate());
            statement.setString(6, form.getBusinessPhoneNumber());
            statement.setString(7, form.getBusinessWebsite());
            statement.setString(8, form.getStudentName());
            statement.setString(9, form.getStudentAddress());
            statement.setString(10, form.getDateOfBirth());
            statement.setString(11, form.getBSN());
            statement.setString(12, form.getNatureOfBusiness());
            statement.setDouble(13, form.getEarningsFirstYear());
            statement.setDouble(14, form.getCompanySpendings());
            statement.setString(15, form.getNoOfEmployers());
            statement.setString(16, form.getInitialsAndSurname());
            statement.setString(17, form.getDateOfSignature());
            statement.setString(18, form.getStudentPhoneNumber());
            statement.setString(19, form.getSignatureBase64());

            statement.executeUpdate();
            return form;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public VATform updateVat(VATform form) throws SQLException {
        String sql = "UPDATE employment_form SET companyName = ?, companyAddress = ?, businessStartDate = ?, businessPhoneNumber = ?, businessWebsite = ?, studentName = ?, studentAddress = ?, dateOfBirth = ?, BSN = ?, natureOfBusiness = ?, earningsFirstYear = ?, companySpendings = ?, noOfEmployers = ?, initialsAndSurname = ?, dateOfSignature = ?, studentPhoneNumber = ?, signature = ? WHERE student_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, form.getCompanyName());
            statement.setString(2, form.getCompanyAddress());
            statement.setString(3, form.getBusinessStartDate());
            statement.setString(4, form.getBusinessPhoneNumber());
            statement.setString(5, form.getBusinessWebsite());
            statement.setString(6, form.getStudentName());
            statement.setString(7, form.getStudentAddress());
            statement.setString(8, form.getDateOfBirth());
            statement.setString(9, form.getBSN());
            statement.setString(10, form.getNatureOfBusiness());
            statement.setDouble(11, form.getEarningsFirstYear());
            statement.setDouble(12, form.getCompanySpendings());
            statement.setString(13, form.getNoOfEmployers());
            statement.setString(14, form.getInitialsAndSurname());
            statement.setString(15, form.getDateOfSignature());
            statement.setString(16, form.getStudentPhoneNumber());
            statement.setString(17, form.getSignatureBase64());
            statement.setString(18, form.getStudent_id());

            statement.executeUpdate();
            return form;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public VATform create(VATform form) {
        String sql = "SELECT * FROM employment_form WHERE student_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, form.getStudent_id());
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return updateVat(form);
            } else {
                return createVat(form);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }


    /**
     * Retrieves the maximum form ID from the employment_form table.
     *
     * @return the maximum form ID; {@code 0} if no records are found
     * @throws SQLException if a database access error occurs
     */
    public int getMaxId() throws SQLException {
        String query = "SELECT form_id FROM employment_form ORDER BY form_id DESC LIMIT 1";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("form_id");
            } else {
                return 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Retrieves a VAT form by email from the database.
     *
     * @param email the email of the VAT form to be retrieved
     * @return the retrieved VAT form object; {@code null} if not found
     */
    public VATform getVATform(String email) {

        String sql = "SELECT * FROM employment_form WHERE student_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new VATform(resultSet.getInt("form_id"), resultSet.getString("student_id"),
                                   resultSet.getString("companyName"),
                                   resultSet.getString("companyAddress"),
                                   resultSet.getString("businessStartDate"),
                                   resultSet.getString("businessPhoneNumber"),
                                   resultSet.getString("businessWebsite"),
                                   resultSet.getString("studentName"),
                                   resultSet.getString("studentAddress"),
                                   resultSet.getString("dateOfBirth"), resultSet.getString("BSN"),
                                   resultSet.getString("natureOfBusiness"),
                                   resultSet.getDouble("earningsFirstYear"),
                                   resultSet.getDouble("companySpendings"),
                                   resultSet.getString("noOfEmployers"),
                                   resultSet.getString("initialsAndSurname"),
                                   resultSet.getString("dateOfSignature"),
                                   resultSet.getString("studentPhoneNumber"),
                                   resultSet.getString("signature"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * Generates a PDF of the VAT form for the given email.
     *
     * @param email the email of the student whose VAT form PDF is to be generated
     * @throws IOException if an I/O error occurs
     */
    public void getPDFByEmail(String email) throws IOException {
        VATform form = getVATform(email);

        InputStream templateInputStream = getClass().getClassLoader()
                .getResourceAsStream("Belastingdienst_form.pdf");
        String outputDirectoryPath = System.getProperty("user.home") + "/Downloads/";
        String filledPdfFileName = "Belastingdienst_form_filled_" + form.getStudent_id() + ".pdf";
        String pdfFilePath = outputDirectoryPath + filledPdfFileName;

        VATformPDFgenerator.generateVATFormPDF(form, templateInputStream, pdfFilePath);
    }
    /**
     * Retrieves a list of student IDs from the employment_form table.
     *
     * @return a list of student IDs
     */
    public List<String> getStudents() {
        List<String> students = new ArrayList<>();
        String sql = "SELECT student_id FROM employment_form ";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                students.add(resultSet.getString("student_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }

}
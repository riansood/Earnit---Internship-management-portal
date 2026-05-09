package Routes;

import dao.CommentDao;
import dao.EmployeeDao;

import dao.StudentDao;
import dao.VATformDao;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import model.Comment;
import model.Employee;
import model.Student;
/**
 * This class defines the RESTful web services for managing employees.
 * It includes endpoints for employee login, retrieving PDF forms, and fetching students and comments.
 */
@Path("/employee")
public class EmployeeRoutes {

    /**
     * Endpoint for employee login.
     * Validates the employee's credentials (email and password) and returns the employee object if authentication is successful.
     *
     * @param employee The Employee object containing email and password for authentication.
     * @return A {@link Response} object indicating the success or failure of the login attempt.
     *         If successful, returns the authenticated Employee object.
     *         If unsuccessful, returns a 401 Unauthorized status with an error message.
     */
    @Path("/login")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response loginEmployee(Employee employee) {
        try {
            Employee authenticatedEmployee = EmployeeDao.INSTANCE.matchCredendials(employee.getEmail(), employee.getPassword());
            if (authenticatedEmployee != null) {
                return Response.status(Response.Status.OK)
                        .entity(authenticatedEmployee)
                        .build();
            } else {
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity("{\"error\": \"Invalid credentials\"}")
                        .build();

            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"Error during login\", \"details\": \"" + e.getMessage() + "\"}")
                    .build();
        }

    }
    /**
     * Retrieves the VAT form PDF for a specific student identified by their email.
     *
     * @param email The email of the student whose VAT form PDF is to be retrieved.
     * @throws SQLException If there is an error accessing the database.
     * @throws IOException If there is an error handling the PDF file.
     */
    @GET
    @Path("/pdf/{email}")
    public void getPDFbyEmail(@PathParam("email") String email) throws SQLException, IOException {
        VATformDao.INSTANCE.getPDFByEmail(email);
    }
    /**
     * Retrieves a list of student emails.
     *
     * @return A {@link List} of {@link String} objects, where each string is a student's email.
     */
    @GET
    @Path("/students")
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> getStudents(){
       return VATformDao.INSTANCE.getStudents();
    }
    /**
     * Retrieves a list of all comments.
     *
     * @return A {@link List} of {@link String} objects, where each string is a comment.
     */
    @GET
    @Path("/comments")
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> getComments(){
        return CommentDao.INSTANCE.getComments();
    }




}

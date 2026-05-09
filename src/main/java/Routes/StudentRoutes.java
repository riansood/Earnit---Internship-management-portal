package Routes;

import dao.CommentDao;
import dao.StudentDao;
import dao.VATformDao;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.*;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;


import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import model.Student;
import org.json.JSONObject;
import util.SendEmailSSL;

/**
 * This class handles the REST API endpoints for student-related operations.
 * It includes functionalities for student registration, login, information retrieval,
 * and other student-specific actions.
 */
@Path( "/studentLogin")
public class StudentRoutes extends HttpServlet {

    /**
     * Signs up a new student by creating a new student record in the database.
     *
     * @param student The student object containing the registration details.
     * @return The created student object with the assigned ID.
     */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Student signUpStudent(Student student){
//  SendEmailSSL.sendWelcomeEmail(student.getEmail(), student.getName());
        return  StudentDao.INSTANCE.create(student);
        
    }


    /**
     * Retrieves the student information based on the provided email.
     *
     * @param email The email of the student whose information is to be retrieved.
     * @return The student object with the specified email.
     */
    @GET
    @Path("/{email}")
    @Produces(MediaType.APPLICATION_JSON)
    public Student getStudent(@PathParam("email") String email){
        return StudentDao.INSTANCE.getStudentByEmail(email);
    }

    @GET
    @Path("/email")
    @Produces(MediaType.APPLICATION_JSON)
    public Student getStudentBySession( @Context HttpServletRequest request){
        HttpSession session = request.getSession(false);
        String email = (String) session.getAttribute("email");
        return StudentDao.INSTANCE.getStudentByEmail(email);
    }

    /**
     * Updates the BTW number for the currently logged-in student.
     *
     * @param request The HttpServletRequest object to access the current session.
     * @param BTWnumber The new BTW number to be updated.
     */
    @PUT
    @Path("/BTW_number")
    @Consumes(MediaType.TEXT_PLAIN)
    public void insertBTWnumber( @Context HttpServletRequest request, String BTWnumber){
        HttpSession session = request.getSession(false);
        StudentDao.INSTANCE.updateBTWnumber((String) session.getAttribute("email"),BTWnumber);
    }

    /**
     * Logs in a student by validating the provided credentials and creating a new session.
     *
     * @param student The student object containing the login credentials.
     * @param request The HttpServletRequest object to manage the session.
     * @return A Response object indicating the success or failure of the login attempt.
     */
    @Path("/login")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response loginStudent(Student student, @Context HttpServletRequest request) {
        String email = student.getEmail();
        String password = student.getPassword();
        Student storedStudent = StudentDao.INSTANCE.getStudentByEmail(email);

        if (storedStudent == null) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("{\"error\":\"Invalid email or password\"}").build();
        }

        String storedPasswordHash = storedStudent.getPassword();
        String storedSalt = StudentDao.INSTANCE.getSaltbyEmail(email);

        if (StudentDao.INSTANCE.isPasswordValid(password, storedPasswordHash, storedSalt)) {
            HttpSession session = request.getSession(true);
            session.setAttribute("email", email);
//            System.out.println(session.getAttribute("email"));
            return Response.ok(storedStudent).build();

        } else {
            return Response.status(Response.Status.UNAUTHORIZED).entity("{\"error\":\"Invalid email or password\"}").build();
        }

    }
    /**
     * Retrieves the PDF associated with the currently logged-in student's email.
     *
     * @param request The HttpServletRequest object to access the current session.
     * @throws IOException If an I/O error occurs during PDF retrieval.
     */
    @GET
    @Path("/pdf")
    public void getPDFbyEmail(@Context HttpServletRequest request) throws IOException {
        HttpSession session = request.getSession(false);
        VATformDao.INSTANCE.getPDFByEmail((String) session.getAttribute("email"));
    }
    /**
     * Retrieves the PDF associated with the specified student's email.
     *
     * @param request The HttpServletRequest object to access the current session.
     * @param email The email of the student whose PDF is to be retrieved.
     * @throws IOException If an I/O error occurs during PDF retrieval.
     */
    @GET
    @Path("/pdf/{email}")
    public void getPDF(@Context HttpServletRequest request, @PathParam("email") String email) throws IOException {
        VATformDao.INSTANCE.getPDFByEmail(email);
    }
    /**
     * Retrieves the comments associated with the currently logged-in student's email.
     *
     * @param request The HttpServletRequest object to access the current session.
     * @return A list of comments associated with the student's email.
     */
    @GET
    @Path("/comment")
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> getCommentsByEmail(@Context HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        String email = (String) session.getAttribute("email");
        return CommentDao.INSTANCE.getComment(email);
    }
    /**
     * Logs out the currently logged-in student by invalidating the current session.
     *
     * @param request The HttpServletRequest object to access the current session.
     * @return A Response object indicating the success of the logout operation.
     */
    @POST
    @Path("/logout")
    public Response logoutStudent(@Context HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return Response.ok("{\"message\":\"Logout successful\"}").build();
    }

    /**
     * Checks the status of the tax form access for the currently logged-in student.
     *
     * @param request The HttpServletRequest object to access the current session.
     * @return A Response object indicating the status of the tax form access.
     */
    @GET
    @Path("/tax-form")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTaxFormStatus(@Context HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("email") == null) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("{\"error\":\"Unauthorized access\"}").build();
        }

        String email = (String) session.getAttribute("email");
        Student student = StudentDao.INSTANCE.getStudentByEmail(email);
        if (student == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("{\"error\":\"Student not found\"}").build();
        }

        if (student.getBTW_number() == null) {
            return Response.status(Response.Status.FORBIDDEN).entity("{\"error\":\"BTW number is required to access Tax form\"}").build();
        }

        // If BTW number is not null, return success status with relevant information
        return Response.ok("{\"message\":\"Student has valid BTW number. Access to Tax form allowed.\"}").build();
    }
    /**
     * Retrieves the BTW number for the currently logged-in student.
     *
     * @param request The HttpServletRequest object to access the current session.
     * @return The BTW number of the student.
     */
    @GET
    @Path("/receiveBTW")
    @Produces(MediaType.TEXT_PLAIN)
    public String getBTWnumber(@Context HttpServletRequest request){
        HttpSession session = request.getSession(false);
        String email = (String) session.getAttribute("email");
        return StudentDao.INSTANCE.getStudentByEmail(email).getBTW_number();
    }

    @PUT
    @Path("/editProfile")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Student editProfile(Student student, @Context HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        student.setEmail((String) session.getAttribute("email"));
        return StudentDao.INSTANCE.editProfile(student);
    }

    @DELETE
    @Path("/delete")
    public void deleteAccount(@Context HttpServletRequest request){
        HttpSession session = request.getSession(false);
        String email = (String) session.getAttribute("email");
        StudentDao.INSTANCE.delete(email);
        session.invalidate();
    }
}

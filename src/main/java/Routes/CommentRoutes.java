package Routes;

import dao.CommentDao;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.core.Context;
import java.util.Collections;
import model.Comment;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.sql.SQLException;
/**
 * {@code CommentRoutes} is a RESTful web service class that handles HTTP requests related to comment management.
 * <p>
 * This class provides endpoints for creating comments, accepting comments, and has a placeholder for retrieving comment statuses.
 * The endpoints are designed to work with JSON data and interact with the {@link CommentDao} data access object.
 * </p>
 */
@Path("/comments")
public class CommentRoutes {
    /**
     * Creates a new comment.
     * <p>
     * This endpoint accepts a {@link Comment} object in JSON format, processes it, and creates a new comment in the database.
     * </p>
     *
     * @param comment the {@link Comment} object to be created
     * @return the created {@link Comment} object in JSON format
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Comment createComment(Comment comment) {
        return CommentDao.INSTANCE.createComment(comment);
    }
    /**
     * Accepts a comment for a specific student.
     * <p>
     * This endpoint updates the acceptance status of a comment for the student identified by the provided student ID.
     * </p>
     *
     * @param studentId the ID of the student whose comment is to be accepted
     * @return a {@link Response} indicating the success or failure of the operation
     */
    @PUT
    @Path("/accept/{student_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response acceptComment(@PathParam("student_id") String studentId) {
        try {
            CommentDao.INSTANCE.update_isaccepted(studentId);
            return Response.status(Response.Status.OK).entity("{\"message\":\"Comment accepted\"}").build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Could not accept comment\"}").build();
        }
    }

    @GET
    @Path("/status/{student_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCommentStatus(@PathParam("student_id") String email) {
        try {
            boolean isAccepted = CommentDao.INSTANCE.isApproved(email);
            return Response.ok().entity(Collections.singletonMap("isAccepted", isAccepted)).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to fetch comment status").build();
        }
    }
}

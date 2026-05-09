package Routes;

import dao.VATformDao;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import java.io.IOException;
import java.sql.SQLException;
import model.VATform;
import org.apache.pdfbox.pdmodel.PDDocument;
import util.SendEmailSSL;
import util.VATformPDFgenerator;

/**
 * This class handles the REST API endpoints for VAT form-related operations.
 * It includes functionalities for creating a VAT form and (commented-out) methods for updating different fields of the VAT form.
 */
@Path("/vatForm")
public class VATformRoutes {
    /**
     * Creates a new VAT form for the currently logged-in user.
     * The student's email is retrieved from the session and set as the student ID in the VAT form.
     *
     * @param form The VATform object containing the details of the VAT form to be created.
     * @param request The HttpServletRequest object to access the current session and obtain the logged-in user's email.
     * @return The created VATform object.
     * @throws SQLException If there is a database access error.
     * @throws IOException If there is an error in handling the VAT form file.
     */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public VATform createVATform(VATform form, @Context HttpServletRequest request) throws SQLException, IOException {
        HttpSession session = request.getSession(false);
        form.setStudent_id((String) session.getAttribute("email"));
        SendEmailSSL.sendApplicationEmail((String) session.getAttribute("email"));
        return VATformDao.INSTANCE.create(form);
    }
}

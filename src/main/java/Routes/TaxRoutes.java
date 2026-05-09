package Routes;

import dao.TaxDao;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import model.Tax;

/**
 * This class handles the REST API endpoints for tax-related operations.
 * It includes functionalities for updating tax information, retrieving tax details
 * for the currently logged-in user, and getting the tax amount for a specific email.
 */
@Path("/tax")
public class TaxRoutes {

    /**
     * Updates the tax information for the currently logged-in user.
     *
     * @param tax The Tax object containing updated tax details.
     * @param request The HttpServletRequest object to access the current session.
     * @return The updated Tax object.
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Tax updateTax (Tax tax, @Context HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        tax.setEmail((String) session.getAttribute("email"));
        return TaxDao.INSTANCE.tax(tax);
    }


    /**
     * Retrieves the tax information for the currently logged-in user.
     *
     * @param request The HttpServletRequest object to access the current session.
     * @return The Tax object containing the tax details for the logged-in user.
     */
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Tax getTaxByEmail(@Context HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return TaxDao.INSTANCE.getTaxByEmail((String) session.getAttribute("email"));
    }
    /**
     * Retrieves the tax amount for the specified email.
     *
     * @param email The email of the user whose tax amount is to be retrieved.
     * @return The tax amount for the specified email.
     */
    @GET
    @Path("/instance/{email}")
    @Produces(MediaType.TEXT_PLAIN)
    public Double tax(@PathParam("email") String email){
        return TaxDao.INSTANCE.getTaxByEmail(email).getTaxAmount();
    }
}

package filter;

import dao.DatabaseConnection;
import jakarta.servlet.*;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Logger;

/**
 * {@code SessionFilter} is a servlet filter that handles session management for incoming HTTP requests.
 * <p>
 * This filter checks for the presence of a valid session and ensures that the "studentId" attribute is present.
 * If the session or attribute is missing, it redirects the user to the login page.
 * It also manages the lifecycle of a database connection used for logging.
 * </p>
 */
public class SessionFilter<HttpSession> implements Filter {

    private static final Logger logger = Logger.getLogger(SessionFilter.class.getName());

    private Connection connection;
    /**
     * Initializes the filter by loading the database driver and establishing a database connection.
     * <p>
     * This method is called by the web container to indicate to the filter that it is being placed into service.
     * </p>
     *
     * @param filterConfig the filter configuration object used by the web container to pass initialization parameters
     * @throws ServletException if an error occurs during initialization
     */

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        DatabaseConnection.loadDriver();
        connection = DatabaseConnection.getConnection();
        if (connection != null) {
            System.out.println("Database connection pool initialized");
            logger.info("SessionFilter initialized");
        } else {
            throw new ServletException("Failed to initialize database connection pool");
        }
    }
    /**
     * Performs the filtering operation by checking for the presence of a valid session and the "studentId" attribute.
     * <p>
     * If the session is invalid or the "studentId" attribute is missing, the user is redirected to the login page.
     * Otherwise, the request is passed along the filter chain.
     * </p>
     *
     * @param request the servlet request object
     * @param response the servlet response object
     * @param chain the filter chain to pass the request and response objects
     * @throws IOException if an I/O error occurs during the filtering process
     * @throws ServletException if a servlet exception occurs during the filtering process
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = (HttpSession) httpRequest.getSession(false);

        // Check if the session exists and contains the studentId attribute
        if (session == null || ((jakarta.servlet.http.HttpSession) session).getAttribute("studentId") == null) {
            // If no session or studentId, redirect to login page
            httpResponse.sendRedirect("login.jsp");
        } else {
            // Proceed with the request
            chain.doFilter(request, response);
        }
    }

    /**
     * Destroys the filter by closing the database connection and logging the event.
     * <p>
     * This method is called by the web container to indicate to the filter that it is being taken out of service.
     * </p>
     */
    @Override
    public void destroy() {
        if (connection != null) {
            try {
                connection.close();
                logger.info("Database connection closed");
            } catch (SQLException e) {
                logger.warning("Failed to close database connection: " + e.getMessage());
            }
        }
        logger.info("SessionFilter destroyed");
    }
}
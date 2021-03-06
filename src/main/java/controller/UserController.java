package controller;

import enums.CrudMethod;
import exception.DuplicateEmailException;
import exception.DuplicateException;
import exception.DuplicateLoginException;
import org.apache.log4j.Logger;
import service.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

/**
 * Abstract class used for working with users.
 */
public abstract class UserController {

    private static final Logger LOGGER = Logger.getLogger(UserController.class);
    protected UserService service = new UserService();

    /**
     * Wrap request from controller and do one of CRUD operations.
     * Forwarding req on success or error page.
     * @param type one of CRUD methods
     * @param req request ro controller
     * @param resp response request to controller
     * @param userFields name of params which you need to get from request
     * @throws ServletException
     * @throws IOException
     */
    public void doCrudMethod(CrudMethod type, HttpServletRequest req,
                             HttpServletResponse resp, String... userFields)
            throws ServletException, IOException {

        Map<String, String> params = getNecessaryParamsMap(req, userFields);

        try {
            switch (type) {
                case UPDATE:
                    service.update(params);
                    LOGGER.info(req.getUserPrincipal().getName() + " update " + params.get("login"));
                    break;
                case CREATE:
                    service.create(params);

                    Principal user = req.getUserPrincipal();
                    if (user != null)
                        LOGGER.info(req.getUserPrincipal().getName() + " create " + params.get("login"));
                    else
                        LOGGER.info("Guest" + " create " + params.get("login"));
                    break;
            }

            req.getRequestDispatcher("/pages/success/success.jsp").forward(req, resp);
        } catch (DuplicateLoginException e) {
            LOGGER.debug(e.getMessage());
            req.setAttribute("err", "Login not unique");
            req.getRequestDispatcher("/pages/errors/error.jsp").forward(req, resp);
        } catch (DuplicateEmailException e) {
            LOGGER.debug(e.getMessage());
            req.setAttribute("err", "Email not unique");
            req.getRequestDispatcher("/pages/errors/error.jsp").forward(req, resp);
        } catch (DuplicateException e) {
            LOGGER.debug(e.getMessage());
            req.setAttribute("err", "Login and Email are not unique");
            req.getRequestDispatcher("/pages/errors/error.jsp").forward(req, resp);
        } catch (IllegalArgumentException e) {
            LOGGER.warn("Dao exception", e);
            req.setAttribute("err", "Some fields are incorrect");
            req.getRequestDispatcher("/pages/errors/error.jsp").forward(req, resp);
        } catch (Exception e) {
            LOGGER.error("Unexpected exception", e);
            req.setAttribute("err", "Unexpected exception");
            req.getRequestDispatcher("/pages/errors/error.jsp").forward(req, resp);
        }
    }

    /**
     * Get params from servlet request
     * @param req request to controller
     * @param params name of params which you need to get from request
     * @return
     */
    private Map<String, String> getNecessaryParamsMap(HttpServletRequest req, String... params) {
        Map<String, String> paramMap = new HashMap<>();

        for (String param : params) {
            paramMap.put(param, req.getParameter(param));
        }

        return paramMap;
    }
}

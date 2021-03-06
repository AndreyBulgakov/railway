package controller.guest;

import controller.UserController;
import enums.CrudMethod;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by andrey on 04.02.16.
 */
public class RegisterController extends UserController {

    protected void doGetRegister(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/pages/login/register.jsp").forward(req, resp);
    }

    protected void doPostCreate(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doCrudMethod(CrudMethod.CREATE, req, resp, "login", "password", "name", "surname", "email");
    }
}

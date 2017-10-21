package controller;

import model.UserDao;
import model.exceptions.UserException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by Marina on 21.10.2017 г..
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("user");
        String password = request.getParameter("pass");

        try {
            if (UserDao.getInstance().existsUser(username, password)) {
                request.getSession().setAttribute("logged", true);
                request.getSession().setAttribute("user", UserDao.getInstance().getUserByUsername(username));
                request.getRequestDispatcher("wanderlust.jsp").forward(request, response);
                return;
            } else {
                request.getRequestDispatcher("login.jsp").forward(request, response);
                return;
            }
        } catch (SQLException | UserException e) {
            e.printStackTrace();
            request.getRequestDispatcher("error.jsp").forward(request, response);


        }


    }}

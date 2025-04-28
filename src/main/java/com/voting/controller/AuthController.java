package com.voting.controller;

import com.voting.dao.AdminDao;
import com.voting.dao.UserDao;
import com.voting.model.Admin;
import com.voting.model.User;
import com.voting.util.Constants;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@WebServlet(name = "AuthController", value = {
        "/register",
        "/login",
        "/logout",
        "/admin/login",
        "/admin/logout"
})
public class AuthController extends HttpServlet {
    private final UserDao userDao = new UserDao();
    private final AdminDao adminDao = new AdminDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();

        switch (path) {
            case "/register":
                request.getRequestDispatcher("/WEB-INF/views/auth/register.jsp").forward(request, response);
                break;
            case "/login":
                request.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(request, response);
                break;
            case "/logout":
                logoutUser(request, response);
                break;
            case "/admin/login":
                request.getRequestDispatcher("/WEB-INF/views/auth/admin_login.jsp").forward(request, response);
                break;
            case "/admin/logout":
                logoutAdmin(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();

        switch (path) {
            case "/register":
                registerUser(request, response);
                break;
            case "/login":
                loginUser(request, response);
                break;
            case "/admin/login":
                loginAdmin(request, response);
                break;
        }
    }

    private void registerUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        String fullName = request.getParameter("fullName");
        String dob = request.getParameter("dateOfBirth");
        String address = request.getParameter("address");

        try {
            User user = new User(
                    username,
                    password,
                    email,
                    fullName,
                    new SimpleDateFormat("yyyy-MM-dd").parse(dob),
                    address
            );

            if (userDao.registerUser(user)) {
                request.setAttribute(Constants.SUCCESS_MESSAGE, "Registration successful! Please login.");
                request.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(request, response);
            } else {
                request.setAttribute(Constants.ERROR_MESSAGE, "Registration failed. Username or email may already exist.");
                request.getRequestDispatcher("/WEB-INF/views/auth/register.jsp").forward(request, response);
            }
        } catch (ParseException e) {
            request.setAttribute(Constants.ERROR_MESSAGE, "Invalid date format.");
            request.getRequestDispatcher("/WEB-INF/views/auth/register.jsp").forward(request, response);
        }
    }

    private void loginUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        User user = userDao.loginUser(username, password);
        if (user != null) {
            if (user.isVerified()) {
                HttpSession session = request.getSession();
                session.setAttribute(Constants.USER_SESSION, user);
                response.sendRedirect(request.getContextPath() + "/user/dashboard");
            } else {
                request.setAttribute(Constants.ERROR_MESSAGE, "Your account is not yet verified. Please contact administrator.");
                request.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(request, response);
            }
        } else {
            request.setAttribute(Constants.ERROR_MESSAGE, "Invalid username or password.");
            request.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(request, response);
        }
    }

    private void logoutUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        response.sendRedirect(request.getContextPath() + "/login");
    }

    private void loginAdmin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        Admin admin = adminDao.loginAdmin(username, password);
        if (admin != null) {
            HttpSession session = request.getSession();
            session.setAttribute(Constants.ADMIN_SESSION, admin);
            response.sendRedirect(request.getContextPath() + "/admin/dashboard");
        } else {
            request.setAttribute(Constants.ERROR_MESSAGE, "Invalid admin credentials.");
            request.getRequestDispatcher("/WEB-INF/views/auth/admin_login.jsp").forward(request, response);
        }
    }

    private void logoutAdmin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        response.sendRedirect(request.getContextPath() + "/admin/login");
    }
}
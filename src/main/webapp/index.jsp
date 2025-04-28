package com.voting.controller;

import com.voting.dao.CandidateDao;
import com.voting.dao.ElectionDao;
import com.voting.dao.UserDao;
import com.voting.dao.VoteDao;
import com.voting.model.Admin;
import com.voting.model.Candidate;
import com.voting.model.Election;
import com.voting.util.Constants;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@WebServlet(name = "AdminController", value = {
        "/admin/dashboard",
        "/admin/manage-elections",
        "/admin/add-election",
        "/admin/save-election",
        "/admin/delete-election",
        "/admin/manage-candidates",
        "/admin/add-candidate",
        "/admin/save-candidate",
        "/admin/delete-candidate",
        "/admin/view-results",
        "/admin/manage-users",
        "/admin/verify-user"
})
public class AdminController extends HttpServlet {
    private final ElectionDao electionDao = new ElectionDao();
    private final CandidateDao candidateDao = new CandidateDao();
    private final VoteDao voteDao = new VoteDao();
    private final UserDao userDao = new UserDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute(Constants.ADMIN_SESSION) == null) {
            response.sendRedirect(request.getContextPath() + "/admin/login");
            return;
        }

        Admin admin = (Admin) session.getAttribute(Constants.ADMIN_SESSION);
        String path = request.getServletPath();
        
        switch (path) {
            case "/admin/dashboard":
                showAdminDashboard(request, response, admin);
                break;
            case "/admin/manage-elections":
                showManageElections(request, response);
                break;
            case "/admin/add-election":
                showAddElectionForm(request, response);
                break;
            case "/admin/delete-election":
                deleteElection(request, response);
                break;
            case "/admin/manage-candidates":
                showManageCandidates(request, response);
                break;
            case "/admin/add-candidate":
                showAddCandidateForm(request, response);
                break;
            case "/admin/delete-candidate":
                deleteCandidate(request, response);
                break;
            case "/admin/view-results":
                showElectionResults(request, response);
                break;
            case "/admin/manage-users":
                showManageUsers(request, response);
                break;
            case "/admin/verify-user":
                verifyUser(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute(Constants.ADMIN_SESSION) == null) {
            response.sendRedirect(request.getContextPath() + "/admin/login");
            return;
        }

        String path = request.getServletPath();
        
        if ("/admin/save-election".equals(path)) {
            saveElection(request, response);
        } else if ("/admin/save-candidate".equals(path)) {
            saveCandidate(request, response);
        }
    }

    private void showAdminDashboard(HttpServletRequest request, HttpServletResponse response, Admin admin) 
            throws ServletException, IOException {
        request.setAttribute("admin", admin);
        request.setAttribute("electionCount", electionDao.getAllElections().size());
        request.setAttribute("userCount", userDao.getAllUsers().size());
        request.getRequestDispatcher("/WEB-INF/views/admin/dashboard.jsp").forward(request, response);
    }

    private void showManageElections(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.setAttribute("elections", electionDao.getAllElections());
        request.getRequestDispatcher("/WEB-INF/views/admin/manage_elections.jsp").forward(request, response);
    }

    private void showAddElectionForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/admin/add_election.jsp").forward(request, response);
    }

    private void saveElection(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String startDateStr = request.getParameter("startDate");
        String endDateStr = request.getParameter("endDate");
        
        try {
            Election election = new Election(
                title,
                description,
                new SimpleDateFormat("yyyy-MM-dd'T'HH:mm").parse(startDateStr),
                new SimpleDateFormat("yyyy-MM-dd'T'HH:mm").parse(endDateStr)
            );
            
            if (electionDao.createElection(election)) {
                request.setAttribute(Constants.SUCCESS_MESSAGE, "Election created successfully!");
            } else {
                request.setAttribute(Constants.ERROR_MESSAGE, "Failed to create election.");
            }
        } catch (ParseException e) {
            request.setAttribute(Constants.ERROR_MESSAGE, "Invalid date format.");
        }
        
        request.getRequestDispatcher("/WEB-INF/views/admin/add_election.jsp").forward(request, response);
    }

    private void deleteElection(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        int electionId = Integer.parseInt(request.getParameter("id"));
        
        if (electionDao.deleteElection(electionId)) {
            request.setAttribute(Constants.SUCCESS_MESSAGE, "Election deleted successfully!");
        } else {
            request.setAttribute(Constants.ERROR_MESSAGE, "Failed to delete election.");
        }
        
        response.sendRedirect(request.getContextPath() + "/admin/manage-elections");
    }

    private void showManageCandidates(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        int electionId = Integer.parseInt(request.getParameter("electionId"));
        
        request.setAttribute("election", electionDao.getElectionById(electionId));
        request.setAttribute("candidates", candidateDao.getCandidatesByElection(electionId));
        request.getRequestDispatcher("/WEB-INF/views/admin/manage_candidates.jsp").forward(request, response);
    }

    private void showAddCandidateForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        int electionId = Integer.parseInt(request.getParameter("electionId"));
        
        request.setAttribute("election", electionDao.getElectionById(electionId));
        request.getRequestDispatcher("/WEB-INF/views/admin/add_candidate.jsp").forward(request, response);
    }

    private void saveCandidate(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        int electionId = Integer.parseInt(request.getParameter("electionId"));
        String name = request.getParameter("name");
        String party = request.getParameter("party");
        String bio = request.getParameter("bio");
        
        Candidate candidate = new Candidate(electionId, name, party, bio);
        
        if (candidateDao.addCandidate(candidate)) {
            request.setAttribute(Constants.SUCCESS_MESSAGE, "Candidate added successfully!");
        } else {
            request.setAttribute(Constants.ERROR_MESSAGE, "Failed to add candidate.");
        }
        
        request.setAttribute("election", electionDao.getElectionById(electionId));
        request.getRequestDispatcher("/WEB-INF/views/admin/add_candidate.jsp").forward(request, response);
    }

    private void deleteCandidate(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        int candidateId = Integer.parseInt(request.getParameter("id"));
        int electionId = Integer.parseInt(request.getParameter("electionId"));
        
        if (candidateDao.deleteCandidate(candidateId)) {
            request.setAttribute(Constants.SUCCESS_MESSAGE, "Candidate deleted successfully!");
        } else {
            request.setAttribute(Constants.ERROR_MESSAGE, "Failed to delete candidate.");
        }
        
        response.sendRedirect(request.getContextPath() + "/admin/manage-candidates?electionId=" + electionId);
    }

    private void showElectionResults(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        int electionId = Integer.parseInt(request.getParameter("electionId"));
        
        request.setAttribute("election", electionDao.getElectionById(electionId));
        request.setAttribute("results", voteDao.getElectionResults(electionId));
        request.getRequestDispatcher("/WEB-INF/views/admin/view_results.jsp").forward(request, response);
    }

    private void showManageUsers(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.setAttribute("users", userDao.getAllUsers());
        request.getRequestDispatcher("/WEB-INF/views/admin/manage_users.jsp").forward(request, response);
    }

    private void verifyUser(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        int userId = Integer.parseInt(request.getParameter("id"));
        
        if (userDao.verifyUser(userId)) {
            request.setAttribute(Constants.SUCCESS_MESSAGE, "User verified successfully!");
        } else {
            request.setAttribute(Constants.ERROR_MESSAGE, "Failed to verify user.");
        }
        
        response.sendRedirect(request.getContextPath() + "/admin/manage-users");
    }
}
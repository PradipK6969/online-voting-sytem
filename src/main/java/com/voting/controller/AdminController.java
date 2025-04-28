package com.voting.controller;

import com.voting.dao.CandidateDao;
import com.voting.dao.ElectionDao;
import com.voting.dao.UserDao;
import com.voting.dao.VoteDao;
import com.voting.model.Admin;
import com.voting.model.Candidate;
import com.voting.model.Election;
import com.voting.util.Constants;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("AdminController called for path: " + request.getServletPath());
        System.out.println("Session exists: " + (request.getSession(false) != null));
        if (request.getSession(false) != null) {
            System.out.println("Admin in session: " +
                    (request.getSession(false).getAttribute(Constants.ADMIN_SESSION) != null));
        }
        try {
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute(Constants.ADMIN_SESSION) == null) {
                response.sendRedirect(request.getContextPath() + "/admin/login");
                return;
            }

            String path = request.getServletPath();

            switch (path) {
                case "/admin/dashboard":
                    showAdminDashboard(request, response);
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
                default:
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                    break;
            }
        } catch (Exception e) {
            handleError(request, response, e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
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
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            handleError(request, response, e);
        }
    }

    private void showAdminDashboard(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            request.setAttribute("electionCount", electionDao.getAllElections().size());
            request.setAttribute("userCount", userDao.getAllUsers().size());
            request.getRequestDispatcher("/WEB-INF/views/admin/dashboard.jsp").forward(request, response);
        } catch (Exception e) {
            throw new ServletException("Failed to show admin dashboard", e);
        }
    }

    private void showManageElections(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            request.setAttribute("elections", electionDao.getAllElections());
            request.getRequestDispatcher("/WEB-INF/views/admin/manage_elections.jsp").forward(request, response);
        } catch (Exception e) {
            throw new ServletException("Failed to manage elections", e);
        }
    }

    private void showAddElectionForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            request.getRequestDispatcher("/WEB-INF/views/admin/add_election.jsp").forward(request, response);
        } catch (Exception e) {
            throw new ServletException("Failed to show election form", e);
        }
    }

    private void saveElection(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String title = request.getParameter("title");
            String description = request.getParameter("description");
            String startDateStr = request.getParameter("startDate");
            String endDateStr = request.getParameter("endDate");

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
            request.setAttribute(Constants.ERROR_MESSAGE, "Invalid date format. Please use YYYY-MM-DDTHH:MM format.");
        } catch (Exception e) {
            request.setAttribute(Constants.ERROR_MESSAGE, "An unexpected error occurred: " + e.getMessage());
        }

        showAddElectionForm(request, response);
    }

    private void deleteElection(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int electionId = Integer.parseInt(request.getParameter("id"));

            if (electionDao.deleteElection(electionId)) {
                request.getSession().setAttribute(Constants.SUCCESS_MESSAGE, "Election deleted successfully!");
            } else {
                request.getSession().setAttribute(Constants.ERROR_MESSAGE, "Failed to delete election.");
            }
        } catch (NumberFormatException e) {
            request.getSession().setAttribute(Constants.ERROR_MESSAGE, "Invalid election ID format.");
        } catch (Exception e) {
            request.getSession().setAttribute(Constants.ERROR_MESSAGE, "Failed to delete election: " + e.getMessage());
        }

        response.sendRedirect(request.getContextPath() + "/admin/manage-elections");
    }

    private void showManageCandidates(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int electionId = Integer.parseInt(request.getParameter("electionId"));

            Election election = electionDao.getElectionById(electionId);
            if (election == null) {
                throw new ServletException("Election not found");
            }

            request.setAttribute("election", election);
            request.setAttribute("candidates", candidateDao.getCandidatesByElection(electionId));
            request.getRequestDispatcher("/WEB-INF/views/admin/manage_candidates.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            throw new ServletException("Invalid election ID format", e);
        } catch (Exception e) {
            throw new ServletException("Failed to manage candidates", e);
        }
    }

    private void showAddCandidateForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int electionId = Integer.parseInt(request.getParameter("electionId"));

            Election election = electionDao.getElectionById(electionId);
            if (election == null) {
                throw new ServletException("Election not found");
            }

            request.setAttribute("election", election);
            request.getRequestDispatcher("/WEB-INF/views/admin/add_candidate.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            throw new ServletException("Invalid election ID format", e);
        } catch (Exception e) {
            throw new ServletException("Failed to show candidate form", e);
        }
    }

    private void saveCandidate(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
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
        } catch (NumberFormatException e) {
            request.setAttribute(Constants.ERROR_MESSAGE, "Invalid election ID format.");
        } catch (Exception e) {
            request.setAttribute(Constants.ERROR_MESSAGE, "Failed to save candidate: " + e.getMessage());
        }

        showAddCandidateForm(request, response);
    }

    private void deleteCandidate(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int candidateId = Integer.parseInt(request.getParameter("id"));
            int electionId = Integer.parseInt(request.getParameter("electionId"));

            if (candidateDao.deleteCandidate(candidateId)) {
                request.getSession().setAttribute(Constants.SUCCESS_MESSAGE, "Candidate deleted successfully!");
            } else {
                request.getSession().setAttribute(Constants.ERROR_MESSAGE, "Failed to delete candidate.");
            }
        } catch (NumberFormatException e) {
            request.getSession().setAttribute(Constants.ERROR_MESSAGE, "Invalid ID format.");
        } catch (Exception e) {
            request.getSession().setAttribute(Constants.ERROR_MESSAGE, "Failed to delete candidate: " + e.getMessage());
        }

        response.sendRedirect(request.getContextPath() + "/admin/manage-candidates?electionId=" +
                request.getParameter("electionId"));
    }

    private void showElectionResults(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int electionId = Integer.parseInt(request.getParameter("electionId"));

            Election election = electionDao.getElectionById(electionId);
            if (election == null) {
                throw new ServletException("Election not found");
            }

            request.setAttribute("election", election);
            request.setAttribute("results", voteDao.getElectionResults(electionId));
            request.getRequestDispatcher("/WEB-INF/views/admin/view_results.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            throw new ServletException("Invalid election ID format", e);
        } catch (Exception e) {
            throw new ServletException("Failed to show election results", e);
        }
    }

    private void showManageUsers(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            request.setAttribute("users", userDao.getAllUsers());
            request.getRequestDispatcher("/WEB-INF/views/admin/manage_users.jsp").forward(request, response);
        } catch (Exception e) {
            throw new ServletException("Failed to manage users", e);
        }
    }

    private void verifyUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int userId = Integer.parseInt(request.getParameter("id"));

            if (userDao.verifyUser(userId)) {
                request.getSession().setAttribute(Constants.SUCCESS_MESSAGE, "User verified successfully!");
            } else {
                request.getSession().setAttribute(Constants.ERROR_MESSAGE, "Failed to verify user.");
            }
        } catch (NumberFormatException e) {
            request.getSession().setAttribute(Constants.ERROR_MESSAGE, "Invalid user ID format.");
        } catch (Exception e) {
            request.getSession().setAttribute(Constants.ERROR_MESSAGE, "Failed to verify user: " + e.getMessage());
        }

        response.sendRedirect(request.getContextPath() + "/admin/manage-users");
    }

    private void handleError(HttpServletRequest request, HttpServletResponse response, Exception e)
            throws ServletException, IOException {
        e.printStackTrace();
        request.setAttribute("errorMessage", "An error occurred: " + e.getMessage());
        request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
    }
}
package com.voting.controller;

import com.voting.dao.CandidateDao;
import com.voting.dao.ElectionDao;
import com.voting.dao.VoteDao;
import com.voting.model.User;
import com.voting.model.Vote;
import com.voting.util.Constants;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "UserController", value = {
        "/user/dashboard",
        "/user/elections",
        "/user/vote",
        "/user/voting-history"
})
public class UserController extends HttpServlet {
    private final ElectionDao electionDao = new ElectionDao();
    private final CandidateDao candidateDao = new CandidateDao();
    private final VoteDao voteDao = new VoteDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute(Constants.USER_SESSION) == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        User user = (User) session.getAttribute(Constants.USER_SESSION);
        String path = request.getServletPath();

        switch (path) {
            case "/user/dashboard":
                showUserDashboard(request, response, user);
                break;
            case "/user/elections":
                showElections(request, response, user);
                break;
            case "/user/vote":
                showVotePage(request, response, user);
                break;
            case "/user/voting-history":
                showVotingHistory(request, response, user);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute(Constants.USER_SESSION) == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        User user = (User) session.getAttribute(Constants.USER_SESSION);
        String path = request.getServletPath();

        if ("/user/vote".equals(path)) {
            castVote(request, response, user);
        }
    }

    private void showUserDashboard(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException {
        request.setAttribute("user", user);
        request.getRequestDispatcher("/WEB-INF/views/user/dashboard.jsp").forward(request, response);
    }

    private void showElections(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException {
        request.setAttribute("elections", electionDao.getAllElections());
        request.getRequestDispatcher("/WEB-INF/views/user/elections.jsp").forward(request, response);
    }

    private void showVotePage(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException {
        int electionId = Integer.parseInt(request.getParameter("electionId"));

        if (voteDao.hasUserVoted(electionId, user.getId())) {
            request.setAttribute(Constants.ERROR_MESSAGE, "You have already voted in this election.");
            response.sendRedirect(request.getContextPath() + "/user/elections");
            return;
        }

        request.setAttribute("election", electionDao.getElectionById(electionId));
        request.setAttribute("candidates", candidateDao.getCandidatesByElection(electionId));
        request.getRequestDispatcher("/WEB-INF/views/user/vote.jsp").forward(request, response);
    }

    private void castVote(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException {
        int electionId = Integer.parseInt(request.getParameter("electionId"));
        int candidateId = Integer.parseInt(request.getParameter("candidateId"));

        if (voteDao.hasUserVoted(electionId, user.getId())) {
            request.setAttribute(Constants.ERROR_MESSAGE, "You have already voted in this election.");
            response.sendRedirect(request.getContextPath() + "/user/elections");
            return;
        }

        Vote vote = new Vote(electionId, candidateId, user.getId());
        if (voteDao.castVote(vote)) {
            request.setAttribute(Constants.SUCCESS_MESSAGE, "Your vote has been cast successfully!");
            response.sendRedirect(request.getContextPath() + "/user/voting-history");
        } else {
            request.setAttribute(Constants.ERROR_MESSAGE, "Failed to cast your vote. Please try again.");
            request.getRequestDispatcher("/WEB-INF/views/user/vote.jsp").forward(request, response);
        }
    }

    private void showVotingHistory(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException {
        request.setAttribute("votes", voteDao.getVotingHistory(user.getId()));
        request.getRequestDispatcher("/WEB-INF/views/user/voting_history.jsp").forward(request, response);
    }
}
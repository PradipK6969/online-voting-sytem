package com.voting.dao;

import com.voting.model.Vote;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VoteDao {
    public boolean castVote(Vote vote) {
        String sql = "INSERT INTO votes(election_id, candidate_id, user_id) VALUES(?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, vote.getElectionId());
            pstmt.setInt(2, vote.getCandidateId());
            pstmt.setInt(3, vote.getUserId());

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean hasUserVoted(int electionId, int userId) {
        String sql = "SELECT COUNT(*) FROM votes WHERE election_id = ? AND user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, electionId);
            pstmt.setInt(2, userId);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Vote> getVotingHistory(int userId) {
        List<Vote> votes = new ArrayList<>();
        String sql = "SELECT v.*, e.title as election_title, c.name as candidate_name " +
                "FROM votes v " +
                "JOIN elections e ON v.election_id = e.id " +
                "JOIN candidates c ON v.candidate_id = c.id " +
                "WHERE v.user_id = ? " +
                "ORDER BY v.voted_at DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Vote vote = new Vote();
                vote.setId(rs.getInt("id"));
                vote.setElectionId(rs.getInt("election_id"));
                vote.setCandidateId(rs.getInt("candidate_id"));
                vote.setUserId(rs.getInt("user_id"));
                vote.setVotedAt(rs.getTimestamp("voted_at"));

                // Set additional info using the new method
                vote.setAdditionalInfo("electionTitle", rs.getString("election_title"));
                vote.setAdditionalInfo("candidateName", rs.getString("candidate_name"));

                votes.add(vote);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return votes;
    }

    public List<Object[]> getElectionResults(int electionId) {
        List<Object[]> results = new ArrayList<>();
        String sql = "SELECT c.id, c.name, c.party, COUNT(v.id) as vote_count " +
                "FROM candidates c " +
                "LEFT JOIN votes v ON c.id = v.candidate_id AND v.election_id = ? " +
                "WHERE c.election_id = ? " +
                "GROUP BY c.id, c.name, c.party " +
                "ORDER BY vote_count DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, electionId);
            pstmt.setInt(2, electionId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Object[] row = new Object[4];
                row[0] = rs.getInt("id");
                row[1] = rs.getString("name");
                row[2] = rs.getString("party");
                row[3] = rs.getInt("vote_count");
                results.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }
}
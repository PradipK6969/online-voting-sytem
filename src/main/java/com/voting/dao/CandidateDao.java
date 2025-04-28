package com.voting.dao;

import com.voting.model.Candidate;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CandidateDao {
    public boolean addCandidate(Candidate candidate) {
        String sql = "INSERT INTO candidates(election_id, name, party, bio) VALUES(?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, candidate.getElectionId());
            pstmt.setString(2, candidate.getName());
            pstmt.setString(3, candidate.getParty());
            pstmt.setString(4, candidate.getBio());

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Candidate> getCandidatesByElection(int electionId) {
        List<Candidate> candidates = new ArrayList<>();
        String sql = "SELECT * FROM candidates WHERE election_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, electionId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Candidate candidate = new Candidate();
                candidate.setId(rs.getInt("id"));
                candidate.setElectionId(rs.getInt("election_id"));
                candidate.setName(rs.getString("name"));
                candidate.setParty(rs.getString("party"));
                candidate.setBio(rs.getString("bio"));
                candidate.setPhotoUrl(rs.getString("photo_url"));
                candidate.setCreatedAt(rs.getTimestamp("created_at"));
                candidates.add(candidate);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return candidates;
    }

    public boolean deleteCandidate(int candidateId) {
        String sql = "DELETE FROM candidates WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, candidateId);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Candidate getCandidateById(int candidateId) {
        String sql = "SELECT * FROM candidates WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, candidateId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Candidate candidate = new Candidate();
                candidate.setId(rs.getInt("id"));
                candidate.setElectionId(rs.getInt("election_id"));
                candidate.setName(rs.getString("name"));
                candidate.setParty(rs.getString("party"));
                candidate.setBio(rs.getString("bio"));
                candidate.setPhotoUrl(rs.getString("photo_url"));
                candidate.setCreatedAt(rs.getTimestamp("created_at"));
                return candidate;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
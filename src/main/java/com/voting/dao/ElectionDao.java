package com.voting.dao;

import com.voting.model.Election;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ElectionDao {
    public boolean createElection(Election election) {
        String sql = "INSERT INTO elections(title, description, start_date, end_date) VALUES(?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, election.getTitle());
            pstmt.setString(2, election.getDescription());
            pstmt.setTimestamp(3, new java.sql.Timestamp(election.getStartDate().getTime()));
            pstmt.setTimestamp(4, new java.sql.Timestamp(election.getEndDate().getTime()));

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Election> getAllElections() {
        List<Election> elections = new ArrayList<>();
        String sql = "SELECT * FROM elections ORDER BY start_date DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Election election = new Election();
                election.setId(rs.getInt("id"));
                election.setTitle(rs.getString("title"));
                election.setDescription(rs.getString("description"));
                election.setStartDate(rs.getTimestamp("start_date"));
                election.setEndDate(rs.getTimestamp("end_date"));
                election.setStatus(rs.getString("status"));
                election.setCreatedAt(rs.getTimestamp("created_at"));
                elections.add(election);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return elections;
    }

    public Election getElectionById(int id) {
        String sql = "SELECT * FROM elections WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Election election = new Election();
                election.setId(rs.getInt("id"));
                election.setTitle(rs.getString("title"));
                election.setDescription(rs.getString("description"));
                election.setStartDate(rs.getTimestamp("start_date"));
                election.setEndDate(rs.getTimestamp("end_date"));
                election.setStatus(rs.getString("status"));
                election.setCreatedAt(rs.getTimestamp("created_at"));
                return election;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean updateElectionStatus(int electionId, String status) {
        String sql = "UPDATE elections SET status = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, status);
            pstmt.setInt(2, electionId);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteElection(int electionId) {
        String sql = "DELETE FROM elections WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, electionId);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
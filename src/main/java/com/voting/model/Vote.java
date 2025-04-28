package com.voting.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Vote {
    private int id;
    private int electionId;
    private int candidateId;
    private int userId;
    private Date votedAt;
    private Map<String, Object> additionalInfo = new HashMap<>();

    // Constructors
    public Vote() {}

    public Vote(int electionId, int candidateId, int userId) {
        this.electionId = electionId;
        this.candidateId = candidateId;
        this.userId = userId;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getElectionId() { return electionId; }
    public void setElectionId(int electionId) { this.electionId = electionId; }
    public int getCandidateId() { return candidateId; }
    public void setCandidateId(int candidateId) { this.candidateId = candidateId; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public Date getVotedAt() { return votedAt; }
    public void setVotedAt(Date votedAt) { this.votedAt = votedAt; }

    // Additional info methods
    public void setAdditionalInfo(String key, Object value) {
        additionalInfo.put(key, value);
    }

    public Object getAdditionalInfo(String key) {
        return additionalInfo.get(key);
    }

    public Map<String, Object> getAdditionalInfo() {
        return additionalInfo;
    }
}
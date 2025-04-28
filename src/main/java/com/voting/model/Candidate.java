package com.voting.model;

import java.util.Date;

public class Candidate {
    private int id;
    private int electionId;
    private String name;
    private String party;
    private String bio;
    private String photoUrl;
    private Date createdAt;

    // Constructors
    public Candidate() {}

    public Candidate(int electionId, String name, String party, String bio) {
        this.electionId = electionId;
        this.name = name;
        this.party = party;
        this.bio = bio;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getElectionId() { return electionId; }
    public void setElectionId(int electionId) { this.electionId = electionId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getParty() { return party; }
    public void setParty(String party) { this.party = party; }
    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }
    public String getPhotoUrl() { return photoUrl; }
    public void setPhotoUrl(String photoUrl) { this.photoUrl = photoUrl; }
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
}
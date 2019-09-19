package com.example.firebase;

public class Track {

    private String tracksId;
    private String tracksName;
    private int tracksRating;

    public Track(){}

    public Track(String tracksId, String tracksName, int tracksRating) {
        this.tracksId = tracksId;
        this.tracksName = tracksName;
        this.tracksRating = tracksRating;
    }

    public String getTracksId() {
        return tracksId;
    }

    public void setTracksId(String tracksId) {
        this.tracksId = tracksId;
    }

    public String getTracksName() {
        return tracksName;
    }

    public void setTracksName(String tracksName) {
        this.tracksName = tracksName;
    }

    public int getTracksRating() {
        return tracksRating;
    }

    public void setTracksRating(int tracksRating) {
        this.tracksRating = tracksRating;
    }
}

package com.blaisedev.starwarsmglt.models;

public class APIParams {

    public APIParams() {
    }

    private String endpoint;
    private int distance;

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }
}

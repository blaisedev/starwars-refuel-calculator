package com.blaisedev.starwarsmglt.models;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

public class APIParams {

    public APIParams() {
    }

    private String endpoint;

    @Min(value = 1, message="Value must be greater than or equal to 1")
    @Max(value = 1000000000, message="Value must be less than or equal to 1000000000")
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

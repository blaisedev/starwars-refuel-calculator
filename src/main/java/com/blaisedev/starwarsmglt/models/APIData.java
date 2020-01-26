package com.blaisedev.starwarsmglt.models;

import com.blaisedev.starwarsmglt.models.StarShip;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class APIData {

    public APIData() {
    }

    private int count;
    private String next;
    private String previous;
    private List<StarShip> results;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public String getPrevious() {
        return previous;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }

    public List<StarShip> getResults() {
        return results;
    }

    public void setResults(List<StarShip> results) {
        this.results = results;
    }
}

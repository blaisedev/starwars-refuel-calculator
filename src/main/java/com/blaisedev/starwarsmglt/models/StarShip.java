package com.blaisedev.starwarsmglt.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class StarShip {

    public StarShip() {
    }

    private String name;
    @JsonProperty("MGLT")
    private String MGLT;
    private String consumables;
    private int consumablesInHours;
    private String refuelCount;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMGLT() {
        return MGLT;
    }

    public void setMGLT(String MGLT) {
        this.MGLT = MGLT;
    }

    public String getConsumables() {
        return consumables;
    }

    public void setConsumables(String consumables) {
        this.consumables = consumables;
    }

    public int getConsumablesInHours() {
        return consumablesInHours;
    }

    public void setConsumablesInHours(int consumablesInHours) {
        this.consumablesInHours = consumablesInHours;
    }

    public String getRefuelCount() {
        return refuelCount;
    }

    public void setRefuelCount(String refuelCount) {
        this.refuelCount = refuelCount;
    }
}


package com.blaisedev.starwarsmglt.controller;

import com.blaisedev.starwarsmglt.models.APIData;
import com.blaisedev.starwarsmglt.models.APIParams;
import com.blaisedev.starwarsmglt.services.StarShipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@CrossOrigin
@RequestMapping("/api/starship")
public class StarShipController {

    @Autowired
    StarShipService starShipService;

    /**
     * Gateway method to the service which returns the StarShip data
     * @param apiParams  endpoint/ distance varies with user selection
     * @return  converted StarShip data
     */
    @PostMapping("/refuels")
    public APIData getStarShipRefuels(@Valid @RequestBody APIParams apiParams) {
        APIData apiData = starShipService.getStarShipRefuelData(apiParams);
        return apiData;
    }

}


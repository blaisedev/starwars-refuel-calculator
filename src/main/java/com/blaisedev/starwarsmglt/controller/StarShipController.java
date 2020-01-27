package com.blaisedev.starwarsmglt.controller;

import com.blaisedev.starwarsmglt.ConsumableType;
import com.blaisedev.starwarsmglt.models.APIData;
import com.blaisedev.starwarsmglt.models.APIParams;
import com.blaisedev.starwarsmglt.models.StarShip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin
@Validated
@RequestMapping("/api/starship")
public class StarShipController {

    @Autowired
    RestTemplate restTemplate;

    @Value("${api.endpoint}")
    private String defaultEndpoint;

    @Value("${api.page}")
    private int page;


    @RequestMapping("/refuels/{amount}")
    public APIData getListOfStarShipsByPage(@PathVariable @Min(1) @Max(100000000) int amount) {

        APIData apiData;
        List<StarShip> starShipsList = new ArrayList<>();
        do {
            apiData = restTemplate.getForObject(defaultEndpoint + page, APIData.class);
            apiData.getResults().forEach(f -> starShipsList.add(f));
            page++;
        } while(apiData.getNext() != null);
        page = 1;
        convertData(starShipsList, amount);
        apiData.setResults(starShipsList);
        return apiData;
    }

    
    @RequestMapping("/refuels")
    public APIData getStarShipRefuels(@RequestBody APIParams apiParams) {

        APIData apiData;
        String endpoint = apiParams.getEndpoint();
        int distance = apiParams.getDistance();
        if(endpoint == null) {
            apiData = restTemplate.getForObject(defaultEndpoint + page, APIData.class);
        } else {
            apiData = restTemplate.getForObject(endpoint , APIData.class);;
        }
        convertData(apiData.getResults(), distance);
        return apiData;
    }

    //TODO CHange this to accept APIDATA and return that
    private List<StarShip> convertData(List<StarShip> starShipList, int distance) {
        ConsumableType consumableType;
        for (StarShip starShip: starShipList) {
            //TODO CONVERT mglt and consumables to upper case to remove discrepencys method
            convertDataToUpperCase(starShip);
            //TODO return a sting type here
             consumableType = findConsumableTimeFrameType(starShip);
        //TODO then set the consumbable in hours by type
            setConsumableInHoursByType(starShip, consumableType);
        //TODO calculateNumberOfRefuels
            calculateNumberOfRefuels(starShip, distance);
            System.out.println("Name=" + starShip.getName() + " " +"MGFL=" + starShip.getMGLT() + " " +  " RefuelCount=" + starShip.getRefuelCount());
        }
        return starShipList;
    }

    private void convertDataToUpperCase(StarShip starShip) {
        String name = starShip.getName().toUpperCase();
        starShip.setName(name);
        String consumables = starShip.getConsumables().toUpperCase();
        starShip.setConsumables(consumables);
        String mglt = starShip.getMGLT().toUpperCase();
        starShip.setMGLT(mglt);
    }

    private void setConsumableInHoursByType(StarShip starShip, ConsumableType consumableType){

        switch(consumableType){
            case HOUR:
                setConsumableInHours(starShip);
                break;
            case DAY:
                calculateDaysToHours(starShip);
                break;
            case WEEK:
                calculateWeeksToHours(starShip);
                break;
            case MONTH:
                calculateMonthsToHours(starShip);
                break;
            case YEAR:
                calculateYearsToHours(starShip);
                break;
                default:
                    //TODO CHeck if needs changing
                    starShip.setRefuelCount(ConsumableType.UNKNOWN.toString());

        }
//        if(starShip.getConsumables().contains("hour")){
//            setConsumableInHours(starShip);
//            System.out.println("Name=" + starShip.getName() + " " +"MGFL=" + starShip.getMGLT() + " " + starShip.getConsumables() + " RefuelCount=" + starShip.getRefuelCount());
//        }else if(starShip.getConsumables().contains("day")){
//            calculateDaysToHours(starShip);
//            System.out.println("Name=" + starShip.getName() + " " +"MGFL=" + starShip.getMGLT() + " " + starShip.getConsumables() + " RefuelCount=" + starShip.getConsumablesInHours());
//        }else if(starShip.getConsumables().contains("week")){
//            calculateWeeksToHours(starShip);
//            System.out.println("Name=" + starShip.getName() + " " +"MGFL=" + starShip.getMGLT() + " " + starShip.getConsumables() + " RefuelCount=" + starShip.getConsumablesInHours());
//        }else if(starShip.getConsumables().contains("month")){
//            calculateMonthsToHours(starShip);
//            System.out.println("Name=" + starShip.getName() + " " +"MGFL=" + starShip.getMGLT() + " " + starShip.getConsumables() + " RefuelCount=" + starShip.getConsumablesInHours());
//        }else if(starShip.getConsumables().contains("year")){
//            calculateYearsToHours(starShip);
//            System.out.println("Name=" + starShip.getName() + " " +"MGFL=" + starShip.getMGLT() + " " + starShip.getConsumables() + " RefuelCount=" + starShip.getConsumablesInHours());
//        }else{
//            starShip.setRefuelCount(starShip.getConsumables());
//            System.out.println("Name=" + starShip.getName() + " " +"MGFL=" + starShip.getMGLT() + " " + starShip.getConsumables() + " RefuelCount=" + starShip.getRefuelCount());
//        }
    }

    private ConsumableType findConsumableTimeFrameType(StarShip starShip){
        String consumable = starShip.getConsumables();
        ConsumableType consumableType = ConsumableType.UNKNOWN;
        if(consumable.contains("HOUR")){
            consumableType = ConsumableType.HOUR;
        }else if(consumable.contains("DAY")){
            consumableType = ConsumableType.DAY;
        }else if(consumable.contains("WEEK")){
            consumableType = ConsumableType.WEEK;
        }else if(consumable.contains("MONTH")){
            consumableType = ConsumableType.MONTH;
        }else if(consumable.contains("YEAR")){
            consumableType = ConsumableType.YEAR;
        }
        return consumableType;
    }

    private void calculateNumberOfRefuels(StarShip starShip, int MGLTDistance) {

        String mglt = starShip.getMGLT();
        String consumables = starShip.getConsumables();
        String unknown = ConsumableType.UNKNOWN.toString();
        String numberOfRefuels;
        if((consumables.equals(unknown)) || (mglt.equals(unknown))){
            numberOfRefuels = unknown;
        } else {
            int consumablesInHours = starShip.getConsumablesInHours();
            int MGLTPerHour = Integer.valueOf(mglt);
            long MGLTTraveledBeforeRefuel = MGLTPerHour * consumablesInHours;
            long refuels = MGLTDistance / MGLTTraveledBeforeRefuel;
            numberOfRefuels = String.valueOf(refuels);
        }
        starShip.setRefuelCount(numberOfRefuels);
    }

    private void setConsumableInHours(StarShip starShip) {
        String totalHours = splitStringToNumber(starShip.getConsumables());
        int convertedHours = Integer.valueOf(totalHours);
        starShip.setConsumablesInHours(convertedHours);
    }

    private void calculateDaysToHours(StarShip starShip){
        String days = splitStringToNumber(starShip.getConsumables());
        int day = 24;
        int daysInHours = Integer.valueOf(days) * day;
        starShip.setConsumablesInHours(daysInHours);
    }

    private void calculateWeeksToHours(StarShip starShip){
        String weeks = splitStringToNumber(starShip.getConsumables());
        int week = 24  * 7;
        int weeksInHours = Integer.valueOf(weeks) * week;
        starShip.setConsumablesInHours(weeksInHours);
    }

    private void calculateMonthsToHours(StarShip starShip){
        String months = splitStringToNumber(starShip.getConsumables());
        //TODO rounding here so going to 720 should be 730
        double averageMonth = 365.0 / 12.0;
        double month = 24.0  * averageMonth;
        int monthsInHours = Integer.valueOf(months) * (int)month;
        starShip.setConsumablesInHours(monthsInHours);
    }

    private void calculateYearsToHours(StarShip starShip){
        String years = splitStringToNumber(starShip.getConsumables());
        int year = 24  * 365;
        int yearsInHours = Integer.valueOf(years) * year;
        starShip.setConsumablesInHours(yearsInHours);
    }

    private String splitStringToNumber(String time) {
        String[] split = time.split("\\s+");
        String newValue = split[0].trim();
        return newValue;
    }

    //TODO Set up service and utility classes
}


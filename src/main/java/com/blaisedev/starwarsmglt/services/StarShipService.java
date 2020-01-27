package com.blaisedev.starwarsmglt.services;


import com.blaisedev.starwarsmglt.ConsumableType;
import com.blaisedev.starwarsmglt.models.APIData;
import com.blaisedev.starwarsmglt.models.APIParams;
import com.blaisedev.starwarsmglt.models.StarShip;
import com.blaisedev.starwarsmglt.utilities.StarShipUtilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class StarShipService {


    @Autowired
    RestTemplate restTemplate;

    @Autowired
    StarShipUtilities starShipUtilities;

    @Value("${api.endpoint}")
    private String defaultEndpoint;

    @Value("${api.page}")
    private int page;

    /**
     * This method is the link with the outside api swapi.co. it collects the data
     * into an APIData object and passes it to convertData for data conversion
     * @param apiParams distance - the distance in which to do the calculation.
     *                  endpoint - varies with pagination
     * @return
     */
    public APIData getStarShipRefuelData(APIParams apiParams) {
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

    /**
     * Method chains together all necessary methods to convert the data.
     * Uses StarShipUtilities to do the majority of calculations.
     * @param starShipList
     * @param distance
     * @return
     */
    private List<StarShip> convertData(List<StarShip> starShipList, int distance) {
        ConsumableType consumableType;
        for (StarShip starShip: starShipList) {
            convertDataToUpperCase(starShip);

            consumableType = findConsumableTimeFrameType(starShip);

            setConsumableInHoursByType(starShip, consumableType);

            calculateNumberOfRefuels(starShip, distance);
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

    public void setConsumableInHoursByType(StarShip starShip, ConsumableType consumableType) {

        switch (consumableType) {
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
                starShip.setRefuelCount(ConsumableType.UNKNOWN.toString());

        }
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

    /**
     * Takes the string value in hours and converts it to number form
     * eg "7 Hours" -> 7
     * @param starShip
     */
    private void setConsumableInHours(StarShip starShip) {
        String totalHours = starShipUtilities.splitStringToNumber(starShip.getConsumables());
        int convertedHours = Integer.valueOf(totalHours);
        starShip.setConsumablesInHours(convertedHours);
    }

    /**
     * Takes the string value in days and converts it to hours and number form
     * eg "2 days" -> 48
     * @param starShip
     */
    private void calculateDaysToHours(StarShip starShip){
        String days = starShipUtilities.splitStringToNumber(starShip.getConsumables());
        int day = 24;
        int daysInHours = Integer.valueOf(days) * day;
        starShip.setConsumablesInHours(daysInHours);
    }

    /**
     * Takes the string value in weeks and converts it to hours and number form
     * eg "1 week" -> 168
     * @param starShip
     */
    private void calculateWeeksToHours(StarShip starShip){
        String weeks = starShipUtilities.splitStringToNumber(starShip.getConsumables());
        int week = 24  * 7;
        int weeksInHours = Integer.valueOf(weeks) * week;
        starShip.setConsumablesInHours(weeksInHours);
    }

    /**
     * Takes the string value in months and converts it to hours and number form
     * Month is taken as an average of the year! eg "2 months" -> 60.833
     * @param starShip
     */
    private void calculateMonthsToHours(StarShip starShip){
        String months = starShipUtilities.splitStringToNumber(starShip.getConsumables());
        double averageMonth = 365.0 / 12.0;
        double month = 24.0  * averageMonth;
        int monthsInHours = Integer.valueOf(months) * (int)month;
        starShip.setConsumablesInHours(monthsInHours);
    }

    /**
     * Takes the string value in years and converts it to hours and number form
     * eg "1 year" -> 8760
     * @param starShip
     */
    private void calculateYearsToHours(StarShip starShip){
        String years = starShipUtilities.splitStringToNumber(starShip.getConsumables());
        int year = 24  * 365;
        int yearsInHours = Integer.valueOf(years) * year;
        starShip.setConsumablesInHours(yearsInHours);
    }

    /**
     * Utility method to calculate the number of refuels needed over a given distance if
     * correct criteria available.
     * @param starShip
     * @param MGLTDistance
     */
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


}

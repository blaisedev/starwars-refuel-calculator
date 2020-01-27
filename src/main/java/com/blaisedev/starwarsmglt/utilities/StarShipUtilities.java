package com.blaisedev.starwarsmglt.utilities;

import org.springframework.stereotype.Component;

@Component
public class StarShipUtilities {

    /**
     * Utility method to take in string value containing a time frame eg
     * "1 hour" , "2 weeks" and return its number equivalent 1, 2
     * @param time
     * @return
     */
    public String splitStringToNumber(String time) {
        String[] split = time.split("\\s+");
        String newValue = split[0].trim();
        return newValue;
    }
}

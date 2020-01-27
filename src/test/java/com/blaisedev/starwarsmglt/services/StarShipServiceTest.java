package com.blaisedev.starwarsmglt.services;

import com.blaisedev.starwarsmglt.ConsumableType;
import com.blaisedev.starwarsmglt.models.APIData;
import com.blaisedev.starwarsmglt.models.APIParams;
import com.blaisedev.starwarsmglt.models.StarShip;
import com.blaisedev.starwarsmglt.utilities.StarShipUtilities;
import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.client.RestTemplate;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
class StarShipServiceTest {

    @InjectMocks
    StarShipService starShipService;

    @Mock
    StarShipUtilities starShipUtilities;

    @Mock
    RestTemplate restTemplate;

    StarShip testStarShip;
    APIData apiData;
    APIParams apiParams;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        testStarShip = new StarShip();
        apiData = new APIData();
        apiParams = new APIParams();
    }

    @Nested
    @DisplayName("Testing getStarShipRefuelData")
    class getStarShipRefuelData {

        @Test
        @DisplayName("Testing Set Consumables In Hours where Hours")
        void testHourType() {
            apiParams.setEndpoint("url");
            apiParams.setDistance(1000000);
            List<StarShip> list = new ArrayList<>();
            testStarShip.setMGLT("75");
            testStarShip.setConsumables("2 MONTHS");
            testStarShip.setName("MILLENIUM FALCON");
            list.add(testStarShip);
            apiData.setResults(list);
            when(restTemplate.getForObject("url", APIData.class)).thenReturn(apiData);
            when(starShipUtilities.splitStringToNumber(testStarShip.getConsumables())).thenReturn("2");
            APIData actual = starShipService.getStarShipRefuelData(apiParams);
            APIData expected = apiData;
            assertEquals(expected, actual);

        }

    }

    @Nested
    @DisplayName("Testing Set Consumables In Hours By Type")
    class ConsumablesInHourByType {

        @Test
        @DisplayName("Testing Set Consumables In Hours where Hours")
        void testHourType() {

            testStarShip.setConsumables("8 HOURS");
            when(starShipUtilities.splitStringToNumber(testStarShip.getConsumables())).thenReturn("8");
            starShipService.setConsumableInHoursByType(testStarShip, ConsumableType.HOUR);
            int expected = 8;
            int actual = testStarShip.getConsumablesInHours();
            assertEquals(expected, actual);

        }

        @Test
        @DisplayName("Testing Calculate Days To Hours")
        void testDayType() {
            testStarShip.setConsumables("2 DAYS");
            when(starShipUtilities.splitStringToNumber(testStarShip.getConsumables())).thenReturn("2");
            starShipService.setConsumableInHoursByType(testStarShip, ConsumableType.DAY);

            int expected = 48;
            int actual = testStarShip.getConsumablesInHours();
            assertEquals(expected, actual);

        }

        @Test
        @DisplayName("Testing Calculate Weeks To Hours")
        void testWeekType() {
            testStarShip.setConsumables("1 WEEK");
            when(starShipUtilities.splitStringToNumber(testStarShip.getConsumables())).thenReturn("1");
            starShipService.setConsumableInHoursByType(testStarShip, ConsumableType.WEEK);
            int expected = 168;
            int actual = testStarShip.getConsumablesInHours();
            assertEquals(expected, actual);

        }

        @Test
        @DisplayName("Testing Calculate Months To Hours")
        void testMonthType() {
            testStarShip.setConsumables("3 MONTHS");
            when(starShipUtilities.splitStringToNumber(testStarShip.getConsumables())).thenReturn("3");
            starShipService.setConsumableInHoursByType(testStarShip, ConsumableType.MONTH);
            int expected = 2190;
            int actual = testStarShip.getConsumablesInHours();
            assertEquals(expected, actual);

        }

        @Test
        @DisplayName("Testing Calculate Years To Hours")
        void testYearType() {
            testStarShip.setConsumables("5 YEARS");
            when(starShipUtilities.splitStringToNumber(testStarShip.getConsumables())).thenReturn("5");
            starShipService.setConsumableInHoursByType(testStarShip, ConsumableType.YEAR);
            int expected = 43800;
            int actual = testStarShip.getConsumablesInHours();
            assertEquals(expected, actual);

        }

        @Test
        @DisplayName("Testing default")
        void testUnknownType() {
            testStarShip.setConsumables("UNKNOWN");
            starShipService.setConsumableInHoursByType(testStarShip, ConsumableType.UNKNOWN);
            String expected = "UNKNOWN";
            String actual = testStarShip.getRefuelCount();
            assertEquals(expected, actual);

        }
    }


}
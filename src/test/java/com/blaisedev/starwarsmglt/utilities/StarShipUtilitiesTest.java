package com.blaisedev.starwarsmglt.utilities;

import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.jupiter.api.Assertions.*;


@RunWith(MockitoJUnitRunner.class)
class StarShipUtilitiesTest {

    @InjectMocks
    StarShipUtilities starShipUtilities;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @DisplayName("Testing Split String To Number")
    void splitStringToNumber() {
        String test = "5 YEARS";
        String actual = starShipUtilities.splitStringToNumber(test);
        String expected = "5";
        assertEquals(expected, actual);
    }
}
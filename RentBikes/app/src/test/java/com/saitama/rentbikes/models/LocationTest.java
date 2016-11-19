package com.saitama.rentbikes.models;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by gabrielvega on 2016-11-19.
 */
public class LocationTest {

    Location location;
    private static final double EXPECTED_LAT = 66.12123123;
    private static final double EXPECTED_LNG = 10.2323232;

    @Before
    public void setUp() throws Exception {
        location = new Location(66.12123123, 10.2323232);
    }

    @Test
    public void getLat() throws Exception {
        assertEquals(EXPECTED_LAT, location.getLat(), 0);
    }

    @Test
    public void setLat() throws Exception {
        location.setLat(66.12123123);
        assertEquals(EXPECTED_LAT, location.getLat(), 0);

    }

    @Test
    public void getLng() throws Exception {
        assertEquals(EXPECTED_LNG, location.getLng(), 0);
    }

    @Test
    public void setLng() throws Exception {
        location.setLng(10.2323232);
        assertEquals(EXPECTED_LNG, location.getLng(), 0);

    }

}
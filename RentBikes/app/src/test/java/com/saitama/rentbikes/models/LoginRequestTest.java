package com.saitama.rentbikes.models;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by gabrielvega on 2016-11-19.
 */
public class LoginRequestTest {

    LoginRequest loginRequest;
    private static final String EXPECTED_EMAIL = "crossover@crossover.com";
    private static final String EXPECTED_PASSWORD = "crossover";

    @Before
    public void setUp() throws Exception {
        loginRequest = new LoginRequest();
        loginRequest.setEmail(EXPECTED_EMAIL);
        loginRequest.setPassword(EXPECTED_PASSWORD);
    }

    @Test
    public void getEmail() throws Exception {
        assertEquals(EXPECTED_EMAIL, loginRequest.getEmail());

    }

    @Test
    public void setEmail() throws Exception {
        loginRequest.setEmail("crossover@crossover.com");
        assertEquals(EXPECTED_EMAIL, loginRequest.getEmail());

    }

    @Test
    public void getPassword() throws Exception {
        assertEquals(EXPECTED_PASSWORD, loginRequest.getPassword());

    }

    @Test
    public void setPassword() throws Exception {

        loginRequest.setEmail("crossover");
        assertEquals(EXPECTED_PASSWORD, loginRequest.getPassword());

    }

}
package com.saitama.rentbikes.models;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by gabrielvega on 2016-11-19.
 */
public class LoginResponseTest {

    LoginResponse loginResponse;
    private static final String EXPECTED_TOKEN = "AYqCb97qqSQ2AbMNjKR5skKQbpOpFE3oLr43A9NDFmABpIAtkgAAAAA";


    @Before
    public void setUp() throws Exception {
        loginResponse = new LoginResponse();
        loginResponse.setAccessToken(EXPECTED_TOKEN);
    }

    @Test
    public void getAccessToken() throws Exception {
        assertEquals(EXPECTED_TOKEN, loginResponse.getAccessToken());
    }

    @Test
    public void setAccessToken() throws Exception {
        loginResponse.setAccessToken("AYqCb97qqSQ2AbMNjKR5skKQbpOpFE3oLr43A9NDFmABpIAtkgAAAAA");
        assertEquals(EXPECTED_TOKEN, loginResponse.getAccessToken());
    }

}
package com.saitama.rentbikes.models;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by gabrielvega on 2016-11-19.
 */
public class SignupResponseTest {

    SignupResponse signupResponse;
    private static final String EXPECTED_TOKEN = "AYqCb97qqSQ2AbMNjKR5skKQbpOpFE3oLr43A9NDFmABpIAtkgAAAAA";


    @Before
    public void setUp() throws Exception {
        signupResponse = new SignupResponse();
        signupResponse.setAccessToken(EXPECTED_TOKEN);
    }

    @Test
    public void getAccessToken() throws Exception {
        assertEquals(EXPECTED_TOKEN, signupResponse.getAccessToken());

    }

    @Test
    public void setAccessToken() throws Exception {
        signupResponse.setAccessToken("AYqCb97qqSQ2AbMNjKR5skKQbpOpFE3oLr43A9NDFmABpIAtkgAAAAA");
        assertEquals(EXPECTED_TOKEN, signupResponse.getAccessToken());

    }

}
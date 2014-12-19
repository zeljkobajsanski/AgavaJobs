package com.bitseverywhere.agavajobs.tests;

import android.test.InstrumentationTestCase;

import com.bitseverywhere.agavajobs.models.domain.Biografija;
import com.bitseverywhere.agavajobs.services.HttpService;

import junit.framework.Assert;

import org.json.JSONException;

import java.io.IOException;

/**
 * Created by Željko on 18.12.2014..
 */
public class HttpServiceTest extends InstrumentationTestCase {

    public void testSacuvajBiografiju() throws Exception {
        Biografija cv = HttpService.getInstance().vratiBiografiju(7);
        int id = HttpService.getInstance().sacuvajBiografiju(cv);
        assertTrue(id == 7);
    }

    public void testSuccessfulLogin() throws Exception {
        int userId = HttpService.getInstance().login("zeljko.bajsanski@gmail.com", "1");
        assertEquals(5, userId);
    }

    public void testUnsuccessfulLogin() {
        try {
            HttpService.getInstance().login("zeljko.bajsanski@gmail.com", "");
        } catch (Exception e) {
            Assert.assertEquals("User not logged in", e.getMessage());
        }
    }

    public void testUcitajBiografiju() throws IOException, JSONException {
        Biografija cv = HttpService.getInstance().vratiBiografiju(3232);
    }
}

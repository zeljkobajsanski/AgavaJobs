package com.bitseverywhere.agavajobs.tests;

import android.test.InstrumentationTestCase;

import com.bitseverywhere.agavajobs.models.domain.Biografija;
import com.bitseverywhere.agavajobs.models.domain.Posao;
import com.bitseverywhere.agavajobs.services.HttpService;

import junit.framework.Assert;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

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

    public void testRegistrujNalog() throws Exception {
        int userId = HttpService.getInstance().registrujNalog("Zeljko", "Bajsanski",
                "bitseverywhere@gmail.com", "1");
        Assert.assertTrue(userId > 0);
    }

    public void testVratiMojeKonkurse() throws Exception {
        List<Posao> poslovi = HttpService.getInstance().vratiMojeKonkurse(3232);
        Assert.assertTrue(poslovi.size() > 0);
    }

    public void testPing() throws IOException {
        HttpService.getInstance().ping();
    }
}

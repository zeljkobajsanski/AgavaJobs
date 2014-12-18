package com.bitseverywhere.agavajobs.tests;

import android.test.InstrumentationTestCase;

import com.bitseverywhere.agavajobs.models.domain.Biografija;
import com.bitseverywhere.agavajobs.services.HttpService;

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
}

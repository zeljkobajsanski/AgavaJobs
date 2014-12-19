package com.bitseverywhere.agavajobs.models.domain;

/**
 * Created by Željko on 19.12.2014..
 */
public class Stats {
    private int premijum, hot, standarni;

    public Stats(int premijum, int hot, int standarni) {
        this.premijum = premijum;
        this.hot = hot;
        this.standarni = standarni;
    }

    public int getPremijum() {
        return premijum;
    }

    public int getHot() {
        return hot;
    }

    public int getStandarni() {
        return standarni;
    }
}

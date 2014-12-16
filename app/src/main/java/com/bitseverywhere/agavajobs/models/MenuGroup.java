package com.bitseverywhere.agavajobs.models;

/**
 * Created by Željko on 13.12.2014..
 */
public class MenuGroup implements IMenuItem {

    private String caption;

    public MenuGroup(String caption) {
        this.caption = caption;
    }

    @Override
    public boolean isMenuGroup() {
        return true;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }
}

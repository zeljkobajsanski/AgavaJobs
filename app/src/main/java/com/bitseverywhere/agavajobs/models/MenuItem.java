package com.bitseverywhere.agavajobs.models;

import android.graphics.drawable.Drawable;

/**
 * Created by Željko on 13.12.2014..
 */
public class MenuItem implements IMenuItem {
    private Drawable image;
    private String caption;

    public MenuItem(Drawable image, String caption) {
        this.image = image;
        this.caption = caption;
    }

    public Drawable getImage() {
        return image;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    @Override
    public boolean isMenuGroup() {
        return false;
    }
}

package se.evinja.hazeldroid.navigation;

import android.graphics.drawable.Drawable;


public class Item_Navigation {
    private String title;
    private Drawable icon;

    public Item_Navigation(String title, Drawable icon) {
        this.title = title;
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }
}

package com.github.chengheaven.sidebar;

/**
 * @author Heaven・Cheng Created on 17/7/17.
 */

public class SidebarBean {

    private String title;
    private int color;
    private int imageResource;
    private int imageResourceSelected = 0;


    public SidebarBean(String title) {
        this.title = title;
    }

    public SidebarBean(String title, int imageResource) {
        this.title = title;
        this.imageResource = imageResource;
    }

    public SidebarBean(String title, int color, int imageResource) {
        this.title = title;
        this.color = color;
        this.imageResource = imageResource;
    }

    public SidebarBean(String title, int color, int imageResource, int imageResourceSelected) {
        this.title = title;
        this.color = color;
        this.imageResource = imageResource;
        this.imageResourceSelected = imageResourceSelected;
    }


    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImageResourceSelected() {
        return imageResourceSelected;
    }

    public void setImageResourceSelected(int imageResourceActive) {
        this.imageResourceSelected = imageResourceActive;
    }
}

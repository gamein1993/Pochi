package com.will.pochi.item;

public abstract class Meal {
    private final int imageId;
    private final int imageWidth;
    private final int imageHeight;
    public Meal(int imageId) {
        this.imageId = imageId;
        this.imageWidth = 0;
        this.imageHeight = 0;
    }
    public int getImageId() {
        return imageId;
    }
}

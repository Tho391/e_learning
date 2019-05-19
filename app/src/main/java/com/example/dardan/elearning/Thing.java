package com.example.dardan.elearning;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by Dardan on 4/5/2016.
 */
public class Thing implements Serializable {
    private int id;
    private Bitmap image;
    private String sound;
    private String text;
    private String noise;
    private int categoryId;


    public Thing(Bitmap image, String sound, String text, String noise, int categoryId) {
        this.image = image;
        this.sound = sound;
        this.text = text;
        this.noise = noise;
        this.categoryId = categoryId;
    }

    public Thing(Bitmap image, String sound, String text, String noise) {
        this.image = image;
        this.sound = sound;
        this.text = text;
        this.noise = noise;
    }

    public Thing(Bitmap image, String sound, String text) {
        this(image, sound, text, "0");
    }

    public Thing() {

    }

    public Thing(String thingName, Bitmap thingImage) {
        new Thing(thingImage, "", thingName, "");
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getNoise() {
        return noise;
    }

    public void setNoise(String noise) {
        this.noise = noise;
    }

    public boolean hasNoise() {
        return false;
        //return this.noise != 0;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }
}

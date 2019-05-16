package com.example.dardan.elearning;

/**
 * Created by Dardan on 4/5/2016.
 */
public class Thing {
    private int id;
    private String image;
    private String sound;
    private String text;
    private String noise;

    public void setNoise(String noise) {
        this.noise = noise;
    }

    private int categoryId;



    public Thing(String image, String sound, String text, String noise, int categoryId) {
        this.image = image;
        this.sound = sound;
        this.text = text;
        this.noise = noise;
        this.categoryId = categoryId;
    }

    public Thing(String image, String sound, String text, String noise) {
        this.image = image;
        this.sound = sound;
        this.text = text;
        this.noise = noise;
    }

    public Thing(String image, String sound, String text) {
        this(image, sound, text, "0");
    }

    public Thing() {

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

    public boolean hasNoise() {
        return false;
        //return this.noise != 0;
    }

    public String getSound() { return sound; }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
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

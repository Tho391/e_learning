package com.example.dardan.elearning;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.Serializable;
import java.util.ArrayList;

import static com.example.dardan.elearning.MySQLiteHelper.COLORS;
import static com.example.dardan.elearning.MySQLiteHelper.THEMES;

public class Category implements Serializable {
    ArrayList<Thing> things;
    int currentIndex = 0;
    int id;
    String title;
    Bitmap image;
    int highScore;
    int color;
    int theme;

    public Category(Context context) {
        title = "Title";
        Bitmap b = BitmapFactory.decodeResource(context.getResources(), R.drawable.add_image);
        image = b;
        highScore = 0;
        color = COLORS[1];
        theme = THEMES[1];
        things = new ArrayList<>();
    }

    public Category(String title, Bitmap image, int highScore, int color, int theme) {
        this.title = title;
        this.image = image;
        this.highScore = highScore;
        this.color = color;
        this.theme = theme;
        this.things = new ArrayList<>();
    }

    public void addThing(Thing thing) {
        things.add(thing);
    }

    public ArrayList<Thing> getListOfThings() {
        return this.things;
    }

    public Thing nextThing() {
        if (hasNextThing())
            return things.get(++currentIndex);
        return null;
    }

    public Thing prevThing() {
        if (hasPrevThing())
            return things.get(--currentIndex);
        return null;
    }

    public Thing currentThing() {
        return things.get(currentIndex);
    }

    boolean hasNextThing() {
        return currentIndex < things.size() - 1;
    }

    boolean hasPrevThing() {
        return currentIndex > 0;
    }

    public void goToFirstThing() {
        currentIndex = 0;
    }

    public void updateHighscore() {
        //this.highScore = Highscores.getHighscore(this.columnName);
    }
}

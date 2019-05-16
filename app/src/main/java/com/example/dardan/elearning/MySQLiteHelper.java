package com.example.dardan.elearning;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import static com.example.dardan.elearning.Ulti.getUrl;

/**
 * Created by Dardan on 11/17/2016.
 */

public class MySQLiteHelper extends SQLiteOpenHelper {
    //datebase
    private static final String DATABASE_NAME = "e_learning.db";
    private static final int DATABASE_VERSION = 1;
    //common column
    public static final String COLUMN_ID = "_id";

    //table category
    public static final String TABLE_CATEGORY = "category";
    public static final String COLUMN_COLOR = "color";
    public static final String COLUMN_HIGHSCORE = "highScore";
    public static final String COLUMN_THEME = "theme";
    public static final String COLUMN_TITLE = "title";
    //table thing and it's columns
    public static final String TABLE_THING = "thing";
    public static final String COLUMN_IMAGE = "image";
    public static final String COLUMN_TEXT = "text";
    public static final String COLUMN_NOISE = "noise";
    public static final String COLUMN_SOUND = "sound";
    private static final String COLUMN_CATEGORY_ID = "categoryId";
    //create table category
    private static final String CREATE_TABLE_CATEGORY =
            "create table " + TABLE_CATEGORY
                    + "( "
                    + COLUMN_ID + " integer primary key autoincrement,"
                    + COLUMN_TITLE + " text,"
                    + COLUMN_IMAGE + " text,"
                    + COLUMN_HIGHSCORE + " integer,"
                    + COLUMN_COLOR + " integer,"
                    + COLUMN_THEME + " integer"
                    + ");";

    //create table thing
    private static final String CREATE_TABLE_THING =
            "create table " + TABLE_THING
                    + "( "
                    + COLUMN_ID + " integer primary key autoincrement,"
                    + COLUMN_TEXT + " text,"
                    + COLUMN_IMAGE + " text,"
                    + COLUMN_SOUND + " text,"
                    + COLUMN_NOISE + " text,"
                    + COLUMN_CATEGORY_ID + " integer"
                    + ");";


    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_CATEGORY);
        db.execSQL(CREATE_TABLE_THING);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_THING);

        onCreate(db);
    }

    //add default category
    public void createDefaultCategory() {
        int count = getCategoryCount();
        //create 4 category
        if (count == 0) {
            Category fruitCategory = new Category(
                    "Fruits",
                    getUrl(R.drawable.fruits).toString(),
                    0,
                    R.color.primary_dark,
                    R.style.GreenTheme);

            fruitCategory.addThing(new Thing(getUrl(R.drawable.apple).toString(), getUrl(R.raw.apple).toString(), "Apple"));
            fruitCategory.addThing(new Thing(getUrl(R.drawable.orange).toString(), getUrl(R.raw.orange).toString(), "Orange"));
            fruitCategory.addThing(new Thing(getUrl(R.drawable.banana).toString(), getUrl(R.raw.banana).toString(), "Banana"));
            fruitCategory.addThing(new Thing(getUrl(R.drawable.cherry).toString(), getUrl(R.raw.cherry).toString(), "Cherry"));
            fruitCategory.addThing(new Thing(getUrl(R.drawable.dates).toString(), getUrl(R.raw.dates).toString(), "Dates"));
            fruitCategory.addThing(new Thing(getUrl(R.drawable.coconut).toString(), getUrl(R.raw.coconut).toString(), "Coconut"));
            fruitCategory.addThing(new Thing(getUrl(R.drawable.grape).toString(), getUrl(R.raw.grape).toString(), "Grape"));
            fruitCategory.addThing(new Thing(getUrl(R.drawable.kiwi).toString(), getUrl(R.raw.kiwi).toString(), "Kiwi"));
            fruitCategory.addThing(new Thing(getUrl(R.drawable.lemon).toString(), getUrl(R.raw.lemon).toString(), "Lemon"));
            fruitCategory.addThing(new Thing(getUrl(R.drawable.peach).toString(), getUrl(R.raw.peach).toString(), "Peach"));
            fruitCategory.addThing(new Thing(getUrl(R.drawable.pear).toString(), getUrl(R.raw.pear).toString(), "Pear"));
            fruitCategory.addThing(new Thing(getUrl(R.drawable.persimmon).toString(), getUrl(R.raw.persimmon).toString(), "Persimmon"));
            fruitCategory.addThing(new Thing(getUrl(R.drawable.pineapple).toString(), getUrl(R.raw.pineapple).toString(), "Pineapple"));
            fruitCategory.addThing(new Thing(getUrl(R.drawable.plum).toString(), getUrl(R.raw.plum).toString(), "Plum"));
            fruitCategory.addThing(new Thing(getUrl(R.drawable.raspberry).toString(), getUrl(R.raw.raspberry).toString(), "Raspberry"));
            fruitCategory.addThing(new Thing(getUrl(R.drawable.strawberry).toString(), getUrl(R.raw.strawberry).toString(), "Strawberry"));
            fruitCategory.addThing(new Thing(getUrl(R.drawable.watermelon).toString(), getUrl(R.raw.watermelon).toString(), "Watermelon"));
            fruitCategory.addThing(new Thing(getUrl(R.drawable.mango).toString(), getUrl(R.raw.mango).toString(), "Mango"));


            addCategory(fruitCategory);

        }


    }

    public int getCategoryCount() {
        String countQuery = "SELECT * FROM " + TABLE_CATEGORY;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        // return count
        return count;
    }

    public void addCategory(Category category) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, category.title);
        values.put(COLUMN_IMAGE, category.image);
        values.put(COLUMN_HIGHSCORE, category.highScore);
        values.put(COLUMN_COLOR, category.color);
        values.put(COLUMN_THEME, category.theme);
        // insert 1 row to table
        db.insert(TABLE_CATEGORY, null, values);

        int lastCategoryId = getLastId(TABLE_CATEGORY);

        for (Thing i : category.things) {
            ContentValues values1 = new ContentValues();
            values.put(COLUMN_TEXT, i.getText());
            values.put(COLUMN_IMAGE, i.getImage());
            values.put(COLUMN_SOUND, i.getNoise());
            values.put(COLUMN_NOISE, i.getSound());
            values.put(COLUMN_CATEGORY_ID, lastCategoryId);
            db.insert(TABLE_THING, null, values1);
        }

        // close connection
        db.close();
    }

    private int getLastId(String table) {
        String countQuery = "SELECT max(_id) FROM " + table;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int id = Integer.parseInt(cursor.getString(0));

        db.close();
        return id;
    }

    public int updateCategory(Category category) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, category.title);
        values.put(COLUMN_IMAGE, category.image);
        values.put(COLUMN_HIGHSCORE, category.highScore);
        values.put(COLUMN_COLOR, category.color);
        values.put(COLUMN_THEME, category.theme);
        // updating row
        int update1 = db.update(TABLE_CATEGORY, values, COLUMN_ID + " = ?",
                new String[]{String.valueOf(category.id)});
        for (Thing i : category.things) {
            ContentValues values1 = new ContentValues();
            values.put(COLUMN_TEXT, i.getText());
            values.put(COLUMN_IMAGE, i.getImage());
            values.put(COLUMN_SOUND, i.getNoise());
            values.put(COLUMN_NOISE, i.getSound());
            values.put(COLUMN_CATEGORY_ID, i.getCategoryId());

            db.update(TABLE_THING, values1, COLUMN_CATEGORY_ID + " = ?",
                    new String[]{String.valueOf(category.id)});
        }


        return update1;
    }

    public void deleteCategory(Category category) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (Thing i : category.things) {
            db.delete(TABLE_THING, COLUMN_CATEGORY_ID + " = ?",
                    new String[]{String.valueOf(category.id)});
        }
        db.delete(TABLE_CATEGORY, COLUMN_ID + " = ?",
                new String[]{String.valueOf(category.id)});
        db.close();
    }

    public List<Category> getAllCategory() {
        ArrayList<Category> categoryList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_CATEGORY;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // Browse on the cursor, and add to the list.
        if (cursor.moveToFirst()) {
            do {
                Category category = new Category();
                category.id = Integer.parseInt(cursor.getString(0));
                category.title = cursor.getString(1);
                category.image = cursor.getString(2);
                category.highScore = Integer.parseInt(cursor.getString(3));
                category.color = Integer.parseInt(cursor.getString(4));
                category.theme = Integer.parseInt(cursor.getString(5));
                // add to list
                categoryList.add(category);
            } while (cursor.moveToNext());
        }
        // return note list
        return categoryList;
    }

    public Category getCategory(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CATEGORY, new String[]{COLUMN_ID,
                        COLUMN_TITLE, COLUMN_IMAGE, COLUMN_HIGHSCORE, COLUMN_COLOR, COLUMN_THEME},
                COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Category category = new Category();
        category.id = id;
        if (cursor != null) {

            category.title = cursor.getString(1);
            category.image = cursor.getString(2);
            category.highScore = Integer.parseInt(cursor.getString(3));
            category.color = Integer.parseInt(cursor.getString(4));
            category.theme = Integer.parseInt(cursor.getString(5));

            Cursor cursor1 = db.query(TABLE_THING,
                    new String[]{COLUMN_ID, COLUMN_TEXT, COLUMN_IMAGE,COLUMN_SOUND, COLUMN_NOISE, COLUMN_CATEGORY_ID},
                    COLUMN_CATEGORY_ID + " = ?",
                    new String[]{String.valueOf(id)}, null, null, null, null);
            if (cursor1 != null)
                cursor1.moveToFirst();
            ArrayList<Thing> things = new ArrayList<>();
            do {
                Thing thing = new Thing();

                thing.setId(Integer.parseInt(cursor.getString(0)));
                thing.setText(cursor.getString(1));
                thing.setImage(cursor.getString(2));
                thing.setSound(cursor.getString(3));
                thing.setNoise(cursor.getString(4));
                thing.setCategoryId(Integer.parseInt(cursor.getString(5)));
                // add to list
                things.add(thing);
            } while (cursor1.moveToNext());
        }

        // return note
        return category;
    }
}

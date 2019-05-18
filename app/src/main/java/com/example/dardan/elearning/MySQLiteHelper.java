package com.example.dardan.elearning;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.ArrayList;

import static com.example.dardan.elearning.Ulti.BitmapToByte;
import static com.example.dardan.elearning.Ulti.ByteToBitmap;

/**
 * Created by Dardan on 11/17/2016.
 */

public class MySQLiteHelper extends SQLiteOpenHelper {
    private Context context;
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
    public static final String COLUMN_CATEGORY_ID = "categoryId";
    //create table category
    private static final String CREATE_TABLE_CATEGORY =
            "create table " + TABLE_CATEGORY
                    + "( "
                    + COLUMN_ID + " integer primary key autoincrement,"
                    + COLUMN_TITLE + " text,"
                    + COLUMN_IMAGE + " BLOB,"
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
                    + COLUMN_IMAGE + " BLOB,"
                    + COLUMN_SOUND + " BLOB,"
                    + COLUMN_NOISE + " BLOB,"
                    + COLUMN_CATEGORY_ID + " integer"
                    + ");";
    public static final int[] COLORS = {R.color.primary, R.color.primary_dark, R.color.primary_light,
            R.color.blue_primary, R.color.blue_primary_dark, R.color.blue_primary_light,
            R.color.pink_primary, R.color.pink_primary_dark, R.color.pink_primary_light,
            R.color.purple_primary, R.color.purple_primary_dark, R.color.pink_primary_light};
    public static final int[] THEMES = {R.style.GreenTheme, R.style.BlueTheme, R.style.PinkTheme, R.style.PurpleTheme};

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
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


        //byte[] fruitsImage = BitmapToByte(bmp);

        int count = getCategoryCount();
        if (count == 0) {
            Bitmap fruitsImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.fruits);
            Category fruitCategory = new Category("Fruits", fruitsImage, 0, COLORS[6], THEMES[1]);
            fruitCategory.addThing(new Thing(BitmapFactory.decodeResource(context.getResources(), R.drawable.apple), String.valueOf(R.raw.apple), "Apple", "0"));
            fruitCategory.addThing(new Thing(BitmapFactory.decodeResource(context.getResources(), R.drawable.orange), String.valueOf(R.raw.orange), "Orange", "0"));
            fruitCategory.addThing(new Thing(BitmapFactory.decodeResource(context.getResources(), R.drawable.banana), String.valueOf(R.raw.banana), "Banana", "0"));
            fruitCategory.addThing(new Thing(BitmapFactory.decodeResource(context.getResources(), R.drawable.cherry), String.valueOf(R.raw.cherry), "Cherry", "0"));
            fruitCategory.addThing(new Thing(BitmapFactory.decodeResource(context.getResources(), R.drawable.dates), String.valueOf(R.raw.dates), "Dates", "0"));
            fruitCategory.addThing(new Thing(BitmapFactory.decodeResource(context.getResources(), R.drawable.coconut), String.valueOf(R.raw.coconut), "Coconut", "0"));
            fruitCategory.addThing(new Thing(BitmapFactory.decodeResource(context.getResources(), R.drawable.grape), String.valueOf(R.raw.grape), "Grape", "0"));
            fruitCategory.addThing(new Thing(BitmapFactory.decodeResource(context.getResources(), R.drawable.kiwi), String.valueOf(R.raw.kiwi), "Kiwi", "0"));
            fruitCategory.addThing(new Thing(BitmapFactory.decodeResource(context.getResources(), R.drawable.lemon), String.valueOf(R.raw.lemon), "Lemon", "0"));
            fruitCategory.addThing(new Thing(BitmapFactory.decodeResource(context.getResources(), R.drawable.peach), String.valueOf(R.raw.peach), "Peach", "0"));
            fruitCategory.addThing(new Thing(BitmapFactory.decodeResource(context.getResources(), R.drawable.pear), String.valueOf(R.raw.pear), "Pear", "0"));
            fruitCategory.addThing(new Thing(BitmapFactory.decodeResource(context.getResources(), R.drawable.persimmon), String.valueOf(R.raw.persimmon), "Persimmon", "0"));
            fruitCategory.addThing(new Thing(BitmapFactory.decodeResource(context.getResources(), R.drawable.pineapple), String.valueOf(R.raw.pineapple), "Pineapple", "0"));
            fruitCategory.addThing(new Thing(BitmapFactory.decodeResource(context.getResources(), R.drawable.plum), String.valueOf(R.raw.plum), "Plum", "0"));
            fruitCategory.addThing(new Thing(BitmapFactory.decodeResource(context.getResources(), R.drawable.raspberry), String.valueOf(R.raw.raspberry), "Raspberry", "0"));
            fruitCategory.addThing(new Thing(BitmapFactory.decodeResource(context.getResources(), R.drawable.strawberry), String.valueOf(R.raw.strawberry), "Strawberry", "0"));
            fruitCategory.addThing(new Thing(BitmapFactory.decodeResource(context.getResources(), R.drawable.watermelon), String.valueOf(R.raw.watermelon), "Watermelon", "0"));
            fruitCategory.addThing(new Thing(BitmapFactory.decodeResource(context.getResources(), R.drawable.mango), String.valueOf(R.raw.mango), "Mango", "0"));

            addCategory(fruitCategory);

            Bitmap animalImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.animals);
            Category animalCategory = new Category("Animals", animalImage, 0, COLORS[4], THEMES[2]);
            animalCategory.addThing(new Thing(BitmapFactory.decodeResource(context.getResources(), R.drawable.dog), String.valueOf(R.raw.dog), "Dog", R.raw.dognoise + ""));
            animalCategory.addThing(new Thing(BitmapFactory.decodeResource(context.getResources(), R.drawable.bear), String.valueOf(R.raw.bear), "Bear", R.raw.bearnoise + ""));
            animalCategory.addThing(new Thing(BitmapFactory.decodeResource(context.getResources(), R.drawable.wolf), String.valueOf(R.raw.wolf), "Wolf", R.raw.wolfnoise + ""));
            animalCategory.addThing(new Thing(BitmapFactory.decodeResource(context.getResources(), R.drawable.dolphin), String.valueOf(R.raw.dolphin), "Dolphin", R.raw.dolphinnoise + ""));
            animalCategory.addThing(new Thing(BitmapFactory.decodeResource(context.getResources(), R.drawable.duck), String.valueOf(R.raw.duck), "Duck", R.raw.ducknoise + ""));
            animalCategory.addThing(new Thing(BitmapFactory.decodeResource(context.getResources(), R.drawable.leopard), String.valueOf(R.raw.leopard), "Leopard", R.raw.leopardnoise + ""));
            animalCategory.addThing(new Thing(BitmapFactory.decodeResource(context.getResources(), R.drawable.lion), String.valueOf(R.raw.lion), "Lion", R.raw.lionnoise + ""));
            animalCategory.addThing(new Thing(BitmapFactory.decodeResource(context.getResources(), R.drawable.monkey), String.valueOf(R.raw.monkey), "Monkey", R.raw.monkeynoise + ""));
            animalCategory.addThing(new Thing(BitmapFactory.decodeResource(context.getResources(), R.drawable.penguin), String.valueOf(R.raw.penguin), "Penguin", R.raw.penguinnoise + ""));
            animalCategory.addThing(new Thing(BitmapFactory.decodeResource(context.getResources(), R.drawable.rooster), String.valueOf(R.raw.rooster), "Rooster", R.raw.roosternoise + ""));
            animalCategory.addThing(new Thing(BitmapFactory.decodeResource(context.getResources(), R.drawable.sheep), String.valueOf(R.raw.sheep), "Sheep", R.raw.sheepnoise + ""));
            animalCategory.addThing(new Thing(BitmapFactory.decodeResource(context.getResources(), R.drawable.snake), String.valueOf(R.raw.snake), "Snake", R.raw.snakenoise + ""));
            animalCategory.addThing(new Thing(BitmapFactory.decodeResource(context.getResources(), R.drawable.tiger), String.valueOf(R.raw.tiger), "Tiger", R.raw.tigernoise + ""));
            animalCategory.addThing(new Thing(BitmapFactory.decodeResource(context.getResources(), R.drawable.fox), String.valueOf(R.raw.fox), "Fox", R.raw.foxnoise + ""));
            animalCategory.addThing(new Thing(BitmapFactory.decodeResource(context.getResources(), R.drawable.camel), String.valueOf(R.raw.camel), "Camel", R.raw.camelnoise + ""));

            addCategory(animalCategory);

//            foodCategory.addThing(new Thing(R.drawable.bread, R.raw.bread, "Bread"));
//            foodCategory.addThing(new Thing(R.drawable.burger, R.raw.burger, "Burger"));
//            foodCategory.addThing(new Thing(R.drawable.cheese, R.raw.cheese, "Cheese"));
//            foodCategory.addThing(new Thing(R.drawable.chocolate, R.raw.chocolate, "Chocolate"));
//            foodCategory.addThing(new Thing(R.drawable.coffee, R.raw.coffee, "Coffee"));
//            foodCategory.addThing(new Thing(R.drawable.egg, R.raw.egg, "Egg"));
//            foodCategory.addThing(new Thing(R.drawable.honey, R.raw.honey, "Honey"));
//            foodCategory.addThing(new Thing(R.drawable.hotdog, R.raw.hotdog, "Hot Dog"));
//            foodCategory.addThing(new Thing(R.drawable.icecream, R.raw.icecream, "Ice Cream"));
//            foodCategory.addThing(new Thing(R.drawable.meat, R.raw.meat, "Meat"));
//            foodCategory.addThing(new Thing(R.drawable.pizza, R.raw.pizza, "Pizza"));
//            foodCategory.addThing(new Thing(R.drawable.sandwich, R.raw.sandwich, "Sandwich"));
//            foodCategory.addThing(new Thing(R.drawable.sausage, R.raw.sausage, "Sausage"));
//            foodCategory.addThing(new Thing(R.drawable.water, R.raw.water, "Water"));
//
//            colorsCategory.addThing(new Thing(R.drawable.blue, R.raw.blue, "Blue"));
//            colorsCategory.addThing(new Thing(R.drawable.pink, R.raw.pink, "Pink"));
//            colorsCategory.addThing(new Thing(R.drawable.green, R.raw.green, "Green"));
//            colorsCategory.addThing(new Thing(R.drawable.orange_color, R.raw.orange_color, "Orange"));
//            colorsCategory.addThing(new Thing(R.drawable.purple, R.raw.purple, "Purple"));
//            colorsCategory.addThing(new Thing(R.drawable.red, R.raw.red, "Red"));
//            colorsCategory.addThing(new Thing(R.drawable.yellow, R.raw.yellow, "Yellow"));
//            colorsCategory.addThing(new Thing(R.drawable.brown, R.raw.brown, "Brown"));
//            colorsCategory.addThing(new Thing(R.drawable.gray, R.raw.gray, "Gray"));
//            colorsCategory.addThing(new Thing(R.drawable.white, R.raw.white, "White"));
//            colorsCategory.addThing(new Thing(R.drawable.black, R.raw.black, "Black"));

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

        //values.put(COLUMN_IMAGE, category.image);
        values.put(COLUMN_IMAGE, BitmapToByte(category.image));


        values.put(COLUMN_HIGHSCORE, category.highScore);
        values.put(COLUMN_COLOR, category.color);
        values.put(COLUMN_THEME, category.theme);
        // insert 1 row to table
        db.insert(TABLE_CATEGORY, null, values);
        //db.close();

        //db = getWritableDatabase();
        int lastCategoryId = getLastId(TABLE_CATEGORY);

        for (Thing i : category.things) {
            ContentValues values1 = new ContentValues();
            values1.put(COLUMN_TEXT, i.getText());
            values1.put(COLUMN_IMAGE, BitmapToByte(i.getImage()));
            values1.put(COLUMN_SOUND, i.getNoise());
            values1.put(COLUMN_NOISE, i.getSound());
            values1.put(COLUMN_CATEGORY_ID, lastCategoryId);
            db.insert(TABLE_THING, null, values1);
        }

        // close connection
        db.close();
    }

    private int getLastId(String table) {
        String countQuery = "SELECT max(_id) FROM " + table;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        if (cursor != null)
            cursor.moveToFirst();
        int id = Integer.parseInt(cursor.getString(0));

        //db.close();
        return id;
    }

    public int updateCategory(Category category) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, category.title);
        values.put(COLUMN_IMAGE, BitmapToByte(category.image));
        values.put(COLUMN_HIGHSCORE, category.highScore);
        values.put(COLUMN_COLOR, category.color);
        values.put(COLUMN_THEME, category.theme);
        // updating row
        int update1 = db.update(TABLE_CATEGORY, values, COLUMN_ID + " = ?",
                new String[]{String.valueOf(category.id)});
        for (Thing i : category.things) {
            ContentValues values1 = new ContentValues();
            values.put(COLUMN_TEXT, i.getText());
            values.put(COLUMN_IMAGE, BitmapToByte(i.getImage()));
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

    public ArrayList<Category> getAllCategory() {
        ArrayList<Category> categoryList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_CATEGORY;
        ArrayList<Integer> idList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // Browse on the cursor, and add to the list.
        if (cursor.moveToFirst()) {
            do {
                int id = Integer.parseInt(cursor.getString(0));
                idList.add(id);
            } while (cursor.moveToNext());
        }
        for (int i : idList) {
            categoryList.add(getCategory(i));
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
            //category.image = cursor.getString(2);
            category.image = ByteToBitmap(cursor.getBlob(2));

            category.highScore = Integer.parseInt(cursor.getString(3));
            category.color = Integer.parseInt(cursor.getString(4));
            category.theme = Integer.parseInt(cursor.getString(5));

            Cursor cursor1 = db.query(TABLE_THING,
                    new String[]{COLUMN_ID, COLUMN_TEXT, COLUMN_IMAGE, COLUMN_SOUND, COLUMN_NOISE, COLUMN_CATEGORY_ID},
                    COLUMN_CATEGORY_ID + " = ?",
                    new String[]{String.valueOf(id)}, null, null, null, null);
            if (cursor1 != null)
                cursor1.moveToFirst();
            ArrayList<Thing> things = new ArrayList<>();
            do {
                Thing thing = new Thing();

                thing.setId(Integer.parseInt(cursor1.getString(0)));
                thing.setText(cursor1.getString(1));
                //thing.setImage(cursor.getString(2));
                thing.setImage(ByteToBitmap(cursor1.getBlob(2)));

                thing.setSound(cursor1.getString(3));
                thing.setNoise(cursor1.getString(4));
                thing.setCategoryId(Integer.parseInt(cursor1.getString(5)));
                // add to list
                things.add(thing);
            } while (cursor1.moveToNext());
            category.things = things;
        }

        // return note
        return category;
    }

    public Category getCategory(String title) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CATEGORY, new String[]{COLUMN_ID,
                        COLUMN_TITLE, COLUMN_IMAGE, COLUMN_HIGHSCORE, COLUMN_COLOR, COLUMN_THEME},
                COLUMN_TITLE + "=?",
                new String[]{title}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Category category = new Category();
        if (cursor != null) {
            category.id = Integer.parseInt(cursor.getString(0));
            category.title = cursor.getString(1);
            //category.image = cursor.getString(2);
            category.image = ByteToBitmap(cursor.getBlob(2));

            category.highScore = Integer.parseInt(cursor.getString(3));
            category.color = Integer.parseInt(cursor.getString(4));
            category.theme = Integer.parseInt(cursor.getString(5));

            Cursor cursor1 = db.query(TABLE_THING,
                    new String[]{COLUMN_ID, COLUMN_TEXT, COLUMN_IMAGE, COLUMN_SOUND, COLUMN_NOISE, COLUMN_CATEGORY_ID},
                    COLUMN_CATEGORY_ID + " = ?",
                    new String[]{String.valueOf(category.id)}, null, null, null, null);
            if (cursor1 != null)
                cursor1.moveToFirst();
            ArrayList<Thing> things = new ArrayList<>();
            do {
                Thing thing = new Thing();

                thing.setId(Integer.parseInt(cursor1.getString(0)));
                thing.setText(cursor1.getString(1));
                //thing.setImage(cursor.getString(2));
                thing.setImage(ByteToBitmap(cursor1.getBlob(2)));

                thing.setSound(cursor1.getString(3));
                thing.setNoise(cursor1.getString(4));
                thing.setCategoryId(Integer.parseInt(cursor1.getString(5)));
                // add to list
                things.add(thing);
            } while (cursor1.moveToNext());
            category.things = things;
        }

        // return note
        return category;
    }


    public int getHighScore(String categoryTitle) {
        Category category = getCategory(categoryTitle);
        return category.highScore;
    }

    public void updateHighScore(String categoryTitle, int highscore) {
        Category category = getCategory(categoryTitle);
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_HIGHSCORE, highscore);
        // updating row
        db.update(TABLE_CATEGORY, values, COLUMN_ID + " = ?",
                new String[]{String.valueOf(category.id)});
    }

    public void setHighScore(String categoryTitle, int highScore) {
        Category category = getCategory(categoryTitle);
        category.highScore = highScore;
        updateCategory(category);
    }
}

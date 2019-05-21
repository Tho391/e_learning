package com.example.dardan.elearning;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class CategoriesActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, EasyPermissions.PermissionCallbacks, AdapterView.OnItemLongClickListener {
    public static ArrayList<Category> categories;
    CustomCategoryAdapter adapter;
    ListView listView;
    MySQLiteHelper db;
    private static final String SHAREPREFERENCES_NAME = "firstTime";
    boolean firstTime = true;

    private static final int REQUEST_CODE = 121;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_list);

        if (Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.LOLLIPOP) {
            String[] perms = {
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
            };
            if (!EasyPermissions.hasPermissions(this, perms)) {
                EasyPermissions.requestPermissions(this, "All permissions are required in oder to run this application", REQUEST_CODE, perms);
            }
        }


        //kiểm tra có phải lần đầu mở app k
//        SharedPreferences sharedPreferences= this.getSharedPreferences(SHAREPREFERENCES_NAME, Context.MODE_PRIVATE);
//        if (sharedPreferences!=null){
//            firstTime = true;
//        }
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putBoolean(SHAREPREFERENCES_NAME,firstTime);
//        editor.apply();

        categories = new ArrayList<>();

        //thay populate => createAdapter
        //populateCategoriesList();

        db = new MySQLiteHelper(this);
        //db.createDefaultCategory();
        categories = db.getAllCategory();
        createAdapter();
    }

    //todo sửa lại adapter
    private void createAdapter() {
        listView = findViewById(R.id.listViewCards);
        listView.setOnItemClickListener(this);
        listView.setLongClickable(true);
        listView.setOnItemLongClickListener(this);
        if (categories != null) {
            // Create the adapter to convert the array to views
            adapter = new CustomCategoryAdapter(this, categories);
            // Attach the adapter to a ListView

            listView.setAdapter(adapter);

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateCategory();
    }

    @Override
    protected void onResume() {


        super.onResume();
        //hàm này là update high score cho mỗi category
        //updateHighscores();
        updateCategory();

    }

    private void updateCategory() {
        ArrayList<Category> list = db.getAllCategory();
        if (categories != null) {
            categories.clear();
            if (list != null)
                categories.addAll(list);
            if (categories != null)
                adapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_quiz, menu);

        //add item to option menu
//        try{
//        for (Category c: categories) {
//            menu.getItem(1).getSubMenu().add(c.title);
//        }
//        }catch (Exception e){
//            e.getMessage();
//        }

        MenuItem menuItem = menu.getItem(2);
        SubMenu quizMenu = menuItem.getSubMenu();
        quizMenu.clear();
        //SubMenu quizMenu = menu.addSubMenu("Quiz");
        if (categories != null)
            for (Category c : categories) {
                quizMenu.addSubMenu(0, c.id, c.id, c.title);
            }


        return true;
    }

    //todo set value thành id cho category
    //todo sửa menu thành list
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(this, QuizActivity.class);

        if (item.getItemId() == R.id.add_default) {
            //createDefaultData();
            if (db.getCategoryCount() < 1)
                Toast.makeText(this, "Creating...", Toast.LENGTH_LONG).show();
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    createDefaultData();
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    //super.onPostExecute(aVoid);
                    //categories= db.getAllCategory();
                    toast("Done");
                    updateCategory();
                }
            }.execute();
        } else if (item.getItemId() == R.id.add_category) {
            Intent intent1 = new Intent(this, AddCategoryActivity.class);
            startActivity(intent1);
            return true;
        } else
            for (Category c : categories) {
                if (item.getItemId() == c.id) {
                    intent.putExtra("position", categories.indexOf(c));
                    startActivity(intent);
                    return true;
                }
            }


        //lấy title của menu => id của category => truyền sang activity quiz
//        Category category = db.getCategory(item.getTitle().toString());
//        int id = category.id;
//        intent.putExtra("position",id);
        return true;
    }

    private void createDefaultData() {
        db.createDefaultCategory();
    }

    private void populateCategoriesList() {

//        Highscores.open(this);
//        Category fruitCategory = new Category("Fruits",
//                R.drawable.fruits,
//                Highscores.getHighscore(MySQLiteHelper.COLUMN_FRUITS),
//                getResources().getColor(R.color.primary_dark),
//                R.style.GreenTheme,
//                MySQLiteHelper.COLUMN_FRUITS,
//                R.drawable.ic_face_green);
//        Category animalCategory = new Category("Animals",
//                R.drawable.animals,
//                Highscores.getHighscore(MySQLiteHelper.COLUMN_ANIMALS),
//                getResources().getColor(R.color.blue_primary_dark),
//                R.style.BlueTheme,
//                MySQLiteHelper.COLUMN_ANIMALS,
//                R.drawable.ic_face_blue);
//        Category foodCategory = new Category("Food",
//                R.drawable.food,
//                Highscores.getHighscore(MySQLiteHelper.COLUMN_FOOD),
//                getResources().getColor(R.color.pink_primary_dark),
//                R.style.PinkTheme,
//                MySQLiteHelper.COLUMN_FOOD,
//                R.drawable.ic_face_pink);
//        Category colorsCategory = new Category("Colors",
//                R.drawable.colors,
//                Highscores.getHighscore(MySQLiteHelper.COLUMN_COLOR),
//                getResources().getColor(R.color.purple_primary_dark),
//                R.style.PurpleTheme,
//                MySQLiteHelper.COLUMN_COLOR,
//                R.drawable.ic_face_purple);
//        Highscores.close();
//
//        fruitCategory.addThing(new Thing(R.drawable.apple, R.raw.apple, "Apple"));
//        fruitCategory.addThing(new Thing(R.drawable.orange, R.raw.orange, "Orange"));
//        fruitCategory.addThing(new Thing(R.drawable.banana, R.raw.banana, "Banana"));
//        fruitCategory.addThing(new Thing(R.drawable.cherry, R.raw.cherry, "Cherry"));
//        fruitCategory.addThing(new Thing(R.drawable.dates, R.raw.dates, "Dates"));
//        fruitCategory.addThing(new Thing(R.drawable.coconut, R.raw.coconut, "Coconut"));
//        fruitCategory.addThing(new Thing(R.drawable.grape, R.raw.grape, "Grape"));
//        fruitCategory.addThing(new Thing(R.drawable.kiwi, R.raw.kiwi, "Kiwi"));
//        fruitCategory.addThing(new Thing(R.drawable.lemon, R.raw.lemon, "Lemon"));
//        fruitCategory.addThing(new Thing(R.drawable.peach, R.raw.peach, "Peach"));
//        fruitCategory.addThing(new Thing(R.drawable.pear, R.raw.pear, "Pear"));
//        fruitCategory.addThing(new Thing(R.drawable.persimmon, R.raw.persimmon, "Persimmon"));
//        fruitCategory.addThing(new Thing(R.drawable.pineapple, R.raw.pineapple, "Pineapple"));
//        fruitCategory.addThing(new Thing(R.drawable.plum, R.raw.plum, "Plum"));
//        fruitCategory.addThing(new Thing(R.drawable.raspberry, R.raw.raspberry, "Raspberry"));
//        fruitCategory.addThing(new Thing(R.drawable.strawberry, R.raw.strawberry, "Strawberry"));
//        fruitCategory.addThing(new Thing(R.drawable.watermelon, R.raw.watermelon, "Watermelon"));
//        fruitCategory.addThing(new Thing(R.drawable.mango, R.raw.mango, "Mango"));
//
//        animalCategory.addThing(new Thing(R.drawable.dog, R.raw.dog, "Dog", R.raw.dognoise));
//        animalCategory.addThing(new Thing(R.drawable.bear, R.raw.bear, "Bear", R.raw.bearnoise));
//        animalCategory.addThing(new Thing(R.drawable.wolf, R.raw.wolf, "Wolf", R.raw.wolfnoise));
//        animalCategory.addThing(new Thing(R.drawable.dolphin, R.raw.dolphin, "Dolphin", R.raw.dolphinnoise));
//        animalCategory.addThing(new Thing(R.drawable.duck, R.raw.duck, "Duck", R.raw.ducknoise));
//        animalCategory.addThing(new Thing(R.drawable.leopard, R.raw.leopard, "Leopard", R.raw.leopardnoise));
//        animalCategory.addThing(new Thing(R.drawable.lion, R.raw.lion, "Lion", R.raw.lionnoise));
//        animalCategory.addThing(new Thing(R.drawable.monkey, R.raw.monkey, "Monkey", R.raw.monkeynoise));
//        animalCategory.addThing(new Thing(R.drawable.penguin, R.raw.penguin, "Penguin", R.raw.penguinnoise));
//        animalCategory.addThing(new Thing(R.drawable.rooster, R.raw.rooster, "Rooster", R.raw.roosternoise));
//        animalCategory.addThing(new Thing(R.drawable.sheep, R.raw.sheep, "Sheep", R.raw.sheepnoise));
//        animalCategory.addThing(new Thing(R.drawable.snake, R.raw.snake, "Snake", R.raw.snakenoise));
//        animalCategory.addThing(new Thing(R.drawable.tiger, R.raw.tiger, "Tiger", R.raw.tigernoise));
//        animalCategory.addThing(new Thing(R.drawable.fox, R.raw.fox, "Fox", R.raw.foxnoise));
//        animalCategory.addThing(new Thing(R.drawable.camel, R.raw.camel, "Camel", R.raw.camelnoise));
//
//        foodCategory.addThing(new Thing(R.drawable.bread, R.raw.bread, "Bread"));
//        foodCategory.addThing(new Thing(R.drawable.burger, R.raw.burger, "Burger"));
//        foodCategory.addThing(new Thing(R.drawable.cheese, R.raw.cheese, "Cheese"));
//        foodCategory.addThing(new Thing(R.drawable.chocolate, R.raw.chocolate, "Chocolate"));
//        foodCategory.addThing(new Thing(R.drawable.coffee, R.raw.coffee, "Coffee"));
//        foodCategory.addThing(new Thing(R.drawable.egg, R.raw.egg, "Egg"));
//        foodCategory.addThing(new Thing(R.drawable.honey, R.raw.honey, "Honey"));
//        foodCategory.addThing(new Thing(R.drawable.hotdog, R.raw.hotdog, "Hot Dog"));
//        foodCategory.addThing(new Thing(R.drawable.icecream, R.raw.icecream, "Ice Cream"));
//        foodCategory.addThing(new Thing(R.drawable.meat, R.raw.meat, "Meat"));
//        foodCategory.addThing(new Thing(R.drawable.pizza, R.raw.pizza, "Pizza"));
//        foodCategory.addThing(new Thing(R.drawable.sandwich, R.raw.sandwich, "Sandwich"));
//        foodCategory.addThing(new Thing(R.drawable.sausage, R.raw.sausage, "Sausage"));
//        foodCategory.addThing(new Thing(R.drawable.water, R.raw.water, "Water"));
//
//        colorsCategory.addThing(new Thing(R.drawable.blue, R.raw.blue, "Blue"));
//        colorsCategory.addThing(new Thing(R.drawable.pink, R.raw.pink, "Pink"));
//        colorsCategory.addThing(new Thing(R.drawable.green, R.raw.green, "Green"));
//        colorsCategory.addThing(new Thing(R.drawable.orange_color, R.raw.orange_color, "Orange"));
//        colorsCategory.addThing(new Thing(R.drawable.purple, R.raw.purple, "Purple"));
//        colorsCategory.addThing(new Thing(R.drawable.red, R.raw.red, "Red"));
//        colorsCategory.addThing(new Thing(R.drawable.yellow, R.raw.yellow, "Yellow"));
//        colorsCategory.addThing(new Thing(R.drawable.brown, R.raw.brown, "Brown"));
//        colorsCategory.addThing(new Thing(R.drawable.gray, R.raw.gray, "Gray"));
//        colorsCategory.addThing(new Thing(R.drawable.white, R.raw.white, "White"));
//        colorsCategory.addThing(new Thing(R.drawable.black, R.raw.black, "Black"));
//
//        categories.add(fruitCategory);
//        categories.add(animalCategory);
//        categories.add(foodCategory);
//        categories.add(colorsCategory);

        // Create the adapter to convert the array to views
        adapter = new CustomCategoryAdapter(this, categories);
        // Attach the adapter to a ListView
        listView = (ListView) findViewById(R.id.listViewCards);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }

    private void updateHighscores() {
        Highscores.open(this);
        for (Category c : categories) {
            c.updateHighscore();
        }
        Highscores.close();
        // notifies the adapter to display the latest Highscores
        // actually this method calls getView from CustomCategoryAdapter class
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, ThingsActivity.class);
        intent.putExtra("position", position);
        startActivity(intent);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        // if permissions have been granted, or has been passed, take an action here,
        // perform your action here
        // this method will be called as soon as permissions have been granted
        // you can add a toast message here, to see what happens
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            // if some permissions has been denied, show a settings dialog
            // that will take user to the application permissions setting secreen
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.requestPermissions(this, "All permissions are required to run this application", requestCode, permissions);

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        //return false;
        Log.v("long clicked", "pos: " + position);

        createDialog(position);

        return true;
    }

    private void createDialog(final int positionCategory) {
        String[] items = new String[]{"Edit", "Delete"};
        AlertDialog singleChoice = new AlertDialog.Builder(this)
                .setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        /* User clicked on a radio button do some stuff */
                        dialog.dismiss();
                        int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                        switch (selectedPosition) {
                            case 0:
                                editCategory(positionCategory);
                                break;
                            case 1:
                                deleteCategory(positionCategory);
                                break;
                        }
                    }
                }).create();
        singleChoice.show();
    }

    private void deleteCategory(final int selectedPosition) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("Are you sure to cancel creating a new category?");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        //finish();
                        deleteCate(selectedPosition);
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();


    }

    private void deleteCate(int selectedPosition) {
        final Category category = categories.get(selectedPosition);

        //db.deleteCategory(category);
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                db.deleteCategory(category);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                //super.onPostExecute(aVoid);
                toast("Deleted");
            }
        }.execute();
    }

    private void toast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
        updateCategory();
    }

    private void editCategory(int positionCategory) {
        Toast.makeText(this, "Edit", Toast.LENGTH_LONG).show();
    }

}

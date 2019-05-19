package com.example.dardan.elearning;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import static com.example.dardan.elearning.AddCategoryActivity.CATEGORY;
import static com.example.dardan.elearning.Ulti.getDataFromSharePreferences;

public class AddThingsActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageButton rightButton;
    private ImageButton leftButton;
    private ImageView thingImage;
    private TextView thingName;
    private ImageButton audioButton;
    private ImageButton addButton;
    private int currenIndex = 0;
    private Thing currentThing;
    private Category category;
    private ArrayList<String> thingCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_things);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Category");

        findView();
        //get category from intent
        getCategory();

    }

    private void getCategory() {
//        Intent intent = getIntent();
//        category = (Category) intent.getSerializableExtra(CATEGORY);
        category = getDataFromSharePreferences(this, CATEGORY);
    }

    private void findView() {
        thingName = findViewById(R.id.thingName);
        thingImage = findViewById(R.id.thingImage);

        rightButton = findViewById(R.id.buttonRightThing);
        leftButton = findViewById(R.id.buttonLeftThing);
        audioButton = findViewById(R.id.buttonAudioThing);
        addButton = findViewById(R.id.buttonAdd);


        rightButton.setOnClickListener(this);
        leftButton.setOnClickListener(this);
        audioButton.setOnClickListener(this);
        thingImage.setOnClickListener(this);
        addButton.setOnClickListener(this);
    }

    @Override
    public boolean onSupportNavigateUp() {
        // closes the Activity when the back button on the action bar is pressed
        finish();
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonLeftThing:
                //chuyển đến thing trước đó
                if (category.prevThing() != null) {
                    currentThing = category.prevThing();
                }
                break;
            case R.id.buttonRightThing:
                if (category.nextThing() != null)
                    currentThing = category.nextThing();
                break;
            case R.id.buttonAudioThing:
                //todo lấy link âm thanh
                //playSound(currentThing.getSound());
                break;
            case R.id.thingImage:
                //todo gọi library để add hình
                break;
            case R.id.buttonAdd: {
                //todo new 1 thing mới
                //check user đã điền answer chưa
                String name = thingName.getText().toString();
                if (TextUtils.isEmpty(name)) {
                    thingName.setError("Please fill in the answer for this image!");
                } else {
                    //save currentThing
                    saveThing();
                    //reset title & image to default
                    resetThingActivity();
                }
            }
            break;
        }
    }

    private void saveThing() {
        String thingName = this.thingName.getText().toString();
        Bitmap thingImage = ((BitmapDrawable) this.thingImage.getDrawable()).getBitmap();
        Thing thing = new Thing(thingName, thingImage);
        currentThing = thing;
        category.things.add(thing);
    }

    private void resetThingActivity() {
        thingName.setText("");
        thingImage.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.add_image));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_add_things, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_category:{
                //todo add things to category & save category
            }

                return true;
        }
        return true;
    }

}

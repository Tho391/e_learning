package com.example.dardan.elearning;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import static com.example.dardan.elearning.Ulti.putToSharePreferences;

public class AddCategoryActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edtTitle;
    private TextView btnNext;
    private ImageButton btnImage;
    private Category newCategory;

    AlertDialog alertDialog;

    public static final String CATEGORY = "category";
    public static final int NEW_CATEGORY_CODE = 1111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Category");
        findView();
        //create default category
        newCategory = new Category(this);

        //create alert dialog
        createDialog();

    }

    private void findView() {
        edtTitle = findViewById(R.id.thingName);
        btnNext = findViewById(R.id.button_next);
        btnImage = findViewById(R.id.thingImage);

        btnImage.setOnClickListener(this);
        btnNext.setOnClickListener(this);
    }

    private void createDialog() {
        alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("Are you sure to cancel creating a new category?");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
    }

    @Override
    public boolean onSupportNavigateUp() {
        // closes the Activity when the back button on the action bar is pressed
        alertDialog.show();
        return true;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_next: {
                //start add thing activity
                Intent intent = new Intent(this, AddThingsActivity.class);
                //intent.putExtra(CATEGORY, newCategory);
                //put to share preference
                //SharedPreferences mPrefs = getSharedPreferences(SHARE_PREFERENCES,MODE_PRIVATE);
                putToSharePreferences(this, CATEGORY, newCategory);
                startActivity(intent);
            }
            break;
            case R.id.thingImage: {
                //choose photo from gallery
            }
            break;
        }
    }
}

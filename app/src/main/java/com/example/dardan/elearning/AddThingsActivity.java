package com.example.dardan.elearning;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import static com.example.dardan.elearning.AddCategoryActivity.CATEGORY;
import static com.example.dardan.elearning.AddCategoryActivity.RESULT_LOAD_CAMERA;
import static com.example.dardan.elearning.AddCategoryActivity.RESULT_LOAD_IMG;
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
    MySQLiteHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_things);

        db = new MySQLiteHelper(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Add Objects");

        findView();
        //get category from intent
        getCategory();

    }

    private void getCategory() {
//        Intent intent = getIntent();
//        category = (Category) intent.getSerializableExtra(CATEGORY);
        category = new Category(this);
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
                //choose photo from gallery
                getImageFromLibraryOrCamera();
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
                    Toast.makeText(this, thingName.getText() + " added", Toast.LENGTH_LONG).show();
                    //reset title & image to default_cate
                    resetThingActivity();
                }
            }
            break;
        }
    }

    private void getImageFromLibraryOrCamera() {
        //todo dung dialog

        String[] items = new String[]{"From Library", "From Camera"};
        AlertDialog singleChoice = new AlertDialog.Builder(this)
                .setTitle("Where you want to get picture")
                .setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        /* User clicked on a radio button do some stuff */
                        dialog.dismiss();
                        int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                        switch (selectedPosition) {
                            case 0:
                                getImageFromLibrary();
                                break;
                            case 1:
                                getImageFromCamera();
                                break;
                        }
                    }
                }).create();
        singleChoice.show();
    }

    private void getImageFromCamera() {
        //camera
        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePicture, RESULT_LOAD_CAMERA);
    }

    private void getImageFromLibrary() {
        //library
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
    }

    private void saveThing() {
        String thingName = this.thingName.getText().toString();
        Bitmap thingImage = ((BitmapDrawable) this.thingImage.getDrawable()).getBitmap();
        Thing thing = new Thing(thingName, thingImage);
        //currentThing = thing;
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_category: {
                if (category.things.size() > 0) {
                    //todo save category

                    db.addCategory(category);
                    //set result code
                    Intent returnIntent = new Intent();
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();

                    //deleteSharedPreferences(CATEGORY);
                    Toast.makeText(this, category.title + " category added", Toast.LENGTH_LONG).show();
                } else
                    Toast.makeText(this, "You have not add any objects!", Toast.LENGTH_LONG).show();
            }
            break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RESULT_LOAD_IMG: {
                if (resultCode == Activity.RESULT_OK) {
                    try {
                        Uri imageUri = data.getData();
                        InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

                        thingImage.setImageBitmap(selectedImage);

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }
            }
            break;
            case RESULT_LOAD_CAMERA: {
                if (resultCode == Activity.RESULT_OK) {
                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    thingImage.setImageBitmap(photo);
                }
            }
            break;
        }
    }
}

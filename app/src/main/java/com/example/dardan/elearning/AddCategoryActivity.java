package com.example.dardan.elearning;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;

import static com.example.dardan.elearning.Ulti.putToSharePreferences;

public class AddCategoryActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int CLOSE_CODE = 1006;
    public static final int RESULT_LOAD_IMG = 1007;
    public static final int RESULT_LOAD_CAMERA = 1008;
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
        setTitle("Add Category");
        findView();
        //create default_cate category
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
                //check user đã điền title chưa
                String name = edtTitle.getText().toString();
                if (TextUtils.isEmpty(name)) {
                    edtTitle.setError("Please fill in the title for this category!");
                } else {
                    //add title & image to category
                    newCategory.title = name;
                    Bitmap bitmap = ((BitmapDrawable) btnImage.getDrawable()).getBitmap();
                    newCategory.image = bitmap;

                    //start add thing activity
                    Intent intent = new Intent(this, AddThingsActivity.class);
                    //intent.putExtra(CATEGORY, newCategory);
                    //put to share preference
                    //SharedPreferences mPrefs = getSharedPreferences(SHARE_PREFERENCES,MODE_PRIVATE);
                    putToSharePreferences(this, CATEGORY, newCategory);
                    //startActivity(intent);
                    startActivityForResult(intent, CLOSE_CODE);
                }
            }
            break;
            case R.id.thingImage: {
                //choose photo from gallery
                getImageFromLibraryOrCamera();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CLOSE_CODE: {
                if (resultCode == Activity.RESULT_OK) {
                    finish();
                }
            }
            break;
            case RESULT_LOAD_IMG: {
                if (resultCode == Activity.RESULT_OK) {
                    try {
                        Uri imageUri = data.getData();
                        InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

                        btnImage.setImageBitmap(selectedImage);

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
                    btnImage.setImageBitmap(photo);
                }
            }
            break;
        }
    }
}

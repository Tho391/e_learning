package com.example.dardan.elearning;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Locale;
import java.util.UUID;

public class ThingsActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageButton rightButton;
    private ImageButton leftButton;
    private ImageView quizButton;
    private ImageView mainPicture;
    private TextView mainName;
    private ImageButton audioButton;
    private RelativeLayout relativeLayout;
    private MediaPlayer mediaPlayer;
    private Thing currentThing;
    private Category currentCategory;
    private int position;
    TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();//return the intent that started this activity
        position = intent.getIntExtra("position", 0);
        currentCategory = CategoriesActivity.categories.get(position);
        currentCategory.currentIndex = 0;
        //currentCategory.goToFirstThing();
        currentThing = currentCategory.currentThing();
        setTheme(currentCategory.theme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_things);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mainName = findViewById(R.id.thingName);
        mainPicture = findViewById(R.id.thingImage);
        rightButton = findViewById(R.id.buttonRightThing);
        leftButton = findViewById(R.id.buttonLeftThing);
        audioButton = findViewById(R.id.buttonAudioThing);
        relativeLayout = findViewById(R.id.thingLayout);
        quizButton = findViewById(R.id.buttonQuiz);

        rightButton.setOnClickListener(this);
        leftButton.setOnClickListener(this);
        audioButton.setOnClickListener(this);
        mainPicture.setOnClickListener(this);
        quizButton.setOnClickListener(this);


        updateResources();


        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                tts.setLanguage(Locale.UK);
            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        // it is considered good practice to release the Media Player object
        // when the activity is stopped
        releaseMediaPlayer();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // it is considered good practice to release the Media Player object
        // when the activity is paused
        releaseMediaPlayer();
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
                if (currentCategory.prevThing() != null) {
                    currentThing = currentCategory.prevThing();
                    updateResources();
                }
                break;
            case R.id.buttonRightThing:
                if (currentCategory.nextThing() != null) {
                    currentThing = currentCategory.nextThing();
                    updateResources();
                }
                break;
            case R.id.buttonAudioThing: {
                //playSound(currentThing.getSound());

                playSound();

            }
            break;
            case R.id.thingImage:
                if (currentThing.hasNoise()) {
                    playSound(currentThing.getNoise());
                }
                break;
            case R.id.buttonQuiz:
                //Intent previousIntent = getIntent();
                //int position = previousIntent.getIntExtra("position", 0);
                Intent intent = new Intent(this, QuizActivity.class);
                intent.putExtra("position", position);
                startActivity(intent);
                break;
        }
    }

    private void playSound() {
        String toSpeak = mainName.getText().toString();
        String utteranceId = UUID.randomUUID().toString();
        tts.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null, utteranceId);
    }

    /**
     * Plays the appropriate sound/noise for the current thing and
     * updates the UI (button color & text, background color etc.) based on the
     * current Category and Thing
     */
    protected void updateResources() {
        rightButton.setVisibility(currentCategory.hasNextThing() ? View.VISIBLE : View.INVISIBLE);
        leftButton.setVisibility(currentCategory.hasPrevThing() ? View.VISIBLE : View.INVISIBLE);

        try {
            if (currentThing.hasNoise()) {
                playSound(currentThing.getNoise());
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    public void onCompletion(MediaPlayer player) {
                        player.reset();
                        playSound(currentThing.getSound());
                    }
                });
            } else
                playSound(currentThing.getSound());
        } catch (Exception e) {
            e.getMessage();
        }
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = this.getTheme(); //gets the current Theme

        // retrieves the color value from this theme and puts it in the typedValue variable
        theme.resolveAttribute(R.attr.colorAccent, typedValue, true);
        int accentColor = typedValue.data;

        setButtonColor(leftButton, accentColor);
        setButtonColor(rightButton, accentColor);
        setButtonColor(audioButton, accentColor);

        // merr vleren e atributit background - fillimisht duhet te deklarohet ne attrs.xml
        theme.resolveAttribute(R.attr.colorPrimaryLight, typedValue, true);
        int primaryLightColor = typedValue.data;

        mainPicture.setBackgroundColor(primaryLightColor);
        relativeLayout.setBackgroundColor(primaryLightColor);
        mainName.setBackgroundColor(primaryLightColor);
        setTitle(currentCategory.title);

        // make the picture Invisible and then Visible to add some animation
        mainPicture.setVisibility(View.INVISIBLE);
        //mainPicture.setImageResource(currentThing.getImage());
        if (currentThing.getImage() != null)
            mainPicture.setImageBitmap(currentThing.getImage());


        mainPicture.setVisibility(View.VISIBLE);
        quizButton.setImageResource(R.drawable.ic_face_blue);
        mainName.setText(currentThing.getText());


        //playSound();
    }

    private void setButtonColor(ImageButton button, int color) {
        GradientDrawable bgShape = (GradientDrawable) button.getBackground();
        bgShape.setColor(color);
    }

    /**
     * Plays the sound/noise which is passed as an argument.
     * If the media player is in the middle of playing another sound/noise,
     * it stops and resets the player and starts playing the sound.
     *
     * @param sound the sound to be played by the player
     */
    private void playSound(int sound) {
        // if the player is in the middle of playing another sound/noise
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            stopAndResetPlayer();
        }
        mediaPlayer = MediaPlayer.create(this, sound);
        mediaPlayer.start();

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer player) {
                player.reset();
            }
        });
    }

    private void playSound(String path) {
        // if the player is in the middle of playing another sound/noise
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            stopAndResetPlayer();
        }
        String filePath = Environment.getExternalStorageDirectory() + path;
        try {
            mediaPlayer.setDataSource(filePath);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Can not play sound", Toast.LENGTH_LONG).show();
        }


        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer player) {
                player.reset();
            }
        });
    }

    /**
     * Stops and resets the Media Player associated with this Activity
     */
    private void stopAndResetPlayer() {
        mediaPlayer.stop();
        mediaPlayer.reset();
    }

    /**
     * Releases the media player (if is not null) and sets it to null
     */
    private void releaseMediaPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}

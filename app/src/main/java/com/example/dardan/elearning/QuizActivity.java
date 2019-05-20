package com.example.dardan.elearning;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class QuizActivity extends AppCompatActivity implements View.OnClickListener {
    Category currentCategory;
    ArrayList<Thing> things;
    private ImageView mainPicture;
    private RelativeLayout relativeLayout;
    private Thing thingAnswer;
    private TextView questionTextView;
    private TextView scoreTextView;
    private MediaPlayer mediaPlayer;


    private int score = 0;
    private int questionNumber = 1;

    // if this variable is set, the touch events are not processed (the UI is blocked)
    private boolean stopUserInteractions;

    LinearLayout mLnCauTL;
    LinearLayout mLsButton;
    private Button bA, bB, bC, bD, bE, bF, bG, bH, bI, bJ, bK, bL, bM, bN, bO, bP, bQ, bR, bS, bT, bU, bV, bW, bX, bY, bZ;
    private EditText inputText;
    private ArrayList<EditText> etAnswers=new ArrayList<>();
    private ArrayList<Button> btnAnswes=new ArrayList<>();
    private ArrayList<Integer> randRemoveEtAnswers=new ArrayList<>();
    Button btnAdd;
    private int dem=0;
    private String answer;

    MySQLiteHelper mySQLiteHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mySQLiteHelper = new MySQLiteHelper(this);
        Intent intent = getIntent();//return the intent that started this activity
        int position = intent.getIntExtra("position", 0);
        currentCategory = CategoriesActivity.categories.get(position);
        setTheme(currentCategory.theme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // back button

        things = currentCategory.getListOfThings();
        relativeLayout = findViewById(R.id.quizLayout);
        mainPicture = findViewById(R.id.quizImage);

        scoreTextView = findViewById(R.id.scoreCounter);
        questionTextView = findViewById(R.id.questionCounter);


        setComponent();

        updateResources();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // it is considered good practice to release the Media Player object
        // when the activity is stopped
        releaseMediaPlayer();
    }

    @Override
    protected void onResume() {
        super.onResume();
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
        finish();
        return true;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // it won't process the touch events if this variable is set to true
        if (stopUserInteractions) {
            return true;
        } else {
            return super.dispatchTouchEvent(ev);
        }
    }

    protected void updateResources() {

        // if the quiz has just started
        if (questionNumber == 1) {
            scoreTextView.setText("Score: " + 0);
        } else if (questionNumber > 10) {
//            this.finish();
//            Highscores.open(this);
//            if (Highscores.setHighscore(currentCategory.columnName, score))
//                Toast.makeText(this, "New Highscore!", Toast.LENGTH_LONG).show();
//            Highscores.close();

            mySQLiteHelper.updateHighScore(currentCategory.title,score);
            this.finish();

            return;
        }
        questionTextView.setText("Question: " + questionNumber);
        questionNumber++;
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = this.getTheme(); //gets the current Theme

        //merr vleren e atributit background - fillimisht duhet te deklarohet ne attrs.xml
        theme.resolveAttribute(R.attr.colorPrimaryLight, typedValue, true);
        int primaryLightColor = typedValue.data;
        mainPicture.setBackgroundColor(primaryLightColor);
        relativeLayout.setBackgroundColor(primaryLightColor);

        Random r = new Random();
        Set<Integer> set = new HashSet<>();
        while (set.size() < 3) {
            set.add(r.nextInt(things.size()));
        }
        // three random Thing indexes. e.g., [2, 6, 9] which may represent:
        // [Apple, Mango, Pear]
        Integer[] answers = set.toArray(new Integer[set.size()]);
        // indexes [0, 1 , 2] to randomly fetch the Thing indexes:
        // something like: [0-Apple, 1-Mango and 2-Pear]
        ArrayList<Integer> indexes = new ArrayList<>(Arrays.asList(0, 1, 2));
        //a random index to set the picture question:
        // e.g., 1-Mango
        int index = indexes.get(r.nextInt(indexes.size()));
        thingAnswer = things.get(answers[index]);

        mainPicture.setVisibility(View.INVISIBLE);
        mainPicture.setImageBitmap(thingAnswer.getImage());
        mainPicture.setVisibility(View.VISIBLE);

        createEdAnswer(thingAnswer.getText());

    }

    private void setRandomAnswer(RadioButton button, ArrayList<Integer> indexes, Integer[] answers) {
        // params:
        // e.g., indexes = [0, 1, 2]
        // e.g., answers [2, 6, 9]
        Random r = new Random();
        // random index from [0, 1, 2]. e.g., 1-Mango
        int index = indexes.get(r.nextInt(indexes.size()));
        // e.g., remove the index 1 so Mango won't appear two times as answer
        indexes.remove(Integer.valueOf(index));
        // indexes = [0, 2]
        button.setText(things.get(answers[index]).getText());
    }


    @Override
    public void onClick(final View v) {
        if(v instanceof  Button){
            Button btn=(Button) v;
            String letter = (String) ((Button) v).getText();
            dem++;
            int possitonCurrent=randRemoveEtAnswers.get(dem-1);
            etAnswers.get(possitonCurrent).setText(letter);

            if(dem==randRemoveEtAnswers.size()){
                String answerTmp = "";
                for(int i=0;i<etAnswers.size();i++){
                    answerTmp=answerTmp+etAnswers.get(i).getText();
                }
                if(answerTmp.equals(this.answer)){
                    score++;
                    scoreTextView.setText("Score: " + score);
                    playSound(true);
                    Log.e("ThanhCong","ban da tra loi dung");
                }
                else {
                    Log.e("ThanhCong","ban da tra loi sai");
                    playSound(false);
                }

                // block UI so the user is not able to select answer before the new question appears
                stopUserInteractions = true;
                Handler handler = new Handler();
                // wait 2 seconds before going to the next question
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        updateResources();
                        if (v instanceof RadioButton)
                            ((RadioButton) v).setChecked(false);
                        stopUserInteractions = false; // enable UI again after the next question is displayed
                    }
                }, 2000);
//        // lambda expression as a replacement for the Runnable anonymous class
//        handler.postDelayed(() -> updateResources(), 2000);
            }
        }

    }

    /**
     * Plays a random sound effect
     *
     * @param isCorrect true if the answer is correct, false otherwise
     */
    private void playSound(boolean isCorrect) {
        mediaPlayer = MediaPlayer.create(this,
                isCorrect ? randomCorrectSound() : randomWrongSound());
        mediaPlayer.start();

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer player) {
                player.reset();
            }
        });
    }

    /**
     * Populates a list with sound effects for correct answers and chooses
     * one of them randomly
     *
     * @return a randomly chosen sound effect from the list
     */
    private int randomCorrectSound() {
        List<Integer> correctSounds = new ArrayList<>();
        correctSounds.add(R.raw.correct1_good_job);
        correctSounds.add(R.raw.correct2_well_done);
        correctSounds.add(R.raw.correct3_perfect);
        correctSounds.add(R.raw.correct4_amazing);
        correctSounds.add(R.raw.correct5_great);
        Random rand = new Random();

        return correctSounds.get(rand.nextInt(correctSounds.size()));
    }

    /**
     * Populates a list with sound effects for wrong answers and chooses
     * one of them randomly
     *
     * @return a randomly chosen sound effect from the list
     */
    private int randomWrongSound() {
        List<Integer> wrongSounds = new ArrayList<>();
        wrongSounds.add(R.raw.wrong1_oh_no);
        wrongSounds.add(R.raw.wrong2_try_again);
        wrongSounds.add(R.raw.wrong3_wrong);
        wrongSounds.add(R.raw.wrong4_you_need_some_practice);
        Random rand = new Random();

        return wrongSounds.get(rand.nextInt(wrongSounds.size()));
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

    public void setComponent(){
  /*      bA = (Button) findViewById(R.id.bA);
        bB = (Button) findViewById(R.id.bB);
        bC = (Button) findViewById(R.id.bC);
        bD = (Button) findViewById(R.id.bD);
        bE = (Button) findViewById(R.id.bE);
        bF = (Button) findViewById(R.id.bF);
        bG = (Button) findViewById(R.id.bG);
        bH = (Button) findViewById(R.id.bH);
        bI = (Button) findViewById(R.id.bI);
        bJ = (Button) findViewById(R.id.bJ);
        bK = (Button) findViewById(R.id.bK);
        bL = (Button) findViewById(R.id.bL);
        bM = (Button) findViewById(R.id.bM);
        bN = (Button) findViewById(R.id.bN);
        bO = (Button) findViewById(R.id.bO);
        bP = (Button) findViewById(R.id.bP);
        bQ = (Button) findViewById(R.id.bQ);
        bR = (Button) findViewById(R.id.bR);
        bS = (Button) findViewById(R.id.bS);
        bT = (Button) findViewById(R.id.bT);
        bU = (Button) findViewById(R.id.bU);
        bV = (Button) findViewById(R.id.bV);
        bW = (Button) findViewById(R.id.bW);
        bX = (Button) findViewById(R.id.bX);
        bY = (Button) findViewById(R.id.bY);
        bZ = (Button) findViewById(R.id.bZ);*/
/*        bA.setOnClickListener(this);
        bB.setOnClickListener(this);
        bC.setOnClickListener(this);
        bD.setOnClickListener(this);
        bE.setOnClickListener(this);
        bF.setOnClickListener(this);
        bG.setOnClickListener(this);
        bH.setOnClickListener(this);
        bI.setOnClickListener(this);
        bJ.setOnClickListener(this);
        bK.setOnClickListener(this);
        bL.setOnClickListener(this);
        bM.setOnClickListener(this);
        bN.setOnClickListener(this);
        bO.setOnClickListener(this);
        bP.setOnClickListener(this);
        bQ.setOnClickListener(this);
        bR.setOnClickListener(this);
        bS.setOnClickListener(this);
        bT.setOnClickListener(this);
        bU.setOnClickListener(this);
        bV.setOnClickListener(this);
        bW.setOnClickListener(this);
        bX.setOnClickListener(this);
        bY.setOnClickListener(this);
        bZ.setOnClickListener(this);*/

        mLnCauTL= findViewById(R.id.cautl);
        mLsButton= findViewById(R.id.btngroup);


    }

    public void createEdAnswer(String answer){
        this.answer=answer;

        char[] answerSlipt= answer.toCharArray();
        int numberElementRemove = 0;

        do {
            numberElementRemove=(int)(Math.random() * ((answerSlipt.length-2)-2) + 2);
        }while (numberElementRemove>=answerSlipt.length);

        if(mLnCauTL.getChildCount()>0 && etAnswers.size()> 0){
            mLnCauTL.removeAllViews();
            etAnswers.clear();
            randRemoveEtAnswers.clear();
            dem=0;
        }

        randRemoveEtAnswers=new ArrayList<>();
        etAnswers=new ArrayList<>();

        //make random removeelement
        for(int i=0;i<numberElementRemove;i++){
            Boolean isSame=false;
            int tmp=(int )(Math.random() * (answerSlipt.length-1) + 1);
            for(i=0;i<randRemoveEtAnswers.size();i++){
                if(randRemoveEtAnswers.get(i) == tmp){
                    i--;
                    isSame=true;
                    break;
                }
            }

            if(!isSame){
                randRemoveEtAnswers.add(tmp);
            }
        }
        CreateButtonRand(randRemoveEtAnswers,answerSlipt);
        sort(randRemoveEtAnswers);

        EditText etTmp=null;
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        for(int i=0;i<answerSlipt.length;i++){
            boolean isRemoveElement=false;
            etTmp=new EditText(this);
            etTmp.setId(i);
            etTmp.setWidth(70);
            etTmp.setSingleLine(true);
            etTmp.setTypeface(null, Typeface.BOLD);
            etTmp.setTextColor(Color.BLACK);
            etTmp.setEnabled(false);
            for(int j=0;j<randRemoveEtAnswers.size();j++){
                if(i==randRemoveEtAnswers.get(j)){
                    isRemoveElement=true;
                    break;
                }
            }
            if(!isRemoveElement){
                etTmp.setText(answerSlipt[i]+"");
            }
            else{
                etTmp.setText("");
            }
            mLnCauTL.addView(etTmp);
            etAnswers.add(etTmp);
        }
    }

    void sort(ArrayList<Integer> arrayList)
    {
        int n = arrayList.size();

        // One by one move boundary of unsorted subarray
        for (int i = 0; i < n-1; i++)
        {
            // Find the minimum element in unsorted array
            int min_idx = i;
            for (int j = i+1; j < n; j++)
                if (arrayList.get(j) < arrayList.get(min_idx))
                    min_idx = j;

            // Swap the found minimum element with the first
            // element
            int temp = arrayList.get(min_idx);
            arrayList.set(min_idx,arrayList.get(i));
            arrayList.set(i,temp) ;
        }
    }

    @SuppressLint("ResourceAsColor")
    public void CreateButtonRand(ArrayList<Integer> rdRemoveAnswer, char[] answerSlipt){

        mLsButton.removeAllViews();
        ContextThemeWrapper newContext = new ContextThemeWrapper(this, R.style.Widget_AppCompat_Button_Colored);


        char[] radWordForButton = new char[answerSlipt.length];
        int sizeRadWord=answerSlipt.length-rdRemoveAnswer.size();
        Random rand = new Random();
        int tmp=0;
        char wordTmp;

        for(int i=0;i<sizeRadWord;i++){
            int value = rand.nextInt((122-97)+1)+97;
            radWordForButton[i]=(char)value;
        }

        //
        for(int i=0;i<rdRemoveAnswer.size();i++){
            int j=sizeRadWord+i;
            radWordForButton[j]=answerSlipt[rdRemoveAnswer.get(i)];
        }
        //radomButton
        for(int i=0;i<rdRemoveAnswer.size();i++){
            int j=sizeRadWord+i;
            int value=0;
            do{
                value= rand.nextInt(( answerSlipt.length-1)+1);
            }while (value==tmp);

            wordTmp=radWordForButton[value];
            radWordForButton[value]=radWordForButton[j];
            radWordForButton[j]=wordTmp;
        }

        for(int i=0;i<radWordForButton.length;i++){
            Button btn=new Button(newContext);
            btn.setId(i);
            btn.setText(radWordForButton[i]+"");
            btn.setOnClickListener(this);
            mLsButton.addView(btn);
        }

    }

}

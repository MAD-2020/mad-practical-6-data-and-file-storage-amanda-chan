package sg.edu.np.week_6_whackamole_3_0;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

import static java.lang.Integer.parseInt;

public class Main4Activity extends AppCompatActivity {

    /* Hint:
        1. This creates the Whack-A-Mole layout and starts a countdown to ready the user
        2. The game difficulty is based on the selected level
        3. The levels are with the following difficulties.
            a. Level 1 will have a new mole at each 10000ms.
                - i.e. level 1 - 10000ms
                       level 2 - 9000ms
                       level 3 - 8000ms
                       ...
                       level 10 - 1000ms
            b. Each level up will shorten the time to next mole by 100ms with level 10 as 1000 second per mole.
            c. For level 1 ~ 5, there is only 1 mole.
            d. For level 6 ~ 10, there are 2 moles.
            e. Each location of the mole is randomised.
        4. There is an option return to the login page.
     */
    private static final String FILENAME = "Main4Activity.java";
    private static final String TAG = "Whack-A-Mole3.0!";
    CountDownTimer readyTimer;
    CountDownTimer newMolePlaceTimer;

    public String GLOBAL_PREFS = "MyPrefs";
    public String MY_USERNAME= "MyUserNAME";
    SharedPreferences sharedPreferences;

    MyDBHandler dbHandler = new MyDBHandler(this,null,null,1);

    int lvlSelected;
    int score = 0;
    TextView scoreView;
    String username;

    Button setButton;
    Button moleButton;
    Button moleButton2;
    Button buttonListener;
    Button backButton;
    int[] moleTimer = {10000, 9000, 8000, 7000, 6000, 5000, 4000, 3000, 2000, 1000};

    private void readyTimer(){
        /*  HINT:
            The "Get Ready" Timer.
            Log.v(TAG, "Ready CountDown!" + millisUntilFinished/ 1000);
            Toast message -"Get Ready In X seconds"
            Log.v(TAG, "Ready CountDown Complete!");
            Toast message - "GO!"
            belongs here.
            This timer countdown from 10 seconds to 0 seconds and stops after "GO!" is shown.
         */
        readyTimer = new CountDownTimer(10000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Log.v(TAG, "Ready Countdown! " + millisUntilFinished/1000);
                Toast.makeText(getApplicationContext(), "Get Ready In " + millisUntilFinished / 1000 + " seconds", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFinish() {
                Log.v(TAG, "Ready CountDown Complete!");
                Toast.makeText(getApplicationContext(), "GO!", Toast.LENGTH_LONG).show();
                placeMoleTimer();
                readyTimer.cancel();
            }
        };
        readyTimer.start();
    }
    private void placeMoleTimer(){
        /* HINT:
           Creates new mole location each second.
           Log.v(TAG, "New Mole Location!");
           setNewMole();
           belongs here.
           This is an infinite countdown timer.
         */
        int lvlTime = moleTimer[lvlSelected - 1];
        newMolePlaceTimer = new CountDownTimer(lvlTime, 1000){
            @Override
            public void onTick(long millisUntilFinished) {
            }
            @Override
            public void onFinish() {
                setNewMole();
                Log.v(TAG, "New Mole Location!");
                newMolePlaceTimer.start();
            }
        };
        newMolePlaceTimer.start();
    }
    private static final int[] BUTTON_IDS = {
            /* HINT:
                Stores the 9 buttons IDs here for those who wishes to use array to create all 9 buttons.
                You may use if you wish to change or remove to suit your codes.*/
            R.id.button1,
            R.id.button2,
            R.id.button3,
            R.id.button4,
            R.id.button5,
            R.id.button6,
            R.id.button7,
            R.id.button8,
            R.id.button9

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        /*Hint:
            This starts the countdown timers one at a time and prepares the user.
            This also prepares level difficulty.
            It also prepares the button listeners to each button.
            You may wish to use the for loop to populate all 9 buttons with listeners.
            It also prepares the back button and updates the user score to the database
            if the back button is selected.
         */

        Intent molePage = getIntent();
        lvlSelected = molePage.getIntExtra("levelSelected", 0);

        backButton = findViewById(R.id.backButton);
        scoreView = findViewById(R.id.scoreView);
        scoreView.setText(String.valueOf(score));

        sharedPreferences = getSharedPreferences(GLOBAL_PREFS, MODE_PRIVATE);
        username = sharedPreferences.getString(MY_USERNAME, "");



        for(final int id : BUTTON_IDS){
            /*  HINT:
            This creates a for loop to populate all 9 buttons with listeners.
            You may use if you wish to remove or change to suit your codes.
            */
            buttonListener = (Button) findViewById(id);
            buttonListener.setOnClickListener(new View.OnClickListener() {
             @Override
              public void onClick(View v) {
                    doCheck(buttonListener);
               }
            });

        }
    }
    @Override
    protected void onStart(){
        super.onStart();
        scoreView.setText(String.valueOf(score));
        readyTimer();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserScore();
                Intent intent = new Intent(Main4Activity.this, Main3Activity.class);
                startActivity(intent);
            }
        });

    }

    private void doCheck(Button checkButton)
    {
        /* Hint:
            Checks for hit or miss
            Log.v(TAG, FILENAME + ": Hit, score added!");
            Log.v(TAG, FILENAME + ": Missed, point deducted!");
            belongs here.
        */
        if (checkButton.getText().toString().equals("*")){
            Log.v(TAG, "Hit, score added!");
            score += 1;
        }
        else{
            Log.v(TAG, "Missed, score deducted!");
            score -= 1;
        }
        scoreView.setText(String.valueOf(score));
        placeMoleTimer();

    }

    public void setNewMole()
    {
        /* Hint:
            Clears the previous mole location and gets a new random location of the next mole location.
            Sets the new location of the mole. Adds additional mole if the level difficulty is from 6 to 10.
         */
        if(lvlSelected < 6){
            Random ran = new Random();
            int randomLocation = ran.nextInt(9);
            for(final int id : BUTTON_IDS){
                setButton = (Button) findViewById(id);
                setButton.setText("o");
            }
            moleButton = (Button) findViewById(BUTTON_IDS[randomLocation]);
            moleButton.setText("*");
        }
        else {
            Random ran = new Random();
            int randomLocation = ran.nextInt(9);
            int randomLocation2 = ran.nextInt(8);
            for(final int id : BUTTON_IDS){
                setButton = (Button) findViewById(id);
                setButton.setText("o");
            }
            moleButton = (Button) findViewById(BUTTON_IDS[randomLocation]);
            moleButton.setText("*");
            moleButton2 = (Button) findViewById(BUTTON_IDS[randomLocation2]);
            moleButton2.setText("*");
        }
    }

    private void updateUserScore()
    {

     /* Hint:
        This updates the user score to the database if needed. Also stops the timers.
        Log.v(TAG, FILENAME + ": Update User Score...");
      */
        newMolePlaceTimer.cancel();
        readyTimer.cancel();
        UserData userData = dbHandler.findUser(username);
        int highestScore = userData.getScores().get(lvlSelected - 1);
        ArrayList<Integer> currentScore = userData.getScores();
        if (score > highestScore)
        {
            currentScore.set(lvlSelected - 1, score);
            userData.setScores(currentScore);
            dbHandler.deleteAccount(username);
            dbHandler.addUser(userData);
            Log.v(TAG, FILENAME + ": Update User Score...");
        }

    }

}

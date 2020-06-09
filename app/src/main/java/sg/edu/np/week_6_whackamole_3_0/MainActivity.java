package sg.edu.np.week_6_whackamole_3_0;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    /*
        1. This is the main page for user to log in
        2. The user can enter - Username and Password
        3. The user login is checked against the database for existence of the user and prompts
           accordingly via Toastbox if user does not exist. This loads the level selection page.
        4. There is an option to create a new user account. This loads the create user page.
     */
    private static final String FILENAME = "MainActivity.java";
    private static final String TAG = "Whack-A-Mole3.0!";

    public String GLOBAL_PREFS = "MyPrefs";
    public String MY_USERNAME= "MyUserNAME";
    SharedPreferences sharedPreferences;

    private TextView newUser;
    private Button loginButton;

    MyDBHandler dbHandler = new MyDBHandler(this,null,null,1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        newUser = findViewById(R.id.textView_newUser);
        newUser.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                startActivity(intent);
                Log.v(TAG, FILENAME + ": Create new user!");
                return false;
            }
        });

        loginButton = findViewById(R.id.buttonLogin);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText etUsername = findViewById(R.id.editText_Username);
                EditText etPassword = findViewById(R.id.editText_Password);
                Log.v(TAG, FILENAME + ": Logging in with: " + etUsername.getText().toString() + ": " + etPassword.getText().toString());

                if(isValidUser(etUsername.getText().toString(), etPassword.getText().toString()) == true)
                {
                    Intent intent = new Intent(MainActivity.this, Main3Activity.class);
                    startActivity(intent);
                    Log.v(TAG, FILENAME + ": Valid User! Logging in");
                    sharedPreferences = getSharedPreferences(GLOBAL_PREFS, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(MY_USERNAME, etUsername.getText().toString());
                    editor.commit();
                    Toast.makeText(MainActivity.this, "Valid", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(MainActivity.this,"Invalid Username or Password", Toast.LENGTH_SHORT).show();
                    Log.v(TAG, FILENAME + ": Invalid user!");
                }
            }
        });


        /* Hint:
            This method creates the necessary login inputs and the new user creation ontouch.
            It also does the checks on button selected.
            Log.v(TAG, FILENAME + ": Create new user!");
            Log.v(TAG, FILENAME + ": Logging in with: " + etUsername.getText().toString() + ": " + etPassword.getText().toString());
            Log.v(TAG, FILENAME + ": Valid User! Logging in");
            Log.v(TAG, FILENAME + ": Invalid user!");

        */


    }

    protected void onStop(){
        super.onStop();
        finish();
    }

    public boolean isValidUser(String userName, String password){

        /* HINT:
            This method is called to access the database and return a true if user is valid and false if not.
            Log.v(TAG, FILENAME + ": Running Checks..." + dbData.getMyUserName() + ": " + dbData.getMyPassword() +" <--> "+ userName + " " + password);
            You may choose to use this or modify to suit your design.
         */

        UserData dbData = dbHandler.findUser(userName);
        Log.v(TAG, FILENAME + ": Running Checks..." + dbData.getMyUserName() + ": " + dbData.getMyPassword() +" <--> "+ userName + " " + password);
        if(dbData.getMyUserName().equals(userName) && dbData.getMyPassword().equals(password))
        {
            return true;
        }
        return false;

    }

}

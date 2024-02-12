package algonquin.cst2335.lian0122;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Button;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        // Retrieve the previously saved email address, if any
        String emailAddress = prefs.getString("LoginName","");
        Log.w("MainActivity","In onCreate() - Loading Widgets");

        EditText et_email = findViewById(R.id.email_editText);
        // Set the email address to EditText if it was previously saved
        et_email.setText(emailAddress);

        Button bt = findViewById(R.id.loginButton);
        bt.setOnClickListener(clk ->{
            Intent nextPage = new Intent( MainActivity.this, SecondActivity.class);
            nextPage.putExtra("EmailAddress",et_email.getText().toString());

            // Save the currently input email into SharedPreferences
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("LoginName",et_email.getText().toString());
            editor.putFloat("Height",4.5f);
            editor.putInt("Age", 35);
            editor.apply();

            startActivity(nextPage);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.w("MainActivity","In onStart() - The application is now visible on screen");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.w("MainActivity","In onResume() - The application is now responding to user input");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.w("MainActivity","In onPause() - The application no longer responds to user input");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.w("MainActivity","In onStop() - The application is no longer visible");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.w("MainActivity","In onDestroy() - Any memory used by the application is freed");
    }
}
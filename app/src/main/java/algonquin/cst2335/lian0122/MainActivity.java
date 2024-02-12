package algonquin.cst2335.lian0122;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/** This is lab Week5, the purpose is to learn how to write and generate JavaDoc
 *
 * @author Ping Liang
 * @version 1.0
 */
@SuppressLint("SetTextI18n")
public class MainActivity extends AppCompatActivity {
    /**This holds the text at the centre of the screen*/
    private TextView tv_pwd = null;

    /**This holds the password right below the textView*/
    private EditText et_pwd = null;
    /**This holds the button at the bottom of the screen*/
    private Button bt_login = null;
    /**This function check the password's complexity
     *
     * @param pw the String object that we are checking
     * @return Return true if the password is complex enough, otherwise return false
     */

    boolean checkPasswordComplexity(String pw){
        boolean foundUpperCase = false;
        boolean foundLowerCase = false;
        boolean foundNumber = false;
        boolean foundSpecial = false;

        for (char c : pw.toCharArray()){
            if (Character.isUpperCase(c)) foundUpperCase = true;
            if (Character.isLowerCase(c)) foundLowerCase = true;
            if (Character.isDigit(c)) foundNumber = true;
            if (isSpecialCharacter(c)) foundSpecial = true;
        }

        if(!foundUpperCase) {
            Toast.makeText(this,R.string.upper_case_message,Toast.LENGTH_LONG).show();
            return false;
        } else if( ! foundLowerCase) {
            Toast.makeText(this,R.string.lower_case_message,Toast.LENGTH_LONG).show();
            return false;
        } else if( ! foundNumber) {
            Toast.makeText(this,R.string.number_case_message,Toast.LENGTH_LONG).show();
            return false;
        } else if(! foundSpecial) {
            Toast.makeText(this,R.string.special_case_message,Toast.LENGTH_LONG).show();
            return false;
        }
            return true;
    }

    /**
     * Checks if a character is a special character.
     *
     * @param c The character to check.
     * @return True if the character is a special character, false otherwise.
     */
    private boolean isSpecialCharacter(char c) {
        switch (c) {
            case '#':
            case '$':
            case '%':
            case '^':
            case '&':
            case '*':
            case '!':
            case '@':
            case '?':
                return true;//return true if c is one of: #$%^&*!@?
            default:
                return false;//return false otherwise
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_pwd = findViewById(R.id.textView_pwd);
        et_pwd = findViewById(R.id.editText_pwd);
        bt_login = findViewById(R.id.button_login);

        bt_login.setOnClickListener(clk -> {
            String password = et_pwd.getText().toString();
            if (checkPasswordComplexity(password)) {
                tv_pwd.setText(R.string.pwd_success_message);
                // Proceed with login or next steps if the password is complex enough
                Toast.makeText(this, R.string.toast_pwd_success_message, Toast.LENGTH_LONG).show();
            } else {
                // If the password does not meet the complexity requirements, update the TextView
                // with a message indicating rejection
                tv_pwd.setText(R.string.pwd_fail_message);
            }
        });
    }
}
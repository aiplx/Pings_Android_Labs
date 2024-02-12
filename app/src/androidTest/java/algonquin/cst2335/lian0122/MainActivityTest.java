package algonquin.cst2335.lian0122;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.pressImeActionButton;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * This class contains instrumentation tests for the MainActivity of an Android application.
 * It uses Espresso framework for UI testing.
 */
@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    /**
     * Rule to launch MainActivity for testing.
     */
    @Rule
    public ActivityScenarioRule<MainActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);

    /**
     * Tests the behavior of the MainActivity when an incorrect password is entered.
     * It simulates user input of a password and verifies the application's response
     * to ensure the correct error message is displayed.
     */
    @Test
    public void mainActivityTest() {
        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(700);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatEditText = onView(withId(R.id.editText_pwd));
        appCompatEditText.perform(replaceText("12345"), closeSoftKeyboard());

        ViewInteraction materialButton = onView(withId(R.id.button_login));
        materialButton.perform(click());

        ViewInteraction textView = onView(withId(R.id.textView_pwd));
        textView.check(matches(withText(R.string.pwd_fail_message)));
    }

    /**
     * Tests the application's response to a password missing an uppercase character.
     * It inputs a password, simulates a button click, and checks the error message
     * to validate that the correct validation message is shown.
     */
    @Test
    public void testFindMissingUpperCase() {
        //find the EditText
        ViewInteraction appCompatEditText = onView(withId(R.id.editText_pwd));
        //type in password123#$*
        appCompatEditText.perform(replaceText("password123#$*"));

        // find the button
        ViewInteraction materialButton = onView(withId(R.id.button_login));
        //click the button
        materialButton.perform(click());

        //find the text view
        ViewInteraction textView = onView(withId(R.id.textView_pwd));
        //check the text
        textView.check(matches(withText(R.string.pwd_fail_message)));
    }

    /**
     * Tests the application's response to a password missing a lowercase character.
     * It inputs a password, simulates a button click, and checks the error message
     * to validate that the correct validation message is shown.
     */
    @Test
    public void testFindMissingLowerCase() {
        //find the EditText
        ViewInteraction appCompatEditText = onView(withId(R.id.editText_pwd));
        //type in password123#$*
        appCompatEditText.perform(replaceText("QQQSA123#$*"));

        // find the button
        ViewInteraction materialButton = onView(withId(R.id.button_login));
        //click the button
        materialButton.perform(click());

        //find the text view
        ViewInteraction textView = onView(withId(R.id.textView_pwd));
        //check the text
        textView.check(matches(withText(R.string.pwd_fail_message)));
    }

    /**
     * Tests the application's response to a password missing a digit.
     * It inputs a password, simulates a button click, and checks the error message
     * to validate that the correct validation message is shown.
     */
    @Test
    public void testFindMissingDigitCase() {
        //find the EditText
        ViewInteraction appCompatEditText = onView(withId(R.id.editText_pwd));
        //type in password123#$*
        appCompatEditText.perform(replaceText("QQQSAsqs#$*"));

        // find the button
        ViewInteraction materialButton = onView(withId(R.id.button_login));
        //click the button
        materialButton.perform(click());

        //find the text view
        ViewInteraction textView = onView(withId(R.id.textView_pwd));
        //check the text
        textView.check(matches(withText(R.string.pwd_fail_message)));
    }

    /**
     * Tests the application's response to a password missing a special character.
     * It inputs a password, simulates a button click, and checks the error message
     * to validate that the correct validation message is shown.
     */
    @Test
    public void testFindMissingSpecialCase() {
        //find the EditText
        ViewInteraction appCompatEditText = onView(withId(R.id.editText_pwd));
        //type in password123#$*
        appCompatEditText.perform(replaceText("QQQsa123"));

        // find the button
        ViewInteraction materialButton = onView(withId(R.id.button_login));
        //click the button
        materialButton.perform(click());

        //find the text view
        ViewInteraction textView = onView(withId(R.id.textView_pwd));
        //check the text
        textView.check(matches(withText(R.string.pwd_fail_message)));
    }

    /**
     * Tests the application's response to a perfect password that meets all the requirements.
     * It inputs a correct password, simulates a button click, and checks the success message
     * to ensure the application validates the password correctly.
     */
    @Test
    public void testPerfectCase() {
        //find the EditText
        ViewInteraction appCompatEditText = onView(withId(R.id.editText_pwd));
        //type in password123#$*
        appCompatEditText.perform(replaceText("QQQsa123#$*"));

        // find the button
        ViewInteraction materialButton = onView(withId(R.id.button_login));
        //click the button
        materialButton.perform(click());

        //find the text view
        ViewInteraction textView = onView(withId(R.id.textView_pwd));
        //check the text
        textView.check(matches(withText(R.string.pwd_success_message)));
    }
}

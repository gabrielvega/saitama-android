package com.saitama.rentbikes.activities;

/**
 * Created by gabrielvega on 2016-11-19.
 */

import android.app.Activity;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.saitama.rentbikes.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;


/**
 * Basic tests showcasing simple view matchers and actions like {@link ViewMatchers#withId},
 * {@link ViewActions#click} and {@link ViewActions#typeText}.
 * <p>
 * Note that there is no need to tell Espresso that a view is in a different {@link Activity}.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class SignupActivityTest {

    public static final String TYPED_EMAIL = "crossover@crossover.com";
    public static final String TYPED_PASSWORD = "crossover";

    @Rule
    public ActivityTestRule<LoginActivity> mActivityRule = new ActivityTestRule<>(
            LoginActivity.class);

    @Test
    public void changeText_sameActivity() {
        // Type text and then press the button.
        onView(withId(R.id.et_email))
                .perform(typeText(TYPED_EMAIL), closeSoftKeyboard());
        onView(withId(R.id.et_password))
                .perform(typeText(TYPED_PASSWORD), closeSoftKeyboard());

        // Check that the text was changed.
        onView(withId(R.id.et_email)).check(matches(withText(TYPED_EMAIL)));
        onView(withId(R.id.et_password)).check(matches(withText(TYPED_PASSWORD)));
    }

}
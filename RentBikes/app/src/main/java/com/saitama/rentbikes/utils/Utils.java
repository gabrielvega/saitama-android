package com.saitama.rentbikes.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

/**
 * @author gabrielvega
 * @version 1.0.0
 *          created 2016-11-17
 * @since 1.0.0
 */

public class Utils {

    public static long TIMEOUT = 10000;

    public static void toast(String text, int duration, Context context) {

        if (duration == 0)
            duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    public static void setAccessToken(Context ctx, String cid) {
        SharedPreferences pref = ctx.getSharedPreferences(Const.SHARED_PREFERENCES_NAME.getValue(), 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(encrypt(Const.ACCESSTOKEN.getValue()), encrypt(cid));
        editor.commit();
    }

    public static String getAccessToken(Context ctx) {
        SharedPreferences pref = ctx.getSharedPreferences(Const.SHARED_PREFERENCES_NAME.getValue(), 0);
        return decrypt(pref.getString(encrypt(Const.ACCESSTOKEN.getValue()), ""));
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public static void showProgress(Context ctx, View formView, View progressView, final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = ctx.getResources().getInteger(android.R.integer.config_shortAnimTime);

            formView.setVisibility(show ? View.GONE : View.VISIBLE);
            formView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    formView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            progressView.setVisibility(show ? View.VISIBLE : View.GONE);
            progressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    progressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            progressView.setVisibility(show ? View.VISIBLE : View.GONE);
            formView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    /**
     * @param input String to be encrypted
     * @return Encripted string
     */
    public static String encrypt(String input) {
        return Base64.encodeToString(input.getBytes(), Base64.DEFAULT);
    }

    /**
     * @param input String to be decrypted
     * @return Decrypted string
     */
    public static String decrypt(String input) {
        return new String(Base64.decode(input, Base64.DEFAULT));
    }

}

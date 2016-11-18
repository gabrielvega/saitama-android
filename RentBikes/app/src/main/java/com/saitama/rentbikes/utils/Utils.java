package com.saitama.rentbikes.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

/**
 * @author gabrielvega
 * @since 1.0.0
 * @version 1.0.0
 * created 2016-11-17
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
        SharedPreferences pref = ctx.getSharedPreferences(Const.SHARED_PREFERENCES_NAME.getValue(), 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(Const.ACCESSTOKEN.getValue(), cid);
        editor.commit();
    }

    public static String getAccessToken(Context ctx) {
        SharedPreferences pref = ctx.getSharedPreferences(Const.SHARED_PREFERENCES_NAME.getValue(), 0); // 0 - for private mode
        return pref.getString(Const.ACCESSTOKEN.getValue(), "");
    }

}

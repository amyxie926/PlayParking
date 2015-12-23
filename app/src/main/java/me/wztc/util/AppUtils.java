package me.wztc.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.util.regex.Pattern;

public class AppUtils {
    public static final String REG_CELLPHONE = "^1[3,4,5,7,8]\\d{9}$";

    public static boolean isEmpty(String string) {
        return string == null || string.length() == 0 || string.equals("null");
    }

    public static void storeUserSession(Context context, String name, Object value) {
        if (context == null)
            return;

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit = pref.edit();

        if (value instanceof String)
            edit.putString(name, (String) value);
        else if (value instanceof Integer)
            edit.putInt(name, (Integer) value);
        else if (value instanceof Long)
            edit.putLong(name, (Long) value);
        else if (value instanceof Boolean)
            edit.putBoolean(name, (Boolean) value);
        else if (value instanceof Float)
            edit.putFloat(name, (Float) value);

        edit.commit();
    }

    public static Object getUserSession(Context context, String name, Object defaultValue) {
        if (context == null)
            return defaultValue;

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);

        if (defaultValue instanceof String)
            return pref.getString(name, (String) defaultValue);
        else if (defaultValue instanceof Integer)
            return pref.getInt(name, (Integer) defaultValue);
        else if (defaultValue instanceof Long)
            return pref.getLong(name, (Long) defaultValue);
        else if (defaultValue instanceof Boolean)
            return pref.getBoolean(name, (Boolean) defaultValue);
        else if (defaultValue instanceof Float)
            return pref.getFloat(name, (Float) defaultValue);
        else
            return defaultValue;
    }

    public static void clearUserSession(Context context, String name) {
        if (context != null && name != null) {
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor edit = pref.edit();
            edit.remove(name);
            edit.commit();
        }
    }

    /**
     * hide the keyboard
     *
     * @param context
     * @param view
     */
    public static void hideSoftKb(Context context, View view) {
        if (context == null) {
            return;
        }
        try {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isMobilePhone(String phone) {
        if (Pattern.matches(REG_CELLPHONE, phone)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @param str
     * @return
     * @author pl
     */
    public static String parseEmpty(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        } else {
            return str;
        }
    }

}

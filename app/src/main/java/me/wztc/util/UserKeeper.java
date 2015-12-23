package me.wztc.util;

import android.content.Context;
import android.content.SharedPreferences;
import me.wztc.model.User;

public class UserKeeper {

    private static final String PREFERENCES_NAME = "user_cache";

    private static final String KEY_UID = "uid";
    private static final String KEY_SEX = "sex";
    private static final String KEY_USER_NAME = "username";
    private static final String KEY_MOBILE = "mobile";
    private static final String KEY_MONEY = "money";

    public static void saveUser(Context context, User user) {
        if (context == null || user == null) {
            return;
        }
        SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_APPEND);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(KEY_UID, user.getUserId());
        editor.putString(KEY_USER_NAME, user.getUserName());
        editor.putString(KEY_SEX, user.getSex());
        editor.putString(KEY_MOBILE, user.getPhoneNumber());
        editor.putString(KEY_MONEY, user.getMoney());
        editor.commit();

    }

    public static User readUser(Context context) {
        if (null == context) {
            return null;
        }

        SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_APPEND);
        User user = new User();

        user.setUserId(pref.getString(KEY_UID, ""));
        user.setUserName(pref.getString(KEY_USER_NAME, ""));
        user.setSex(pref.getString(KEY_SEX, ""));
        user.setPhoneNumber(pref.getString(KEY_MOBILE, ""));
        user.setMoney(pref.getString(KEY_MONEY, ""));

        if (AppUtils.isEmpty(user.getUserId())){
            user = null;
        }
        return user;
    }

    public static void clear(Context context) {
        if (null == context) {
            return;
        }

        SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_APPEND);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.apply();
    }
}

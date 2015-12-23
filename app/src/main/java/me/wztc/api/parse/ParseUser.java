package me.wztc.api.parse;

import me.wztc.model.User;
import org.json.JSONException;
import org.json.JSONObject;

public class ParseUser {
    public static User parse(JSONObject json) throws JSONException {
        if (json == null) {
            return null;
        }
        User user = new User();

        user.setUserId(json.getString("userId"));
        user.setPhoneNumber(json.getString("phone"));
        user.setUserName(json.getString("userName"));
        user.setHeadImage(json.optString("avatarUrl"));
        user.setSex(json.optString("sex"));
        user.setPlateNo(json.optString("card"));
        user.setIntegral(json.optString("integral"));
        user.setMoney(json.optString("money"));
        return user;
    }
}

package me.wztc.api.parse;

import org.json.JSONException;
import org.json.JSONObject;

public class ParseVerify {

    public static String parse(JSONObject json) throws JSONException {
        String code = json.getString("code");
        return code;
    }
}

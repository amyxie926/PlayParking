package me.wztc.api.parse;

import org.json.JSONObject;

public class ParseCommon {
    public static String parseCommon(JSONObject json) {
        String status = json.optString("status");
        return status;
    }
}

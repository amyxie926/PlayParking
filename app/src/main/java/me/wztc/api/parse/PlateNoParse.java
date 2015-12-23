package me.wztc.api.parse;

import org.json.JSONObject;

public class PlateNoParse {
	public static String plateNoParse(JSONObject json) {
        String plateNo =json.optString("plateNo");
        return plateNo;
    }
}

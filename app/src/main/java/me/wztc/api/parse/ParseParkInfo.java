package me.wztc.api.parse;

import me.wztc.model.ParkInfo;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ParseParkInfo {
    public static ArrayList<ParkInfo> parse(JSONArray array) throws JSONException {
        if (array == null || array.length() == 0) {
            return null;
        }

        ArrayList<ParkInfo> parkInfos = new ArrayList<>();
        int size = array.length();
        for (int i = 0; i < size; i++) {
            parkInfos.add(parseParkInfo(array.getJSONObject(i)));
        }
        return parkInfos;
    }

    public static ParkInfo parseParkInfo(JSONObject json) throws JSONException {
        ParkInfo parkInfo = new ParkInfo();

        parkInfo.setParkId(json.optString("parkId"));
        parkInfo.setParkName(json.optString("parkName"));
        parkInfo.setAddress(json.optString("address"));
        parkInfo.setCapacity(json.optInt("capacity"));
        parkInfo.setType(json.optString("type"));
        parkInfo.setLng(json.optDouble("lng"));
        parkInfo.setLat(json.optDouble("lat"));
        parkInfo.setRemark(json.optString("remark"));
        parkInfo.setPrice((float) json.optDouble("price"));

        return parkInfo;
    }
}

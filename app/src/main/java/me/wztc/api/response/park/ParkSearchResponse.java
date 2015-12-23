package me.wztc.api.response.park;

import me.wztc.api.parse.ParseParkInfo;
import me.wztc.api.response.APIResponse;
import me.wztc.model.ParkInfo;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ParkSearchResponse extends APIResponse {
    private ArrayList<ParkInfo> parkInfos;

    public ParkSearchResponse(JSONObject json) throws JSONException {
        String parklist = json.getString("parklist");
        parkInfos = ParseParkInfo.parse(new JSONArray(parklist));
    }

    public ArrayList<ParkInfo> getParkInfos() {
        return parkInfos;
    }
}

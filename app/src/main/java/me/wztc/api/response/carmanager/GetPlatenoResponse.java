package me.wztc.api.response.carmanager;

import me.wztc.api.parse.PlateNoParse;
import me.wztc.api.response.APIResponse;
import org.json.JSONException;
import org.json.JSONObject;

public class GetPlatenoResponse extends APIResponse {
    private String plateNo;

    public GetPlatenoResponse(JSONObject json) throws JSONException{
        plateNo = PlateNoParse.plateNoParse(json).toString();
    }

    public String getPlateNo() {
        return plateNo;
    }
}

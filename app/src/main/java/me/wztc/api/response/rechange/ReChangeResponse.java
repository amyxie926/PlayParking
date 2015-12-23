package me.wztc.api.response.rechange;

import me.wztc.api.response.APIResponse;
import org.json.JSONException;
import org.json.JSONObject;

public class ReChangeResponse extends APIResponse {
    private String money;

    public ReChangeResponse(JSONObject json) throws JSONException {
        String user = json.optString("user");
        if (user != null) {
            JSONObject j = new JSONObject(user);
            money = j.getString("money");
        }
    }

    public String getMoney() {
        return money;
    }
}

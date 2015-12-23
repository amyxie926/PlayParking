package me.wztc.api.response.pay;

import me.wztc.api.response.APIResponse;
import org.json.JSONException;
import org.json.JSONObject;

public class PayInfoSyncResponse extends APIResponse {
    private String status;

    public PayInfoSyncResponse(JSONObject json) throws JSONException {
        status = json.optString("resCode");
    }

    public String getStatus() {
        return status;
    }
}

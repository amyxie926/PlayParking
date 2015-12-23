package me.wztc.api.response.register;

import me.wztc.api.parse.ParseVerify;
import me.wztc.api.response.APIResponse;

import org.json.JSONException;
import org.json.JSONObject;

public class GetSMSResponse extends APIResponse {
    private String code;

    public GetSMSResponse(JSONObject json) throws JSONException {
        code = ParseVerify.parse(json);
    }

    public String getCode() {
        return code;
    }
}

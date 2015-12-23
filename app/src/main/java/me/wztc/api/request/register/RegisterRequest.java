package me.wztc.api.request.register;

import me.wztc.api.request.ApiRequest;
import me.wztc.api.response.BaseResponse;
import me.wztc.util.MD5Util;
import org.json.JSONException;
import org.json.JSONObject;

public class RegisterRequest extends ApiRequest<BaseResponse> {

    private String phoneNumber, password;

    public RegisterRequest(String phoneNumber, String password) {
        this.phoneNumber = phoneNumber;
        this.password = password;
    }

    @Override
    protected String serviceComponent() {
        return "/iparking-ips/user_insert";
    }

    @Override
    protected String getJsonParams() {
        JSONObject json = new JSONObject();
        try {
            json.put("phoneNumber", phoneNumber);
            json.put("password", MD5Util.getMD5(password));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json.toString();
    }

    @Override
    protected void onResponseReceived(JSONObject json) throws JSONException {
        BaseResponse response = new BaseResponse(json);
        responseHandler.handleResponse(response);
    }
}

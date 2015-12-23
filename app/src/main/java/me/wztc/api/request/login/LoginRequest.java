package me.wztc.api.request.login;

import me.wztc.api.request.ApiRequest;
import me.wztc.api.response.login.LoginResponse;
import me.wztc.util.MD5Util;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginRequest extends ApiRequest<LoginResponse> {

    private String phoneNumber;
    private String password;

    public LoginRequest(String phoneNumber, String password) {
        this.phoneNumber = phoneNumber;
        this.password = password;
    }

    @Override
    protected String serviceComponent() {
        return "/iparking-ips/user_login";
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
        LoginResponse response = new LoginResponse(json);
        responseHandler.handleResponse(response);
    }
}

package me.wztc.api.request.login;

import org.json.JSONException;
import org.json.JSONObject;

import me.wztc.api.request.ApiRequest;
import me.wztc.api.response.BaseResponse;
import me.wztc.util.MD5Util;

public class ForgetPasswordRequest extends ApiRequest<BaseResponse> {

    private String phoneNumber;
    private String password;

    public ForgetPasswordRequest(String phoneNumber, String password) {
        this.phoneNumber = phoneNumber;
        this.password=password;
    }

	@Override
    protected String serviceComponent() {
        return "/iparking-ips/user_forget_password";
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

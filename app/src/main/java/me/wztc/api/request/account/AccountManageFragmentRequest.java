package me.wztc.api.request.account;

import org.json.JSONException;
import org.json.JSONObject;

import me.wztc.api.request.ApiRequest;
import me.wztc.api.response.account.GetPlatenoResponse;

public class AccountManageFragmentRequest extends ApiRequest<GetPlatenoResponse> {

    private String userId;
    private String headImage;
    private String userName;
    private String sex;

    public AccountManageFragmentRequest(String userId, String headImage, String userName, String sex) {
        this.userId = userId;
        this.headImage = headImage;
        this.userName = userName;
        this.sex = sex;
    }

    @Override
    protected String getServiceHost() {
        return "test.iparking.me";
    }

    @Override
    protected int getServicePort() {
        return 8080;
    }

    @Override
    protected String serviceComponent() {
        return "/iparking-ips/user_update_user";
    }

    protected String getJsonParams() {
        JSONObject json = new JSONObject();
        try {
            json.put("userId", userId);
            json.put("headImage", headImage);
            json.put("userName", userName);
            json.put("sex", sex);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json.toString();
    }

    @Override
    protected void onResponseReceived(JSONObject json) throws JSONException {
        GetPlatenoResponse response = new GetPlatenoResponse(json);
        responseHandler.handleResponse(response);
    }

}

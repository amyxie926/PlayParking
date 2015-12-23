package me.wztc.api.request.login;

import me.wztc.api.request.ApiRequest;
import me.wztc.api.response.register.GetSMSResponse;
import org.json.JSONException;
import org.json.JSONObject;

public class GetSMSRequest extends ApiRequest<GetSMSResponse> {

    private String phoneNumber;

    public GetSMSRequest(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    protected String serviceComponent() {
        return "/iparking-ips/user_forget_verify";
    }

    @Override
    protected String getJsonParams() {
        JSONObject json = new JSONObject();
        try {
            json.put("phoneNumber", phoneNumber);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json.toString();
    }

    @Override
    protected void onResponseReceived(JSONObject json) throws JSONException {
        GetSMSResponse response = new GetSMSResponse(json);
        responseHandler.handleResponse(response);
    }
}

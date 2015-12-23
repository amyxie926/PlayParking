package me.wztc.api.request.carmanager;

import org.json.JSONException;
import org.json.JSONObject;

import me.wztc.api.request.ApiRequest;
import me.wztc.api.response.carmanager.GetPlatenoResponse;


public class GetPlatenoRequest extends ApiRequest<GetPlatenoResponse> {
    private String userId;


    public GetPlatenoRequest(String  userId) {
        this.userId = userId;
       
    }

    @Override
    protected String serviceComponent() {
        return "/iparking-ips/getplateno";
    }

    @Override
    protected String getJsonParams() {
        JSONObject json = new JSONObject();
        try {
            json.put("userId", userId);
           
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

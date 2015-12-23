package me.wztc.api.request.pay;

import me.wztc.api.request.ApiRequest;
import me.wztc.api.response.pay.PayBillResponse;
import org.json.JSONException;
import org.json.JSONObject;

public class PayBillRequest extends ApiRequest<PayBillResponse> {


    private String userId;

    public PayBillRequest(String userId) {
        this.userId = userId;
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
    protected String serviceComponent() {
        return "/iparking-ips/park_bill";
    }

    @Override
    protected void onResponseReceived(JSONObject json) throws JSONException {
        PayBillResponse response = new PayBillResponse(json);
        responseHandler.handleResponse(response);
    }
}

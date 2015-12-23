package me.wztc.api.request.rechange;

import me.wztc.api.request.ApiRequest;
import me.wztc.api.response.rechange.ReChangeResponse;
import org.json.JSONException;
import org.json.JSONObject;

public class ReChangeRequest extends ApiRequest<ReChangeResponse> {

    private String userId, money, orderNo;

    public ReChangeRequest(String userId, String money, String orderNo) {
        this.userId = userId;
        this.money = money;
        this.orderNo = orderNo;
    }

    @Override
    protected String serviceComponent() {
        return "/iparking-ips/user_recharge";
    }

    @Override
    protected String getJsonParams() {
        JSONObject json = new JSONObject();

        try {
            json.put("userId", userId);
            json.put("money", money);
            json.put("orderNo", orderNo);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json.toString();
    }

    @Override
    protected void onResponseReceived(JSONObject json) throws JSONException {
        ReChangeResponse response = new ReChangeResponse(json);
        responseHandler.handleResponse(response);
    }
}

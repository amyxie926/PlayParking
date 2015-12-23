package me.wztc.api.request.pay;

import me.wztc.api.HttpMethod;
import me.wztc.api.request.ApiRequest;
import me.wztc.api.response.pay.PayInfoSyncResponse;
import me.wztc.util.Logger;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 11、 停车场支付信息同步接口API接口
 */
public class PayInfoSyncRequest extends ApiRequest<PayInfoSyncResponse> {
    private String wztcOrder;
    private String orderNo;
    private String amount;
    private String discount;


    public PayInfoSyncRequest(String wztcOrder, String orderNo, String amount, String discount) {
        this.wztcOrder = wztcOrder;
        this.orderNo = orderNo;
        this.amount = amount;
        this.discount = discount;
    }

    @Override
    protected HttpMethod getHttpMethod() {
        return HttpMethod.post;
    }

//    @Override
//    protected HashMap<String, String> getParameters() {
//        HashMap<String, String> p = super.getParameters();
//        p.put("wztc_order", wztcOrder);
//        p.put("orderNo", orderNo);
//        p.put("amount", amount);
//        p.put("discount", discount);
//        return p;
//    }

        @Override
    protected String getJsonParams() {
        JSONObject json = new JSONObject();

        try {
            json.put("wztc_order", wztcOrder);
            json.put("orderNo", orderNo);
            json.put("amount", amount);
            json.put("discount", discount);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json.toString();
    }

    @Override
    protected String serviceComponent() {
        return "/iparking-ips/park_pay_notice";
    }

    @Override
    protected void onResponseReceived(JSONObject json) throws JSONException {
        PayInfoSyncResponse response = new PayInfoSyncResponse(json);
        responseHandler.handleResponse(response);
    }

    @Override
    protected void success(int statusCode, JSONObject json) {
        try {
            String status = json.optString("resCode");

            if (status != null && status.equals("0"))
                onResponseReceived(json);
            else
                error(String.valueOf(statusCode), json);
        } catch (Exception e) {
            e.printStackTrace();
            error(String.valueOf(statusCode), json);
        }
    }

    protected void error(String errorCode, JSONObject json) {

        try {
            Logger.logE("json", json.toString());
            String code = json.optString("resCode");
            String msg = json.optString("resMsg");
            responseError(code, msg);
            return;
        } catch (NullPointerException e) {
            e.printStackTrace();
            responseError(errorCode, null);
        }

    }
}

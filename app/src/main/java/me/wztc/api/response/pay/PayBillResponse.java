package me.wztc.api.response.pay;

import me.wztc.api.parse.ParseBill;
import me.wztc.api.response.APIResponse;
import me.wztc.model.Bill;

import org.json.JSONException;
import org.json.JSONObject;

public class PayBillResponse extends APIResponse {
    private Bill bill;

    public PayBillResponse(JSONObject json) throws JSONException {
        String billJson = json.getString("bill");
        bill = ParseBill.parseBill(new JSONObject(billJson));
        bill.setWztcOrder(json.optString("wztc_order"));
    }

    public Bill getBill() {
        return bill;
    }
}

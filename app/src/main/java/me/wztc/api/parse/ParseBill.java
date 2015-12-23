package me.wztc.api.parse;

import me.wztc.model.Bill;

import org.json.JSONException;
import org.json.JSONObject;

public class ParseBill {

    public static Bill parseBill(JSONObject json) throws JSONException {
        Bill bill = new Bill();
        bill.setWztcOrder(json.optString("wztc_order"));
        bill.setUserId(json.optString("userId"));
        bill.setPlateNo(json.optString("plateNo"));
        bill.setBillStatus(json.optString("billStatus"));
        bill.setParkId(json.optString("parkId"));
        bill.setParkName(json.optString("parkName"));
        bill.setFee(json.optString("fee"));
        bill.setParkBillNo(json.optString("parkBillNo"));
        bill.setEntryTime(json.optString("entryTime"));
        bill.setDelayTime(json.optString("delayTime"));
        return bill;
    }
}

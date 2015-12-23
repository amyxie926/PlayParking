package me.wztc.api.request.goods;

import me.wztc.api.request.ApiRequest;
import me.wztc.api.response.goods.GetGoodsListResponse;
import org.json.JSONException;
import org.json.JSONObject;

public class GetGoodsListRequest extends ApiRequest<GetGoodsListResponse> {

    @Override
    protected String getServiceHost() {
        return "dev.iparking.me";
    }

    @Override
    protected int getServicePort() {
        return 80;
    }

    @Override
    protected String serviceComponent() {
        return null;
    }

    @Override
    protected void onResponseReceived(JSONObject json) throws JSONException {

    }
}

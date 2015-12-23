package me.wztc.api.request.park;

import me.wztc.api.request.ApiRequest;
import me.wztc.api.response.park.ParkSearchResponse;
import me.wztc.util.AppUtils;
import org.json.JSONException;
import org.json.JSONObject;

public class ParkSearchRequest extends ApiRequest<ParkSearchResponse> {

    public static final String TYPE_NOT_RECOMMEND = "1";//非合作
    public static final String TYPE_RECOMMEND = "2";//合作停车场
    public static final String MEMBER_ALL = "0";//显示所有
    public static final String MEMBER_COOPERATION = "1";//显示合作

    private String type;//搜索类型(1-附近停车场，2-推荐停车场或合作停车场)。
    private String member;//是否需要判断会员，添加会员卡时，附近停车场只能显示合作的停车场，0是显示所有，1是显示合作
    private double lng;//经度
    private double lat;//纬度
    private String keyword;//搜索关键字

    public ParkSearchRequest(String type, String member, double lng, double lat, String keyword) {
        this.type = type;
        this.member = member;
        this.lng = lng;
        this.lat = lat;
        this.keyword = keyword;
    }

    @Override
    protected String getJsonParams() {
        JSONObject json = new JSONObject();

        try {
            json.put("type", type);
            json.put("member", member);
            json.put("lng", lng);
            json.put("lat", lat);
            if (!AppUtils.isEmpty(keyword)) {
                json.put("keyword", keyword);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json.toString();
    }

    @Override
    protected String serviceComponent() {
        return "/iparking-ips/park_getParkInfo";
    }

    @Override
    protected void onResponseReceived(JSONObject json) throws JSONException {
        ParkSearchResponse response = new ParkSearchResponse(json);
        responseHandler.handleResponse(response);
    }
}

package me.wztc.api.request.carmanager;

import org.json.JSONException;
import org.json.JSONObject;

import me.wztc.api.request.ApiRequest;
import me.wztc.api.response.BaseResponse;

public class DeletePlatenoRequest extends ApiRequest<BaseResponse> {
	 	private String plateNo;
	 	private String userId;
	    public DeletePlatenoRequest ( String  userId, String  plateNo) {
	        	this.userId = userId;
	    	  this. plateNo =  plateNo;
	    }
	  
	    @Override
	    protected String serviceComponent() {
	        return "/iparking-ips/updateplateno";
	    }

	    @Override
	    protected String getJsonParams() {
	        JSONObject json = new JSONObject();
	        try {
	            json.put("plateNo", plateNo);
	            json.put("userId", userId);
	           
	        } catch (JSONException e) {
	            e.printStackTrace();
	        }
	        return json.toString();
	    }

	    @Override
	    protected void onResponseReceived(JSONObject json) throws JSONException {
	        BaseResponse response = new BaseResponse(json);
	        responseHandler.handleResponse(response);
	    }
}

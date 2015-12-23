package me.wztc.api.request.account;

import org.json.JSONException;
import org.json.JSONObject;

import me.wztc.api.request.ApiRequest;
import me.wztc.api.response.BaseResponse;
import me.wztc.util.MD5Util;

public class PasswordChangeRequest extends ApiRequest<BaseResponse>{
	
	private String newpassword;
	private String oldpassword;
	private String userId;
	public PasswordChangeRequest(String userId,String newpassword,String oldpassword){
		this.oldpassword = oldpassword;
		this.newpassword = newpassword;
		this.userId=userId;
	}
	@Override
	protected String serviceComponent() {
		// TODO Auto-generated method stub
	 return "/iparking-ips/user_update_password";
	}
	 @Override
	    protected String getJsonParams() {
	        JSONObject json = new JSONObject();
	        try {
	        	json.put("userId", userId);
	        	json.put("oldpassword", MD5Util.getMD5(oldpassword));
	            json.put("newpassword", MD5Util.getMD5(newpassword));
	        } catch (JSONException e) {
	            e.printStackTrace();
	        }
	        return json.toString();
	    }


	@Override
	protected void onResponseReceived(JSONObject json) throws JSONException {
		// TODO Auto-generated method stub
		BaseResponse response = new BaseResponse(json);
        responseHandler.handleResponse(response);
		
	}

}

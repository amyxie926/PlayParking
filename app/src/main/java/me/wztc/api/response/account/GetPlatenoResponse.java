package me.wztc.api.response.account;

import org.json.JSONException;
import org.json.JSONObject;

import me.wztc.api.parse.ParseUser;
import me.wztc.api.response.APIResponse;
import me.wztc.model.User;

public class GetPlatenoResponse  extends APIResponse {
	  private User user;
	  
	  public GetPlatenoResponse (JSONObject json) throws JSONException {
		  	String userJson = json.getString("user");
	        user = ParseUser.parse(new JSONObject(userJson));
	    }
	  
	  	public User getUser() {
	        return user;
	    }
}

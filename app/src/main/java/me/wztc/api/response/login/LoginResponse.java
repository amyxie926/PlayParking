package me.wztc.api.response.login;

import me.wztc.api.parse.ParseUser;
import me.wztc.api.response.APIResponse;
import me.wztc.model.User;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginResponse extends APIResponse {
    private User user;

    public LoginResponse(JSONObject json) throws JSONException {
        String userJson = json.getString("user");
        user = ParseUser.parse(new JSONObject(userJson));
    }

    public User getUser() {
        return user;
    }
}

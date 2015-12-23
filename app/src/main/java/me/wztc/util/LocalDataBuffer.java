package me.wztc.util;

import com.baidu.location.BDLocation;
import me.wztc.model.User;

public class LocalDataBuffer {

    private static LocalDataBuffer instance = new LocalDataBuffer();
    private static User user;
    private static BDLocation location;


    private LocalDataBuffer() {
        super();
    }

    public static LocalDataBuffer getInstance() {
        return instance;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        LocalDataBuffer.user = user;
    }

    public BDLocation getLocation() {
        return location;
    }

    public void setLocation(BDLocation location) {
        LocalDataBuffer.location = location;
    }
}

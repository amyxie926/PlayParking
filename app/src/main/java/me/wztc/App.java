package me.wztc;

import android.app.Application;
import android.content.Context;
import com.baidu.mapapi.SDKInitializer;
import com.wztc.R;

import me.wztc.util.DeviceInfo;

public class App extends Application {
    @Override
    public void onCreate() {

        super.onCreate();
        SDKInitializer.initialize(getApplicationContext());
        setupEnvironment(getApplicationContext());
    }

    private void setupEnvironment(Context context) {
        String schema = context.getString(R.string.dev_schema);
        String host = context.getString(R.string.dev_host);
        int port = Integer.valueOf(context.getString(R.string.dev_port));
        String path = context.getString(R.string.dev_path);

        DeviceInfo.setDevEnvironment(schema, host, port, path);
    }
}

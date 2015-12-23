package me.wztc.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;

import me.wztc.notification.NotificationCenter;

public class BaseActivity extends FragmentActivity implements OnClickListener {
    protected NotificationCenter mCenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCenter = NotificationCenter.getInstance();

    }

    @Override
    public void onClick(View v) {
    }

}

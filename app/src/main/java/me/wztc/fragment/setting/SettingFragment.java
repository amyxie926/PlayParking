package me.wztc.fragment.setting;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.wztc.R;
import me.wztc.fragment.BaseFragment;
import me.wztc.notification.Notification;
import me.wztc.util.AppConstants;
import me.wztc.util.AppUtils;
import me.zcw.togglebutton.ToggleButton;

/**
 * 系统设置页面
 */
public class SettingFragment extends BaseFragment {
    private ToggleButton autoSearchParkingTb, wifiTipUpdateTb;

    private LinearLayout checkVersionLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        view.findViewById(R.id.setting_fragment_download_offline_maps_layout).setOnClickListener(this);
        view.findViewById(R.id.setting_fragment_clear_cache_layout).setOnClickListener(this);
        view.findViewById(R.id.setting_fragment_about_us_layout).setOnClickListener(this);

        checkVersionLayout = (LinearLayout) view.findViewById(R.id.setting_fragment_update_version_layout);
        checkVersionLayout.setOnClickListener(this);

        // Toggle Button
        autoSearchParkingTb = (ToggleButton) view.findViewById(R.id.setting_fragment_auto_search_parking_switch_tb);
        wifiTipUpdateTb = (ToggleButton) view.findViewById(R.id.setting_fragment_the_wifi_tip_update_tb);

        autoSearchParkingTb.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                AppUtils.storeUserSession(getActivity(), AppConstants.PREFERENCE_AUTO_SEARCH_PARKING, on);
            }
        });
        wifiTipUpdateTb.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                AppUtils.storeUserSession(getActivity(), AppConstants.PREFERENCE_WIFI_TIP_UPDATE, on);
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setTitle(R.string.setting_fragment_title);
        readSetting();
    }
    

    private void readSetting() {
        boolean autoSearch =  (Boolean) AppUtils.getUserSession(getActivity(), AppConstants.PREFERENCE_AUTO_SEARCH_PARKING, true);
        boolean wifiTip = (Boolean) AppUtils.getUserSession(getActivity(), AppConstants.PREFERENCE_WIFI_TIP_UPDATE, true);

        if (autoSearch) {
            autoSearchParkingTb.setToggleOn();
        } else {
            autoSearchParkingTb.setToggleOff();
        }

        if (wifiTip) {
            wifiTipUpdateTb.setToggleOn();
        } else {
            wifiTipUpdateTb.setToggleOff();
        }

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {

        case R.id.setting_fragment_auto_search_parking_switch_layout:// 自动搜索停车场开关
            setPushCommit();
            break;
        case R.id.setting_fragment_push_switch_layout://推送开关
            setPushLike();
            break;
        case R.id.setting_fragment_download_offline_maps_layout:// 下载离线地图

            break;
        case R.id.setting_fragment_clear_cache_layout:// 清除缓存
            break;
        case R.id.setting_fragment_about_us_layout:// 关于我们
            mCenter.postNotification(new Notification(AppConstants.NOTIFICATION_ADD_FRAGMENT, new AboutUsFragment()));
            break;
        case R.id.setting_fragment_update_version_layout:// 版本更新
            break;
        default:
            break;
        }
    }

    private void setPushCommit() {
        boolean commit = (boolean) AppUtils.getUserSession(getActivity(), AppConstants.PREFERENCE_AUTO_SEARCH_PARKING, true);
        boolean result = !commit;

        AppUtils.storeUserSession(getActivity(), AppConstants.PREFERENCE_AUTO_SEARCH_PARKING, result);
        if (result) {
            autoSearchParkingTb.setToggleOn();
        } else {
            autoSearchParkingTb.setToggleOff();
        }
    }

    private void setPushLike() {
        boolean like = (boolean) AppUtils.getUserSession(getActivity(), AppConstants.PREFERENCE_WIFI_TIP_UPDATE, true);
        boolean result = !like;

        AppUtils.storeUserSession(getActivity(), AppConstants.PREFERENCE_WIFI_TIP_UPDATE, result);
        if (result) {
            wifiTipUpdateTb.setToggleOn();
        } else {
            wifiTipUpdateTb.setToggleOff();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}

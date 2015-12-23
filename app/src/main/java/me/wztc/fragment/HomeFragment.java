package me.wztc.fragment;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.baidu.mapapi.model.LatLng;
import com.wztc.R;
import me.wztc.api.request.park.ParkSearchRequest;
import me.wztc.api.response.APIResponseHandler;
import me.wztc.api.response.park.ParkSearchResponse;
import me.wztc.dialog.SearchDialog;
import me.wztc.fragment.map.BaseMapFragment;
import me.wztc.fragment.pay.PayFragment;
import me.wztc.model.ParkInfo;
import me.wztc.notification.Notification;
import me.wztc.util.AppConstants;

import java.util.ArrayList;

/**
 * 首页画面
 */
public class HomeFragment extends BaseFragment {
    private BaseMapFragment mMapFragment;

    private LinearLayout payLayout;

    private boolean isShowPay;
    private boolean isGetAroundPark;

//    private View settingView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        FragmentManager manager = getChildFragmentManager();

        mMapFragment = (BaseMapFragment) manager.findFragmentById(R.id.bmapView);
        if (mMapFragment == null) {
            mMapFragment = new BaseMapFragment();
            mMapFragment.setOnGetLocationListener(onGetLocationListener);

            FragmentTransaction transaction = manager.beginTransaction();
            transaction.add(R.id.bmapView, mMapFragment);
            transaction.commit();
        }
        initView(view);
        return view;
    }

    private void initView(View view) {
        view.findViewById(R.id.header_menu).setOnClickListener(this);
        view.findViewById(R.id.home_scan_code_tv).setOnClickListener(this);
        view.findViewById(R.id.home_LPR_tv).setOnClickListener(this);
        view.findViewById(R.id.home_pay_tv).setOnClickListener(this);
        view.findViewById(R.id.home_search_tv).setOnClickListener(this);
        View gps = view.findViewById(R.id.home_gps_iv);
        gps.setOnClickListener(this);

        payLayout = (LinearLayout) view.findViewById(R.id.home_fragment_pay_layout);

//        CheckBox moreSettingCheckBox = (CheckBox) view.findViewById(R.id.home_more_setting_check_box);
//        moreSettingCheckBox.setOnCheckedChangeListener(onCheckedChangeListener);
//        settingView = view.findViewById(R.id.home_more_setting_row);
    }

//    private CompoundButton.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
//        @Override
//        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//            if (isChecked) {
//                settingView.setVisibility(View.VISIBLE);
//            } else {
//                settingView.setVisibility(View.GONE);
//            }
//        }
//    };

    private void isShowPay() {
        if (!isShowPay) {
            payLayout.setVisibility(View.VISIBLE);
            isShowPay = true;
        } else {
            payLayout.setVisibility(View.INVISIBLE);
            isShowPay = false;
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.header_menu://点击菜单按钮
                mCenter.postNotification(new Notification(AppConstants.NOTIFICATION_TOGGLE_MENU));
                break;
            case R.id.home_scan_code_tv:// 扫码
                break;
            case R.id.home_LPR_tv:// 车牌识别
                PayFragment payFragment = new PayFragment();
                if (isLogin()) {
                    mCenter.postNotification(new Notification(AppConstants.NOTIFICATION_ADD_FRAGMENT, payFragment));
                } else {
                    mCenter.postNotification(new Notification(AppConstants.NOTIFICATION_LOGIN, payFragment));
                }
                break;
            case R.id.home_pay_tv:// 支付
                isShowPay();
                break;
            case R.id.home_gps_iv://重新定位
                if (mMapFragment != null) {
                    mMapFragment.moveToCurrentLocation();
                }
                break;
            case R.id.home_search_tv://点击搜索
                SearchDialog dialog = new SearchDialog(getActivity());
                dialog.setLatLng(mMapFragment.getCurrentLatLng());
                dialog.setListener(onSearchResultListener);
                dialog.show();
                break;
            default:
                break;
        }
    }

    private SearchDialog.OnSearchResultListener onSearchResultListener = new SearchDialog.OnSearchResultListener() {
        @Override
        public void getSearchResult(ArrayList<ParkInfo> parkInfos) {
            if (mMapFragment != null) {
                mMapFragment.addMarkers(parkInfos, true);
            }
        }
    };

    private BaseMapFragment.OnGetLocationListener onGetLocationListener = new BaseMapFragment.OnGetLocationListener() {
        @Override
        public void onGetLocation() {
            getGetAroundPark();
        }
    };

    private void getGetAroundPark() {
        if (mMapFragment == null) {
            return;
        }
        LatLng latLng = mMapFragment.getCurrentLatLng();
        ParkSearchRequest request = new ParkSearchRequest(ParkSearchRequest.TYPE_RECOMMEND, ParkSearchRequest.MEMBER_ALL,
                latLng.longitude, latLng.latitude, null);
        request.start(new APIResponseHandler<ParkSearchResponse>() {
            @Override
            public void handleError(String errorCode, String errorMessage) {

            }

            @Override
            public void handleResponse(ParkSearchResponse response) {
                if (getActivity() == null || mMapFragment == null) {
                    return;
                }
                mMapFragment.addMarkers(response.getParkInfos(),false);
            }
        });
    }
}

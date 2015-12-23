package me.wztc.fragment.map;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.SupportMapFragment;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.wztc.R;
import me.wztc.api.request.park.ParkSearchRequest;
import me.wztc.model.ParkInfo;

import java.util.ArrayList;

import me.wztc.util.AppConstants;
import me.wztc.util.AppUtils;
import me.wztc.util.LocalDataBuffer;
import me.wztc.util.Logger;
import me.wztc.util.TimeUtil;

public class BaseMapFragment extends SupportMapFragment {
    private static final String APP_FOLDER_NAME = "parking";
    private static final String COOR_TYPE = "bd09ll";//百度定位SDK可以返回三种坐标系，分别是bd09, bd09ll和gcj02，其中bd09ll能无偏差地显示在百度地图上。
    private static final int WHAT_UPDATE_LOCATION = 0x1;
//    private static final int DELAY_UPDATE_LOCATION = 3 * 1000;

    public static final float DEFAULT_ZOOM_LEVEL = 16f;
    public static final float ZOOM_LEVEL_BIG = 18f;
    public static final int BASE_Y_OFFSET = 18;
    private float zoomLevel = DEFAULT_ZOOM_LEVEL;
    private MapView mMapView;
    private BaiduMap mBaiduMap;

    private LocationClient mLocationClient = null;

    private boolean needMoveToCurrent = false;//判断是否需要回到定位位置的标志
    private BDLocation mCurrentLocation;//当前定位位置
    private LatLng mMapCenter;//当前地图中心位置

    private OnGetLocationListener listener;

    private float density;
    private String mSDCardPath = null;

    public void setOnGetLocationListener(OnGetLocationListener listener) {
        this.listener = listener;
    }

    public String getCity() {
        String city = null;

        if (mCurrentLocation != null) {
            if (!AppUtils.isEmpty(mCurrentLocation.getCity())) {
                city = mCurrentLocation.getCity();
            } else {
                city = mCurrentLocation.getProvince();
            }
        }
        return city;
    }

    public String getDistrict() {
        String district = null;
        if (mCurrentLocation != null) {
            district = mCurrentLocation.getDistrict();
        }

        return district;
    }

    public String getStreet() {
        String street = null;
        if (mCurrentLocation != null) {
            street = mCurrentLocation.getStreet();
        }

        return street;
    }

    public String getStreetNo() {
        String streetNo = null;
        if (mCurrentLocation != null) {
            streetNo = mCurrentLocation.getStreetNumber();
        }

        return streetNo;
    }

    public LatLng getCurrentLatLng() {
        if (mCurrentLocation == null) {
            return null;
        }
        return new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        needMoveToCurrent = true;
        density = getResources().getDisplayMetrics().density;
    }

    @Override
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        mMapView = getMapView();
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMyLocationEnabled(true);
        mBaiduMap.setOnMarkerClickListener(onMarkerClickListener);
        mBaiduMap.setOnMapStatusChangeListener(onMapStatusChangeListener);
        mBaiduMap.setOnMapLoadedCallback(onMapLoadedCallback);

        // 设置定位图层的配置（定位模式，是否允许方向信息，用户自定义定位图标）
        BitmapDescriptor mCurrentMarker = BitmapDescriptorFactory.fromResource(R.drawable.icon_landing_arrow);
        MyLocationConfiguration config = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL, true,
                mCurrentMarker);
        mBaiduMap.setMyLocationConfigeration(config);

        UiSettings settings = mBaiduMap.getUiSettings();
        settings.setCompassEnabled(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);// 设置定位模式
        option.setIsNeedAddress(true);
        // 国测局经纬度坐标系：gcj02
        // 百度墨卡托坐标系 ：bd09
        // 百度经纬度坐标系 ：bd09ll
        option.setCoorType(COOR_TYPE);
        mLocationClient = new LocationClient(getActivity()); // 声明LocationClient类
        mLocationClient.setLocOption(option);
        mLocationClient.registerLocationListener(myListener); // 注册监听函数
        mLocationClient.start();

        requestLocation(DEFAULT_ZOOM_LEVEL);
    }

    @Override
    public void onStop() {
        super.onStop();
        mLocationClient.stop();
        mLocationClient.unRegisterLocationListener(myListener);
        mLocationClient = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    public void requestLocation(float zoomLevel) {
        this.zoomLevel = zoomLevel;
        needMoveToCurrent = true;
        locationUpdateHandler.sendEmptyMessage(WHAT_UPDATE_LOCATION);

    }

    public void moveToCurrentLocation() {
        if (mBaiduMap == null) {
            return;
        }
        if (mCurrentLocation != null) {
            LatLng latLng = getCurrentLatLng();
            MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(latLng);
            mBaiduMap.animateMapStatus(u);
        } else {
            requestLocation(zoomLevel);
        }
    }

    private BDLocationListener myListener = new BDLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {

            if (bdLocation == null)
                return;
            Editor editor = getActivity().getSharedPreferences("config", Context.MODE_APPEND).edit();
            editor.putString("latitude", "" + bdLocation.getLatitude());
            editor.putString("longitude", "" + bdLocation.getLongitude());
            editor.apply();
            StringBuffer sb = new StringBuffer(256);
            sb.append("time : ");
            sb.append(bdLocation.getTime());
            sb.append("\nerror code : ");
            sb.append(bdLocation.getLocType());
            sb.append("\nlatitude : ");
            sb.append(bdLocation.getLatitude());
            sb.append("\nlontitude : ");
            sb.append(bdLocation.getLongitude());
            sb.append("\nradius : ");
            sb.append(bdLocation.getRadius());
            if (bdLocation.getLocType() == BDLocation.TypeGpsLocation) {
                sb.append("\nspeed : ");
                sb.append(bdLocation.getSpeed());
                sb.append("\nsatellite : ");
                sb.append(bdLocation.getSatelliteNumber());
            } else if (bdLocation.getLocType() == BDLocation.TypeNetWorkLocation) {
                sb.append("\naddr : ");
                sb.append(bdLocation.getAddrStr());
            }

            Logger.logI("GEO", sb.toString());

            if (bdLocation.getLocType() != BDLocation.TypeGpsLocation
                    && bdLocation.getLocType() != BDLocation.TypeNetWorkLocation) {
                // 定位出错
                return;
            }

            onLocationGet(bdLocation);
        }
    };

    /**
     * 获取location后处理
     *
     * @param location
     */
    private void onLocationGet(BDLocation location) {
        if (mCurrentLocation == null) {
            mCurrentLocation = location;
            LocalDataBuffer.getInstance().setLocation(mCurrentLocation);
            if (listener != null) {
                listener.onGetLocation();
            }
        } else {
            // 上一次定位时间
            long oldL = TimeUtil.parseTime(mCurrentLocation.getTime(), AppConstants.TIME_FORMAT);
            // 本次定位时间
            long newL = TimeUtil.parseTime(location.getTime(), AppConstants.TIME_FORMAT);

            if (newL > oldL) {
                // 新的location
                mCurrentLocation = location;
                LocalDataBuffer.getInstance().setLocation(mCurrentLocation);
                if (listener != null) {
                    listener.onGetLocation();
                }
            }
        }
        if (needMoveToCurrent) {
            LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
            MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(ll, zoomLevel);
            mBaiduMap.animateMapStatus(u);
            needMoveToCurrent = false;
        }

        // 构造定位数据
        MyLocationData locData = new MyLocationData.Builder().accuracy(location.getRadius())
                .latitude(location.getLatitude()).longitude(location.getLongitude()).build();
        // 设置定位数据
        mBaiduMap.setMyLocationData(locData);

    }

    private Handler locationUpdateHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
            case WHAT_UPDATE_LOCATION:
                if (mLocationClient != null && mLocationClient.isStarted()) {
                    mLocationClient.requestLocation();
                }
                break;
            }

        }
    };

    public void addMarkers(ArrayList<ParkInfo> parkInfos, boolean needUpdateMapPosition) {
        if (parkInfos == null || parkInfos.size() == 0) {
            return;
        }
        mBaiduMap.clear();
        LatLngBounds.Builder bounds = null;
        if (needUpdateMapPosition) {
            bounds = new LatLngBounds.Builder();
        }
        for (int i = 0; i < parkInfos.size(); i++) {
            ParkInfo parkInfo = parkInfos.get(i);

            LatLng latLng = new LatLng(parkInfo.getLat(), parkInfo.getLng());
            if (needUpdateMapPosition) {
                bounds.include(latLng);
            }
            //构建Marker图标
            BitmapDescriptor bitmap;
            if (parkInfo.getType().equals(ParkSearchRequest.TYPE_RECOMMEND)) {
                bitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_marker_orange);
            } else {
                bitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_marker_white);
            }
            //构建Bundle数据
            Bundle bundle = new Bundle();
            bundle.putParcelable(AppConstants.BUNDLE_PARK_INFO, parkInfo);
            //构建MarkerOption，用于在地图上添加Marker
            OverlayOptions option = new MarkerOptions().position(latLng).icon(bitmap).extraInfo(bundle);
            //在地图上添加Marker，并显示
            mBaiduMap.addOverlay(option);
        }
        if (needUpdateMapPosition) {
            MapStatusUpdate update = MapStatusUpdateFactory.newLatLngBounds(bounds.build());
            mBaiduMap.animateMapStatus(update);
        }

    }

    private BaiduMap.OnMarkerClickListener onMarkerClickListener = new BaiduMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(Marker marker) {
            MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(marker.getPosition());
            mBaiduMap.animateMapStatus(u);
            Bundle bundle = marker.getExtraInfo();
            if (bundle.containsKey(AppConstants.BUNDLE_PARK_INFO)) {
                final ParkInfo parkInfo = bundle.getParcelable(AppConstants.BUNDLE_PARK_INFO);

                LayoutInflater inflater = LayoutInflater.from(getActivity());
                View view = inflater.inflate(R.layout.layout_park_item, null);
                TextView name = (TextView) view.findViewById(R.id.home_parking_name);
                TextView address = (TextView) view.findViewById(R.id.home_parking_address);
                TextView go = (TextView) view.findViewById(R.id.home_parking_go);
                go.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        naviTo(parkInfo.getParkName(), new LatLng(parkInfo.getLat(), parkInfo.getLng()));
                    }
                });
                name.setText(parkInfo.getParkName());
                address.setText(parkInfo.getAddress());
                InfoWindow infoWindow = new InfoWindow(view, marker.getPosition(), (int) (-density * BASE_Y_OFFSET));
                mBaiduMap.showInfoWindow(infoWindow);
                return true;
            }
            return false;
        }
    };

    private BaiduMap.OnMapStatusChangeListener onMapStatusChangeListener = new BaiduMap.OnMapStatusChangeListener() {
        @Override
        public void onMapStatusChangeStart(MapStatus mapStatus) {

        }

        @Override
        public void onMapStatusChange(MapStatus mapStatus) {
        }

        @Override
        public void onMapStatusChangeFinish(MapStatus mapStatus) {
            mMapCenter = mapStatus.target;
        }
    };

    private BaiduMap.OnMapLoadedCallback onMapLoadedCallback = new BaiduMap.OnMapLoadedCallback() {
        @Override
        public void onMapLoaded() {
            if (mMapCenter != null) {
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(mMapCenter);
                mBaiduMap.animateMapStatus(u);
            }
        }
    };

//    private void naviTo(String name, LatLng latLng) {
//        if (initDirs()) {
//            initNavi(name, latLng);
//        }
//    }

//    private boolean initDirs() {
//        mSDCardPath = getSdcardDir();
//        if (mSDCardPath == null) {
//            return false;
//        }
//        File f = new File(mSDCardPath, APP_FOLDER_NAME);
//        if (!f.exists()) {
//            try {
//                f.mkdir();
//            } catch (Exception e) {
//                e.printStackTrace();
//                return false;
//            }
//        }
//        return true;
//    }

    private void initNavi(final String name,final LatLng latLng) {
////        BaiduNaviManager.getInstance().setNativeLibraryPath(mSDCardPath + "/BaiduNaviSDK_SO");
//        BaiduNaviManager.getInstance().init(getActivity(), mSDCardPath, APP_FOLDER_NAME,
//                new BaiduNaviManager.NaviInitListener() {
//                    @Override
//                    public void onAuthResult(int status, String msg) {
//                        Logger.logE("","onAuthResult");
//                        if (0 == status) {
////                            authinfo = "key校验成功!";
//                        } else {
////                            authinfo = "key校验失败, " + msg;
//                        }
//                    }
//
//                    public void initSuccess() {
//                        BNRoutePlanNode sNode = new BNRoutePlanNode(latLng.longitude, latLng.latitude, name, null);
//                        List<BNRoutePlanNode> list = new ArrayList<BNRoutePlanNode>();
//                        list.add(sNode);
//                        BaiduNaviManager.getInstance().launchNavigator(getActivity(), list, 1, true, new DemoRoutePlanListener(sNode));
//                    }
//
//                    public void initStart() {
//                        Logger.logE("","initStart");
//
//                    }
//
//                    public void initFailed() {
//                        Logger.logE("","initFailed");
//                    }
//                }, null);
    }


//    private String getSdcardDir() {
//        if (Environment.getExternalStorageState().equalsIgnoreCase(
//                Environment.MEDIA_MOUNTED)) {
//            return Environment.getExternalStorageDirectory().toString();
//        }
//        return null;
//    }

//    public class DemoRoutePlanListener implements BaiduNaviManager.RoutePlanListener {
//
//        private BNRoutePlanNode mBNRoutePlanNode = null;
//
//        public DemoRoutePlanListener(BNRoutePlanNode node) {
//            mBNRoutePlanNode = node;
//        }
//
//        @Override
//        public void onJumpToNavigator() {
//            Intent intent = new Intent(getActivity(), NaviGuideActivity.class);
//            Bundle bundle = new Bundle();
//            bundle.putSerializable(NaviGuideActivity.ROUTE_PLAN_NODE, mBNRoutePlanNode);
//            intent.putExtras(bundle);
//            startActivity(intent);
//        }
//
//        @Override
//        public void onRoutePlanFailed() {
//
//        }
//    }

    public interface OnGetLocationListener {
        void onGetLocation();
    }
}

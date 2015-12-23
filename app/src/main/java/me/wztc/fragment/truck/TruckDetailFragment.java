package me.wztc.fragment.truck;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebStorage;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.wztc.R;
import me.wztc.fragment.BaseFragment;
import me.wztc.util.Logger;

@SuppressWarnings("unused")
public class TruckDetailFragment extends BaseFragment {
    private static final String TAG = "TruckDetailFragment";

    private WebView webview;
    public String mURL = "";
    private String titleName;
    private String titleNames;
    private String latitude;
    private String longitude;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trunk_detail, container,
                false);
        init(view);
        return view;
    }

    private void init(View view) {
        webview = (WebView) view.findViewById(R.id.webView1);
        latitude = getActivity().getSharedPreferences("config", Context.MODE_APPEND).getString("latitude", "");
        longitude = getActivity().getSharedPreferences("config", Context.MODE_APPEND).getString("longitude", "");
        Logger.logE(TAG + "---latitude-", latitude);
        SendUrlMethod();

    }

    private void SendUrlMethod() {
        Bundle bundles = getArguments();
        titleName = bundles.getString("insurance");
        titleNames = bundles.getString("driving");
        if (titleName == "insurance") {
            mURL = "http://mchanxian.sinosig.com/car/baseInformation/baseInformation.html?agentCode=W02411076&spsource=&netcampaignno=&accountType=&accountNo=&terminalType=&payType=&s=&b="
                    + "&lon="
                    + longitude
                    + "&lat="
                    + latitude
                    + "&coordtype=bd9011";
        } else if (titleNames == "driving") {
            mURL = "http://h.aidaijia.com/redirect?s=3043e55d523d3d2fb0116ebae63af06a"
                    + "&lon="
                    + longitude
                    + "&lat="
                    + latitude
                    + "&coordtype=bd9011";
        }
        Log.i("URL", "URL:" + mURL);

        WebSettings webSettings = webview.getSettings();

        webSettings.setJavaScriptEnabled(true);

        // 启用数据库
        webSettings.setDatabaseEnabled(true);
        String dir = getActivity().getApplicationContext()
                .getDir("database", Context.MODE_PRIVATE).getPath();
        // 设置定位的数据库路径
        webSettings.setGeolocationDatabasePath(dir);
        webSettings.setDatabasePath(dir);
        webSettings.setDomStorageEnabled(true);
        // 启用地理定位
        webSettings.setGeolocationEnabled(true);

        webview.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onGeolocationPermissionsShowPrompt(String origin,
                                                           GeolocationPermissions.Callback callback) {
                //配置权限（同样在WebChromeClient中实现）
                callback.invoke(origin, true, false);
                super.onGeolocationPermissionsShowPrompt(origin, callback);
            }

            public void onExceededDatabaseQuota(String url,
                                                String databaseIdentifier, long currentQuota,
                                                long estimatedSize, long totalUsedQuota,
                                                WebStorage.QuotaUpdater quotaUpdater) {
                quotaUpdater.updateQuota(estimatedSize * 2);
            }

        });

        //		settings.setJavaScriptEnabled(true);
        //		mWbview.setWebViewClient(new WebViewClient());
        //		mWbview.loadUrl(mURL);
        webview.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        webview.loadUrl(mURL);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // set title
        Bundle bundle = getArguments();
        titleName = bundle.getString("insurance");
        titleNames = bundle.getString("driving");
        if (titleName == "insurance") {
            setTitle(R.string.truck_fragment__insurance);
        } else if (titleNames == "driving") {
            setTitle(R.string.truck_fragment__driving);
        }
    }


    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        webview.removeAllViews();
        webview.clearHistory();
        webview.destroy();
    }
}

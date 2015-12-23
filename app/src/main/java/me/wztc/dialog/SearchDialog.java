package me.wztc.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import com.baidu.mapapi.model.LatLng;
import com.wztc.R;
import me.wztc.adapter.SearchAdapter;
import me.wztc.api.request.park.ParkSearchRequest;
import me.wztc.api.response.APIResponseHandler;
import me.wztc.api.response.park.ParkSearchResponse;
import me.wztc.model.ParkInfo;
import me.wztc.util.AppUtils;
import me.wztc.util.ToastUtil;
import com.pnikosis.materialishprogress.ProgressWheel;

import java.util.ArrayList;

public class SearchDialog extends BaseDialog implements View.OnClickListener {

    private boolean isProgress;

    private EditText keywordEv;

    private SearchAdapter adapter;
    private LatLng latLng;

    private ListView listView;
    private ProgressWheel progressWheel;

    private OnSearchResultListener listener;

    public SearchDialog(Context context) {
        super(context);
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public void setListener(OnSearchResultListener listener) {
        this.listener = listener;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_search_dialog);
        findViewById(R.id.header_back).setOnClickListener(this);
        findViewById(R.id.home_search_tv).setOnClickListener(this);
        listView = (ListView) findViewById(R.id.home_search_history_list);
        progressWheel = (ProgressWheel) findViewById(R.id.home_search_progress);

        keywordEv = (EditText) findViewById(R.id.home_search_et);
//        keywordEv.addTextChangedListener(textWatcher);
        keywordEv.setOnEditorActionListener(onEditorActionListener);

        if (adapter == null) {
            adapter = new SearchAdapter();
        }

        ListView listView = (ListView) findViewById(R.id.home_search_history_list);
        listView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.header_back:
            dismiss();
            break;
        case R.id.home_search_tv:
            String keyword = keywordEv.getText().toString().trim();
            if (AppUtils.isEmpty(keyword)) {
                return;
            }
            AppUtils.hideSoftKb(getContext(), keywordEv);

            search(keyword);
            break;
        }
    }

    private void search(String keyword) {

        if (latLng == null) {
            return;
        }
        if (isProgress) {
            return;
        }
        isProgress = true;

        listView.setVisibility(View.GONE);
        progressWheel.setVisibility(View.VISIBLE);
        ParkSearchRequest request = new ParkSearchRequest(ParkSearchRequest.TYPE_NOT_RECOMMEND, ParkSearchRequest.MEMBER_ALL,
                latLng.longitude, latLng.latitude, keyword);
        request.start(new APIResponseHandler<ParkSearchResponse>() {
            @Override
            public void handleError(String errorCode, String errorMessage) {
                isProgress = false;
                listView.setVisibility(View.VISIBLE);
                progressWheel.setVisibility(View.GONE);
                ToastUtil.toastShort(getContext(), errorMessage);
            }

            @Override
            public void handleResponse(ParkSearchResponse response) {
                isProgress = false;
                listView.setVisibility(View.VISIBLE);
                progressWheel.setVisibility(View.GONE);
                ArrayList<ParkInfo> parkInfos = response.getParkInfos();
                if (parkInfos == null || parkInfos.size() == 0) {
                    //无搜索结果
                    ToastUtil.toastShort(getContext(), R.string.home_search_no_result);
                    return;
                }
                if (listener != null) {
                    listener.getSearchResult(parkInfos);
                    dismiss();
                }
            }
        });
    }

//    private TextWatcher textWatcher = new TextWatcher() {
//        @Override
//        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//        }
//
//        @Override
//        public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            String keyword = s.toString();
//            if (AppUtils.isEmpty(keyword)) {
//                return;
//            }
//
//        }
//
//        @Override
//        public void afterTextChanged(Editable s) {
//
//        }
//    };

    private TextView.OnEditorActionListener onEditorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                String keyword = v.getText().toString();
                if (AppUtils.isEmpty(keyword)) {
                    return false;
                }

                search(keyword);
                return true;
            }
            return false;
        }
    };

    public interface OnSearchResultListener {
        void getSearchResult(ArrayList<ParkInfo> parkInfos);
    }
}

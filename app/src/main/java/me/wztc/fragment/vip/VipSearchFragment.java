package me.wztc.fragment.vip;

import android.os.Bundle;
import android.view.*;
import android.view.inputmethod.EditorInfo;
import android.widget.*;
import com.baidu.location.BDLocation;
import com.baidu.mapapi.model.LatLng;
import com.wztc.R;
import me.wztc.adapter.SearchAdapter;
import me.wztc.api.request.park.ParkSearchRequest;
import me.wztc.api.response.APIResponseHandler;
import me.wztc.api.response.park.ParkSearchResponse;
import me.wztc.fragment.BaseFragment;
import me.wztc.model.ParkInfo;
import me.wztc.util.AppUtils;
import me.wztc.util.LocalDataBuffer;
import me.wztc.util.ToastUtil;
import com.pnikosis.materialishprogress.ProgressWheel;

import java.util.ArrayList;

/**
 * 会员卡搜索画面
 */
public class VipSearchFragment extends BaseFragment {
    private static final int PAGE_SIZE = 3;
    private boolean isProgress;

    private EditText keywordEv;

    private SearchAdapter adapter;
    private LatLng latLng;

    private ListView listView;
    private TextView footer;
    private ProgressWheel progressWheel;

    private String parkType = ParkSearchRequest.TYPE_RECOMMEND;


    private ArrayList<ParkInfo> parkInfos;
    private int index;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vip_search, container, false);
        view.findViewById(R.id.vip_search_tv).setOnClickListener(this);
        listView = (ListView) view.findViewById(R.id.vip_search_list);
        //add footer
        footer = new TextView(getActivity());
        footer.setText(R.string.vip_search_fragment_next);
        footer.setTextSize(getResources().getDimensionPixelSize(R.dimen.base_text_size_medium));
        footer.setLayoutParams(new ListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        footer.setPadding(8, 8, 8, 8);
        footer.setGravity(Gravity.CENTER);
        footer.setOnClickListener(onNextClickListener);
        listView.addFooterView(footer);
        progressWheel = (ProgressWheel) view.findViewById(R.id.vip_search_progress);

        keywordEv = (EditText) view.findViewById(R.id.vip_search_keyword);
        keywordEv.setOnEditorActionListener(onEditorActionListener);

        //停车场类型RadioGroup
        RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.vip_search_park_rg);
        radioGroup.setOnCheckedChangeListener(onCheckedChangeListener);
        if (adapter == null) {
            adapter = new SearchAdapter();
        }

        listView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        BDLocation location = LocalDataBuffer.getInstance().getLocation();
        if (location != null) {
            latLng = new LatLng(location.getLatitude(), location.getLongitude());
        }
        if (!isInit) {
            search(null);
            isInit = true;
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
        case R.id.vip_search_tv:
            String keyword = keywordEv.getText().toString().trim();

            AppUtils.hideSoftKb(getActivity(), keywordEv);

            search(keyword);
            break;
        }
    }

    private RadioGroup.OnCheckedChangeListener onCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
            case R.id.vip_search_recommend:
                parkType = ParkSearchRequest.TYPE_RECOMMEND;
                break;
            case R.id.vip_search_around:
                parkType = ParkSearchRequest.TYPE_NOT_RECOMMEND;
                break;
            }
        }
    };

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
        index = 0;
        ParkSearchRequest request = new ParkSearchRequest(parkType, ParkSearchRequest.MEMBER_ALL, latLng.longitude,
                latLng.latitude, keyword);
        request.start(new APIResponseHandler<ParkSearchResponse>() {
            @Override
            public void handleError(String errorCode, String errorMessage) {
                isProgress = false;
                listView.setVisibility(View.VISIBLE);
                progressWheel.setVisibility(View.GONE);
                ToastUtil.toastShort(getActivity(), errorMessage);
            }

            @Override
            public void handleResponse(ParkSearchResponse response) {
                isProgress = false;
                listView.setVisibility(View.VISIBLE);
                progressWheel.setVisibility(View.GONE);
                parkInfos = response.getParkInfos();
                if (parkInfos == null || parkInfos.size() == 0) {
                    //无搜索结果
                    ToastUtil.toastShort(getActivity(), R.string.home_search_no_result);
                    return;
                }

                setPage();
            }
        });
    }

    private void setPage() {
        if (parkInfos == null) {
            return;
        }

        ArrayList<ParkInfo> temp = new ArrayList<>();
        if ((index + 1) * PAGE_SIZE <= parkInfos.size()) {
            temp.add(parkInfos.get(index * PAGE_SIZE));
            temp.add(parkInfos.get(index * PAGE_SIZE + 1));
            temp.add(parkInfos.get(index * PAGE_SIZE + 2));
            footer.setText(R.string.vip_search_fragment_next);
            footer.setOnClickListener(onNextClickListener);
        } else {
            int first = index * PAGE_SIZE;
            int least = parkInfos.size();
            for (int i = first; i < least; i++) {
                temp.add(parkInfos.get(i));
            }
            footer.setText(R.string.vip_search_fragment_no_more);
            footer.setOnClickListener(null);

        }
        adapter.setParkInfos(temp);
        adapter.notifyDataSetChanged();
    }

    private View.OnClickListener onNextClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            index++;
            setPage();
        }
    };

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

}

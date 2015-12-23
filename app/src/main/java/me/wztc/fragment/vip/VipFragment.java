package me.wztc.fragment.vip;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.wztc.R;
import me.wztc.adapter.VipAdapter;
import me.wztc.fragment.BaseFragment;
import me.wztc.model.Vip;
import me.wztc.notification.Notification;
import me.wztc.util.AppConstants;

/**
 * 会员卡画面
 */
public class VipFragment extends BaseFragment {
    private ListView listView;
    private VipAdapter adapter;

    private TextView noVipTv;

    @SuppressLint("InflateParams")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_vip, container, false);
        View childView = inflater.inflate(R.layout.layout_vip_listview_footer, null);
        childView.findViewById(R.id.vip_fragment_add_car).setOnClickListener(this);
        initView(view);

        if (adapter == null) {
            adapter = new VipAdapter();
        }

        if (getVipData().size() > 0) {
            listView.setVisibility(View.VISIBLE);
            noVipTv.setVisibility(View.GONE);
            adapter.setVips(getVipData());
        } else {
            listView.setVisibility(View.GONE);
            noVipTv.setVisibility(View.VISIBLE);
        }

        adapter.setVipListListener(vipListListener);
        listView.addFooterView(childView);
        listView.setAdapter(adapter);
        return view;
    }

    private void initView(View view) {
        listView = (ListView) view.findViewById(R.id.vip_fragment_listview);
        noVipTv = (TextView) view.findViewById(R.id.vip_fragment_no_vip);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setTitle(R.string.vip_fragment_title);
    }

    private ArrayList<Vip> getVipData() {
        ArrayList<Vip> vips = new ArrayList<Vip>();
        Vip vip1 = new Vip();
        vip1.setMarketName("百联西郊购物广场1");
        vip1.setVipType("月卡");
        vip1.setCarNumber("沪88888888");
        vip1.setVipValidDate(611329651120100l);

        Vip vip2 = new Vip();
        vip2.setMarketName("百联西郊购物广场2");
        vip2.setVipType("月卡");
        vip2.setCarNumber("沪7777777");
        vip2.setVipValidDate(622212010l);

        Vip vip3 = new Vip();
        vip3.setMarketName("百联西郊购物广场3");
        vip3.setVipType("月卡");
        vip3.setCarNumber("沪66666666");
        vip3.setVipValidDate(633320100000l);

        Vip vip4 = new Vip();
        vip4.setMarketName("百联西郊购物广场4");
        vip4.setVipType("月卡");
        vip4.setCarNumber("沪555555");
        vip4.setVipValidDate(644420100000l);

        vips.add(vip1);
        vips.add(vip2);
        vips.add(vip3);
        vips.add(vip4);
        vips.add(vip1);
        vips.add(vip2);
        vips.add(vip3);
        vips.add(vip4);
        return vips;
    }

    private VipAdapter.VipListListener vipListListener = new VipAdapter.VipListListener() {

        @Override
        public void goVipDetails(Vip vip) {
            VipDetailslFragment vipDetailslFragment = new VipDetailslFragment();

            Bundle bundle = new Bundle();
            bundle.putParcelable(AppConstants.BUNDLE_VIP, vip);
            mCenter.postNotification(new Notification(AppConstants.NOTIFICATION_ADD_FRAGMENT, vipDetailslFragment));
        }
    };

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
        case R.id.vip_fragment_add_car:
            mCenter.postNotification(new Notification(AppConstants.NOTIFICATION_ADD_FRAGMENT, new VipSearchFragment()));
            break;
        }
    }
}

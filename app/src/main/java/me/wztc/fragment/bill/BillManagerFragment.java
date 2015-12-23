package me.wztc.fragment.bill;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import android.widget.TextView;
import com.wztc.R;
import me.wztc.adapter.BillAdapter;
import me.wztc.fragment.BaseFragment;
import me.wztc.model.Bill;
import me.wztc.notification.Notification;
import me.wztc.util.AppConstants;

public class BillManagerFragment extends BaseFragment {

    private ListView listView;
    private TextView noDataTv;
    private BillAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bill_manager, null);  // View android.view.LayoutInflater.inflate(int resource, ViewGroup root)
        initData(view);

        if (adapter == null) {
            adapter = new BillAdapter();
        }

        if (getBills().size() > 0) {
            listView.setVisibility(View.VISIBLE);
            noDataTv.setVisibility(View.GONE);
            adapter.setBills(getBills());
        } else {
            listView.setVisibility(View.GONE);
            noDataTv.setVisibility(View.VISIBLE);
        }
        adapter.setBillListListener(billListListener);
        listView.setAdapter(adapter);
        return view;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // set title
        setTitle(R.string.bill_manager_fragment_title);
    }

    private void initData(View view) {
        listView = (ListView) view.findViewById(R.id.bill_manager_fragmnet_listview);
        noDataTv = (TextView) view.findViewById(R.id.bill_manager_fragment_no_vip);
    }


    private ArrayList<Bill> getBills() {
        ArrayList<Bill> bills = new ArrayList<Bill>();

        Bill bill = new Bill();
        bill.setParkName("百联西郊购物广场");
        bill.setFee("21");
        bill.setDelayTime("2015-08-15 15:30:22");

        Bill bill2 = new Bill();
        bill2.setParkName("百联西郊购物广场");
        bill2.setFee("45");
        bill2.setDelayTime("2015-08-17 18:43:26");

        Bill bill3 = new Bill();
        bill3.setParkName("百联西郊购物广场");
        bill3.setFee("23");
        bill3.setDelayTime("2015-08-18 19:50:43");

        bills.add(bill);
        bills.add(bill2);
        bills.add(bill3);
        return bills;
    }

    private BillAdapter.BillListListener billListListener = new BillAdapter.BillListListener() {
        @Override
        public void goBillDetails(Bill bill) {
            BillDetailFragment billDetailFragment = new BillDetailFragment();

            Bundle bundle = new Bundle();
            bundle.putParcelable(AppConstants.BUNDLE_PARK_BILL, bill);
            mCenter.postNotification(new Notification(AppConstants.NOTIFICATION_ADD_FRAGMENT, billDetailFragment));
        }
    };

    @Override
    public void onPause() {
        super.onPause();
    }
}
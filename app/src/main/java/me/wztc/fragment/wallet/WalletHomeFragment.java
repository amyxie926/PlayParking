package me.wztc.fragment.wallet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import com.wztc.R;
import me.wztc.adapter.WalletBillAdapter;
import me.wztc.fragment.BaseFragment;
import me.wztc.fragment.bill.BillDetailFragment;
import me.wztc.fragment.recharge.RechargeFragment;
import me.wztc.model.Bill;
import me.wztc.model.User;
import me.wztc.notification.Notification;
import me.wztc.util.AppConstants;
import me.wztc.util.AppUtils;
import me.wztc.util.LocalDataBuffer;

import java.util.ArrayList;

/**
 * 钱包首页
 */
public class WalletHomeFragment extends BaseFragment {

    private WalletBillAdapter adapter;

    private TextView moneyTv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wallet_home, container, false);

        moneyTv = (TextView) view.findViewById(R.id.wallet_home_fragment_money);

        ListView listView = (ListView) view.findViewById(R.id.wallet_home_fragment_list);

        listView.setOnItemClickListener(onItemClickListener);
        if (adapter == null) {
            adapter = new WalletBillAdapter();
        }
        listView.setAdapter(adapter);
        view.findViewById(R.id.wallet_home_fragment_pay).setOnClickListener(this);

        view.findViewById(R.id.wallet_home_fragment_current_bill).setOnClickListener(this);
        view.findViewById(R.id.wallet_home_fragment_bill_history).setOnClickListener(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setTitle(R.string.wallet_home_fragment_title);
        getBill();
        User user = LocalDataBuffer.getInstance().getUser();
        if (user != null) {
            String money = user.getMoney();
            if (!AppUtils.isEmpty(money)) {
                if (money.contains(".")) {
                    int index = money.indexOf(".");
                    if (index > 0) {
                        moneyTv.setText(money.substring(0, index - 2));
                    }
                } else {
                    moneyTv.setText(money.substring(0, money.length() - 2));
                }
            }
        }
    }

    private void getBill() {
        ArrayList<Bill> bills = new ArrayList<>();

        bills.add(new Bill());
        bills.add(new Bill());
        bills.add(new Bill());
        bills.add(new Bill());
        adapter.setBills(bills);
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
        case R.id.wallet_home_fragment_pay:
            mCenter.postNotification(
                    new Notification(AppConstants.NOTIFICATION_ADD_FRAGMENT, new RechargeFragment()));
            break;
        default:
            break;
        }
    }

    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mCenter.postNotification(
                    new Notification(AppConstants.NOTIFICATION_ADD_FRAGMENT, new BillDetailFragment()));
        }
    };
}

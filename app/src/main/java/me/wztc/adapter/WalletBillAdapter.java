package me.wztc.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.wztc.R;
import me.wztc.model.Bill;

import java.util.ArrayList;

public class WalletBillAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private ArrayList<Bill> bills;

    public void setBills(ArrayList<Bill> bills) {
        this.bills = bills;
    }

    @Override
    public int getCount() {
        if (bills == null)
            return 0;
        else
            return bills.size();
    }

    @Override
    public Bill getItem(int position) {
        return bills.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder;
        if (view == null) {

            if (inflater == null) {
                inflater = LayoutInflater.from(parent.getContext());
            }

            view = inflater.inflate(R.layout.layout_wallet_bill_item, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        return view;
    }

    private class ViewHolder {
        public ViewHolder(View itemView) {
        }
    }
}

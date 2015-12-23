package me.wztc.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.wztc.R;
import me.wztc.model.Bill;

import java.util.ArrayList;

public class BillAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private ArrayList<Bill> bills;
    private BillListListener billListListener;

    public void setBills(ArrayList<Bill> bills) {
        this.bills = bills;
    }

    public void setBillListListener(BillListListener billListListener) {
        this.billListListener = billListListener;
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

            view = inflater.inflate(R.layout.layout_bill_item, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        final Bill bill = getItem(position);
        holder.parkNameTv.setText(bill.getParkName());

        String totalFee = String.format(parent.getContext().getString(R.string.bill_manager_fragment_total_fee), bill.getFee());
        holder.totalFeeTv.setText(totalFee);

        String payDate = String.format(parent.getContext().getString(R.string.bill_manager_fragment_pay_date), bill.getDelayTime());
        holder.payDateTv.setText(payDate);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (billListListener != null) {
                    billListListener.goBillDetails(bill);
                }
            }
        });
        return view;
    }

    private class ViewHolder {
        TextView parkNameTv;
        TextView totalFeeTv;
        TextView payDateTv;

        public ViewHolder(View itemView) {
            parkNameTv = (TextView) itemView.findViewById(R.id.bill_item_layout_parkName);
            totalFeeTv = (TextView) itemView.findViewById(R.id.bill_item_layout_total_fee);
            payDateTv = (TextView) itemView.findViewById(R.id.bill_item_layout_pay_date);
        }
    }

    public interface BillListListener {
        void goBillDetails(Bill bill);
    }
}

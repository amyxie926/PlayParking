package me.wztc.adapter;

import java.util.ArrayList;
import java.util.Calendar;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wztc.R;
import me.wztc.model.Vip;
import me.wztc.util.AppConstants;
import me.wztc.util.TimeUtil;

public class VipAdapter extends BaseAdapter {
    private ArrayList<Vip> vips;
    private VipListListener vipListListener;

    public VipAdapter() {
    }

    public void setVips(ArrayList<Vip> vips) {
        this.vips = vips;
    }

    public void setVipListListener(VipListListener vipListListener) {
        this.vipListListener = vipListListener;
    }

    @Override
    public int getCount() {
        return vips.size();
    }

    @Override
    public Vip getItem(int position) {
        return vips.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MembershipCarViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(parent.getContext(), R.layout.layout_vip, null);
            holder = new MembershipCarViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (MembershipCarViewHolder) convertView.getTag();
        }

        final Vip vip = getItem(position);

        holder.marketNameTv.setText(vip.getMarketName());
        holder.vipTypeTv.setText(vip.getVipType());
        holder.carNumberTv.setText(vip.getCarNumber());
        String vipValidDate = String.format(parent.getContext().getString(R.string.vip_fragment_vip_validDate),
                TimeUtil.formatTime(vip.getVipValidDate(), AppConstants.TIME_FORMAT_SHORT));
        holder.vipValidDateTv.setText(vipValidDate);

        long currentTime = Calendar.getInstance().getTimeInMillis();// 当前系统时间
        if (currentTime > vip.getVipValidDate()) {// 判定会员卡是否失效
            holder.vipLayout.setBackgroundResource(R.drawable.icon_vip_invalid_bg);
        } else {
            holder.vipLayout.setBackgroundResource(R.drawable.icon_vip_bg);
            convertView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (vipListListener != null) {
                        vipListListener.goVipDetails(vip);
                    }
                }
            });
        }


        return convertView;

    }

    public class MembershipCarViewHolder {
        LinearLayout vipLayout;
        TextView marketNameTv;
        TextView vipTypeTv;
        TextView carNumberTv;
        TextView vipValidDateTv;

        public MembershipCarViewHolder(View view) {
            vipLayout = (LinearLayout) view.findViewById(R.id.layout_vip_layout);
            marketNameTv = (TextView) view.findViewById(R.id.layout_marketname_tv);
            vipTypeTv = (TextView) view.findViewById(R.id.layout_vip_type_tv);
            carNumberTv = (TextView) view.findViewById(R.id.layout_car_number_tv);
            vipValidDateTv = (TextView) view.findViewById(R.id.layout_vip_valid_date_tv);

        }
    }

    public interface VipListListener {
        void goVipDetails(Vip vip);
    }
}

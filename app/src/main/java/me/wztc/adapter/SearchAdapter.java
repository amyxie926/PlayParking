package me.wztc.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.wztc.R;
import me.wztc.model.ParkInfo;

import java.util.ArrayList;

public class SearchAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private ArrayList<ParkInfo> parkInfos;

    public void setParkInfos(ArrayList<ParkInfo> parkInfos) {
        this.parkInfos = parkInfos;
    }

    @Override
    public int getCount() {
        if (parkInfos == null)
            return 0;
        else
            return parkInfos.size();
    }

    @Override
    public ParkInfo getItem(int position) {
        return parkInfos.get(position);
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
            view = inflater.inflate(R.layout.layout_vip_search_item, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        ParkInfo parkInfo = getItem(position);
        holder.name.setText(parkInfo.getParkName());

        return view;
    }

    private class ViewHolder {
        TextView name;

        public ViewHolder(View item) {
            name = (TextView) item.findViewById(R.id.vip_search_item_name);
        }
    }
}

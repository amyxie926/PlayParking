package me.wztc.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.wztc.R;

/**
 * @author Administrator
 *
 */
public class MyGridViewAdapter extends BaseAdapter {
    public interface MyGridViewAdapterCallBack {
        public void toSecondFragment(String area, int position);
    };

    private Context context;
    private List<String> datas;
    private MyGridViewAdapterCallBack callBack;

    public MyGridViewAdapter(Context context, List<String> datas) {
        this.context = context;
        this.datas = datas;
    }

    public void setCallBack(MyGridViewAdapterCallBack callBack) {
        this.callBack = callBack;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        MyGridViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.fragment_carmanager_gv_item, null);
            holder = new MyGridViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (MyGridViewHolder) convertView.getTag();
        }
        holder.bt_frag_carmanager_item.setText(datas.get(position));

        holder.bt_frag_carmanager_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.toSecondFragment(datas.get(position), position);
            }
        });
        return convertView;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public class MyGridViewHolder {
        private Button bt_frag_carmanager_item;

        public MyGridViewHolder(View holder) {
            bt_frag_carmanager_item = (Button) holder.findViewById(R.id.bt_frag_carmanager_item);
        }
    }
}

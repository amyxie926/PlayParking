package me.wztc.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.wztc.R;
import me.wztc.model.IntegeraExchange;

import java.util.ArrayList;

public class IntegeraExchangeAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private ArrayList<IntegeraExchange> integeraExchanges;
    private IntegeraExchangeClickListener integeraExchangeClickListener;

    public void setIntegeraExchanges(ArrayList<IntegeraExchange> integeraExchanges) {
        this.integeraExchanges = integeraExchanges;
    }

    public void setIntegeraExchangeClickListener(IntegeraExchangeClickListener integeraExchangeClickListener) {
        this.integeraExchangeClickListener = integeraExchangeClickListener;
    }

    @Override
    public int getCount() {
        if (integeraExchanges == null)
            return 0;
        else
            return integeraExchanges.size();
    }

    @Override
    public IntegeraExchange getItem(int position) {
        return integeraExchanges.get(position);
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

            view = inflater.inflate(R.layout.layout_integera_exchange, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

      final  IntegeraExchange integeraExchange = getItem(position);

        holder.nameTv.setText(integeraExchange.getName());
        holder.dateTv.setText(integeraExchange.getDate());
        holder.infoTv.setText(integeraExchange.getInfo());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (integeraExchangeClickListener != null) {
                    integeraExchangeClickListener.onClick(integeraExchange);
                }
            }
        });
        return view;
    }

    private class ViewHolder {
        TextView nameTv;
        TextView dateTv;
        TextView infoTv;

        public ViewHolder(View itemView) {
            nameTv = (TextView) itemView.findViewById(R.id.layout_integera_exchange_name);
            dateTv = (TextView) itemView.findViewById(R.id.layout_integera_exchange_time);
            infoTv = (TextView) itemView.findViewById(R.id.layout_integera_exchange_info);
        }
    }

    public interface IntegeraExchangeClickListener {
        void onClick(IntegeraExchange integeraExchange);
    }
}

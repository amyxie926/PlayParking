package me.wztc.fragment.integral;

import java.util.ArrayList;
import java.util.HashMap;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.wztc.R;
import me.wztc.adapter.IntegeraExchangeAdapter;
import me.wztc.fragment.BaseFragment;
import me.wztc.model.IntegeraExchange;

public class IntegralRecordFragment extends BaseFragment {

    private ListView recordListView;
    private IntegeraExchangeAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_integral_record, null);  // View android.view.LayoutInflater.inflate(int resource, ViewGroup root)

        recordListView = (ListView) view.findViewById(R.id.record);
        if (adapter == null) {
            adapter = new IntegeraExchangeAdapter();
        }

        adapter.setIntegeraExchanges(getData());
        adapter.setIntegeraExchangeClickListener(integeraExchangeClickListener);
        recordListView.setAdapter(adapter);
        return view;
    }

    private ArrayList<IntegeraExchange> getData() {
        ArrayList<IntegeraExchange> integeraExchanges = new ArrayList<IntegeraExchange>();
        IntegeraExchange integeraExchange = new IntegeraExchange();
        integeraExchange.setName("兑换成功");
        integeraExchange.setInfo("恭喜你获得手机100元充值卡");
        integeraExchange.setDate("2015-06-24 12:00");

        IntegeraExchange integeraExchange2 = new IntegeraExchange();
        integeraExchange2.setName("兑换失败");
        integeraExchange2.setInfo("抱歉由于库存不足，无法兑换。积分已返还!");
        integeraExchange2.setDate("2015-07-23 11:37");

        IntegeraExchange integeraExchange3 = new IntegeraExchange();
        integeraExchange3.setName("兑换成功");
        integeraExchange3.setInfo("恭喜你获得一号店100元购物卡");
        integeraExchange3.setDate("2015-08-20 19:50");

        integeraExchanges.add(integeraExchange);
        integeraExchanges.add(integeraExchange2);
        integeraExchanges.add(integeraExchange3);
        return integeraExchanges;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // set title
        setTitle(R.string.integral_record_fragment_title);
    }

    private IntegeraExchangeAdapter.IntegeraExchangeClickListener integeraExchangeClickListener = new IntegeraExchangeAdapter.IntegeraExchangeClickListener() {
        @Override
        public void onClick(IntegeraExchange integeraExchange) {

        }
    };

    private void initMenuListView(View view) {
        // TODO Auto-generated method stub

                /*在数组中存放数据*/
        ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("ItemTitle", "兑换成功");
        map.put("ItemText", "恭喜你获得手机100元充值卡");
        map.put("ItemText1", "2015-06-12" + " " + "13:22");
        listItem.add(map);
        HashMap<String, Object> map1 = new HashMap<String, Object>();
        map1.put("ItemTitle", "兑换失败");
        map1.put("ItemText", "抱歉由于库存不足，无法兑换。积分已返还!");
        map1.put("ItemText1", "2015-06-11" + " " + "10:21");
        listItem.add(map1);
        HashMap<String, Object> map2 = new HashMap<String, Object>();
        map2.put("ItemTitle", "兑换成功");
        map2.put("ItemText", "恭喜你获得一号店100元购物卡");
        map2.put("ItemText1", "2015-06-11" + " " + "10:15");
        listItem.add(map2);
        SimpleAdapter mSimpleAdapter = new SimpleAdapter(getActivity(), listItem,
                R.layout.layout_item, new String[]{"ItemTitle", "ItemText", "ItemText1"},
                new int[]{R.id.ItemTitle, R.id.ItemText, R.id.ItemText1});
        //为ListView绑定适配器
        recordListView.setAdapter(mSimpleAdapter);
        recordListView.setOnItemClickListener(new
                                                      OnItemClickListener() {
                                                          public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                                                                  long arg3) {
                                                              //setTitle("你点击了第"+arg2+"行");//设置标题栏显示点击的行

                                                          }
                                                      });
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
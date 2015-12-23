package me.wztc.fragment.integral;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wztc.R;
import me.wztc.fragment.BaseFragment;
import me.wztc.model.User;
import me.wztc.notification.Notification;
import me.wztc.util.AppConstants;
import me.wztc.util.LocalDataBuffer;

public class IntegeralexchangeFragment extends BaseFragment {
    private static final String TAG = "IntegeralexchangeFragment";

    //积分
    private TextView tv_score;
    // 积分商城
    private LinearLayout integralShop;
    
    /*// 兑换记录
    private LinearLayout integralRecord;
    //
    private ListView integralRecordLv;

    private IntegeraExchangeAdapter adapter;
    */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("InflateParams")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_integera_exchange, null); // View
        // android.view.LayoutInflater.inflate(int
        // resource,
        // ViewGroup
        // root)
        initMenuListView(view);

        /*if (adapter == null) {
            adapter = new IntegeraExchangeAdapter();
        }

        adapter.setIntegeraExchanges(getData());
        adapter.setIntegeraExchangeClickListener(integeraExchangeClickListener);
        integralRecordLv.setAdapter(adapter);*/
        return view;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // set title
        setTitle(R.string.integral_exchange_fragment_title);
    }

    private void initMenuListView(View view) {
//    	tv_score = (TextView) view.findViewById(R.id.tv_score);
    	
    	/*
    	 //未从后台服务器调用成功所以先注释
    	//方法integralScore中表示积分显示，调用User类中积分单例对象
		IntegeralScore(LocalDataBuffer.getInstance().getUser());*/
		
        integralShop = (LinearLayout) view.findViewById(R.id.integralShop);
        integralShop.setOnClickListener(this);
        /*
        integralRecord = (LinearLayout) view.findViewById(R.id.integralRecord);
        integralRecord.setOnClickListener(this);
        integralRecordLv = (ListView) view.findViewById(R.id.integralRecordLv);*/
    }
    /**
     * 方法integralScore中表示积分在UI中显示~积分显示0为在UI中设置的假积分
     * @param user
     */
    private void IntegeralScore(User user){
    	if(user == null){
    		tv_score.setText(null);
    	}else{
    		tv_score.setText(user.getIntegral());
    		/*String integral = tv_score.getText().toString().trim();
    		System.out.println("积分："+integral);*/
    	}
    }

    /*private ArrayList<IntegeraExchange> getData() {
        ArrayList<IntegeraExchange> integeraExchanges = new ArrayList<IntegeraExchange>();
        IntegeraExchange integeraExchange = new IntegeraExchange();
        integeraExchange.setName("每日登录");
        integeraExchange.setInfo("恭喜您获得每日登录奖励:积分+2");
        integeraExchange.setDate("2015-07-24 16:00");

        IntegeraExchange integeraExchange2 = new IntegeraExchange();
        integeraExchange2.setName("停车消费");
        integeraExchange2.setInfo("恭喜您获得停车消费奖励:积分+2");
        integeraExchange2.setDate("2015-07-26 18:36");

        IntegeraExchange integeraExchange3 = new IntegeraExchange();
        integeraExchange3.setName("停车消费");
        integeraExchange3.setInfo("恭喜您获得停车消费奖励:积分+2");
        integeraExchange3.setDate("2015-08-05 12:36");

        integeraExchanges.add(integeraExchange);
        integeraExchanges.add(integeraExchange2);
        integeraExchanges.add(integeraExchange3);
        return integeraExchanges;
    }


    private IntegeraExchangeAdapter.IntegeraExchangeClickListener integeraExchangeClickListener = new IntegeraExchangeAdapter.IntegeraExchangeClickListener() {
        @Override
        public void onClick(IntegeraExchange integeraExchange) {

        }
    };
    */

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        super.onClick(v);
        switch (v.getId()) {
        case R.id.integralShop:
            // 跳转到积分商城
            mCenter.postNotification(new Notification(AppConstants.NOTIFICATION_ADD_FRAGMENT, new IntegralFragment()));
            break;
        /*case R.id.integralRecord:
            // 跳转到兑换记录
            mCenter.postNotification(new Notification(AppConstants.NOTIFICATION_ADD_FRAGMENT,
                    new IntegralRecordFragment()));
            break;*/
        default:
            break;
        }
    }
    public void onResume() {
        super.onResume();
        IntegeralScore(LocalDataBuffer.getInstance().getUser());
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
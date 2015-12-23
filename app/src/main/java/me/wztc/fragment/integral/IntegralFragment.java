package me.wztc.fragment.integral;

import java.util.ArrayList;
import java.util.List;


import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.wztc.R;
import me.wztc.adapter.AdaptparkIntegral;
import me.wztc.api.request.integral.IntegralFragmentRequest;
import me.wztc.api.request.integral.IntegralFragmentRequest.IntegralResponse;
import me.wztc.api.response.APIResponseHandler;
import me.wztc.fragment.BaseFragment;
import me.wztc.model.ModelGoods;
import me.wztc.model.User;
import me.wztc.util.LocalDataBuffer;
import me.wztc.util.ToastUtil;

public class IntegralFragment extends BaseFragment implements OnRefreshListener2<GridView> {
	protected static final int STOP_REFRUSH = 0;
	protected static final int FILL_DATA = 1;
	private PullToRefreshGridView mgridView;
	private TextView tv_user_integral;
	private AdaptparkIntegral mAdapt;
	ArrayList<ModelGoods> marray = new ArrayList<ModelGoods>();
	private User currUser;
	private int pageNo=1;//当请求的页数
	private int pageSize=20;//每頁显示的个数
	private int refrushType=-1;//0是加载更多，1是下拉刷新
	private List<ModelGoods>datas;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		currUser= LocalDataBuffer.getInstance().getUser();
		datas=new ArrayList<ModelGoods>();
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_integral_shop, null); // View
		mgridView = (PullToRefreshGridView) view
				.findViewById(R.id.pullToRefreshGridView1);
		tv_user_integral = (TextView) view.findViewById(R.id.tv_user_integral);
		
		/*
		 //未从后台服务器调用成功所以先注释
		 //方法integralScore中表示积分显示，调用User类中积分单例对象
		integralScore(LocalDataBuffer.getInstance().getUser());*/
		
		initMenuListView(view);
		doPost();
		return view;
	}
	/**
	 * 方法integralScore中表示积分在UI中显示~积分显示188为在UI中设置的假积分
	 * @param user
	 */
	private void integralScore(User user){
		
		if(user == null){
			tv_user_integral.setText(null);
		}else{
			tv_user_integral.setText(user.getIntegral());
		}
	}
	private void doPost() {
		if(refrushType==-1){//第一次加载
			showLoadingDialog();
		}


		IntegralFragmentRequest request=new IntegralFragmentRequest(Integer.parseInt(currUser.getUserId()),pageNo,pageSize);
		request.start(new APIResponseHandler<IntegralResponse>() {
			@Override
			public void handleResponse(IntegralFragmentRequest.IntegralResponse response) {
				if(getActivity()!=null){
					if(refrushType==-1){//第一次加载
						dismissLoadingDialog();
					}else{
						handler.sendEmptyMessage(STOP_REFRUSH);
					}
					if(pageNo==1){
						datas.clear();
					}
					if(response.getStatus()!=null&&response.getStatus()==0){
						if(response.getGoods().size()==0){
							if(pageNo==1){
								ToastUtil.toastLong(getActivity(), "暂无数据");
							}else{
								ToastUtil.toastLong(getActivity(), "已加载全部");
								pageNo--;
							}
							return;
						}
						datas.addAll(response.getGoods());
						handler.sendEmptyMessage(FILL_DATA);
					}else{
						String msg;
						if(TextUtils.isEmpty(response.getMsg())){
							msg="系统异常";
						}else{
							msg=response.getMsg();
						}
						//ToastUtil.toastLong(getActivity(), msg);
					}
				}
			}
			@Override
			public void handleError(String errorCode, String errorMessage) {
				String msg;
				if(TextUtils.isEmpty(errorMessage)){
					msg="系统异常";
				}else{
					msg=errorMessage;
				}
				//ToastUtil.toastLong(getActivity(), msg);
				if(getActivity()!=null){
					if(refrushType==-1){//第一次加载
						dismissLoadingDialog();
					}else{
						handler.sendEmptyMessage(STOP_REFRUSH);
					}
				}
			}
		});
	}
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		setTitle(R.string.integral_fragment_title);
	}
	private void initMenuListView(View view) {
		mgridView.setMode(Mode.BOTH);
		mgridView.setOnRefreshListener(this);
	}
	
	private Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(getActivity()!=null){
				if(msg.what==STOP_REFRUSH){
					mgridView.onRefreshComplete();
				}else if(msg.what==FILL_DATA){//填充数据
					if(mAdapt==null){
						mAdapt=new AdaptparkIntegral(getActivity(),datas);
						mgridView.setAdapter(mAdapt);
					}else{
						mAdapt.notifyDataSetChanged();
					}
				}
			}
		}
	};
	/**
	 * 加载更多
	 */
	@Override
	public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
		refrushType=1;
		pageNo=1;
		doPost();
	}
	/**
	 * 下拉刷新
	 */
	@Override
	public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
		refrushType=0;
		pageNo++;
		doPost();
	}
	public void onResume() {
        super.onResume();
        integralScore(LocalDataBuffer.getInstance().getUser());
    }
	public void onPause() {
        super.onPause();
    }
}
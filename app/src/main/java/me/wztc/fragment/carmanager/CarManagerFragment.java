package me.wztc.fragment.carmanager;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.wztc.R;
import me.wztc.adapter.CarManagerAdapter;
import me.wztc.api.request.carmanager.DeletePlatenoRequest;
import me.wztc.api.request.carmanager.GetPlatenoRequest;
import me.wztc.api.response.APIResponseHandler;
import me.wztc.api.response.BaseResponse;
import me.wztc.api.response.carmanager.GetPlatenoResponse;
import me.wztc.fragment.BaseFragment;
import me.wztc.model.User;
import me.wztc.util.AppUtils;
import me.wztc.util.LocalDataBuffer;
import me.wztc.util.ToastUtil;

public class CarManagerFragment extends BaseFragment {
	protected static final int QUERY_OK = 0;
	protected static final int QUERY_FAIL = 1;
	protected static final int DELETE_OK = 0;
	protected static final int DELETE_FAIL = 1;
	private User user;
	//车牌号
	private ListView lv_car_manager;
	private CarManagerAdapter adapter;
	private Button  addbutton;
	//存储车牌
	private List<String> datas;
	private Button surebutton;
	private Button canclebutton;
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_carmanager, container,
				false);
		init(view);
		return view;
	}

	private void init(View view) {
		datas = new ArrayList<String>();
		lv_car_manager = (ListView) view.findViewById(R.id.lv_car_manager);
		adapter=new CarManagerAdapter(getActivity(), datas, R.layout.car_manager_item_layout);
		lv_car_manager.setAdapter(adapter);
		//listview中点击车牌弹出对话框
		OnItemclick();
		
		if(datas.size()==0){
			//显示没有绑定车牌
		}
		
	}
	private Dialog dialog;
	private void OnItemclick() {
			lv_car_manager.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						final int position, long id) {
					//对话框自定义布局
					View dialogView=View.inflate(getActivity(), R.layout.layout_dialog_detecar, null);
					AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
					builder.setView(dialogView).create();
					dialog=builder.show();				
				surebutton=(Button) dialogView.findViewById(R.id.tv_confirm_ok);
					//确定按钮点击事件
				surebutton.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.dismiss();
						//删除车牌方法
						DeletePlateNo();
					}

					private void DeletePlateNo() {
						user = LocalDataBuffer.getInstance().getUser();
						String userId = user.getUserId();
						DeletePlatenoRequest request = new DeletePlatenoRequest(userId," ");
						request.start(new APIResponseHandler<BaseResponse>() {
							@Override
				            public void handleError(String errorCode, String errorMessage) {
				                isProcessing = false;
				                dismissLoadingDialog();
				                if (getActivity() == null) {
				                    return;
				                }
				                ToastUtil.toastLong(getActivity(),
										R.string.carmanager_fragment_deletecarnumber_fail);
				            }

							@Override
							public void handleResponse(BaseResponse response) {
								if (getActivity() == null) { 
									return;
								}
								isProcessing = false;
								dismissLoadingDialog();
								if("0".equals(AppUtils.parseEmpty(response.getStatus()))){
									datas.remove(position);
									handler.sendEmptyMessage(QUERY_OK);
									ToastUtil.toastLong(getActivity(),
											R.string.carmanager_fragment_deletecarnumber_success);
								}else{
									handler.sendEmptyMessage(DELETE_FAIL);
								}
							}
						});
						
							}
							
				});
				canclebutton=(Button) dialogView.findViewById(R.id.tv_confirm_cancel);
				canclebutton.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.dismiss();
					}
				});
				}
			});
	}

	@Override
	public void onResume() {
		super.onResume();
		GetPlateno();
	}
	private void GetPlateno() {
		datas.clear();
		user = LocalDataBuffer.getInstance().getUser();
		String userId = user.getUserId();
		GetPlatenoRequest getplatenoRequest = new GetPlatenoRequest(userId);
		getplatenoRequest.start(new APIResponseHandler<GetPlatenoResponse>() {
			@Override
			public void handleError(String errorCode, String errorMessage) {
				isProcessing = false;
				dismissLoadingDialog();
				if (getActivity() == null) {
					return;
				}
				ToastUtil.toastLong(getActivity(),
						R.string.	carmanager_fragment_bindcarnumber);
				
				//ToastUtil.toastLong(getActivity(), errorMessage);
			}

			@Override
			public void handleResponse(GetPlatenoResponse response) {
				isProcessing = false;
				dismissLoadingDialog();
				if (getActivity() == null) {
					return;
				}

				if (!AppUtils.isEmpty(response.getPlateNo())) {
					datas.add(response.getPlateNo());
					handler.sendEmptyMessage(QUERY_OK);
				} else {
					handler.sendEmptyMessage(QUERY_FAIL);
				}
			}
		});
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		// set title
		setTitle(R.string.carmanager_fragment__title);
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (getActivity()!=null) {
				if (msg.what == QUERY_OK) {
					if(datas!=null){
						if(adapter==null){
							adapter=new CarManagerAdapter(getActivity(), datas, R.layout.car_manager_item_layout);
							lv_car_manager.setAdapter(adapter);
						}else{
							adapter.notifyDataSetChanged();
							}
						}
					}
				}
			}
		};
	};

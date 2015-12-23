package me.wztc.fragment.carmanager;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.wztc.R;
import me.wztc.api.request.carmanager.BindCarRequest;
import me.wztc.api.response.APIResponseHandler;
import me.wztc.api.response.BaseResponse;
import me.wztc.fragment.BaseFragment;
import me.wztc.model.User;
import me.wztc.notification.Notification;
import me.wztc.util.AllCapTransformationMethod;
import me.wztc.util.AppConstants;
import me.wztc.util.AppUtils;
import me.wztc.util.LocalDataBuffer;
import me.wztc.util.ToastUtil;

public class CarManagerAddThirdFragment extends BaseFragment {
	protected static final int BIND_OK = 0;
	protected static final int BIND_FAIL = 1;
	private Button bt_sure;
	private Button bt_area;
	private EditText et_number;
	private User user;

	public String area, area_no;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_carmanager_addcar_third,
				container, false);
		init(view);
		return view;
	}

	private void init(View view) {
		bt_sure = (Button) view.findViewById(R.id.bt_sure);
		bt_area = (Button) view.findViewById(R.id.bt_frag_carmanager_items);
		et_number = (EditText) view.findViewById(R.id.edit_data);
		et_number.setTransformationMethod(new AllCapTransformationMethod());
		bt_sure.setOnClickListener(this);
		bt_area.setText(area + area_no);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		// set title
		setTitle(R.string.carmanager_fragment__addcar);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.bt_sure:
			commitPlateNo();
			break;

		}
	}

	/**
	 * 提交车牌号
	 */
	private void commitPlateNo() {
		user = LocalDataBuffer.getInstance().getUser();
		String userId=user.getUserId();
		if(user==null){
			ToastUtil.toastShort(getActivity(), "请登录");
			return;
		}
		if (TextUtils.isEmpty(et_number.getText().toString().trim())) {
			ToastUtil.toastShort(getActivity(), R.string.carmanager_fragment_input_carnoerror);
			return;
		}
		if (et_number.getText().toString().length() < 5) {
			ToastUtil.toastLong(getActivity(),
					R.string.carmanager_fragment_carnumber_error);
			return;
		}

		if (isProcessing) {
			return;
		}
		isProcessing = true;
		showLoadingDialog();
		// 车牌号
		final StringBuffer plateNo = new StringBuffer();
		String number=et_number.getText().toString().trim();
		String numbers=number.toUpperCase();
		plateNo.append(bt_area.getText().toString().trim()).append(
				numbers);
		BindCarRequest request = new BindCarRequest(userId,
				plateNo.toString());
		request.start(new APIResponseHandler<BaseResponse>() {
			@Override
            public void handleError(String errorCode, String errorMessage) {
                isProcessing = false;
                dismissLoadingDialog();
                if (getActivity() == null) {
                    return;
                }
                ToastUtil.toastLong(getActivity(), errorMessage);
            }

			@Override
			public void handleResponse(BaseResponse response) {
				if (getActivity() == null) {
					return;
				}
				isProcessing = false;
				dismissLoadingDialog();
				if("0".equals(AppUtils.parseEmpty(response.getStatus()))){
					handler.sendEmptyMessage(BIND_OK);
				}else{
					handler.sendEmptyMessage(BIND_FAIL);
				}
			}
		});
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
	}
	
	private Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			
			if(msg.what==BIND_OK){
				ToastUtil.toastShort(getActivity(), R.string.carmanager_fragment_bindcarnumber_success);
				 CarManagerFragment fragment=new CarManagerFragment();
				    fragment.getArguments();
					mCenter.postNotification(new Notification(
							AppConstants.NOTIFICATION_ADD_FRAGMENT,
							fragment));
			}else if(msg.what==BIND_FAIL){
				ToastUtil.toastShort(getActivity(), R.string.carmanager_fragment_bindcarnumber_fail);
			}
		};
	};
}

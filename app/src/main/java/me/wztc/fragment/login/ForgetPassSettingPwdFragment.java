package me.wztc.fragment.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.wztc.R;
import me.wztc.api.request.login.ForgetPasswordRequest;
import me.wztc.api.response.APIResponseHandler;
import me.wztc.api.response.BaseResponse;
import me.wztc.fragment.BaseFragment;
import me.wztc.notification.Notification;
import me.wztc.util.AppConstants;
import me.wztc.util.AppUtils;
import me.wztc.util.ToastUtil;

/**
 * 密码设置
 */
public class ForgetPassSettingPwdFragment extends BaseFragment {

    private String phone;
    private EditText newForgetPass, newSureForgetPass;
    private ImageView newForgetPassClear, newSureForgetPassClear;
    private Button completeBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forgetpass_pwd_setting, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        completeBtn = (Button) view.findViewById(R.id.forgetpass_fragment_next_button);
        newForgetPass = (EditText) view.findViewById(R.id.forgetpass_fragment_pass);
        newSureForgetPass = (EditText) view.findViewById(R.id.forgetpass_fragment_confrim_pass);
        newForgetPassClear = (ImageView) view.findViewById(R.id.forgetpass_fragment_pass_clear);
        newSureForgetPassClear = (ImageView) view.findViewById(R.id.forgetpass_fragment_confrim_pass_clear);
        completeBtn.setOnClickListener(this);
        newForgetPassClear.setOnClickListener(this);
        newSureForgetPassClear.setOnClickListener(this);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // set title
        setTitle(R.string.forgetpass_fragment_register_title);
        Bundle bundle = getArguments();
        phone = bundle.getString(AppConstants.BUNDLE_PHONE);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
        case R.id.forgetpass_fragment_next_button:
            commit();
            break;
        case R.id.forgetpass_fragment_pass_clear:
            newForgetPass.setText(null);
            break;
        case R.id.forgetpass_fragment_confrim_pass_clear:
            newSureForgetPass.setText(null);
            break;
        }
    }

    /**
     * 设置新密码
     */
    private void commit() {
        if (AppUtils.isEmpty(phone)) {
            return;
        }
        final String pass = newForgetPass.getText().toString().trim();
        final String confirmPass = newSureForgetPass.getText().toString().trim();

        //check pass
        if (AppUtils.isEmpty(pass) || pass.length() < 8) {
            ToastUtil.toastLong(getActivity(), R.string.password_fragment_message_pass_error);
            return;
        }

        //check  confirmPass
        if (AppUtils.isEmpty(confirmPass) || !confirmPass.equals(pass)) {
            ToastUtil.toastLong(getActivity(), R.string.password_fragment_message_pass_not_same);
            return;
        }

        if (isProcessing) {
            return;
        }
        isProcessing = true;
        showLoadingDialog();
        //后台请求的密码设置
        ForgetPasswordRequest request = new ForgetPasswordRequest(phone, pass);
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
                isProcessing = false;
                dismissLoadingDialog();
                if (getActivity() == null) {
                    return;
                }

                ToastUtil.toastLong(getActivity(), R.string.forget_pass_success);
                mCenter.postNotification(new Notification(AppConstants.NOTIFICATION_GO_BACK));
                mCenter.postNotification(new Notification(AppConstants.NOTIFICATION_GO_BACK));
            }
        });
    }

}

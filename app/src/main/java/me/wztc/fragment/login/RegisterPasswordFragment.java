package me.wztc.fragment.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.wztc.R;
import me.wztc.api.request.register.RegisterRequest;
import me.wztc.api.response.APIResponseHandler;
import me.wztc.api.response.BaseResponse;
import me.wztc.fragment.BaseFragment;
import me.wztc.util.AppConstants;
import me.wztc.util.AppUtils;
import me.wztc.util.ToastUtil;

/**
 * 注册密码
 */
public class RegisterPasswordFragment extends BaseFragment {

    private String phone;
    private EditText passEv, confirmPassEv;
    private ImageView newForgetPassClear, newSureForgetPassClear;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register_password, container, false);
        passEv = (EditText) view.findViewById(R.id.register_fragment_pass1);
        confirmPassEv = (EditText) view.findViewById(R.id.register_fragment_pass2);

        newForgetPassClear = (ImageView) view.findViewById(R.id.forgetpass_fragment_pass_clear);
        newSureForgetPassClear = (ImageView) view.findViewById(R.id.forgetpass_fragment_confrim_pass_clear);
        newForgetPassClear.setOnClickListener(this);
        newSureForgetPassClear.setOnClickListener(this);
        view.findViewById(R.id.register_fragment_commit).setOnClickListener(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        phone = bundle.getString(AppConstants.BUNDLE_PHONE);

        //set title
        setTitle(R.string.resister_fragment_register_title);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
        case R.id.register_fragment_commit:
            commit();
            break;
        case R.id.forgetpass_fragment_pass_clear:
            clearFirst();
            break;
        case R.id.forgetpass_fragment_confrim_pass_clear:
            clearSecond();
            break;
            /*default:
                break;*/
        }
    }

    private void clearFirst() {
        passEv.setText("");
    }

    private void clearSecond() {
        confirmPassEv.setText("");
    }

    /**
     * 提交注册
     */
    private void commit() {
        if (AppUtils.isEmpty(phone)) {
            return;
        }
        final String pass = passEv.getText().toString().trim();
        final String confirmPass = confirmPassEv.getText().toString().trim();

        //check pass
        if (AppUtils.isEmpty(pass) || pass.length() < 8) {
            ToastUtil.toastLong(getActivity(), R.string.password_fragment_message_pass_error);
            return;
        }

        //check confirm pass
        if (AppUtils.isEmpty(confirmPass) || !confirmPass.equals(pass)) {
            ToastUtil.toastLong(getActivity(), R.string.password_fragment_message_pass_not_same);
            return;
        }

        if (isProcessing) {
            return;
        }
        isProcessing = true;
        showLoadingDialog();
        //后台请求密码设置
        RegisterRequest request = new RegisterRequest(phone, pass);
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

                ToastUtil.toastLong(getActivity(), R.string.password_fragment_message_success);
            }
        });
    }
}
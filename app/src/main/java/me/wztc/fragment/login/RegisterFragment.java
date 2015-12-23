package me.wztc.fragment.login;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.CheckBox;
import android.widget.ImageView;

import me.wztc.api.response.register.GetSMSResponse;
import me.wztc.notification.Notification;
import me.wztc.util.AppUtils;
import me.wztc.util.ToastUtil;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.wztc.R;
import me.wztc.api.request.register.GetSMSRequest;
import me.wztc.api.response.APIResponseHandler;
import me.wztc.fragment.BaseFragment;
import me.wztc.fragment.account.AccountManageFragment;
import me.wztc.util.AppConstants;
import me.wztc.util.TimeCount;

/**
 * 注册
 */
public class RegisterFragment extends BaseFragment {

    private EditText phoneEt;
    private EditText codeEt;
    private CheckBox agreementCb;
    private ImageView phoneClearIv, codeClearIv;

    private Button getCodeBtn;

    private TimeCount timeCount;

    private String verify;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        phoneEt = (EditText) view.findViewById(R.id.register_fragment_phone);
        getCodeBtn = (Button) view.findViewById(R.id.register_fragment_get_code_btn);
        getCodeBtn.setOnClickListener(this);

        phoneClearIv = (ImageView) view.findViewById(R.id.register_fragment_phone_clear);
        phoneClearIv.setOnClickListener(this);

        codeClearIv = (ImageView) view.findViewById(R.id.register_fragment_code_clear);
        codeClearIv.setOnClickListener(this);

        codeEt = (EditText) view.findViewById(R.id.register_fragment_code);

        agreementCb = (CheckBox) view.findViewById(R.id.register_fragment_agreement_checkBox);
        view.findViewById(R.id.register_fragment_register_button).setOnClickListener(this);

        phoneEt.addTextChangedListener(phoneWatcher);
        codeEt.addTextChangedListener(codeWatcher);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // set title
        setTitle(R.string.resister_fragment_register_title);
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.register_fragment_phone_clear:
                phoneEt.setText("");
                break;
            case R.id.register_fragment_get_code_btn:// 获取验证码
                getDynamicCode();
                break;
            case R.id.register_fragment_code_clear:
                codeEt.setText("");
                break;
            case R.id.register_fragment_register_button:// 注册
                register();
                break;
        }
    }

    private void getDynamicCode() {
        final String phone = phoneEt.getText().toString().trim();

        // check phone
        if (!AppUtils.isMobilePhone(phone)) {
            ToastUtil.toastShort(getActivity(), R.string.register_fragment_message_phone_error);
            return;
        }

        //请求发送验证码api
        if (isProcessing) {
            return;
        }
        isProcessing = true;
        showLoadingDialog();

        //向接口传入电话号码信息
        GetSMSRequest request = new GetSMSRequest(phone);
        request.start(new APIResponseHandler<GetSMSResponse>() {
            @Override
            public void handleError(String errorCode, String errorMessage) {
                isProcessing = false;
                dismissLoadingDialog();
                if (getActivity() == null) {
                    return;
                }
                ToastUtil.toastShort(getActivity(), errorMessage);
            }

            @Override
            public void handleResponse(GetSMSResponse response) {
                isProcessing = false;
                dismissLoadingDialog();
                if (getActivity() == null) {
                    return;
                }
                verify = response.getCode();
                ToastUtil.toastShort(getActivity(), R.string.register_fragment_message_code_success);
                // 倒数计时
                timeCount = new TimeCount(getActivity(), AppConstants.TIME_COUNT_MILLIS_IN_FUTURE,
                        AppConstants.TIME_COUNT_COUNT_DOWN_INTERVAL, getCodeBtn);// 构造CountDownTimer对象
                timeCount.start();
            }
        });
    }

    private void register() {
        final String phone = phoneEt.getText().toString().trim();
        final String code = codeEt.getText().toString().trim();


        // check phone
        if (!AppUtils.isMobilePhone(phone)) {
            ToastUtil.toastShort(getActivity(), R.string.register_fragment_message_phone_error);
            return;
        }

        //check code
        if (AppUtils.isEmpty(code) || code.length() != 6) {
            ToastUtil.toastShort(getActivity(), R.string.register_fragment_message_code_error);
            return;
        }

        if (!code.equals(verify)) {
            ToastUtil.toastShort(getActivity(), R.string.register_fragment_message_code_error);
            return;
        }

        //check是否勾选服务条款
        if (!agreementCb.isChecked()) {
            ToastUtil.toastShort(getActivity(), R.string.register_fragment_message_agreement_not_checked);
            return;
        }

        RegisterPasswordFragment fragment = new RegisterPasswordFragment();
        //设置参数
        Bundle bundle = new Bundle();
        bundle.putString(AppConstants.BUNDLE_PHONE, phone);
        fragment.setArguments(bundle);
        //mCenter.postNotification(new Notification(AppConstants.NOTIFICATION_ADD_FRAGMENT, fragment));
        mCenter.postNotification(new Notification(AppConstants.NOTIFICATION_ADD_FRAGMENT, new AccountManageFragment()));
    }

    private TextWatcher phoneWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String account = s.toString();
            if (AppUtils.isEmpty(account)) {
                phoneClearIv.setVisibility(View.INVISIBLE);
            } else {
                phoneClearIv.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private TextWatcher codeWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String account = s.toString();
            if (AppUtils.isEmpty(account)) {
                codeClearIv.setVisibility(View.INVISIBLE);
            } else {
                codeClearIv.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
}

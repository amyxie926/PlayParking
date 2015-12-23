package me.wztc.fragment.login;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.wztc.R;
import me.wztc.api.request.login.LoginRequest;
import me.wztc.api.response.APIResponseHandler;
import me.wztc.api.response.login.LoginResponse;
import me.wztc.fragment.BaseFragment;
import me.wztc.model.User;
import me.wztc.notification.Notification;
import me.wztc.util.AppConstants;
import me.wztc.util.AppUtils;
import me.wztc.util.LocalDataBuffer;
import me.wztc.util.ToastUtil;
import me.wztc.util.UserKeeper;

/**
 * 登录fragment
 */
public class LoginFragment extends BaseFragment {

    private EditText phoneEt;
    private EditText passwordEt;
    private TextView forgetPass, registerTv;

    private ImageView phoneClearIv, passwordClearIv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        registerTv = (TextView) view.findViewById(R.id.login_fragment_register);
        registerTv.setOnClickListener(this);

        phoneEt = (EditText) view.findViewById(R.id.login_fragment_phone);
        passwordEt = (EditText) view.findViewById(R.id.login_fragment_pass);
        forgetPass = (TextView) view.findViewById(R.id.login_fragment_forget_pass);
        forgetPass.setOnClickListener(this);

        phoneClearIv = (ImageView) view.findViewById(R.id.login_fragment_phone_clear);
        phoneClearIv.setOnClickListener(this);

        passwordClearIv = (ImageView) view.findViewById(R.id.login_fragment_pass_clear);
        passwordClearIv.setOnClickListener(this);

        view.findViewById(R.id.login_fragment_login_button).setOnClickListener(this);

        forgetPass.setOnClickListener(this);

        phoneEt.addTextChangedListener(phoneWatcher);
        passwordEt.addTextChangedListener(passWatcher);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // set title
        setTitle(R.string.login_fragment_login);
        // 如果有保存的账号，把账号填到输入框中
        String account = (String) AppUtils.getUserSession(getActivity(), AppConstants.PREFERENCE_PHONE, "");
        //String password = (String) AppUtils.getUserSession(getActivity(), AppConstants., "")
        if (!AppUtils.isEmpty(account)) {
            phoneEt.setText(account);
            passwordEt.requestFocus();
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.login_fragment_register:
                mCenter.postNotification(new Notification(AppConstants.NOTIFICATION_ADD_FRAGMENT, new RegisterFragment()));
                break;
            case R.id.login_fragment_phone_clear:
                phoneEt.setText("");
                break;
            case R.id.login_fragment_pass_clear:
                passwordEt.setText("");
                break;
            case R.id.login_fragment_login_button:// 登录
                login();
                break;
            case R.id.login_fragment_forget_pass:// 忘记密码
                mCenter.postNotification(new Notification(AppConstants.NOTIFICATION_ADD_FRAGMENT,
                        new ForgetPasswordFragment()));
                break;
        }
    }

    private void login() {
        String phoneNumber = phoneEt.getText().toString().trim();
        String password = passwordEt.getText().toString().trim();

        if (AppUtils.isEmpty(phoneNumber)) {
            ToastUtil.toastLong(getActivity(), R.string.login_fragment_login_phone_null);
            return;
        }

        if (AppUtils.isEmpty(password)) {
            ToastUtil.toastLong(getActivity(), R.string.login_fragment_login_pwd_null);
            return;
        }

        AppUtils.storeUserSession(getActivity(), AppConstants.PREFERENCE_PHONE, phoneNumber);
        AppUtils.hideSoftKb(getActivity(), phoneEt);
        if (isProcessing) {
            return;
        }
        isProcessing = true;
        showLoadingDialog();

        LoginRequest loginRequest = new LoginRequest(phoneNumber, password);
        loginRequest.start(new APIResponseHandler<LoginResponse>() {
            @Override
            public void handleResponse(LoginResponse response) {
                isProcessing = false;
                dismissLoadingDialog();
                if (getActivity() == null) {
                    return;
                }

                //持久化保存user
                UserKeeper.saveUser(getActivity(), response.getUser());
                User user = response.getUser();
                LocalDataBuffer.getInstance().setUser(user);
                ToastUtil.toastLong(getActivity(), R.string.login_fragment_message_success);
                AppUtils.storeUserSession(getActivity(), AppConstants.PREFERENCE_PHONE, user.getPhoneNumber());
                mCenter.postNotification(new Notification(AppConstants.NOTIFICATION_GO_BACK));
                mCenter.postNotification(new Notification(AppConstants.NOTIFICATION_LOGIN_SUCCESS));
            }

            @Override
            public void handleError(String errorCode, String errorMessage) {
                isProcessing = false;
                dismissLoadingDialog();
                if (getActivity() == null) {
                    return;
                }

                ToastUtil.toastLong(getActivity(), errorMessage);
            }
        });

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

    private TextWatcher passWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String account = s.toString();
            if (AppUtils.isEmpty(account)) {
                passwordClearIv.setVisibility(View.INVISIBLE);
            } else {
                passwordClearIv.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @Override
    public void onPause() {
        super.onPause();
        registerTv.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onResume() {
        super.onResume();
        registerTv.setVisibility(View.VISIBLE);
    }
}

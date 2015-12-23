package me.wztc.fragment.account;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.wztc.R;
import me.wztc.api.request.account.PasswordChangeRequest;
import me.wztc.api.response.APIResponseHandler;
import me.wztc.api.response.BaseResponse;
import me.wztc.fragment.BaseFragment;
import me.wztc.model.User;
import me.wztc.notification.Notification;
import me.wztc.util.AppConstants;
import me.wztc.util.AppUtils;
import me.wztc.util.LocalDataBuffer;
import me.wztc.util.ToastUtil;

public class ChangePasswordFragment extends BaseFragment {

    private EditText oldPassEt, newPassEt, confirmPassEt;
    private User user;
    private ImageView oldPassClearIv, newPassClearIv, confrimPassClearIv;
    private Button completeBtn;

    @SuppressLint("InflateParams")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_password, null);  // View android.view.LayoutInflater.inflate(int resource, ViewGroup root)
        oldPassEt = (EditText) view.findViewById(R.id.change_password_fragment_old_pass_et);
        oldPassClearIv = (ImageView) view.findViewById(R.id.change_password_fragment_old_pass_clear);
        oldPassClearIv.setOnClickListener(this);

        newPassEt = (EditText) view.findViewById(R.id.change_password_fragment_new_pass_et);
        newPassClearIv = (ImageView) view.findViewById(R.id.change_password_fragment_new_pass_clear);
        newPassClearIv.setOnClickListener(this);

        confirmPassEt = (EditText) view.findViewById(R.id.change_password_fragment_confirm_pass_et);
        confrimPassClearIv = (ImageView) view.findViewById(R.id.change_password_fragment_confirm_pass_clear);
        confrimPassClearIv.setOnClickListener(this);

        completeBtn = (Button) view.findViewById(R.id.change_password_fragment_complete_btn);
        completeBtn.setOnClickListener(this);

        oldPassEt.addTextChangedListener(oldPwdWatcher);
        newPassEt.addTextChangedListener(newPwdWatcher);
        confirmPassEt.addTextChangedListener(confrimPwdWatcher);
        return view;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        // set title
        setTitle(R.string.password_fragment_title);
    }

    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
        case R.id.change_password_fragment_old_pass_clear:
            oldPassEt.setText("");
            break;
        case R.id.change_password_fragment_new_pass_clear:
            newPassEt.setText("");
            break;
        case R.id.change_password_fragment_confirm_pass_clear:
            confirmPassEt.setText("");
            break;
        case R.id.change_password_fragment_complete_btn:
            commit();
            break;
        }
    }


    /**
     * 密码修改
     */
    private void commit() {
        // TODO Auto-generated method stub
                /*if (AppUtils.isEmpty(userId)) {
                    return;
		        }*/
    	//取得LocalDataBuffer的userId
        user = LocalDataBuffer.getInstance().getUser();
        String userId=user.getUserId();
        final String pass = newPassEt.getText().toString().trim();
        final String newpass = oldPassEt.getText().toString().trim();
        final String confirmpass = confirmPassEt.getText().toString().trim();

        //check pass
        if (AppUtils.isEmpty(pass) || pass.length() < 8) {
            ToastUtil.toastLong(getActivity(), R.string.password_fragment_message_pass_error);
            return;
        }
        //check newpass
        if (AppUtils.isEmpty(newpass) || newpass.length() < 8) {
            ToastUtil.toastLong(getActivity(), R.string.password_fragment_message_pass_error);
            return;
        }
        //check confirm pass
        if (AppUtils.isEmpty(confirmpass) || !confirmpass.equals(pass)) {
            ToastUtil.toastLong(getActivity(), R.string.password_fragment_message_pass_not_same);
            commit();
            return;
        }
        //隐藏输入法键盘
        AppUtils.hideSoftKb(getActivity(), oldPassEt);
        AppUtils.hideSoftKb(getActivity(), newPassEt);
        AppUtils.hideSoftKb(getActivity(), confirmPassEt);
        if (isProcessing) {
            return;
        }
        isProcessing = true;
        showLoadingDialog();
        //后台请求密码设置
        PasswordChangeRequest request = new PasswordChangeRequest(userId, pass, newpass);
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

                //ToastUtil.toastLong(getActivity(), R.string.password_fragment_message_success2);
                ToastUtil.toastLong(getActivity(), "恭喜你密码修改成功！");
                mCenter.postNotification(new Notification(AppConstants.NOTIFICATION_ADD_FRAGMENT, new AccountManageFragment()));
                commit();
            }
        });
    }


    private TextWatcher oldPwdWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String account = s.toString();
            if (AppUtils.isEmpty(account)) {
                oldPassClearIv.setVisibility(View.INVISIBLE);
            } else {
                oldPassClearIv.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };


    private TextWatcher newPwdWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String account = s.toString();
            if (AppUtils.isEmpty(account)) {
                newPassClearIv.setVisibility(View.INVISIBLE);
            } else {
                newPassClearIv.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private TextWatcher confrimPwdWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String account = s.toString();
            if (AppUtils.isEmpty(account)) {
                confrimPassClearIv.setVisibility(View.INVISIBLE);
            } else {
                confrimPassClearIv.setVisibility(View.VISIBLE);
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
    }
}
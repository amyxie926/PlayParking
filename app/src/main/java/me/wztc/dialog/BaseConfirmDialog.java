package me.wztc.dialog;

import com.wztc.R;
import me.wztc.listener.DialogBtnClickListener;
import me.wztc.util.AppUtils;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class BaseConfirmDialog extends BaseDialog implements OnClickListener {

    private int mTitle, mContent, mOkBtn, mCancelBtn;
    private String mContentStr;

    protected TextView tvConfirmTitle, tvConfirmOk, tvConfirmCancel, tvConfirmContent;

    public BaseConfirmDialog(Context context) {
        super(context);
    }
    public BaseConfirmDialog(Context context, DialogBtnClickListener listener) {
        super(context, listener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_base_confirm_dialog);

        tvConfirmContent = (TextView) findViewById(R.id.tv_confirm_content);

        tvConfirmOk = (TextView) findViewById(R.id.tv_confirm_ok);
        tvConfirmOk.setOnClickListener(this);

        tvConfirmCancel = (TextView) findViewById(R.id.tv_confirm_cancel);
        tvConfirmCancel.setOnClickListener(this);
        init();

        if (mTitle > 0) {
            setTitleText(mTitle);
        }

        if (mContent > 0) {
            setContentText(mContent);
        }

        if (!AppUtils.isEmpty(mContentStr)) {
            setContentText(mContentStr);
        }

        if (mOkBtn > 0) {
            setOkBtnText(mOkBtn);
        }

        if (mCancelBtn > 0) {
            setCancelBtnText(mCancelBtn);
        }

    }

    private void init() {
        tvConfirmOk.setText(R.string.base_ok);
        tvConfirmCancel.setText(R.string.base_cancel);
    }

    public void setTitleText(int title) {
        mTitle = title;
        if (tvConfirmTitle != null) {
            tvConfirmTitle.setText(title);
        }
    }

    public void setContentText(String content) {
        mContentStr = content;
        if (tvConfirmContent != null) {
            tvConfirmContent.setText(content);
        }
    }

    public void setContentText(int content) {
        mContent = content;
        if (tvConfirmContent != null) {
            tvConfirmContent.setText(content);
        }
    }

    public void setOkBtnText(int okBtn) {
        mOkBtn = okBtn;
        if (tvConfirmOk != null) {
            tvConfirmOk.setText(okBtn);
        }
    }

    public void setCancelBtnText(int cancelBtn) {
        mCancelBtn = cancelBtn;
        if (tvConfirmCancel != null) {
            tvConfirmCancel.setText(cancelBtn);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
        case R.id.tv_confirm_cancel:
            if (null != listener)
                listener.onCancelBtnClick();
            dismiss();
            break;
        case R.id.tv_confirm_ok:

            if (null != listener)
                listener.onOKBtnClick();
            dismiss();
            break;
        }
    }
}

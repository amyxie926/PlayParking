package me.wztc.dialog;

import com.wztc.R;
import android.content.Context;
import me.wztc.listener.DialogBtnClickListener;

public class LogOffDialog extends BaseConfirmDialog {

    public LogOffDialog(Context context, DialogBtnClickListener listener) {
        super(context, listener);
    }

    @Override
    public void show() {
        super.show();
        tvConfirmTitle.setText(R.string.account_fragment_log_off);
        tvConfirmContent.setText(R.string.account_fragment_log_off_question);
        //dismiss();
    }
}

package me.wztc.dialog;


import com.wztc.R;
import me.wztc.listener.DialogBtnClickListener;

import android.app.Dialog;
import android.content.Context;

public class BaseDialog extends Dialog {

    protected String errCode;
    protected DialogBtnClickListener listener;

    public BaseDialog(Context context) {
        super(context, R.style.full_screen_dialog);
    }

    public BaseDialog(Context context, int theme) {
        super(context, R.style.full_screen_dialog);
    }

    public BaseDialog(Context context, DialogBtnClickListener dialogBtnClickListener) {
        super(context, R.style.full_screen_dialog);
        listener = dialogBtnClickListener;
    }

    public void setListener(DialogBtnClickListener listener) {
        this.listener = listener;
    }

    public String getDialogName() {
        return getClass().getSimpleName();
    }

}

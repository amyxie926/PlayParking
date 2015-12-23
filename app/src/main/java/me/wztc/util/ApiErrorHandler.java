package me.wztc.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import com.wztc.R;
import me.wztc.listener.DialogBtnClickListener;

public class ApiErrorHandler {

    public static void handler(Context context, String errorMessage) {
        handler(context, errorMessage, null);
    }

    public static void handler(Context context, String errorMessage, final DialogBtnClickListener listener) {
        if (context == null)
            return;
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle(R.string.error_dialog_title);
        if (AppUtils.isEmpty(errorMessage)) {
            dialog.setMessage(R.string.base_network_error);
        } else {
            dialog.setMessage(errorMessage);
        }
        dialog.setPositiveButton(R.string.base_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (listener != null) {
                    listener.onOKBtnClick();
                }
            }
        });
        dialog.create().show();
    }
}

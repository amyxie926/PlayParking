package me.wztc.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import com.wztc.R;

public class UiErrorHandler {

    public static void handler(Context context, int id) {
        handler(context, id, null);
    }

    public static void handler(Context context, int id, OnClickListener listener) {
        if (context == null)
            return;
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle(R.string.error_dialog_title);
        dialog.setMessage(id);
        dialog.setPositiveButton(R.string.base_ok, listener);
        dialog.create().show();
    }

    public static void handler(Context context, String message) {
        handler(context, message, null);
    }

    public static void handler(Context context, String message, OnClickListener listener) {
        if (context == null)
            return;
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle(R.string.error_dialog_title);
        dialog.setMessage(message);
        dialog.setPositiveButton(R.string.base_ok, listener);
        dialog.create().show();
    }

}

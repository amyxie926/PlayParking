package me.wztc.util;

import android.content.Context;
import android.content.res.Resources;
import android.view.Gravity;
import com.github.johnpersano.supertoasts.SuperToast;
import com.github.johnpersano.supertoasts.util.Style;
import com.wztc.R;

public class ToastUtil {

    private static int topOffSet;

    public static void toastLong(Context context, String message) {
        if (context == null)
            return;
        SuperToast toast = SuperToast.create(context, message, SuperToast.Duration.SHORT, Style.getStyle(Style.GRAY));
        toast.setGravity(Gravity.TOP, 0, getTopOffSet(context));
        toast.show();
    }

    public static void toastShort(Context context, String message) {
        if (context == null)
            return;
        SuperToast toast =
                SuperToast.create(context, message, SuperToast.Duration.VERY_SHORT, Style.getStyle(Style.GRAY));
        toast.setGravity(Gravity.TOP, 0, getTopOffSet(context));
        toast.show();
    }

    public static void toastLong(Context context, int id) {
        if (context == null)
            return;
        SuperToast toast =
                SuperToast.create(context, context.getResources().getString(id), SuperToast.Duration.SHORT, Style.getStyle(Style.GRAY));
        toast.setGravity(Gravity.TOP, 0, getTopOffSet(context));
        toast.show();
    }

    public static void toastShort(Context context, int id) {
        if (context == null)
            return;
        SuperToast toast =
                SuperToast.create(context, context.getText(id), SuperToast.Duration.VERY_SHORT,
                        Style.getStyle(Style.GRAY));
        toast.setGravity(Gravity.TOP, 0, getTopOffSet(context));
        toast.show();
    }

    private static int getTopOffSet(Context context) {
        if (topOffSet > 0) {
            return topOffSet;
        }
        if (context == null) {
            return 0;
        }
        Resources resources = context.getResources();
        topOffSet = resources.getDimensionPixelSize(R.dimen.base_title_height) +
                resources.getDimensionPixelSize(R.dimen.padding_small);
        return topOffSet;
    }
}

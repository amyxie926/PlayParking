package me.wztc.util;

import com.wztc.R;

import android.content.Context;
import android.os.CountDownTimer;
import android.widget.TextView;

public class TimeCount extends CountDownTimer {

    private TextView bt;
    private Context context;

    public TimeCount(Context context, long millisInFuture, long countDownInterval, TextView bt) {
        super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
        this.context = context;
        this.bt = bt;
    }

    /**
     * 计时过程显示(1分钟)
     */
    @Override
    public void onTick(long millisUntilFinished) {
        bt.setClickable(false);

        bt.setText(String.format(context.getResources().getString(R.string.register_fragment_dynamic_code_text),
                millisUntilFinished / 1000 + ""));
        bt.setTextColor(context.getResources().getColor(R.color.color_white));
        bt.setBackgroundResource(R.drawable.gray_shape_bg);
    }

    /**
     * 计时完毕时触发
     */
    @Override
    public void onFinish() {
        bt.setText(R.string.register_fragment_button_afresh_get);
        bt.setBackgroundResource(R.drawable.green_bg_button);
        bt.setTextColor(context.getResources().getColor(R.color.color_white));
        bt.setClickable(true);
    }
}

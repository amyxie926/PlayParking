package me.wztc.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.wztc.R;
import me.wztc.listener.DialogBtnClickListener;
import me.wztc.util.AppUtils;

public class BaseErrorDialog extends BaseDialog implements OnClickListener {

    protected TextView tv_confrim_content;
    protected Button ok_button;

    public BaseErrorDialog(Context context, int theme) {
        super(context, theme);
    }

    public BaseErrorDialog(Context context) {
        super(context);
    }

    public BaseErrorDialog(Context context, DialogBtnClickListener dialogBtnClickListener) {
        super(context, dialogBtnClickListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         setContentView(R.layout.layout_base_error_dialog);
        
         ok_button = (Button) findViewById(R.id.base_error_dialog_confirm_ok);
         ok_button.setOnClickListener(this);
        
         tv_confrim_content = (TextView)
         findViewById(R.id.base_error_dialog_confirm_content);

    }

    @Override
    public void onClick(View v) {
         int id = v.getId();
         switch (id) {
         case R.id.base_error_dialog_confirm_ok:
         if (listener != null)
         listener.onOKBtnClick();
         dismiss();
         break;
         }

    }

    public void showError(String errCode, String errorMessage) {
        this.errCode = errCode;
        if (!isShowing())
            show();

         if (!AppUtils.isEmpty(errorMessage)) {
         tv_confrim_content.setText(errorMessage);
         } else {
         tv_confrim_content.setText(R.string.base_network_error);
         }
    }

}

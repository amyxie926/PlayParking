package me.wztc.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.wztc.R;

public class CombinationPayDialog extends BaseDialog implements View.OnClickListener {
    private TextView payMoenyTv;
    private CheckBox currentBalanceCk;


    public CombinationPayDialog(Context context) {
        super(context);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_combination_pay_dialog);

        payMoenyTv = (TextView) findViewById(R.id.combination_pay_layout_current_balance_ck);
        currentBalanceCk = (CheckBox) findViewById(R.id.combination_pay_layout_current_balance_ck);

        findViewById(R.id.pay_fragment_confrim_pay_button).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pay_fragment_confrim_pay_button:
                break;
        }
    }
}

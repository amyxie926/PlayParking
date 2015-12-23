package me.wztc.ui;

import java.util.ArrayList;

import com.wztc.R;
import me.wztc.model.radio.PayType;
import me.wztc.model.radio.RechargeType;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

public class RadioView extends LinearLayout {

    private LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

    private LayoutParams paramsLine;

    private LayoutInflater inflater;

    private ArrayList<ViewHolder> viewHolders;

    public RadioView(Context context) {
        super(context);
        initView(context);
    }

    public RadioView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public RadioView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public RadioView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    private void initView(Context context) {
        setOrientation(VERTICAL);
        inflater = LayoutInflater.from(context);

        Resources resources = getResources();
        int height = (int) (resources.getDisplayMetrics().density * 1);
        paramsLine = new LayoutParams(LayoutParams.MATCH_PARENT, height);

        viewHolders = new ArrayList<ViewHolder>();
    }

    public void setLeftView(ArrayList<Object> list) {

        if (list == null) {
            return;
        }

        for (int i = 0; i < list.size(); i++) {
            Object data = list.get(i);
            if (data instanceof PayType) {
                PayType payType = (PayType) data;
                addItem(payType);
            } else if (data instanceof RechargeType) {
                RechargeType rechargeType = (RechargeType) data;
                addItem(rechargeType);
            }
        }
    }

    /**
     * 支付类型
     * 
     * @param payType
     */
    @SuppressLint("InflateParams")
    private void addItem(PayType payType) {
        View itemRoot = inflater.inflate(R.layout.layout_common_radio, null);
        itemRoot.setLayoutParams(params);

        final ViewHolder viewHolder = new ViewHolder(itemRoot);
        viewHolders.add(viewHolder);

        LinearLayout itemContainer = (LinearLayout) itemRoot.findViewById(R.id.radio_contais);

        View radioLayout = inflater.inflate(R.layout.layout_pay_type, null);
        radioLayout.setLayoutParams(params);

        ImageView payTypeIv = (ImageView) radioLayout.findViewById(R.id.pay_type_iv);
        payTypeIv.setImageResource(payType.getResId());

        TextView payTypeTv = (TextView) radioLayout.findViewById(R.id.pay_type_tv);
        payTypeTv.setText(payType.getNameId());

        TextView balanceTv = (TextView) radioLayout.findViewById(R.id.pay_type_balance_tv);
        String rechargeBalance = String.format(
                itemRoot.getContext().getString(R.string.recharge_fragment_playparking_balance), 88);
        if (payType==PayType.balancePay) {
            balanceTv.setVisibility(View.VISIBLE);
            balanceTv.setText(rechargeBalance);
        } else {
            balanceTv.setVisibility(View.GONE);
        }

        itemContainer.addView(radioLayout);

        addView(itemRoot);
        addLine();

        itemRoot.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                setRadioButtonStatus(viewHolder);
            }
        });
    }

    /**
     * 充值类型
     * 
     * @param rechargeInfo
     */
    @SuppressLint("InflateParams")
    private void addItem(RechargeType rechargeInfo) {
        View itemRoot = inflater.inflate(R.layout.layout_common_radio, null);
        itemRoot.setLayoutParams(params);

        final ViewHolder viewHolder = new ViewHolder(itemRoot);
        viewHolders.add(viewHolder);

        LinearLayout itemContainer = (LinearLayout) itemRoot.findViewById(R.id.radio_contais);

        View radioLayout = inflater.inflate(R.layout.layout_recharge_type, null);
        radioLayout.setLayoutParams(params);

        TextView rechargeMoneyTv = (TextView) radioLayout.findViewById(R.id.rechgarge_moeny_tv);
        rechargeMoneyTv.setText(rechargeInfo.getRechargeMoney());

        TextView giveMoneyTv = (TextView) radioLayout.findViewById(R.id.rechgarge_give_tv);
        giveMoneyTv.setText(rechargeInfo.getGiveMoeny());

        itemContainer.addView(radioLayout);

        addView(itemRoot);
        addLine();

        itemRoot.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                setRadioButtonStatus(viewHolder);
            }
        });
    }

    @SuppressLint("InflateParams")
    private void addLine() {
        View itemRoot = inflater.inflate(R.layout.layout_pay_type_line, null);
        itemRoot.setLayoutParams(paramsLine);
        addView(itemRoot);
    }

    @SuppressLint("InflateParams")
    private void addPaddingLine() {
        View itemRoot = inflater.inflate(R.layout.layout_pay_type_line, null);
        itemRoot.setLayoutParams(paramsLine);
        addView(itemRoot);
    }

    private void setRadioButtonStatus(ViewHolder holder) {
        for (ViewHolder h : viewHolders) {
            h.rb.setChecked(false);
        }

        holder.rb.setChecked(true);
    }

    /**
     * 设置为默认选择
     * 
     * @param index
     */
    public void setSelectedRadio(int index) {
        for (ViewHolder h : viewHolders) {
            h.rb.setChecked(false);
        }
        viewHolders.get(index).rb.setChecked(true);
    }

    /**
     * 选中
     * 
     * @return
     */
    public int getSelectRadio() {
        for (int i = 0; i < viewHolders.size(); i++) {
            ViewHolder holder = viewHolders.get(i);
            if (holder.rb.isChecked()) {
                return i;
            }
        }

        return -1;
    }

    private class ViewHolder {
        // View leftView;
        RadioButton rb;

        public ViewHolder(View view) {
            // leftView = view.findViewById(R.id.radio_contais);
            rb = (RadioButton) view.findViewById(R.id.radio_rb);
        }
    }
}

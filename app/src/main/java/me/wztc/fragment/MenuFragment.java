package me.wztc.fragment;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wztc.R;
import me.wztc.fragment.account.AccountManageFragment;
import me.wztc.fragment.account.PetNameFragment;
import me.wztc.fragment.bill.BillManagerFragment;
import me.wztc.fragment.carmanager.CarManagerFragment;
import me.wztc.fragment.integral.IntegeralexchangeFragment;
import me.wztc.fragment.setting.SettingFragment;
import me.wztc.fragment.truck.TruckFragment;
import me.wztc.fragment.vip.VipFragment;
import me.wztc.fragment.wallet.WalletHomeFragment;
import me.wztc.model.User;
import me.wztc.notification.Notification;
import me.wztc.util.AppConstants;
import me.wztc.util.AppUtils;
import me.wztc.util.LocalDataBuffer;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 菜单画面
 */
public class MenuFragment extends BaseFragment {
    private static final int REQUEST_PICK_PICTURE = 0x1955;
    private static final int REQUEST_TAKE_PICTURE = 0x1956;
    private static final int REQUEST_CROP_PICTURE = 0x1957;

    private static final int IMAGE_SIZE = 300;

    private TextView userNameTv, userPhoneTv;
    private CircleImageView menuLoginImage;

    private LinearLayout menuUnLoginLayout;
    private LinearLayout menuLoginLayout;
    private LinearLayout messagingLayout;// 消息
    private LinearLayout exchangeLayout;// 积分
    private LinearLayout couponLayout;// 优惠券
    private LinearLayout rechargeLayout;// 充值
    private LinearLayout membershipCarLayout;// 会员卡
    private LinearLayout bindCarsManagerLayout;// 绑定车辆管理
    private LinearLayout billingManagerLayout;// 账单管理
    private LinearLayout trunkLayout;// 后备箱
    private TextView settingTv;// 设置
    private TextView homeTv;// 首页

    private TextView rechargeBalanceTv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_menu, container, false);
        initMenuTopView(view);
        initMenuListView(view);

        setLoginView(LocalDataBuffer.getInstance().getUser());

        Resources resources = getResources();
        int width = resources.getDisplayMetrics().widthPixels;
        view.setPadding(0, 0, (int) (width * 0.1f), 0);

        return view;
    }

    private void initMenuTopView(View view) {
        menuUnLoginLayout = (LinearLayout) view.findViewById(R.id.menu_fragment_menu_unlogin_layout);
        menuUnLoginLayout.setOnClickListener(this);

        menuLoginLayout = (LinearLayout) view.findViewById(R.id.menu_fragment_menu_login_layout);

        userNameTv = (TextView) view.findViewById(R.id.menu_fragment_user_name);
        userPhoneTv = (TextView) view.findViewById(R.id.menu_fragment_user_phone);
        menuLoginImage = (CircleImageView) view.findViewById(R.id.menu_fragment_user_image);
        menuLoginImage.setOnClickListener(this);
    }

    private void initMenuListView(View view) {
        // 消息
        messagingLayout = (LinearLayout) view.findViewById(R.id.menu_fragment_messaging_layout);
        messagingLayout.setOnClickListener(this);

        // 积分
        exchangeLayout = (LinearLayout) view.findViewById(R.id.menu_fragment_integral_layout);
        exchangeLayout.setOnClickListener(this);

        // 优惠券
        couponLayout = (LinearLayout) view.findViewById(R.id.menu_fragment_coupon_layout);
        couponLayout.setOnClickListener(this);

        // 充值
        rechargeLayout = (LinearLayout) view.findViewById(R.id.menu_fragment_recharge_layout);
        rechargeLayout.setOnClickListener(this);

        rechargeBalanceTv = (TextView) view.findViewById(R.id.menu_fragment_recharge_balance_tv);

        User user = LocalDataBuffer.getInstance().getUser();
        String moneyStr = "";
        if (user != null) {
            String money = user.getMoney();
            if (!AppUtils.isEmpty(money)) {
                if (money.contains(".")) {
                    int index = money.indexOf(".");
                    if (index > 0) {
                        moneyStr = money.substring(0, index - 2);
                    }
                } else {
                    moneyStr = money.substring(0, money.length() - 2);
                }
            }
        }
        String rechargeBalance = String.format(getString(R.string.menu_fragment_recharge_balance), moneyStr);
        rechargeBalanceTv.setText(rechargeBalance);

        // 会员卡
        membershipCarLayout = (LinearLayout) view.findViewById(R.id.menu_fragment_membership_car_layout);
        membershipCarLayout.setOnClickListener(this);

        // 绑定车辆管理
        bindCarsManagerLayout = (LinearLayout) view.findViewById(R.id.menu_fragment_bind_cars_manager_layout);
        bindCarsManagerLayout.setOnClickListener(this);

        // 账单管理
        billingManagerLayout = (LinearLayout) view.findViewById(R.id.menu_fragment_billing_manager_layout);
        billingManagerLayout.setOnClickListener(this);

        // 后备箱
        trunkLayout = (LinearLayout) view.findViewById(R.id.menu_fragment_trunk_layout);
        trunkLayout.setOnClickListener(this);

        // 设置
        settingTv = (TextView) view.findViewById(R.id.menu_fragment_setting);
        settingTv.setOnClickListener(this);

        // 首页
        homeTv = (TextView) view.findViewById(R.id.menu_fragment_home);
        homeTv.setOnClickListener(this);
    }

    public void setLoginView(User user) {

        if (user == null) {
            menuUnLoginLayout.setVisibility(View.VISIBLE);
            menuLoginLayout.setVisibility(View.GONE);
            menuLoginImage.setImageResource(R.drawable.icon_menu_user_avatar);
            userPhoneTv.setText(null);
            userNameTv.setText(null);

        } else {
            menuUnLoginLayout.setVisibility(View.GONE);
            menuLoginLayout.setVisibility(View.VISIBLE);
            menuLoginLayout.setOnClickListener(this);
            //menuLoginImage.set;
            userPhoneTv.setText(user.getPhoneNumber());
            userNameTv.setText(user.getUserName());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.menu_fragment_menu_unlogin_layout:// 登陆
            mCenter.postNotification(new Notification(AppConstants.NOTIFICATION_TOGGLE_MENU));
            mCenter.postNotification(new Notification(AppConstants.NOTIFICATION_LOGIN));
            break;
        case R.id.menu_fragment_menu_login_layout://设置昵称
        	mCenter.postNotification(new Notification(AppConstants.NOTIFICATION_TOGGLE_MENU));
        	PetNameFragment petNameFragment = new PetNameFragment();
        	 if (isLogin()) {
                 mCenter.postNotification(
                         new Notification(AppConstants.NOTIFICATION_ADD_FRAGMENT, petNameFragment));
             } else {
                 mCenter.postNotification(new Notification(AppConstants.NOTIFICATION_LOGIN, petNameFragment));
             }
            //mCenter.postNotification(new Notification(AppConstants.NOTIFICATION_ADD_FRAGMENT, new PetNameFragment()));
            break;
        case R.id.menu_fragment_user_image:// 头像
            mCenter.postNotification(new Notification(AppConstants.NOTIFICATION_TOGGLE_MENU));
            Fragment accountManageFragment = new AccountManageFragment();
            if (isLogin()) {
                mCenter.postNotification(
                        new Notification(AppConstants.NOTIFICATION_ADD_FRAGMENT, accountManageFragment));
            } else {
                mCenter.postNotification(new Notification(AppConstants.NOTIFICATION_LOGIN, accountManageFragment));
            }

            //selectImage();
            break;

        case R.id.menu_fragment_messaging_layout:// 消息

            break;
        case R.id.menu_fragment_integral_layout:// 积分
            mCenter.postNotification(new Notification(AppConstants.NOTIFICATION_TOGGLE_MENU));
            Fragment fragment = new IntegeralexchangeFragment();
            if (isLogin()) {
                mCenter.postNotification(new Notification(AppConstants.NOTIFICATION_ADD_FRAGMENT, fragment));
            } else {
                mCenter.postNotification(new Notification(AppConstants.NOTIFICATION_LOGIN, fragment));
            }
            break;
        case R.id.menu_fragment_coupon_layout:// 优惠卷
            break;
        case R.id.menu_fragment_recharge_layout:// 充值
            // 关闭侧滑
            mCenter.postNotification(new Notification(AppConstants.NOTIFICATION_TOGGLE_MENU));
            Fragment walletHomeFragment = new WalletHomeFragment();
            if (isLogin()) {
                mCenter.postNotification(new Notification(AppConstants.NOTIFICATION_ADD_FRAGMENT, walletHomeFragment));
            } else {
                mCenter.postNotification(new Notification(AppConstants.NOTIFICATION_LOGIN, walletHomeFragment));
            }
            break;
        case R.id.menu_fragment_membership_car_layout:// 会员卡
            // 关闭侧滑
            mCenter.postNotification(new Notification(AppConstants.NOTIFICATION_TOGGLE_MENU));
            Fragment vipFragment = new VipFragment();
            if (isLogin()) {
                mCenter.postNotification(new Notification(AppConstants.NOTIFICATION_ADD_FRAGMENT, vipFragment));
            } else {
                mCenter.postNotification(new Notification(AppConstants.NOTIFICATION_LOGIN, vipFragment));
            }
            break;
        case R.id.menu_fragment_bind_cars_manager_layout:// 绑定车辆管理
            mCenter.postNotification(new Notification(AppConstants.NOTIFICATION_TOGGLE_MENU));
            Fragment carManagerFragment = new CarManagerFragment();
            if (isLogin()) {
                mCenter.postNotification(new Notification(AppConstants.NOTIFICATION_ADD_FRAGMENT, carManagerFragment));
            } else {
                mCenter.postNotification(new Notification(AppConstants.NOTIFICATION_LOGIN, carManagerFragment));
            }
            break;
        case R.id.menu_fragment_billing_manager_layout:// 账单管理
            mCenter.postNotification(new Notification(AppConstants.NOTIFICATION_TOGGLE_MENU));
            Fragment billManagerFragment = new BillManagerFragment();
            if (isLogin()) {
                mCenter.postNotification(new Notification(AppConstants.NOTIFICATION_ADD_FRAGMENT, billManagerFragment));
            } else {
                mCenter.postNotification(new Notification(AppConstants.NOTIFICATION_LOGIN, billManagerFragment));
            }
            break;
        case R.id.menu_fragment_trunk_layout:// 后备箱
            mCenter.postNotification(new Notification(AppConstants.NOTIFICATION_TOGGLE_MENU));
            Fragment truckFragment = new TruckFragment();
            mCenter.postNotification(new Notification(AppConstants.NOTIFICATION_ADD_FRAGMENT, truckFragment));
            break;
        case R.id.menu_fragment_setting:// 设置
            mCenter.postNotification(new Notification(AppConstants.NOTIFICATION_TOGGLE_MENU));
            mCenter.postNotification(new Notification(AppConstants.NOTIFICATION_ADD_FRAGMENT, new SettingFragment()));
            break;
        case R.id.menu_fragment_home:// 首页
            mCenter.postNotification(new Notification(AppConstants.NOTIFICATION_TOGGLE_MENU));
            mCenter.postNotification(new Notification(AppConstants.NOTIFICATION_GO_HOME));
            break;
        default:
            break;
        }
    }
 
/* private void selectImage() {
        if (isLogin()) {
            AvatarDialog dialog = new AvatarDialog();
            dialog.setOnAvatarClickListener(onAvatarClickListener);
            dialog.show(getFragmentManager(), null);

            // Intent intent = new Intent();
            // intent.setClass(getActivity(), SelectPictureActivity.class);
            // startActivityForResult(intent, REQUEST_CODE_GET_IMAGE);

        } else {
            requestLogin(null);
        }
    }

    private OnAvatarClickListener onAvatarClickListener = new OnAvatarClickListener() {

        @Override
        public void takePicture() {
            Intent intent = new Intent();
            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra("return-data", true);
            try {
                startActivityForResult(intent, REQUEST_TAKE_PICTURE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void pickPicture() {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_PICK);
            intent.setType("image/*");

            intent.putExtra("crop", "true");
            // intent.putExtra("circleCrop", "true");
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("outputX", IMAGE_SIZE);
            intent.putExtra("outputY", IMAGE_SIZE);
            intent.putExtra("return-data", true);

            try {
                startActivityForResult(intent, REQUEST_PICK_PICTURE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private void cropImage(Bitmap bitmap) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setType("image/*");
        intent.putExtra("data", bitmap);
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);//
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", IMAGE_SIZE);//
        intent.putExtra("outputY", IMAGE_SIZE);
        intent.putExtra("return-data", true);

        try {
            startActivityForResult(intent, REQUEST_CROP_PICTURE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        switch (requestCode) {
        case REQUEST_TAKE_PICTURE:
            Bitmap selectedPicture = data.getParcelableExtra("data");
            if (selectedPicture != null) {
                cropImage(selectedPicture);
            }
            break;
        case REQUEST_PICK_PICTURE:
            Bitmap pickedImage = data.getParcelableExtra("data");
            setImage(pickedImage);
            break;
        case REQUEST_CROP_PICTURE:
            Bitmap cropPicture = data.getParcelableExtra("data");
            setImage(cropPicture);
            break;
        default:
            break;
        }
    }

    private void setImage(Bitmap selectedPicture) {
        if (selectedPicture == null) {
            return;
        }

        User user = LocalDataBuffer.getInstance().getUser();
        if (user == null) {
            return;
        }

        menuLoginImage.setImageBitmap(selectedPicture);

    }*/

    @Override
    public void onResume() {
        super.onResume();
        setLoginView(LocalDataBuffer.getInstance().getUser());
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}

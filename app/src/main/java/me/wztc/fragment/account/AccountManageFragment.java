package me.wztc.fragment.account;

import java.io.ByteArrayOutputStream;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wztc.R;
import me.wztc.api.request.account.AccountManageFragmentRequest;
import me.wztc.api.response.APIResponseHandler;
import me.wztc.api.response.account.GetPlatenoResponse;
import me.wztc.dialog.AvatarDialog;
import me.wztc.dialog.AvatarDialog.OnAvatarClickListener;
import me.wztc.fragment.BaseFragment;
import me.wztc.model.User;
import me.wztc.notification.Notification;
import me.wztc.util.AppConstants;
import me.wztc.util.AppUtils;
import me.wztc.util.LocalDataBuffer;
import me.wztc.util.ToastUtil;
import me.wztc.util.UserKeeper;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountManageFragment extends BaseFragment {
    private View view;
    private User accoutUser;

    //private String user;
    /*private String headImage;
	private String userName1;
	private String sex;*/

    private TextView conserveTV;
    private CircleImageView menuLoginImage;
    private LinearLayout photo;
    private LinearLayout accountID;
    private TextView accountNO;
    private LinearLayout userName;
    private TextView userNameSet;
    private LinearLayout changePassword;
    //private LinearLayout accountManagment;
    private TextView back;
    //private ImageButton photoBtn;
    private LinearLayout gender;
    private TextView genderChoice;
    private int mBackStackNesting;

    /* 请求码 */
    private static final int REQUEST_PICK_PICTURE = 0x1955;
    private static final int REQUEST_TAKE_PICTURE = 0x1956;
    private static final int REQUEST_CROP_PICTURE = 0x1957;

    private static final int TOCHANGENAME = 5;

    private static final int IMAGE_SIZE = 300;

    private String name;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @SuppressLint("InflateParams")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_account_manager,
                null);  // View android.view.LayoutInflater.inflate(int resource, ViewGroup root)
        initMenuListView();
        return view;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // set title
        setTitle(R.string.account_manage_fragment_title);
    }

    private void initMenuListView() {
        //保存设置
        conserveTV = (TextView) view.findViewById(R.id.conserveTV);
        conserveTV.setOnClickListener(this);
        //设置头像
        menuLoginImage = (CircleImageView) view.findViewById(R.id.account_manager_frgment_user_image);
        menuLoginImage.setOnClickListener(this);
        //账户ID
        accountID = (LinearLayout) view.findViewById(R.id.account_manager_frgment_accountID);
        accountID.setOnClickListener(this);
        accountNO = (TextView) view.findViewById(R.id.accont_manage_accountID);
        //用户名
        userName = (LinearLayout) view.findViewById(R.id.account_manager_frgment_username);
        userName.setOnClickListener(this);
        userNameSet = (TextView) view.findViewById(R.id.tv_name);
        //性别
        gender = (LinearLayout) view.findViewById(R.id.account_manager_frgment_gender);
        gender.setOnClickListener(this);
        genderChoice = (TextView) view.findViewById(R.id.genderChoice);
        //修改密码
        changePassword = (LinearLayout) view.findViewById(R.id.account_manager_frgment_changePassword);
        changePassword.setOnClickListener(this);
        //注销
        view.findViewById(R.id.account_manager_frgment_logout_button).setOnClickListener(this);
        AccountMessage(LocalDataBuffer.getInstance().getUser());

        userNameSet.addTextChangedListener(accoutWatcher);
        genderChoice.addTextChangedListener(accoutWatcher);

        if (getArguments() != null) {
            name = getArguments().getString(AppConstants.BUNDLE_USERNAME);
            //sex[which]=getArguments().getString(AppConstants.BUNDLE_SEX);
        }
        if (!TextUtils.isEmpty(name)) {
            userNameSet.setText(name);
        }
        /*if(!TextUtils.isEmpty(sex)){
        	genderChoice.setText(sex);
        }*/
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
        case R.id.conserveTV:
            conservation();
            break;
        case R.id.account_manager_frgment_user_image:
            selectImage();
            break;
        case R.id.account_manager_frgment_username:
            mCenter.postNotification(new Notification(AppConstants.NOTIFICATION_ADD_FRAGMENT, new PetNameFragment()));
            break;
        case R.id.account_manager_frgment_gender:
            onGender();
            break;
        case R.id.account_manager_frgment_changePassword:
            mCenter.postNotification(
                    new Notification(AppConstants.NOTIFICATION_ADD_FRAGMENT, new ChangePasswordFragment()));
            break;
        case R.id.account_manager_frgment_logout_button:
            logout();
            break;
        default:
            break;
        }

    }

    private void logout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("提示");
        builder.setMessage("你确定要离开登录吗？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                LocalDataBuffer.getInstance().setUser(null);//.clean();
                UserKeeper.clear(getActivity());
                mCenter.postNotification(new Notification(AppConstants.NOTIFICATION_GO_HOME));
                mCenter.postNotification(new Notification(AppConstants.NOTIFICATION_LOGOUT));
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.create().show();

    }

    private TextWatcher accoutWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String user = s.toString();
            if (AppUtils.isEmpty(user)) {
                conserveTV.setVisibility(View.INVISIBLE);
            } else {
                conserveTV.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    public void AccountMessage(User user) {
        if (user == null) {
            menuLoginImage.setImageResource(R.drawable.icon_menu_user_avatar);
        } else {
            //menuLoginImage.setTag(user.getHeadImage());
            accountNO.setText(user.getPhoneNumber());
            userNameSet.setText(user.getUserName());
            genderChoice.setText(user.getSex());
        }
    }

    /**
     * 设置头像
     */
    private void selectImage() {
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

        /**
         * 裁剪图片方法实现
         *
         */
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

    private Bitmap pickedImage;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
        case REQUEST_TAKE_PICTURE:
            Bitmap selectedPicture = data.getParcelableExtra("data");
            if (selectedPicture != null) {
                cropImage(selectedPicture);
            }
            break;
        case REQUEST_PICK_PICTURE:
            pickedImage = data.getParcelableExtra("data");
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

    /**
     * 保存裁剪之后的图片数据
     */
    private void setImage(Bitmap selectedPicture) {
        if (selectedPicture == null) {
            return;
        }

        User user = LocalDataBuffer.getInstance().getUser();
        if (user == null) {
            return;
        }
        menuLoginImage.setImageBitmap(selectedPicture);
    }

    /**
     * 把图片转成字符串
     * bit流转换
     *
     * @param bitmap
     * @return
     */
    public String bitmaptoString(Bitmap bitmap) {
        if (bitmap == null) {
            return "";
        }
        //初始化一个流对象
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.PNG, 100, output);
        return output.toString();
    }

    /**
     * 性别设置
     */
    @SuppressLint("NewApi")
    private void onGender() {
        //final String sex = User.getInstance().getSex();
       /* new AlertDialog.Builder(getActivity(), mBackStackNesting)
                .setSingleChoiceItems(new String[]{"男士", "女士", "保密"}, 0,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            	if (equals(0)) {
                            		genderChoice.setText("男");
                            		//genderChoice.setTag(sex);
                        		} else if (equals(1)) {
                        			genderChoice.setText("女");
                        			//genderChoice.setTag(sex);
                        		} else if(equals(2)) {
                        			genderChoice.setText("保密");
                        			//genderChoice.setTag(sex);
                        		}
                            	//genderChoice.setTag(sexSure);
                                dialog.dismiss();
                            }
                        }
                ).setNegativeButton(null, null).show();*/
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final String[] sex = {"男", "女", "未知性别"};
        final String sexSure = LocalDataBuffer.getInstance().getUser().getSex();
        builder.setSingleChoiceItems(sex, 1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                User user = LocalDataBuffer.getInstance().getUser();
                if (user == null) {
                    return;
                } else {
                    genderChoice.setText(sex[which]);
                }
                //ToastUtil.toastLong(getActivity(), ""+sex[which]);
	            /*if(!TextUtils.isEmpty(sex[which])){
	            	genderChoice.setText(sex[which]);
	            	genderChoice.setTag(sex);
	            }*/
                dialog.dismiss();
            }
        });
        builder.show();
    }

    /**
     * 账户信息的后台处理
     */
    private void conservation() {
        accoutUser = LocalDataBuffer.getInstance().getUser();
        String userId = accoutUser.getUserId();
        final String headImage = bitmaptoString(pickedImage).getBytes().toString().trim();
        //final String headImage = menuLoginImage.toString().trim();
        final String userName = userNameSet.getText().toString().trim();
        final String sex = genderChoice.getText().toString().trim();
        if (isProcessing) {
            return;
        }
        isProcessing = true;
        showLoadingDialog();
        //后台账户请求设置
        AccountManageFragmentRequest request = new AccountManageFragmentRequest(userId, headImage, userName, sex);
        request.start(new APIResponseHandler<GetPlatenoResponse>() {
            @Override
            public void handleError(String errorCode, String errorMessage) {
                isProcessing = false;
                dismissLoadingDialog();
                if (getActivity() == null) {
                    return;
                }

                ToastUtil.toastLong(getActivity(), errorMessage);
            }

            public void handleResponse(GetPlatenoResponse response) {
                isProcessing = false;
                dismissLoadingDialog();
                if (getActivity() == null) {
                    return;
                }

                //持久化保存user
                UserKeeper.saveUser(getActivity(), response.getUser());
                User user = response.getUser();
                LocalDataBuffer.getInstance().setUser(user);
                ToastUtil.toastLong(getActivity(), "账户信息保存成功");
                mCenter.postNotification(new Notification(AppConstants.NOTIFICATION_GO_BACK));
            }
        });
    }

}
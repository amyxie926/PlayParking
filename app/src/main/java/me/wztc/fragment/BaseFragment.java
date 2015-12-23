package me.wztc.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import com.wztc.R;
import me.wztc.dialog.LoadingDialog;
import me.wztc.notification.Notification;
import me.wztc.notification.NotificationCenter;
import me.wztc.notification.TitleInteface;
import me.wztc.util.AppConstants;
import me.wztc.util.AppUtils;
import me.wztc.util.LocalDataBuffer;
import me.wztc.util.Logger;

public class BaseFragment extends Fragment implements OnClickListener {

    protected NotificationCenter mCenter;
    protected boolean isProcessing = false;
    protected TitleInteface titleInteface;
    protected boolean isInit = false;

    private LoadingDialog loadingDialog;

    public BaseFragment() {
        super();
        mCenter = NotificationCenter.getInstance();
    }

    public void onResumeFragment() {
        Logger.logD(null, getClass().getSimpleName() + "----resume");
    }

    public void onPauseFragment() {
        Logger.logD(null, getClass().getSimpleName() + "----pause");
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        View back = view.findViewById(R.id.header_back);
        if (back != null)
            back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
        case R.id.header_back:
            onGoBack();
            break;

        default:
            break;
        }
    }

    /**
     * fragment入栈动画效果
     *
     * @return 动画配置
     */
    public int[] getAnimationForAdd() {
        return new int[] { R.anim.slide_left_in, R.anim.slide_left_out };
    }

    /**
     * fragment出栈动画效果
     *
     * @return 动画配置
     */
    public int[] getAnimationForRemove() {
        return new int[] { R.anim.slide_right_in, R.anim.slide_right_out };
    }

    public void onGoBack() {
        mCenter.postNotification(new Notification(AppConstants.NOTIFICATION_GO_BACK));
    }

    public boolean isGoBack() {
        return true;
    }

    public void showLoadingDialog() {
        showLoadingDialog(null);
    }

    public void showLoadingDialog(Bundle data) {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(getActivity());
        }
        loadingDialog.setCancelable(false);
        loadingDialog.setMessage(getString(R.string.base_loading));
        if (data != null) {
            String message = data.getString(AppConstants.BUNDLE_LOADING_DIALOG_MESSAGE);
            if (!AppUtils.isEmpty(message))
                loadingDialog.setMessage(message);
            boolean cancelable = data.getBoolean(AppConstants.BUNDLE_LOADING_DIALOG_CANCELABLE, true);
            loadingDialog.setCancelable(cancelable);
        }
        loadingDialog.show();
    }

    public void dismissLoadingDialog() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }

    protected boolean isLogin() {
        return LocalDataBuffer.getInstance().getUser() != null;
    }

    /**
     * 请求登录，并跳转到对应画面
     * @param fragment
     */
    protected void requestLogin(BaseFragment fragment) {
        mCenter.postNotification(new Notification(AppConstants.NOTIFICATION_LOGIN, fragment));
    }

    protected void setTitle(int titleId) {
        Activity activity = getActivity();
        TextView title = (TextView) activity.findViewById(R.id.header_title);
        if (title != null) {
            title.setText(titleId);
        }
    }

    protected void setTitle(String titleName) {
        Activity activity = getActivity();
        TextView title = (TextView) activity.findViewById(R.id.header_title);
        if (title != null) {
            title.setText(titleName);
        }
    }
    /**
	 * 设置右边按钮
	 */
	public void setRightButton(int paramInt,
			OnClickListener paramOnClickListener) {
		this.titleInteface.setRightButton(paramInt, paramOnClickListener);
	}

	//获得右边按钮
	public ImageView getRightButtonFirst() {
		// TODO Auto-generated method stub
		return titleInteface.getRightButtonFirst();
	}

}

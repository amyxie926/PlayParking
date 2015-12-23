package me.wztc.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import com.wztc.R;
import me.wztc.fragment.BaseFragment;
import me.wztc.fragment.HomeFragment;
import me.wztc.fragment.MenuFragment;
import me.wztc.fragment.login.LoginFragment;
import me.wztc.model.User;
import me.wztc.notification.Notification;
import me.wztc.notification.NotificationCenter;
import me.wztc.notification.NotificationCenter.NotificationListener;
import me.wztc.util.AppConstants;
import me.wztc.util.LocalDataBuffer;
import me.wztc.util.UserKeeper;
import me.special.ResideMenu.ResideMenu;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {
    private ArrayList<BaseFragment> fragments;
    private FrameLayout layoutMain;// fragment容器
    private ResideMenu resideMenu;

    private BaseFragment autoJumpFragment;//登录成功后需要自动跳转的页面

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        autoLogin();

        setContentView(R.layout.activity_main);
        layoutMain = (FrameLayout) findViewById(R.id.container);
        fragments = new ArrayList<BaseFragment>();

        mCenter.addNotificationListener(AppConstants.NOTIFICATION_TOGGLE_MENU, syncMenu);
        setUpMenu();
        addFirstPage();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCenter.removeNotificationListener(AppConstants.NOTIFICATION_TOGGLE_MENU, syncMenu);
        fragments.clear();
        fragments = null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 事件总线添加
        mCenter.addNotificationListener(AppConstants.NOTIFICATION_ADD_FRAGMENT, addFragment);
        mCenter.addNotificationListener(AppConstants.NOTIFICATION_GO_BACK, goBack);
        mCenter.addNotificationListener(AppConstants.NOTIFICATION_GO_HOME, goHome);
        mCenter.addNotificationListener(AppConstants.NOTIFICATION_LOGOUT, logout);
        mCenter.addNotificationListener(AppConstants.NOTIFICATION_LOGIN, login);
        mCenter.addNotificationListener(AppConstants.NOTIFICATION_LOGIN_SUCCESS, loginSuccess);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 事件总线移除
        mCenter.removeNotificationListener(AppConstants.NOTIFICATION_ADD_FRAGMENT, addFragment);
        mCenter.removeNotificationListener(AppConstants.NOTIFICATION_GO_BACK, goBack);
        mCenter.removeNotificationListener(AppConstants.NOTIFICATION_GO_HOME, goHome);
        mCenter.removeNotificationListener(AppConstants.NOTIFICATION_LOGOUT, logout);
        mCenter.removeNotificationListener(AppConstants.NOTIFICATION_LOGIN, login);
        mCenter.removeNotificationListener(AppConstants.NOTIFICATION_LOGIN_SUCCESS, loginSuccess);
    }

    private void setUpMenu() {

        // attach to current activity;
        resideMenu = new ResideMenu(this);
        resideMenu.setUse3D(false);
        resideMenu.setBackground(R.drawable.icon_menu_title_bg);
        resideMenu.attachToActivity(this);
//        resideMenu.setMenuListener(menuListener);
        //valid scale factor is between 0.0f and 1.0f. leftmenu'width is 150dip.
        resideMenu.setScaleValue(0.4f);
        resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT);

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return resideMenu.dispatchTouchEvent(ev);
    }

    private void autoLogin() {
        User user = UserKeeper.readUser(this);
        if (user != null) {
            LocalDataBuffer.getInstance().setUser(user);
        }
    }

    /**
     * 添加默认页
     */
    private void addFirstPage() {

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        BaseFragment fragment = new HomeFragment();

        ft.replace(layoutMain.getId(), fragment);
        ft.addToBackStack(null);
        ft.commit();

        fragments.add(fragment);

    }

    private NotificationListener addFragment = new NotificationListener() {

        @Override
        public void notificationReceived(Notification notification) {
            Object obj = notification.getObject();
            if (obj instanceof BaseFragment)
                addFragment((BaseFragment) obj, true);
        }
    };

    private NotificationListener goBack = new NotificationListener() {

        @Override
        public void notificationReceived(Notification notification) {
            goBack();
        }
    };

    private NotificationListener syncMenu = new NotificationListener() {

        @Override
        public void notificationReceived(Notification notification) {
            toggleMenu();
        }
    };

    private NotificationCenter.NotificationListener goHome = new NotificationCenter.NotificationListener() {

        @Override
        public void notificationReceived(Notification notification) {
            if (fragments.size() <= 1)
                return;
            while (fragments.size() > 1) {

                try {
                    getSupportFragmentManager().popBackStack();
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                }
                fragments.remove(getTopFragment());

            }
        }
    };

    private NotificationCenter.NotificationListener logout = new NotificationCenter.NotificationListener() {

        @Override
        public void notificationReceived(Notification notification) {
            logout();
        }
    };

    private NotificationCenter.NotificationListener login = new NotificationCenter.NotificationListener() {

        @Override
        public void notificationReceived(Notification notification) {
            autoJumpFragment = (BaseFragment) notification.getObject();
            addFragment(new LoginFragment(), true);
        }
    };

    private NotificationCenter.NotificationListener loginSuccess = new NotificationCenter.NotificationListener() {

        @Override
        public void notificationReceived(Notification notification) {
            //登录成功后自动跳转
            if (autoJumpFragment != null) {
                addFragment(autoJumpFragment, true);
                autoJumpFragment = null;
            }

            //更新menu
            FragmentManager manager = getSupportFragmentManager();
            Fragment fragment = manager.findFragmentById(R.id.navigation_drawer);
            if (fragment != null && fragment instanceof MenuFragment) {
                MenuFragment f = (MenuFragment) fragment;
                f.setLoginView(LocalDataBuffer.getInstance().getUser());
            }
        }
    };

    /**
     * append fragment to main layout
     */
    private void addFragment(BaseFragment fragment, boolean toBackStack) {
        BaseFragment top = getTopFragment();
        top.onPauseFragment();

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(fragment.getAnimationForAdd()[0], fragment.getAnimationForAdd()[1],
                fragment.getAnimationForRemove()[0], fragment.getAnimationForRemove()[1]);
        ft.replace(layoutMain.getId(), fragment);
        ft.addToBackStack(null);
        ft.commit();

        fragments.add(fragment);
        BaseFragment top2 = getTopFragment();
        top2.onResumeFragment();
    }

    /**
     * display previous fragment
     */
    private void goBack() {
        if (resideMenu.isOpened()) {
            resideMenu.closeMenu();
            return;
        } else {
            if (fragments.size() <= 1)
                return;

            BaseFragment fragment = getTopFragment();
            fragment.onPauseFragment();

            getSupportFragmentManager().popBackStack();

            fragments.remove(fragment);

            BaseFragment fragment2 = getTopFragment();
            fragment2.onResumeFragment();
        }
    }

    /**
     * logout
     */
    private void logout() {
        //更新menu
        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentById(R.id.navigation_drawer);
        if (fragment != null && fragment instanceof MenuFragment) {
            MenuFragment f = (MenuFragment) fragment;
            f.setLoginView(null);
        }
    }

    /**
     * 根据menu状态开关menu
     */
    private void toggleMenu() {
        if (resideMenu.isOpened()) {
            resideMenu.closeMenu();
        } else {
            resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
        }
    }

    private BaseFragment getTopFragment() {
        int size = fragments.size();
        BaseFragment top = fragments.get(size - 1);
        return top;
    }

    protected boolean isLogin() {
        return LocalDataBuffer.getInstance().getUser() != null;
    }


    @Override
    public void onBackPressed() {
        if (resideMenu.isOpened()) {
            resideMenu.closeMenu();
            return;
        } else {
            if (fragments.size() > 1) {
                goBack();
            } else if (fragments.size() == 1) {
                // close
                finish();
            } else {
                super.onBackPressed();
            }
        }
    }
}

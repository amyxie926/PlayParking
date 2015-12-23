package me.wztc.dialog;

import com.wztc.R;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;

public class AvatarDialog extends DialogFragment implements OnClickListener {

    private OnAvatarClickListener onAvatarClickListener;

    public void setOnAvatarClickListener(OnAvatarClickListener onAvatarClickListener) {
        this.onAvatarClickListener = onAvatarClickListener;
    }

    @SuppressLint("InflateParams")
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.layout_avatar, null);

        view.findViewById(R.id.avatar_change_take_picture_layout).setOnClickListener(this);
        view.findViewById(R.id.avatar_change_choose_image_layout).setOnClickListener(this);

        AlertDialog.Builder b = new AlertDialog.Builder(getActivity());

        b.setTitle(R.string.avatar_change_avatar_title);
        b.setPositiveButton(R.string.base_cancel, null);
        b.setView(view);
        return b.create();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.avatar_change_take_picture_layout:// 拍照
            dismiss();
            if (onAvatarClickListener != null) {
                onAvatarClickListener.takePicture();
            }
            break;
        case R.id.avatar_change_choose_image_layout:// 相册选择
            dismiss();
            if (onAvatarClickListener != null) {
                onAvatarClickListener.pickPicture();
            }
            break;

        default:
            break;
        }
    }

    public interface OnAvatarClickListener {
        // 拍照
        void takePicture();

        // 相册选择
        void pickPicture();

    }
}

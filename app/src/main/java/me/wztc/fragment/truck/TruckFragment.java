package me.wztc.fragment.truck;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.wztc.R;
import me.wztc.fragment.BaseFragment;
import me.wztc.notification.Notification;
import me.wztc.util.AppConstants;

public class TruckFragment extends BaseFragment {

	@SuppressWarnings("unused")
	private ImageView trunk_back;
	private ImageView insurance; // 保险
	private ImageView driving; // 爱代驾

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_trunk, container, false);
		init(view);
		return view;
	}

	private void init(View view) {

		insurance = (ImageView) view.findViewById(R.id.Insurance);
		driving = (ImageView) view.findViewById(R.id.Driving);
		insurance.setOnClickListener(this);
		driving.setOnClickListener(this);

	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		setTitle(R.string.truck_fragment__title);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		// 保险
		case R.id.Insurance:
			TruckDetailFragment fragment= new TruckDetailFragment();
			Bundle bundle=new Bundle();
			bundle.putString("insurance", "insurance");
			fragment.setArguments(bundle);
			mCenter.postNotification(new Notification(
					AppConstants.NOTIFICATION_ADD_FRAGMENT,
					fragment));
			break;
		// 爱代驾
		case R.id.Driving:
			TruckDetailFragment fragments= new TruckDetailFragment();
			Bundle bundles=new Bundle();
			bundles.putString("driving", "driving");
			fragments.setArguments(bundles);
			mCenter.postNotification(new Notification(
					AppConstants.NOTIFICATION_ADD_FRAGMENT,
					fragments));
			break;
		}
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

}

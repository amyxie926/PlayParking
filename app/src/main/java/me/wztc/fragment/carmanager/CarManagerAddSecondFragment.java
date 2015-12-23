package me.wztc.fragment.carmanager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.wztc.R;
import me.wztc.adapter.MyGridViewAdapter;
import me.wztc.adapter.MyGridViewAdapter.MyGridViewAdapterCallBack;
import me.wztc.fragment.BaseFragment;
import me.wztc.notification.Notification;
import me.wztc.util.AppConstants;

public class CarManagerAddSecondFragment extends BaseFragment {
	private GridView gv_alphabet;
	private MyGridViewAdapter alphabet_adapter;
	private List<String> alphabet_data;
	public String area;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(
				R.layout.fragment_carmanager_addcar_second, container, false);
		init(view);
		return view;
	}
	private void init(View view) {
		alphabet_data = new ArrayList<String>();
		alphabet_data.add(area);
		
		alphabet_data.addAll(Arrays.asList(getResources().getStringArray(
				R.array.area_number)));
		gv_alphabet = (GridView) view.findViewById(R.id.gv_alphabet);
		if (alphabet_data != null && alphabet_data.size() > 0) {
			alphabet_adapter = new MyGridViewAdapter(getActivity(),
					alphabet_data);
			gv_alphabet.setAdapter(alphabet_adapter);
			
			alphabet_adapter.setCallBack(new MyGridViewAdapterCallBack() {
				@Override
				public void toSecondFragment(String area, int position) {
					CarManagerAddThirdFragment thirdFragment = new CarManagerAddThirdFragment();
					if (position != 0) {
						thirdFragment.area = alphabet_data.get(0);
						thirdFragment.area_no = alphabet_data.get(position);
						mCenter.postNotification(new Notification(
								AppConstants.NOTIFICATION_ADD_FRAGMENT,
								thirdFragment));
					}
				}
			});
		}
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		// set title`
		setTitle(R.string.carmanager_fragment__addcar);
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

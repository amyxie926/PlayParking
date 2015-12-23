package me.wztc.fragment.carmanager;

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

public class CarManagerAddFirstFragment extends BaseFragment implements
		MyGridViewAdapterCallBack {
    private static final View convertView = null;
    private GridView gv_carmanager_hot, gv_carmanager_otherarea;
    private MyGridViewAdapter hot_adapter, other_adapter;
    private List<String> hot_data, other_data;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_carmanager_addcar_first, container, false);
        init(view);
        return view;
    }
    private void init(View view) {
        hot_data = Arrays.asList(getResources().getStringArray(R.array.hot_areas));
        other_data = Arrays.asList(getResources().getStringArray(R.array.other_areas));
        gv_carmanager_hot = (GridView) view.findViewById(R.id.gv_carmanager_hot);
        gv_carmanager_otherarea = (GridView) view.findViewById(R.id.gv_carmanager_otherarea);
        //热门地区
		if (hot_data != null && hot_data.size() > 0) {
			hot_adapter = new MyGridViewAdapter(getActivity(), hot_data);
			gv_carmanager_hot.setAdapter(hot_adapter);
		}
		hot_adapter.setCallBack(this);
		
		//其他地区
		if (other_data != null && other_data.size() > 0) {
			other_adapter = new MyGridViewAdapter(getActivity(), other_data);
			gv_carmanager_otherarea.setAdapter(other_adapter);
		}
		other_adapter.setCallBack(this);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // set title
        setTitle(R.string.carmanager_fragment__addcar);
    }
	@Override
	public void toSecondFragment(String area, int position) {
		CarManagerAddSecondFragment secondFragment = new CarManagerAddSecondFragment();
		secondFragment.area = area;
		mCenter.postNotification(new Notification(
				AppConstants.NOTIFICATION_ADD_FRAGMENT, secondFragment));
	}
	
}

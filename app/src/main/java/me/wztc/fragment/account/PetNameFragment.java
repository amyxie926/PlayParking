package me.wztc.fragment.account;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.wztc.R;
import me.wztc.fragment.BaseFragment;
import me.wztc.notification.Notification;
import me.wztc.util.AppConstants;
import me.wztc.util.AppUtils;

public class PetNameFragment extends BaseFragment {
	private EditText editText1;
	private TextView complete;
	private static final int TOCHANGENAME = 5;
	   @Override
	        public void onCreate(Bundle savedInstanceState) {
	            super.onCreate(savedInstanceState);
	        }
	        
	        @Override
	        public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                Bundle savedInstanceState) {
	            View view = inflater.inflate(R.layout.fragment_pet_name, null);  // View android.view.LayoutInflater.inflate(int resource, ViewGroup root)
	            editText1=(EditText) view.findViewById(R.id.editText1);
	            editText1.setOnClickListener(this);
	            complete=(TextView) view.findViewById(R.id.tv);
	            complete.setOnClickListener(this);
	            return view;
	        }
	        public void onViewCreated(View view, Bundle savedInstanceState) {
	            super.onViewCreated(view, savedInstanceState);
	            // set title
	            setTitle(R.string.name_fragment_title);
	        }
	        
	        public void onClick(View v) {
	        	super.onClick(v);
	        	switch (v.getId()) {
				case R.id.tv:
					AppUtils.hideSoftKb(getActivity(), editText1);
		        	String userName=editText1.getText().toString().trim();
		        	AccountManageFragment fragment = new AccountManageFragment();
		            //设置参数
		            Bundle bundle = new Bundle();
		            bundle.putString(AppConstants.BUNDLE_USERNAME, userName);
		            fragment.setArguments(bundle);
					mCenter.postNotification(new Notification(AppConstants.NOTIFICATION_ADD_FRAGMENT, fragment));
					break;

				default:
					break;
				}
	        }
			@Override
	        public void onPause() {
	            super.onPause();
	        }
	    }
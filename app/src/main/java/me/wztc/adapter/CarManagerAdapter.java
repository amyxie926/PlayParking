package me.wztc.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.wztc.R;
import me.wztc.fragment.carmanager.CarManagerAddFirstFragment;
import me.wztc.notification.Notification;
import me.wztc.notification.NotificationCenter;
import me.wztc.util.AppConstants;
import me.wztc.util.AppUtils;

public class CarManagerAdapter extends BaseAdapter{

	private List<String>datas;
	private Context context;
	private int layout_id;
	private Button button;
	public CarManagerAdapter(Context context ,List<String>datas,int layout_id){
		this.datas=datas;
		this.context=context;
		this.layout_id=layout_id;
	}
	@Override
	public int getCount() {
		return datas.size()+1;
	}

	@Override
	public Object getItem(int position) {
		return datas.get(position);
	}
	
	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder;
		if(datas.size()==position){
			View footView=View.inflate(context, R.layout.car_manager_foot_layout, null);
			button=(Button) footView.findViewById(R.id.ImageView_addcar);
			button.setOnClickListener(new OnClickListener() {
				NotificationCenter 	mCenter = NotificationCenter.getInstance();
				@Override
				public void onClick(View v) {
					mCenter.postNotification(new Notification(
							AppConstants.NOTIFICATION_ADD_FRAGMENT,
							new CarManagerAddFirstFragment()));
					
				}
			});
			return footView;
		}
		String data=datas.get(position);
		
		if(convertView==null){
			convertView=View.inflate(context, layout_id, null);
			holder=new Holder(convertView);
			convertView.setTag(holder);
		}else{
			if(convertView.getTag() instanceof Holder){
				holder=(Holder) convertView.getTag();
			}else{
				convertView=View.inflate(context, layout_id, null);
				holder=new Holder(convertView);
				convertView.setTag(holder);
			}
		}
		holder.tv_no.setText(AppUtils.parseEmpty(data));
		return convertView;
	}
	private class Holder{
		private TextView tv_no;
		public Holder(View view){
			tv_no=(TextView) view.findViewById(R.id.tv_no);
		}
	}

	
}

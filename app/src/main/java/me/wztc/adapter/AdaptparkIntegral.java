package me.wztc.adapter;

import java.util.List;

import net.tsz.afinal.FinalBitmap;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wztc.R;
import me.wztc.model.ModelGoods;

public class AdaptparkIntegral extends BaseAdapter{

	private List<ModelGoods> arraygoods;
	private Context context;
	private FinalBitmap mFb;
	public AdaptparkIntegral(Context context,List<ModelGoods> datas){
		this.context=context;
		this.arraygoods=datas;
		mFb = FinalBitmap.create(context);
		mFb.configLoadingImage(R.drawable.img_default);
	}
	@Override
	public int getCount() {
		if(arraygoods==null){
			return 0;
		}
		return arraygoods.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		CellHolder _cellholder=null;
		if(convertView==null){
			_cellholder=new CellHolder();
			convertView = View.inflate(context,R.layout.item_integral,null);
			_cellholder.img = (ImageView) convertView.findViewById(R.id.imggood);
			_cellholder.tvname = (TextView) convertView.findViewById(R.id.tv_integral_title);
			_cellholder.tvintegral = (TextView) convertView.findViewById(R.id.tv_integral);
			convertView.setTag(_cellholder);
		}else{
			_cellholder=(CellHolder) convertView.getTag();
		}
		ModelGoods modelGood = this.arraygoods.get(position);
		_cellholder.tvname.setText(""+modelGood.getGoods_name());
		_cellholder.tvintegral.setText(""+modelGood.getIntegral());
		mFb.display(_cellholder.img, modelGood.getPic_url());
		return convertView;
	}
	class CellHolder{
		ImageView  img;
		TextView tvname;
		TextView tvintegral;
	}
}

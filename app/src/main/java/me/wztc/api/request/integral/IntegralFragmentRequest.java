package me.wztc.api.request.integral;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import me.wztc.api.request.ApiRequest;
import me.wztc.api.request.integral.IntegralFragmentRequest.IntegralResponse;
import me.wztc.api.response.APIResponse;
import me.wztc.model.ModelGoods;

public class IntegralFragmentRequest extends ApiRequest<IntegralResponse> {
	private int user_Id;
	private int pageStart;
	private int pageSize;

	public IntegralFragmentRequest(int user_Id, int pageStart, int pageSize) {
		this.user_Id = user_Id;
		this.pageStart = pageStart;
		this.pageSize = pageSize;
	}

	@Override
	protected String getServiceHost() {
		return "dev.iparking.me";
	}

	@Override
	protected int getServicePort() {
		return 8080;
	}

	@Override
	protected String serviceComponent() {
		return "/park/goodsInfolist.do";
	}

	@Override
	protected String getJsonParams() {
		JSONObject json = new JSONObject();
		try {
			json.put("user_Id", user_Id);
			json.put("pageStart", pageStart);
			json.put("pageSize", pageSize);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return json.toString();
	}

	@Override
	protected void onResponseReceived(JSONObject json) throws JSONException {
		IntegralResponse response = new IntegralResponse(json);
		responseHandler.handleResponse(response);
	}

	public class IntegralResponse extends APIResponse {
		private List<ModelGoods> goods;
		private Integer status;
		private String msg;

		public IntegralResponse(JSONObject json) {
			goods = new ArrayList<ModelGoods>();
			try {
				status = Integer.valueOf(json.getString("status"));
				if (status == 0) {
					JSONArray _jaParks = json.getJSONArray("dataList");
					for (int i = 0; i < _jaParks.length(); ++i) {
						JSONObject _joPark = _jaParks.getJSONObject(i);
						ModelGoods _good = new ModelGoods();
						_good.setGoods_id(_joPark.getLong("goods_id"));
						_good.setPic_url(_joPark.getString("pic_url"));
						_good.setGoods_name(_joPark.getString("goods_name"));
						_good.setType(_joPark.getString("type"));
						_good.setCount(Integer.valueOf(_joPark
								.getString("count")));
						_good.setIntegral(_joPark.getString("integral"));
						goods.add(_good);
					}
				} else {
					msg = json.getString("msg");
				}
			} catch (Exception e) {
				e.printStackTrace();
				status = 1;
			}
		}

		public List<ModelGoods> getGoods() {
			return goods;
		}

		public Integer getStatus() {
			return status;
		}

		public String getMsg() {
			return msg;
		}
	}
}

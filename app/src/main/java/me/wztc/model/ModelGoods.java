package me.wztc.model;


public class ModelGoods {

	long uid;
	long goods_id;
	String pic_url;
	String goods_name;
	String type;
	int count;
	String integral;
	public long getGoods_id() {
		return goods_id;
	}
	public void setGoods_id(long goods_id) {
		this.goods_id = goods_id;
	}
	public String getPic_url() {
		return pic_url;
	}
	public void setPic_url(String pic_url) {
		this.pic_url = pic_url;
	}
	public String getGoods_name() {
		return goods_name;
	}
	public void setGoods_name(String goods_name) {
		this.goods_name = goods_name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public String getIntegral() {
		return integral;
	}
	public void setIntegral(String integral) {
		this.integral = integral;
	}
	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}
	//
	public static ModelGoods getInstance(){
		if(userInfo==null){
			userInfo=new ModelGoods();
			userInfo.setUid(Long.valueOf(-1));
			return userInfo;
		}
		return userInfo;
	}
	private static ModelGoods userInfo;
	//
	@Override
	public String toString() {
		return "ModelGoods [goods_id=" + goods_id + ", pic_url=" + pic_url
				+ ", goods_name=" + goods_name + ", type=" + type + ", count="
				+ count + ", integral=" + integral + "]";
	}
	
	
}


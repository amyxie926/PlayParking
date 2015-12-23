package me.wztc.notification;

import android.view.View;
import android.widget.ImageView;

/**
 * 标题栏接口
 * 
 * @author Administrator
 * 
 */
public abstract interface TitleInteface {

	/**
	 * 设置右边第二个按钮的文本颜色
	 * 
	 * @param paramInt
	 *            资源文件
	 */
	public abstract void setRightButtonSecondTextColor(int paramInt);

	/**
	 * 设置左边按钮
	 * 
	 * @param paramInt
	 * @param paramOnClickListener
	 */
	public abstract void setLeftButton(int paramInt,
									   View.OnClickListener paramOnClickListener);

	/**
	 * 设置标题文字
	 * 
	 * @param paramCharSequence
	 *            文本
	 */
	public abstract void setTitleText(CharSequence paramCharSequence);

	/**
	 * 设置标题文字
	 * 
	 * @param paramInt
	 *            资源文件
	 */
	public abstract void setTitleText(int paramInt);

	/**
	 * 设置右边按钮
	 * 
	 * @param paramInt
	 * @param paramOnClickListener
	 */
	public abstract void setRightButton(int paramInt,
										View.OnClickListener paramOnClickListener);

	//获得右边按钮
	public abstract ImageView getRightButtonFirst();
	
	/**
	 * 设置右边第二个按钮
	 * 
	 * @param paramInt
	 * @param paramOnClickListener
	 */
	public abstract void setRightButtonSecond(int paramInt,
											  View.OnClickListener paramOnClickListener);

	/**
	 * 设置标题栏背景
	 * 
	 * @param paramInt
	 *            资源文件
	 */
	public abstract void setTitleBgResource(int paramInt);
	/**
	 * 设置标题栏背景
	 * 
	 * @param paramInt
	 *            资源文件
	 */
	public abstract void setTitleBgColorResource(int paramInt);

	/**
	 * 设置标题栏文本颜色
	 * 
	 * @param paramInt
	 *            资源文件
	 */
	public abstract void setTitleTextColorResource(int paramInt);
}
package com.v2gogo.project.adapter;

import com.v2gogo.project.main.image.GlideImageLoader;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewHolder
{

	private SparseArray<View> mViews;
	private int mPosition;
	private View mConvertView;
	private Context mContext;

	public ViewHolder(ViewGroup parent, int layoutId, int position)
	{
		this.mPosition = position;
		this.mViews = new SparseArray<View>();
		this.mContext = parent.getContext();
		mConvertView = LayoutInflater.from(mContext).inflate(layoutId, parent, false);
		mConvertView.setTag(this);
	}

	public static ViewHolder get(int position, View convertView, ViewGroup parent, int layoutId)
	{
		if (convertView == null)
		{
			return new ViewHolder(parent, layoutId, position);
		}
		else
		{
			ViewHolder holder = (ViewHolder) convertView.getTag();
			holder.mPosition = position;
			return holder;
		}
	}

	/**
	 * 通过viewId获取控件
	 * 
	 * @param viewId
	 * @return
	 */
	public <T extends View> T getView(int viewId)
	{
		View view = mViews.get(viewId);
		if (view == null)
		{
			view = mConvertView.findViewById(viewId);
			mViews.put(viewId, view);
		}
		return (T) view;
	}

	public View getConvertView()
	{
		return mConvertView;
	}

	/**
	 * 设置TextView的值
	 * 
	 * @param viewId
	 * @param text
	 * @return
	 */
	public ViewHolder setText(int viewId, String text)
	{
		TextView tView = getView(viewId);
		tView.setText(text);
		return this;
	}

	/**
	 * 设置ImageView的值(通过图片资源)
	 * 
	 * @param viewId
	 * @param text
	 * @return
	 */
	public ViewHolder setImageResource(int viewId, int resId)
	{
		ImageView imgView = getView(viewId);
		imgView.setImageResource(resId);
		return this;
	}

	/**
	 * 设置ImageView的值(通过bitmap)
	 * 
	 * @param viewId
	 * @param bm
	 * @return
	 */
	public ViewHolder setImageResource(int viewId, Bitmap bm)
	{
		ImageView imgView = getView(viewId);
		imgView.setImageBitmap(bm);
		return this;
	}

	/**
	 * 设置ImageView的值(通过图片资源)
	 * 
	 * @param viewId
	 * @param url
	 * @return
	 */
	public ViewHolder setImageURI(int viewId, String url)
	{
		ImageView imgView = getView(viewId);
		// Imageloader.getInstance().loadImg(view,url);
		GlideImageLoader.loadImageWithCircle(mContext, url, imgView);
		return this;

	}
}

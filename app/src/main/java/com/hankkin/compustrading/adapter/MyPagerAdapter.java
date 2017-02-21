package com.hankkin.compustrading.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class MyPagerAdapter extends PagerAdapter {
	List<View> mViewList;
 	public MyPagerAdapter(List<View> viewlist){
		mViewList = viewlist;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mViewList.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return arg0 == arg1;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView(mViewList.get(position));
	}

	@Override
	public int getItemPosition(Object object) {
		// TODO Auto-generated method stub
		return super.getItemPosition(object);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		container.addView(mViewList.get(position));
		return mViewList.get(position);
	}

	public List<View> getViewList() {
		return mViewList;
	}
	/*public void setmViewList(List<View> mViewList) {
		this.mViewList = mViewList;
	}*/
	
}

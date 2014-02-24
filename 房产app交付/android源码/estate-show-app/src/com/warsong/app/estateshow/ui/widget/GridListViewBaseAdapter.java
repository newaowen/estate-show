package com.warsong.app.estateshow.ui.widget;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

/**
 * 使用adapter对listView模拟gridview Created by shalafi on 6/6/13.
 */
public abstract class GridListViewBaseAdapter extends BaseAdapter {

	protected boolean autoFitColumn = true;
	
	// 外部设置点击事件
	public interface ItemClickListener {
		void onItemClicked(View v, int position, long itemId);
	}

	private class CustomOnClickListener implements OnClickListener {

		private int mPosition;

		public CustomOnClickListener(int currentPos) {
			mPosition = currentPos;
		}

		@Override
		public void onClick(View v) {
			if (mItemClickListener != null) {
				mItemClickListener.onItemClicked(v, mPosition, getItemId(mPosition));
			}
		}
	}

	protected int mNumColumns;
	protected Context mContext;
	private ItemClickListener mItemClickListener;

	public GridListViewBaseAdapter(Context context) {
		mContext = context;
		mNumColumns = 1;
	}

	public final void setOnItemClickListener(ItemClickListener listener) {
		mItemClickListener = listener;
	}

	public final int getNumColumns() {
		return mNumColumns;
	}

	public final void setNumColumns(int numColumns) {
		mNumColumns = numColumns;
		notifyDataSetChanged();
	}

	/**
	 * 返回多少行
	 */
	@Override
	public final int getCount() {
		return (int) Math.ceil(getItemCount() * 1f / getNumColumns());
	}

	public abstract int getItemCount();

	protected abstract View getItemView(int position, View view, ViewGroup parent);

	@Override
	public final View getView(int position, View view, ViewGroup viewGroup) {
		ViewGroup layout;
		//计算列宽度
		int columnWidth = getColumnWidth(view, viewGroup);
		// Make it be rows of the number of columns
		if (view == null) {
			// This is items view
			layout = createItemRow(position, columnWidth);
		} else {
			layout = (ViewGroup) view;
			updateItemRow(position, getItemContainerView(layout), columnWidth);
		}
		return layout;
	}
	
	private int getColumnWidth(View view, ViewGroup viewGroup) {
		int columnWidth = 0;
		int padding = getHorizontalPadding();
		if (viewGroup != null) {
			columnWidth = (viewGroup.getWidth() - padding) / mNumColumns;
		} else if (view != null) {
			columnWidth = (view.getWidth() - padding) / mNumColumns;
		}
		return columnWidth;
	}
	
	protected int getHorizontalPadding() {
		return 0;
	}
	
	/**
	 * 获取行容器
	 * @return
	 */
	public ViewGroup getRowView() {
		LinearLayout layout = new LinearLayout(mContext);
		layout.setOrientation(LinearLayout.HORIZONTAL);
		return layout;
	}

	/**
	 * 获取行内元素容器(一般为行容器，也可不同，但必须是row内元素)
	 * @return
	 */
	public ViewGroup getItemContainerView(ViewGroup parent) {
		return parent;
	}

	private ViewGroup createItemRow(int row, int columnWidth) {
		ViewGroup layout = getRowView();
		ViewGroup container = getItemContainerView(layout);
		// Now add the sub views to it
		for (int i = 0; i < mNumColumns; i++) {
			int itemPos = row * mNumColumns + i;
			// Get the new View
			if (itemPos < getItemCount()) { //只添加有效索引内的子item
				View insideView = getItemView(itemPos, null, container);
				container.addView(insideView);
				setItemViewProperty(itemPos, insideView, columnWidth);
			}
		}
		return layout;
	}

	private void updateItemRow(int row, ViewGroup container, int columnWidth) {
		for (int i = 0; i < mNumColumns; i++) {
			int itemPos = row * mNumColumns + i;
			View insideView = container.getChildAt(i);
			if (itemPos < getItemCount()) {
				if (insideView == null) { // 如果原数据没有该view,则新建view
					insideView = getItemView(itemPos, null, container);
					container.addView(insideView);
				}
				setItemViewProperty(itemPos, insideView, columnWidth);
			} else { // 删除多余的view
				if (insideView != null) {
					container.removeView(insideView);
				}
			}
		}
	}

	/**
	 * 更新列宽度
	 * @param pos
	 * @param view
	 * @param columnWidth
	 */
	private void setItemViewProperty(int pos, View view, int columnWidth) {
		if (autoFitColumn) {
			LayoutParams params = view.getLayoutParams();
			params.width = columnWidth;
			view.setLayoutParams(params);
		}
		// 重置点击事件
		view.setOnClickListener(new CustomOnClickListener(pos));
	}

}
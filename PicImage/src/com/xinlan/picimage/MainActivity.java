package com.xinlan.picimage;

import gridview.StickyGridHeadersGridView;
import gridview.StickyGridHeadersGridView.OnHeaderClickListener;
import gridview.StickyGridHeadersSimpleArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import lib.picturechooser.util.ImageLoader;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

public class MainActivity extends Activity {
	private Context mContext;
	private StickyGridHeadersGridView gridView;
	private List<Image> imageList;
	private LayoutInflater mInflater;
	private final ImageLoader imageLoader = new ImageLoader();
	private ImageList data;
	private StickyGridHeadersSimpleArrayAdapter adapter;
	private TextView contentText;

	public boolean isScroll = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mContext = this;
		mInflater = LayoutInflater.from(this);
		gridView = (StickyGridHeadersGridView) findViewById(R.id.gridview);
		contentText = (TextView) findViewById(R.id.content);
		imageList = selectAllImages();
		data = new ImageList(imageList);
		adapter = new StickyGridHeadersSimpleArrayAdapter(this, data,
				R.layout.head_view, R.layout.imageitem);
		gridView.setOnHeaderClickListener(adapter);
		gridView.setAdapter(adapter);

		// gridView.setOnScrollListener(new OnScrollListener() {
		//
		// @Override
		// public void onScrollStateChanged(AbsListView view, int scrollState) {
		// switch(scrollState){
		// case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
		// //System.out.println("SCROLL_STATE_TOUCH_SCROLL");
		// isScroll = true;
		// break;
		// case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
		// //System.out.println("SCROLL_STATE_FLING");
		// isScroll = true;
		// break;
		// case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
		// //System.out.println("SCROLL_STATE_IDLE");
		// isScroll = false;
		// break;
		// }//end swtich
		// }
		//
		// @Override
		// public void onScroll(AbsListView view, int firstVisibleItem,
		// int visibleItemCount, int totalItemCount) {
		//
		// }
		// });

		updateContentText();
	}

	public void updateContentText() {
		contentText.setText(getSelectNum());
	}

	public String getSelectNum() {
		StringBuffer sb = new StringBuffer("选中");
		int total = 0;
		List<TimeImage> imageList = data.getList();
		for (int i = 0, len = imageList.size(); i < len; i++) {
			if (imageList.get(i).isSelected)
				total++;
		}// end for i
		sb.append(total + "");
		return sb.append("张图片").toString();
	}

	// private final class GridAdapter extends BaseAdapter implements
	// StickyGridHeadersSimpleAdapter {
	//
	// @Override
	// public int getCount() {
	// return data.getList().size();
	// }
	//
	// @Override
	// public Object getItem(int position) {
	// return position;
	// }
	//
	// @Override
	// public long getItemId(int position) {
	// return 0;
	// }
	//
	// @Override
	// public View getView(int position, View convertView, ViewGroup parent) {
	//
	// ItemViewHolder viewHolder;
	// if (convertView == null) {
	// convertView = mInflater.inflate(R.layout.imageitem, null);
	// viewHolder = new ItemViewHolder();
	// viewHolder.img = (ImageView) convertView
	// .findViewById(R.id.icon);
	// convertView.setTag(convertView);
	// } else {
	// viewHolder = (ItemViewHolder) convertView.getTag();
	// }
	// imageLoader.DisplayImage(data.getList().get(position).getPath(),
	// viewHolder.img);
	//
	// return convertView;
	// }
	//
	// @Override
	// public long getHeaderId(int position) {
	// return (long) data.getList().get(position).getDateText().hashCode();
	// }
	//
	// @Override
	// public View getHeaderView(int position, View convertView,
	// ViewGroup parent) {
	// HeadViewHolder viewHolder;
	// if (convertView == null) {
	// convertView = mInflater.inflate(R.layout.head_view, null);
	// viewHolder = new HeadViewHolder();
	// viewHolder.titleText = (TextView) convertView
	// .findViewById(R.id.text);
	// convertView.setTag(convertView);
	// } else {
	// viewHolder = (HeadViewHolder) convertView.getTag();
	// }
	// viewHolder.titleText.setText(data.getList().get(position)
	// .getDateText());
	// return convertView;
	// }
	//
	// protected class ItemViewHolder {
	// ImageView img;
	// }
	//
	// protected class HeadViewHolder {
	// TextView titleText;
	// }
	// }// end inner class

	/**
	 * 选择出所有图片
	 */
	private List<Image> selectAllImages() {
		long t1 = System.currentTimeMillis();
		Cursor cur = mContext.getContentResolver().query(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				new String[] { MediaStore.Images.Media.DISPLAY_NAME,
						MediaStore.Images.Media.DATA,
						MediaStore.Images.Media.DATE_TAKEN,
						MediaStore.Images.Media.SIZE }, null, null,
				MediaStore.Images.Media.DATE_TAKEN + " DESC");

		ArrayList<Image> images = new ArrayList<Image>(cur.getCount());

		if (cur != null) {
			if (cur.moveToFirst()) {
				while (!cur.isAfterLast()) {
					Image image = new Image(cur.getString(0), cur.getString(1),
							cur.getLong(2), cur.getInt(3));
					images.add(image);
					cur.moveToNext();
				}// end while
			}
			cur.close();
		}
		System.out.println("所有图片--->" + images.size());
		long t2 = System.currentTimeMillis();
		System.out.println("查询所有图片耗时--->" + (t2 - t1));

		return images;
	}
}// end class

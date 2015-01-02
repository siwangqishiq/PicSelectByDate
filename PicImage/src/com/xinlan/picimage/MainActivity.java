package com.xinlan.picimage;

import gridview.StickyGridHeadersGridView;
import gridview.StickyGridHeadersSimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import lib.picturechooser.util.ImageLoader;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity {
	private Context mContext;
	private StickyGridHeadersGridView gridView;
	private List<Image> imageList;
	private LayoutInflater mInflater;
	private final ImageLoader imageLoader = new ImageLoader();
	private ImageList data;
	private TextView contentText;

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
		gridView.setAdapter(new StickyGridAdapter());
		updateContentText();
	}

	private final class StickyGridAdapter extends BaseAdapter implements
			StickyGridHeadersSimpleAdapter, OnScrollListener {
		ViewGroup.LayoutParams textLayoutParams = new ViewGroup.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		Drawable imgDefault = getResources()
				.getDrawable(R.drawable.ic_launcher);// 载入图片占位符
		ImageCheckBoxListener mImageCheckBoxListener = new ImageCheckBoxListener();
		HeadCheckClick mHeadCheckClick = new HeadCheckClick();
		private HashMap<CheckBox, Image> imageCheckBoxMap = new HashMap<CheckBox, Image>();// 记录所有创建的图片CheckBox
		private HashMap<ImageView, String> headCheckMap = new HashMap<ImageView, String>();// head记录表

		@Override
		public int getCount() {
			return data.getList().size();
		}

		@Override
		public Object getItem(int position) {
			return data.getList().get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder;
			if (convertView == null) {
				viewHolder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.imageitem, null);
				viewHolder.image = (ImageView) convertView
						.findViewById(R.id.icon);
				viewHolder.checkBox = (CheckBox) convertView
						.findViewById(R.id.check_box);
				convertView.setTag(viewHolder);

			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			Image imageData = data.getList().get(position);
			imageLoader.DisplayImage(imageData.getPath(), viewHolder.image);

			imageCheckBoxMap.put(viewHolder.checkBox, imageData);// 更新
			viewHolder.checkBox.setChecked(imageData.isSelected);
			viewHolder.checkBox
					.setOnCheckedChangeListener(mImageCheckBoxListener);

			return convertView;
		}

		@Override
		public long getHeaderId(int position) {
			return data.getList().get(position).getTimeStr().hashCode();
		}

		@Override
		public View getHeaderView(int position, View convertView,
				ViewGroup parent) {
			HeadViewHolder viewHolder;
			if (convertView == null) {
				viewHolder = new HeadViewHolder();
				convertView = mInflater.inflate(R.layout.head_view, null);
				viewHolder.timeText = (TextView) convertView
						.findViewById(R.id.text);
				viewHolder.selectedImage = (ImageView) convertView
						.findViewById(R.id.select_image);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (HeadViewHolder) convertView.getTag();
			}

			headCheckMap.put(viewHolder.selectedImage,
					data.getList().get(position).getTimeStr());

			viewHolder.timeText.setText(data.getList().get(position)
					.getTimeStr());
			viewHolder.selectedImage.setTag(data.getList().get(position)
					.getTimeStr());
			viewHolder.selectedImage.setOnClickListener(mHeadCheckClick);

			if (data.getHeadRecord().get(
					data.getList().get(position).getTimeStr())) {
				viewHolder.selectedImage
						.setImageResource(R.drawable.icon_xuanzhong);
			} else {
				viewHolder.selectedImage
						.setImageResource(R.drawable.icon_weixuanzhong);
			}

			return convertView;
		}

		private final class HeadCheckClick implements OnClickListener {
			@Override
			public void onClick(View v) {
				ImageView imgView = (ImageView) v;
				String curtime = (String) v.getTag();
				boolean selectedValue = !data.getHeadRecord().get(curtime);
				data.getHeadRecord().put(curtime, selectedValue);
				List<Image> subImageList = data.getMap().get(curtime);
				for (int i = 0, len = subImageList.size(); i < len; i++) {
					subImageList.get(i).isSelected = selectedValue;
				}// end for i
				if (selectedValue) {
					imgView.setImageResource(R.drawable.icon_xuanzhong);
				} else {
					imgView.setImageResource(R.drawable.icon_xuanzhong);
				}

				// imageCheckBoxMap.keySet()
				for (CheckBox checkBox : imageCheckBoxMap.keySet()) {// 更新子CheckBox控件
					Image item = imageCheckBoxMap.get(checkBox);
					checkBox.setChecked(item.isSelected);
				}// end for each
					// System.out.println("===>"+curtime+"    "+data.getHeadRecord().get(curtime));
			}
		}// end inner class

		/**
		 * 
		 * @author panyi
		 * 
		 */
		private final class ImageCheckBoxListener implements
				OnCheckedChangeListener {
			private List<ImageView> tempImageList = new ArrayList<ImageView>();

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				CheckBox checkbox = (CheckBox) buttonView;
				imageCheckBoxMap.get(checkbox).isSelected = isChecked;// 更新选中

				updateContentText();

				// 更新头部栏选中状态
				boolean headCheckedIsSelected = false;
				String timeTitle = imageCheckBoxMap.get(checkbox).getTimeStr();// 标题头
				List<Image> subImageList = data.getMap().get(timeTitle);
				int selectedNum = 0;
				int sublen = subImageList.size();
				for (int i = 0; i < sublen; i++) {
					if (subImageList.get(i).isSelected) {
						selectedNum++;
					}
				}// end for i
				if (selectedNum >= sublen) {
					headCheckedIsSelected = true;
				} else {
					headCheckedIsSelected = false;
				}// end if

				tempImageList.clear();
				for (ImageView img : headCheckMap.keySet()) {
					String value = headCheckMap.get(img);
					if (timeTitle.equals(value)) {
						tempImageList.add(img);
					}
				}// end for each

				// TextView s =
				// (TextView)gridView.getStickiedHeader().findViewById(R.id.text);
				// System.out.println("ssss--->"+s.getText());

				for (int i = 0, len = tempImageList.size(); i < len; i++) {
					if (headCheckedIsSelected) {// 选中
						tempImageList.get(i).setImageResource(
								R.drawable.icon_xuanzhong);
						data.getHeadRecord().put(timeTitle, true);
					} else {// 未选中
						tempImageList.get(i).setImageResource(
								R.drawable.icon_weixuanzhong);
						data.getHeadRecord().put(timeTitle, false);
					}
				}// end for i
				gridView.invalidate();// 更新GridView界面
			}
		}// end inner class

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {

		}

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {

		}
	}// end inner class

	public void updateContentText() {
		contentText.setText(getSelectNum());
	}

	public String getSelectNum() {
		StringBuffer sb = new StringBuffer("选中");
		int total = 0;
		List<Image> imageList = data.getList();
		for (int i = 0, len = imageList.size(); i < len; i++) {
			if (imageList.get(i).isSelected)
				total++;
		}// end for i
		sb.append(total + "");
		return sb.append("张图片").toString();
	}

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
		long t2 = System.currentTimeMillis();
		System.out.println("处理图片耗时---->" + (t2 - t1));

		return images;
	}

	static class ViewHolder {
		ImageView image;
		CheckBox checkBox;
	}

	static class HeadViewHolder {
		TextView timeText;
		ImageView selectedImage;
	}
}// end class

/*
 Copyright 2013 Tonic Artos

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */

package gridview;

import gridview.StickyGridHeadersGridView.OnHeaderClickListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lib.picturechooser.util.ImageLoader;

import com.xinlan.picimage.ImageList;
import com.xinlan.picimage.MainActivity;
import com.xinlan.picimage.R;
import com.xinlan.picimage.TimeImage;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

/**
 * @author Tonic Artos
 * @param <T>
 */
public class StickyGridHeadersSimpleArrayAdapter extends BaseAdapter implements
		StickyGridHeadersSimpleAdapter, OnHeaderClickListener {
	protected static final String TAG = StickyGridHeadersSimpleArrayAdapter.class
			.getSimpleName();

	private MainActivity context;
	private int mHeaderResId;

	private LayoutInflater mInflater;
	private ImageList data;
	private List<TimeImage> items;

	private int mItemResId;
	private List<TimeImage> mItems;
	private final ImageLoader imageLoader = new ImageLoader();
	private CheckChange checkChangeListener = new CheckChange();
	private HeadCheckChange headCheckListener = new HeadCheckChange();

	private List<CheckBox> checkBoxList = new ArrayList<CheckBox>();
	private List<CheckBox> headCheckBoxList = new ArrayList<CheckBox>();

	public StickyGridHeadersSimpleArrayAdapter(MainActivity context,
			ImageList data, int headerResId, int itemResId) {
		init(context, data, headerResId, itemResId);
	}

	@Override
	public boolean areAllItemsEnabled() {
		return false;
	}

	@Override
	public int getCount() {
		return mItems.size();
	}

	@Override
	public long getHeaderId(int position) {
		TimeImage item = getItem(position);
		long value = (long) item.getDateText().hashCode();
		return value;
	}

	@Override
	@SuppressWarnings("unchecked")
	public View getHeaderView(int position, View convertView, ViewGroup parent) {
		HeaderViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(mHeaderResId, parent, false);
			holder = new HeaderViewHolder();
			holder.textView = (TextView) convertView.findViewById(R.id.text);
			holder.checkAllBox = (CheckBox) convertView
					.findViewById(R.id.check_all);
			convertView.setTag(holder);

			headCheckBoxList.add(holder.checkAllBox);
		} else {
			holder = (HeaderViewHolder) convertView.getTag();
		}

		// System.out.println("head check box size--->"+headCheckBoxList.size());

		TimeImage item = getItem(position);
		holder.checkAllBox.setTag(item.getDateText());
		List<TimeImage> groups = data.getMap().get(item.getDateText());
		if (isGroupAllSelected(groups)) {
			holder.checkAllBox.setChecked(true);
		} else {
			holder.checkAllBox.setChecked(false);
		}

		holder.checkAllBox.setOnCheckedChangeListener(headCheckListener);
		holder.textView.setText(item.getDateText());

		return convertView;
	}

	private boolean isGroupAllSelected(List<TimeImage> list) {
		for (int i = 0, len = list.size(); i < len; i++) {
			if (!list.get(i).isSelected)
				return false;
		}// end for i
		return true;
	}

	@Override
	public TimeImage getItem(int position) {
		return mItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	@SuppressWarnings("unchecked")
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(mItemResId, parent, false);
			holder = new ViewHolder();
			holder.img = (ImageView) convertView.findViewById(R.id.icon);
			holder.checkBox = (CheckBox) convertView
					.findViewById(R.id.check_box);
			convertView.setTag(holder);

			checkBoxList.add(holder.checkBox);// CheckBox加入记录表
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		TimeImage item = getItem(position);
		imageLoader.DisplayImage(item.getPath(), holder.img);
		holder.checkBox.setTag(item);
		holder.checkBox.setChecked(item.isSelected);

		holder.checkBox.setOnCheckedChangeListener(checkChangeListener);
		// holder.checkBox.setSelected(true);
		// if (item instanceof CharSequence) {
		// holder.textView.setText((CharSequence)item);
		// } else {
		// holder.textView.setText(item.toString());
		// }
		// System.out.println("checkbox 数量---->"+checkBoxList.size());
		return convertView;
	}

	private final class HeadCheckChange implements OnCheckedChangeListener {
		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			String timeImage = (String) buttonView.getTag();
			List<TimeImage> groups = data.getMap().get(timeImage);
			for (int i = 0, len = groups.size(); i < len; i++) {
				groups.get(i).isSelected = isChecked;
			}// end for i

			for (int i = 0, len = checkBoxList.size(); i < len; i++) {
				CheckBox checkBox = checkBoxList.get(i);
				TimeImage item = (TimeImage) checkBox.getTag();
				checkBox.setChecked(item.isSelected);
			}// end for i

			context.updateContentText();
		}
	}// end inner class

	private final class CheckChange implements OnCheckedChangeListener {

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			TimeImage item = (TimeImage) buttonView.getTag();
			item.isSelected = isChecked;
			// System.out.println(data.getName());
			String timeStr = item.getDateText();

			List<TimeImage> groups = data.getMap().get(timeStr);
			int selectNum = 0;
			int unSelectedNum = 0;
			int len = groups.size();
			for (int i = 0; i < len; i++) {
				if (groups.get(i).isSelected) {
					selectNum++;
				} else {
					unSelectedNum++;
				}
			}// end for i

			if (selectNum == len) {// 全选
				//System.out.println("全选"+timeStr);
				for(int i=0,size = headCheckBoxList.size();i<size;i++){
					String viewDateStr = (String)headCheckBoxList.get(i).getTag();
					System.out.println("--->"+viewDateStr);
					if(timeStr.equals(viewDateStr)){
						headCheckBoxList.get(i).setSelected(true);
						break;
					}
				}//end for i
			}
			if (unSelectedNum == len) {// 全不选
				//System.out.println("全不选"+timeStr);
				for(int i=0,size = headCheckBoxList.size();i<size;i++){
					String viewDateStr = (String)headCheckBoxList.get(i).getTag();
					if(timeStr.equals(viewDateStr)){
						headCheckBoxList.get(i).setSelected(false);
						break;
					}
				}//end for i
			}

			context.updateContentText();
		}
	}// end inner class

	private boolean isGroupSelectedAll(String key) {
		List<TimeImage> groups = data.getMap().get(key);
		for (int i = 0, len = groups.size(); i < len; i++) {
			if (!groups.get(i).isSelected)
				return false;
		}// end for i
		return true;
	}

	private boolean isGroupUnSelectedAll(String key) {
		List<TimeImage> groups = data.getMap().get(key);
		for (int i = 0, len = groups.size(); i < len; i++) {
			if (groups.get(i).isSelected)
				return false;
		}// end for i
		return true;
	}

	private void init(MainActivity context, ImageList data, int headerResId,
			int itemResId) {
		this.context = context;
		this.data = data;
		this.mItems = data.getList();
		this.mHeaderResId = headerResId;
		this.mItemResId = itemResId;
		mInflater = LayoutInflater.from(context);
	}

	protected class HeaderViewHolder {
		public TextView textView;
		public CheckBox checkAllBox;
	}

	protected class ViewHolder {
		public ImageView img;
		public CheckBox checkBox;
	}

	@Override
	public void onHeaderClick(AdapterView<?> parent, View view, long id) {
		TextView text = (TextView) view.findViewById(R.id.text);
		String key = text.getText().toString();
		List<TimeImage> groups = data.getMap().get(key);
		System.out.println(id + "    " + text.getText());
		System.out.println("含有图片---->" + groups.size());
	}
}// end class

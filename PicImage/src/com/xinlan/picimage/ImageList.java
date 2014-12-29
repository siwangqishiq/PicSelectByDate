package com.xinlan.picimage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ImageList {
	private static final SimpleDateFormat sdf = new SimpleDateFormat(
			"yyyy年MM月dd日");
	private Map<String, List<TimeImage>> map;
	private List<TimeImage> list;

	public ImageList(List<Image> srcData) {
		map = new HashMap<String, List<TimeImage>>();
		list = new ArrayList<TimeImage>();

		handleData(srcData);
	}

	/**
	 * 按时间排序数据
	 */
	private void handleData(List<Image> srcData) {
		long t1 = System.currentTimeMillis();
		for (int i = 0, len = srcData.size(); i < len; i++) {
			Image item = srcData.get(i);
			Date date = new Date(item.getTaken());
			String timeStr = sdf.format(date);
			TimeImage timeImage = new TimeImage(timeStr, item.getName(),
					item.getPath(), item.getTaken(), item.getSize());
			if (map.containsKey(timeStr)) {
				List<TimeImage> itemList = map.get(timeStr);
				itemList.add(timeImage);
			} else {
				List<TimeImage> itemList = new ArrayList<TimeImage>();
				itemList.add(timeImage);
				map.put(timeStr, itemList);
			}
			
			list.add(timeImage);
		}// end for i
		long t2 = System.currentTimeMillis();
		System.out.println("分类耗时--->" + (t2 - t1));
	}

	public Map<String, List<TimeImage>> getMap() {
		return map;
	}

	public void setMap(Map<String, List<TimeImage>> map) {
		this.map = map;
	}

	public List<TimeImage> getList() {
		return list;
	}

	public void setList(List<TimeImage> list) {
		this.list = list;
	}
}// end class

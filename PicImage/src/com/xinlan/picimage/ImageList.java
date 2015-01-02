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
	private Map<String, List<Image>> map;
	private List<Image> list;
	private HashMap<String,Boolean> headRecord = new HashMap<String,Boolean>();

	public ImageList(List<Image> srcData) {
		map = new HashMap<String, List<Image>>();
		list = new ArrayList<Image>();

		handleData(srcData);
	}

	/**
	 * 
	 */
	private void handleData(List<Image> srcData) {
		long t1 = System.currentTimeMillis();
		for (int i = 0, len = srcData.size(); i < len; i++) {
			Image item = srcData.get(i);
			Date date = new Date(item.getTaken());
			String timeStr = sdf.format(date);
			Image timeImage = new Image(item.getName(),
					item.getPath(), item.getTaken(), item.getSize());
			timeImage.setTimeStr(timeStr);
			if (map.containsKey(timeStr)) {
				List<Image> itemList = map.get(timeStr);
				itemList.add(timeImage);
			} else {
				List<Image> itemList = new ArrayList<Image>();
				itemList.add(timeImage);
				map.put(timeStr, itemList);
				headRecord.put(timeStr, false);
			}
			
			list.add(timeImage);
		}// end for i
		long t2 = System.currentTimeMillis();
		System.out.println("图片分类耗时--->" + (t2 - t1));
	}

	public Map<String, List<Image>> getMap() {
		return map;
	}

	public void setMap(Map<String, List<Image>> map) {
		this.map = map;
	}

	public List<Image> getList() {
		return list;
	}

	public void setList(List<Image> list) {
		this.list = list;
	}
	
	public HashMap<String, Boolean> getHeadRecord() {
		return headRecord;
	}

	public void setHeadRecord(HashMap<String, Boolean> headRecord) {
		this.headRecord = headRecord;
	}
}// end class

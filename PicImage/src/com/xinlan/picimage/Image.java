package com.xinlan.picimage;

public class Image {
	private String name;//图片名
	private String path;//图片路径
	private Long taken;//图片唯一标示
	private Integer size;//图片尺寸
	private String timeStr;
	public boolean isSelected = false;
	
	public Image(String name, String path, Long taken, Integer size) {
		this.name = name;
		this.path = path;
		this.taken = taken;
		this.size = size;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public Long getTaken() {
		return taken;
	}
	public void setTaken(Long taken) {
		this.taken = taken;
	}
	public Integer getSize() {
		return size;
	}
	public void setSize(Integer size) {
		this.size = size;
	}
	public String getTimeStr() {
		return timeStr;
	}
	public void setTimeStr(String timeStr) {
		this.timeStr = timeStr;
	}
}//end class

package com.xinlan.picimage;

public class Image {
	private String name;//名称
	private String path;//路径
	private Long taken;//拍摄时间
	private Integer size;//图片大小
	
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
}//end class

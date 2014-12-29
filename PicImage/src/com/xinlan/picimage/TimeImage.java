package com.xinlan.picimage;


public class TimeImage extends Image {
	private String dateText;
	public boolean isSelected;

	public TimeImage(String dateText, String name, String path, Long taken,
			Integer size) {
		super(name, path, taken, size);
		this.dateText = dateText;
		isSelected = false;
	}

	public String getDateText() {
		return dateText;
	}

	public void setDateText(String dateText) {
		this.dateText = dateText;
	}
}//end class

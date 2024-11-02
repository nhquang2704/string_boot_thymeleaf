package vn.iotstar.model;

import java.io.Serializable;

public class CategoryModel implements Serializable {
	private static final long serialVersionUID = 1L;

	
	private int categoryid;
	private String categoryname;
	private String images;
	private int status;
	private Boolean isEdit = false;
	
	public Boolean getIsEdit() {
	    return isEdit;
	}
	public CategoryModel() {
		super();
	}
	public int getCategoryid() {
		return categoryid;
	}
	public void setCategoryid(int categoryid) {
		this.categoryid = categoryid;
	}
	public String getCategoryname() {
		return categoryname;
	}
	public void setCategoryname(String categoryname) {
		this.categoryname = categoryname;
	}
	public String getImages() {
		return images;
	}
	public void setImages(String images) {
		this.images = images;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
	public void setIsEdit(boolean isEdit) {
	    this.isEdit = isEdit;
	}

	
	
}

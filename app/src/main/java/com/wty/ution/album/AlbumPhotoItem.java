package com.wty.ution.album;

import java.io.Serializable;

public class AlbumPhotoItem implements Serializable {
	private static final long serialVersionUID = 1L;
	private int  photoID;
	private boolean select;
	private String path;
	private String name;
	
	public AlbumPhotoItem(int id,String name,String path,boolean select) {
        photoID = id;
        this.path=path;
        this.select = select;
        this.setName(name);
    }
	
	public AlbumPhotoItem(int id,boolean flag) {
		photoID = id;
		select = flag;
	}
	
	
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public int getPhotoID() {
		return photoID;
	}
	public void setPhotoID(int photoID) {
		this.photoID = photoID;
	}
	public boolean isSelect() {
		return select;
	}
	public void setSelect(boolean select) {
		this.select = select;
	}
	@Override
	public String toString() {
		return "PhotoItem [photoID=" + photoID + ", select=" + select + "]";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}

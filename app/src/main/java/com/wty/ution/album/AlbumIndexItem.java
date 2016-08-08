package com.wty.ution.album;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 功能描述：相册索引界面item
 * @author wty
 **/
public class AlbumIndexItem implements Serializable{

	private static final long serialVersionUID = 1L;

	private String dir_id; //相册id
	private String name;   //相册名字
	private String count; //数量
	private String bitmap;  // 相册第一张图片
	private boolean selected;
	private List<AlbumPhotoItem> bitList = new ArrayList<AlbumPhotoItem>();
	
	public AlbumIndexItem() {
	}
	
	public AlbumIndexItem(String name, String count, String bitmap) {
		super();
		this.name = name;
		this.count = count;
		this.bitmap = bitmap;
	}

	@Override
	public String toString() {
		return "PhotoAibum [name=" + name + ", count=" + count + ", bitmap="
				+ bitmap + ", bitList=" + bitList + "]";
	}
	
	/**
	 * 获取未读的uri
	 * @return
	 */
	public List<String> getSelectedItemUris(){
	    List<String> uris = new ArrayList<String>();
	    for(AlbumPhotoItem item:getBitList()){
	        if(item.isSelect()){
	            uris.add(item.getPath());
	        }
	    }
	    return uris;
	}
	
	public List<String> getSelectedItemIds(){
		List<String> ids = new ArrayList<String>();
	    for(AlbumPhotoItem item:getBitList()){
	        if(item.isSelect()){
	            ids.add(item.getName());
	        }
	    }
	    return ids;
	}



    public boolean isSelected() {
        return selected;
    }
    public void setSelected(boolean selected) {
        this.selected = selected;
    }
    public String getDir_id() {
        return dir_id;
    }
    public void setDir_id(String dir_id) {
        this.dir_id = dir_id;
    }
	public List<AlbumPhotoItem> getBitList() {
		return bitList;
	}
	public void setBitList(List<AlbumPhotoItem> bitList) {
		this.bitList = bitList;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	public String getBitmap() {
		return bitmap;
	}
	public void setBitmap(String bitmap) {
		this.bitmap = bitmap;
	}
}

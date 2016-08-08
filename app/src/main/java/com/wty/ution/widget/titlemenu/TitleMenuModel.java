package com.wty.ution.widget.titlemenu;


public class TitleMenuModel{
	
	private String text;
	private int unread;
	private TitleMenuEvent event;
	private boolean selected;
	private int total;
    private boolean ignoreTotal;
	public TitleMenuModel(String label,int unread,int total,boolean selected,TitleMenuEvent event) {
		this.text = label;
		this.unread = unread;
		this.selected = selected;
		this.event = event;
		this.total = total;
        ignoreTotal = false;
	}

    public TitleMenuModel(String label,boolean selected,TitleMenuEvent event) {
        this.text = label;
        this.selected = selected;
        this.event = event;
        ignoreTotal = true;
    }

	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public void setEvent(TitleMenuEvent event) {
		this.event = event;
	}
	
	public void setSelected(boolean selected){
		this.selected = selected;
	}
	
	public void setUnread(int unread) {
		this.unread = unread;
	}
	
	public void setTotal(int total){
		this.total = total;
	}

	public TitleMenuEvent getEvent() {
		return event;
	}

	public String getModelId() {
		return text;//以label当做tab 的id 。。。
	}

	public boolean isSelected() {
		return selected;
	}

	public int getUnread() {
		return unread;
	}

	public int getTotal() {
		return total;
	}

    public boolean isIgnoreTotal() {
        return ignoreTotal;
    }

    public void setIgnoreTotal(boolean ignoreTotal) {
        this.ignoreTotal = ignoreTotal;
    }

    public interface TitleMenuEvent {
        public void onSelect();
        public void onNormal();
    }
}
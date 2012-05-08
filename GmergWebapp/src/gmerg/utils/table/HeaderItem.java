/*
 * Created on November-2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package gmerg.utils.table;

/**
 * @author Mehran Sharghi
 *
 */
public class HeaderItem {
	String title;
	
	String shortTitle;	// is always equals to the title unless is set specifically

	boolean sortable;

	int type; // 0: Normal   1: Image   2:Checkbox
	
	String mouseOver; 
	
	String imageName;

	public HeaderItem(String title, boolean sortable) {
		this(title, null, sortable, 0, null, null);
	}
	
	public HeaderItem(String title, String shortTitle, boolean sortable) {
		this(title, shortTitle, sortable, 0, null, null);
	}

	public HeaderItem(String title, boolean sortable, String mouseOver) {
		this(title, null, sortable, 0, mouseOver, null);
	}

	public HeaderItem(String title, boolean sortable, int type) {
		this(title, null, sortable, type, null, null);
	}

	public HeaderItem(String title, boolean sortable, int type, String imageName) {
		this(title, null, sortable, type, null, imageName);
	}

	public HeaderItem(String title, String shortTitle, boolean sortable, int type, String mouseOver, String imageName) {
		this.sortable = sortable;
		this.type = type;
		this.title = title;
		this.shortTitle = shortTitle;
		this.imageName = imageName;
		if (mouseOver != null)
			this.mouseOver = mouseOver;
		else
			this.mouseOver = sortable? "Sort by " + title : title;
	}

	public String getTitle() {
		return title;
	}

	public String getMouseOver() {
		return mouseOver;
	}

	public boolean isSortable() {
		return sortable;
	}

	public void setSortable(boolean sortable) {
		this.sortable = sortable;
	}

	public int getType() {
		return type;
	}

	public String getNameEncodedTitle() {
		return DataItem.nameEncoded(title);
	}

	public String getImageName() {
		return imageName;
	}

	public String getShortTitle() {
		if (shortTitle==null)
			return title;
		return shortTitle;
	}

	public void setShortTitle(String shortTitle) {
		this.shortTitle = shortTitle;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public void setMouseOver(String mouseOver) {
		this.mouseOver = mouseOver;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setType(int type) {
		this.type = type;
	}
} 
  

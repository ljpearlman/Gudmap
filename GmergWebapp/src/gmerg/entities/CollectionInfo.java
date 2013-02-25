package gmerg.entities;

import gmerg.assemblers.CollectionAssembler;

import java.util.ArrayList;

public class CollectionInfo {
	
	private int id;
	private String name;
	private int owner;
	private String ownerName;
	private int type;
	private int status;
	private String description;
	private int focusGroup;
	private String focusGroupName;
	private String lastUpdate;
	private int entries;

	private String tutorial;

	// constructor
	public CollectionInfo() {
		// set default values which are not possible in the real time
		// based on the values we can check if the object has been really assigned propertities 
		owner = -1;
		type = -1;
		status = -1;
		focusGroup = -1;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getOwner() {
		return owner;
	}

	public void setOwner(int owner) {
		this.owner = owner;
	}
	
	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}
	
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getFocusGroup() {
		return focusGroup;
	}

	public void setFocusGroup(int focusGroup) {
		this.focusGroup = focusGroup;
	}

	public String getFocusGroupName() {
		return focusGroupName;
	}

	public void setFocusGroupName(String focusGroupName) {
		this.focusGroupName = focusGroupName;
	}

	public String getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(String lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public int getEntries() {
		return entries;
	}

	public void setEntries(int entries) {
		this.entries = entries;
	}

	
	public String getTutorial() {
		return tutorial;
	}

	public void setTutorial(String tutorial) {
		this.tutorial = tutorial;
	}

	public String getAllInfo() {
		String outputString = "name = " + getName() + "\n";
		String str = null;
		str = Globals.getCollectionCategoryName(getType());
		if (null != str)
		    outputString += "type = " + str.toLowerCase() + "\n";
		outputString += "description = " + getDescription() + "\n";
		outputString += "focus group = " + Globals.getFocusGroup(getFocusGroup()) + "\n";
		outputString += "status = " + ((getStatus()==0)? "private" : "public") + "\n";
		ArrayList<String> ids = CollectionAssembler.instance().getCollectionItems(getId());
		//Add collection items IDs 
		if (ids==null)
			return outputString;
		for (int i = 0; i < ids.size(); i++)
			outputString += ((i == 0) ? "" : "\t") + ids.get(i);
		
		return outputString;
	}
	
}

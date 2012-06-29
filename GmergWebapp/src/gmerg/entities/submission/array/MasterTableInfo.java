package gmerg.entities.submission.array;

public class MasterTableInfo {

    protected String id;
    protected String masterId;
    protected String sectionId;
    protected String title;
    protected String description;
    protected String platform;
    
    public MasterTableInfo() {

    }
    
    public MasterTableInfo(String id, String title, String description, String platform) {
    	this.id = id;
    	this.title = title;
    	this.description = description;
    	this.platform = platform;
    }
    
    public MasterTableInfo(String masterId, String sectionId, String title, String description, String platform) {
    	this.masterId = masterId;
    	this.sectionId = sectionId;
    	this.id = masterId + "_" + sectionId;
    	this.title = title;
    	this.description = description;
    	this.platform = platform;
    }
    
    public void print() {
	System.out.println(" id = "+id);
	System.out.println(" masterId = "+masterId);
	System.out.println(" sectionId = "+sectionId);
	System.out.println(" title = "+title);
	System.out.println(" description = "+description);
	System.out.println(" platform = "+platform);
    }
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getMasterId() {
		return this.masterId;
	}
	
	public void setMasterId(String masterId) {
		this.masterId = masterId;
	}
	
	public String getSectionId() {
		return this.sectionId;
	}
	
	public void setSectionId(String sectionId) {
		this.sectionId = sectionId;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

}

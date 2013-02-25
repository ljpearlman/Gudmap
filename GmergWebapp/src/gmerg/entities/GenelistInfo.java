package gmerg.entities;

import java.util.ArrayList;

public class GenelistInfo {
	
	protected String genelistId;
	protected String title;
	protected String filename;
	protected String description;
	protected int[] HeatmapCols;
	protected String summary;
	protected String cdtFileName;
	protected String gtrFileName;
	protected String submitter;
	protected String entryDate;
	protected boolean clustered;
	protected int numberOfEntries;
	protected String platformId;
	protected ArrayList<String> folderIds; // added by xingjun - 05/02/2010
	protected int displayOrder = 0; // xingjun - 27/05/2010 - default value is zero
	
	public GenelistInfo() {
		
	}
	
	public GenelistInfo(String id, String title, String submitter, String description, String fileName, String cdtFileName, String gtrFileName) {
		this(id, title, submitter, description, fileName, cdtFileName, gtrFileName, null);
	}
	
	public GenelistInfo(String id, String title, String submitter, String description, String fileName, String cdtFileName, String gtrFileName, String platformId) {
		this.genelistId = id;
		this.title = title;
		this.description = description;
		this.submitter = submitter;
		this.filename = fileName;
		this.cdtFileName = cdtFileName;
		this.gtrFileName = gtrFileName;
		this.platformId = platformId;
	}
	
	public String getGenelistId() {
		return genelistId;
	}
	
	public void setGenelistId(String genelistId) {
		this.genelistId = genelistId;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public int[] getHeatmapCols() {
		return HeatmapCols;
	}
	
	public void setHeatmapCols(int[] heatmapCols) {
		HeatmapCols = heatmapCols;
	}

	public String getSummary() {
		return summary;
	}
	
	public void setSummary(String summary) {
		this.summary = summary;
	}
	
	public String getSubmitter() {
		return submitter;
	}
	
	public void setSubmitter(String submitter) {
		this.submitter = submitter;
	}
	
	public String getEntryDate() {
		return entryDate;
	}
	
	public void setEntryDate(String entryDate) {
		this.entryDate = entryDate;
	}
	
	public boolean isClustered() {
		return clustered;
	}
	
	public void setClustered(boolean clustered) {
		this.clustered = clustered;
	}
	
	public int getNumberOfEntries() {
		return numberOfEntries;
	}
	
	public void setNumberOfEntries(int numberOfEntries) {
		this.numberOfEntries = numberOfEntries;
	}

	public String getFilename() {
		return filename;
	}
	
	public void setFilename(String filename) {
		this.filename = filename;
	}
	
	public String getCdtFileName() {
		return cdtFileName;
	}
	
	public void setCdtFileName(String cdtFileName) {
		this.cdtFileName = cdtFileName;
	}
	
	public String getGtrFileName() {
		return gtrFileName;
	}

	public void setGtrFileName(String gtrFileName) {
		this.gtrFileName = gtrFileName;
	}

	public String getPlatformId() {
		return platformId;
	}

	public void setPlatformId(String platformId) {
		this.platformId = platformId;
	}
	
	public ArrayList<String> getFolderIds() {
		return folderIds;
	}
	
	public void setFolderIds(ArrayList<String> folderIds) {
		this.folderIds = folderIds;
	}
	
	public int getDisplayOrder() {
		return displayOrder;
	}
	
	public void setDisplayOrder(int displayOrder) {
		this.displayOrder = displayOrder;
	}
	
}

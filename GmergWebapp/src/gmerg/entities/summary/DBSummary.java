package gmerg.entities.summary;

public class DBSummary {

    private String totIshGenes;
    private String totAvailIshSubs;
    private String totEditIshSubs;
    private String totAvailArraySubs;
    private String totEditArraySubs;
    private String lastEditorUpdate;
    private String lastSoftwrUpdate;
    private String lastEntryDate;
    private String databaseServer;
    private String project;

    private String totalAvailableSubmissionsIHC;
    private String totalEditSubmissionsIHC;
    
    private String totIhcGenes;
    private String totTgGenes;
    
    private String applicationVersion;
    
    // added by xingjun - 26/08/2008 - transgenic data
    private String totalAvailableSubmissionsTG;
    
    public void setTotIshGenes(String genes) {
        totIshGenes = genes;
    }

    public String getTotIshGenes() {
        return totIshGenes;
    }

    public void setTotAvailIshSubs(String subs) {
        totAvailIshSubs = subs;
    }

    public String getTotAvailIshSubs() {
        return totAvailIshSubs;
    }

    public void setTotEditIshSubs(String subs) {
        totEditIshSubs = subs;
    }

    public String getTotEditIshSubs() {
        return totEditIshSubs;
    }

    public void setTotAvailArraySubs(String subs) {
        totAvailArraySubs = subs;
    }

    public String getTotAvailArraySubs() {
        return totAvailArraySubs;
    }

    public void setTotEditArraySubs(String subs) {
        totEditArraySubs = subs;
    }

    public String getTotEditArraySubs() {
        return totEditArraySubs;
    }

    public void setLastEditorUpdate(String date) {
        lastEditorUpdate = date;
    }

    public String getLastEditorUpdate() {
        return lastEditorUpdate;
    }

    public void setLastSoftwrUpdate(String date) {
        lastSoftwrUpdate = date;
    }

    public String getLastSoftwrUpdate() {
        return lastSoftwrUpdate;
    }

    public void setLastEntryDate(String date) {
        lastEntryDate = date;
    }

    public String getLastEntryDate() {
        return lastEntryDate;
    }
    
    public String getDatabaseServer() {
    	return this.databaseServer;
    }
    
    public void setDatabaseServer(String databaseServer) {
    	this.databaseServer = databaseServer;
    }
    
    public String getProject() {
    	return this.project;
    }
    
    public void setProject(String project) {
    	this.project = project;
    }
    
    public String getTotalAvailableSubmissionsIHC() {
    	return this.totalAvailableSubmissionsIHC;
    }
    
    public void setTotalAvailableSubmissionsIHC(String totalAvailableSubmissionsIHC) {
    	this.totalAvailableSubmissionsIHC = totalAvailableSubmissionsIHC;
    }
    
    
    public String getTotalEditSubmissionsIHC() {
    	return this.totalEditSubmissionsIHC;
    }
    
    public void setTotalEditSubmissionsIHC(String totalEditSubmissionsIHC) {
    	this.totalEditSubmissionsIHC = totalEditSubmissionsIHC;
    }
    
    // added by xingjun - 26/08/2008 - start - transgenic data need to be added
    public String getTotalAvailableSubmissionsTG() {
    	return this.totalAvailableSubmissionsTG;
    }
    
    public void setTotalAvailableSubmissionsTG(String totalAvailableSubmissionsTG) {
    	this.totalAvailableSubmissionsTG = totalAvailableSubmissionsTG;
    }
    // added by xingjun - 26/08/2008 - end

    public void setTotIhcGenes(String genes) {
        totIhcGenes = genes;
    }

    public String getTotIhcGenes() {
        return totIhcGenes;
    }

    public void setTotTgGenes(String genes) {
        totTgGenes = genes;
    }

    public String getTotTgGenes() {
        return totTgGenes;
    }
    
    public String getApplicationVersion() {
    	return this.applicationVersion;
    }
    
    public void setApplicationVersion(String applicationVersion) {
    	this.applicationVersion = applicationVersion;
    }
    
}

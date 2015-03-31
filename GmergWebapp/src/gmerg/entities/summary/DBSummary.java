package gmerg.entities.summary;

public class DBSummary {

    private String totIshGenes;
    private String totAvailIshSubs;
    private String totEditIshSubs;
    private String totAvailArraySubs;
    private String totEditArraySubs;
    
    private String totIhcGenes;
    private String totalAvailableSubmissionsIHC;
    private String totalEditSubmissionsIHC;
    private String totTgGenes;
    private String totalAvailableSubmissionsTG;
    
    private String totWishGenes;
    private String totalAvailableSubmissionsWISH;
    private String totSishGenes;
    private String totalAvailableSubmissionsSISH;
    private String totOptGenes;
    private String totalAvailableSubmissionsOPT;
    private String totalAvailableSequences;
    
    private String applicationVersion;
    private String lastEditorUpdate;
    private String lastSoftwrUpdate;
    private String lastEntryDate;
    private String databaseServer;
    private String project;
    
    
    
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
    	return databaseServer;
    }
    
    public void setDatabaseServer(String databaseServer) {
    	this.databaseServer = databaseServer;
    }
    
    public String getProject() {
    	return project;
    }
    
    public void setProject(String project) {
    	this.project = project;
    }
    
    public String getTotalAvailableSubmissionsIHC() {
    	return totalAvailableSubmissionsIHC;
    }
    
    public void setTotalAvailableSubmissionsIHC(String totalAvailableSubmissionsIHC) {
    	this.totalAvailableSubmissionsIHC = totalAvailableSubmissionsIHC;
    }
    
    
    public String getTotalEditSubmissionsIHC() {
    	return totalEditSubmissionsIHC;
    }
    
    public void setTotalEditSubmissionsIHC(String totalEditSubmissionsIHC) {
    	this.totalEditSubmissionsIHC = totalEditSubmissionsIHC;
    }
    
    public String getTotalAvailableSubmissionsTG() {
    	return totalAvailableSubmissionsTG;
    }
    
    public void setTotalAvailableSubmissionsTG(String totalAvailableSubmissionsTG) {
    	this.totalAvailableSubmissionsTG = totalAvailableSubmissionsTG;
    }

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
    	return applicationVersion;
    }
    
    public void setApplicationVersion(String applicationVersion) {
    	this.applicationVersion = applicationVersion;
    }
    
    public void setTotWishGenes(String genes){
    	totWishGenes=genes;
    }
    
    public String getTotWishGenes() {
    	return totWishGenes;
    }
    
    public void setTotalAvailableSubmissionsWISH(String totalAvailableSubmissionsWISH) {
    	this.totalAvailableSubmissionsWISH= totalAvailableSubmissionsWISH;
    }
    
    public String getTotalAvailableSubmissionsWISH() {
    	return totalAvailableSubmissionsWISH;
    }
    
    public void setTotSishGenes(String genes){
    	totSishGenes=genes;
    }
    
    public String getTotSishGenes() {
    	return totSishGenes;
    }
    
    public void setTotalAvailableSubmissionsSISH(String totalAvailableSubmissionsSISH) {
    	this.totalAvailableSubmissionsSISH= totalAvailableSubmissionsSISH;
    }
    
    public String getTotalAvailableSubmissionsSISH() {
    	return totalAvailableSubmissionsSISH;
    }
    
    public void setTotOptGenes(String genes){
    	totOptGenes=genes;
    }
    
    public String getTotOptGenes() {
    	return totOptGenes;
    }
    
    public void setTotalAvailableSubmissionsOPT(String totalAvailableSubmissionsOPT) {
    	this.totalAvailableSubmissionsOPT= totalAvailableSubmissionsOPT;
    }
    
    public String getTotalAvailableSubmissionsOPT() {
    	return totalAvailableSubmissionsOPT;
    }

    public void setTotalAvailableSequences(String totalAvailableSequences) {
    	this.totalAvailableSequences= totalAvailableSequences;
    }
    
    public String getTotalAvailableSequences() {
    	return totalAvailableSequences;
    }
    
}

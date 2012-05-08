package gmerg.entities.submission.array;

import java.util.ArrayList;

/**
 * added oid for series object
 * @author xingjun
 *
 */
public class Series {

    private String geoID;
    private String numSamples;
    private String title;
    private String summary;
    private String type;
    private String design;
    private ArrayList summaryResults;
    private int oid;
    private String description;// list of components - xingjun - 02/03/2010
    private int archiveId;// xingjun - 12/08/2010
    
    public void setGeoID(String id) {
        geoID = id;
    }

    public String getGeoID() {
        return geoID;
    }

    public void setNumSamples(String num) {
        numSamples = num;
    }

    public String getNumSamples() {
        return numSamples;
    }

    public void setTitle(String ttl) {
        title = ttl;
    }

    public String getTitle() {
        return title;
    }

    public void setSummary(String summ) {
        summary = summ;
    }

    public String getSummary() {
        return summary;
    }

    public void setType(String typ) {
        type = typ;
    }

    public String getType() {
        return type;
    }

    public void SetDesign(String dsgn) {
        design = dsgn;
    }

    public String getDesign() {
        return design;
    }

    public ArrayList getSummaryResults() {
        return summaryResults;
    }

    public void setSummaryResults(ArrayList values) {
        summaryResults = values;
    }
    
    public void setOid(int oid) {
        this.oid = oid;
    }

    public int getOid() {
        return this.oid;
    }

    // xingjun - 02/03/2010
    public void SetDescription(String description) {
        this.description = description;
    }

    // xingjun - 02/03/2010
    public String getDescription() {
        return this.description;
    }
    
    // xingjun - 12/08/2010 - start
    public int getArchiveId() {
    	return this.archiveId;
    }
    
    public void setArchiveId(int archiveId) {
    	this.archiveId = archiveId;
    }
    //  xingjun - 12/08/2010 - end 

}

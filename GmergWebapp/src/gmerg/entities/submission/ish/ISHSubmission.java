package gmerg.entities.submission.ish;

import gmerg.entities.submission.Antibody;
import gmerg.entities.submission.Person;
import gmerg.entities.submission.Probe;
import gmerg.entities.submission.Specimen;
import gmerg.entities.submission.Submission;
import gmerg.entities.submission.ImageDetail;
import gmerg.entities.submission.ExpressionDetail;

import java.util.ArrayList;
import java.util.List;


public class ISHSubmission extends Submission {

    protected Probe probe;
    protected ArrayList linkedPublications;
    protected String[] acknowledgements;
    protected ArrayList annotationTree;
    protected ArrayList linkedSubmissions;
    protected ExpressionDetail [] annotatedComponents;
    protected Antibody antibody;
    protected String tissue; 
    /** wlzImage: filePath is the wlz download url
     *	          clickFilePath is the iip viewer url
    */
    protected ImageDetail wlzImage = null;

    public ISHSubmission() {
    
    }

    public String getGeneSymbol() {
	String str = assayType.toLowerCase();
	String ret = null;

	if (-1 != str.indexOf("ish")) {
	    if (null != probe)
		ret = probe.getGeneSymbol();
	} else if (-1 == str.indexOf("tg")) {
	    if (null != antibody)
		ret = antibody.getGeneSymbol();
	} else
	    ret = super.getGeneSymbol();
    
	if (null != ret && ret.trim().equals(""))
	    ret = null;
	
	return ret;
    }
    public String getGeneName() {
	String str = assayType.toLowerCase();
	String ret = null;

	if (-1 != str.indexOf("ish")) {
	    if (null != probe)
		ret = probe.getGeneName();
	} else if (-1 == str.indexOf("tg")) {
	    if (null != antibody)
		ret = antibody.getGeneName();
	}
    
	if (null != ret && ret.trim().equals(""))
	    ret = null;
	
	return ret;
    }
    
    public void setProbe(Probe prb) {
        probe = prb;
    }

    public Probe getProbe() {
        return probe;
    }

    public ArrayList getLinkedPublications() {
        return linkedPublications;
    }

    public void setLinkedPublications(ArrayList pubs) {
        linkedPublications = pubs;
    }
    
    public ArrayList getLinkedSubmissions() {
        return linkedSubmissions;
    }

    public void setLinkedSubmissions(ArrayList subs) {
        linkedSubmissions = subs;
    }
    
    public String[] getAcknowledgements() {
        return acknowledgements;
    }

    public void setAcknowledgements(String[] input) {
        acknowledgements = input;
    }

    
    public ArrayList getAnnotationTree(){
        return annotationTree;
    }
    
    public void setAnnotationTree(ArrayList tree){
        annotationTree = tree;
    }
    
    public ExpressionDetail [] getAnnotatedComponents (){
        return annotatedComponents;
    }
    
    public void setAnnotatedComponents(ExpressionDetail [] value) {
        annotatedComponents = value;
    }
    
    public Antibody getAntibody() {
    	return antibody;
    }
    
    public void setAntibody(Antibody antibody) {
    	this.antibody = antibody;
    }
    
    public String getTissue() {
    	return tissue;
    }
    
    public void setTissue(String tissue) {
    	this.tissue = tissue;
    }  
    
    public ImageDetail getWlzImage() {
    	return wlzImage;
    }
    
    public void setWlzImage(ImageDetail input) {
    	wlzImage = input;
    }    
}

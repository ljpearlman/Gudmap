package gmerg.entities.submission.ish;

import gmerg.entities.submission.Antibody;
import gmerg.entities.submission.Person;
import gmerg.entities.submission.Probe;
import gmerg.entities.submission.Specimen;
import gmerg.entities.submission.Submission;
import gmerg.entities.submission.ExpressionDetail;
import gmerg.entities.submission.Transgenic;

import java.util.ArrayList;
import java.util.List;


public class ISHSubmission extends Submission {

    private Probe probe;
    private ArrayList linkedPublications;
    private String[] acknowledgements;
    private ArrayList annotationTree;
    private ArrayList linkedSubmissions;
    private ExpressionDetail [] annotatedComponents;
    private Antibody antibody;
    private Transgenic transgenic;
    private Transgenic[] transgenics = null; // added by xingjun - 09/11/2009
    private boolean multipleTransgenics; // added by xingjun - 09/11/2009
    private String tissue; // added by bernie - 15/09/2010
    private String project; 

    public ISHSubmission() {
    
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
    	return this.antibody;
    }
    
    public void setAntibody(Antibody antibody) {
    	this.antibody = antibody;
    }
    
    // added by xingjun - 27/08/2008
    public Transgenic getTransgenic() {
    	return this.transgenic;
    }
    
    public void setTransgenic(Transgenic transgenic) {
    	this.transgenic = transgenic;
    }

    // added by xingjun - 09/11/2009
    public Transgenic[] getTransgenics() {
    	return this.transgenics;
    }
    
    public void setTransgenics(Transgenic[] transgenics) {
    	this.transgenics = transgenics;
    }

    public boolean isMultipleTransgenics() {
    	return this.multipleTransgenics;
    }
    
    public void setMultipleTransgenics(boolean multipleTransgenics) {
    	this.multipleTransgenics = multipleTransgenics;
    }
    
    // added by Bernie - 23/09/2010
    public String getTissue() {
    	return tissue;
    }
    
    public void setTissue(String tissue) {
    	this.tissue = tissue;
    }  
    
    public String getProject() {
    	return project;
    }
    
    public void setProject(String project) {
    	this.project = project;
    }    
   
}

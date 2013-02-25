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
    private Transgenic[] transgenics = null; 
    private boolean multipleTransgenics; 
    private String tissue; 
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
    	return antibody;
    }
    
    public void setAntibody(Antibody antibody) {
    	this.antibody = antibody;
    }
    
    public Transgenic getTransgenic() {
    	return transgenic;
    }
    
    public void setTransgenic(Transgenic transgenic) {
    	this.transgenic = transgenic;
    }

    public Transgenic[] getTransgenics() {
    	return transgenics;
    }
    
    public void setTransgenics(Transgenic[] transgenics) {
    	this.transgenics = transgenics;
    }

    public boolean isMultipleTransgenics() {
    	return multipleTransgenics;
    }
    
    public void setMultipleTransgenics(boolean multipleTransgenics) {
    	this.multipleTransgenics = multipleTransgenics;
    }
    
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

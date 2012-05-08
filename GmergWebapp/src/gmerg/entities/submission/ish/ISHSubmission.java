package gmerg.entities.submission.ish;

import gmerg.entities.submission.Antibody;
import gmerg.entities.submission.Person;
import gmerg.entities.submission.Probe;
import gmerg.entities.submission.Specimen;
import gmerg.entities.submission.Submission;
import gmerg.entities.submission.ExpressionDetail;
import gmerg.entities.submission.Transgenic;

import java.util.ArrayList;
//import java.util.List;


public class ISHSubmission extends Submission {

    private Probe probe;
    private ArrayList linkedPublications;
//    private String peopleAcknowledged;
    private ArrayList peopleAcknowledged; // modified by XP 05 June 2007
    private ArrayList acknowledgements;
    private ArrayList annotationTree;
    private ArrayList linkedSubmissions;
    private ExpressionDetail [] annotatedComponents;
    private Antibody antibody;
    private Transgenic transgenic;
    private Transgenic[] transgenics = null; // added by xingjun - 09/11/2009
    private boolean multipleTransgenics; // added by xingjun - 09/11/2009
    private String tissue; // added by bernie - 15/09/2010

    public ISHSubmission() {
    
    }
    
    public ISHSubmission(String accID, String stage,
                         Probe probe, Specimen specimen, ArrayList originalImages,
                         String authors, Person pI, Person submitter, ArrayList annotationTree,
                         ArrayList linkedPublications, ArrayList acknowledgements,
                         ExpressionDetail [] annotatedComponents, ArrayList peopleAcknowledged) {

    	super(accID, specimen, pI, submitter, stage, authors, originalImages);
    	this.probe = probe;
    	this.linkedPublications = linkedPublications;
    	this.acknowledgements = acknowledgements;
    	this.annotationTree = annotationTree;
        this.annotatedComponents = annotatedComponents;
        this.peopleAcknowledged = peopleAcknowledged; // added by XP 05 June 2007
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
    
    // modified by XP 05 June 2007 -- String to ArrayList
    public void setPeopleAcknowledged(ArrayList value){
        peopleAcknowledged = value;
    }
    
    // modified by XP 05 June 2007 -- String to ArrayList
    public ArrayList getPeopleAcknowledged(){
        return peopleAcknowledged;
    }

    public void setAcknowledgements(ArrayList acknowls) {
        acknowledgements = acknowls;
    }

    public ArrayList getAcknowledgements() {
        return acknowledgements;
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
}

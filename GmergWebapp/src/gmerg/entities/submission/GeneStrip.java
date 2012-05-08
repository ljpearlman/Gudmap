package gmerg.entities.submission;

import java.util.ArrayList;

public class GeneStrip {
	private ArrayList titles;
	private ArrayList sortTitles;
	private ArrayList colors;
	private ArrayList emptyValues;
	private String expressions;
	
	private boolean selected;
	private String geneSymbol;
	private ArrayList synonym;
	private ArrayList stages;
	private ArrayList probeids;// for Mehran
	private String submissionNumber;
	private ArrayList subids;
	private String thumbnail;
	private String diseaseNumber;
	private ArrayList diseaseids;
	private String genesetNumber;
	private ArrayList genesetids;
	
	
	public ArrayList getColors() {
		return colors;
	}
	public void setColors(ArrayList colors) {
		this.colors = colors;
	}
	public ArrayList getEmptyValues() {
		return emptyValues;
	}
	public void setEmptyValues(ArrayList emptyValues) {
		this.emptyValues = emptyValues;
	}

	public String getExpressions() {
		return expressions;
	}
	public void setExpressions(String expressions) {
		this.expressions = expressions;
	}
	public ArrayList getSortTitles() {
		return sortTitles;
	}
	public void setSortTitles(ArrayList sortTitles) {
		this.sortTitles = sortTitles;
	}
	public ArrayList getTitles() {
		return titles;
	}
	public void setTitles(ArrayList titles) {
		this.titles = titles;
	}
	public ArrayList getDiseaseids() {
		return diseaseids;
	}
	public void setDiseaseids(ArrayList diseaseids) {
		this.diseaseids = diseaseids;
	}
	public String getDiseaseNumber() {
		return diseaseNumber;
	}
	public void setDiseaseNumber(String diseaseNumber) {
		this.diseaseNumber = diseaseNumber;
	}
	public ArrayList getGenesetids() {
		return genesetids;
	}
	public void setGenesetids(ArrayList genesetids) {
		this.genesetids = genesetids;
	}
	public String getGenesetNumber() {
		return genesetNumber;
	}
	public void setGenesetNumber(String genesetNumber) {
		this.genesetNumber = genesetNumber;
	}
	public String getGeneSymbol() {
		return geneSymbol;
	}
	public void setGeneSymbol(String geneSymbol) {
		this.geneSymbol = geneSymbol;
	}
	public ArrayList getProbeids() {
		return probeids;
	}
	public void setProbeids(ArrayList probeids) {
		this.probeids = probeids;
	}
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	public ArrayList getStages() {
		return stages;
	}
	public void setStages(ArrayList stages) {
		this.stages = stages;
	}
	public ArrayList getSubids() {
		return subids;
	}
	public void setSubids(ArrayList subids) {
		this.subids = subids;
	}
	public String getSubmissionNumber() {
		return submissionNumber;
	}
	public void setSubmissionNumber(String submissionNumber) {
		this.submissionNumber = submissionNumber;
	}
	public ArrayList getSynonym() {
		return synonym;
	}
	public void setSynonym(ArrayList synonym) {
		this.synonym = synonym;
	}
	public String getThumbnail() {
		return thumbnail;
	}
	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}
}

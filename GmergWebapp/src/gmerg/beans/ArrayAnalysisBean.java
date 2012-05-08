package gmerg.beans;

import javax.faces.model.SelectItem;
import javax.faces.event.ValueChangeEvent;
import javax.faces.context.FacesContext;
import javax.faces.component.*;
import javax.faces.application.FacesMessage;
import javax.faces.validator.*;
import javax.faces.application.NavigationHandler;

import java.util.*;
import gmerg.model.ArrayAnalysisDelegate;

/**
 * Managed Bean for ArrayAnalysis JSF page
 * 
 * @author Mehran Sharghi
 * 
 */

public class ArrayAnalysisBean {
	boolean samplesLoaded = false;

	String seriesGeoId = "";

	int numGenes = 0;

	int numSamples = 0;

	double minSignal = 0;

	double maxSignal = 0;

	double avgSignal = 0;

	double minStdDev = 0;

	double maxStdDev = 0;

	double avgStdDev = 0;

	boolean presenceFilter = false;

	boolean stdDevFilter = false;

	boolean signalFilter = false;

	int presence = 75;

	double stdDev = 0;

	double signalThreshold = 0;

	int signalPresence = 75;

	int presenceFilterPass = -1;

	int stdDevFilterPass = -1;

	int signalFilterPass = -1;

	int totalFilterPass = -1;

	int clusteringMethod = 0;

	int distanceMeasure = 0;

	boolean visualiseResult = false;
	
	boolean clusterWaiting = false;

	ArrayAnalysisDelegate arrayAnalysisDelegate;
	
	UIInput seriesIdInput; 
	UIInput stdDevInput;
	UIInput signalThresholdInput;
	
	// ********************************************************************************
	// Constructors
	// ********************************************************************************
	public ArrayAnalysisBean() {
		super();
		arrayAnalysisDelegate = new ArrayAnalysisDelegate();
	}

	// ********************************************************************************
	// Action Methods
	// ********************************************************************************
	public String getSamplesWaiting() {
		FacesContext context = FacesContext.getCurrentInstance();
		seriesIdInput.updateModel(context);

		clusterWaiting = false;
		
		if(seriesGeoId==null || seriesGeoId=="")
			return "failed";
		return "waitingPage";
	}
	
	public String getSamples() {
/*		
		for(int i=0; i<1200000000; i++);
		if(true)
			return "success";
*/
		FacesContext context = FacesContext.getCurrentInstance();
		seriesIdInput.updateModel(context);

		if(seriesGeoId==null || seriesGeoId=="")
			return "failed";

		if (arrayAnalysisDelegate.retrieveSamples(seriesGeoId)) {
			numGenes = arrayAnalysisDelegate.getDataSet().getGeneNo();
			numSamples = arrayAnalysisDelegate.getDataSet().getArrayNo();
			double[] results = new double[6];
			arrayAnalysisDelegate.getAnalysis().getStatistics(results);
			minSignal = results[0];
			maxSignal = results[1];
			avgSignal = results[2];
			minStdDev = results[3];
			maxStdDev = results[4];
			avgStdDev = results[5];
			clearAnalysis();
			// set Standard Deviation and Signal threshold initial values equal
			// to their minimum + 40% of the difference between their average
			// and minimum
			signalThreshold = Math.round((results[0] + (results[2] - results[0]) * .4) * 100) / 100.0;
			stdDev = Math.round((results[3] + (results[5] - results[3]) * .4) * 100) / 100.0;
			totalFilterPass = arrayAnalysisDelegate.getAnalysis().getFilteredGeneNo();
			setFilteringRanges();
			samplesLoaded = true;
			refreshPage();
			return "success";
		} else {
			System.out.println("****No sample is retrieved for " + seriesGeoId);
			samplesLoaded = false;
			return "falied";
		}
	}

	public void filter() {
		/* Since the Filter button (button corresponding to this action) is immediate and it is a button, no validation is 
		 * performed and values for other components are not updated. Because there has been no validation 
		 * update model won't work therefore validation for the required components is called first.
		 */  
		FacesContext context = FacesContext.getCurrentInstance();
		UIComponent viewRoot = context.getViewRoot();
		((UISelectOne) viewRoot.findComponent("analysisFilteringForm:presenceChoises")).validate(context);
		((UISelectBoolean) viewRoot.findComponent("analysisFilteringForm:presenceFilter")).validate(context);
		((UISelectBoolean) viewRoot.findComponent("analysisFilteringForm:stdDevFilter")).validate(context);
		((UISelectBoolean) viewRoot.findComponent("analysisFilteringForm:signalFilter")).validate(context);
		((UISelectOne) viewRoot.findComponent("analysisFilteringForm:signalPresenceChoises")).validate(context);
		stdDevInput.validate(context);
		signalThresholdInput.validate(context);
		applyFilters();

		clusterWaiting = false;
	}

	public void clearAll() {
		samplesLoaded = false;
		seriesGeoId = "";
		clearAnalysis();
		refreshPage();
		clusterWaiting = false;
	}

	public void cluster() {
		if(samplesLoaded){
			arrayAnalysisDelegate.clusterDataSet(distanceMeasure, clusteringMethod);
			visualiseResult = true;
			clusterWaiting = true;
		} else {
			FacesMessage message = new FacesMessage();
			message.setSummary("You have to select samples before Clustering.");
			message.setDetail("You have to select samples before Clustering.");
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage("analysisClusteringForm:cluster", message);
		}
	}

	// ********************************************************************************
	// Listeners
	// ********************************************************************************
	public void filterListener(ValueChangeEvent event) {

		applyFilters();

		clusterWaiting = false;

		FacesContext context = FacesContext.getCurrentInstance();
		context.renderResponse();
	}

	// ********************************************************************************
	// Helpers
	// ********************************************************************************
	private void refreshPage() {
		seriesIdInput.setValue(seriesGeoId);
		stdDevInput.setSubmittedValue(String.valueOf(stdDev));
		signalThresholdInput.setSubmittedValue(String.valueOf(signalThreshold));
		// restart the page: this is a hack to force update model that ignored for immediate components
		FacesContext context = FacesContext.getCurrentInstance();
		NavigationHandler navigation = context.getApplication().getNavigationHandler();
		navigation.handleNavigation(FacesContext.getCurrentInstance(), null, "refreshPage");
	}
	
	private void clearAnalysis() {
		presenceFilter = false;
		stdDevFilter = false;
		signalFilter = false;
		presence = 75;
		stdDev = 0;
		signalThreshold = 0;
		signalPresence = 75;
		presenceFilterPass = -1;
		stdDevFilterPass = -1;
		signalFilterPass = -1;
		totalFilterPass = -1;
		clusteringMethod = 0;
		distanceMeasure = 0;
		visualiseResult = false;
	}

	private void setFilteringRanges() {
		Validator[] validators = stdDevInput.getValidators();
		((DoubleRangeValidator) validators[0]).setMinimum(minStdDev);
		((DoubleRangeValidator) validators[0]).setMaximum(maxStdDev);

		validators = signalThresholdInput.getValidators();
		((DoubleRangeValidator) validators[0]).setMinimum(minSignal);
		((DoubleRangeValidator) validators[0]).setMaximum(maxSignal);
	}

	public void applyFilters() {
		FacesContext context = FacesContext.getCurrentInstance();
		if (samplesLoaded) {
			//get new values from components since this action is in responce to immediate components
			UIComponent viewRoot = context.getViewRoot();
			((UISelectOne) viewRoot.findComponent("analysisFilteringForm:presenceChoises")).updateModel(context);
			((UISelectBoolean) viewRoot.findComponent("analysisFilteringForm:presenceFilter")).updateModel(context);
			((UISelectBoolean) viewRoot.findComponent("analysisFilteringForm:stdDevFilter")).updateModel(context);
			((UISelectBoolean) viewRoot.findComponent("analysisFilteringForm:signalFilter")).updateModel(context);
			((UISelectOne) viewRoot.findComponent("analysisFilteringForm:signalPresenceChoises")).updateModel(context);
			stdDevInput.updateModel(context);
			signalThresholdInput.updateModel(context);

			//apply filters
			int[] results = new int[4];
			boolean applyStdDevFilter = stdDevInput.isValid() && stdDevFilter;
			boolean applySignalFilter = signalThresholdInput.isValid() && signalFilter;

			arrayAnalysisDelegate.applyFilter(presenceFilter, applyStdDevFilter, applySignalFilter, 
					                          presence, stdDev, signalThreshold, signalPresence, results);
			presenceFilterPass = results[0];
			stdDevFilterPass = results[1];
			signalFilterPass = results[2];
			totalFilterPass = results[3];
		} else {
			FacesMessage message = new FacesMessage();
			message.setSummary("You have to select samples before Filtering.");
			message.setDetail("You have to select samples before Filtering.");
			context.addMessage("analysisFilteringForm:filter", message);
		}

	}

	// ********************************************************************************
	// Getters & Setter
	// ********************************************************************************
	public ArrayAnalysisDelegate getArrayAnalysisDelegate() {
		return arrayAnalysisDelegate;
	}

	public double getAvgSignal() {
		return avgSignal;
	}

	public double getAvgStdDev() {
		return avgStdDev;
	}

	public int getClusteringMethod() {
		return clusteringMethod;
	}

	public List getClusteringMethodChoises() {
		final String[] clusteringMethods = { "Average linkage",
				"Single linkage", "Maximum linkage", "Centroid linkage" };
		List<SelectItem> clusteringMethodChoises = new ArrayList<SelectItem>();

		for (int i = 0; i < clusteringMethods.length; i++) {
			SelectItem item = new SelectItem(new Integer(i),
					clusteringMethods[i]);
			clusteringMethodChoises.add(item);
		}

		return clusteringMethodChoises;
	}

	public int getDistanceMeasure() {
		return distanceMeasure;
	}

	public List getDistanceMeasureChoises() {
		final String[] distanceMeasures = { "Euclidean distance",
				"City-block distance", "Pearson correlation",
				"Pearson correlation (absolute value)",
				"Uncentered correlation",
				"Uncentered correlation (absolute value)",
				"Spearman's rank correlation", "Kendall's tau" };
		List<SelectItem> distanceChoises = new ArrayList<SelectItem>();

		for (int i = 0; i < distanceMeasures.length; i++) {
			SelectItem item = new SelectItem(new Integer(i),
					distanceMeasures[i]);
			distanceChoises.add(item);
		}

		return distanceChoises;
	}

	public double getMaxSignal() {
		return maxSignal;
	}

	public double getMaxStdDev() {
		return maxStdDev;
	}

	public double getMinSignal() {
		return minSignal;
	}

	public double getMinStdDev() {
		return minStdDev;
	}

	public int getNumGenes() {
		return numGenes;
	}

	public int getNumSamples() {
		return numSamples;
	}

	public int getPresence() {
		return presence;
	}

	public List getPresenceChoises() {
		List<SelectItem> presenceChoises = new ArrayList<SelectItem>();

		for (int i = 0; i < 17; i++) {
			SelectItem item = new SelectItem(new Integer(100 - i * 5), String.valueOf(100 - i * 5) + "%");
			presenceChoises.add(item);
		}

		return presenceChoises;
	}

	public int getPresenceFilterPass() {
		return presenceFilterPass;
	}

	public String getSeriesGeoId() {
		return seriesGeoId;
	}

	public List getSeriesGeoIds() {
		String[] seriesGeoIds = arrayAnalysisDelegate.getSeriesGeoIds();

		List<SelectItem> geoIds = new ArrayList<SelectItem>();
		if (!samplesLoaded) {
			SelectItem item = new SelectItem("");
			geoIds.add(item);
		}

		for (int i = 0; i < seriesGeoIds.length; i++) {
			SelectItem item = new SelectItem(seriesGeoIds[i]);
			geoIds.add(item);
		}
		return geoIds;
	}

	public int getSignalFilterPass() {
		return signalFilterPass;
	}

	public int getSignalPresence() {
		return signalPresence;
	}

	public List getSignalPresenceChoises() {
		List<SelectItem> signalPresenceChoises = new ArrayList<SelectItem>();

		for (int i = 0; i < 11; i++) {
			SelectItem item = new SelectItem(new Integer(100 - i * 5), String.valueOf(100 - i * 5) + "%");
			signalPresenceChoises.add(item);
		}

		return signalPresenceChoises;
	}

	public double getSignalThreshold() {
		return signalThreshold;
	}

	public double getStdDev() {
		return stdDev;
	}

	public int getStdDevFilterPass() {
		return stdDevFilterPass;
	}

	public int getTotalFilterPass() {
		return totalFilterPass;
	}

	public boolean isPresenceFilter() {
		return presenceFilter;
	}

	public boolean isSamplesLoaded() {
		return samplesLoaded;
	}

	public boolean isSignalFilter() {
		return signalFilter;
	}

	public boolean isStdDevFilter() {
		return stdDevFilter;
	}

	public boolean isVisualiseResult() {
		return visualiseResult;
	}

	public void setArrayAnalysisDelegate(
			ArrayAnalysisDelegate arrayAnalysisDelegate) {
		this.arrayAnalysisDelegate = arrayAnalysisDelegate;
	}

	public void setAvgSignal(double avgSignal) {
		this.avgSignal = avgSignal;
	}

	public void setAvgStdDev(double avgStdDev) {
		this.avgStdDev = avgStdDev;
	}

	public void setClusteringMethod(int clusteringMethod) {
		this.clusteringMethod = clusteringMethod;
	}

	public void setDistanceMeasure(int distanceMeasure) {
		this.distanceMeasure = distanceMeasure;
	}

	public void setMaxSignal(double maxSignal) {
		this.maxSignal = maxSignal;
	}

	public void setMaxStdDev(double maxStdDev) {
		this.maxStdDev = maxStdDev;
	}

	public void setMinSignal(double minSignal) {
		this.minSignal = minSignal;
	}

	public void setMinStdDev(double minStdDev) {
		this.minStdDev = minStdDev;
	}

	public void setNumGenes(int numGenes) {
		this.numGenes = numGenes;
	}

	public void setNumSamples(int numSamples) {
		this.numSamples = numSamples;
	}

	public void setPresence(int presence) {
		this.presence = presence;
	}

	public void setPresenceFilter(boolean presenceFilter) {
		this.presenceFilter = presenceFilter;
	}

	public void setPresenceFilterPass(int presenceFilterPass) {
		this.presenceFilterPass = presenceFilterPass;
	}

	public void setSamplesLoaded(boolean samplesLoaded) {
		this.samplesLoaded = samplesLoaded;
	}

	public void setSeriesGeoId(String seriesGeoId) {
		this.seriesGeoId = seriesGeoId;
	}

	public void setSignalFilter(boolean signalFilter) {
		this.signalFilter = signalFilter;
	}

	public void setSignalFilterPass(int signalFilterPass) {
		this.signalFilterPass = signalFilterPass;
	}

	public void setSignalPresence(int signalPresence) {
		this.signalPresence = signalPresence;
	}

	public void setSignalThreshold(double signalThreshold) {
		this.signalThreshold = signalThreshold;
	}

	public void setStdDev(double stdDev) {
		this.stdDev = stdDev;
	}

	public void setStdDevFilter(boolean stdDevFilter) {
		this.stdDevFilter = stdDevFilter;
	}

	public void setStdDevFilterPass(int stdDevFilterPass) {
		this.stdDevFilterPass = stdDevFilterPass;
	}

	public void setTotalFilterPass(int totalFilterPass) {
		this.totalFilterPass = totalFilterPass;
	}

	public void setVisualiseResult(boolean visualiseResult) {
		this.visualiseResult = visualiseResult;
	}

	public UIInput getSeriesIdInput() {
		return seriesIdInput;
	}
	public void setSeriesIdInput(UIInput seriesIdInput) {
		this.seriesIdInput = seriesIdInput;
	}

	public UIInput getSignalThresholdInput() {
		return signalThresholdInput;
	}

	public void setSignalThresholdInput(UIInput signalThresholdInput) {
		this.signalThresholdInput = signalThresholdInput;
	}

	public UIInput getStdDevInput() {
		return stdDevInput;
	}

	public void setStdDevInput(UIInput stdDevInput) {
		this.stdDevInput = stdDevInput;
	}

	public boolean isClusterWaiting() {
		return clusterWaiting;
	}

	public void setClusterWaiting(boolean clusterWaiting) {
		this.clusterWaiting = clusterWaiting;
	}
	
	public int getGeneNumLimit() {
//		return ArrayAnalysisDelegate.getGeneNumLimit();
		return 1000;
	}


}

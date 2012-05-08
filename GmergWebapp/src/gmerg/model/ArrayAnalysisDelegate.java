package gmerg.model;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletOutputStream;

import java.util.ArrayList;
import java.io.*;

import gmerg.assemblers.ArrayAnalysisAssembler;
import analysis.AnalysisResultWriter;
import analysis.Analysis;
import analysis.DataSet;

/**
 * Bussiness logic for ArrayAnalysis JSF page
 * 
 * @author Mehran Sharghi
 * 
 */

public class ArrayAnalysisDelegate {
	static int GENE_NO_LIMIT = 1000;

	DataSet dataSet;

	Analysis analysis;

	String[] seriesGeoIds;

	DataSet clusteredDataSet;
	
	ArrayAnalysisAssembler arrayAnalysisAssembler;

	/********************************************************************************
	 * Constructor
	 *
	 */
	public ArrayAnalysisDelegate() {
		dataSet = null;
		analysis = null;
		seriesGeoIds = null;
		clusteredDataSet = null;
		arrayAnalysisAssembler = new ArrayAnalysisAssembler();
	}

	/******************************************************************************
	 * Retrieve sample ids of all samples associated with a series from the database
	 *    
	 */
	public boolean retrieveSamples(String seriesGeoId) {
		String[][] samplesInfo = null;

		samplesInfo = arrayAnalysisAssembler.retrieveSampleIds(seriesGeoId);
		if (samplesInfo != null) {
			dataSet = arrayAnalysisAssembler.retrieveExpressionData(samplesInfo);
			analysis = new Analysis(dataSet, GENE_NO_LIMIT);
			return true;
		} else
			return false;
	}

	/******************************************************************************
	 * Retrive all series GeoIds from databse
	 * 
	 */
	public String[] getSeriesGeoIds() {
		if (seriesGeoIds == null) {
			ArrayList temp = arrayAnalysisAssembler.getSeriesGEOIds();
			seriesGeoIds = new String[temp.size()];
			for (int i = 0; i < temp.size(); i++)
				seriesGeoIds[i] = (String) temp.get(i);
		}
		return seriesGeoIds;
	}

	/***********************************************************************************************
	 * Apply all filters to data
	 *    
	 */
	public void applyFilter(boolean presenceFilter, boolean stdDevFilter,
			boolean signalFilter, int presence, double stdDev,
			double signalThreshold, int signalPresence, int results[]) {

		analysis.resetFilterMask();

		results[0] = -1;
		results[1] = -1;
		results[2] = -1;

		// Filter data
		if (presenceFilter)
			results[0] = analysis.filterNotPresentData(presence);

		if (stdDevFilter)
			results[1] = analysis.filterNotChangingData(stdDev);

		if (signalFilter)
			results[2] = analysis.filterLowSignalData(signalPresence,
					signalThreshold);

		if (presenceFilter || stdDevFilter || signalFilter)
			results[3] = analysis.getFilteredGeneNo();
		else
			results[3] = analysis.filterNoData();
	}

	/******************************************************************************
	 * Cluster filtered data set
	 *  
	 */
	public void clusterDataSet(int distance, int clustering) {
//		System.out.println("in clustert DataSet --- distance measure=" + distance + "clustering method="+ clustering);

		if (analysis == null) {
//			System.out.println("Error!  analysis is null. (in cluster data set)");
			return;
		}

		// Cluster data (after filtering & normalization)

		clusteredDataSet = analysis.clusterData(distance, clustering, 'r');
//		System.out.println("Data is successfuly clustered.");
	}

	/********************************************************************************
	 * Send filterd data as a DataSet object to be used by TreeView applet
	 * 
	 */
	public void sendDataSet(HttpServletResponse response) {
//		System.out.println("in sendDataSet");

		if (clusteredDataSet == null) {
//			System.out.println("Error!  clustered data set is null. (sendDataSet)");
			return;
		}

		// Send Data to Visualisation Applet
		AnalysisResultWriter resultWriter = new AnalysisResultWriter(clusteredDataSet);
		int[] geneOrder = resultWriter.sortTree();

		clusteredDataSet.setGeneOrder(geneOrder);
		clusteredDataSet.setMask(null); // remove unncessary data to minimize transfer time

		ObjectOutputStream outputToApplet;
		try {

			// String s=clusteredDataSet.getClass().getName();
			// System.out.println("class="+s);
			// response.setContentType("java-internal/" + s);

			response.setContentType("application/octet-stream");
			// response.setContentType("application/x-java-serialized-object");
			ServletOutputStream servletOutput = response.getOutputStream();
			outputToApplet = new ObjectOutputStream(servletOutput);
			outputToApplet.writeObject(clusteredDataSet);
			outputToApplet.flush();
			outputToApplet.close();
//			System.out.println("Data transmission complete.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//********************************************************************************
	// Getters & Setters
	// ********************************************************************************
	
	public Analysis getAnalysis() {
		return analysis;
	}

	public void setAnalysis(Analysis analysis) {
		this.analysis = analysis;
	}

	public DataSet getDataSet() {
		return dataSet;
	}

	public void setDataSet(DataSet dataSet) {
		this.dataSet = dataSet;
	}
	
	public static int getGeneNumLimit() {
		return GENE_NO_LIMIT;
	}

}
package gmerg.entities;

import java.util.Arrays;

public class HeatmapData {
	double[][] data;
	double[] median;
	double[] stdDev;
	
	
	public HeatmapData(){
		data = null; 
		median = null;
		stdDev = null;
	}
	
	public HeatmapData(double[][] data){
		this(data, null, null);
	}
	
	public HeatmapData(double[][]data, double[]median, double[]stdDev) {
		this.data = data;
		this.median = median;
		this.stdDev = stdDev;
		if (data!= null) {
			int numRows = data.length;
			if (median==null) {
				median = new double[numRows];
				for (int i=0; i<numRows; i++) 
					median[i] = findMedian(data[i]);
			}
			if (stdDev==null) {
				stdDev = new double[numRows];
				for (int i=0; i<numRows; i++) 
					stdDev[i] = findStdDev(data[i]);
			}
		}
	}
	
	public static double findMedian(double[] data) {
		int n = data.length;
		double[] temp = new double[n]; 
		System.arraycopy(data, 0, temp, 0, n);
		Arrays.sort(temp);
		int mid = n / 2;
		return (n%2==0)? (temp[mid-1]+temp[mid])/2.0 : temp[mid];   
			
	}
	
	public static double findStdDev(double[] data) {
		double mean = findMean(data);
		double sum =0;
		//Find standard deviation
		for (int i=0; i<data.length; i++)
			sum += (data[i]-mean) * (data[i]-mean);
		return Math.sqrt(sum/data.length);
	}
	
	public static double findMean(double[] data) {
		double sum =0;
		for (int i=0; i<data.length; i++)
			sum += data[i];
		return sum/data.length;
		
	}

	public double[][] getExpression() {
		return data;
	}

	public void setExpression(double[][] expression) {
		this.data = expression;
	}

	public double[] getMedian() {
		return median;
	}

	public void setMedian(double[] median) {
		this.median = median;
	}

	public double[] getStdDev() {
		return stdDev;
	}

	public void setStdDev(double[] stdDev) {
		this.stdDev = stdDev;
	}
	
}	

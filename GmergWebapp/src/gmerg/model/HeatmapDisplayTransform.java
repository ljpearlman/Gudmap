/**
 * 
 */
package gmerg.model;

import gmerg.entities.HeatmapData;

import java.util.Arrays;

/**
 * @author xingjun
 * 
 * used for processed gene list display page
 *
 */
public class HeatmapDisplayTransform {
	
	private double scaleFactor;
	private double upContrast;
	private double downContrast;
	private double zeroOffset;
	private double limit;
	private double scaledLimit;
	    
	public HeatmapDisplayTransform () {
		this(1, 1, 1, 1, 0);
	}
	
	public HeatmapDisplayTransform (double upContrast, double downContrast, double limit, double zeroOffset) {
		this(1, upContrast, downContrast, limit, zeroOffset);
	}
	
	public HeatmapDisplayTransform (double scaleFactor, double upContrast, double downContrast, double limit, double zeroOffset) {
		this.scaleFactor = scaleFactor;
		this.upContrast = upContrast;
		this.downContrast = downContrast;
		this.limit = limit;
		this.zeroOffset = zeroOffset;
		scaledLimit = Math.abs(limit * scaleFactor);;
	}
	

	public void setDisplayAdjustParameters(double upContrast, double downContrast, double limit, double zeroOffset) {
		setDisplayAdjustParameters(1, upContrast, downContrast, limit, zeroOffset);
	}
	
	public void setDisplayAdjustParameters(double scale, double upContrast, double downContrast, double limit, double zeroOffset) {
		this.scaleFactor = scale;
		this.upContrast = upContrast;
		this.downContrast = downContrast;
		this.limit = limit;
		this.zeroOffset = zeroOffset;
		scaledLimit = Math.abs(limit * scale);
	}
	
	public void adjustExpressionsForDisplay(HeatmapData data) {
		adjustExpressionsForDisplay(data, scaleFactor, upContrast, downContrast, limit, zeroOffset);
	}

	public double getAdjustedExpression(double value) {
		value -= zeroOffset;
		value *= scaleFactor;
		value /= (value<0)? downContrast : upContrast;
		if (value < -scaledLimit)
			value = -scaledLimit;
		if (value > scaledLimit)
			value = scaledLimit;
		
		return value;
	}

	public void adjustExpressionsForDisplay(HeatmapData data, double upContrast, double downContrast, double limit, double zeroOffset) {
		adjustExpressionsForDisplay(data, 1, upContrast, downContrast, limit, zeroOffset);
	}

	public void adjustExpressionsForDisplay(HeatmapData expression,  double scaleFactor, double upContrast, double downContrast, double limit, double zeroOffset) {
//		int upLimit = (int)Math.round(limit / upContrast);
//		int downLimit = (int)Math.round(limit / downContrast);
		double scaledLimit = Math.abs(limit * scaleFactor);
//		double[][] data = expression.getExpression();
//	System.out.println("BBB    "+scaleFactor+"   "+ upContrast+"   "+ downContrast+"   "+limit+"   "+ zeroOffset+"   "+scaledLimit);
//		if (data==null) {
//			System.out.println("warning!!!--- in adjustExpressionsForDisplay-- data is null");
//			return;
//		}

		// xingjun - 20/07/2011 - may get empty expression value
		double[][] data = null;
		if (expression == null) {
			System.out.println("warning!!!--- in adjustExpressionsForDisplay-- data is null");
			return;
		}
		data = expression.getExpression();

		normalize(expression.getExpression(), expression.getMedian(), expression.getStdDev());
		
		for(int i=0; i<data.length; i++)
			for(int j=0; j<data[0].length; j++) {
				data[i][j] -= zeroOffset;
				data[i][j] *= scaleFactor;
				data[i][j] = data[i][j] / ((data[i][j] < 0)? downContrast : upContrast);
				if (data[i][j] < -scaledLimit)
					data[i][j] = -scaledLimit;
				if (data[i][j] > scaledLimit)
					data[i][j] = scaledLimit;
			}
	}

	public String[][] getCluster() {
		String[][] cluster = null;
		return cluster;
	}
	
	public void normalize(double[][] data) {
		normalize(data, null, null);
	}
	
	public void normalize(double[][] data, double[] rowMedian, double[] rowStdDev) {
		for(int i=0; i<data.length; i++) {
			double median = (rowMedian==null)? findMedian(data[i]) : rowMedian[i];
			double stdDev = (rowStdDev==null)? findStdDev(data[i]) : rowStdDev[i];
			for(int j=0; j<data[0].length; j++) {
				data[i][j] -= median;
				data[i][j] /= stdDev;
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
	
	public static double findMean(double[] data) {
		double sum =0;
		for (int i=0; i<data.length; i++)
			sum += data[i];
		return sum/data.length;
		
	}
	
	public static double findStdDev(double[] data) {
		double mean = findMean(data);
		double sum =0;
		//Find standard deviation
		for (int i=0; i<data.length; i++)
			sum += (data[i]-mean) * (data[i]-mean);
		return Math.sqrt(sum/data.length);
	}
	
}

/**
 * 
 */
package gmerg.assemblers;

import java.sql.Connection;

import gmerg.db.DBHelper;
import gmerg.db.GenelistDAO;
import gmerg.db.MySQLDAOFactory;
import gmerg.entities.BrowseTableTitle;
import analysis.DataSet;

/**
 * @author xingjun
 * 
 * used for processed gene list display page
 *
 */
public class GenelistExpressionAssembler {
	
	int genelistId;
    
	public GenelistExpressionAssembler () {
		genelistId = 0;
	}
	
	public GenelistExpressionAssembler (int genelistId) {
		this.genelistId = genelistId-2;
	}

	// *****  this should return a data set
	public DataSet retrieveExpressionData() {
		Connection conn = DBHelper.getDBConnection();
		GenelistDAO genelistDAO = MySQLDAOFactory.getGenelistDAO(conn);
		
		double[][] data = genelistDAO.getAllAnalysisData(genelistId);

		String[] expressionHeader = genelistDAO.getAnalysisTitles(genelistId);
		String[][] arrayAnnotation = new String[expressionHeader.length][1];
		for(int i=0; i<expressionHeader.length; i++)
			arrayAnnotation[i][0] = expressionHeader[i];

		String[] expressionIds =  genelistDAO.getAllIds(genelistId);
		String[][] geneAnnotations = new String[expressionIds.length][2];
		String[][] allOntologies = genelistDAO.getAllOntologies(genelistId);

		for(int i=0; i<expressionIds.length; i++) { 
			geneAnnotations[i][0] = expressionIds[i];
			String geneSymbols = allOntologies[i][2]; //should be retreived from database (now it is taken from ontology columns)
			if (geneSymbols==null || geneSymbols=="")
				geneSymbols = "-";
			else
				geneSymbols = geneSymbols.replace("///", ",");
			geneAnnotations[i][1] = geneSymbols;  
		}
		DataSet dataSet = new DataSet(data, null, data.length, data[0].length, geneAnnotations, arrayAnnotation);

		String[] arrayHeader = {"Sample ID"};
		String[] geneHeader =  {"ProbeID", "GeneSymbol"};
		boolean[] geneHeadersClickable = {false, true};
		dataSet.setGeneHeaders(geneHeader);
		dataSet.setArrayHeaders(arrayHeader);
		dataSet.setGeneHeadersClickable(geneHeadersClickable);

		return dataSet;
	}
	

	/**********************************************************************************************************
	 * Adjustments for display of expression data 
	 */
	
	double scaleFactor = 1;
	double upContrast = 1;
	double downContrast = 1;
	double zeroOffset = 0;
	double limit = 1;
	double scaledLimit = Math.abs(limit * scaleFactor);;
	
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
	
	public void adjustExpressionsForDisplay(double[][] data) {
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

	public void adjustExpressionsForDisplay(double[][] data, double upContrast, double downContrast, double limit, double zeroOffset) {
		adjustExpressionsForDisplay(data, 1, upContrast, downContrast, limit, zeroOffset);
	}
	
	public void adjustExpressionsForDisplay(double[][] data, double scaleFactor, double upContrast, double downContrast, double limit, double zeroOffset) {
//		int upLimit = (int)Math.round(limit / upContrast);
//		int downLimit = (int)Math.round(limit / downContrast);
		double scaledLimit = Math.abs(limit * scaleFactor);
//	System.out.println("BBB    "+scaleFactor+"   "+ upContrast+"   "+ downContrast+"   "+limit+"   "+ zeroOffset+"   "+scaledLimit);
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
	

	
}

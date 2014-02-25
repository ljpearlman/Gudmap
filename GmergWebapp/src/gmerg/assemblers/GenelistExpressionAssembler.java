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
    private boolean debug = false;
	int genelistId;
    
	public GenelistExpressionAssembler () {
	if (debug)
	    System.out.println("GenelistExpressionAssembler.constructor");

		genelistId = 0;
	}
	
	public GenelistExpressionAssembler (int genelistId) {
	if (debug)
	    System.out.println("GenelistExpressionAssembler.constructor");

		this.genelistId = genelistId-2;
	}

	// *****  this should return a data set
	public DataSet retrieveExpressionData() {
		Connection conn = DBHelper.getDBConnection();
		try{
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
		catch(Exception e){
			System.out.println("GenelistExpressionAssembler::retrieveExpressionData !!!");
			return null;
		}	
		finally{
			DBHelper.closeJDBCConnection(conn);
		}
	}
	

	/**********************************************************************************************************
	 * Adjustments for display of expression data 
	 */

	public void adjustExpressionsForDisplay(double[][] data, double scaleFactor, double upContrast, double downContrast, double limit, double zeroOffset) {
	    if (debug) {
		System.out.println("GenelistExpressionAssembler.adjustExpressionsForDisplay");
		System.out.println(" scale = "+scaleFactor+" upContrast = "+upContrast+" downContrast = "+downContrast+" limit = "+limit+" zeroOffset = "+zeroOffset);
	    }
		double scaledLimit = Math.abs(limit * scaleFactor);

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
}

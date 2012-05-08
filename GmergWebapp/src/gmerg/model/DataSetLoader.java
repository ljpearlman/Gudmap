package gmerg.model;

import java.net.URL;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import edu.stanford.genetics.treeview.*;
import edu.stanford.genetics.treeview.model.*;
import analysis.DataSet;

// This a clsss to populate a data set from a CDT and an optional GTR file
public class DataSetLoader {

	private int nGene;
	private int nExpr;
	private int nGenePrefix;
	private int nExprPrefix;
	
	private DataSet dataSet;
	private String cdtFile; 	
	private String gtrFile; 	

	public DataSetLoader() {
		this(null, null);
	}
	
	public DataSetLoader(String cdtFile, String gtrFile) {
		this.cdtFile = cdtFile;
		this.gtrFile = gtrFile;
//this.cdtFile = "12959_default-log2.cdt";
//this.gtrFile = null;
	}

	public String loadDataSet() {
		dataSet = new DataSet();
		
		ExternalContext eContext = FacesContext.getCurrentInstance().getExternalContext();
		URL url=null;
		try{
			url = new URL(cdtFile);
//			url = eContext.getResource("/genelist_data/cdt_files/" + cdtFile);
		}
		catch (Exception e) {
//			System.out.println("Exception");
			e.printStackTrace();
		};
		FlatFileParser2 parser = new FlatFileParser2();
		if (cdtFile!=null) 
			try {
//				parser.setResource(cdtPath + "/" + cdtFile);
				parser.setResource(url.toString());
				RectData tempTable = parser.loadIntoTable();
				parseCDT(tempTable);
			} catch (LoadException e) {
//				System.out.println("exception happed while parsing cdt"+ e.getMessage());
				return "exception happed while parsing cdt";
			} catch (Exception e) {
				// this should never happen!
				LogBuffer.println("TVModel.ResourceLoader.run() : while parsing cdt got error " + e.getMessage());
				e.printStackTrace();
				return "Error Parsing CDT";
			}
		if (gtrFile != null) 
			try {
//				parser.setResource(cdtPath + "/" + gtrFile);
				RectData tempTable = parser.loadIntoTable();
				parseGTR(tempTable);
			} catch (Exception e) {
				e.printStackTrace();
//				System.out.println("error parsing GTR: " + e.getMessage() + "\n ignoring gene tree.");
			}
			
		return null;
	}

/*	
	public static void main(String[] args) {
		DataSetLoader test = new DataSetLoader();
		test.dataSet = new DataSet();
		test.loadData();
		test.printDataSet();
	}   

	protected void printDataSet() {
		System.out.println("genes no="+ dataSet.getGeneNo());
		System.out.println("array no="+ dataSet.getArrayNo());
		String[] geneHeaders = dataSet.getGeneHeaders();
		for(int i=0; i<nGene; i++)
			System.out.print(geneHeaders[i]+"\t"); 
		System.out.println(); 
		String[] aHeaders = dataSet.getArrayHeaders();
		for(int i=0; i<nExpr; i++)
			System.out.print(aHeaders[i]+"\t");
		
		double[][] data=dataSet.getData();
		for(int i=0; i<nGene; i++) {
			for(int j=0; j<nExpr; j++)
				System.out.print(data[i][j]+"\t");
			System.out.println(); 
		}
	}
*/
	
	protected void  parseCDT(RectData tempVector) throws LoadException {
		// find eweightLine, ngene, nexpr
		findCdtDimensions(tempVector);
		loadArrayAnnotation(tempVector);
		loadGeneAnnotation(tempVector);
		loadCdtData(tempVector);
	}

	
	protected void findCdtDimensions(RectData tempVector)  {
		int gweightCol = -1;
		int rectCol = tempVector.getCol();
		for (int i = 0; i < rectCol; i++){
			String s = tempVector.getColumnName(i);
			if (s == null) {
//				System.out.println("Got null header, setting to empty string");
				s = "";
			}
			if (s.equalsIgnoreCase("GWEIGHT")) {
				gweightCol = i;
				break;
			}
		}
		
		if (gweightCol == -1) {
			if (tempVector.getColumnName(0).equalsIgnoreCase("GID")){
			nGenePrefix = 3;
			} else {
				nGenePrefix = 2;
			}
		} else {
			nGenePrefix = gweightCol + 1;
		}
		
		nExpr = rectCol - nGenePrefix;
		int eweightRow = -1;
		if (tempVector.getColumnName(0).equalsIgnoreCase("EWEIGHt")){
			eweightRow = 0;
		}else{
			int rectRow = tempVector.getRow(); 
			for (int i = 0; i < rectRow; i++) {
				String s = tempVector.getString(i, 0);
				if (s.equalsIgnoreCase("EWEIGHT")) {
					eweightRow = i+1;
					break;
				}
			}
		}
		if (eweightRow == -1) {
			if(tempVector.getString(0, 0).equalsIgnoreCase("AID")){
				nExprPrefix = 2;
			} else {
				nExprPrefix = 1;
			}
		} else {
			nExprPrefix = eweightRow + 1;
		}
		
		nGene = tempVector.size() - nExprPrefix;
		dataSet.setGeneNo(nGene);
		dataSet.setArrayNo(nExpr);
	}
	/**
	 * Loads array annotation from RectData into targetModel.
	 * 
	 * @param tempVector RectData contain annotation info
	 */
	protected void loadArrayAnnotation(RectData tempVector) {
		//System.out.println("DataSetLoader:loadArrayAnnotation");
		String [] arrayPrefix = new String[nExprPrefix];
		String [][] aHeaders = new String [nExpr][nExprPrefix];
		
		for (int i = 0; i < nExprPrefix; i++) {
			String [] tokens = (String []) tempVector.elementAt(i);
			arrayPrefix[i] = tokens[0];
			for (int j = 0; j < nExpr; j++)
				aHeaders[j][i] = tokens[j + nGenePrefix];
		}
		dataSet.setArrayHeaders(arrayPrefix);
		dataSet.setArrayAnnotations(aHeaders);
	}
	
	/**
	 * Loads gene annotation from RectData into targetModel.
	 * 
	 * @param tempVector RectData contain annotation info
	 */
	protected void loadGeneAnnotation(RectData tempVector) {
//		System.out.println("loading Gene Annotations");
		String [] genePrefix = new String[nGenePrefix];
		String [][] gHeaders = new String [nGene][nGenePrefix];
		
		String [] firstLine = (String []) tempVector.elementAt(0);
		for (int i = 0; i < nGenePrefix; i++) {
			genePrefix[i] = firstLine[i];
		}
		for (int i = 0; i < nGene; i++) {
//			String [] tokens = (String []) tempVector.elementAt(i + nArrayPrefix);
			for (int j = 0; j < nGenePrefix; j++) {
//				gHeaders[i][j] = tokens[j];
				gHeaders[i][j] = tempVector.getString(i+nExprPrefix-1,j);
			}
		}
		dataSet.setGeneHeaders(genePrefix);
		dataSet.setGeneAnnotations(gHeaders);
	}
	
	protected void loadCdtData(RectData tempVector) {
//		System.out.println("Parsing strings into doubles...");
		double [][] exprData = new double[nGene][nExpr];
		
		for (int gene = 0 ; gene < nGene; gene++) {
			String [] tokens = (String []) tempVector.elementAt(gene+nExprPrefix);
			int found = tokens.length - nGenePrefix;
			if (found != nExpr) {
				String err = "Wrong number of fields for gene " + tokens[0] + 
				" row " + (gene + nExprPrefix)+
				" Expected " + nExpr + ", found " + found;
//				System.out.println(err);
				err = "Line contains:";
				err += " " + tokens[0];
				for (int i = 1; i < tokens.length; i++) {
					err += ", " + tokens[i];
				}
//				System.out.println(err);
				if (found > nExpr) {
//					System.out.println("ignoring extra values");
					found = nExpr;
				} else if (found < nExpr) {
//					System.out.println("treating missing values as No Data..");
					for (int i = found; i < nExpr; i++) {
						exprData[gene][i] = DataModel.NODATA;
					}
				}
			}
			for (int expr = 0; expr < found; expr++) {
				try {
					exprData[gene][expr] = makeDouble(tokens[expr+nGenePrefix]);
				} catch (Exception e) {
//					System.out.println(e.getMessage());
//					System.out.println("Treating value as not found for gene " + gene + " experiment " + expr);
					exprData[gene][expr] = DataModel.NODATA;
				}
			}
		}
		dataSet.setData(exprData);

	}
	
	protected double makeDouble(String s) throws NumberFormatException {
		if (s == null) {
			return DataModel.NODATA;
		} else {
			try {
				Double tmp = new Double(s);
				double retval = tmp.doubleValue();
				// need to check, since RectData does this.
				if (Double.isNaN(retval)) {
					return DataModel.NODATA;
				}
				return retval; 
			} catch (Exception e) {
//				System.out.println("assigning nodata to badly formatted num'" + s +"'");
				return DataModel.NODATA;
			}
		}
	}

	
	//************************************************************************************************
	private void parseGTR(RectData tempVector) throws LoadException {
		String [] firstRow = (String []) tempVector.firstElement();
		if ( // decide if this is not an extended file..
			(firstRow.length == 4)// is the length classic?
		&& (firstRow[0].equalsIgnoreCase("NODEID") == false) // does it begin with a non-canonical upper left?
		) { // okay, need to assign headers...
////			targetModel.setGtrPrefix(new String [] {"NODEID", "LEFT", "RIGHT", "CORRELATION"});
			String [][] gtrHeaders = new String[tempVector.size()][];
			for (int i =0; i < gtrHeaders.length; i++) {
				gtrHeaders[i] = (String []) tempVector.elementAt(i);
			}
////			targetModel.setGtrHeaders(gtrHeaders);
		} else {// first row of tempVector is actual header names...
////			targetModel.setGtrPrefix(firstRow);

			String [][] gtrHeaders = new String[tempVector.size()-1][];
			for (int i =0; i < gtrHeaders.length; i++) {
				gtrHeaders[i] = (String []) tempVector.elementAt(i+1);
			}
////			targetModel.setGtrHeaders(gtrHeaders);
		}
	}

	public DataSet getDataSet() {
		return dataSet;
	}
	

	
	
}















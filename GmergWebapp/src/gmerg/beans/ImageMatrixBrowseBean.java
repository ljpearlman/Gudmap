package gmerg.beans;

import java.util.ArrayList;
import gmerg.utils.table.*;
import gmerg.utils.*;
import gmerg.assemblers.ImageMatrixBrowseAssembler;
import gmerg.entities.Globals;

public class ImageMatrixBrowseBean {
    private boolean debug = false;

	String gene;
	String symbolId;
	
	public ImageMatrixBrowseBean() {
	    if (debug)
		System.out.println("ImageMatrixBrowseBean.constructor");

	    gene = Visit.getRequestParam("gene");
	    symbolId = Visit.getRequestParam("symbolid");
		String viewName = "imageMatrix_" + gene;
		if (TableUtil.isTableViewInSession())
			return;
		
		ArrayList<String> imageIds = DbUtility.retrieveImageIdsByGeneSymbol(gene);	   
       	TableUtil.saveTableView(populateImageMatrixTableView(viewName, imageIds));		
	}	
	
	public static GenericTableView populateImageMatrixTableView(String viewName, ArrayList<String> imageIds) {
		CollectionBrowseHelper helper = Globals.getCollectionBrowseHelper(imageIds, 2); // corresponds to image collection
		ImageMatrixBrowseAssembler assembler = (ImageMatrixBrowseAssembler)helper.getCollectionBrowseAssembler();
		GenericTable table = assembler.createTable();
		GenericTableView tableView = getDefaultImageMatrixTableView(viewName, table);
		return  tableView;
	}
		
	public static GenericTableView getDefaultImageMatrixTableView(String viewName, GenericTable table) {
		GenericTableView tableView = new GenericTableView (viewName, 10, table);
		tableView.setCellsSelectable();
		tableView.setCollectionBottons(1);
		//tableView.addCollection(2, 0);
		tableView.addCollection(2, 4);//DEREK
		tableView.setDisplayTotals(false);
		tableView.setVerticalAlign(1);
		
		return tableView;
	}
	
	
	public String getGene() {
		return gene;
	}
	
}

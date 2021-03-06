package gmerg.beans;

import gmerg.assemblers.NGDSeriesAssembler;
import gmerg.entities.submission.nextgen.NGDSeries;
import gmerg.utils.table.*;
import gmerg.utils.FacesUtil;
import gmerg.utils.Utility;

import java.io.Serializable;
import java.util.HashMap;


/**
 * NGDSeriesBean - controls access to and display of data on 
 * a single Next Gen Data series
 */

public class NGDSeriesBean implements Serializable{
    /**
	 * 
	
	private static final long serialVersionUID = 1L; */

	private boolean debug = false;

    private NGDSeriesAssembler assembler;
    private NGDSeries series;   //object to contain all series data (except sample data)
    private String seriesId;   //the identifier for the specified series

    
    public NGDSeriesBean() {
	if (debug)
	    System.out.println("NGDSeriesBean.constructor");

        //get the series id from the request parameters
        seriesId = FacesUtil.getRequestParamValue("seriesId");
        
        //if series id not found in request, check for existence in session    
        if(seriesId == null || seriesId.equals("")) {
            seriesId = (String)FacesUtil.getSessionValue("seriesId");
        }

        assembler = null;
        //if id parameter contains a valid string, execute code to query db
        if(seriesId != null && !seriesId.equals("")) {
            //store series id in session
        	FacesUtil.setSessionValue("seriesId", seriesId);
    		HashMap<String, Object> queryParams = new HashMap<String, Object>();
    		queryParams.put("seriesId", seriesId);
            assembler = new NGDSeriesAssembler(queryParams);
            series = assembler.getSeriesMetaData();
            
            //if series data found build table of samples data
            if(series != null) {
                if (TableUtil.isTableViewInSession()) 
                    return;
                
        		GenericTableView tableView = populateSeriesSamplesTableView("seriesSamples");
        		TableUtil.saveTableView(tableView);
            }
        }
    }
    
    /**
     * returns a series object conatining data for a particulatr series
     * @return series - object containing data for a series
     */
    public NGDSeries getSeries() {
        return series;
    }
    
    /**
     * @param series - object containing data for a series
     */
    public void setSeries(NGDSeries series){
        this.series = series;
    }
    
    /**
     * populates a GenericTableView which will contain data on a particular table
     * in addition to the table data itself - in this case, sample data for a series.
     * @param viewName - the name of the table
     * @return
     */
    private GenericTableView populateSeriesSamplesTableView(String viewName) {
	    GenericTable table = assembler.createTable();
        GenericTableView tableView = new GenericTableView (viewName, 5, table);
        tableView.setRowsSelectable();
		tableView.addCollection(0, 0);
        tableView.setCollectionBottons(1);
        tableView.setDisplayTotals(false);
        //tableView.setColAlignment(2, 0);
        tableView.setColAlignment(5, 0);
        tableView.setColAlignment(6, 0);
        
        return  tableView;
    }
    
}

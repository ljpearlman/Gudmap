package gmerg.beans;

import gmerg.utils.table.GenericTable;
import gmerg.utils.table.GenericTableView;
import gmerg.utils.table.TableUtil;
import gmerg.utils.table.DataItem;

import java.util.Map;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;

public class MicroarrayDetailsBean {

	private String name;
	private String component;
	private String value;
	private int row;
	private String probeId;
	private String refSeq;
	private String chromosone;
	private String trStart;
	private String trEnd;
	private String humanOrthologSymbol;
	private String humanOrthologEntrezId;
	
	private GenericTableView tableView; 
	private GenericTable table; 
	private String geneSymbol;
	protected DataModel data = null;
	
	public MicroarrayDetailsBean () 
	{
        FacesContext context = FacesContext.getCurrentInstance();
        Map requestParams = context.getExternalContext().getRequestParameterMap();
        name = (String) requestParams.get("name");
        component = (String) requestParams.get("component");
        value = (String) requestParams.get("value");
        row = Integer.parseInt((String) requestParams.get("row"));
        
		// retrieve tableView object from session
		tableView = TableUtil.getTableViewFromSession(name);
		//System.out.println(tableView+"--->MicroarrayDetailsBean: tableView retrieved from session: "+name);
		
		
		table = tableView.getTable();
		DataItem[][] tableData = tableView.getData();
		
		//Bernie - 13/4/2011 - Mantis 540 - mod to col index of tableData due to display changes
		// Xingjun - 10/05/2011 - got java.lang.ArrayIndexOutOfBoundsException when the array size is less than given value; added check statements
//		if (tableData != null) 	System.out.println("MicroarrayDetailsBean:tableData size: " + tableData[row].length);
		if (tableData != null) 	{
			probeId = tableData[row].length>0 ? (String)tableData[row][0].getValue() : "";
			geneSymbol = tableData[row].length>3 ? (String)tableData[row][3].getValue() : "";
			refSeq = tableData[row].length>79 ? (String)tableData[row][79].getValue() : "";
			chromosone = tableData[row].length>81 ? (String)tableData[row][81].getValue() : "";
			trStart = tableData[row].length>83 ? (String)tableData[row][83].getValue() : "";		
			trEnd = tableData[row].length>84 ? (String)tableData[row][84].getValue() : "";  
			humanOrthologSymbol = tableData[row].length>92 ? (String)tableData[row][92].getValue() : "";		
			humanOrthologEntrezId = tableData[row].length>93 ? (String)tableData[row][93].getValue() : "";  
			humanOrthologEntrezId = tableData[row].length>86 ? (String)tableData[row][86].getValue() : "";  
		} else {
			probeId = "";
			geneSymbol = "";
			refSeq = "";
			chromosone = "";
			trStart = "";		
			trEnd = "";  
			humanOrthologSymbol = "";		
			humanOrthologEntrezId = "";  
			humanOrthologEntrezId = "";  
		}
		
	}

	public String getName() {
        return name;
    }

	public String getGeneSymbol() {
        return geneSymbol;
    }
	
	public String getComponent() {
        return component;
    }
	
	public String getValue() {
        return value;
    }
	
	public int getRow() {
        return row;
    }
	
	public String getProbeId() {
        return probeId;
    }
	
	public String getRefSeq() {
        return refSeq;
    }
	
	public String getChromosone() {
        return chromosone;
    }
	
	public String getTrStart() {
        return trStart;
    }
	
	public String getTrEnd() {
        return trEnd;
    }
	
	public String getHumanOrthologSymbol() {
        return humanOrthologSymbol;
    }
	
	public String getHumanOrthologEntrezId() {
        return humanOrthologEntrezId;
    }	
	
}

package gmerg.beans;

import gmerg.utils.table.GenericTable;
import gmerg.utils.table.GenericTableView;
import gmerg.utils.table.TableUtil;
import gmerg.utils.table.DataItem;
import gmerg.utils.table.HeaderItem;

import java.util.Map;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;

public class MicroarrayDetailsBean {
    private boolean debug = false;

	private String name;
	private String component;
                protected String gudmapId;
                protected String gudmapIdUrl;
	private String value;
	private int row;
	private String probeId;
	private String refSeq;
	private String entrezGeneId;
	private String mgiGeneId;
	private String humanOrthologSymbol;
	private String humanOrthologEntrezId;
	
	private GenericTableView tableView; 
	private GenericTable table; 
	private String geneSymbol;
	protected DataModel data = null;
	
	public MicroarrayDetailsBean () 
	{
	if (debug)
	    System.out.println("MicroarrayDetailsBean.constructor");

        FacesContext context = FacesContext.getCurrentInstance();
        Map requestParams = context.getExternalContext().getRequestParameterMap();
        name = (String) requestParams.get("name");
        component = (String) requestParams.get("component");
	int index = -1;
	if (null != component) 
	    index = component.toLowerCase().indexOf("gudmap:");
	if (-1 == index) {
	    gudmapId = null;
	    gudmapIdUrl = null;
	} else {
	    gudmapId = component.substring(index);
	    component = component.substring(0, index);
	    gudmapId = gudmapId.toUpperCase();
	    gudmapIdUrl = "mic_submission.html?id="+gudmapId.replace(":", "%3A");
	}

        value = (String) requestParams.get("value");
        row = Integer.parseInt((String) requestParams.get("row"));
        
		// retrieve tableView object from session
		tableView = TableUtil.getTableViewFromSession(name);
		//System.out.println(tableView+"--->MicroarrayDetailsBean: tableView retrieved from session: "+name);
		
		
		table = tableView.getTable();
		DataItem[][] tableData = tableView.getData();

		probeId = "";
		geneSymbol = "";
		refSeq = "";
		entrezGeneId = "";		
		mgiGeneId = "";  
		humanOrthologSymbol = "";		
		humanOrthologEntrezId = "";  

		if (tableData != null) {
		    probeId = (String)tableData[row][0].getValue();
		    geneSymbol = (String)tableData[row][3].getValue();

		    HeaderItem[] header = tableView.getTable().getHeader();
		    int iSize = header.length;
		    int i = 0;
		    for (i = 0; i < iSize; i++) {
			if (-1 != header[i].getTitle().indexOf("Probe Seq ID"))
			    refSeq = (String)tableData[row][i].getValue();
			if (-1 != header[i].getTitle().indexOf("Entrez Gene ID"))
			    entrezGeneId = (String)tableData[row][i].getValue();
			if (-1 != header[i].getTitle().indexOf("MGI Gene ID"))
			    mgiGeneId = (String)tableData[row][i].getValue();
			if (-1 != header[i].getTitle().indexOf("Human Ortholog Symbol"))
			    humanOrthologSymbol = (String)tableData[row][i].getValue();
			if (-1 != header[i].getTitle().indexOf("Human Ortholog Entez ID"))
			    humanOrthologEntrezId = (String)tableData[row][i].getValue();
		    }
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
	
	public String getEntrezGeneId() {
        return entrezGeneId;
    }
	
	public String getMgiGeneId() {
        return mgiGeneId;
    }
	
	public String getHumanOrthologSymbol() {
        return humanOrthologSymbol;
    }
	
	public String getHumanOrthologEntrezId() {
        return humanOrthologEntrezId;
    }	
	
    public String getGudmapId() {
	return gudmapId;
    }
	
    public String getGudmapIdUrl() {
	return gudmapIdUrl;
    }
}

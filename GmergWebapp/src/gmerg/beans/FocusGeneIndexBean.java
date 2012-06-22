package gmerg.beans;

import javax.faces.component.html.HtmlDataTable;

import gmerg.assemblers.FocusGeneIndexAssembler;
import gmerg.utils.FacesUtil;
import gmerg.utils.Visit;

public class FocusGeneIndexBean {
    private boolean debug = false;
	private String[] index;
//	private ArrayDataModel rows;
	private HtmlDataTable myDataTable = new HtmlDataTable();
	
	public FocusGeneIndexBean() {
	    if (debug)
		System.out.println("FocusGeneIndexBean.constructor");

		index =new String[]{"A","B","C","D","E",
							"F","G","H","I","J",
							"K","L","M","N","O",
							"P","Q","R","S","T",
							"U","V","W","X","Y",
							"Z","0-9"};
		getData();
	}
    
    public String queryGenes() {

    	Visit.setStatusParam("query", FacesUtil.getRequestParamValue("query"));
    	Visit.setStatusParam("input", FacesUtil.getRequestParamValue("input"));
    	return "AdvancedQuery";
    }
    
    private void getData() {
		FocusGeneIndexAssembler assembler = new FocusGeneIndexAssembler();
		String focusedOrgan = Visit.getRequestParam("focusedOrgan");
		String index = FacesUtil.getRequestParamValue("index");
		if(null == index || index.equals(""))
			index = "A";
		Object[][] data = assembler.getGeneIndex(index, focusedOrgan);
		myDataTable.setValue(data);
    }
    
	public String[] getIndex() {
		return index;
	}

	public void setIndex(String[] index) {
		this.index = index;
	}

	public HtmlDataTable getMyDataTable() {
		return myDataTable;
	}

	public void setMyDataTable(HtmlDataTable myDataTable) {
		this.myDataTable = myDataTable;
	}
	
/*
    public ArrayDataModel getRows() {
		if (rows!=null)
			return rows;
	
		FocusGeneIndexAssembler assembler = new FocusGeneIndexAssembler();
		String focusedOrgan = Visit.getRequestParam("focusedOrgan");
		String index = FacesUtil.getRequestParamValue("index");
		if(null == index || index.equals(""))
			index = "A";
		Object[][] data = assembler.getGeneIndex(index, focusedOrgan);
		rows = new ArrayDataModel();
		rows.setWrappedData(data);
		myDataTable.setValue(data);
		return rows;
	}
	
	public void setRows(ArrayDataModel rows) {
		this.rows = rows;
	}
*/
    
}

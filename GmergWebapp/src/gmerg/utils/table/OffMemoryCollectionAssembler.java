package gmerg.utils.table;

/**
 * @author Mehran Sharghi
 *
 * Abstract class for generic table data assembler
 * 
 */

import java.util.ArrayList;
import java.util.HashMap;

public abstract class OffMemoryCollectionAssembler extends OffMemoryTableAssembler {
	
	protected ArrayList<String> ids;
	protected CollectionBrowseHelper helper;
	
	// ********************************************************************************
	// Constructors
	// ********************************************************************************
	public OffMemoryCollectionAssembler(CollectionBrowseHelper helper) {
		super();
		this.helper = helper;
	}
	
	public OffMemoryCollectionAssembler(HashMap<String,Object> params, CollectionBrowseHelper helper) {
		super(params);
		this.helper = helper;
		ids = (ArrayList<String>)params.get(helper.getIdsParamName());
	}
	
	// ********************************************************************************
	// Abstract Methods
	// ********************************************************************************
//	abstract public ArrayList<String> getCollectionIdsFromParam();
	
	// ********************************************************************************
	// Public Methods
	// ********************************************************************************
	public int retrieveNumberOfRows() {
		int total = 0;
		if (ids != null)
			total = ids.size();
		return total;
	}

    public void setParams() {  // This should be override if there is any parameter locally maintained in the assembler & super.setParams()s hould be called from there
		super.setParams();
    }
    
	public String[] getAllIds(int columnNo) { 
		return null;
	}
	
	public ArrayList<String> getCollectionIds() {
		return ids;
	}

	public void setIds(ArrayList<String> ids) {
		this.ids = ids;
		anyParameterChenged = true;
	}

	public CollectionBrowseHelper getHelper() {
		return helper;
	}

	public void setHelper(CollectionBrowseHelper helper) {
		this.helper = helper;
		anyParameterChenged = true;
	}

}	
	



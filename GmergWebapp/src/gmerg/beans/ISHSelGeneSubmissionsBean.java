package gmerg.beans;

import gmerg.utils.table.TableUtil;

//Note: this bean is similar to ishSelSubmissionsBean. It serves a link from procesedGenelistDataBean.
// it can be removed and ishSelSubmissionsBean used instead but then ishSelSubmissionsBean should
//be turned to a multiple instance bean (queryType is a good candidate for distinguishing parameter).

public class ISHSelGeneSubmissionsBean extends ISHSelSubmissionsBean {
	// ********************************************************************************
	// Constructors & Initializers
	// ********************************************************************************
    public ISHSelGeneSubmissionsBean() {
	if (debug)
	    System.out.println("ISHSelGeneSubmissionsBean.constructor");

        if (TableUtil.isTableViewInSession())
			return;
        
		String viewName = "ishGeneSubmissions";
		TableUtil.saveTableView(populateQueryPageResultTableView(viewName));
	}
    
}
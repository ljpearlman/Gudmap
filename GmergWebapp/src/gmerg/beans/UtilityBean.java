package gmerg.beans;

import java.util.HashMap;

import gmerg.control.GudmapPageHistoryFilter;
import gmerg.utils.FacesUtil;
import gmerg.utils.Utility;
import gmerg.utils.table.TableUtil;

/**
 * @author Mehran Sharghi
 *
 */
public class UtilityBean {

	public String getRefererURI() {
		return GudmapPageHistoryFilter.getRefererPage();
	}
	
	public boolean getSetActiveTableViewName() {
		TableUtil.setActiveTableViewName();
		return false;
	}
	
	public String getTreeviewAppletCode() {
		HashMap<String, String> treeViewAppletParams = new HashMap<String, String>();
		treeViewAppletParams.put("genelistId", FacesUtil.getRequestParamValue("genelistId"));
		treeViewAppletParams.put("cdtFile", FacesUtil.getRequestParamValue("cdtFile"));
		treeViewAppletParams.put("gtrFile", FacesUtil.getRequestParamValue("gtrFile"));
		treeViewAppletParams.put("dataSource", "genelist");
		return Utility.javaTreeViewAppletCode(treeViewAppletParams);
	}

}

/**
 * 
 */
package gmerg.beans;

import gmerg.utils.Visit;

import java.util.HashMap;


/**
 * @author xingjun
 *
 */
public class GlobalSearchBean {
	
	private String input;
	private String globalSearchInput;

	public GlobalSearchBean() {
		input = null;
		globalSearchInput = "";
		
	}

	public String search() {
		input = globalSearchInput;
		if(globalSearchInput == null || globalSearchInput.equals("")) {
//			System.out.println("GlobalSearchBean:search return: NoResult");
			return "NoResult";
		}
//		System.out.println("GlobalSearchBean:search return: GlobalQuery");
		return "GlobalQuery";
	}
	
	public String getSearchParams() {
//		System.out.println("GlobalSearchBean:getSearchParams#########");
		HashMap<String, String> params = new HashMap<String, String>();
		String urlParams = null;
		
		params.put("input", globalSearchInput);
		urlParams = Visit.packParams(params);
		if (urlParams==null)
			return "";
//		System.out.println("GlobalSearchBean:getSearchParams:urlParams: " + urlParams);
		return "?"+urlParams;
	}
	
	public String getGlobalSearchInput() {
		return this.globalSearchInput;
	}

	public void setGlobalSearchInput(String globalSearchInput) {
		this.globalSearchInput = globalSearchInput;
	}

}

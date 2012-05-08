package gmerg.beans;


import javax.faces.model.ArrayDataModel;
import javax.faces.model.DataModel;
import gmerg.control.GudmapPageHistoryFilter;
import gmerg.utils.FacesUtil;
import gmerg.utils.Utility;

/**
 * 
 * @author Mehran Sharghi
 *
 *
*/

public class MessageBean {

	String message;
	String targetPage;
	boolean escape;
	
	// ********************************************************************************
	// Constructors & Initializers
	// ********************************************************************************
	public MessageBean() {
		message = FacesUtil.getFacesRequestParamValue("message");
		targetPage = FacesUtil.getFacesRequestParamValue("targetPage");
		escape = Utility.getValue(FacesUtil.getFacesRequestParamValue("escape"), "false").equalsIgnoreCase("true");
	}	

	// ********************************************************************************
	// Action Methods
	// ********************************************************************************
	public String navigateTarget() {
		if (targetPage==null || targetPage.equals("")) {
			targetPage = GudmapPageHistoryFilter.getRefererPage();
			return "navigateTargetPage"; 
		}
		int extensionIndex = targetPage.indexOf(".jsp");
		if (extensionIndex<0) 
			extensionIndex = targetPage.indexOf(".html");
		if (extensionIndex<0)
			return targetPage;
		
		targetPage = targetPage.replaceFirst(".html", ".jsp");
		return "navigateTargetPage";
	}
	
	// ********************************************************************************
	// Static Methodas
	// ********************************************************************************
	public static String showMessage(String message) {
		return showMessage(message, null, false);
	}
	
	public static String showMessage(String message, boolean escape) {
		return showMessage(message, null, escape);
	}
	
	public static String showMessage(String message, String target) {
		return showMessage(message, target, false);
	}
	
	public static String showMessage(String message, String target, boolean escape) {
		FacesUtil.setFacesRequestParamValue("message", message);
		FacesUtil.setFacesRequestParamValue("targetPage", target);
		FacesUtil.setFacesRequestParamValue("escape", String.valueOf(escape));
		return "messagePage";
	}

	// ********************************************************************************
	// Getters & Setters
	// ********************************************************************************

	public DataModel getMessages() {
		if (message==null || message.equals(""))
			return null;
		String[] messageLines = message.split("\n");
		return new ArrayDataModel(messageLines);
	}

	public String getTargetPage() {
		return targetPage;
	}

	public void setTargetPage(String targetPage) {
		this.targetPage = targetPage;
	}
	
	public boolean getEscape() {
		return escape; 
	}

}
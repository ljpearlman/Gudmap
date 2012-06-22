/**
 * Modified by Mehran in April
 * 
 * Modified by Xingjun on 05/05/2008:
 *   the user role value should be retrieved from the database and included in the user object
 *   (refer to entities/User.java)
 *   at moment user role value is linked to user type in REF_USER table
 * 
 */
package gmerg.beans;

import javax.servlet.http.HttpSession;
import javax.faces.context.FacesContext;

import gmerg.assemblers.SecurityAssembler;
import gmerg.assemblers.EditAssemblerUtil;
import gmerg.control.GudmapPageHistoryFilter;
import gmerg.entities.User;
import gmerg.utils.FacesUtil;

/**
 * @author xingjun
 *
 */
public class UserBean {
    private boolean debug = false;

    private String userName;
    private String password;
//    private String loginPanelStyle;
	private User newUser;
	private boolean displayLoginPanel;
	private boolean userLoggedIn;
	private String loginMessage;
	private String nickName;
	
	// commented by xingjun on 05/05/2008 - start
//	private String[] roles = {"", "guest", "user", "submitter", "editor", "administrator", "super" };
	// commented by xingjun on 05/05/2008 - end
	
//	private int userRole;
  
    public UserBean(){
	if (debug)
	    System.out.println("UserBean.constructor");

    	clearValues();
    }
    
    private void clearValues() {
		userName = "";
		password = "";
		loginMessage = " ";
		displayLoginPanel = false;
		userLoggedIn = false;
		newUser = null;
		// commented by xingjun on 05/05/2008 - start
//		userRole = 0;
		// commented by xingjun on 05/05/2008 - end
    }
    
    public String getUserName() {
        return userName;
    }
    
    public void setUserName(String value) {
        userName = value;
    }
    
    public void setPassword(String value) {
        password = value;
    }
    
    public String getPassword() {
        return password;
    }
    
    public String login() {
    	SecurityAssembler securityAssembler = new SecurityAssembler();
    	newUser = securityAssembler.getData(userName, password);
		if (newUser == null) {
    		userName = "";
    		password = "";
    		loginMessage = "Please enter a valid username and password. ";
    		displayLoginPanel = true;
    		return null;
    	}
   		userName = newUser.getUserName();
   		loginMessage = " ";
   		displayLoginPanel = false;
   		userLoggedIn = true;
   		// commented by xingjun on 05/05/2008 - start
//		userRole = 1; 
//   		String userType = newUser.getUserType();
//   		for(int i=0; i<roles.length; i++)
//   			if(roles[i].equalsIgnoreCase(userType)) {
//   				userRole = i;
//   				break;
//   			}
   		// commented by xingjun on 05/05/2008 - end

   		//change refereURI in session to transfer to the requested page if comming from restricted_access_redirect page
		String refererPage = (String)FacesUtil.getSessionValue("restrictedAccesssPage");
		if (refererPage != null)
			GudmapPageHistoryFilter.setRefererPage(refererPage);
		
/*		refererPage = GudmapPageHistoryFilter.getRefererPage();

 		System.out.println("refererPage="+refererPage);
 		
 		String statusParam = Visit.getStaticStatusParam();
 		if (statusParam != null && !statusParam.equals("")) 
 			refererPage += ((refererPage.indexOf("?") < 0)? "?" : "&") + statusParam;
 		
		System.out.println("refererPage===="+refererPage);
 
		GudmapPageHistoryFilter.setRefererPage(refererPage);
*/		
    	return "refererPage"; 
//   	return "loggedin";		//this for frame based login
    }
    
    public String resetLogin() {
    	clearValues();
    	return null;
    }
    
    public String cancelLogin() {
    	clearValues();
    	return "refererPage"; 
//    	return "cancelled";		//this for frame based login
    }
    
    public String logout(){
    	// added by xingjun - 06/08/2008 //////////////////////////////
    	// when log out, unlock all submissions locked by current user
    	// may need check if succeed
//    	int submissionsUnlocked = EditAssemblerUtil.unlockSubmissions(userName);
    	int submissionsUnlocked = EditAssemblerUtil.unlockSubmissions(newUser);
    	if (submissionsUnlocked == 0) {
//    		System.out.println("No submission locked");
    	} else {
//    		System.out.println(submissionsUnlocked + " submission unlocked");
    	}
    	//////////////////////////////////////////////////////////////
    	clearValues();
    	HttpSession session = (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);
    	if(session != null)
    		session.invalidate();
    	return "success";
    }
    
    public String refreshLoginPanel() {
    	return "refresh";
    }
    
    public void setUserLoggedIn(boolean value) { //dumy method for login_panel_connector to work properly  
    	return;
    }
    
	public String getLoginMessage() {
		return loginMessage;
	}

	public void setLoginMessage(String loginMessage) {
		this.loginMessage = loginMessage;
	}

	public boolean isDisplayLoginPanel() {
		return displayLoginPanel;
	}

	public void setDisplayLoginPanel(boolean displayLoginPanel) {
		this.displayLoginPanel = displayLoginPanel;
	}
	
	public boolean isUserLoggedIn() {
		return userLoggedIn;
	}
	
	//added by Ying
	public User getUser() {
		return newUser;
	}
	
	public int getUserId() {
		if (newUser == null)
			return -1;
		return newUser.getUserId();
	}
			
    public String getNickName() {
        return this.newUser.getNickName();
    }
    
    public void setNickName(String value) {
        this.nickName = value;
    }
    
	// commented by xingjun on 05/05/2008 - start
//	public String getUserRole() {
//		return roles[userRole];
//	}
//	
//	public int getUserPrivilege() {
//		return userRole;
//	}
	// commented by xingjun on 05/05/2008 - end
	
}

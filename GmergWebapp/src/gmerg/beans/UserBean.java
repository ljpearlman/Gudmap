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
import java.io.Serializable;
import gmerg.assemblers.SecurityAssembler;
import gmerg.assemblers.EditAssemblerUtil;
import gmerg.control.GudmapPageHistoryFilter;
import gmerg.entities.User;
import gmerg.utils.FacesUtil;

/**
 * @author xingjun
 *
 */
public class UserBean  implements Serializable {
    private boolean debug = false;

    private String userName;
    private String password;
	private User newUser;
	private boolean userLoggedIn;
	private String loginMessage;
	
    public UserBean(){
	if (debug)
	    System.out.println("UserBean.constructor");

    	clearValues();
    }
    
    private void clearValues() {
		userName = "";
		password = "";
		loginMessage = " ";
		userLoggedIn = false;
		newUser = null;
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
    		return null;
    	}
   		userName = newUser.getUserName();
   		loginMessage = " ";
   		userLoggedIn = true;

		String refererPage = (String)FacesUtil.getSessionValue("loginPage");
		if (refererPage != null)
			GudmapPageHistoryFilter.setRefererPage(refererPage);
		
    	return "refererPage"; 
    }
    
    public String resetLogin() {
    	clearValues();
    	return null;
    }
    
    public String cancelLogin() {
    	clearValues();
    	return "refererPage"; 
    }
    
    public String logout(){
    	// when log out, unlock all submissions locked by current user
    	// may need check if succeed
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
    
	public String getLoginMessage() {
		return loginMessage;
	}

	public void setLoginMessage(String input) {
		loginMessage = input;
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
}

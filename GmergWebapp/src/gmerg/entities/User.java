package gmerg.entities;

/*
 * Modified by xingjun 05/05/2008 to add more information (role, privilege, user id, and user PI) 
 * for the user object
 *
 * 
 */
public class User  {

    private String userName;
    private String userType;
    private String userRole;
    private int userPrivilege;
    private int userId;
    private int userPi;
    private String emailAddress;
    private String nickName;

    public User(){
        
    }
    
    public String getUserName() {
        return userName;
    }
    
    public void setUserName(String value) {
        userName = value;
    }
    
    public String getUserType() {
        return userType;
    }
    
    public void setUserType(String value){
        userType = value;
    }
    
    public String getUserRole() {
        return this.userRole;
    }
    
    public void setUserRole(String userRole){
        this.userRole = userRole;
    }
    
    public int getUserPrivilege() {
        return this.userPrivilege;
    }
    
    public void setUserPrivilege(int userPrivilege){
        this.userPrivilege = userPrivilege;
    }
    
    public int getUserId() {
    	return this.userId;
    }
    
    public void setUserId(int userId) {
    	this.userId = userId;
    }
    
    public int getUserPi() {
    	return userPi;
    }
    
    public void setUserPi(int userPi) {
    	this.userPi = userPi;
    }

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
    
    public String getNickName() {
        return this.nickName;
    }
    
    public void setNickName(String value) {
        this.nickName = value;
    }
    
}
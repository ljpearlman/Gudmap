package gmerg.assemblers;

import gmerg.db.DBHelper;
import gmerg.entities.User;
import gmerg.db.MySQLDAOFactory;
import gmerg.db.SecurityDAO;

import java.sql.Connection;

public class SecurityAssembler {
    private boolean debug = false;
    public SecurityAssembler() {
	if (debug)
	    System.out.println("SecurityAssembler.constructor");

    }
    public User getData(String username, String password){
        
        if(username == null || password == null){
            return null;
        }
        
        Connection conn = DBHelper.getDBConnection();
        SecurityDAO securityDAO = MySQLDAOFactory.getSecurityDAO(conn);
        
        User user = securityDAO.getUserWithUsernameAndPassword(username, password);
        
        return user;
        
    }
    
    
}
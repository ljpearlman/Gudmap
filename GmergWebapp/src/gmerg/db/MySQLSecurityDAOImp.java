package gmerg.db;

import gmerg.entities.User;
import gmerg.utils.Utility;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MySQLSecurityDAOImp implements SecurityDAO {
    
    private Connection conn;
    
    public MySQLSecurityDAOImp(Connection conn) {
        this.conn = conn;
    }
    
    public User getUserWithUsernameAndPassword(String username, String password){
    
        ParamQuery parQ = DBQuery.getParamQuery("LOGIN_DETAILS");
        ResultSet resSet = null;
        PreparedStatement prepStat = null;
		String str = null;
		User user = null;
        
        try {
            parQ.setPrepStat(conn);
            prepStat = parQ.getPrepStat();
            prepStat.setString(1, username);
            prepStat.setString(2, password);
            resSet = prepStat.executeQuery();
            
            if(resSet.first()){
                str = Utility.netTrim(resSet.getString(1));
                if(null != str && str.equals(username)) {
                	str = Utility.netTrim(resSet.getString(2));
 		    
					if (null != str && str.equals(password)){
			                    user = new User();
			                    user.setUserName(resSet.getString(1));
			                    user.setUserType(resSet.getString(3));
			                    user.setUserRole(resSet.getString(3));
			                    user.setUserPrivilege(resSet.getInt(4));
			                    user.setUserId(resSet.getInt(5));
			                    user.setUserPi(resSet.getInt(6));
			                    user.setNickName(resSet.getString(7));
			                    return user;
					}
                }
            }
            return null;
            
        }
        catch(SQLException e){
            e.printStackTrace();
            return null;
        }
        finally {
            DBHelper.closeResultSet(resSet);
            DBHelper.closePreparedStatement(prepStat);
        }
    }
    
}
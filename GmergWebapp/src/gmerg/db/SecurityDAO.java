package gmerg.db;

import gmerg.entities.User;

public interface SecurityDAO {
    
    public User getUserWithUsernameAndPassword(String username, String password);
    
    
}
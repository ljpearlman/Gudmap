package gmerg.assemblers;

import gmerg.db.DBHelper;
import gmerg.db.EditHistoryDAO;
import gmerg.db.MySQLDAOFactory;

import java.sql.Connection;
import java.util.ArrayList;

public class EditHistoryAssembler {
	public ArrayList getData(String sort) {	
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		EditHistoryDAO historyDAO = MySQLDAOFactory.getEditHistoryDAO(conn);
		
		
		ArrayList usersInDates = historyDAO.getUsersInDates(sort);

		/*System.out.println("RESULT:"+usersInDates.size());
		for(int i = 0; i < usersInDates.size(); i++) {
			String row0 = (String)((ArrayList)usersInDates.get(i)).get(0);
			System.out.println("RESULT DATE:"+row0);
			ArrayList row1 = (ArrayList)((ArrayList)usersInDates.get(i)).get(1);
			System.out.println("RESULT USER SIZE:"+row1.size());
			for(int j = 0; j < row1.size(); j++) {
				String row2 = (String)((ArrayList)row1.get(j)).get(0);
				System.out.println("RESULT USERNAME:"+row2);
				ArrayList row3 = (ArrayList)((ArrayList)row1.get(j)).get(1);
				System.out.println("RESULT SUBMISSION SIZE:"+row3.size());
			}
		}*/
		
		
		DBHelper.closeJDBCConnection(conn);
		historyDAO = null;
		
		
		// return the object
		return usersInDates;
	}
}

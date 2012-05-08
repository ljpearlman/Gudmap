/**
 * 
 */
package gmerg.assemblers;

import gmerg.db.DBHelper;
import gmerg.db.ISHDAO;
import gmerg.db.ISHEditDAO;
import gmerg.db.MySQLDAOFactory;
import gmerg.entities.submission.ExpressionPattern;
import gmerg.entities.submission.LockingInfo;
import gmerg.entities.User;

import java.sql.Connection;
import java.util.ArrayList;

import javax.faces.context.FacesContext;

/**
 * @author xingjun
 *
 */
public final class EditAssemblerUtil {
	public EditAssemblerUtil() {
		
	}

	/**
	 * @author xingjun - 16/07/2008
	 * insert given patten/locations for specific expression
	 * duplicated with the method in editExpressionAssembler
	 * <p>modified by xingjun - 21/10/2008 
	 * - modified code to assemble location string: when involed into the '+' operation, 
	 *   null string will be allocated a value of 'null' </p>
	 * 
	 */
	public static int insertMultiplePatternAndLocation(ISHEditDAO ishEditDAO, ExpressionPattern[] patterns, 
			String submissionId, String componentId, String userName) {
		// record by record, insert pattern and/or location value into the database & log it
		ArrayList<String> patternList = new ArrayList<String>();
		patternList.add(patterns[0].getPattern());
		ArrayList<String> insertedLocationList = new ArrayList<String>();
		
		// pattern
		int insertPatternNumber =
			ishEditDAO.insertPattern(submissionId, componentId, patterns[0].getPattern(), userName);
		// location
		if (insertPatternNumber != 0) {
			////////////////////////////
			// assemble location string
			// for 'adjacent to' location, locationAPart is 'adjacent to', 
			//                             locationNPart should be numerical data (e.g. 1234) 
			//                             location is the concatenate of la and ln (e.g. adjacent to 1234)
			// for non-'adjacent to' location, locationAPart is not null (e.g. rostral)
			//                                 locationNPart is null
			//                                 location is the concatenate of la and ln (e.g. rostral)
			String la = patterns[0].getLocationAPart();
			String locationAPart = (la==null || la.equals("")) ? "" : la;
			String ln = patterns[0].getLocationNPart();
			String locationNPart = (ln==null || ln.equals("")) ? "" : ln;
			String location = (locationAPart + " " + locationNPart).trim();
			///////////////////////////////
			if (location != null && !location.equals("")) {
				insertPatternNumber = ishEditDAO.insertLocation(submissionId, componentId, 
						patterns[0].getPattern(), location, userName);
				// keep record of insertion of location of specified pattern
				if (insertPatternNumber > 0) insertedLocationList.add(location);
			}
		}
		int len = patterns.length;
		for (int i=1;i<len;i++) {
			if (patternList.contains(patterns[i].getPattern())) { // one pattern presents in more than one locations
//				System.out.println("pattern duplicated#####");
				// insert every location string for the specified pattern if there's one
				////////////////////////////
				// assemble location string
				String la = patterns[i].getLocationAPart();
				String locationAPart = (la==null || la.equals("")) ? "" : la;
				String ln = patterns[i].getLocationNPart();
				String locationNPart = (ln==null || ln.equals("")) ? "" : ln;
				String location = (locationAPart + " " + locationNPart).trim();
				////////////////////////////
				if (location != null && !location.equals("") && !insertedLocationList.contains(location)) {
					/** due to the database redundancy problem this method need to be implemented in some strange way */
					insertPatternNumber = ishEditDAO.insertLocation(submissionId, componentId,
							patterns[i].getPattern(), location, userName);
					// keep record of insertion of location of specified pattern
					if (insertPatternNumber > 0) insertedLocationList.add(location);
				}
			} else { // different pattern
//				System.out.println("different pattern#####");
				// pattern
				String pattern = patterns[i].getPattern();
				insertPatternNumber =
					ishEditDAO.insertPattern(submissionId, componentId, pattern, userName);
				// location
				if (insertPatternNumber != 0) {
					//////////////////////////
					// assemble location string
					String la = patterns[i].getLocationAPart();
					String locationAPart = (la==null || la.equals("")) ? "" : la;
					String ln = patterns[i].getLocationNPart();
					String locationNPart = (ln==null || ln.equals("")) ? "" : ln;
					String location = (locationAPart + " " + locationNPart).trim();
					//////////////////////////
					if (location != null && !location.equals("")) {
						insertPatternNumber =
							ishEditDAO.insertLocation(submissionId, componentId, pattern, location, userName);
						if (insertPatternNumber > 0) { // keep record of insertion of location of specified pattern
							insertedLocationList = new ArrayList<String>();
							insertedLocationList.add(location);
						}
					}
				}
				patternList.add(patterns[i].getPattern());
			}
		}
		return insertPatternNumber;
	} // end of insertMultiplePatternAndLocation

	/**
	 * @author xingjun - 17/07/2008
	 * @param submissionId
	 * @param componentId
	 * @param ishEditDAO
	 * @param patterns
	 * @param userName
	 * @return
	 */
	public static int deleteAllPatternAndLocation(String submissionId, String componentId, 
			ISHEditDAO ishEditDAO, ExpressionPattern[] patterns, String userName) {

		if (patterns == null) {
			return 0;
		}
		
		// get pattern value list
		int len = patterns.length;
		ArrayList<String> patternList = new ArrayList<String>();
		patternList.add(patterns[0].getPattern());
		for (int i=1;i<len;i++) {
			if (!patternList.contains(patterns[i].getPattern())) {
				patternList.add(patterns[i].getPattern());
			}
		}
		String[] patternArray = patternList.toArray(new String[patternList.size()]);
		len = patternArray.length;
		
		// one by one, delete all pattern info
		int deleteRecordNumber = 0;
		for (int i=0;i<len;i++) {
			// delete location
			deleteRecordNumber = 
				ishEditDAO.deleteLocationByPattern(submissionId, componentId, patternArray[i], userName);
			// delete pattern
			deleteRecordNumber = 
				ishEditDAO.deletePatternByIds(submissionId, componentId, patternArray[i], userName);
		}
		return deleteRecordNumber;
	}
	
	/**
	 * @author xingjun - 28/07/2008
	 * @param submissionId
	 * @return
	 */
	public static LockingInfo getLockingInfo(String submissionId) {
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		ISHDAO ishDAO = MySQLDAOFactory.getISHDAO(conn);
		
		// retrieve data
		LockingInfo lockingInfo = ishDAO.getLockingInfo(submissionId);

		// release db resources
		DBHelper.closeJDBCConnection(conn);
		ishDAO = null;
		
		/** ---return the value object---  */
		return lockingInfo;
	}
	
	/**
	 * @author xingjun - 29/07/2008
	 * There's no corresponding record in lock table, need add one
	 * @param submissionId
	 * @return
	 */
	public static int lockSubmission(String submissionId, String userName) {
//		System.out.println("lockSubmission: " + 
//				FacesContext.getCurrentInstance().getViewRoot().getViewId());
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		ISHEditDAO ishEditDAO = MySQLDAOFactory.getISHEditDAO(conn);
		
		// lock
		int submissionLocked =
			ishEditDAO.insertSubmissionLockingInfo(submissionId, userName);

		// release db resources
		DBHelper.closeJDBCConnection(conn);
		ishEditDAO = null;
		
		// return
		return submissionLocked;
	}
	
	/**
	 * @author xingjun
	 * <p>reload version of lockSubmission</p>
	 * @param submissionId
	 * @param user
	 * @return
	 */
	public static int lockSubmission(String submissionId, User user) {
//		System.out.println("lockSubmission: " + 
//				FacesContext.getCurrentInstance().getViewRoot().getViewId());
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		ISHEditDAO ishEditDAO = MySQLDAOFactory.getISHEditDAO(conn);
		
		// lock
		int submissionLocked =
			ishEditDAO.insertSubmissionLockingInfo(submissionId, user.getUserId());

		// release db resources
		DBHelper.closeJDBCConnection(conn);
		ishEditDAO = null;
		
		// return
		return submissionLocked;
	}
	
	/**
	 * @author xingjun -30/07/2008
	 * there's already a record in the locking table, update user name and lock time
	 * 
	 * @param submissionId
	 * @param userName
	 * @return
	 */
	public static int relockSubmission(String submissionId, String userName) {
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		ISHEditDAO ishEditDAO = MySQLDAOFactory.getISHEditDAO(conn);
		
		// lock
		int submissionRelocked =
			ishEditDAO.updateSubmissionLockingInfo(submissionId, userName);

		// release db resources
		DBHelper.closeJDBCConnection(conn);
		ishEditDAO = null;
		
		// return
		return submissionRelocked;
	}
	
	/**
	 * @author xingjun
	 * <p>reload version of relockSubmission</p>
	 * @param submissionId
	 * @param user
	 * @return
	 */
	public static int relockSubmission(String submissionId, User user) {
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		ISHEditDAO ishEditDAO = MySQLDAOFactory.getISHEditDAO(conn);
		
		// lock
		int submissionRelocked =
			ishEditDAO.updateSubmissionLockingInfo(submissionId, user.getUserId());

		// release db resources
		DBHelper.closeJDBCConnection(conn);
		ishEditDAO = null;
		
		// return
		return submissionRelocked;
	}
	
	/**
	 * @author xingjun - 30/07/2008
	 * <p>clear the corresponding locking info in system</p>
	 * @param submissionId
	 * @return
	 */
	public static int unlockSubmission(String submissionId) {
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		ISHEditDAO ishEditDAO = MySQLDAOFactory.getISHEditDAO(conn);
		
		// lock
		int submissionUnlocked =
			ishEditDAO.deleteSubmissionLockingInfo(submissionId);
		
		// release db resources
		DBHelper.closeJDBCConnection(conn);
		ishEditDAO = null;
		
		// return
		return submissionUnlocked;
	}
	
	/**
	 * @author xingjun - 06/08/2008
	 * <p>unlock all submissions locked by given user</p>
	 * @param userName
	 * @return
	 */
	public static int unlockSubmissions(String userName) {
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		ISHEditDAO ishEditDAO = MySQLDAOFactory.getISHEditDAO(conn);
		
		// lock
		int submissionsUnlocked =
			ishEditDAO.deleteUserLockingInfo(userName);
//		System.out.println("submissionsUnlocked: " + submissionsUnlocked);
		
		// release db resources
		DBHelper.closeJDBCConnection(conn);
		ishEditDAO = null;
		
		// return
		return submissionsUnlocked;
	}
	
	/**
	 * @author xingjun - 15/08/2008
	 * <p>reload version of unlockSubmissions</p>
	 * @param user
	 * @return
	 */
	public static int unlockSubmissions(User user) {
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		ISHEditDAO ishEditDAO = MySQLDAOFactory.getISHEditDAO(conn);
		
		// lock
		int submissionsUnlocked =
			ishEditDAO.deleteUserLockingInfo(user.getUserId());
//		System.out.println("submissionsUnlocked: " + submissionsUnlocked);
		
		// release db resources
		DBHelper.closeJDBCConnection(conn);
		ishEditDAO = null;
		
		// return
		return submissionsUnlocked;
	}
	
}

/**
 * 
 */
package gmerg.assemblers;

import java.sql.Connection;

import gmerg.db.DBHelper;
import gmerg.db.ISHDAO;
import gmerg.db.MySQLDAOFactory;
import gmerg.entities.submission.ImageDetail;

/**
 * @author xingjun
 *
 */
public class ImageDetailAssembler {
	
	public ImageDetail getData(String submissionAccessionId, String serialNum) {
		
		if (submissionAccessionId == null || submissionAccessionId.equals("")) {
			return null;
		}
		if (serialNum == null || serialNum.equals("")) {
			System.out.println("Warning! ImageDetailAssembler:getData()-- serialNum is null, used default value 1.");
			serialNum = "1";
		} else if (!DBHelper.isValidInteger(serialNum)) {
			System.out.println("Warning! ImageDetailAssembler:getData()-- serialNum is invalid, used default value 1.");
			serialNum = "1";
		}
		
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		ISHDAO ishDAO = MySQLDAOFactory.getISHDAO(conn);
		
		/** ---get data--- */
		ImageDetail imageDetail =
			ishDAO.findImageDetailBySubmissionId(submissionAccessionId, Integer.parseInt(serialNum)-1);

		// release the db resources
		DBHelper.closeJDBCConnection(conn);
		ishDAO = null;
		
		/** ---return the object--- */
		return imageDetail;
	}

}

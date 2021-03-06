/**
 * 
 */
package gmerg.assemblers;

import java.sql.Connection;
import java.util.ArrayList;

import gmerg.db.DBHelper;
import gmerg.db.ISHDAO;
import gmerg.db.MySQLDAOFactory;
import gmerg.entities.submission.ImageDetail;

/**
 * @author xingjun
 *
 */
public class ImageDetailAssembler {
    private boolean debug = false;
    public ImageDetailAssembler() {
	if (debug)
	    System.out.println("ImageDetailAssembler.constructor");

    }

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
		try{
			ISHDAO ishDAO = MySQLDAOFactory.getISHDAO(conn);
			ImageDetail imageDetail = ishDAO.findImageDetailBySubmissionId(submissionAccessionId, Integer.parseInt(serialNum)-1);
			return imageDetail;
		}
		catch(Exception e){
			System.out.println("ImageDetailAssembler::getData !!!");
			return null;
		}
		finally{
			DBHelper.closeJDBCConnection(conn);
		}
	}

}

package gmerg.assemblers;

import gmerg.entities.submission.Antibody;

import gmerg.db.DBHelper;
import gmerg.db.ISHDAO;
import gmerg.db.MySQLDAOFactory;

import java.sql.*;
import java.util.ArrayList;

public class AntibodyAssembler {

	public Antibody getData(String antibodyId) {

		if (antibodyId == null) {
			return null;
		}

		/** ---get data from dao---  */
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		ISHDAO ishDAO = MySQLDAOFactory.getISHDAO(conn);

		Antibody antibody = ishDAO.findAntibodyByAntibodyId(antibodyId);


		//get related ihc submissions
		if (antibody.getGeneSymbol() != null){
			ArrayList relatedSubmissionISH = ishDAO.findRelatedSubmissionBySymbolISH(antibody.getGeneSymbol());		
			if (null != relatedSubmissionISH) {
				antibody.setIshSubmissions(relatedSubmissionISH);
			}
		}

		// release the db resources
		DBHelper.closeJDBCConnection(conn);
		ishDAO = null;

		return antibody;
	}
	
}

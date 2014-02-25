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

		// create a dao
		Connection conn = DBHelper.getDBConnection();
		try{
			ISHDAO ishDAO = MySQLDAOFactory.getISHDAO(conn);
			
			Antibody antibody = ishDAO.findAntibodyByAntibodyId(antibodyId);
			if (antibody.getGeneSymbol() != null){
				ArrayList relatedSubmissionISH = ishDAO.findRelatedSubmissionBySymbolISH(antibody.getGeneSymbol());		
				if (null != relatedSubmissionISH) {
					antibody.setIshSubmissions(relatedSubmissionISH);
				}
			}
			return antibody;
		}
		catch(Exception e){
			System.out.println("AntibodyAssembler::getData failed !!!");
			return null;
		}
		finally{
			DBHelper.closeJDBCConnection(conn);
		}
	}
	
}

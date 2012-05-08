/**
 * 
 */
package gmerg.assemblers;

import java.sql.Connection;
import java.util.ArrayList;

import gmerg.db.DBHelper;
import gmerg.db.ISHDAO;
import gmerg.db.MySQLDAOFactory;
//import gmerg.entities.submission.ish.ISHBrowseMolecularMarkerCandidate;

/**
 * @author xingjun
 *
 */
public class MolecularMarkerAssembler {
	
	public ArrayList getMarkerCandidates() {
		
		/** ---get data from dao--- */
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		ISHDAO ishDAO = MySQLDAOFactory.getISHDAO(conn);
		
		// get the value
		ArrayList markerCandidatesRaw = ishDAO.getMolecularMarkerCandidates();
		
		// format the raw data of marker candidates into appropriate data structure
		ArrayList markerCandidates = formatMarkerCandidateData(markerCandidatesRaw);
		
		// release db resources
		DBHelper.closeJDBCConnection(conn);
		ishDAO = null;

		/** return the value  */
		return markerCandidates;
	}
	
	/**
	 * finish the task of assembling molecular marker data
	 * need further modification
	 * @param markerCandidatesRaw
	 * @return
	 */
	private ArrayList formatMarkerCandidateData(ArrayList markerCandidatesRaw) {
		
		if (markerCandidatesRaw == null || markerCandidatesRaw.isEmpty()) {
			return null;
		}

		// go through the candidate raw data
		int len = markerCandidatesRaw.size();
		ArrayList<Object> result = new ArrayList<Object>();
		
//		System.out.println("candidate number: " + Integer.toString(len));
//		System.out.println("component: " + ((String[])markerCandidatesRaw.get(2))[1]);
//		System.out.println("symbol: " + ((String[])markerCandidatesRaw.get(2))[2]);
//		System.out.println("stage: " + ((String[])markerCandidatesRaw.get(2))[3]);
		
		for (int i=0;i<len;i++) {
			String componentName = ((String[])markerCandidatesRaw.get(i))[1];
			String symbol = ((String[])markerCandidatesRaw.get(i))[2];
			String stage = ((String[])markerCandidatesRaw.get(i))[3];
			String ISHAccId = ((String[])markerCandidatesRaw.get(i))[4];
			ArrayList<String> ISHAccessionIDs = new ArrayList<String>();
			ISHAccessionIDs.add(ISHAccId);
			ArrayList<String> IHCAccessionIDs = new ArrayList<String>();
			IHCAccessionIDs.add("");
			ArrayList<String> arrayAccessionIDs = new ArrayList<String>();
			arrayAccessionIDs.add("");
			
			ArrayList<Object> candidateItem = new ArrayList<Object>();
			candidateItem.add(componentName);
			candidateItem.add(symbol);
			candidateItem.add(stage);
			candidateItem.add(ISHAccessionIDs);
			candidateItem.add(IHCAccessionIDs);
			candidateItem.add(arrayAccessionIDs);
			
			result.add(candidateItem);
		}
		
		return result;
//		return null;
	}

}

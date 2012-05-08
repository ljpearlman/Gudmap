/**
 * 
 */
package gmerg.assemblers;

import gmerg.entities.submission.Probe;
import gmerg.db.DBHelper;
import gmerg.db.MySQLDAOFactory;
import gmerg.db.ISHDevDAO;

import java.sql.Connection;

/**
 * @author xingjun
 *
 */
public class EditProbeAssembler {
	
	public Probe getData(String submissionId) {
		
		if (submissionId == null || submissionId.equals("")) {
			return null;
		}
		
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		ISHDevDAO ishDevDAO = MySQLDAOFactory.getISHDevDAO(conn);
		
		
		// get data
		Probe probe = ishDevDAO.findProbeBySubmissionId(submissionId);
//		String probeNotes = ishDevDAO.findProbeNoteBySubmissionId(submissionId);
//		String maprobeNotes = ishDevDAO.findMaProbeNoteBySubmissionId(submissionId);
//		String fullSequences = ishDevDAO.findProbeFullSequenceBySubmissionId(submissionId);
		
		// assemble probe data
//		if (probe != null) {
//			if (probeNotes != null && !probeNotes.equals("")) {
//				probe.setNotes(probeNotes);
//			}
//			if (maprobeNotes != null && !maprobeNotes.equals("")) {
//				probe.setMaprobeNoteString(maprobeNotes);
//			}
//			if (fullSequences != null && !fullSequences.equals("")) {
//				probe.setFullSequenceString(fullSequences);
//			}
//		}
		
		// release db resources
		DBHelper.closeJDBCConnection(conn);
		ishDevDAO = null;
		
		// return the object
		return probe;
	}

}

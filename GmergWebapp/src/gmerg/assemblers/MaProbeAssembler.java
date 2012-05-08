package gmerg.assemblers;

import gmerg.entities.submission.Probe;

import gmerg.db.DBHelper;
import gmerg.db.ISHDAO;
import gmerg.db.MySQLDAOFactory;

import java.sql.*;
import java.util.ArrayList;

public class MaProbeAssembler {

	public Probe getData(String probeId, String maprobeId) {

		if (probeId == null || probeId.equals("")) {
			return null;
		}

		/** ---get data from dao---  */
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		ISHDAO ishDAO = MySQLDAOFactory.getISHDAO(conn);

		Probe probe = ishDAO.findMaProbeByProbeId(probeId, maprobeId);

		if (probe != null && probe.getSeqStatus() != null
				&& !probe.getSeqStatus().equalsIgnoreCase("Unsequenced.")) {
			if (probe.getSeq5Loc().equals("n/a")
					|| probe.getSeq3Loc().equals("n/a")) {
				probe.setSeqInfo("Accession number for part sequence: ");
			} else {
				probe.setSeqInfo("Probe sequence spans from "
						+ probe.getSeq5Loc() + " to " + probe.getSeq3Loc()
						+ " of");
			}
		}
		
		// added by Bernie 29/06/2011 Mantis 558 Task6
		//get related ish submissions
		if (probe.getGeneSymbol() != null){
			ArrayList relatedSubmissionISH = ishDAO.findRelatedSubmissionBySymbolISH(probe.getGeneSymbol());		
			if (null != relatedSubmissionISH) {
				probe.setIshSubmissions(relatedSubmissionISH);
			}
		}

		// release the db resources
		DBHelper.closeJDBCConnection(conn);
		ishDAO = null;

		return probe;
	}

}

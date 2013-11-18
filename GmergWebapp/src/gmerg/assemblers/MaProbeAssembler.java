package gmerg.assemblers;

import gmerg.entities.submission.Probe;

import gmerg.db.DBHelper;
import gmerg.db.ISHDAO;
import gmerg.db.MySQLDAOFactory;

import java.sql.*;
import java.util.ArrayList;

public class MaProbeAssembler {
    private boolean debug = false;
    public MaProbeAssembler() {
	if (debug)
	    System.out.println("MaProbeAssembler.constructor");
    }

	public Probe getData(String probeId, String maprobeId) {

		if ((probeId == null || probeId.equals("")) && (maprobeId == null || maprobeId.equals(""))) {
		    System.out.println("MaProbeAssembler.getData  !!! possible error: null probeId and maprobeId");
			return null;
		}

		/** ---get data from dao---  */
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		ISHDAO ishDAO = MySQLDAOFactory.getISHDAO(conn);

		Probe probe = ishDAO.findMaProbeByProbeId(probeId, maprobeId);

		if (probe == null)
		    System.out.println("!!!!possible error: probeId="+probeId+" maprobeId="+maprobeId+" does not have a probe in DB");
		else {
		    if (probe.getSeqStatus() != null && !probe.getSeqStatus().equalsIgnoreCase("Unsequenced.")) {
				if (probe.getSeq5Loc().equals("n/a") || probe.getSeq3Loc().equals("n/a")) {
				    probe.setSeqInfo("Accession number for part sequence: ");
				} 
				else {
				    probe.setSeqInfo("Probe sequence spans from "
						     + probe.getSeq5Loc() + " to " + probe.getSeq3Loc()
						     + " of");
				}
		    }
		    
		    if (probe.getGeneSymbol() != null){
				ArrayList relatedSubmissionISH = ishDAO.findRelatedSubmissionBySymbolISH(probe.getGeneSymbol());		
				if (null != relatedSubmissionISH) {
				    probe.setIshSubmissions(relatedSubmissionISH);
				}
		    }
		}

		// release the db resources
		DBHelper.closeJDBCConnection(conn);
		ishDAO = null;

		return probe;
	}

}

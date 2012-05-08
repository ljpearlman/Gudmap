/**
 * 
 */
package gmerg.beans;

import java.util.Map;

import javax.faces.context.FacesContext;

import gmerg.assemblers.EditProbeAssembler;
import gmerg.entities.submission.Probe;

/**
 * @author xingjun
 *
 */
public class EditProbeBean {
	
	private String submissionId;
	private Probe probe;
	private boolean renderProbeSeqInfo;
	private EditProbeAssembler editProbeAssembler;
	
	public EditProbeBean() {
        
		// obtain context
		FacesContext context = FacesContext.getCurrentInstance();
        Map requestParams =
            context.getExternalContext().getRequestParameterMap();
            
        // obtain parameter
        this.submissionId = (String) requestParams.get("accessionId");
        
		// obtain probe detail
        editProbeAssembler = new EditProbeAssembler();
		probe = editProbeAssembler.getData(this.submissionId);
		
		// decide if display probe sequence
		if (probe != null) {
			if (probe.getSeqStatus() != null && !probe.getSeqStatus().equals("unsequenced")) {
				this.renderProbeSeqInfo = true;
				if (probe.getSeq5Loc().equals("n/a") || probe.getSeq3Loc().equals("n/a")) {
					probe.setSeqInfo("Accession number for part sequence: ");
				} else {
					probe.setSeqInfo("Probe sequence spans from " +
                                                         probe.getSeq5Loc() + " to " +
                                                         probe.getSeq3Loc() + " of ");
				}
			} else {
				this.renderProbeSeqInfo = false;
			}
		}
	}
	
	public String getSubmissionId() {
		return this.submissionId;
	}
	
	public void setSubmissionId(String submissionId) {
		this.submissionId = submissionId;
	}
	
	public Probe getProbe() {
		return this.probe;
	}
	
	public void setProbe(Probe probe) {
		this.probe = probe;
	}
	
	public boolean isRenderProbeSeqInfo() {
		return this.renderProbeSeqInfo;
	}

}

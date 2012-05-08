package gmerg.control;

import gmerg.utils.FacesUtil;

import javax.faces.event.PhaseListener;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;


/**
 * Phase listener is to mark invoke apllication phase span. It currently being used by variable resolver 
 * to handle access to multiple instance beans specially when there are multiple instances in the same page. 
 *  
 * @author Mehran Sharghi
 * 
 */

public class GudmapInvokeApplicationPhaseTracker implements PhaseListener {
	
	static final String phaseId = "application_phase";
	static final String phaseKey = "current_phase";
	
	public PhaseId getPhaseId(){
		return PhaseId.INVOKE_APPLICATION;
	}

	public void beforePhase(PhaseEvent event) {
		FacesUtil.setFacesRequestParamValue(phaseKey, phaseId);
	}
	
	public void afterPhase(PhaseEvent event) {
		if (phaseId.equals(FacesUtil.getFacesRequestParamValue(phaseKey))) 
			FacesUtil.removeFacesRequestParam(phaseKey);
	}
	
	public static boolean isApplicationPhase() {
		return phaseId.equals(FacesUtil.getFacesRequestParamValue(phaseKey));
	}
}

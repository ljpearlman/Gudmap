package gmerg.beans;

import gmerg.entities.submission.Probe;
import gmerg.utils.FacesUtil;
import gmerg.assemblers.MaProbeAssembler;

public class MaProbeBean {
	
	private Probe maProbe;
	private MaProbeAssembler assembler;
	
	public MaProbeBean() {
		String probeId = FacesUtil.getRequestParamValue("probe");
		String maprobeId = FacesUtil.getRequestParamValue("maprobe");
		
		if (maprobeId != null )
			maprobeId = maprobeId.substring(8);
		
		assembler = new MaProbeAssembler();
		maProbe = assembler.getData(probeId, maprobeId);
	}
	
	public Probe getMaProbe() {
		return maProbe;
	}
	
	public void setMaProbe(Probe probe){
		maProbe = probe;
	}
	

}

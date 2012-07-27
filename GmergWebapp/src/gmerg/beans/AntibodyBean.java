package gmerg.beans;

import gmerg.entities.submission.Antibody;
import gmerg.utils.FacesUtil;
import gmerg.assemblers.AntibodyAssembler;


public class AntibodyBean {
	
	private Antibody antibody;
	private AntibodyAssembler assembler;
	
	public AntibodyBean() {
		String antibodyId = FacesUtil.getRequestParamValue("antibody");
		
		assembler = new AntibodyAssembler();
		antibody = assembler.getData(antibodyId);
	}
	
	public Antibody getAntibody() {
		return antibody;
	}
	
	public void setAntibody(Antibody antibody){
		this.antibody = antibody;
	}

}

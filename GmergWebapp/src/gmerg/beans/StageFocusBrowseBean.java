package gmerg.beans;

import gmerg.assemblers.FocusBrowseAssembler;
import gmerg.utils.Visit;

public class StageFocusBrowseBean {
    private boolean debug = false;

	private FocusBrowseAssembler assembler = null;
//	private String[] stage = new String[]{"17","18","19","20","21","22","23","24","25","26","27","28"};
	private String[] stage;
	private String[][] stageList;
	private String gene;
	private String geneId;
	private String species;
	
	/**
	 * modified by xingjun - 29/01/2009
	 * added gene symbol param to get relevant stage summary info
	 *
	 */
	public StageFocusBrowseBean() {
	if (debug)
	    System.out.println("StageFocusBrowseBean.constructor");

		String organ = Visit.getRequestParam("focusedOrgan");
		gene = Visit.getRequestParam("gene");
		geneId = Visit.getRequestParam("geneId");
		species = Visit.getRequestParam("species"); 
		if (species == null)
			species = "Mus Musculus";
		assembler = new FocusBrowseAssembler();
//		stageList = assembler.getStageList(stage, organ);
		stage = assembler.getStages(species);
		stageList = assembler.getStageList(stage, organ, geneId);
	}

	/**
	 * modified by xingjun - 29/01/2009
	 * added extra column to display dpc value
	 * @return
	 */
	public String[][] getSubmissions() {
		String[][] tableData = new String[stage.length][6];
		for(int i=0; i<stage.length; i++) {
			tableData[i][0] = new String(stage[i]);
			if(null != stageList[i][0]) {
				tableData[i][1] = new String(stageList[i][0]);
			} else {
				tableData[i][1] = new String("0");
			}
			if(null != stageList[i][1]) {
				tableData[i][2] = new String(stageList[i][1]);
			} else {
				tableData[i][2] = new String("0");
			}
			if(null != stageList[i][2]) {
				tableData[i][3] = new String(stageList[i][2]);
			} else {
				tableData[i][3] = new String("0");
			}
			tableData[i][4] = new String(stage[i].toLowerCase());
			
			if(null != stageList[i][3]) {
				tableData[i][5] = new String(stageList[i][3]);
			} else {
				tableData[i][5] = new String("0");
			}
		}
		return tableData;
	}
	
	/**
	 * @author xingjun - 30/01/2009
	 * @return
	 */
	public String getGene() {
		if (gene == null || gene.equals("")) {
			return "";
		} else {
			return gene;
		}
	}
	
	public String getSpecies() {
		return species;
	}
	
}

package gmerg.entities.submission;

public class Specimen {

    private String stageFormat = "";
    private String otherStageValue = "";
    private String strain = "";
    private String sex = "";
    private String genotype = "";
    private String assayType = "";
    private String fixationMethod = "";
    private String embedding="";
    private String phase = "";
    private String[] notes= new String[0];
    private String species = "";

    public String getStageFormat() {
        return stageFormat;
    }

    public void setStageFormat(String value) {
	if (null == value)
	    stageFormat = "";
	else
	    stageFormat = value.trim();
    }

    public String getPhase() {
        return phase;
    }

    public void setPhase(String value) {
	if (null == value)
	    phase = "";
	else
	    phase = value.trim();
    }

    public String getOtherStage() {
	String ret = stageFormat;
	if (ret.equals("")) 
	    ret = otherStageValue;
	else {
	    if (!otherStageValue.equals("")) {
		if (ret.toLowerCase().equals("p"))
		    ret = ret + " "+ otherStageValue;
		else
		    ret = otherStageValue+" "+ret;
	    }
	}

	if (!ret.equals("") && !phase.equals("")) {
		String str = phase.toLowerCase();
		if (!str.equals("unspecified") && !str.equals("null"))
		    ret = ret+" "+phase;
	}
	
	return ret;
    }
	
    public String getOtherStageValue() {
        return otherStageValue;
    }

    public void setOtherStageValue(String value) {
	if (null == value)
	    otherStageValue = "";
	else
	    otherStageValue = value.trim();
    }

    public String getStrain() {
        return strain;
    }

    public void setStrain(String value) {
	if (null == value)
	    strain = "";
	else
	    strain = value.trim();
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String value) {
	if (null == value)
	    sex = "";
	else
	    sex = value.trim();
    }

    public String getGenotype() {
        return genotype;
    }

    public void setGenotype(String value) {
	if (null == value) 
	    genotype= "";
	else
	    genotype = value.trim();
    }

    public String getAssayType() {
        return assayType;
    }

    public void setAssayType(String value) {
	if (null == value)
	    assayType = "";
	else
	    assayType = value.trim();
    }

    public String getFixMethod() {
        return fixationMethod;
    }

    public void setFixMethod(String value) {
	if (null == value)
	    fixationMethod = "";
	else
	    fixationMethod = value.trim();
    }

    public String getEmbedding() {
        return embedding;
    }

    public void setEmbedding(String value) {
	if (null == value)
	    embedding = "";
	else
	    embedding = value.trim();
    }

    public String[] getNotes() {
        return notes;
    }

    public void setNotes(String[] value) {
        notes = value;
	if (null == notes)
	    notes = new String[0];
    }
    
    public String getSpecies() {
        return species;
    }

    public void setSpecies(String value) {
	if (null == value) 
		species= "";
	else
		species = value.trim();
    }
   
}

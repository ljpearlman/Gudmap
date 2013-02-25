package gmerg.entities.submission;

public class Specimen {

    private String stageFormat;
    private String otherStageValue;
    private String strain;
    private String sex;
    private String genotype;
    private String assayType;
    private String fixationMethod;
    private String embedding;
    private String[] notes;

    public String getStageFormat() {
        return stageFormat;
    }

    public void setStageFormat(String value) {
        stageFormat = value;
    }

    public String getOtherStageValue() {
        return otherStageValue;
    }

    public void setOtherStageValue(String value) {
        otherStageValue = value;
    }

    public String getStrain() {
        return strain;
    }

    public void setStrain(String value) {
        strain = value;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String value) {
        sex = value;
    }

    public String getGenotype() {
        return genotype;
    }

    public void setGenotype(String value) {
        genotype = value;

	if (null == value) {
	    genotype= "";
	    return;
	} 

        if (genotype.trim().equalsIgnoreCase("true")) {
            genotype = "Wild Type";
        } else if (genotype.trim().equalsIgnoreCase("false")) {
           genotype = "Non-wild Type";
        } else {
           genotype = "";
        }

    }

    public String getAssayType() {
        return assayType;
    }

    public void setAssayType(String value) {
        assayType = value;
    }

    public String getFixMethod() {
        return fixationMethod;
    }

    public void setFixMethod(String value) {
        fixationMethod = value;
    }

    public String getEmbedding() {
        return embedding;
    }

    public void setEmbedding(String value) {
        embedding = value;
    }

    public String[] getNotes() {
        return notes;
    }

    public void setNotes(String[] value) {
        notes = value;
	if (null == notes)
	    notes = new String[0];
    }
}

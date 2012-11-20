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
    protected boolean hasNotes = false;

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


    //modify this method so that processing occurs in bean
    public String getGenotype() {

        if (genotype.trim().equalsIgnoreCase("true")) {
            genotype = "Wild Type";
        } else if (genotype.trim().equalsIgnoreCase("false")) {
           genotype = "Non-wild Type";
        } else {
           genotype = "";
        }
        return genotype;
    }

    public void setGenotype(String value) {
        genotype = value;
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
	if (null != notes && 0 < notes.length)
	    hasNotes =  true;
	else
	    hasNotes =  false;
    }

    public boolean getHasNotes() {
	return hasNotes;
    }
}

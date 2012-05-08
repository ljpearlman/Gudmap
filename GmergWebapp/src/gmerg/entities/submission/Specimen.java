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
    private String notes;

    public String getStageFormat() {
        return this.stageFormat;
    }

    public void setStageFormat(String value) {
        this.stageFormat = value;
    }

    public String getOtherStageValue() {
        return this.otherStageValue;
    }

    public void setOtherStageValue(String value) {
        this.otherStageValue = value;
    }

    public String getStrain() {
        return this.strain;
    }

    public void setStrain(String value) {
        this.strain = value;
    }

    public String getSex() {
        return this.sex;
    }

    public void setSex(String value) {
        this.sex = value;
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
        return this.genotype;
    }

    public void setGenotype(String value) {
        this.genotype = value;
    }

    public String getAssayType() {
        return this.assayType;
    }

    public void setAssayType(String value) {
        this.assayType = value;
    }

    public String getFixMethod() {
        return this.fixationMethod;
    }

    public void setFixMethod(String value) {
        this.fixationMethod = value;
    }

    public String getEmbedding() {
        return this.embedding;
    }

    public void setEmbedding(String value) {
        this.embedding = value;
    }

    public String getNotes() {
        return this.notes;
    }

    public void setNotes(String value) {
        this.notes = value;
    }
}

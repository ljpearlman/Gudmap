package gmerg.entities.submission;


public class BrowseSubmission {

    private String id;
    private String stage;
    private String age;
    private String lab;
    private String date;
    private boolean selected;

    public BrowseSubmission() {
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setStage(String stg) {
        stage = stg;
    }

    public String getStage() {
        return stage;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getAge() {
        return age;
    }

    public void setLab(String lab) {
        this.lab = lab;
    }

    public String getLab() {
        return lab;
    }

    public void setDate(String dt) {
        date = dt;
    }

    public String getDate() {
        return date;
    }

    public void setSelected(boolean sel) {
        selected = sel;
    }

    public boolean getSelected() {
        return selected;
    }

}

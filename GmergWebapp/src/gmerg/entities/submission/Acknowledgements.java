package gmerg.entities.submission;

public class Acknowledgements {

    private String project;
    private String names;
    private String address;
    private String url;
    private String reason;

    public void setProject(String project) {
        this.project = project;
    }

    public String getProject() {
        return this.project;
    }

    public void setNames(String names) {
        this.names = names;
    }

    public String getNames() {
        return this.names;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return this.address;
    }

    public void setURL(String url) {
        this.url = url;
    }

    public String getURL() {
        return this.url;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getReason() {
        return this.reason;
    }

}

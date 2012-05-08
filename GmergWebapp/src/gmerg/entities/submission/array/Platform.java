package gmerg.entities.submission.array;

public class Platform {

    private String geoID;
    private String title;
    private String name;
    private String distribution;
    private String technology;
    private String organism;
    private String manufacturer;
    private String manufactureProtocol;
    private String catNo;

    public void setGeoID(String id) {
        geoID = id;
    }

    public String getGeoID() {
        return geoID;
    }

    public void setTitle(String ttl) {
        title = ttl;
    }

    public String getTitle() {
        return title;
    }

    public void setName(String nme) {
        name = nme;
    }

    public String getName() {
        return name;
    }

    public void setDistribution(String dist) {
        distribution = dist;
    }

    public String getDistribution() {
        return distribution;
    }

    public void setTechnology(String tech) {
        technology = tech;
    }

    public String getTechnology() {
        return technology;
    }

    public void setOrganism(String org) {
        organism = org;
    }

    public String getOrganism() {
        return organism;
    }

    public void setManufacturer(String man) {
        manufacturer = man;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufactureProtocol(String mp) {
        manufactureProtocol = mp;
    }

    public String getManufactureProtocol() {
        return manufactureProtocol;
    }

    public void setCatNo(String cn) {
        catNo = cn;
    }

    public String getCatNo() {
        return catNo;
    }

}

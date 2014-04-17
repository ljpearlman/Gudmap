package gmerg.entities.submission.array;

public class NGDSample {

    private String geoID;
    private String title;
    private String source;
    private String organism;
    private String strain;
    private String mutation;
    private String sex;
    private String devAge;
    private String theilerStage;
    private String dissectionMethod;
    private String molecule;
    private String a_260_280;
    private String extractionProtocol;
    private String amplificationKit;
    private String amplificationProtocol;
    private String amplificationRounds;
    private String volTargHybrid;
    private String label;
    private String washScanHybProtocol;
    private String gcosTgtVal;
    private String dataAnalProtocol;
    private String reference;
    private String description;
    private String experimentalDesign;// xingjun - 14/05/2010
    private String labelProtocol;// xingjun - 14/05/2010
    private String scanProtocol;// xingjun - 14/05/2010
    private String poolSize;// bernie - 07/09/2010
    private String pooledSample;// bernie - 07/09/2010
    private String developmentalLandmarks;// bernie - 22/11/2010 (Mantis 505)
    
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

    public void setSource(String src) {
        source = src;
    }

    public String getSource() {
        return source;
    }

    public void setOrganism(String org) {
        organism = org;
    }

    public String getOrganism() {
        return organism;
    }

    public void setStrain(String strn) {
        strain = strn;
    }

    public String getStrain() {
        return strain;
    }

    public void setMutation(String mut) {
        mutation = mut;
    }

    public String getMutation() {
        return mutation;
    }

    public void setSex(String s) {
        sex = s;
    }

    public String getSex() {
        return sex;
    }

    public void setDevAge(String age) {
        devAge = age;
    }

    public String getDevAge() {
        return devAge;
    }

    public void setTheilerStage(String stage) {
    	this.theilerStage = stage;
    }
    
    public String getTheilerStage() {
    	return theilerStage;
    }
    
    public void setdissectionMethod(String dm) {
        dissectionMethod = dm;
    }

    public String getDissectionMethod() {
        return dissectionMethod;
    }

    public void setMolecule(String mol) {
        molecule = mol;
    }

    public String getMolecule() {
        return molecule;
    }

    public void setA_260_280(String value) {
        a_260_280 = value;
    }

    public String getA_260_280() {
        return a_260_280;
    }

    public void setExtractionProtocol(String ep) {
        extractionProtocol = ep;
    }

    public String getExtractionProtocol() {
        return extractionProtocol;
    }

    public void setAmplificationKit(String ak) {
        amplificationKit = ak;
    }

    public String getAmplificationKit() {
        return amplificationKit;
    }

    public void setAmplificationProtocol(String ap) {
        amplificationProtocol = ap;
    }

    public String getAmplificationProtocol() {
        return amplificationProtocol;
    }

    public void setAmplificationRounds(String rounds) {
        amplificationRounds = rounds;
    }

    public String getAmplificationRounds() {
        return amplificationRounds;
    }

    public void setVolTargHybrid(String vth) {
        volTargHybrid = vth;
    }

    public String getVolTargHybrid() {
        return volTargHybrid;
    }

    public void setLabel(String lbl) {
        label = lbl;
    }

    public String getLabel() {
        return label;
    }

    public void setWashScanHybProtocol(String wshp) {
        washScanHybProtocol = wshp;
    }

    public String getWashScanHybProtocol() {
        return washScanHybProtocol;
    }

    public void setGcosTgtVal(String gtv) {
        gcosTgtVal = gtv;
    }

    public String getGcosTgtVal() {
        return gcosTgtVal;
    }

    public void setDataAnalProtocol(String dap) {
        dataAnalProtocol = dap;
    }

    public String getDataAnalProtocol() {
        return dataAnalProtocol;
    }

    public void setReference(String ref) {
        reference = ref;
    }

    public String getReference() {
        return reference;
    }
    
    public void setDescription(String description) {
    	this.description = description;
    }
    
    public String getDescription() {
    	return description;
    }

    public String getExperimentalDesign() {
    	return experimentalDesign;
    }
    
    public void setExperimentalDesign(String experimentalDesign) {
    	this.experimentalDesign = experimentalDesign;
    }
    
    public String getLabelProtocol() {
    	return labelProtocol;
    }
    
    public void setLabelProtocol(String labelProtocol) {
    	this.labelProtocol = labelProtocol;
    }
    
    public String getScanProtocol() {
    	return scanProtocol;
    }
    
    public void setScanProtocol(String scanProtocol) {
    	this.scanProtocol = scanProtocol;
    }

    public String getPoolSize() {
    	return poolSize;
    }
    
    public void setPoolSize(String poolSize) {
    	this.poolSize = poolSize;
    }

    public String getPooledSample() {
    	return pooledSample;
    }
    
    public void setPooledSample(String pooledSample) {
    	this.pooledSample = pooledSample;
    }

    public String getDevelopmentalLandmarks(){
    	return developmentalLandmarks;    	
    }

    public void setDevelopmentalLandmarks(String developmentalLandmarks){
    	this.developmentalLandmarks = developmentalLandmarks;    	
    }

}

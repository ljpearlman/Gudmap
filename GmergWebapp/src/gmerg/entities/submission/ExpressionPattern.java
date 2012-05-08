package gmerg.entities.submission;

public class ExpressionPattern {

    private String pattern;
    private String locations;
    private String patternImage;
    private boolean selected;
    private int expressionId;
    private int patternId;
    private int locationId;
    
    private String locationAPart; // alphabetical part of location when its value in 'adjacent to xxxxx' format
    private String locationNPart; // numerical part of location when its value in 'adjacent to xxxxx' format
    
    public ExpressionPattern() {
    }
    
    public void setPattern(String value){
        pattern = value;
    }
    
    public String getPattern(){
        return pattern;
    }
    
    public void setLocations(String values){
        locations = values;
    }
    
    public String getLocations(){
        return locations;
    }
    
    public void setPatternImage(String value){
        patternImage = value;
    }
    
    public String getPatternImage() {
        return patternImage;
    }
    
    public boolean isSelected() {
    	return this.selected;
    }
    
    public void setSelected(boolean selected) {
    	this.selected = selected;
    }
    
    public int getExpressionId() {
    	return this.expressionId;
    }
    
    public void setExpressionId(int expressionId) {
    	this.expressionId = expressionId;
    }
    
    public int getPatternId() {
    	return this.patternId;
    }
    
    public void setPatternId(int patternId) {
    	this.patternId = patternId;
    }
    
    public int getLocationId() {
    	return this.locationId;
    }
    
    public void setLocationId(int locationId) {
    	this.locationId = locationId;
    }
    
    // added by xingjun - 13/06/2008 - need to split the location value when it's in 'adjacent to xxx' format
    public String getLocationAPart() {
//    	this.locationAPart = this.locations.substring(0, 11);
    	return this.locationAPart;
    }
    
    public void setLocationAPart(String locationAPart) {
    	this.locationAPart = locationAPart;
    }
    
    public String getLocationNPart() {
//    	this.locationNPart = this.locations.substring(11).trim();
    	return this.locationNPart;
    }
    
    public void setLocationNPart(String locationNPart) {
    	this.locationNPart = locationNPart;
    }
}

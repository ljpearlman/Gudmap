/**
 * 
 */
package gmerg.db;

/**
 * @author xingjun
 *
 */
public class ParamMap {

	private String paramId;
	private String paramName;
	
	public ParamMap(String paramId, String paramName) {
		this.paramId = paramId;
		this.paramName = paramName;
	}
	
	public String getParamId() {
		return this.paramId;
	}
	
	public void setParamId (String paramId) {
		this.paramId = paramId;
	}
	
	public String getParamName() {
		return this.paramName;
	}
	
	public void setParamName(String paramName) {
		this.paramName = paramName;
	}
}

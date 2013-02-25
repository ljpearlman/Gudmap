package gmerg.entities;

import java.util.HashMap;

/**
 * @author Bernie
 *
 */
public class NodeData {

	private String nodeText;
	private String nodeID;
	HashMap<String, Object> params = new HashMap<String, Object>();

    public NodeData(String nodeText, String nodeID) {
        this.nodeText = nodeText;
        this.nodeID = nodeID;        
    }

    public void setNodeText(String nodeText){
    	this.nodeText = nodeText;
    }
    
    public String getNodeText(){
    	return nodeText;
    }

    public void setNodeID(String nodeID){
    	this.nodeID = nodeID;
    }
    
    public String getNodeID(){
    	return nodeID;
    }

    public void setParams(String key, Object value){
    	this.params.put(key, value);
    }
    
    public HashMap<String, Object> getParams(){
    	return params;
    }

}

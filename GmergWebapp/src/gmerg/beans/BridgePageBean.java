/**
 * 
 */
package gmerg.beans;

import gmerg.assemblers.BridgePageAssembler;
//import gmerg.assemblers.FocusForAllAssembler;
import gmerg.entities.NodeData;
import gmerg.utils.Utility;
import gmerg.utils.Visit;
import gmerg.utils.DbUtility;

import java.util.ArrayList;
import java.util.HashMap;

import org.richfaces.component.TreeNode;
import org.richfaces.component.TreeNodeImpl;
import org.richfaces.component.events.NodeSelectedEvent;
import org.richfaces.component.html.HtmlTree;

import javax.faces.context.FacesContext;
import javax.faces.application.NavigationHandler;

/**
 * @author Bernie & xingjun
 *
 */
public class BridgePageBean{
    private boolean debug = false;
	private String geneTitle = "GENES";
	private String probeTitle = "PROBES";
	private String insituTitle = "IN SITU DATA";
	private String microarrayTitle = "MICROARRAY DATA";
	private String gudmapTitle = "GUDMAP TRANSGENIC MOUSE STRAINS";
	private String tutorialTitle = "TUTORIALS";
	private String anatomyTitle = "ANATOMICAL COMPONENTS";
	
    private TreeNode rootGeneNode; 
    private TreeNode rootProbeNode; 
    private TreeNode rootInsituNode;
    private TreeNode rootMicroarrayNode;
    private TreeNode rootGudmapNode;
    private TreeNode rootTutorialNode;
    private TreeNode rootAnatomyNode;
	
	private int numberOfGenesQueryByGene;
	private int[] numberOfEntriesQueryByGene;
	private int numberOfGenesQueryByGeneFunction;
	private int numberOfEntriesQueryByGeneFunction;
	private int numberOfEntriesQueryByAnatomy;
	private int numberOfEntriesQueryByAccession;
	
	private BridgePageAssembler geneBridgePageAssembler;
	private BridgePageAssembler anatomyBridgePageAssembler;
	private BridgePageAssembler accessionBridgePageAssembler;
	private BridgePageAssembler functionBridgePageAssembler;
	
	private String bridgePageSearchInput ;

	
	
	private String nodeTitle;
	private String organ;
	private String inputString;
	private String input;
	private String[] inputs;
	private String query;
	private String sub;
	private String exp;
	private String wildcards;
	private String geneStage;
	private String geneAnnotation;
	private String[] queryCriteria;

	HashMap<String, NodeData> nodes = new HashMap<String, NodeData>();
	// constructor
	public BridgePageBean() {
	    if (debug)
		System.out.println("BridgePageBean.constructor");

		input = Visit.getRequestParam("input");
		System.out.println("BridgePageBean:input: " + input);
		
		//TODO : parse the input to extract genes, anatomy into separate arrays etc

		// genes by Gene
		this.numberOfGenesQueryByGene = this.retrieveNumberOfGenesQueryByGene();
		
				
		// others
		String[] wildcards = new String[]{ Visit.getRequestParam("geneWildcard"), Visit.getRequestParam("geneStage"), Visit.getRequestParam("geneAnnotation") };
		String[] inputs = null;
		String [] tmp = null;
		if (!(input==null || input.equals(""))) {
			if(Utility.getProject().equalsIgnoreCase("EuReGene")) {
				tmp = input.split("\t|\n|\r|\f|;");
				if(tmp !=null){
				    int numValidInputs = 0;
				    for(int i=0;i<tmp.length;i++){
					    if(tmp[i] != null && !tmp[i].trim().equals("")){
						    numValidInputs ++;
					    }
				    }
				    inputs = new String [numValidInputs];
				    int tmpIndex = 0;
				    int inputIndex = 0;
				    
					while(inputIndex < numValidInputs){
						if(tmp[tmpIndex] != null && !tmp[tmpIndex].trim().equals("")){
							inputs[inputIndex] = tmp[tmpIndex];
							inputIndex++;
						}
						tmpIndex++;
					}
				}
			} else {
				inputs = input.split("\t|\n|\r|\f|;");
//				System.out.println("number of symbols: " + inputs.length);
			}
//			geneSymbols = input.split("[[\\s|,]&&[^ ]]");
			/////// added by xingjun - 06/04/2009 - if the input by user comes like 'string*', it means wildcard search (start with) //////////////
//			System.out.println("check input string###########: ");
			int numberOfInputItems = inputs.length;
			for (int i=0;i<numberOfInputItems;i++) {
//				System.out.println("input string: " + inputs[i]);
				int stringLen = inputs[i].length();
				if (stringLen > 0) {
					// got last character of the input string
					String lastChar = inputs[i].substring(stringLen-1, stringLen);
//					System.out.println("last char: " + lastChar);
					if (lastChar.equals("*")) {
						inputs[i] = inputs[i].substring(0, stringLen-1);
						wildcards[0] = "starts with";
//						System.out.println("input string removed wc: " + inputs[i]);
					} else {
//						System.out.println("not wild~~~~~~~~ ");
						wildcards[0] = "equals";
					}
				}

			}
			///////////////////////////////////////////////////////////////////
		}
		
//		System.out.println("BridgePageBean======================");
//		System.out.println("inputs: " + inputs.length + ":" + inputs[0]);
//		System.out.println("wildcards: " + wildcards.length + ":" + wildcards[0]);

		// entries by Gene
//		System.out.println("query: " + "Gene");
		HashMap<String, Object> queryParams = new HashMap<String, Object>();
		queryParams.put("inputs", inputs);
		queryParams.put("query", "Gene");
		queryParams.put("wildcards", wildcards);
		geneBridgePageAssembler = new BridgePageAssembler(queryParams);
		
		// entries by Anatomy
//		System.out.println("query: " + "Anatomy");
//		queryParams = new HashMap<String, Object>();
//		queryParams.put("inputs", inputs);
//		queryParams.put("query", "Anatomy");
//		queryParams.put("wildcards", wildcards);
//		anatomyBridgePageAssembler = new BridgePageAssembler(queryParams);
//		this.numberOfEntriesQueryByAnatomy = anatomyBridgePageAssembler.retrieveNumberOfRows();

		// entries by Accession ID
//		System.out.println("query: " + "Accession ID");
//		queryParams = new HashMap<String, Object>();
//		queryParams.put("inputs", inputs);
//		queryParams.put("query", "Accession ID");
//		queryParams.put("wildcards", wildcards);
//		accessionBridgePageAssembler = new BridgePageAssembler(queryParams);
//		this.numberOfEntriesQueryByAccession = accessionBridgePageAssembler.retrieveNumberOfRows();
		
		// entries by Gene Function
//		System.out.println("query: " + "Gene Function");
//		queryParams = new HashMap<String, Object>();
//		queryParams.put("inputs", inputs);
//		queryParams.put("query", "Gene Function");
//		queryParams.put("wildcards", wildcards);
//		functionBridgePageAssembler = new BridgePageAssembler(queryParams);
//		this.numberOfEntriesQueryByGeneFunction = functionBridgePageAssembler.retrieveNumberOfRows();
		
		// genes by Gene Function
//		queryParams = new HashMap<String, Object>();
//		queryParams.put("inputs", inputs);
//		queryParams.put("query", "Gene Function");
//		queryParams.put("widecard", wildcards);
//		bridgePageAssembler = new BridgePageAssembler(queryParams);
//		this.numberOfEntriesQueryByGene = bridgePageAssembler.retrieveNumberOfRows();
//		this.numberOfGenesQueryByGeneFunction = 0;
		
//		bridgePageAssembler = new BridgePageAssembler();
//		this.numberOfGenesQueryByGene = bridgePageAssembler.getNumberOfGenesQueryByGene(input);
//		this.numberOfEntriesQueryByAccession = bridgePageAssembler.getNumberOfEntriesQueryByGene(input);
//		this.numberOfEntriesQueryByAnatomy = bridgePageAssembler.getNumberOfEntriesQueryByAnatomy(input);
//		this.numberOfEntriesQueryByAccession = bridgePageAssembler.getNumberOfEntriesQueryByAccession(input);
//		this.numberOfGenesQueryByGeneFunction = bridgePageAssembler.getNumberOfGenesQueryByGeneFunction(input);
//		this.numberOfEntriesQueryByGeneFunction = bridgePageAssembler.getNumberOfEntriesQueryByGeneFunction(input);
		
	}
	
	// return the number of gene symbols in the input string ( includes processing wildcard entries )
	private int retrieveNumberOfGenesQueryByGene() {
		String wildcard = "equals";
		
		String[] geneInput = input.split(" "); 
		System.out.println("BridgePageBean:geneInput count " + geneInput.length);
		int symbolCount = 0;
		if (input != null) {
			inputString = input = input.trim();
			input = input.replaceFirst(" ", ";");
			for (int i=0; i < geneInput.length; i++)
			{
				String tempInput = geneInput[i];
				int stringLen = tempInput.length();
	    		String lastChar = tempInput.trim().substring(stringLen-1, stringLen);
	    		if (lastChar.equals("*")) {
	    			tempInput = tempInput.substring(0, stringLen-1);
	    			wildcard = "starts with";
	    		}
	    		ArrayList<String> geneSymbols = DbUtility.retrieveGeneSymbolsFromGeneInput(tempInput, wildcard);    		
	    		if (geneSymbols != null) {
	    			System.out.println("BridgePageBean:geneSymbols count " + geneSymbols.size());
	    			symbolCount += geneSymbols.size();
	    		} 
			}
		}
		return symbolCount;
/*	
    	// check input string to decide wildcard value
    	if (input != null) {
    		inputString = input = input.trim();
    		int stringLen = input.length();
    		String lastChar = input.trim().substring(stringLen-1, stringLen);
//			System.out.println("last char: " + lastChar);
    		if (lastChar.equals("*")) {
    			input = input.substring(0, stringLen-1);
    			wildcard = "starts with";
//  				System.out.println("input string removed wc: " + input);
    		}
		}
    	System.out.println("BridgePageBean:input strings " + input);
		// get gene symbols
		ArrayList<String> geneSymbols = DbUtility.retrieveGeneSymbolsFromGeneInput(input, wildcard);
		//inputs = (String []) geneSymbols.toArray (new String [geneSymbols.size ()]);

		if (geneSymbols != null) {
			System.out.println("BridgePageBean:retrieveNumberOfGenesQueryByGene = " + geneSymbols.size());
			return geneSymbols.size();
		} else {
			return 0;
		}
*/		
	}

    private void LoadGeneTree(){
    	System.out.println("BridgePageBean::LoadGeneTree");
    	rootGeneNode = new TreeNodeImpl();
    	
        TreeNodeImpl childNode1 = new TreeNodeImpl();
        String nodeText = "All Genes(" + this.numberOfGenesQueryByGene + ")";
        NodeData nodeData = new NodeData(nodeText, "GeneTree1");
        nodeData.setParams("input",input);
        nodeData.setParams("query", "Gene");
        nodes.put(nodeText, nodeData);
        childNode1.setParent(rootGeneNode);
        childNode1.setData(nodeText);
        rootGeneNode.addChild("1", childNode1);
        
         TreeNodeImpl childNode2 = new TreeNodeImpl();
        childNode2.setData("Anchor");
        childNode2.setParent(rootGeneNode);
        rootGeneNode.addChild("2", childNode2);
        
        TreeNodeImpl childNode3 = new TreeNodeImpl();
        childNode3.setData("Marker");
        childNode3.setParent(rootGeneNode);
        rootGeneNode.addChild("3", childNode3);
     }

    private void LoadProbeTree(){
    	//System.out.println("BridgePageBean::LoadProbeTree");
    	rootProbeNode = new TreeNodeImpl();
    	
        TreeNodeImpl childNode1 = new TreeNodeImpl();
        childNode1.setParent(rootProbeNode);
        childNode1.setData("All");
        rootProbeNode.addChild("1", childNode1);

        TreeNodeImpl childNode2 = new TreeNodeImpl();
        childNode2.setData("RNA");
        childNode2.setParent(rootProbeNode);
        rootProbeNode.addChild("2", childNode2);       
	       
	        TreeNodeImpl childNode21 = new TreeNodeImpl();
	        childNode21.setData("riboprobes");
	        childNode21.setParent(childNode2);
	        childNode2.addChild("2.1", childNode21);  
	
	        TreeNodeImpl childNode22 = new TreeNodeImpl();
	        childNode22.setData("Affymetrix");
	        childNode22.setParent(childNode2);
	        childNode2.addChild("2.2", childNode22);  
        
        TreeNodeImpl childNode3 = new TreeNodeImpl();
        childNode3.setData("Antibody");
        childNode3.setParent(rootProbeNode);
        rootProbeNode.addChild("3", childNode3);       
    }

    private void LoadInsituTree(){
    	System.out.println("BridgePageBean::LoadInsituTree");
    	rootInsituNode = new TreeNodeImpl();

        TreeNodeImpl childNode1 = new TreeNodeImpl();
        String nodeText = "All Insitu(" + geneBridgePageAssembler.retrieveNumberOfRowsInGroups()[0] + ")";
        NodeData nodeData = new NodeData(nodeText, "InsituTree1");
        nodeData.setParams("input",input);
        nodeData.setParams("query", "Gene");
        nodeData.setParams("sub", "ish");
        nodeData.setParams("geneWildcard", "contains");
        nodes.put(nodeText, nodeData);
        childNode1.setData(nodeText);
        childNode1.setParent(rootInsituNode);
        rootInsituNode.addChild("1", childNode1);

        TreeNodeImpl childNode2 = new TreeNodeImpl();
        childNode2.setData("expression");
        childNode2.setParent(rootInsituNode);
        rootInsituNode.addChild("2", childNode2);
        
	        TreeNodeImpl childNode21 = new TreeNodeImpl();
	  	    nodeText = "expression present(" + geneBridgePageAssembler.getNumberOfInsituByGeneByEpression("present") + ")";
	        nodeData = new NodeData(nodeText, "InsituTree1");
	        nodeData.setParams("input",input);
	        nodeData.setParams("query", "Gene");
	        nodeData.setParams("sub", "ish");
	        nodeData.setParams("exp", "present");
	        nodeData.setParams("geneWildcard", "contains");
	        nodes.put(nodeText, nodeData);
	        childNode21.setData(nodeText);
	        childNode21.setParent(childNode2);
	        childNode2.addChild("2.1", childNode21);
	       
	        TreeNodeImpl childNode22 = new TreeNodeImpl();
	        nodeText = "expression not detected(" + geneBridgePageAssembler.getNumberOfInsituByGeneByEpression("absent") + ")";
	        nodeData = new NodeData(nodeText, "InsituTree1");
	        nodeData.setParams("input",input);
	        nodeData.setParams("query", "Gene");
	        nodeData.setParams("sub", "ish");
	        nodeData.setParams("exp", "absent");
	        nodeData.setParams("geneWildcard", "contains");
	        nodes.put(nodeText, nodeData);
	        childNode22.setData(nodeText);
	        childNode22.setParent(childNode2);
	        childNode2.addChild("2.2", childNode22);
	       
	        TreeNodeImpl childNode23 = new TreeNodeImpl();
	        nodeText = "expression uncertain(" + geneBridgePageAssembler.getNumberOfInsituByGeneByEpression("unknown") + ")";
	        nodeData = new NodeData(nodeText, "InsituTree1");
	        nodeData.setParams("input",input);
	        nodeData.setParams("query", "Gene");
	        nodeData.setParams("sub", "ish");
	        nodeData.setParams("exp", "unknown");
	        nodeData.setParams("geneWildcard", "contains");
	        nodes.put(nodeText, nodeData);
	        childNode23.setData(nodeText);
	        childNode23.setParent(childNode2);
	        childNode2.addChild("2.3", childNode23);
       
        TreeNodeImpl childNode3 = new TreeNodeImpl();
        childNode3.setData("theiler stage");
        childNode3.setParent(rootInsituNode);
        rootInsituNode.addChild("3", childNode3);
        	
        	for (int i = 0; i < 12; i++)
        	{
        		int stage = 17 + i;
        		String title = "TS" + Integer.toString(stage);
        		String position = "3." + Integer.toString(stage);
        		
    	        TreeNodeImpl childNode = new TreeNodeImpl();
    	        int count = geneBridgePageAssembler.getNumberOfInsituByGeneByStage(Integer.toString(stage));
    	        nodeText = title + "(" + count + ")";
    	        nodeData = new NodeData(nodeText, "InsituTree1");
    	        nodeData.setParams("input",input);
    	        nodeData.setParams("query", "Gene");
    	        nodeData.setParams("sub", "ish");
       	        nodeData.setParams("geneWildcard", "contains");
       	        nodeData.setParams("geneStage", Integer.toString(stage));
     	        nodes.put(nodeText, nodeData);
    	        childNode.setData(nodeText);
    	        childNode.setParent(childNode3);
    	        childNode3.addChild(position, childNode);

        	}
        
        TreeNodeImpl childNode4 = new TreeNodeImpl();
        childNode4.setData("anatomical region");
        childNode4.setParent(rootInsituNode);
        rootInsituNode.addChild("4", childNode4);
        
	        TreeNodeImpl childNode41 = new TreeNodeImpl();
	        nodeText = "Metanephros(" + geneBridgePageAssembler.getNumberOfInsituByGeneByAnatomy("1") + ")";
	        nodeData = new NodeData(nodeText, "InsituTree1");
	        nodeData.setParams("input",input);
	        nodeData.setParams("query", "Gene");
	        nodeData.setParams("sub", "ish");
   	        nodeData.setParams("geneWildcard", "contains");
   	        nodeData.setParams("organ", Integer.toString(1));
 	        nodes.put(nodeText, nodeData);
	        childNode41.setData(nodeText);
	        childNode41.setParent(childNode4);
	        childNode4.addChild("4.1", childNode41);
	       
	        TreeNodeImpl childNode42 = new TreeNodeImpl();
	        nodeText = "Lower Urinary Tract(" + geneBridgePageAssembler.getNumberOfInsituByGeneByAnatomy("5") + ")";
	        nodeData.setParams("input",input);
	        nodeData.setParams("query", "Gene");
	        nodeData.setParams("sub", "ish");
   	        nodeData.setParams("geneWildcard", "contains");
   	        nodeData.setParams("organ", Integer.toString(5));
	        childNode42.setData(nodeText);
	        childNode42.setParent(childNode4);
	        childNode4.addChild("4.2", childNode42);
	       
	        TreeNodeImpl childNode43 = new TreeNodeImpl();
	        nodeText = "Male Repoductive System(" + geneBridgePageAssembler.getNumberOfInsituByGeneByAnatomy("3") + ")";
	        nodeData.setParams("input",input);
	        nodeData.setParams("query", "Gene");
	        nodeData.setParams("sub", "ish");
   	        nodeData.setParams("geneWildcard", "contains");
   	        nodeData.setParams("organ", Integer.toString(3));
	        childNode43.setData(nodeText);
	        childNode43.setParent(childNode4);
	        childNode4.addChild("4.3", childNode43);
	       
	        TreeNodeImpl childNode44 = new TreeNodeImpl();
	        nodeText = "Female Repoductive System(" + geneBridgePageAssembler.getNumberOfInsituByGeneByAnatomy("4") + ")";
	        nodeData.setParams("input",input);
	        nodeData.setParams("query", "Gene");
	        nodeData.setParams("sub", "ish");
   	        nodeData.setParams("geneWildcard", "contains");
   	        nodeData.setParams("organ", Integer.toString(4));
	        childNode44.setData(nodeText);
	        childNode44.setParent(childNode4);
	        childNode4.addChild("4.4", childNode44);
	       
	        TreeNodeImpl childNode45 = new TreeNodeImpl();
	        nodeText = "Early Genitourinary System(" + geneBridgePageAssembler.getNumberOfInsituByGeneByAnatomy("2") + ")";
	        nodeData.setParams("input",input);
	        nodeData.setParams("query", "Gene");
	        nodeData.setParams("sub", "ish");
   	        nodeData.setParams("geneWildcard", "contains");
   	        nodeData.setParams("organ", Integer.toString(2));
	        childNode45.setData(nodeText);
	        childNode45.setParent(childNode4);
	        childNode4.addChild("4.5", childNode45);       
    }
    
    private void LoadMicroarrayTree(){
    	System.out.println("BridgePageBean::LoadMicroarrayTree");
    	rootMicroarrayNode = new TreeNodeImpl();
    	
        TreeNodeImpl childNode1 = new TreeNodeImpl();
        String nodeText = "All Microarray";//(" + geneBridgePageAssembler.retrieveNumberOfRowsInGroups()[0] + ")";
        NodeData nodeData = new NodeData(nodeText, "MicroarrayTree1");
        nodeData.setParams("input",input);
        nodeData.setParams("query", "Gene");
        nodeData.setParams("sub", "microarray");
        nodeData.setParams("geneWildcard", "contains");
        nodes.put(nodeText, nodeData);
        childNode1.setData(nodeText);
        childNode1.setParent(rootMicroarrayNode);
        rootMicroarrayNode.addChild("1", childNode1);
        
        TreeNodeImpl childNode2 = new TreeNodeImpl();
        childNode2.setData("Probesets");
        childNode2.setParent(rootMicroarrayNode);
        rootMicroarrayNode.addChild("2", childNode2);

        TreeNodeImpl childNode3 = new TreeNodeImpl();
        childNode3.setData("Series");
        childNode3.setParent(rootMicroarrayNode);
        rootMicroarrayNode.addChild("3", childNode3);

	        TreeNodeImpl childNode31 = new TreeNodeImpl();
	        childNode31.setData("All");
	        childNode31.setParent(childNode3);
	        childNode3.addChild("3.1", childNode31);
	       
	        TreeNodeImpl childNode32 = new TreeNodeImpl();
	        childNode32.setData("Anatomy");
	        childNode32.setParent(childNode3);
	        childNode3.addChild("3.2", childNode32);

		        TreeNodeImpl childNode321 = new TreeNodeImpl();
		        childNode321.setData("Metanephros");
		        childNode321.setParent(childNode32);
		        childNode321.addChild("3.2.1", childNode321);
		       
		        TreeNodeImpl childNode322 = new TreeNodeImpl();
		        childNode322.setData("Lower Urinary Tract");
		        childNode322.setParent(childNode32);
		        childNode32.addChild("3.2.2", childNode322);
		       
		        TreeNodeImpl childNode323 = new TreeNodeImpl();
		        childNode323.setData("Male Repoductive System");
		        childNode323.setParent(childNode32);
		        childNode32.addChild("3.2.3", childNode323);
		       
		        TreeNodeImpl childNode324 = new TreeNodeImpl();
		        childNode324.setData("Female Repoductive System");
		        childNode324.setParent(childNode32);
		        childNode32.addChild("3.2.4", childNode324);
		       
		        TreeNodeImpl childNode325 = new TreeNodeImpl();
		        childNode325.setData("Early Genitourinary System");
		        childNode325.setParent(childNode32);
		        childNode32.addChild("3.2.5", childNode325);
	        
	        TreeNodeImpl childNode33 = new TreeNodeImpl();
	        childNode33.setData("Theiler Stage");
	        childNode33.setParent(childNode3);
	        childNode3.addChild("3.3", childNode33);
	        	
	        	for (int i = 0; i < 12; i++)
	        	{
	        		int stage = 17 + i;
	        		String title = "TS" + Integer.toString(stage);
	        		String position = "3.3." + Integer.toString(stage);
	        		
	    	        TreeNodeImpl childNode = new TreeNodeImpl();
	    	        childNode.setData(title);
	    	        childNode.setParent(childNode3);
	    	        childNode3.addChild(position, childNode);
	        	}


        TreeNodeImpl childNode4 = new TreeNodeImpl();
        childNode4.setData("Samples");
        childNode4.setParent(rootMicroarrayNode);
        rootMicroarrayNode.addChild("4", childNode4);

	        TreeNodeImpl childNode41 = new TreeNodeImpl();
	        childNode41.setData("All");
	        childNode41.setParent(childNode4);
	        childNode4.addChild("4.1", childNode41);
	       
	        TreeNodeImpl childNode42 = new TreeNodeImpl();
	        childNode42.setData("Anatomy");
	        childNode42.setParent(childNode4);
	        childNode4.addChild("4.2", childNode42);

		        TreeNodeImpl childNode421 = new TreeNodeImpl();
		        childNode421.setData("Metanephros");
		        childNode421.setParent(childNode42);
		        childNode421.addChild("4.2.1", childNode421);
		       
		        TreeNodeImpl childNode422 = new TreeNodeImpl();
		        childNode422.setData("Lower Urinary Tract");
		        childNode422.setParent(childNode42);
		        childNode42.addChild("4.2.2", childNode422);
		       
		        TreeNodeImpl childNode423 = new TreeNodeImpl();
		        childNode423.setData("Male Repoductive System");
		        childNode423.setParent(childNode42);
		        childNode42.addChild("4.2.3", childNode423);
		       
		        TreeNodeImpl childNode424 = new TreeNodeImpl();
		        childNode424.setData("Female Repoductive System");
		        childNode424.setParent(childNode42);
		        childNode42.addChild("4.2.4", childNode424);
		       
		        TreeNodeImpl childNode425 = new TreeNodeImpl();
		        childNode425.setData("Early Genitourinary System");
		        childNode425.setParent(childNode42);
		        childNode42.addChild("4.2.5", childNode425);

	        TreeNodeImpl childNode43 = new TreeNodeImpl();
	        childNode43.setData("Theiler Stage");
	        childNode43.setParent(childNode4);
	        childNode4.addChild("4.3", childNode43);
	        	
	        	for (int i = 0; i < 12; i++)
	        	{
	        		int stage = 17 + i;
	        		String title = "TS" + Integer.toString(stage);
	        		String position = "4.3." + Integer.toString(stage);
	        		
	    	        TreeNodeImpl childNode = new TreeNodeImpl();
	    	        childNode.setData(title);
	    	        childNode.setParent(childNode3);
	    	        childNode3.addChild(position, childNode);
	        	}

        TreeNodeImpl childNode5 = new TreeNodeImpl();
        childNode5.setData("Genelists");
        childNode5.setParent(rootMicroarrayNode);
        rootMicroarrayNode.addChild("5", childNode5);
    }

    private void LoadGudmapTree(){
    	rootGudmapNode = new TreeNodeImpl();
    	
        TreeNodeImpl childNode1 = new TreeNodeImpl();
        childNode1.setData("Transgenic Mouse Strains");
        childNode1.setParent(rootGudmapNode);
        rootGudmapNode.addChild("1", childNode1);
    }

    public void processTreeNodeImplSelection(NodeSelectedEvent event) {
		System.out.println("BridgePageBean:processTreeNodeImplSelection");

        HtmlTree tree = (HtmlTree) event.getComponent();
        TreeNode currentNode = tree.getTreeNode(tree.getRowKey());
        NodeData data = nodes.get(currentNode.getData());
        //inputs = data.getParamsList().get("inputs");
        inputs = (String[])data.getParams().get("inputs");
        query = (String)data.getParams().get("query");
        sub = (String)data.getParams().get("sub");
        exp = (String)data.getParams().get("exp");
        organ = (String)data.getParams().get("organ");
        wildcards = (String)data.getParams().get("wildcards");
        geneStage = (String)data.getParams().get("geneStage");
        geneAnnotation = (String)data.getParams().get("geneAnnotation");
        
        System.out.println("BridgePageBean:processTreeNodeImplSelection pageid = " + data.getNodeID());
 
        // now navigate to the requested page
    	//System.out.println("BridgePageBean:processTreeNodeImplSelection - redirect page");
		FacesContext context = FacesContext.getCurrentInstance();
		NavigationHandler navigation = context.getApplication().getNavigationHandler();
		navigation.handleNavigation(FacesContext.getCurrentInstance(), null, data.getNodeID());
	   
   }

    // set the accordion titles
	public String getGeneTitle(){
		return this.geneTitle;
	}
	public String getProbeTitle(){
		return this.probeTitle;
	}
	public String getInsituTitle(){
		return this.insituTitle;
	}
	public String getMicroarrayTitle(){
		return this.microarrayTitle;
	}
	public String getGudmapTitle(){
		return this.gudmapTitle;
	}
	public String getTutorialTitle(){
		return this.tutorialTitle;
	}
	public String getAnatomyTitle(){
		return this.anatomyTitle;
	}

	// initialize the treeviews	
    public TreeNode getRootGeneNode() {
    	LoadGeneTree();
        return rootGeneNode;
    }

    public TreeNode getRootProbeNode() {
    	LoadProbeTree();
        return rootProbeNode;
    }

    public TreeNode getRootInsituNode() {
    	LoadInsituTree();
        return rootInsituNode;
    }
    
    public TreeNode getRootMicroarrayNode() {
    	LoadMicroarrayTree();
        return rootMicroarrayNode;
    }

    public TreeNode getRootGudmapNode() {
    	LoadGudmapTree();
        return rootGudmapNode;
    }

    public TreeNode getRootTutorialNode() {
    	//LoadTutorialTree();
        return rootTutorialNode;
    }

    public TreeNode getRootAnatomyNode() {
    	//LoadAnatomyTree();
        return rootAnatomyNode;
    }

    
	// getters and setters
	public int getNumberOfGenesQueryByGene() {
		return this.numberOfGenesQueryByGene;
	}
	
	public void setNumberOfGenesQueryByGene(int numberOfGenesQueryByGene) {
		this.numberOfGenesQueryByGene = numberOfGenesQueryByGene;
	}
	
	public int[] getNumberOfEntriesQueryByGene() {
		return this.numberOfEntriesQueryByGene;
	}
	
	public void setNumberOfEntriesQueryByGene(int[] numberOfEntriesQueryByGene) {
		this.numberOfEntriesQueryByGene = numberOfEntriesQueryByGene;
	}
	
	public int getNumberOfGenesQueryByGeneFunction() {
		return this.numberOfGenesQueryByGeneFunction;
	}
	
	public void setNumberOfGenesQueryByGeneFunction(int numberOfGenesQueryByGeneFunction) {
		this.numberOfGenesQueryByGeneFunction = numberOfGenesQueryByGeneFunction;
	}
	
	public int getNumberOfEntriesQueryByGeneFunction() {
		return this.numberOfEntriesQueryByGeneFunction;
	}
	
	public void setNumberOfEntriesQueryByGeneFunction(int numberOfEntriesQueryByGeneFunction) {
		this.numberOfEntriesQueryByGeneFunction = numberOfEntriesQueryByGeneFunction;
	}
	
	public int getNumberOfEntriesQueryByAnatomy() {
		return this.numberOfEntriesQueryByAnatomy;
	}
	
	public void setNumberOfEntriesQueryByAnatomy(int numberOfEntriesQueryByAnatomy) {
		this.numberOfEntriesQueryByAnatomy = numberOfEntriesQueryByAnatomy;
	}
	
	public int getNumberOfEntriesQueryByAccession() {
		return this.numberOfEntriesQueryByAccession;
	}
	
	public void setNumberOfEntriesQueryByAccession(int numberOfEntriesQueryByAccession) {
		this.numberOfEntriesQueryByAccession = numberOfEntriesQueryByAccession;
	}
	
	public String getInput() {
		return this.input;
	}

	public String getInputString() {
		return this.inputString;
	}

	public String getNodeTitle() {
		return this.nodeTitle;
	}

	// code to retrieve values
	public String getSearchParams() {
		System.out.println("BridgePageBean:getSearchParams#########");
		HashMap<String, String> params = new HashMap<String, String>();
		String urlParams = null;
		
		params.put("input", input);
		params.put("inputs", input);
		params.put("query", query);
		params.put("sub", sub);
		params.put("exp", exp);
		params.put("organ", organ);
		params.put("wildcards", wildcards);
		params.put("geneStage", geneStage);
		params.put("geneAnnotation", geneAnnotation);
		urlParams = Visit.packParams(params);
		if (urlParams==null)
			return "";
		System.out.println("BridgePageBean:getSearchParams:urlParams: " + urlParams);
		return "?"+urlParams;
	}

	
	
	

}


package gmerg.beans;

import gmerg.assemblers.AdvancedSearchAssembler;
import gmerg.db.AdvancedSearchDBQuery;
import gmerg.utils.CookieOperations;
import gmerg.utils.FacesUtil;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

public class AdvancedQueryBean 
{
	protected String selectedStepOne;
	protected String selectedOutput;
	private SelectItem[] stepOne;
	private boolean renderedISH=true;
	private boolean renderedMicroarray=true;
	private boolean renderedProbe;
	private boolean renderedSample;
	private Hashtable lookup = null;
	private Hashtable lookup2 = null;
	private Hashtable lookupsection = null;
	private Hashtable lookupInDB = null;
	private Hashtable lookupTable = null;
	private ArrayList<Object[]> collection;
	private Object[][] ish;
	private Object[][] mic;
	private Object[][] both;
	private boolean[] rendered= new boolean[9];
    private boolean renderOrNot;
    
	public AdvancedQueryBean() {
		lookup = null;
		lookup = AdvancedSearchDBQuery.getLookup();

		lookup2 = null;
		lookup2 = AdvancedSearchDBQuery.getLookup2();

		lookupsection = null;
		lookupsection = AdvancedSearchDBQuery.getLookupsection();

		lookupInDB = null;
		lookupInDB = AdvancedSearchDBQuery.getLookupInDB();

		lookupTable = null;
		lookupTable = AdvancedSearchDBQuery.getLookupTable();

	}
	
	public boolean isRenderOrNot() {
		this.renderOrNot = true;
		return renderOrNot;
	}

	public void setRenderOrNot(boolean renderOrNot) {
		this.renderOrNot = renderOrNot;
	}	

    public String advancedQuery() {
    	
    	if(null != selectedStepOne) {
    		if(selectedStepOne.equals("ISH")) {
    			renderedISH=false;
    			renderedMicroarray=true;
    		} else if(selectedStepOne.equals("Microarray")) {
    			renderedISH=true;
    			renderedMicroarray=false;
    		} else if(selectedStepOne.equals("Both")) {
    			renderedISH=false;
    			renderedMicroarray=false;
    		}
    	}
    	//System.out.println("Input:"+selectedStepOne+":"+renderedISH+":"+renderedMicroarray);
    	return "AdvancedQuery";
    }
    
    public String addOption() {
    	FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();
        Map requestParams = externalContext.getRequestParameterMap();
        String organName = (String) requestParams.get("name");

        addToCollection((String)lookup.get(organName));
    	
    	return "AdvancedQuery";
    }

    public String startSearch() { 
    	if(null != collection) {    
    		
    		// borrowed from Chris - show the content of collection - start
//    		for(int i=0;i<collection.size();i++){
//    			System.out.println("the size of the collection is: "+collection.size());
//    			Object [] obj = (Object []) collection.get(i);
//    			System.out.println("the size of the object at element "+i+" in collection is: "+obj.length);
//    			for(int j=0;j<obj.length;j++){
//    				System.out.println("In the object stored in collection the element at "+j+" is: "+obj[j]);
//    				if(j==1) {
//    					Object [] obj2 = (Object []) obj[j];
//    					System.out.println("The length the array ojbect stored under element "+j+" of object 1 is: "+obj2.length);
//    					for(int k=0;k<obj2.length;k++){
//    						Object [] obj3 = (Object []) obj2[k];
//    						System.out.println("obj3 length: "+obj3.length);
//    						for(int l=0;l<obj3.length;l++){
//    							System.out.println("obj3 at element "+l+": "+obj3[l]);
//    						}
//    					}
//    				}
//    			}
//    		}
    		// borrowed from Chris - end
    		
    		ArrayList results = new ArrayList();
    		AdvancedSearchAssembler assembler = new AdvancedSearchAssembler();
    		String sub = getSelectedStepOne();
    		if(null == sub)
    			sub = "Both";
			results = assembler.getQueryResult(collection, null, null, null, null, sub);
			
    		FacesUtil.setSessionValue("ad_query_option", collection);
    		FacesUtil.setSessionValue("ad_query_all_subs", null);
    		    			
    		FacesUtil.setSessionValue("ad_query_sub", sub);
    		
    		if(null == results) {
    			return "NoResult";
    		} else {
    			return "Result";
    		}
    	}
    	return "AdvancedQuery";
    }
    
    public String changeProbe() {
    	FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();
        Map requestParams = externalContext.getRequestParameterMap();
        String section = (String) requestParams.get("section");
        String expand = (String) requestParams.get("expand");
           			
		if(expand.equals("Y")) {
		    rendered[Integer.parseInt((String)lookupsection.get(section))]=true;
		} else if(expand.equals("N")){
			rendered[Integer.parseInt((String)lookupsection.get(section))]=false;
		}
    			    			
    	return "AdvancedQuery";
    }

    public String removeCollection() {
    	FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();
        Map requestParams = externalContext.getRequestParameterMap();
        String section = (String) requestParams.get("collection");
                   					
        deleteFromCollection(section);		    			
    	return "AdvancedQuery";
    }
    
    public String removeAll() {
        this.deleteAllFromCollection();
        return "AdvancedQuery";
    }
    
    public String clearAllValues() {
        String subIds = CookieOperations.getCookieValue("SearchFieldName");
        
        if (!subIds.equals("")) {
            String[] submissionIDs = subIds.split("/");
            collection = new ArrayList<Object[]>();
            String label = null;
            Object[] value = null;
            for (int i = 0; i < submissionIDs.length; i++) {
                label = submissionIDs[i];                       
                value = findViewAll(submissionIDs[i]);                     
                collection.add(new Object[]{label, value});
            }
        }
        
        return "AdvancedQuery";
    }
    

	public SelectItem[] getStepOne() {		
		
		stepOne = new SelectItem[]{new SelectItem("ISH", "ISH"), 
                new SelectItem("Microarray", "Microarray"),
                new SelectItem("Both", "Both")};
		return stepOne;
	}

	public String getSelectedStepOne() {
		return selectedStepOne;
	}

	public void setSelectedStepOne(String selectedStepOne) {
		this.selectedStepOne = selectedStepOne;
	}

	public boolean getRenderedISH() {
		return renderedISH;
	}

	public boolean getRenderedMicroarray() {
		return renderedMicroarray;
	}

	public boolean getRenderedProbe() {
		return renderedProbe;
	}
	
	public boolean getRenderedSample() {
		return renderedSample;
	}
    	
        
	
    /*public ArrayList getCollection() {
    	
    	
        FacesContext ctxt = FacesContext.getCurrentInstance();
        String subIds = CookieOperations.getCookieValue("SearchFieldName", ctxt);
        //System.out.println("COOKIE get:"+subIds);

        //System.out.println("After Cookie two:"+CookieOperations.getCookieValue("MicFocusEmapID", ctxt));
        if (!subIds.equals("")) {
        	//System.out.println("After Append:"+subIds+":"+addedId);
            //note real code will put 'submissionIDs' into a method parameter for DBHelper (or other class) and used to get the correct result set back.
            String[] submissionIDs = subIds.split("/");
            collection = new ArrayList();
            String label = null;
            Object[] value = null;
            for (int i = 0; i < submissionIDs.length; i++) {
                label = submissionIDs[i];    
                value = findViewAll(submissionIDs[i]);  
                collection.add(new Object[]{label, value});
            }
        } else {
        	collection = new ArrayList();
        }
        return collection;
    }*/
	
	   public ArrayList getCollection() {
			String subIds = CookieOperations.getCookieValue("SearchFieldName");
			//System.out.println("COOKIE get:"+subIds);
			
			//System.out.println("After Cookie two:"+CookieOperations.getCookieValue("MicFocusEmapID", ctxt));
			if (!subIds.equals("")) {
			 //System.out.println("After Append:"+subIds+":"+addedId);
			 //note real code will put 'submissionIDs' into a method parameter for DBHelper (or other class) and used to get the correct result set back.
			 String[] submissionIDs = subIds.split("/");
			            //boolean array used to flag which of the collection identifiers have already been defined
			 boolean [] alreadyDefined = new boolean [submissionIDs.length];
			 //by default these are all set to false
			 for(int i=0;i<alreadyDefined.length;i++){
			     alreadyDefined[i] = false;
			 }
			            //only create new collection if not already created
			 if(collection == null){
			     collection = new ArrayList<Object[]>();
			 }
			 else {
			                    //boolean array to flag which items in the collection are marked for
			     //removal (i.e. a corresponding entry doesn't exist in submissionIDs)
			     boolean [] markedForRemoval = new boolean [collection.size()];
			     //by default, they are all set to true
			     for(int i=0;i<markedForRemoval.length;i++){
			         markedForRemoval[i] = true;
			     }
			                    //go through each item in the collection. If a particular
			     //label is in the collection but not on the list of labels
			     //obtained from the cookie, it should be marked for removal
			     //from the list
			     for(int i=0;i<collection.size();i++){
			         Object [] obj = (Object [])collection.get(i);
			         String lab = (String)obj[0];
			         for(int j= 0;j<submissionIDs.length;j++){
			             if(lab.equals(submissionIDs[j])){
			                 markedForRemoval[i] = false;
			             }
			         }
			     }
			                    //reomve all elements marked for removal from the collection
			     for(int i=0;i<markedForRemoval.length;i++){
			         if(markedForRemoval[i]){
			             collection.remove(i);
			         }
			     }
			                /*go through each string in the list obtained from cookie.
			     If a particular string in the array matches one of those in
			     the collection elements,it should be marked as not neeeding
			     to be added to the arraylist*/
			     for(int i=0;i<submissionIDs.length; i++){
			         for(int j=0;j<collection.size();j++){
			             Object [] obj = (Object [])collection.get(j);
			             String lab = (String)obj[0];
			             if(lab.equals(submissionIDs[i])){
			                 alreadyDefined[i] = true;
			             }
			         }
			     }
			 }
			 String label = null;
			 Object[] value = null;
			 for (int i = 0; i < submissionIDs.length; i++) {
			     //only add to collection if not already defined in colleciton
			     if(!alreadyDefined[i]){
			         label = submissionIDs[i];                       
			         value = findViewAll(submissionIDs[i]);                     
			         collection.add(new Object[]{label, value});
			     }
			 }
			} else {
			 collection = new ArrayList<Object[]>();
			}
			return collection;
	}
	
    
    

	public Object[] findViewAll(String componentName) {
		if(null != componentName) {
			
			if(componentName.indexOf("Probe Name") > -1
					|| componentName.indexOf("Gene Name") > -1
					|| componentName.indexOf("Gene Synonym") > -1
					|| componentName.indexOf("MGI ID") > -1
					|| componentName.indexOf("Ensembl ID") > -1
					|| componentName.indexOf("GenBank ID") > -1
					|| componentName.indexOf("Submission Date") > -1//3
					|| componentName.indexOf("Series GEO ID") > -1
					|| componentName.indexOf("Series Title") > -1
					|| componentName.indexOf("Sample GEO ID") > -1
					|| componentName.indexOf("Clone Name") > -1
					|| componentName.indexOf("Affymetrix ID") > -1) {
				return new Object[]{new Object[]{new String(""), 
						new String(""), 
						true, 
						false,
						null,
						false,
						new String("")}};
			} else if(componentName.indexOf("Tissue") > -1
					|| componentName.indexOf("Probe Strain") > -1
					|| componentName.indexOf("Gene Type") > -1
					|| componentName.indexOf("Visualization Method") > -1
					|| componentName.indexOf("Assay Type") > -1
					|| componentName.indexOf("Fixation Method") > -1
					|| componentName.indexOf("Specimen Strain") > -1
					|| componentName.indexOf("Sex") > -1
					|| componentName.indexOf("Stage") > -1
					|| componentName.indexOf("Age") > -1
					|| componentName.indexOf("Genotype") > -1
					|| componentName.indexOf("Lab Name") > -1
					|| componentName.indexOf("Pattern") > -1
					|| componentName.indexOf("Platform GEO ID") > -1
					|| componentName.indexOf("Platform Title") > -1
					|| componentName.indexOf("Platform Name") > -1
					|| componentName.indexOf("Sample Strain") > -1
					|| componentName.indexOf("Sample Sex") > -1
					|| componentName.indexOf("Sample Age") > -1
					|| componentName.indexOf("Sample Stage") > -1) {
				AdvancedSearchAssembler assembler = new AdvancedSearchAssembler();
				//System.out.println("OUT:"+componentName);
				ArrayList results = assembler.getOptionInDB((String)lookupInDB.get(componentName), lookupTable);				
				SelectItem[] items = new SelectItem[results.size()];
				for(int i = 0; i < results.size(); i++) {
					items[i] = new SelectItem((String)results.get(i), (String)results.get(i));
				}				                                    								
				return new Object[]{new Object[]{new String(""), 
						new String(""), 
						false, 
						true, 
						items,
						false,
						new String("")}};
			} else if(componentName.indexOf("EMAP ID") > -1
					|| componentName.indexOf("Ontology Terms") > -1){
				SelectItem[] items = new SelectItem[]{
					new SelectItem("present", "present"),
					new SelectItem("not detected", "not detected")
				};	
				return new Object[]{new Object[]{new String(""), 
						new String(""), 
						true, 
						false,
						null,
						false,
						new String("")}
				/*return new Object[]{new Object[]{new String(""), 
						new String(""), 
						true, 
						true, 
						items,
						false,
						new String("")}*//*,
				new Object[]{new String(""), 
						new String(""), 
						true, 
						true, 
						items,
						false,
						new String("")}*/};
			} else if(componentName.indexOf("Location") > -1){
				AdvancedSearchAssembler assembler = new AdvancedSearchAssembler();
				ArrayList results = new ArrayList();
				results = assembler.getOptionInDB((String)lookupInDB.get(componentName), lookupTable);				
				SelectItem[] items = new SelectItem[results.size()+1];
				for(int i = 0; i < results.size(); i++) {
					items[i] = new SelectItem((String)results.get(i), (String)results.get(i));
				}
				items[results.size()] = new SelectItem("adjacent to ", "adjacent to ");				
				return new Object[]{new Object[]{new String(""), 
						new String(""), 
						true, 
						true, 
						items,
						false,
						new String("")}};
			}  else if(componentName.indexOf("Gene Symbol") > -1){
				
				return new Object[]{new Object[]{new String(""), 
						new String(""), 
						false, 
						false, 
						null,
						true,
						new String("")}};
			}          
		}
		return null;
	}    

    public void addToCollection(String organName) {       
    	if(null != organName) {
    		//System.out.println("addToCollection:"+organName);
	        //send the values to 'CookieOperations' where they will be stored as cookies
	        CookieOperations.setValuesInCookie("SearchFieldName", new String[]{organName});
    	}
        
    }
    
    public void deleteAllFromCollection(){
        CookieOperations.removeAllValuesFromCookie("SearchFieldName");
    }

    public void deleteFromCollection(String id) {
        if (id != null) {
        	//System.out.println("deleteFromCollection:"+id);
            //send the values to 'CookieOperations' where they will be stored as cookies
            CookieOperations.removeSelectedValuesFromCookie("SearchFieldName", new String[]{id});
        } else {
            //create faces message for display in browser
        }
    }    
    
	public Object[][] getIsh() {
		ish = new Object[1][4];
		ish[0]= new Object[]{
					new Object[]{
					    new Object[]{"Probe Name",(String)lookup2.get("Probe Name")},
					    new Object[]{"Clone Name",(String)lookup2.get("Clone Name")},
					    new Object[]{"Tissue",(String)lookup2.get("Tissue")},
					    new Object[]{"Probe Strain",(String)lookup2.get("Probe Strain")},
					    new Object[]{"Gene Type",(String)lookup2.get("Gene Type")},
					    new Object[]{"Visualization Method",(String)lookup2.get("Visualization Method")}
			        },
					rendered[0],"Probe","probe"};						
		
		return ish;
	} 	
	
	public Object[][] getMic() {
		mic = new Object[4][4];
		mic[0]= new Object[]{
					new Object[]{
					    new Object[]{"Sample GEO ID",(String)lookup2.get("Sample GEO ID")},
					    new Object[]{"Sample Strain",(String)lookup2.get("Sample Strain")},
					    new Object[]{"Sample Sex",(String)lookup2.get("Sample Sex")},
					    new Object[]{"Sample Age",(String)lookup2.get("Sample Age")},
					    new Object[]{"Sample Stage",(String)lookup2.get("Sample Stage")}
					},
					rendered[2],"Sample","sample"};						
		mic[1]= new Object[]{
				new Object[]{
				    new Object[]{"Series Title",(String)lookup2.get("Series Title")},
				    new Object[]{"Series GEO ID",(String)lookup2.get("Series GEO ID")}},
				rendered[3],"Series","series"};	
		mic[2]= new Object[]{
				new Object[]{
				    new Object[]{"Platform GEO ID",(String)lookup2.get("Platform GEO ID")},
				    new Object[]{"Platform Title",(String)lookup2.get("Platform Title")},
				    new Object[]{"Platform Name",(String)lookup2.get("Platform Name")}
				},
				rendered[7],"Platform","platform"};						
		mic[3]= new Object[]{
				new Object[]{
				    new Object[]{"Affymetrix ID",(String)lookup2.get("Affymetrix ID")}},
				rendered[8],"Probe Set","probeset"};		
		return mic;
	} 	
	
	public Object[][] getBoth() {
		both = new Object[4][4];
		both[0]= new Object[]{
				new Object[]{
				    new Object[]{"Gene Symbol",(String)lookup2.get("Gene Symbol")},
				    new Object[]{"Gene Name",(String)lookup2.get("Gene Name")},
				    new Object[]{"Gene Synonym",(String)lookup2.get("Gene Synonym")},
				    new Object[]{"MGI ID",(String)lookup2.get("MGI ID")},
				    new Object[]{"Ensembl ID",(String)lookup2.get("Ensembl ID")},
				    new Object[]{"GenBank ID",(String)lookup2.get("GenBank ID")}},
				rendered[1],"Gene","gene"};		
		both[1]= new Object[]{
				new Object[]{
				    new Object[]{"EMAP ID",(String)lookup2.get("EMAP ID")},
				    new Object[]{"Ontology Terms",(String)lookup2.get("Ontology Terms")},
				    new Object[]{"Pattern",(String)lookup2.get("Pattern")},
				    new Object[]{"Location",(String)lookup2.get("Location")}},
				rendered[4],"Ontology","ontology"};
		both[2]= new Object[]{
					new Object[]{
					    new Object[]{"Lab Name",(String)lookup2.get("Lab Name")},
					    new Object[]{"Submission Date",(String)lookup2.get("Submission Date")}},
					rendered[6],"Lab","lab"};						
		both[3]= new Object[]{
				new Object[]{
				    new Object[]{"Assay Type",(String)lookup2.get("Assay Type")},
				    new Object[]{"Fixation Method",(String)lookup2.get("Fixation Method")},
				    new Object[]{"Specimen Strain",(String)lookup2.get("Specimen Strain")},
				    new Object[]{"Sex",(String)lookup2.get("Sex")},
				    new Object[]{"Stage",(String)lookup2.get("Stage")},
				    new Object[]{"Age",(String)lookup2.get("Age")},
				    new Object[]{"Genotype",(String)lookup2.get("Genotype")}
				},
				rendered[5],"Specimen","specimen"};

		return both;
	}

	public void setCollection(ArrayList<Object[]> collection) {
		this.collection = collection;
	}

	public String getSelectedOutput() {
		return selectedOutput;
	}

	public void setSelectedOutput(String selectedOutput) {
		this.selectedOutput = selectedOutput;
	} 	

}

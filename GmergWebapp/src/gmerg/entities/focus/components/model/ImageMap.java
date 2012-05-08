/*
 * Copyright 2004-2005 Sun Microsystems, Inc.  All rights reserved.
 * Use is subject to license terms.
 */


package gmerg.entities.focus.components.model;


import gmerg.beans.CollectionAnatomyBean;
import gmerg.entities.focus.components.listeners.AreaSelectedEvent;
import gmerg.utils.FacesUtil;

import java.util.HashMap;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.ArrayDataModel;
import javax.faces.model.DataModel;
import javax.faces.model.SelectItem;

/**
 * ImageMap is the "backing file" class for the image map application.
 * It contains a method event handler that sets the locale from
 * information in the <code>AreaSelectedEvent</code> event.
 */
public class ImageMap {

	String emapId;
    protected ArrayDataModel model = null;
    protected Object[][] subs;
    protected SelectItem[] organRadioList;
    protected String selectedOrgan;
    protected String operation;
    protected String diagramURL;
    protected boolean diagram = false;
    ImageArea[] areaList = null;
    String prefix = "http://www.gudmap.org/gudmap";
    //String prefix = "..";
    //protected ArrayAnatomyCollection[] collection;
    //protected ArrayDataModel collectionModel = null;
	// ------------------------------------------------------ Instance Variables


    /**
     * <p>The locales to be selected for each hotspot, keyed by the
     * alternate text for that area.</p>
     */
    private Map locales = null;

    // ------------------------------------------------------------ Constructors

    /**
     * <p>Construct a new instance of this image map.</p>
     */
    public ImageMap() {
        locales = new HashMap();
        /*locales.put("NAmerica", Locale.ENGLISH);
        locales.put("SAmerica", new Locale("es", "es"));
        locales.put("Germany", Locale.GERMAN);
        locales.put("Finland", new Locale("fi", "fi"));
        locales.put("France", Locale.FRENCH);
        locales.put("sshape", new Locale("ss", "ss"));
        locales.put("tubule", new Locale("tu", "tu"));*/
        locales.put("sshape", "S-shaped bodies");//"EMAP:27855");
        locales.put("tubule", "Proximal tubules");//"EMAP:28005");
        locales.put("testes", "testes");
        locales.put("epididymis", "epididymis");
        locales.put("ovary", "ovary");
        locales.put("bladder", "bladder");

    }


    /**
     * <p>Select a new Locale based on this event.</p>
     *
     * @param actionEvent The {@link AreaSelectedEvent} that has occurred
     */
    public void processAreaSelected(ActionEvent actionEvent) {
    	//System.out.println("FROM ACTION LISTENER");
        AreaSelectedEvent event = (AreaSelectedEvent) actionEvent;
        String current = event.getMapComponent().getCurrent();
        
        
        setEmapId((String)locales.get(current));
        //setFacesSessionValue("selectedEmapID", (String)locales.get(current));
        addToCollection((String)locales.get(current));
        //setFacesSessionValue("selectedEmapID", null);
        /*try {
        HttpServletResponse response = (HttpServletResponse)context.getExternalContext().getResponse();
        response.sendRedirect("/mic_focus.jsf");        
        }
        catch (IOException ex) {
	        System.err.println("IOException"+ex.getMessage());
	        System.err.println(ex.getStackTrace());
        }*/
        /*try {
        context.getExternalContext().redirect(context.getExternalContext().getRequestContextPath()+"/mic_focus.jsf");
        }
        catch (IOException ex) {
	        System.err.println("IOException"+ex.getMessage());
	        System.err.println(ex.getStackTrace());
        }*/
    }    

    /*
     * Event handling when user click a region in the detailed diagram, this region will be added to my anatomy collection
     */
    public String processAreaSelected() {
    	addToCollection(getEmapId());
        return"viewCollection";
    }        

  
    public void addToCollection(String organName) {   

        Object object = 
            FacesContext
                .getCurrentInstance()
                    .getExternalContext()
                        .getRequestMap()
                            .get("micCollectionAnatomyBean");
        // This only works if myBean2 is session scoped.
        if (object != null) {
        	CollectionAnatomyBean myBean2 = (CollectionAnatomyBean) object; 
            myBean2.addToCollection(organName);
        }    	
    }    
    
    /*public DataModel getCollection() {
        collection = new ArrayAnatomyCollection[1];
        collection[0] = new ArrayAnatomyCollection();
        collection[0].setDownload("Download");
        collection[0].setView("View");
        collection[0].setOrganName("Kidney");
        collection[0].setSelected(true);
        collectionModel = new ArrayDataModel(collection);
        return collectionModel;
    }*/    

    /*
     * Set the display of organ list
     */
    
    public DataModel getSubmissions() {
    	subs = new Object[][]{{"Testis", "testes", "", "", false}, 
    			              {"Epididymis", "epididymis", "", "", false},
    			              {"Ovary", "ovary", "", "", false},
    			              {"Bladder", "bladder", "", "", false},
    			              {"Kidney", "kidney", "", "16", true}};//16 is lab ID

        model = new ArrayDataModel(subs);
        return model;
    }        
    
    /*
     * Event handling when user choose a organ in the organ list and submit, either add it to my collection or display detailed diagram
     */
    public String chooseOrgan() {
    	//System.out.println("GO:"+getSelectedOrgan());
    	if(null != getSelectedOrgan()) {
	    	String operation = getOperation();
	    	if(operation.equals("addToCollection")) {
	    		//setFacesSessionValue("selectedEmapID", getSelectedOrgan());
	    		addToCollection(getSelectedOrgan());
	    		//setFacesSessionValue("selectedEmapID", null);
	    	} else if(operation.equals("showDiagram")) {
//	    		System.out.println("Selected Organ:"+getSelectedOrgan());
	    		setDiagramURL(getImageURL(getSelectedOrgan()));
	    		setAreaBean(getSelectedOrgan());
	    		setDiagram(true);
	    	}
    	}
    	return"viewCollection";
    	//System.out.println("Selected Organ:"+getSelectedOrgan()+":"+getOperation());     
    }                      

    /*
     * Empty my anatomy collection
     */
    public String emptyCollection() {
    	 Object object = 
             FacesContext
                 .getCurrentInstance()
                     .getExternalContext()
                         .getRequestMap()
                             .get("micCollectionAnatomyBean");
         // This only works if myBean2 is session scoped.
         if (object != null) {
         	CollectionAnatomyBean myBean2 = (CollectionAnatomyBean) object; 
             myBean2.emptyCollection();
         }   
        return "viewCollection";
    }                      

    /*
     * Delete the entries in my anatomy collection
     */
    public String deleteCollection() {
    	 Object object = 
             FacesContext
                 .getCurrentInstance()
                     .getExternalContext()
                         .getRequestMap()
                             .get("micCollectionAnatomyBean");
         // This only works if myBean2 is session scoped.
         if (object != null) {
         	CollectionAnatomyBean myBean2 = (CollectionAnatomyBean) object; 
             myBean2.deleteFromCollection();
         }   
        return "viewCollection";
    }                          
    
	public String getImageURL(String componentID) {
		if(null == componentID || componentID.equals(""))
			return "";
		if(componentID.indexOf("testes") > -1 || componentID.indexOf("epididymis") > -1) {
			return prefix+"/images/focus/ca_testesEpididymis.gif";
		} else if(componentID.indexOf("ovary") > -1) {
			return prefix+"/images/focus/ca_ovary.gif";
		} else if(componentID.indexOf("bladder") > -1) {
			return prefix+"/images/focus/ca_bladder.gif";
		} else if(componentID.indexOf("kidney") > -1) {
			return prefix+"/images/focus/TS23metanephrosG.gif";
		}
		return "";
	}	    

	public void setAreaBean(String organ){
    	Object object = null;
    	ImageArea[] myBean = new ImageArea[3];
    	String[][] list = getAreaCoordinate(organ);
    	for(int i = 1; i <= list.length; i++) {
    		myBean[i-1] = new ImageArea();
            myBean[i-1].setAlt(list[i-1][0]);
            myBean[i-1].setCoords(list[i-1][2]);
            myBean[i-1].setShape(list[i-1][1]);
	    	/*object = null;
	        object = FacesContext
	                .getCurrentInstance()
	                    .getExternalContext()
	                        .getRequestMap()
	                            .get("area"+i);
	                            
	        // This only works if myBean2 is request scoped.
	        if (object != null) {
	        	myBean = null;
	        	myBean = (ImageArea) object; 
	        	System.out.println("BEAN:"+list[i-1][0]+":"+list[i-1][1]+":"+list[i-1][2]);
	            myBean.setAlt(list[i-1][0]);
	            myBean.setCoords(list[i-1][2]);
	            myBean.setShape(list[i-1][1]);
	        } else {
	        	System.out.println("BEAN NULL:"+"area"+i); 
	        }*/
    	}
    	if(list.length < 3) {
    		for(int i = list.length+1; i <= 3; i++) {
        		myBean[i-1] = new ImageArea();
                myBean[i-1].setAlt("NA"+i);
                myBean[i-1].setCoords("0,0,0,0");
                myBean[i-1].setShape("RECT");    			
    	    	/*object = null;
    	        object = FacesContext
    	                .getCurrentInstance()
    	                    .getExternalContext()
    	                        .getRequestMap()
    	                            .get("area"+i);
    	                            
    	        // This only works if myBean2 is request scoped.
    	        if (object != null) {
    	        	myBean = null;
    	        	myBean = (ImageArea) object; 
    	            myBean.setAlt("NA"+i);
    	            myBean.setCoords("0,0,0,0");
    	            myBean.setShape("RECT");
    	            System.out.println("BEAN:DEFAULT"); 
    	        } else {
    	        	System.out.println("BEAN NULL:"+"area"+i); 
    	        }*/
    		}
    	}
    	
    	setAreaList(myBean);
	}
	
	public String[][] getAreaCoordinate(String organ){
		String[][] oneArea = null;
		if(null != organ && !organ.equals("")) {
			if(organ.indexOf("kidney") > -1) {	
				oneArea = new String[3][3];	
				oneArea[0] = new String[]{"sshape", "poly", "230,29,239,29,242,32,242,33,243,34,243,36,242,37,242,38,243,39,244,38,245,38,246,37,247,37,248,36,249,36,250,35,254,35,256,37,257,37,259,39,260,39,263,42,263,45,264,46,264,53,263,54,263,56,262,57,262,59,261,60,261,61,259,63,259,64,254,69,252,69,251,70,241,70,240,69,240,66,239,65,236,65,235,66,235,67,234,68,233,68,231,70,228,70,228,69,230,69,234,65,235,65,236,64,235,63,230,63,229,62,228,62,227,61,226,61,222,57,222,56,220,54,220,46,221,45,221,44,222,43,222,41,223,40,223,39,224,38,224,37,225,36,225,34,227,32,227,31,228,30,229,30,230,29"};		
				oneArea[1] = new String[]{"tubule", "poly", "510,129,515,129,516,130,518,130,519,131,520,131,522,133,522,134,523,135,523,136,524,137,524,138,527,141,538,141,541,144,541,145,542,146,542,150,541,151,541,152,536,157,533,157,532,158,530,158,527,161,527,164,529,166,529,167,532,170,532,176,530,178,530,180,528,182,527,182,526,183,517,183,517,182,515,180,513,180,512,179,511,179,509,177,507,177,506,178,506,191,507,192,506,193,501,193,500,192,499,192,496,189,495,189,495,188,492,185,492,184,491,183,490,183,488,185,488,189,487,190,487,200,486,201,482,201,482,200,480,198,479,198,479,196,477,194,472,194,469,197,469,198,468,199,468,205,467,206,468,207,467,208,467,210,466,211,466,215,465,216,465,222,464,223,464,226,465,227,465,233,464,234,464,239,463,240,463,243,462,244,461,244,460,245,460,246,459,247,457,247,457,246,456,245,456,244,455,243,454,243,454,241,453,240,453,238,452,237,452,231,453,230,453,229,454,228,454,226,455,225,455,220,456,219,456,215,457,214,457,210,458,209,458,204,459,203,459,199,460,198,460,196,461,195,461,190,462,189,462,186,464,184,471,184,472,185,473,185,475,187,476,187,477,188,478,188,479,189,480,189,481,188,481,178,482,177,481,176,481,173,483,171,486,171,488,173,489,173,493,177,497,177,498,176,498,175,499,174,499,173,500,172,500,170,501,169,501,168,503,166,504,167,507,167,510,170,511,170,513,172,515,172,517,174,518,174,521,171,521,167,517,163,517,157,518,156,519,156,520,155,522,155,523,154,524,154,525,153,526,153,527,152,528,152,531,149,530,148,520,148,519,147,519,146,518,145,518,143,515,140,513,140,509,144,505,144,504,143,504,138,505,137,505,135,506,134,506,133,510,129"};	
				oneArea[2] = new String[]{"tubule", "poly", "315,77,323,77,324,78,325,78,326,79,327,79,327,80,328,81,328,82,327,83,327,86,326,87,326,89,325,90,325,91,324,92,324,93,321,96,320,96,319,97,317,97,316,98,314,98,313,99,306,99,305,98,304,98,303,97,301,97,300,96,299,96,298,95,296,95,295,94,292,94,291,95,291,98,290,99,290,101,289,102,289,103,288,103,286,105,286,106,284,108,281,108,280,107,280,104,281,103,279,101,279,89,284,84,301,84,302,85,304,85,305,86,311,86,312,85,312,81,313,80,313,79,315,77"};					
			} else if(organ.indexOf("testes") > -1) {	
				oneArea = new String[1][3];	
				oneArea[0] = new String[]{"testes", "RECT", "0,0,200,200"};				
			} else if(organ.indexOf("epididymis") > -1) {	
				oneArea = new String[1][3];	
				oneArea[0] = new String[]{"epididymis", "RECT", "0,0,200,200"};				
			} else if(organ.indexOf("ovary") > -1) {	
				oneArea = new String[1][3];	
				oneArea[0] = new String[]{"ovary", "RECT", "0,0,200,200"};				
			} else if(organ.indexOf("bladder") > -1) {	
				oneArea = new String[1][3];	
				oneArea[0] = new String[]{"bladder", "RECT", "0,0,200,200"};				
			}
		}
		return oneArea;
	}
    /**
     * <p>Return an indication for navigation.  Application using this component,
     * can refer to this method via an <code>action</code> expression in their
     * page, and set up the "outcome" (success) in their navigation rule.
     */
    public String status() {
        return "success";
    }


	public String getEmapId() {

		return emapId;
	}


	public void setEmapId(String emapId) {
		if(null == emapId || emapId.equals("null"))
			emapId = "";
		this.emapId = emapId;
	}

    /*
     * Populate the radio buttons in the organ list
     */
	public SelectItem[] getOrganRadioList() {
		organRadioList = new SelectItem[]{new SelectItem("testes", ""), 
				                          new SelectItem("epididymis", ""),
				                          new SelectItem("ovary", ""),
				                          new SelectItem("bladder", ""),
				                          new SelectItem("kidney", "")};
		return organRadioList;
	}


	public void setOrganRadioList(SelectItem[] organRadioList) {
		this.organRadioList = organRadioList;
	}


	public String getSelectedOrgan() {
		return selectedOrgan;
	}


	public void setSelectedOrgan(String selectedOrgan) {
//		System.out.println("GET DIAGRAM:"+selectedOrgan);
		this.selectedOrgan = selectedOrgan;
	}


	public String getOperation() {
		return operation;
	}


	public void setOperation(String operation) {
		this.operation = operation;
	}


	public String getDiagramURL() {
		if(null == diagramURL)
			diagramURL = prefix+"/images/focus/TS23metanephrosG.gif";
		
		return diagramURL;
	}


	public void setDiagramURL(String diagramURL) {
		this.diagramURL = diagramURL;
	}


	public boolean getDiagram() {
		return diagram;
	}


	public void setDiagram(boolean isDiagram) {
		this.diagram = isDiagram;
	}


	public ImageArea[] getAreaList() {
        if(null == areaList) {
            areaList = new ImageArea[3];
            areaList[0] = new ImageArea("sshape", "230,29,239,29,242,32,242,33,243,34,243,36,242,37,242,38,243,39,244,38,245,38,246,37,247,37,248,36,249,36,250,35,254,35,256,37,257,37,259,39,260,39,263,42,263,45,264,46,264,53,263,54,263,56,262,57,262,59,261,60,261,61,259,63,259,64,254,69,252,69,251,70,241,70,240,69,240,66,239,65,236,65,235,66,235,67,234,68,233,68,231,70,228,70,228,69,230,69,234,65,235,65,236,64,235,63,230,63,229,62,228,62,227,61,226,61,222,57,222,56,220,54,220,46,221,45,221,44,222,43,222,41,223,40,223,39,224,38,224,37,225,36,225,34,227,32,227,31,228,30,229,30,230,29", "poly");		
            areaList[1] = new ImageArea("tubule", "510,129,515,129,516,130,518,130,519,131,520,131,522,133,522,134,523,135,523,136,524,137,524,138,527,141,538,141,541,144,541,145,542,146,542,150,541,151,541,152,536,157,533,157,532,158,530,158,527,161,527,164,529,166,529,167,532,170,532,176,530,178,530,180,528,182,527,182,526,183,517,183,517,182,515,180,513,180,512,179,511,179,509,177,507,177,506,178,506,191,507,192,506,193,501,193,500,192,499,192,496,189,495,189,495,188,492,185,492,184,491,183,490,183,488,185,488,189,487,190,487,200,486,201,482,201,482,200,480,198,479,198,479,196,477,194,472,194,469,197,469,198,468,199,468,205,467,206,468,207,467,208,467,210,466,211,466,215,465,216,465,222,464,223,464,226,465,227,465,233,464,234,464,239,463,240,463,243,462,244,461,244,460,245,460,246,459,247,457,247,457,246,456,245,456,244,455,243,454,243,454,241,453,240,453,238,452,237,452,231,453,230,453,229,454,228,454,226,455,225,455,220,456,219,456,215,457,214,457,210,458,209,458,204,459,203,459,199,460,198,460,196,461,195,461,190,462,189,462,186,464,184,471,184,472,185,473,185,475,187,476,187,477,188,478,188,479,189,480,189,481,188,481,178,482,177,481,176,481,173,483,171,486,171,488,173,489,173,493,177,497,177,498,176,498,175,499,174,499,173,500,172,500,170,501,169,501,168,503,166,504,167,507,167,510,170,511,170,513,172,515,172,517,174,518,174,521,171,521,167,517,163,517,157,518,156,519,156,520,155,522,155,523,154,524,154,525,153,526,153,527,152,528,152,531,149,530,148,520,148,519,147,519,146,518,145,518,143,515,140,513,140,509,144,505,144,504,143,504,138,505,137,505,135,506,134,506,133,510,129", "poly");	
            areaList[2] = new ImageArea("tubule", "315,77,323,77,324,78,325,78,326,79,327,79,327,80,328,81,328,82,327,83,327,86,326,87,326,89,325,90,325,91,324,92,324,93,321,96,320,96,319,97,317,97,316,98,314,98,313,99,306,99,305,98,304,98,303,97,301,97,300,96,299,96,298,95,296,95,295,94,292,94,291,95,291,98,290,99,290,101,289,102,289,103,288,103,286,105,286,106,284,108,281,108,280,107,280,104,281,103,279,101,279,89,284,84,301,84,302,85,304,85,305,86,311,86,312,85,312,81,313,80,313,79,315,77", "poly");					
        }
		return areaList;
	}


	public void setAreaList(ImageArea[] areaList) {
		this.areaList = areaList;
	}
	
}

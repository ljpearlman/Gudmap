package gmerg.beans;

import gmerg.entities.focus.components.model.ImageMap;
import gmerg.entities.submission.array.ArrayAnatomyCollection;
import gmerg.utils.CookieOperations;
import gmerg.utils.FacesUtil;

import java.util.ArrayList;

import javax.faces.context.FacesContext;
import javax.faces.model.ArrayDataModel;
import javax.faces.model.DataModel;

public class CollectionAnatomyBean {
    private boolean debug = false;

    protected ArrayAnatomyCollection[] collection;	
    String prefix = gmerg.utils.Utility.domainUrl+"Gudmap/arrayData";
	protected boolean collectionsAvailable;
	protected ArrayDataModel collectionModel;
    
    public CollectionAnatomyBean() {
	    if (debug)
		System.out.println("CollectionAnatomyBean.constructor");

    }

	public boolean getCollectionsAvailable() {

        String subIds = CookieOperations.getCookieValue("MicFocusEmapID");
        collectionsAvailable = true;
        if (!subIds.equals("")) {
            String[] subs = subIds.split("/");
            if (subs == null) {
                collectionsAvailable = false;
            }
        } else {
            collectionsAvailable = false;
        }

        return collectionsAvailable;
    }
	
    public DataModel getCollection() {

    	String addedId = null;
    	Object object = FacesUtil.getSessionValue("imageMap");
                            
        // This only works if myBean2 is request scoped.
        if (object != null) {
        	ImageMap myBean2 = (ImageMap) object; 
        	addedId = myBean2.getEmapId();
        }
    	//String addedId = (String)getFacesSessionValue("selectedEmapID");
    	if(null != addedId && addedId.equals(""))
    		addedId = null;

    	collection = null;
        String subIds = CookieOperations.getCookieValue("MicFocusEmapID");
        //look up the DB to find the organ detail and append this organ to the list
        subIds = appendAndRefreshCookieString(subIds, addedId);
        //System.out.println("After Cookie two:"+CookieOperations.getCookieValue("MicFocusEmapID", ctxt));
        if (subIds!=null && !subIds.equals("")) {
        	//System.out.println("After Append:"+subIds+":"+addedId);
            //note real code will put 'submissionIDs' into a method parameter for DBHelper (or other class) and used to get the correct result set back.
            String[] submissionIDs = subIds.split("/");
            collection = new ArrayAnatomyCollection[submissionIDs.length];
            for (int i = 0; i < submissionIDs.length; i++) {
                collection[i] = new ArrayAnatomyCollection();
                collection[i].setDownload(findDownloadAll(submissionIDs[i]));
                collection[i].setView(findViewAll(submissionIDs[i]));
                collection[i].setOrganName(submissionIDs[i]);
                collection[i].setSelected(false);
            }
        }   	
        collectionModel = new ArrayDataModel(collection);
        return collectionModel;
    }    

	public String findDownloadAll(String componentName) {
		if(null != componentName) {
			if(componentName.indexOf("kidney") > -1) {
				return prefix + "/potter/october_2006/potter_171006/data/GUDMAP_Potterlab_Pairwise_101706_1400.zip";
			} else if(componentName.indexOf("S-shaped bodies") > -1) {
				return prefix + "/potter/october_2006/potter_171006/data/GUDMAP_Potterlab_Pairwise_101706_1400.zip";
			} else if(componentName.indexOf("Proximal tubules") > -1) {
				return prefix + "/potter/october_2006/potter_171006/data/GUDMAP_Potterlab_Pairwise_101706_1400.zip";
			} else if(componentName.indexOf("testes") > -1) {
				return prefix + "/ciit/november_2006/ciit_031106/data/all_present_10_06.1.xls";
			} else if(componentName.indexOf("ovary") > -1) {
				return prefix + "/ciit/november_2006/ciit_031106/data/all_present_10_06.1.xls";
			} else if(componentName.indexOf("epididymis") > -1) {
				return prefix + "/ciit/november_2006/ciit_031106/data/all_present_10_06.1.xls";
			} else if(componentName.indexOf("bladder") > -1) {
				return prefix + "/cchmc/october_2006/";
			}
		}
		return "";
	}

	public String findViewAll(String componentName) {
		if(null != componentName) {
			if(componentName.indexOf("kidney") > -1) {
				return "16";//lab
			} else if(componentName.indexOf("S-shaped bodies") > -1) {
				return "16";
			} else if(componentName.indexOf("Proximal tubules") > -1) {
				return "16";
			}
		}
		return "";		
	}    
    
    public String appendAndRefreshCookieString(String subIds, String addedId){
    	if(null != addedId && !addedId.equals("")) {
	    	if(!subIds.equals("")) {
	    		String[] submissionIDs = subIds.split("/");
	    		boolean found = false;
	    		for (int i = 0; i < submissionIDs.length; i++) {
	    			if(submissionIDs[i].equalsIgnoreCase(addedId)) {
	    				found = true;
	    			}
	            }
	    		if(false == found)
	    			subIds = subIds + "/" + addedId;
	    	} else {
	    		subIds = addedId;
	    	}
    	}
    	return subIds;
    }
    
    public String [] getSelectedSubmissions(){
    	ArrayAnatomyCollection [] currentSubs = (ArrayAnatomyCollection []) ((ArrayDataModel)FacesUtil.getSessionValue("MicFocusEmapID")).getWrappedData();
        ArrayList<String> subs = new ArrayList<String>(); 
//        String[] emapIds = null;
        for (int i=0; i< currentSubs.length; i++){
            if(currentSubs[i].isSelected()){
            	subs.add(currentSubs[i].getOrganName());
            	/*emapIds = findEMAPIDs(currentSubs[i].getOrganName());
            	if(null != emapIds) {
            		for(int j = 0; j < emapIds.length; j++) {
            			subs.add(emapIds[j]);
            		}
            	}*/               	
            }
        }        
        String[] selectedSubs = new String[subs.size()];       
        for (int i = 0; i < selectedSubs.length; i++) {
            selectedSubs[i] = (String)subs.get(i);
        }
        return selectedSubs;
    }        

    String[] findEMAPIDs(String organName) {
    	return new String[]{"EMAP:1", "EMAP:2", "EMAP:3"};
    }

    public void addToCollection(String organName) {       
    	if(null != organName) {
	        //send the values to 'CookieOperations' where they will be stored as cookies
	        CookieOperations.setValuesInCookie("MicFocusEmapID", new String[]{organName});
    	}
        
    }    
    
    public String addToCollection() {
        
        String[] selectedSubs = getSelectedSubmissions();
        
        //send the values to 'CookieOperations' where they will be stored as cookies
        CookieOperations.setValuesInCookie("MicFocusEmapID", selectedSubs);

        return goToCollection();
    }
    
    public String replaceCollection(){
        String [] selectedSubs = getSelectedSubmissions();
        
        //send the values to 'CookieOperations' where they will be stored as cookies
        CookieOperations.replaceCookieValues("MicFocusEmapID", selectedSubs);
        
        return goToCollection();
    }    
    
    public String emptyCollection() {
        String subId = "MicFocusEmapID";
        CookieOperations.removeAllValuesFromCookie(subId);
        return "viewCollection";

    }

    public String deleteFromCollection() {
    	ArrayAnatomyCollection[] currentSubs =
            (ArrayAnatomyCollection[])collectionModel.getWrappedData();
        
        ArrayList<String> subs = new ArrayList<String>();
        for (int i = 0; i < currentSubs.length; i++) {
            if (currentSubs[i].isSelected()) {
                subs.add(currentSubs[i].getOrganName());
            }
        }

        String[] selectedSubs = new String[subs.size()];

        for (int i = 0; i < selectedSubs.length; i++) {
            selectedSubs[i] = (String)subs.get(i);
            //System.out.println("YINGYING:"+selectedSubs[i]);
        }

        if (selectedSubs != null && selectedSubs.length > 0) {

            //send the values to 'CookieOperations' where they will be stored as cookies
            CookieOperations.removeSelectedValuesFromCookie("MicFocusEmapID", selectedSubs);
        } else {
            //create faces message for display in browser
        }
        return "viewCollection";
    }    

    public String goToCollection() {
        return "viewCollection";
    }    
    
    public boolean isCollectionsAvailable() {
        String subIds = CookieOperations.getCookieValue("submissionID");
        collectionsAvailable = true;
        if (subIds != null && !subIds.equals("")) {
            String[] subs = subIds.split("/");
            if (subs == null) {
                collectionsAvailable = false;
            }
        } else {
            collectionsAvailable = false;
        }

        return collectionsAvailable;
    }
    

}

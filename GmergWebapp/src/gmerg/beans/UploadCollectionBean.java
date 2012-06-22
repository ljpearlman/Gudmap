package gmerg.beans;

import gmerg.entities.Globals;
import gmerg.utils.FacesUtil;
import gmerg.utils.Utility;
import gmerg.utils.table.CollectionBrowseHelper;
import gmerg.utils.table.GenericTableView;
import gmerg.utils.table.TableUtil;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Vector;
import org.apache.myfaces.custom.fileupload.UploadedFile;

public class UploadCollectionBean {
    private boolean debug = false;

	//added by ying
	private UploadedFile myFile;
	
	// ********************************************************************************
	// Constructors & Initializers
	// ********************************************************************************
	public UploadCollectionBean() {
	if (debug)
	    System.out.println("UploadCollectionBean.constructor");

		String viewName = "uploadCollection";
		
		if (TableUtil.isTableViewInSession())
			return;
		ArrayList<String> query = (ArrayList<String>)FacesUtil.getSessionValue("uploadSubmissionID");
        TableUtil.saveTableView(populateCollectionTableView(viewName, query));
        	
	}	

	// ********************************************************************************
	// Getters & Setters
	// ********************************************************************************
	public boolean getDumy() {
        
		return false;
	}
	
	/***************************************************************************************************************************************
	/***************************************************************************************************************************************
	/* Collection																								
	/***************************************************************************************************************************************
	/***************************************************************************************************************************************/
	private GenericTableView populateCollectionTableView(String viewName, ArrayList<String> list) {
		
		CollectionBrowseHelper helper = Globals.getCollectionBrowseHelper(list, 0);	// Entry Ids
	    // Create a collection GenericTableView object for collection data
		GenericTableView tableView = helper.getCollectionTableView(viewName);
		tableView.setRowsSelectable();
		tableView.setCollectionBottons(1);
		tableView.addCollection(0, 0);
		tableView.setDisplayTotals(true);
                
		if(Utility.getProject().equals("GUDMAP"))
			tableView.setColVisible(new boolean[]{ true, true, true, false, false, false,  true, false, true, false, false, false, true,
													true,  false, true, true});
		else 
			tableView.setColVisible(new boolean[]{ true, true, true, false,  true, false, false, true, false, true, true, true, true});

		tableView.setDynamicColumnsLimits(5, 9);
	    
		tableView.setNoDataMessage("Your Uploaded Collection is Empty.");
		return tableView;
	}
	
	
	public String uploadToCollection() {
		String[] list = null;
		ResourceBundle bundle = ResourceBundle.getBundle("configuration");
	    try {   
			if(myFile.getName().indexOf("csv") > -1 || 
					myFile.getName().indexOf("txt") > -1 ||
					myFile.getName().indexOf("TXT") > -1 ||
					myFile.getName().indexOf("CSV") > -1) {
	    		InputStream in = new BufferedInputStream(
	    		myFile.getInputStream());
	    		
	    		InputStreamReader inReader = new InputStreamReader(in);
	            BufferedReader bReader = new BufferedReader(inReader,
	            		in.available());
	            String str = "";
	            String oneLine = "";
	            
	            Vector<String> vec = new Vector<String>();
	
	            while (bReader.ready()) {
	            	oneLine = bReader.readLine();
	            	vec.add(oneLine);
	                str += oneLine;
	            }
	            
	            System.out.println("STR:"+str);
	            
	            if(vec.size() > 1) {
	            	list = changeArray(vec.toArray());                	
	            } else {
	        		list = str.split("\t");                		
	        	}
	            if(null != list && list.length > 20) {
	            	String[] newList = new String[20];
	            	for(int i = 0; i < newList.length; i++) {
	            		newList[i] = list[i];
	            	}
	            	list = null;
	            	list = newList;
	            }
	            in.close();
	            inReader.close();
	            bReader.close();
			}
	     } catch (Exception ex) {
	         ex.printStackTrace();
	     } 
	     ArrayList<String> idArray = new ArrayList<String>();
		 if(null != list) {
			 for(int i = 0; i < list.length; i++) {
				 if(null != list[i] && list[i].matches(bundle.getString("submission_id_prefix")+"\\d+"))
					 idArray.add(list[i]);
			 }
		 }
	     FacesUtil.setSessionValue("uploadSubmissionID", idArray);
		//String[] selectedSubs = list;
		
		//FacesContext context = FacesContext.getCurrentInstance();
		//send the values to 'CookieOperations' where they will be stored as cookies
		//CookieOperations.replaceCookieValues("uploadSubmissionID", selectedSubs, context);

		// This is to sent new selections to be used for populating collections table since cookies are not updated until 
		// the next request from client and I want to avoid redirect
		

		return "Upload";

	}	
	
    public String[] changeArray(Object[] array) {
    	String[] strArray = null;
    	if(null != array) {
    		strArray = new String[array.length];
    		for(int i = 0; i < array.length; i++) {
    			strArray[i] = (String)array[i];
    		}
    	} 
    	return strArray;
    }

	public UploadedFile getMyFile() {
		return myFile;
	}

	public void setMyFile(UploadedFile myFile) {
		this.myFile = myFile;
	}  
}

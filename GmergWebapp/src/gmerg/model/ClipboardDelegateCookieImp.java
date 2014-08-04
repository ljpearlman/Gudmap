package gmerg.model;

import java.util.*;

import gmerg.entities.Globals;
import gmerg.utils.CookieOperations;
import gmerg.utils.Utility;
import gmerg.utils.table.CollectionBrowseHelper;

/**
 * Implements methods for managing a clipboard based on cookies 
 * 
 * @author Mehran Sharghi
 *
 *
*/

public class ClipboardDelegateCookieImp {
    protected static boolean debug = false;

	public ClipboardDelegateCookieImp() {
	}	

	public static int getClicpboardSize(int collectionCategory) {
		return getClicpboardIds(collectionCategory).size();
	}
	    
	public static ArrayList<String> getClicpboardIds(int collectionCategory) {
		ArrayList<String> clipboardIds = new ArrayList<String>();
		String cookieName = getCookieName(collectionCategory);
	    String clipboardCookeiIds = CookieOperations.getCookieValue(cookieName);
		CookieOperations.getCookieValue(cookieName);
	    if (clipboardCookeiIds != null && !"".equals(clipboardCookeiIds) ) {  
	    	String[] ids = clipboardCookeiIds.split("/");
	    	for(int i=0; i<ids.length; i++)
	    		clipboardIds.add(ids[i]); 
	    }	
		ArrayList<String> validId = getValidIds(clipboardIds, collectionCategory);
		return (validId == null)? new ArrayList<String>() : validId;
	}
	
	/**
	 * <p>xingjun  - 05/10/2010 - added code to check if the param ids is null</p>
	 */
	public static int addToClicpboard(String[] ids, int collectionCategory) {
//		int currentClipboardSize = getClicpboardSize(collectionCategory);
		String cookieName = getCookieName(collectionCategory);
		boolean ignoreCase = ! Globals.getCollectionCategory(collectionCategory).isCaseSensitiveIds();
		// xingjun - 05/10/2010 - if users added empty gene symbols into the clipboard empty ids will be passed in and cause NullPointerException
		// so check if the passed-in Ids is null first
//		return getValidIds(CookieOperations.setValuesInCookie(cookieName, ids, ignoreCase), collectionCategory).size();
		ArrayList validIds = getValidIds(CookieOperations.setValuesInCookie(cookieName, ids, ignoreCase), collectionCategory);
		if(debug)
				System.out.println("ClipboardDelegateCookieImp::addToClicpboard | cookiename="+cookieName+" | ignoreCase="+ignoreCase+
						" | collectionCategory="+collectionCategory);
		//return 4;
		return (validIds == null)? 0: validIds.size();
	}
	
	/**
	 *  xingjun - 28/09/2010 - need to check if validIds is null
	 */
	public static int replaceClicpboard(String[] ids, int collectionCategory) {
		ArrayList<String> validIds = getValidIds(Utility.toArrayList(ids), collectionCategory);
		String cookieName = getCookieName(collectionCategory);
		String[] newIds = null;
		if (validIds!= null && validIds.size() > 0) {	
			newIds = new String[validIds.size()];
			newIds = validIds.toArray(newIds);
		}
		
		boolean ignoreCase = ! Globals.getCollectionCategory(collectionCategory).isCaseSensitiveIds();
		CookieOperations.replaceCookieValues(cookieName, newIds, ignoreCase);
//		return validIds.size();
		return (validIds == null)? 0: validIds.size();
	}
	
	public static void removeAllValuesFromClipboard(int collectionCategory) {
		String cookieName = getCookieName(collectionCategory);
		CookieOperations.removeAllValuesFromCookie(cookieName);
	}
	
	public static ArrayList<String> removeValuesFromClipboard(int collectionCategory, String[] ids) {
		String cookieName = getCookieName(collectionCategory);
		boolean ignoreCase = ! Globals.getCollectionCategory(collectionCategory).isCaseSensitiveIds();
		return getValidIds(CookieOperations.removeSelectedValuesFromCookie(cookieName,	ids, ignoreCase), collectionCategory);
	}
	
	public static ArrayList<String> getResultIdsOperationWithClipboard (String operation, String[] newIds, int collectionCategory) {
	    if (debug)
		System.out.println("ClipboardDelegateCookieImp.getResultIdsOperationWithClipboard operation = "+operation+" collectionCategory = "+collectionCategory);

		int n1 = 0;
	    String[] clipboardIds = null;
		String cookieName = getCookieName(collectionCategory);
	    String clipboardCookeiIds = CookieOperations.getCookieValue(cookieName);
	    if (clipboardCookeiIds != null && !"".equals(clipboardCookeiIds) ) { 
	    	clipboardIds = clipboardCookeiIds.split("/");
	    	n1 = clipboardIds.length;
	    }
		ArrayList<String> operationResultIds = new ArrayList<String>();
		if ("union".equalsIgnoreCase(operation)) 
			for (int i=0; i<n1; i++) 
				operationResultIds.add(clipboardIds[i]);
		int n2 = (newIds==null)? 0 : newIds.length;
		boolean ignoreCase = !Globals.getCollectionCategory(1).isCaseSensitiveIds();
		for (int i=0; i<n2; i++) {			
			if (Utility.stringArraySearch(clipboardIds, newIds[i], ignoreCase)<0 ) {
				if ("difference".equalsIgnoreCase(operation) || "union".equalsIgnoreCase(operation)) 
					operationResultIds.add(newIds[i]);
			}
			else 
				if ("intersection".equalsIgnoreCase(operation)) 
					operationResultIds.add(newIds[i]);
		}
		return getValidIds(operationResultIds, collectionCategory);
	}
	
	private static String getCookieName(int collectionCategory) {
		String clipboardName = Globals.getCollectionCategoryName(collectionCategory);
		if(debug)
				System.out.println("ClipboardDelegateCookieImp::getCookieName | clipboardName= "+clipboardName);
		return clipboardName + "CollectionGudmap";
	}
	
	private static ArrayList<String> getValidIds(ArrayList<String> ids, int collectionCategory) {
		if(debug)
			System.out.println("ClipboardDelegateCookieImp::getValidIds | id.length="+ids.size()+" | collectionCategory="+collectionCategory);
		CollectionBrowseHelper helper = Globals.getCollectionBrowseHelper(ids, collectionCategory);
		if (ids!=null && ids.size()>0  && helper != null)
			return helper.getValidIds();
		else
			return null;
	}
	
}

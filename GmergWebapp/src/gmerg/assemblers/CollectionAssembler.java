/**
 * 
 */
package gmerg.assemblers;

import gmerg.db.CollectionDAO;
import gmerg.db.DBHelper;
import gmerg.db.MySQLDAOFactory;
import gmerg.entities.CollectionInfo;
import gmerg.entities.Globals;
import gmerg.utils.table.DataItem;
import gmerg.utils.table.OffMemoryTableAssembler;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Set;

/**
 * @author xingjun
 *
 */
	 
public class CollectionAssembler { //Singleton 
		   
	static private CollectionAssembler _instance = null;
    private boolean debug = false;

	protected CollectionAssembler() {
	if (debug)
	    System.out.println("CollectionAssembler.constructor");

	}

	/**
	 * @return The unique instance of this class.
	 */
	static public CollectionAssembler instance() {
		if (null == _instance) {
			_instance = new CollectionAssembler();
		}
		return _instance;
	}
	
	/**
	 * 
	 * @param collectionId
	 * @return
	 */
	public CollectionInfo getCollectionInfo(int collectionId) {

	    /** ---get data from dao---  */
	    // create a dao
	    Connection conn = DBHelper.getDBConnection();
	    CollectionDAO collectionDAO = MySQLDAOFactory.getCollectionDAO(conn);
	    
	    // retrieve data from database & get intersection of ids
	    CollectionInfo collectionInfo = collectionDAO.getCollectionInfoById(collectionId);

	    // release db resources
	    DBHelper.closeJDBCConnection(conn);
	    collectionDAO = null;

	    /** ---return the value object---  */
		return collectionInfo;
	}

	
	/**
	 * returns a list of submission/genes/query Ids depending on the collection type
	 * @author Mehran
	 * @param selectedCollectionIds
	 * 
	 */
	public ArrayList<String> getUnionOfCollections(String[] selectedCollectionIds, int collectionType, boolean ignoreCase) {
		Hashtable <String, String> result = new Hashtable<String, String>();
		
		/** ---get data from dao---  */
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		CollectionDAO collectionDAO = MySQLDAOFactory.getCollectionDAO(conn);
		
		// retrieve data from database & get union of the ids
		int size = (selectedCollectionIds==null)? 0 : selectedCollectionIds.length;
		for (int i=0; i<size; i++) {
			ArrayList<String> collectionItemsIds = collectionDAO.getCollectionItemsById(Integer.parseInt(selectedCollectionIds[i]));
			for (int j=0; j<collectionItemsIds.size(); j++) { 
				String value = collectionItemsIds.get(j);
				String key = ignoreCase? value.toUpperCase() : value;
				result.put(key, value);
			}
		}
		// release db resources
		DBHelper.closeJDBCConnection(conn);
		collectionDAO = null;
		
		/** ---return the value object---  */
		return new ArrayList<String>(result.values());
	}
	
	/**
	 * returns a list of submission/genes/query Ids depending on the collection type that represents collection1-collection2
	 * @author Mehran
	 * 
	 */
	public ArrayList<String> getDifferenceOfCollections(String[] selectedCollectionIds, int collectionType, int attributeCol, int idCol, String opeartion, boolean ignoreCase) {
		Hashtable <String, String> result = new Hashtable<String, String>();
		
		/** ---get data from dao---  */
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		CollectionDAO collectionDAO = MySQLDAOFactory.getCollectionDAO(conn);
		
		// retrieve data from database & get union of the ids
		if (selectedCollectionIds == null)
			return new ArrayList<String>();

		ArrayList<String> collectionItemsIds1 = collectionDAO.getCollectionItemsById(Integer.parseInt(selectedCollectionIds[0]));
		if (selectedCollectionIds.length < 2) {
			// release db resources
			DBHelper.closeJDBCConnection(conn);
			collectionDAO = null;
			if (opeartion.endsWith("2")) // A-B or B-A
				return new ArrayList<String>();
			else
				return collectionItemsIds1;
		}
		ArrayList<String> collectionItemsIds2 = collectionDAO.getCollectionItemsById(Integer.parseInt(selectedCollectionIds[1]));
		if (opeartion.endsWith("2")) {
			ArrayList<String> temp = collectionItemsIds1;
			collectionItemsIds1 = collectionItemsIds2;
			collectionItemsIds2 = temp;
		}
		
		if (attributeCol>=0 && attributeCol!=idCol) {	// attribut column is present
			OffMemoryTableAssembler assembler = Globals.getCollectionBrowseHelper(collectionItemsIds1, collectionType).getCollectionBrowseAssembler();
			DataItem[][] collectionItems1 = assembler.retrieveData(-1, true, 0, Integer.MAX_VALUE);
			assembler = Globals.getCollectionBrowseHelper(collectionItemsIds2, collectionType).getCollectionBrowseAssembler();
			DataItem[][] collectionItems2 = assembler.retrieveData(-1, true, 0, Integer.MAX_VALUE);
			
			collectionItemsIds2.clear();
			for (int j=0; j<collectionItems2.length; j++) {
				String value = collectionItems2[j][attributeCol].getValue().toString();
				collectionItemsIds2.add(value);
			}
			
			for (int j=0; j<collectionItems1.length; j++) {
				String value = collectionItems1[j][attributeCol].getValue().toString();
				if (! (!ignoreCase && collectionItemsIds2.contains(value) ||
						ignoreCase && arraylistContainsValueIgnoreCase(collectionItemsIds2, value))) {
					String value1 = collectionItems1[j][idCol].getValue().toString();
					result.put(value1, value1);
				}
			}
		}
		else {		//No attribut column is present
			for (String value: collectionItemsIds1) {
				if (! (!ignoreCase && collectionItemsIds2.contains(value) || 
						ignoreCase && arraylistContainsValueIgnoreCase(collectionItemsIds2, value)) )
					result.put(value, value);
			}
		}
		
		// release db resources
		DBHelper.closeJDBCConnection(conn);
		collectionDAO = null;
		
		/** ---return the value object---  */
		return new ArrayList<String>(result.values());
	}
	
	public ArrayList<String> getIntersectionOfCollections(String[] selectedCollectionIds, int collectionType, int attributeCol, int idCol, boolean ignoreCase) {
		return 	getIntersectionOfCollections(selectedCollectionIds, 0, collectionType, attributeCol, idCol, ignoreCase );
	}
	
	public ArrayList<String> getIntersectionOfCollections(String[] selectedCollectionIds, int numSelected, int collectionType, int attributeCol, int idCol, boolean ignoreCase) { // numSelected specifies how many of selected values to be used
		if (selectedCollectionIds == null || selectedCollectionIds.length < 2)
			return new ArrayList<String>();
		int numSelectedToBeUsed = (numSelected == 0)? selectedCollectionIds.length : Math.min(numSelected, selectedCollectionIds.length);
		Hashtable <String, String> result = new Hashtable<String, String>();
		Hashtable <String, String> result1 = new Hashtable<String, String>();
		/** ---get data from dao---  */
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		CollectionDAO collectionDAO = MySQLDAOFactory.getCollectionDAO(conn);
		
		// retrieve data from database & get union of the ids
		for (int i=0; i<numSelectedToBeUsed; i++) { 	
			ArrayList<String> collectionItemsIds = collectionDAO.getCollectionItemsById(Integer.parseInt(selectedCollectionIds[i]));
			if (numSelectedToBeUsed < 2) {
				// release db resources
				DBHelper.closeJDBCConnection(conn);
				collectionDAO = null;
				return collectionItemsIds;
			}
			
			if (attributeCol>=0 && attributeCol!=idCol) {	// attribut column is present
				OffMemoryTableAssembler assembler = Globals.getCollectionBrowseHelper(collectionItemsIds, collectionType).getCollectionBrowseAssembler();
				DataItem[][] collectionItems = assembler.retrieveData(-1, true, 0, Integer.MAX_VALUE);
				for (int j=0; j<collectionItems.length; j++) {
					String value = collectionItems[j][attributeCol].getValue().toString();
					String key = collectionItems[j][idCol].getValue().toString();
					if (i==0)
						result.put(key, value);
					else {
						Set<String> keySet = result.keySet();
						for(String k: keySet) 
							if (value.equals(result.get(k))) {
								result1.put(k, value);
								if (! (!ignoreCase && key.equals(k) || 
										ignoreCase && key.equalsIgnoreCase(k)))		// It shouldn't break the loop because there might be more than one item with the same value but different key
									result1.put(key, value);	
							}
					}
				}
			}
			else {		//No attribut column is present
				for (int j=0; j<collectionItemsIds.size(); j++) {
					String value = collectionItemsIds.get(j);
					if (i==0)
						result.put(value, value);
					else
						if (!ignoreCase && result.containsKey(value) || 
							 ignoreCase && hashtableContainsValueIgnoreCase(result, value) )
							result1.put(value, value);
				}
			}
			if (i>0) {
				Hashtable <String, String> temp = result;
				result = result1;
				result1 = temp;
				result1.clear();
			}
		}

		// release db resources
		DBHelper.closeJDBCConnection(conn);
		collectionDAO = null;
		
		/** ---return the value object---  */
		return new ArrayList<String>(result.keySet());
	}

	/**
	 * returns a list of submission/genes/query Ids depending on the collection type
	 * @author xingjun
	 * @param collectionId
	 * 
	 */
	public ArrayList<String> getCollectionItems(int collectionId) {
		
		/** ---get data from dao---  */
        // create a dao
        Connection conn = DBHelper.getDBConnection();
        CollectionDAO collectionDAO = MySQLDAOFactory.getCollectionDAO(conn);

        // get data from database
        ArrayList<String> collectionItems =
        	collectionDAO.getCollectionItemsById(collectionId);

        // release db resources
        DBHelper.closeJDBCConnection(conn);
        collectionDAO = null;

        /** ---return the value object---  */
        return collectionItems;
	}
		
	public String[] getCollectionNames(String[] collectionIds) {
		// validate the id list
		if (collectionIds == null || collectionIds.length == 0) {
			return null;
		}
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		CollectionDAO collectionDAO = MySQLDAOFactory.getCollectionDAO(conn);
		// retrieve data from database
		int len = collectionIds.length;
		String[] collectionNames = new String[len];
		for (int i = 0; i < len; i++) {
			CollectionInfo collectionInfo = collectionDAO
					.getCollectionInfoById(Integer.parseInt(collectionIds[i]));
			if (collectionInfo != null) {
				collectionNames[i] = collectionInfo.getName();
			}
		}

		// release db resources
		DBHelper.closeJDBCConnection(conn);
		collectionDAO = null;

		/** ---return the value object--- */
		return collectionNames;
	}

	// /////////////////////////////////
	public int removeCollections(String[] collectionIds) {
		// validate the id list
		if (collectionIds == null || collectionIds.length == 0) {
			return 0;
		}
		int deletedCollectionNumber = 0;

		// create a dao
		Connection conn = DBHelper.getDBConnection();
		CollectionDAO collectionDAO = MySQLDAOFactory.getCollectionDAO(conn);
		// make change to database
		for (int i = 0; i < collectionIds.length; i++) {
			deletedCollectionNumber +=
				collectionDAO.deleteCollectionById(Integer.parseInt(collectionIds[i]));
		}

		// release db resources
		DBHelper.closeJDBCConnection(conn);
		collectionDAO = null;

		// return
		return deletedCollectionNumber;
	}
	
	public String removeCollections(String[] collectionIds, int userId) {
		// xingjun - 08/09/2010 - changed the wording of the text returned
		if (collectionIds == null || collectionIds.length == 0) {
			return "There is no collection selected";
		}
		int deletedCollectionNumber = 0;
		
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		CollectionDAO collectionDAO = MySQLDAOFactory.getCollectionDAO(conn);

		int len = collectionIds.length;
		ArrayList<String> candidateIds = new ArrayList<String>();
		for (int i = 0; i < len; i++) {
			CollectionInfo collectionInfo = collectionDAO
					.getCollectionInfoById(Integer.parseInt(collectionIds[i]));
			if (collectionInfo != null && collectionInfo.getOwner() == userId) {
				candidateIds.add(Integer.toString(collectionInfo.getId()));
			}
		}
		int ownCollectionNumber = candidateIds.size();
		if (ownCollectionNumber == 0) {
			return "Cannot remove collections belonging to others";
		}
		
		// make change to database
		for (int i = 0; i < ownCollectionNumber; i++) {
			deletedCollectionNumber +=
				collectionDAO.deleteCollectionById(Integer.parseInt(candidateIds.get(i)));
		}

		// release db resources
		DBHelper.closeJDBCConnection(conn);
		collectionDAO = null;

		if (deletedCollectionNumber < ownCollectionNumber) {
			return "Cannot remove all collections";
		} else if (deletedCollectionNumber == ownCollectionNumber && deletedCollectionNumber < len) {
			return "User's own collection has been removed. Cannot remove collections belonging to others";
		}
		return null;
	}
	
	public String insertCollection(CollectionInfo collectionInfo, ArrayList<String> items) {
		return insertCollection(collectionInfo, items, false);
	}
	
	// the combination of collection name and owner shouldn't be duplicated
	// modified by xingjun - 30/06/2008
	public String insertCollection(CollectionInfo collectionInfo, ArrayList<String> items, boolean overwrite) {
		if (collectionInfo == null) 
			return "Details for collection should be provided";
		
		if (!collectionInfoValueAssigned(collectionInfo)) {
			return "Details for collection should be provided";
		}
		
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		CollectionDAO collectionDAO = MySQLDAOFactory.getCollectionDAO(conn);
		int insertedRecordNumber = 0;
		
		// find out if the would be inserted collection exists
		CollectionInfo overwriteCollectionInfo =
			collectionDAO.getCollectionInfoByName(collectionInfo.getName(), collectionInfo.getOwner());
		if (overwrite) {
			if (overwriteCollectionInfo != null) { // duplication exists, need to delete and then insert
				int removedCollectionNumber =
					collectionDAO.deleteCollectionById(overwriteCollectionInfo.getId());
				if (removedCollectionNumber > 0) {
					// insert into database
					insertedRecordNumber = 
						collectionDAO.insertCollection(collectionInfo, items);
					if (insertedRecordNumber == 0) {
						return "Failed to insert collection into the database";
					}
				} else {
					return "Failed to delete duplicated collection(s). Can not overwrite";
				}
			} else { // no duplication. insert directly
				// insert into database
				insertedRecordNumber = 
					collectionDAO.insertCollection(collectionInfo, items);
				if (insertedRecordNumber == 0) {
					return "Failed to insert collection into the database";
				}
			}
		} else {
			if (overwriteCollectionInfo != null) { // duplication exists, cannot insert
				return "Can not insert collection(s) due to the duplication";
			} else { // no duplication and insert
				// insert into database
				insertedRecordNumber = 
					collectionDAO.insertCollection(collectionInfo, items);
				if (insertedRecordNumber == 0) {
					return "Failed to insert collection into the database";
				}
			}
		}
		// release db resources
		DBHelper.closeJDBCConnection(conn);
		collectionDAO = null;

		// return 
		return (insertedRecordNumber==items.size())? null : "Failed to insert all ids into the database";
	}
	
	private boolean collectionInfoValueAssigned(CollectionInfo collectionInfo) {
		if (collectionInfo.getName() == null || collectionInfo.getName().equals("")) {
			return false;
		}
		if (collectionInfo.getOwner() == -1) {
			return false;
		}
		if (collectionInfo.getType() == -1) {
			return false;
		}
		if (collectionInfo.getStatus() == -1) {
			return false;
		}
		if (collectionInfo.getFocusGroup() == -1) {
			return false;
		}
		return true;
	}

	/**
	 * @author xingjun - 04/06/2008
	 * 
	 * @param collectionInfo
	 * @return
	 */
	public int updateCollectionSummary(CollectionInfo collectionInfo) {
		return updateCollectionSummary(collectionInfo, false);
	}
	
	// modified by xingjun - 30/06/2008
	public int updateCollectionSummary(CollectionInfo collectionInfo, boolean overwrite) {
	// if overwite is true it means that if there is collection with similar name then insert a new one. note that combination of name+owener is unique 
		if (collectionInfo == null) 
			return -1; // Details for collection should be provided
		
		if (!collectionInfoValueAssigned(collectionInfo)) 
			return -1;
		
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		CollectionDAO collectionDAO = MySQLDAOFactory.getCollectionDAO(conn);
		int updatedRecordNumber = 0;
		
		// find out if the would be inserted collection exists
		CollectionInfo overwriteCollectionInfo = collectionDAO.getCollectionInfoByName(collectionInfo.getName(), collectionInfo.getOwner());
		if (overwrite) {
			if (overwriteCollectionInfo != null) { // found one with simmilar name - remove it before inserting the new one 
				// delete the duplicated one in the database
				int removedCollectionNumber = collectionDAO.deleteCollectionById(overwriteCollectionInfo.getId());
				if (removedCollectionNumber == 0) {
					return -2; // failed to remove duplicate collection in database
				} else {
					// update
					updatedRecordNumber = collectionDAO.updateCollectionSummary(collectionInfo);
					if (updatedRecordNumber == 0) {
						return -3; // failed to update collection summary
					}
				}
			}
			else { // no other with simillar name - update
				updatedRecordNumber = collectionDAO.updateCollectionSummary(collectionInfo);
				if (updatedRecordNumber == 0) {
					return -3;
				}
			}
		} else {
			if (overwriteCollectionInfo!=null && overwriteCollectionInfo.getId()!=collectionInfo.getId()) { // duplication exists, cannot update
				return -4; // cannot update due to the duplication
			} else { // no duplication - update
				updatedRecordNumber = collectionDAO.updateCollectionSummary(collectionInfo);
				if (updatedRecordNumber == 0) {
					return -3; // failed to update collection summary
				}
			}
		}
		// release db resources
		DBHelper.closeJDBCConnection(conn);
		collectionDAO = null;

		// return
//		return updatedRecordNumber;
		return 0;
	} 	

	public CollectionInfo getCollectionInfo(String collectionName, int owner) {
		/** ---get data from dao--- */
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		CollectionDAO collectionDAO = MySQLDAOFactory.getCollectionDAO(conn);
		// retrieve data from database
		CollectionInfo collectionInfo = collectionDAO.getCollectionInfoByName(
				collectionName, owner);

		// release db resources
		DBHelper.closeJDBCConnection(conn);
		collectionDAO = null;

		/** ---return the value object--- */
		return collectionInfo;
	}

	private boolean hashtableContainsValueIgnoreCase(Hashtable<String, String> hashtable, String value) {
		Set<String> keySet = hashtable.keySet();
		for(String k: keySet) 
			if (value.equalsIgnoreCase(hashtable.get(k))) 
					return true;
		return false;
	}
	
	private boolean arraylistContainsValueIgnoreCase(ArrayList<String> arraylist, String value) {
		for (String item : arraylist)
			if (item.equalsIgnoreCase(value))
				return true;
		return false;
	}
}


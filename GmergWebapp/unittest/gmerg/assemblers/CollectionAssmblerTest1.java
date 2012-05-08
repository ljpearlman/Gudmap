package gmerg.assemblers;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.ArrayList;

import gmerg.assemblers.CollectionAssembler;
import gmerg.entities.CollectionInfo;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CollectionAssmblerTest1 {

	private CollectionAssembler ca = null;
	
	@Before
	public void setUp() throws Exception {
		HashMap<String, Object> params = new HashMap<String, Object>();
		ArrayList<String> ids = new ArrayList<String>();
		ids.add("1");
		params.put("collectionIds", ids);
//		ca = new CollectionAssembler(params);
		ca = new CollectionAssembler();
	}

	@After
	public void tearDown() throws Exception {
		ca = null;
	}

	@Test
//	public final void testGetCollectionInfo() {
//		fail("Not yet implemented"); // TODO
//		int collectionId = 3;
//		CollectionInfo collectionInfo = ca.getCollectionInfo(collectionId);
//		assertNotNull(collectionInfo);
//		assertEquals("DH_CLN_LOWER", collectionInfo.getName());
//		assertEquals(4, collectionInfo.getEntries());
//		assertEquals("derekh", collectionInfo.getOwnerName());
//		
//		int owner = 7;
//		String collectionName = "KL_CLN_GENE0";
//		collectionInfo = ca.getCollectionInfo(collectionName, owner);
//		assertNotNull(collectionInfo);
//		assertEquals(11, collectionInfo.getId());
//		assertEquals(2, collectionInfo.getEntries());
//		assertEquals("test collection_imb", collectionInfo.getDescription());
//	}
	
	public final void testGetCollectionItems() {
		int collectionId = 10;
//		ArrayList collectionItems = ca.getCollectionItems(collectionId);
//		assertNotNull(collectionItems);
//		assertEquals(3, collectionItems.size());
//		assertEquals("Zzz3", collectionItems.get(0).toString());
		collectionId = 17;
		ArrayList collectionItems = ca.getCollectionItems(collectionId);
		assertNotNull(collectionItems);
		assertEquals(6, collectionItems.size());
		assertEquals("GUDMAP:9683", collectionItems.get(0).toString());
	}
	
//	public final void testGetUnionOfCollections() {
//		String[] collectionIds = {"3", "4"};
//		ArrayList collectionItems = ca.getUnionOfCollections(collectionIds);
//		assertNotNull(collectionItems);
//		assertEquals(9, collectionItems.size());
//		assertEquals("GUDMAP:6458", collectionItems.get(0).toString());
		
//		String[] collectionIds = {"10", "11"};
//		ArrayList collectionItems = ca.getUnionOfCollections(collectionIds);
//		assertNotNull(collectionItems);
//		assertEquals(4, collectionItems.size());
//		assertEquals("Zyx", collectionItems.get(2).toString());

//		String[] collectionIds = {"2", "3", "4"};
//		ArrayList collectionItems = ca.getUnionOfCollections(collectionIds);
//		assertNotNull(collectionItems);
//		for (int i=0;i<collectionItems.size();i++) {
//			System.out.println("collectionItem:" + i + " " + collectionItems.get(i));
//		}
//		assertEquals(11, collectionItems.size());
//		assertEquals("Zyx", collectionItems.get(2).toString());
//	}
	
//	public final void testGetIntersectionOfCollections() {
//		String[] collectionIds = {"10", "11"};
//		ArrayList collectionItems = ca.getIntersectionOfCollections(collectionIds);
//		for (int i=0;i<collectionItems.size();i++) {
//			System.out.println("collectionItem:" + i + " " + collectionItems.get(i));
//		}
//		assertNotNull(collectionItems);
//		assertEquals(1, collectionItems.size());
//		assertEquals("Zyx", collectionItems.get(0).toString());

//		String[] collectionIds = {"2", "3"};
//		ArrayList collectionItems = ca.getIntersectionOfCollections(collectionIds);
//		assertNotNull(collectionItems);
//		assertEquals(1, collectionItems.size());
//		assertEquals("GUDMAP:8583", collectionItems.get(0).toString());
//	}
	
//	public final void testRemoveCollection() {
//		int collectionId = 14;
//		boolean collectionDeleted = ca.removeCollection(collectionId);
//		assertEquals(true, collectionDeleted);
//	}
	
//	public final void testGetCollectionNames() {
//		String[] collectionIds = {"1", "2", "3"};
//		String[] collectionNames = ca.getCollectionNames(collectionIds);
//		assertNotNull(collectionNames);
//		assertEquals("DH_CLN_MALE", collectionNames[0]);

//		String[] collectionIds = {"1"};
//		String[] collectionNames = ca.getCollectionNames(collectionIds);
//		assertNotNull(collectionNames);
//		assertEquals("DH_CLN_MALE", collectionNames[0]);
//	}
	
//	public final void testRemoveCollections() {
//		String[] collectionIds = {"13", "14"};
//		int deletedCollectionNumber =
//			ca.removeCollections(collectionIds);
//		assertEquals(1, deletedCollectionNumber);
//		
//	}
	
//	public final void testInsertCollection() {
//		CollectionInfo collectionInfo = new CollectionInfo();
//		collectionInfo.setName("derek_submission_01"); //name
//		collectionInfo.setOwner(1); //owner
//		collectionInfo.setType(0); //type
//		collectionInfo.setStatus(1); //status
//		collectionInfo.setDescription("testing for inserting collection OVERWRITE"); //description
//		collectionInfo.setFocusGroup(0); //focusGroup
//		collectionInfo.setLastUpdate("2008-07-25"); // last update
////		collectionInfo.setLastUpdate("Fri Jul 25 10:46:16 BST 2008"); // last update
//		
//		ArrayList<String> items = new ArrayList<String>();
//		items.add("GUDMAP:9910");
//		items.add("GUDMAP:9911");
////		items.add("GUDMAP:9917");
//		
////		String insertedRecord = ca.insertCollection(collectionInfo, items);
//		String insertedRecord = ca.insertCollection(collectionInfo, items, true);
//		assertEquals("", insertedRecord);
//	}
	
//	public final void testUpdateCollectionSummary() {
//		CollectionInfo collectionInfo = new CollectionInfo();
//		collectionInfo.setId(15); // id
//		collectionInfo.setName("derek_submission_01"); //name
//		collectionInfo.setStatus(0); //status
//		collectionInfo.setDescription("testing for inserting collection"); //description
//		collectionInfo.setFocusGroup(0); //focusGroup
//		int updatedRecordNumber = ca.updateCollectionSummary(collectionInfo);
//		assertEquals(1, updatedRecordNumber);
//	}
	
//	public final void testIntersectCollection() {
//		
//		ArrayList[] collectionItems = new ArrayList[3];
//		ArrayList<String> collectionItems1 = new ArrayList<String>();
//		collectionItems1.add("Aqp1");
//		collectionItems[0] = collectionItems1;
//		ArrayList<String> collectionItems2 = new ArrayList<String>();
//		collectionItems2.add("Aqp2");
//		collectionItems[0] = collectionItems2;
//		ArrayList<String> collectionItems3 = new ArrayList<String>();
//		collectionItems3.add("Aqp3");
//		collectionItems[0] = collectionItems3;
//
//		CollectionInfo collectionInfo = null;
//		String insertCollectionInfo = null;
		
		// test 1: insert 3 collections
//		collectionInfo = new CollectionInfo();
//		collectionInfo.setName("DH_T_1");
//		collectionInfo.setOwner(1);
//		collectionInfo.setType(1);
//		collectionInfo.setStatus(1);
//		collectionInfo.setDescription("test collection for insertion-dh1");
//		collectionInfo.setFocusGroup(0);
//		insertCollectionInfo = ca.insertCollection(collectionInfo, collectionItems1);
//		assertNull(insertCollectionInfo);
//		
//		collectionInfo = new CollectionInfo();
//		collectionInfo.setName("DH_T_2");
//		collectionInfo.setOwner(1);
//		collectionInfo.setType(1);
//		collectionInfo.setStatus(1);
//		collectionInfo.setDescription("test collection for insertion-dh2");
//		collectionInfo.setFocusGroup(0);
//		insertCollectionInfo = ca.insertCollection(collectionInfo, collectionItems2);
//		assertNull(insertCollectionInfo);
//
//		collectionInfo = new CollectionInfo();
//		collectionInfo.setName("KL_T_1");
//		collectionInfo.setOwner(7);
//		collectionInfo.setType(1);
//		collectionInfo.setStatus(1);
//		collectionInfo.setDescription("test collection for insertion-kl1");
//		collectionInfo.setFocusGroup(0);
//		insertCollectionInfo = ca.insertCollection(collectionInfo, collectionItems3);
//		assertNull(insertCollectionInfo);

		// test 2: try to insert a null collection or a collection without any assigned property
//		collectionInfo = null;
//		collectionInfo = new CollectionInfo();
//		insertCollectionInfo = ca.insertCollection(collectionInfo, collectionItems3);
//		assertNull(insertCollectionInfo);
//		assertEquals(insertCollectionInfo, "");
		
		// test 3: insert a collection which has the same name and owner as an existing collection
//		collectionInfo = new CollectionInfo();
//		collectionInfo.setName("KL_T_1");
//		collectionInfo.setOwner(7);
//		collectionInfo.setType(1);
//		collectionInfo.setStatus(1);
//		collectionInfo.setDescription("test collection for insertion-kl1");
//		collectionInfo.setFocusGroup(0);
//		insertCollectionInfo = ca.insertCollection(collectionInfo, collectionItems3);
//		assertNull(insertCollectionInfo);
//		assertEquals(insertCollectionInfo, "");
		
		// test 4: insert a collection to overwrite an existing collection
//		collectionInfo = new CollectionInfo();
//		collectionInfo.setName("DH_T_1");
//		collectionInfo.setOwner(1);
//		collectionInfo.setType(1);
//		collectionInfo.setStatus(0);
//		collectionInfo.setDescription("test collection for insertion-dh101");
//		collectionInfo.setFocusGroup(1);
//		insertCollectionInfo = ca.insertCollection(collectionInfo, collectionItems3, true);
//		assertNull(insertCollectionInfo);
//		assertEquals(insertCollectionInfo, "");
//	}
	
//	public final void testUpdateCollectionSummary() {
//		
//		CollectionInfo collectionInfo = null;
//		int updatedCollectionInfo = 0;
		
		// test 1: don't have a initialised collection
//		collectionInfo = new CollectionInfo();
//		updatedCollectionInfo = ca.updateCollectionSummary(collectionInfo);
//		assertEquals(updatedCollectionInfo, 3);
		
		// test 2.1: update collection, no name confliction
		//      2.2: update collection with name confliction, no overwrite
//		collectionInfo = new CollectionInfo();
//		collectionInfo.setId(29);
//		collectionInfo.setName("DH_TT_1");
//		collectionInfo.setOwner(1);
//		collectionInfo.setType(1);
//		collectionInfo.setStatus(0);
//		collectionInfo.setDescription("test collection for updating dh101");
//		collectionInfo.setFocusGroup(1);
//		updatedCollectionInfo = ca.updateCollectionSummary(collectionInfo);
//		assertEquals(updatedCollectionInfo, 3);
		
		// test 3: update collection with name confliction, overwrite
//		collectionInfo = new CollectionInfo();
//		collectionInfo.setId(29);
//		collectionInfo.setName("DH_T_2");
//		collectionInfo.setOwner(1);
//		collectionInfo.setType(1);
//		collectionInfo.setStatus(0);
//		collectionInfo.setDescription("test collection for updating dh102");
//		collectionInfo.setFocusGroup(1);
//		updatedCollectionInfo = ca.updateCollectionSummary(collectionInfo, true);
//		assertEquals(updatedCollectionInfo, 3);
//	}
	
	
//	@Test
//	public final void testRemoveCollections() {
		
		// test 1: remove own collection
//		String[] collectionIds = {"28"};
//		int owner = 1;
//		String collectionDeletingInfo = ca.removeCollections(collectionIds, owner);
//		assertNotNull(collectionDeletingInfo);
//		System.out.println("collectionDeletingInfo: " + collectionDeletingInfo);
		
		// test 2: remove collections belong to others
//		String[] collectionIds = {"1", "28"};
//		int owner = 7;
//		String collectionDeletingInfo = ca.removeCollections(collectionIds, owner);
//		assertNotNull(collectionDeletingInfo);
//		assertEquals(collectionDeletingInfo, "");
//		System.out.println("collectionDeletingInfo: " + collectionDeletingInfo);
		
		// test 3: remove collections belong to the owner and other people
//		String[] collectionIds = {"22", "26"};
//		int owner = 1;
//		String collectionDeletingInfo = ca.removeCollections(collectionIds, owner);
//		assertNotNull(collectionDeletingInfo);
//		assertEquals(collectionDeletingInfo, "");
//		System.out.println("collectionDeletingInfo: " + collectionDeletingInfo);
//	}

}

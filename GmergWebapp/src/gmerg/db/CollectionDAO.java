/**
 * 
 */
package gmerg.db;

import gmerg.entities.CollectionInfo;

import java.util.ArrayList;

/**
 * @author xingjun
 *
 */
public interface CollectionDAO {
	
	public ArrayList getCollections(int userId,int collectionType, int status,
			int column, boolean ascending, int offset, int num);
	
	public int getTotalNumberOfCollections(int userId, int collectionType, int status);
	
	public ArrayList<String> getCollectionItemsById(int collectionId);
	public ArrayList<String> getCollectionItemsByIds(String[] collectionIds);
	
	public CollectionInfo getCollectionInfoById(int collectionId);
	public CollectionInfo getCollectionInfoByName(String collectionName, int owner);
	
	public int deleteCollectionById(int collectionId);
    public int insertCollection(CollectionInfo collectionInfo, ArrayList<String> items);
    public int updateCollectionSummary(CollectionInfo collectionInfo);
    
    public int getPublicSubmissionNumberBySubmissionId(ArrayList submissionIds);
    public int checkSubmissionId(ArrayList submissionIds);
    
    public ArrayList<String> getInsituSubmissionImageIdByGene(String symbol);
}

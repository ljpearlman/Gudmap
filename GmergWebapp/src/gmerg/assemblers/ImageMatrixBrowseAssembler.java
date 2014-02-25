package gmerg.assemblers;

import gmerg.db.DBHelper;
import gmerg.db.GeneStripDAO;
import gmerg.db.MySQLDAOFactory;
import gmerg.entities.submission.ImageInfo;
import gmerg.utils.table.*;

import java.sql.Connection;
import java.util.HashMap;
import java.util.ArrayList;

public class ImageMatrixBrowseAssembler extends OffMemoryCollectionAssembler {
    private boolean debug = false;
    
    private ArrayList imageData = null;
    
    public ImageMatrixBrowseAssembler (HashMap<String,Object> params, CollectionBrowseHelper helper) {
		super(params, helper);
		if (debug)
		    System.out.println("ImageMatrixBrowseAssembler.constructor");	
    }
    
    public void setParams() {
		super.setParams();
		imageData = getData();
    }
    
    /**
     * @author xingjun - 27/11/2008
     */
    public DataItem[][] retrieveData(int column, boolean ascending, int offset, int num) {
		imageData = getData();
		
		if (imageData == null || imageData.size() == 0) {
		    return null;
		}
		int dataLength = getDataLength();
		int numRows = Math.min(num, dataLength-offset);
		int numCols = getStages().size();
		
		DataItem[][] dataItems = new DataItem[numRows][numCols];
		ArrayList imagesOfGivenStage =null;
		int iNumber = -1;
		ImageInfo imageInfoItem = null;
		int idx = 0;
		String submissionId = null;
		String stage = null;
		String serialNumber = null;
		
		for (int i=0; i<numRows; i++)  
		    for (int j=0, col=0; j<imageData.size(); j++) {
			
				imagesOfGivenStage = (ArrayList)imageData.get(j);
				iNumber = imagesOfGivenStage.size();
				imageInfoItem = null;
				idx = i+offset;
				if (idx >= iNumber)
				    dataItems[i][col] = new DataItem("");
				else {
				    imageInfoItem = (ImageInfo)imagesOfGivenStage.get(i+offset);
				    submissionId = imageInfoItem.getAccessionId();
				    stage = imageInfoItem.getStage();
				    serialNumber = imageInfoItem.getSerialNo();
		
				    if (debug) {
				    	System.out.println("submissionId=" +submissionId+" imageUrl = "+imageInfoItem.getFilePath()+" stage = "+stage+" clickUrl = "+imageInfoItem.getClickFilePath());
				    }
				    ArrayList<DataItem> complexValue = new ArrayList<DataItem>();
				    complexValue.add(new DataItem(submissionId, -1)); 	// Type=-1 means that the item will not display
				    complexValue.add(new DataItem(submissionId, submissionId, "ish_submission.html?id="+submissionId, 10)); // submission id
				    complexValue.add(new DataItem(stage,"", "http://www.emouseatlas.org/emap/ema/theiler_stages/StageDefinition/"+stage.toLowerCase()+"definition.html", 3));  // stage
				    DataItem zoomViewerItem = new DataItem(imageInfoItem.getFilePath(), "Click to open in the zoom viewer", imageInfoItem.getClickFilePath(), 14);
				    complexValue.add(zoomViewerItem);  // zoom viewer
				    dataItems[i][col] = new DataItem(complexValue, 80);
				}
				col++;
		    }
		if (debug)
		    System.out.println("dataItem size:row: " + dataItems.length + " col: " + dataItems[1].length);
		return dataItems; 
    }
    
    /**
     * @author xingjun - 16/02/2009
     * find out the max number of images at all stages
     * @return
     */
    private int getDataLength() {
		int dataLength = 0;
		int len = imageData.size();
		int imageNumbers = 0;
		for (Object list: imageData) {
		    imageNumbers = ((ArrayList)list).size();
		    if (imageNumbers > dataLength) {
			dataLength = imageNumbers;
		    }
		}
		return dataLength;
    }
    
    
    public int retrieveNumberOfRows() {
		if (debug)
		    System.out.println("=====ImageMatrixBrowseAssembler:retrieveNumberOfRows======");
		
		imageData = getData();
		if (imageData != null && imageData.size() != 0) {
		    return this.getDataLength();
		    
		}
		return 0;
    }
    
    /**
     * @return
     */
    public ArrayList<String> getStages() {
		ArrayList<String> stages = new ArrayList<String>();
		imageData = getData();
		if (imageData==null)
		    return stages;
		int len = imageData.size();
		String stage = null;
		for(Object imagesForGivenStage : imageData) {
		    stage = ((ImageInfo)((ArrayList)imagesForGivenStage).get(0)).getStage();
		    stages.add(stage);
		}
		return stages;
    }
    
    public HeaderItem[] createHeader() {
		ArrayList<String> stages = getStages();
		HeaderItem[] header = new HeaderItem[stages.size()];
		int i = 0;
		for(String stage: stages) {
		    header[i++] = new HeaderItem(stage, false);
		}
		return header;
    }    
    
    /****************************************************************************************
     * private methods
     * 
     ****************************************************************************************/
    /**
     * @param symbol
     * @return
     */
    
    private ArrayList getData() {
		if (debug)
		    System.out.println("=====ImageMatrixBrowseAssembler:getData======");
		if (imageData != null && !isAnyParameterChenged()) {
		    if (debug)
			System.out.println("---------->current Image data used");		
		    return imageData;
		}
		ArrayList rawImageInfo = null;
		ArrayList imageInfo = null;
		// if imageId is not null, get data by image id; or get data by gene
		if (ids != null && ids.size() > 0) {
		    if (debug)
			System.out.println("ImageMatrixBrowserAssembler:getData: ids is not null###ids size: " + ids.size());
		    rawImageInfo = getInsituSubmissionImagesByImageIds(ids);
		}
		
		if (debug)
		    System.out.println("image raw data size: " + rawImageInfo.size());
		// re-construct the result for display
		if (rawImageInfo != null && rawImageInfo.size() != 0) {
		    if (debug)
			System.out.println("no of images: " + rawImageInfo.size());
		    imageInfo = reconstructInsituSubmissionImageInfo(rawImageInfo, 0);
		}
		
		return imageInfo;
    }
    
    /**
     * @author xingjun - 17/02/2009
     * @param imageIds
     * @return
     */
    private ArrayList getInsituSubmissionImagesByImageIds(ArrayList<String> imageIds) {
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		try{
			GeneStripDAO geneStripDAO = MySQLDAOFactory.getGeneStripDAO(conn);
			ArrayList images = geneStripDAO.getInsituSubmissionImagesByImageId(imageIds);
			return images;
		}
		catch(Exception e){
			System.out.println("ImageMatrixBrowseAssembler::getInsituSubmissionImagesByImageIds !!!");
			return null;
		}
		finally{
		    DBHelper.closeJDBCConnection(conn);
		}
    }
    
    /**
     * @author xingjun - 13/02/2009
     * @param rawImageInfo
     * @param ii
     * @return
     */
    private ArrayList reconstructInsituSubmissionImageInfo(ArrayList rawImageInfo, int dummy) {
		if (rawImageInfo != null) {
		    int numberOfImages = rawImageInfo.size();
		    if (debug)
			System.out.println("reconstructInsituSubmissionImageInfo@raw image size: " + numberOfImages);
		    ArrayList<ArrayList> result = new ArrayList<ArrayList>();
		    int step = 0;
		    String tempStage = null;
		    ImageInfo id = null;
		    String stage = null;
		    for (int i=0;i<numberOfImages;i+=step) {
				step = 0;
				ArrayList imagesAtTheSameStage = new ArrayList();
				// go through the list and put images at the same stage into
				// different ArrayList objects and then put them together into
				// the final result ArrayList
				for (int j=i;j<numberOfImages;j++) {
				    id = (ImageInfo)rawImageInfo.get(j);
				    stage = id.getStage();
				    step++;
				    if (tempStage == null) { // first image of the stage
						tempStage = stage;
						imagesAtTheSameStage.add(id);
						if (debug)
						    System.out.println("empty tempStage, add image into stage" + j);
				    } 
				    else { // follow-up images
						/** image at different stage, move on to the next stage */
						if (!stage.equals(tempStage)) { 
						    result.add(imagesAtTheSameStage);
						    if (debug)
						    	System.out.println("nst:add stage into result" + j);
						    tempStage = null;
						    step--;
						    if (debug)
						    	System.out.println("step= " + step);
						    break;
						} 
						else { /** image at the same stage, add it */
						    imagesAtTheSameStage.add(id);
						    if (debug)
						    	System.out.println("non-empty tempStage, add image into stage" + j);
						}
				    }
				    if (j==(numberOfImages-1)) { /** last image */
					result.add(imagesAtTheSameStage);
				    }
				}
		    }
		    return result;
		}
		return null;
    }
}

package gmerg.assemblers;

import gmerg.db.DBHelper;
import gmerg.db.GeneStripDAO;
import gmerg.db.MySQLDAOFactory;
import gmerg.entities.submission.ImageDetail;
import gmerg.utils.table.*;

import java.sql.Connection;
import java.util.HashMap;
import java.util.ArrayList;

public class ImageMatrixBrowseAssembler extends OffMemoryCollectionAssembler {
    private boolean debug = false;

//	private String gene;
	private ArrayList imageData = null;
	
	public ImageMatrixBrowseAssembler (HashMap<String,Object> params, CollectionBrowseHelper helper) {
		super(params, helper);
	if (debug)
	    System.out.println("ImageMatrixBrowseAssembler.constructor");

	}

	public void setParams() {
		super.setParams();
//		gene = getParam("gene");
		imageData = getData();
	}

	/**
	 * @author xingjun - 27/11/2008
	 * modified by mehran - 13/02/2009 - try to re-use the 3 dimension data structure
	 * <p>xingjun - 08/07/2011 - changed the link to the EMAP database: need to change to get the link from the database in the future </p>
	 * 
	 */
	public DataItem[][] retrieveData(int column, boolean ascending, int offset, int num) {
//		data = getData();
//		if (data==null)
//			return null;
//		System.out.println("=====ImageMatrixBrowseAssembler:retrieveData======");
//		System.out.println("=====ImageMatrixBrowseAssembler:retrieveData go to getData======");
		imageData = getData();
		
		if (imageData == null || imageData.size() == 0) {
			return null;
		}
		int dataLength = this.getDataLength();
//		System.out.println("dataLength: " + dataLength);
		int numRows = Math.min(num, dataLength-offset);
		int numCols = getStages().size();

		DataItem[][] dataItems = new DataItem[numRows][numCols];
		for (int i=0; i<numRows; i++)  
			for (int j=0, col=0; j<imageData.size(); j++) {

				ArrayList imagesOfGivenStage = (ArrayList)imageData.get(j);
				int iNumber = imagesOfGivenStage.size();
				ImageDetail imageInfoItem = null;
				int idx = i+offset;
 				if (idx >= iNumber)
 					dataItems[i][col] = new DataItem("");
 				else {
 					imageInfoItem = (ImageDetail)imagesOfGivenStage.get(i+offset);
 					String submissionId = imageInfoItem.getAccessionId();
 					String imageId = imageInfoItem.getImageId(); 
 					String stage = imageInfoItem.getStage();
 					String serialNumber = imageInfoItem.getSerialNo();
 					String imageUrl = this.assembleIIPImageURL(imageInfoItem.getFilePath(), imageInfoItem.getPyramidTileSize());
// 					System.out.println("image url: " + imageUrl);
 					ArrayList<DataItem> complexValue = new ArrayList<DataItem>();
 					complexValue.add(new DataItem(imageId, -1)); 	// Type=-1 means that the item will not display
 					complexValue.add(new DataItem(submissionId, submissionId, "ish_submission.html?id="+submissionId, 10)); // submission id
// 					complexValue.add(new DataItem("TS" + stage,"", "http://genex.hgu.mrc.ac.uk/Databases/Anatomy/Diagrams/ts"+stage+"/", 3));  // stage
 					complexValue.add(new DataItem("TS" + stage,"", "http://www.emouseatlas.org/emap/ema/theiler_stages/StageDefinition/ts"+stage+"definition.html", 3));  // stage
 					DataItem zoomViewerItem = new DataItem(imageUrl, "Click to open in the zoom viewer", submissionId, 18, 0, 120);
 					zoomViewerItem.setSpareValue(serialNumber);	// Serial number
 					complexValue.add(zoomViewerItem);  // zoom viewer
 					dataItems[i][col] = new DataItem(complexValue, 80);
 				}
 				col++;
 		}
//		System.out.println("dataItem size:row: " + dataItems.length + " col: " + dataItems[1].length);
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
		for (int i=0;i<len;i++) {
			int imageNumbers = ((ArrayList)imageData.get(i)).size();
			if (imageNumbers > dataLength) {
				dataLength = imageNumbers;
			}
		}
		return dataLength;
	}

	
	public int retrieveNumberOfRows() {
//		System.out.println("=====ImageMatrixBrowseAssembler:retrieveNumberOfRows======");
		//* ---return the value---  * /
//		System.out.println("=====ImageMatrixBrowseAssembler:retrieveNumberOfRows go to getData======");
		imageData = getData();
//		if (data != null) 
//			return data.length;
		if (imageData != null && imageData.size() != 0) {
			return this.getDataLength();
			
		}
		return 0;
	}
	
	/**
	 * modified by xingjun 16/02/2009
	 * input data object changed
	 * @return
	 */
	public ArrayList<String> getStages() {
		ArrayList<String> stages = new ArrayList<String>();
// 		data = getData();
//		if (data==null)
//			return stages;
 		imageData = getData();
		if (imageData==null)
			return stages;
//		for(int j=0; j<data[0].length; j++) 
//			if ((Boolean)data[0][j][0])
//				stages.add((String)data[0][j][3]);
		int len = imageData.size();
		for(int i=0; i<len; i++) {
			ArrayList imagesForGivenStage = (ArrayList)imageData.get(i);
			String stage = ((ImageDetail)imagesForGivenStage.get(0)).getStage();
			stages.add(stage);
		}
		return stages;
	}
	
	public HeaderItem[] createHeader() {
		ArrayList<String> stages = getStages();
		HeaderItem[] header = new HeaderItem[stages.size()];
		int i = 0;
		for(String stage: stages) {
			header[i++] = new HeaderItem("TS"+stage, false);
		}
 		return header;
	}    
	
	/****************************************************************************************
	 * private methods
	 * 
	 ****************************************************************************************/
	/**
	 * @author xingjun - 28/11/2008
	 * <p>modified by xingjun - input changed and need to change output structure as well</p>
	 * @param symbol
	 * @return
	 */

	private ArrayList getData() {
//		System.out.println("=====ImageMatrixBrowseAssembler:getData======");
		if (!(imageData == null || isAnyParameterChenged())) {
//			System.out.println("---------->current Image data used");		
			return imageData;
		}
		ArrayList rawImageInfo = null;
		ArrayList imageInfo = null;
		// if imageId is not null, get data by image id; or get data by gene
		if (ids != null && ids.size() > 0) {
//			System.out.println("ImageMatrixBrowserAssembler:getData: ids is not null###ids size: " + ids.size());
//			System.out.println("no of image id: " + imageIds.size());
			rawImageInfo = this.getInsituSubmissionImagesByImageIds(ids);
		}
//		else {
//			ArrayList<String> imageIds = this.getCollectionIds();
//			if (imageIds != null && imageIds.size() > 0) {
//				System.out.println("ImageMatrixBrowserAssembler got collection ids!");		
//				rawImageInfo = this.getInsituSubmissionImagesByImageIds(imageIds);
//			} else {
//				System.out.println("ImageMatrixBrowserAssembler did not get collection ids!");		
//			}
//		}
		
//		else {
//			rawImageInfo = this.getInsituSubmissionImagesByGene(gene);
//		}
		
//		System.out.println("image raw data size: " + rawImageInfo.size());
		// re-construct the result for display
		if (rawImageInfo != null && rawImageInfo.size() != 0) {
//			System.out.println("no of images: " + rawImageInfo.size());
			imageInfo = this.reconstructInsituSubmissionImageInfo(rawImageInfo, 0);
		}
//		if (imageInfo != null) 	System.out.println("ImageMatrixAssembler:getData:imageInfo size: " + imageInfo.size());
//		else System.out.println("ImageMatrixAssembler:getData:imageInfo is empty!");
		return imageInfo;
	}
	
	/**
	 * @author xingjun - 27/11/2008
	 * @param symbol
	 * @return
	 */
	private ArrayList getInsituSubmissionImagesByGene(String symbol) {
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		GeneStripDAO geneStripDAO = MySQLDAOFactory.getGeneStripDAO(conn);
		
		// get data from database
		ArrayList images = geneStripDAO.getInsituSubmissionImagesByGene(symbol);
		
		// release resources
        DBHelper.closeJDBCConnection(conn);
		geneStripDAO = null;
		
		// return results
		return images;
	}
	
	/**
	 * @author xingjun - 17/02/2009
	 * @param imageIds
	 * @return
	 */
	private ArrayList getInsituSubmissionImagesByImageIds(ArrayList<String> imageIds) {
		// create a dao
		Connection conn = DBHelper.getDBConnection();
		GeneStripDAO geneStripDAO = MySQLDAOFactory.getGeneStripDAO(conn);
		
		// get data from database
//		for (int i=0;i<imageIds.size();i++)
//		System.out.println("imageIds@assembler: " + imageIds.get(i).toString());
		ArrayList images = 
			geneStripDAO.getInsituSubmissionImagesByImageId(imageIds);
		
		// release resources
        DBHelper.closeJDBCConnection(conn);
		geneStripDAO = null;
		
		// return results
		return images;
	}
	
	/**
	 * @author xingjun - 13/02/2009
	 * <p>modified by xingjun - 20/02/2009 
	 * - fixed the bug: when there's only one image for the last stage, it will not
	 *   be added into the result</p>
	 * @param rawImageInfo
	 * @param ii
	 * @return
	 */
	private ArrayList reconstructInsituSubmissionImageInfo(ArrayList rawImageInfo, int dummy) {
		if (rawImageInfo != null) {
			int numberOfImages = rawImageInfo.size();
//			System.out.println("reconstructInsituSubmissionImageInfo@raw image size: " + numberOfImages);
			ArrayList<ArrayList> result = new ArrayList<ArrayList>();
			int step = 0;
			String tempStage = null;
			for (int i=0;i<numberOfImages;i+=step) {
//				System.out.println("i=" + i);
				step = 0;
				ArrayList<ImageDetail> imagesAtTheSameStage = new ArrayList<ImageDetail>();
				// go through the list and put images at the same stage into
				// different ArrayList objects and then put them together into
				// the final result ArrayList
				for (int j=i;j<numberOfImages;j++) {
//					System.out.println("j=" + j);
					ImageDetail id = (ImageDetail)rawImageInfo.get(j);
					String stage = id.getStage();
//					System.out.println("stage: " + stage);
					step++;
					if (tempStage == null) { // first image of the stage
						tempStage = stage;
						imagesAtTheSameStage.add(id);
//						System.out.println("empty tempStage, add image into stage" + j);
					} else { // follow-up images
						/** image at different stage, move on to the next stage */
						if (!stage.equals(tempStage)) { 
							result.add(imagesAtTheSameStage);
//							System.out.println("nst:add stage into result" + j);
							tempStage = null;
							step--;
//							System.out.println("step= " + step);
							break;
						} else { /** image at the same stage, add it */
							imagesAtTheSameStage.add(id);
//							System.out.println("non-empty tempStage, add image into stage" + j);
						}
					}
					if (j==(numberOfImages-1)) { /** last image */
						result.add(imagesAtTheSameStage);
//						System.out.println("st:add stage into result" + j);
					}
				}
			}
//			System.out.println("reconstructInsituSubmissionImageInfo@number of stages: " + result.size());
//			for (int i=0;i<result.size();i++) {
//				System.out.println("nofImg@stage " + i + ": " + ((ArrayList)result.get(i)).size());
//			}
			return result;
		}
		return null;
	}
	
	/**
	 * @author xingjun - 02/12/2008
	 * <p>used for concatenate iip image url</p>
	 * @param originalURL
	 * @param pyrNumber
	 * @return
	 */
	private String assembleIIPImageURL(Object originalURL, Object pyrNumber) {
		String iipImageURLDot = "."; // part1
		String imageQuality = "100";
		String iipImageURLImgQLT = ".pyr.tif&QLT=" + imageQuality; // part2
		String iipImageURLRegion = "&RGN=0,0,1,1"; // part3
		String imageHeight = "240";
		String iipImageURLHeight = "&HEI=" + imageHeight; // part4
		String iipImageURLConvert = "&CVT=jpeg"; // part5
		
		String fullURL = 
			originalURL.toString() + iipImageURLDot + pyrNumber.toString() + 
			iipImageURLImgQLT + iipImageURLRegion + iipImageURLHeight + iipImageURLConvert;
		return fullURL;
	}
}

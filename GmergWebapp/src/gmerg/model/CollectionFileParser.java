package gmerg.model;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

import gmerg.entities.CollectionInfo;
import gmerg.entities.Globals;
import gmerg.utils.Utility;
import gmerg.utils.table.CollectionBrowseHelper;

import org.apache.myfaces.custom.fileupload.UploadedFile;

/**
 * @author Mehran Sharghi
 *
*/

//********************************************************************************************************************
//********************************************************************************************************************
//* Collection File Parser
//********************************************************************************************************************
public class CollectionFileParser {
	
	private int collectionType;
	private ArrayList<String> ids;
	private HashMap<String, String> attributes;
	private Vector<String> fileContent;
	private String errorMessage;
	private int errorLevel;
	private int lineNo;
	final private String[] collectionStatuses = new String[]{"private", "public"};
	
	public CollectionFileParser() {
		reset();
	}

	public void reset() {
		attributes = new HashMap<String, String>();
		attributes.put("name", "");
		attributes.put("description", "");
		attributes.put("type", "0");
		attributes.put("status", "0");
		attributes.put("focus group", "0");
		errorMessage = "";
		errorLevel = 0;
		lineNo = 0;
		ids = new ArrayList<String>();
		fileContent = new Vector<String>();
	}

	public boolean readFile(UploadedFile uploadFile) {
		fileContent = new Vector<String>();
		try {
			if (uploadFile==null) {
				error(2, "Cannot read the file.", false);
				return false;
			}
			String fileName = uploadFile.getName().toLowerCase();
			if (fileName.indexOf("txt") == -1) {
				error(2, "Only .txt files are allowed.", false);
				return false;
			}
			InputStream in = new BufferedInputStream(uploadFile.getInputStream());
			InputStreamReader inReader = new InputStreamReader(in);
			BufferedReader bReader = new BufferedReader(inReader, in.available());
			while (bReader.ready()) {
				String nextLine = bReader.readLine();
				fileContent.add(nextLine);
			}
			in.close();
			inReader.close();
			bReader.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}

	public int parse() {
		for (;lineNo<fileContent.size(); lineNo++) {
			String[] list = fileContent.elementAt(lineNo).split(";\t");
			for(int j=0; j<list.length; j++) {
				String s = list[j];
				StringTokenizer st = new StringTokenizer(s, ";\t\n\r\f");
				String expression;
				while (st.hasMoreTokens()){
					expression = st.nextToken().trim();
					if (expression.length() == 0)
						continue;
					if(isHeaderExpression(expression)) {
						if (!parseHeaderItem(expression))
							return errorLevel;
					}
					else 
						parseId(expression);
				}
			}
		}
		checkHeaderValidity();
		if (errorLevel<2)	// If there are errors id validity check can not proceed
			checkIdsValidity();
		
		return errorLevel;
	}
	
	public CollectionInfo getCollectionInfo() {
		CollectionInfo collectionInfo = new CollectionInfo();
		collectionInfo.setName(attributes.get("name"));
		collectionInfo.setDescription(attributes.get("description"));
		collectionInfo.setType(collectionType);
		collectionInfo.setStatus(Integer.parseInt(attributes.get("status")));
		collectionInfo.setFocusGroup(Integer.parseInt(attributes.get("focus group")));
		return collectionInfo;
	}

	public void printCollection() {
		Set<String> keys = attributes.keySet();
		Iterator it = keys.iterator();
		while (it.hasNext()) {
			String key = (String) it.next();
//			System.out.println(key + ": " + attributes.get(key));
		}
//		System.out.println("parsed ids = " + getIdsString());
	}

	public ArrayList<String> getIds() {
		return ids;
	}
	
	public String getIdsString() {
		String idsStr = "";
		for(int i=0; i<ids.size(); i++) 
			idsStr += ((i==0)?"":"\t") + ids.get(i);
		return idsStr;
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}
	
	public int getErrorLevel() {
		return errorLevel;
	}
	
	public static boolean isCollectionNameValid(String name) {
		return name.matches("[\\w-]+");
	}

	private void error(int level, String message) {
		error(level, message, true);
	}
	private void error(int level, String message, boolean displayLineNo) {
		final String[] errorLevels = {"Note", "Warning", "Error"};
		errorMessage += errorLevels[level] + ": " + message;
		if (displayLineNo)
			errorMessage += " (line:" + String.valueOf(lineNo+1) + ")";
		errorMessage += "\n";
		errorLevel = Math.max(errorLevel, level);
	}
    
	private void checkHeaderValidity() {
		if (attributes.get("name").equals("")) 
			error(2, "Collection name should be specified.", false);
		else if (!isCollectionNameValid(attributes.get("name")))
			error(2, "Valid characters for collection name are: letters digits _ -", false);
		if (attributes.get("type").equals(""))
			error(2, "Collection type should be specified.", false);
		
		return;
	}
	
	private void checkIdsValidity() {
		int type = Integer.parseInt(attributes.get("type"));
		CollectionBrowseHelper helper = Globals.getCollectionBrowseHelper(ids, type);
		HashSet<String> invalidIds = helper.getInvalidIds();
		if (invalidIds==null || invalidIds.size()==0)
			return;
		
		if (invalidIds.size() == ids.size())
			error(2, "None of the IDs in your file exist in the gudmap database.", false);
		else  
			error(1, "The following IDs are ignored because they do not exist in the gudmap database.\n" + invalidIds.toString() + "\n", false);
		
		ids = helper.getValidIds();
	}
	
	private boolean isHeaderExpression(String expression) {
		String[] tokens = expression.split("=");
		if (tokens != null && tokens.length >= 1 && attributes.containsKey(tokens[0].toLowerCase().trim()) )
			return true;
		return false;
	}
	
	private boolean parseHeaderItem(String expressionStr){
		String[] expression = expressionStr.split("=");
		String key = expression[0].toLowerCase().trim();
		if (attributes.containsKey(key)) {
			String value = (expression.length>1)? expression[1].trim() : "";
			if (key.equals("type")) {
				collectionType = Utility.stringArraySearch(Globals.getCollectionCategoriesNames(), value, true);
				if (collectionType<0) {
					error(2, "Unknown Collection Type " + value);
					return false;
				}
				value = String.valueOf(collectionType);
			}
			if (key.equals("focus group")) {
				int focusGroup = Utility.stringArraySearch(Globals.getFocusGroups(), value, true);
				if (focusGroup<0) {
					error(1, "Focus Group unknown.");
					focusGroup = 0;
				}
				value = String.valueOf(focusGroup);
			}
			if (key.equals("status")) {
				int status = Utility.stringArraySearch(collectionStatuses, value, true);
				if (status<0) {
					error(1, "Unknown Status. Private is used.");
					status = 0;
				}
				value = String.valueOf(status);
			}
			
			attributes.put(key, value);
		}
		else 
			error(1, "Unknown attribute " + key + " is ignored.");
		return true;
	}
	
	/**
	 * <p>modified by xingjun - 27/10/2009 - typo correction on error message</p>
	 * @param id
	 */
	private void parseId(String id){
		if (collectionType>=0 && (collectionType!=0 || Utility.matchesGudmapIdFormat(id))) {
			if (ids.contains(id))
				error(1, "duplicate id " + id + " ignored");
			else
				ids.add(id);
		}
		else
			error(2, "Invalid id " + id + "." );
	}

}


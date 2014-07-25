package gmerg.entities;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeMap;

import javax.faces.model.SelectItem;

import gmerg.model.EntriesCollectionBrowseHelper;
import gmerg.model.GenesCollectionBrowseHelper;
import gmerg.model.ImagesCollectionBrowseHelper;
import gmerg.model.ProbesCollectionBrowseHelper;
import gmerg.db.DBQuery;
import gmerg.utils.Utility;
import gmerg.utils.DbUtility;
import gmerg.utils.Visit;
import gmerg.utils.table.CollectionBrowseHelper;
import gmerg.utils.table.FilterItem;
import gmerg.utils.table.FilterItem.FilterType;

public class Globals {
    
    
    //************************************************* Collections **********
	public static class CollectionAttribute {
	    int colId;
	    String name;
	    boolean caseSensitive;
	    public CollectionAttribute(int colId, String name) {
		this(colId, name, true);
	    }
	    public CollectionAttribute(int colId, String name, boolean caseSensitive) {
		this.colId = colId;
		this.name = name;
		this.caseSensitive = caseSensitive;
	    }
	    public boolean isCaseSensitive() {
		return caseSensitive;
	    }
	    public int getColId() {
		return colId;
	    }
	    public String getName() {
		return name;
	    }
	}
    
    public static class CollectionCategory {
		private String category;
		TreeMap<Integer, CollectionAttribute> attributes;
		boolean caseSensitiveIds;
		
		public CollectionCategory(String category) {
		    this(category, false);
		}
		public CollectionCategory(String category, boolean caseSensitiveIds) {
		    this(category, null, caseSensitiveIds);
		}
		
		public CollectionCategory(String category, CollectionAttribute[] attributes) {
		    this(category, attributes, false);
		}
		
		public CollectionCategory(String category, CollectionAttribute[] attributesArray, boolean caseSensitiveIds) {
		    this.category = category;
		    attributes = new TreeMap<Integer, CollectionAttribute>();
		    if (attributesArray!=null) {
			for (int i=0; i<attributesArray.length; i++)
			    attributes.put(attributesArray[i].getColId(), attributesArray[i]);
		    }
		    this.caseSensitiveIds = caseSensitiveIds;
		}
		
		public int[] getAttributeColNums() {
		    if (null == attributes || null == attributes.keySet())
			return null;
	
		    Set<Integer> a = attributes.keySet();
		    int colNums[] = new int[a.size()];
		    for (Integer i : a)
			colNums[i] = i;
		    return colNums;
		}
		
		public String[] getAttributeNames() {
		    if (null == attributes|| null == attributes.values())
			return null;
	
		    String[] a = new String[attributes.values().size()];
		    return attributes.values().toArray(a);
		}
		
		public String getCategory() {
		    return category;
		}
		
		public void setCategory(String category) {
		    this.category = category;
		}
		
		public SelectItem[] getAttributesSelectItems() {
		    if (null ==attributes || null == attributes.keySet())
			return null;
	
		    Set<Integer> colNums = attributes.keySet();
		    if (colNums.size()==0)
			return null;
		    SelectItem[] attributeItems = new SelectItem[colNums.size()];
		    int i=0;
		    for (Integer col : colNums) {
			attributeItems[i++] = new SelectItem(col, attributes.get(col).getName());
		    }
		    return attributeItems;
		    
		}
		
		public TreeMap<Integer, CollectionAttribute> getAttributes() {
		    return attributes;
		}
		
		public CollectionAttribute getAttribute(int colId) {
		    if (null == attributes)
			return null;
	
		    return attributes.get(colId);
		}
		
		public boolean isCaseSensitiveIds() {
		    return caseSensitiveIds;
		}
		
		public void setCaseSensitiveIds(boolean caseSensitiveIds) {
		    this.caseSensitiveIds = caseSensitiveIds;
		}
	
    }//end class CollectionCategory
    
    private static final CollectionAttribute[] entriesCollectionAttributes = {
	/*new CollectionAttribute(0, "Ids", false), 
	new CollectionAttribute(1, "Genes", false)};*/
    new CollectionAttribute(1, "Ids", false), 
	new CollectionAttribute(0, "Genes", false)};//derek
    
    private static final CollectionCategory[] collectionCategories = {
	new CollectionCategory("Entries", entriesCollectionAttributes), 
	new CollectionCategory("Genes"),
	new CollectionCategory("Images", true),
	new CollectionCategory("MOE430_Probes"),
	new CollectionCategory("ST1_Probes") };
    
    /*private static final CollectionCategory[] collectionCategories = {
    	new CollectionCategory("Genes"),
    	new CollectionCategory("Entries", entriesCollectionAttributes),
    	new CollectionCategory("Images", true),
    	new CollectionCategory("MOE430_Probes"),
    	new CollectionCategory("ST1_Probes") };*/
    
    public static CollectionCategory[] getCollectionCategories() {
	return collectionCategories;
    }
    
    public static String[] getCollectionCategoriesNames() {
	if (null == collectionCategories)
	    return null;

	String[] categories = new String[collectionCategories.length];
	for (int i=0; i<collectionCategories.length; i++) 
	    categories[i] = collectionCategories[i].getCategory();
	return categories;
    }
    
    public static String getCollectionCategoryName(int index) {
	if (null == collectionCategories || index > collectionCategories.length)
	    return null;

	return collectionCategories[index].getCategory();
    }
    
    public static CollectionCategory getCollectionCategory(int index) {
	if (null == collectionCategories || index > collectionCategories.length)
	    return null;

	return collectionCategories[index];
    }
    
    public static CollectionBrowseHelper getCollectionBrowseHelper(ArrayList<String> collectionItemsIds, int collectionType) {
	if (collectionType == 3)
	    return getCollectionBrowseHelper(collectionItemsIds, collectionType, "4_5"); // "4_5" is only used for collection of MOE430 porbes. When no value is specified default is AllKidney master table
	if (collectionType == 4)
	    return getCollectionBrowseHelper(collectionItemsIds, collectionType, "3_2"); // "3_2" is only used for collection of ST1 porbes. When no value is specified default is AllKidney master table
	
	return getCollectionBrowseHelper(collectionItemsIds, collectionType, "3_2");
    }
    
    public static CollectionBrowseHelper getCollectionBrowseHelper(ArrayList<String> collectionItemsIds, int collectionType, int focusGroup) {
	if (collectionType == 3){
	    if (focusGroup == 1 || focusGroup == 2)
		return getCollectionBrowseHelper(collectionItemsIds, collectionType, "4_5");
	    else if (focusGroup == 3 || focusGroup == 4)
		return getCollectionBrowseHelper(collectionItemsIds, collectionType, "4_7");
	    else if (focusGroup == 5)
		return getCollectionBrowseHelper(collectionItemsIds, collectionType, "4_6");
	    else
		return getCollectionBrowseHelper(collectionItemsIds, collectionType, "4_5");
	}
	if (collectionType == 4){
	    if (focusGroup == 1 || focusGroup == 2)
		return getCollectionBrowseHelper(collectionItemsIds, collectionType, "3_2");
	    else if (focusGroup == 3 || focusGroup == 4)
		return getCollectionBrowseHelper(collectionItemsIds, collectionType, "3_1");
	    else if (focusGroup == 5)
		return getCollectionBrowseHelper(collectionItemsIds, collectionType, "3_4");
	    else
		return getCollectionBrowseHelper(collectionItemsIds, collectionType, "3_2");
	}
	
	return getCollectionBrowseHelper(collectionItemsIds, collectionType, "3_2");
    }
    //DEREK
    public static CollectionBrowseHelper getCollectionBrowseHelper(ArrayList<String> collectionItemsIds, int collectionType, String masterTableId) {
		switch (collectionType) {
		case 0:	return new EntriesCollectionBrowseHelper(collectionItemsIds, 0, "collectionIds", 1);
		case 1:	return new GenesCollectionBrowseHelper(collectionItemsIds, 1, "geneSymbols", 1);
		case 2:	return new ImagesCollectionBrowseHelper(collectionItemsIds, 2, "imageIds", 1);
		case 3:	return new ProbesCollectionBrowseHelper("GPL1261", masterTableId, collectionItemsIds, 3, "probeIds", 1); 
		case 4:	return new ProbesCollectionBrowseHelper("GPL6246", masterTableId, collectionItemsIds, 4, "probeIds", 1);
		}	
		return null;
    }
    // The 4th parameter is the IDcol from which the selections are made 
    /*public static CollectionBrowseHelper getCollectionBrowseHelper(ArrayList<String> collectionItemsIds, int collectionType, String masterTableId) {
		switch (collectionType) {
		case 0:	return new EntriesCollectionBrowseHelper(collectionItemsIds, 0, "collectionIds", 0);
		case 1:	return new GenesCollectionBrowseHelper(collectionItemsIds, 1, "geneSymbols", 0);
		case 2:	return new ImagesCollectionBrowseHelper(collectionItemsIds, 2, "imageIds", 0);
		case 3:	return new ProbesCollectionBrowseHelper("GPL1261", masterTableId, collectionItemsIds, 3, "probeIds", 0); 
		case 4:	return new ProbesCollectionBrowseHelper("GPL6246", masterTableId, collectionItemsIds, 4, "probeIds", 0);
		}	
		return null;
    }*/
    
    //************************************************************************
	private static final String[] focusGroups = {	
	"none", 
	"Metanephros", 
	"Early reproductive system",  
	"Male reproductive system", 
	"Female reproductive system", 
	"Lower urinary tract",
	"Mesonephros"};
    
    public static String[] getFocusGroups() {
	return focusGroups;
    }
    
    public static String getFocusGroup(int i) {
	return focusGroups[i];
    }
    
    //************************************************************************
	private static final String[] accessRestrictedPages = { 
	"collectionList_browse", 
	"collectionOperation_browse",
	"collection_upload",
	"collection_inforamtion",
	"edit_entry_page_per_lab"} ;
    
    public static String[] getAccessRestrictedPages() {
	return accessRestrictedPages;
    }
    
    //**************************************************************************
	private static final String[] noSessionControlPages = {
	"database_homepage", 
	"analysis",
	"lab_summaries",
	"series_browse",
	"focus_mic_browse",
	"focus_platform_browse",
	"focus_stage_browse",
	"focus_ish_browse",
	"focus_ihc_browse",
	"focus_insitu_browse",
	"focus_tg_browse",
	"focus_gene_index_browse",
	"focus_stage_browse",
	"all_components_genelists",
	"boolean_test",
	"genelist_folder",
	"mic_focus",
	"requirement",
	"ontologydescription",
	"series_browse",
	"entry_page",
	"zoom_viewer",
	"ish_submission",
	"mic_submission",
	"image_matrix_browse",
	"index"};
    
    public static String[] getNoSessionControlPages() {
	return noSessionControlPages;
    }
    
    //**************************************************************************
	private static String[] historyRequiredPages = {
	"secure/login", 
	"message"};
    
    public static String[] getHistoryRequiredPages() {
	return historyRequiredPages;
    }
    
    //***************************************************************************
	private static final String mailServer = "mailhost.hgu.mrc.ac.uk";
    
    
    public static String getMailServer() {
	return mailServer;
    }
    
    //*****************************************************************************
	private static final String cdtPath = "genelist_data/cdt_files";
    
    public static String getCdtPath() {
	return cdtPath;
    }
    
    //******************************************************** Filters ****************
	public enum PredefinedFilters {LAB, STAGE, SEX, ASSAY, SPECIMEN, GENOTYPE, DATE, EXPRESSION}
    
    // modified by Bernie - 20/05/2011 - changed length of options array from 13 to 12 (Mantis 550)
    public static FilterItem getPredefinedFilter(PredefinedFilters filter) {
	int i = 0;

	switch(filter){
	case LAB:
	    String[] source = null;
	    List list = DbUtility.query(DBQuery.dataSource);
	    int iSize = list.size();
	    source = new String[iSize];
	    for (i = 0; i < iSize; i++)
		source[i] = (String)list.get(i);
	    return new FilterItem(FilterType.MULTIPLE, source, null); 
	case STAGE: 
	    String[]options = new String[12];
	    options[0] = "";
	    for (i=17; i<29; i++)
		options[i-17] = ""+i;
	    FilterItem stageFilter = new FilterItem(FilterType.LISTRANGE, options, null, true);
	    stageFilter.setRangeSwap(true);
	    return stageFilter;
	case SEX:
	    return new FilterItem(FilterType.RADIO, new String[]{"male", "female", "unknown"}, null);
	case ASSAY: 
	    return new FilterItem(FilterType.CHECKBOX, new String[]{"ISH", "IHC", "Array", "TG"}, null);
	    /*return new FilterItem(FilterType.CHECKBOX, new String[]{"ISH", "IHC", "Array", "TG", "Sequence"}, null);*/
	case SPECIMEN:
	{
		return new FilterItem(FilterType.CHECKBOX, new String[]{"section", "wholemount", "opt-wholemount"}, null);
		/*FilterItem specimenFilter=new FilterItem(FilterType.CHECKBOX, new String[]{"section", "wholemount", "opt-wholemount"}, new String[]{"section"});
	    specimenFilter.setActive(true); 
	    return specimenFilter;*/
	}
	case GENOTYPE: 
	    return new FilterItem(FilterType.LIST, new String[]{"wholemount", "section"}, null);
	case DATE:
	    FilterItem dateFilter = new FilterItem(FilterType.DATERANGE, null, null);
	    dateFilter.setRangeSwap(true);
	    return dateFilter;
	case EXPRESSION: 
	    return new FilterItem(FilterType.CHECKBOX, new String[]{"present", "not detected", "uncertain"}, new String[]{"present", "not detected"});
	}
	return new FilterItem(FilterType.SIMPLE, null, null);
    }
    
    /**
     * @author xingjun - 27/11/2008
     * @return
     */
    public static String[] getTheilerStages() {
	return new String[] {"TS17", "TS18", "TS19", "TS20", "TS21", "TS22", 
			     "TS23", "TS24", "TS25", "TS26", "TS27", "TS28"};
    }
    
    //************************************************* Master Table *******************
	private static final int[][][] colsWidth = {
	{{2,20,30}, {3,20,30}, {9,25,30}, {10,25,30}, {11,25,30}},
	{{2,20,30}, {3,20,30}, {9,25,30}, {10,25,30}, {11,25,30}, {13,25,30}, {14,25,30}, {16,25,30}, {17,25,30}}
    };
    
    public static int[][] getOntologyColsWidth(String platformId) {
	int index = 0;
	if ("GPL6246".equals(platformId))	//if ST1
	    index = 1;
	return colsWidth[index];
    }
    
    //************************* gene strip *******************************
	private static final int defaultExpressionProfileBarHeight = 50;
    
    public static int getDefaultExpressionProfileBarHeight() {
	return defaultExpressionProfileBarHeight;
    }
    
    public static String getGudmapEditorsEmail() {
	return "gudmap-editors@hgu.mrc.ac.uk";
    }
    
    public static String getArchiveSiteArrayURL() {
	return "https://www.gudmap.org/Gudmap/archive/hgu_private/microarrays/";
    }
    
    public static SelectItem[] getQuickSearchTypeItems() {
	return new SelectItem[] {
	    new SelectItem("Gene", "Gene"),
	    new SelectItem("Anatomy", "Anatomy"),
	    new SelectItem("Accession ID", "Accession ID"),
	    new SelectItem("Gene Function", "Function")
	    //	    	    new SelectItem("Disease", "Disease")
	};
    }
}

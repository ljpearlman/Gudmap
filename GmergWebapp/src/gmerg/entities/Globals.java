package gmerg.entities;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeMap;

import javax.faces.model.SelectItem;

import gmerg.model.EntriesCollectionBrowseHelper;
import gmerg.model.GenesCollectionBrowseHelper;
import gmerg.model.ImagesCollectionBrowseHelper;
import gmerg.model.ProbesCollectionBrowseHelper;
import gmerg.utils.Utility;
import gmerg.utils.Visit;
import gmerg.utils.table.CollectionBrowseHelper;
import gmerg.utils.table.FilterItem;
import gmerg.utils.table.FilterItem.FilterType;

public class Globals {

	
	//************************************************* Collections **********************************************************
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
//				if (colNums.length != attributeNames.length) 
//					System.out.println("Warnning!: Collection attribute declaration error for collection: " + category);
//				int n = Math.min(colNums.length, attributeNames.length);
				for (int i=0; i<attributesArray.length; i++)
					attributes.put(attributesArray[i].getColId(), attributesArray[i]);
			}
			this.caseSensitiveIds = caseSensitiveIds;
		}

		public int[] getAttributeColNums() {
			Set<Integer> a = attributes.keySet();
			int colNums[] = new int[a.size()];
			for (Integer i : a)
				colNums[i] = i;
			return colNums;
		}

		public String[] getAttributeNames() {
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
			return attributes.get(colId);
		}

/*		
		public String getAttribute(Integer key) {
			return attributes.get(key); 
		}
		
		public void setAttributes(TreeMap<Integer, String> attributes) {
			this.attributes = attributes;
		}
*/
		public boolean isCaseSensitiveIds() {
			return caseSensitiveIds;
		}

		public void setCaseSensitiveIds(boolean caseSensitiveIds) {
			this.caseSensitiveIds = caseSensitiveIds;
		}
		
	}
	
	private static final CollectionAttribute[] entriesCollectionAttributes = {new CollectionAttribute(0, "Ids", false), 
																				 new CollectionAttribute(1, "Genes", false)};
	
	private static final CollectionCategory[] collectionCategories = {new CollectionCategory("Entries", entriesCollectionAttributes), 
																		new CollectionCategory("Genes"),
																		new CollectionCategory("Images", true),
																		new CollectionCategory("MOE430_Probes"),
																		new CollectionCategory("ST1_Probes") /*,
																		new CollectionCategory("Tissues"),
																		new CollectionCategory("Queries") */
																		};
	public static CollectionCategory[] getCollectionCategories() {
		return collectionCategories;
	}

	public static String[] getCollectionCategoriesNames() {
		String[] categories = new String[collectionCategories.length];
		for (int i=0; i<collectionCategories.length; i++) 
			categories[i] = collectionCategories[i].getCategory();
		return categories;
	}

	public static String getCollectionCategoryName(int index) {
		return collectionCategories[index].getCategory();
	}

	public static CollectionCategory getCollectionCategory(int index) {
		return collectionCategories[index];
	}
	
	public static CollectionBrowseHelper getCollectionBrowseHelper(ArrayList<String> collectionItemsIds, int collectionType) {
		return getCollectionBrowseHelper(collectionItemsIds, collectionType, "4_1"); // "4_1" is only used for collection of MOE430 porbes. When no value is specified default is AllKidney master table
	}

	public static CollectionBrowseHelper getCollectionBrowseHelper(ArrayList<String> collectionItemsIds, int collectionType, int focusGroup) {
		if (focusGroup == 1 || focusGroup == 2)
			return getCollectionBrowseHelper(collectionItemsIds, collectionType, "4_1");
		else if (focusGroup == 3 || focusGroup == 4)
			return getCollectionBrowseHelper(collectionItemsIds, collectionType, "4_3");
		else if (focusGroup == 5)
			return getCollectionBrowseHelper(collectionItemsIds, collectionType, "4_2");
		else
			return getCollectionBrowseHelper(collectionItemsIds, collectionType, "4_1");
	}

	public static CollectionBrowseHelper getCollectionBrowseHelper(ArrayList<String> collectionItemsIds, int collectionType, String masterTableId) {
		switch (collectionType) {
			case 0:	return new EntriesCollectionBrowseHelper(collectionItemsIds, 0, "collectionIds", 0);
			case 1:	return new GenesCollectionBrowseHelper(collectionItemsIds, 1, "geneSymbols", 0);
			case 2:	return new ImagesCollectionBrowseHelper(collectionItemsIds, 2, "imageIds", 0);
			case 3:	return new ProbesCollectionBrowseHelper("GPL1261", masterTableId, collectionItemsIds, 3, "probeIds", 0); 
			case 4:	return new ProbesCollectionBrowseHelper("GPL6246", masterTableId, collectionItemsIds, 4, "probeIds", 0);
		}	
		return null;
	}

	//***********************************************************************************************************************************
	// modified by xingjun - 19/01/2009 - added a new focus group (Mesonephros)
	private static final String[] focusGroups = {	"none", 
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
	
	//***********************************************************************************************************************************
	private static final String[] accessRestrictedPages = { "collectionList_browse", 
															"collectionOperation_browse",
															"collection_upload",
		  													"collection_inforamtion",
		  													"edit_entry_page_per_lab"} ;
	public static String[] getAccessRestrictedPages() {
		return accessRestrictedPages;
	}

	//***********************************************************************************************************************************
	private static final String[] noSessionControlPages = {	"database_homepage", 
															"lab_summaries",
															"focus_gene_index_browse",
															"focus_stage_browse",
															"analysis",
															"requirement",
															"download_help",
															"entry_page",
															"ish_edit_expression",
															"edit_expression",
															//"lab_ish_edit",
															"edit_entry_page_per_lab",
															"index",
															"db_home"} ;

	public static String[] getNoSessionControlPages() {
		return noSessionControlPages;
	}

	//***********************************************************************************************************************************
	private static String[] historyRequiredPages = {"secure/login", 
									 				"restricted_access_redirect",
									 				"message"};
	
	public static String[] getHistoryRequiredPages() {
		return historyRequiredPages;
	}

	//***********************************************************************************************************************************
	private static final String mailServer = "mailhost.hgu.mrc.ac.uk";


	public static String getMailServer() {
		return mailServer;
	}

	//***********************************************************************************************************************************
	private static final String cdtPath = "genelist_data/cdt_files";
	
	public static String getCdtPath() {
		return cdtPath;
	}
	
	//******************************************************** Filters ******************************************************************
	public enum PredefinedFilters {LAB, STAGE, SEX, ASSAY, SPECIMEN, GENOTYPE, DATE, EXPRESSION}
	
	// modified by xingjun - 14/07/2010 - narrow the stage range down from 17 to 28
	// modified by Bernie - 20/05/2011 - added Capel to list of labs
	// modified by Bernie - 20/05/2011 - changed length of options array from 13 to 12 (Mantis 550)
	public static FilterItem getPredefinedFilter(PredefinedFilters filter) {
		switch(filter){
				case LAB:
					return new FilterItem(FilterType.MULTIPLE, new String[]{"Lessard", "Little", "McMahon", "Potter", "Gaido", "Mendelsohn", "Southard-Smith", "Capel", "Vezina", "Cohn", "Keast"}, null); 
				case STAGE: 
//					String[]options = new String[29]
					String[]options = new String[12];
					options[0] = "";
//					for (int i=1; i<29; i++)
//						options[i] = String.valueOf(i);
					for (int i=17; i<29; i++)
						options[i-17] = String.valueOf(i);
					FilterItem stageFilter = new FilterItem(FilterType.LISTRANGE, options, null, true);
					stageFilter.setRangeSwap(true);
					return stageFilter;
				case SEX:
					return new FilterItem(FilterType.RADIO, new String[]{"male", "female", "unknown"}, null);
				case ASSAY: 
					if(Utility.getProject().equalsIgnoreCase("EuReGene")) 
						return new FilterItem(FilterType.CHECKBOX, new String[]{"ISH", "Array"}, null);
					else
						return new FilterItem(FilterType.CHECKBOX, new String[]{"ISH", "IHC", "Array", "TG"}, null);
				case SPECIMEN: 
					return new FilterItem(FilterType.CHECKBOX, new String[]{"section", "wholemount"}, null);
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

	//************************************************* Master Table *********************************************************
	// modified by xingjun - 29/07/2010 - removed the SECOND column (probe)
	private static final int[][] defaultCols = {	
//													{1, 3, 5, 8, 9, 10, 15, 18},
//	 												{1, 3, 5, 8, 9, 10, 15, 18} 
													{1, 2, 4, 7, 8, 9, 14, 17},
													{1, 2, 4, 7, 8, 9, 14, 17}
	 											};

	private static final int[][][] colsWidth = {
//													{{2,30,30}, {4,20,30}, {10,25,30}, {11,25,30}, {12,25,30}},
													{{2,20,30}, {3,20,30}, {9,25,30}, {10,25,30}, {11,25,30}},
//													{{2,30,30}, {4,20,30}, {10,25,30}, {11,25,30}, {12,25,30}, {14,25,30}, {15,25,30}, {17,25,30}, {18,25,30}}
													{{2,20,30}, {3,20,30}, {9,25,30}, {10,25,30}, {11,25,30}, {13,25,30}, {14,25,30}, {16,25,30}, {17,25,30}}
												};
	
	private static final int[][] leftAlignedCols = {
//														{2, 10, 11, 12},
														{9, 10, 11},
//														{2, 10, 11, 12, 14, 15, 17, 18}
														{9, 10, 11, 13, 14, 16, 17}
													};
	
	public static int[] getDefaultOntologyCols(String platformId) {
		int index = 0;
		if ("GPL6246".equals(platformId))	//if ST1
			index = 1;
		return defaultCols[index];
	}
	
	public static int[][] getOntologyColsWidth(String platformId) {
		int index = 0;
		if ("GPL6246".equals(platformId))	//if ST1
			index = 1;
		return colsWidth[index];
	}
	
	public static int[] getLeftAlignedOntologyCols(String platformId) {
		int index = 0;
		if ("GPL6246".equals(platformId))	//if ST1
			index = 1;
		return leftAlignedCols[index];
	}
	
	//************************* gene strip *******************************
	private static final int defaultExpressionProfileBarHeight = 50;
	
	public static int getDefaultExpressionProfileBarHeight() {
		return defaultExpressionProfileBarHeight;
	}
	
	public static String getGudmapEditorsEmail() {
		return "gudmap-editors@hgu.mrc.ac.uk";
	}
	
	// added by xingjun - 24/03/2009
	public static String getArchiveSiteArrayURL() {
		return "https://www.gudmap.org/Gudmap/archive/hgu_private/microarrays/";
	}
	
	// added by xingjun - 19/07/2010
	public static SelectItem[] getQuickSearchTypeItems() {
		return new SelectItem[] {
				new SelectItem("Gene", "Gene"),
	    	    new SelectItem("Anatomy", "Anatomy"),
	    	    new SelectItem("Accession ID", "Accession ID"),
	    	    new SelectItem("Gene Function", "Function")
//	    	    new SelectItem("Disease", "Disease")
				};
	}
/*	
	public static String getArrayDataURL() {
		return "http://www.gudmap.org/Gudmap/arrayData/";
	}
*/
}

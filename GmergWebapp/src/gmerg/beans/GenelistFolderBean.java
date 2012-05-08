package gmerg.beans;

import gmerg.assemblers.AllComponentsGenelistAssembler;
import gmerg.entities.GenelistInfo;
import gmerg.entities.Globals;
import gmerg.utils.FacesUtil;
import gmerg.utils.Utility;
import gmerg.utils.Visit;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.apache.myfaces.custom.tree.DefaultMutableTreeNode;
import org.apache.myfaces.custom.tree.model.DefaultTreeModel;


/**
 * <p>
 * Bean holding the tree hierarchy.
 * </p>
 * 
 */
public class GenelistFolderBean implements Serializable
{
	private String cdtFile;
	private String gtrFile;
	private String genelistId;
	private boolean displayTreeView;
    private static final long serialVersionUID = 1L;
    private DefaultTreeModel  treeModel;
	public class GenelistTreeNode extends GenelistInfo {
		private int nodeType; //0-leaf node for a genelist;  1-parent node (group);  2-dumy leaf node
		private boolean containsGenelists;
		
		public GenelistTreeNode(String id, GenelistInfo info) {
			this(id, info.getTitle(), info.getSubmitter(), info.getDescription(), info.getFilename(), 
					info.getCdtFileName(), info.getGtrFileName(), 0, info.getPlatformId());
			summary = info.getSummary();
			numberOfEntries = info.getNumberOfEntries();
			entryDate = info.getEntryDate();
			description = info.getDescription(); // added by xingjun - 13/05/2010
			// xingjun - 27/05/2010
			if (title.equals("Developmental Kidney")) {
				displayOrder = 0;
			} else if (title.equals("Lower Urinary Tract")) {
				displayOrder = 1;
			} else if (title.equals("Reproductive System")) {
				displayOrder = 2;
			} else if (title.equals("Gonadal Tissues")) {
				displayOrder = 3;
			} else if (title.equals("Marker Genes")) {
				displayOrder = 4;
			} else if (title.equals("Disease Genes")) {
				displayOrder = 5;
			} else {
				displayOrder = info.getDisplayOrder();
			}
		}
		
		public GenelistTreeNode(String id, String title, String submitter, String description, String fileName, String cdtFileName, String gtrFileName, String platformId) {
			this(id, title, submitter, description, fileName, cdtFileName, gtrFileName, 0, platformId);
			// xingjun - 27/05/2010
			displayOrder = this.chooseDisplayOrder(title);
		}
		
		public GenelistTreeNode(String id, String title, String description) {
			this(id, title, "", description, "", "", "", 1, null);
			// xingjun - 27/05/2010
			displayOrder = this.chooseDisplayOrder(title);
		}
		
		public GenelistTreeNode(String id) {
			this(id, "", "", "", "", "", "", 2, null);
		}
		
		public GenelistTreeNode(String id, String title, String submitter, String description, String fileName, String cdtFileName, String gtrFileName, int nodeType, String platformId) {
			super(id, title, submitter, description, fileName, cdtFileName, gtrFileName, platformId);
			this.nodeType = nodeType;
			containsGenelists = false;
			// xingjun - 27/05/2010
			displayOrder = this.chooseDisplayOrder(title);
		}
		
		/**
		 * @author xingjun - 27/05/2010
		 */
		private int chooseDisplayOrder(String title) {
			int result = 0;
			if (title.equals("Developmental Kidney")) {
				result = 0;
			} else if (title.equals("Lower Urinary Tract")) {
				result = 1;
			} else if (title.equals("Reproductive System")) {
				result = 2;
			} else if (title.equals("Gonadal Tissues")) {
				result = 3;
			} else if (title.equals("Marker Genes")) {
				result = 4;
			} else if (title.equals("Disease Genes")) {
				result = 5;
			} else {
				result = 0;
			}
			return result;
		}
		
		// getters for display values in an html table
		public String getTitle() {
			return Utility.getValue(title, "&nbsp");
		}
		
		public String getSubmitter() {
			return Utility.getValue(submitter, "&nbsp");
		}
		
		public String getDescription() {
			return Utility.getValue(description, "&nbsp");
		}
		
		public String getEntriesNumber() {
			int n = getNumberOfEntries();
			return Utility.getValue(n==0? "" : String.valueOf(n), "&nbsp");
		}
		
		public String getDownload() {
			String archivePath = Globals.getArchiveSiteArrayURL();
			String downloadName = (cdtFileName==null || cdtFileName.equals("") || !cdtFileName.equals(filename))? filename : archivePath + filename ;
			return Utility.getValue(downloadName, "&nbsp");
		}
		
		public boolean isTreeviewAvailable() {
			if (nodeType != 0)
				return false; 
			
			if (AllComponentsGenelistAssembler.isClusterId(genelistId) )
				return false;
			
			return true;
		}

		public boolean isGenelistNode() {
			return nodeType==0; 
		}
		
		public boolean isHeatmapAvailable() {
			return nodeType==0 && ((cdtFileName==null || cdtFileName.equals("") || !cdtFileName.equals(filename)));
		}

		public String getStyleClass() {
			return containsGenelists? "plaintextbold" : "";
		}
		
		public String getTreeviewParams() {
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("genelistId", genelistId);
			if (cdtFileName!=null && !cdtFileName.equals("") ) {
				String archivePath = Globals.getArchiveSiteArrayURL();
				params.put("cdtFile", archivePath+cdtFileName);	//filename is cdtFilename + directory path 
				if (gtrFileName!=null && !gtrFileName.equals("") ) 
					params.put("gtrFile", archivePath+gtrFileName);
			}
			return Visit.packParams(params);
		}

/*		
		public String getTreeviewLinkScript() {
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("treeviewApplet", "start");
			params.put("genelistId", genelistId);
			if (cdtFileName!=null && !cdtFileName.equals("") ) {
				String archivePath = Globals.getArchiveSiteArrayURL();
				params.put("cdtFile", archivePath+filename);	//filename is cdtFilename + directory path 
				if (gtrFileName!=null && !gtrFileName.equals("") ) 
					params.put("gtrFile", archivePath+gtrFileName);
			}
		    return TableUtil.getActionLinkScript("mainForm", "treeviewLink", params);
		}
*/		
	}
	
	/**
	 * <p>xingjun 22/04/2010
	 * - correct the typo for _25 gonadal tissues
	 * - merge summary info of _1100 Brunskill with its title </p>
	 */
	private static final String[][] genelistsData = {
//		{"_1",		"g", "Studies", 							"",						"_10",	"_15",	"_20",	"_25",	"_30",	"_35", "_40" },
		{"_1",		"g", "Studies", 							"",						"_10",	"_15",	"_20",	"_25",	"_30",	"_35" },
		{"_10",		"g", "Developmental Kidney",				"Developmental Kidney", "_100",	"_110" }, 
		{"_15",		"g", "Lower Urinary Tract",					"Lower Urinary Tract",	"_200"	}, 
		{"_20",		"g", "Reproductive System",					"Reproductive System" }, 
		{"_25",		"g", "Gonadal Tissues",						"Gonadal Tissues" }, 
		{"_30",		"g", "Marker Genes",						"Marker Genes",	},
		{"_35",		"g", "Disease Genes",						"Disease Genes" },
//		{"_40",		"g", "Miscellaneous",						"Miscellaneous" },
		
//		{"_100",	"g", "Affy MOE430",							"Developmental Kidney",	"_1110",	"_1120",	"_1130",	"_1140",	"_1150",	"_1160",	"_1170",	"_1180",	"_1190",	"_1200",	"_1210",	"_1220",	"_1230",	"_1240",	"_1250" }, 
		{"_100",	"g", "Affy MOE430",							"Developmental Kidney",	"_1100"}, 
		{"_110",	"g", "Affy ST_1.0",							"Developmental Kidney" }, 
		{"_200",	"g", "Affy MOE430",							"Lower Urinary Tract" },
		
		{"_1100",	"g", "Brunskill et al (Dev Cell(2008) 15(5):781-91)",							""  },
/*
		{"_1110",	"g", "Metanephric Mesenchyme",				"Metanephric Mesenchyme",					"_1110_1",	"_1110_2" },
		{"_1120",	"g", "Mesenchyme",							"Mesenchyme", 								"_1110_1",	"_1110_2" },
		{"_1130",	"g", "Renal Vesicle",				 		"Renal Vesicle",			 				"_1110_1",	"_1110_2" },
		{"_1140",	"g", "S-shaped body", 						"S-shaped body", 							"_1110_1",	"_1110_2" },
		{"_1150",	"g", "renal corpuscle",			 			"renal corpuscle",			 				"_1110_1",	"_1110_2" },
		{"_1160",	"g", "proximal tubules",					"proximal tubules", 						"_1110_1",	"_1110_2" },
		{"_1170",	"g", "Anlage loop of Henle",	 			"Anlage loop of Henle",		 				"_1110_1",	"_1110_2" },
		{"_1180",	"g", "Ureteric Bud", 						"Ureteric Bud", 							"_1110_1",	"_1110_2" },
		{"_1190",	"g", "Ureteric tip region",					"Ureteric tip region", 						"_1110_1",	"_1110_2" },
		{"_1200",	"g", "cortical collecting duct",			"cortical collecting duct", 				"_1110_1",	"_1110_2" },
		{"_1210",	"g", "Medullary collecting duct",			"Medullary collecting duct", 				"_1110_1",	"_1110_2" },
		{"_1220",	"g", "Urothelium", 							"Urothelium", 								"_1110_1",	"_1110_2" },
		{"_1230",	"g", "Ureteral Smooth Muscle",				"Ureteral Smooth Muscle", 					"_1110_1",	"_1110_2" },
		{"_1240",	"g", "Medullary interstitium",				"Medullary interstitium", 					"_1110_1",	"_1110_2" },
		{"_1250",	"g", "Cortical & nephrogenic interstitium", "Cortical and nephrogenic interstitium",	"_1110_1",	"_1110_2" },

		{"_1110_1",	"g", "Top 100 genes",						"Top 100 genes" },
		{"_1110_2",	"g", "Top 1000 genes",						"Top 1000 genes"}
*/		
/*
			
		{"2",	"l", "223_all",				"Aronow",	"223_all ANOVA order",	"aronow/november_2007/aronow_071107/data/GudmapBook1_110707ba.xls"	, null,											null, "GPL1261"},
		{"3",	"l", "102_SS",				"Aronow",	"S-shaped body",		"aronow/november_2007/aronow_071107/data/GudmapBook2_110707.xls"	, null,											null, "GPL1261"},
		{"4",	"l", "110_RV",				"Aronow",	"Renal Vesicles",		"aronow/november_2007/aronow_071107/data/GudmapBook2_110707.xls"	, null,											null, "GPL1261"},
		{"5",	"l", "12959_default-log2",	"Aronow",	"",						"aranow_040708/data/12959_default-log2.cdt"							, "aranow_040708/data/12959_default-log2.cdt",	null, "GPL1261"},
*/		
};
	private HashMap<String, HashSet<String>> childrenMap;
	private HashMap<String, GenelistTreeNode> allItemsMap;

	
	// ********************************************************************************
	// Contructor
	// ********************************************************************************
	public GenelistFolderBean() {
		
		displayTreeView = false;
		
		if ("appletStoped".equals(FacesUtil.getRequestParamValue("appletRequest"))) {  
			DefaultMutableTreeNode root = populateTree();
			sortTree(root, new NodeComparator(false));
			treeModel = new DefaultTreeModel(root);
			return;
		}
		
		
		DefaultMutableTreeNode root = populateTree();
		
		if (FacesUtil.getRequestParamValue("sortTreeCommand") == null) { 
			String treeSortOrder = Visit.getRequestParam("treeSortOrder", "ascending");
			Visit.setStatusParam("treeSortOrder", treeSortOrder);
			sortTree(root, new NodeComparator(treeSortOrder.equals("ascending")));
		}
		
		treeModel = new DefaultTreeModel(root);
		//else go to the action method
    }
	
	// ********************************************************************************
	// Private Methods  Methods
	// ********************************************************************************
	private DefaultMutableTreeNode populateTree() {
		allItemsMap = new HashMap<String, GenelistTreeNode>();
		childrenMap = new HashMap<String, HashSet<String>>();
		
		for (String[] genelist:genelistsData) {
			String id = genelist[0];
			HashSet<String> children = null;
			if (genelist[1].equalsIgnoreCase("g")) {
				allItemsMap.put(id, new GenelistTreeNode(id, genelist[2], genelist[3]));
				int n = genelist.length-4;
				children = new HashSet<String>();
				if (n>0) 
					for(int i=0; i<n; i++)
						children.add(genelist[i+4]);
				else 							// Add a dumy node for any group repsentative node with no children (empty groups)
					children.add("dumyNode");
				
				childrenMap.put(id, children);	
			}
/*			
			else {
				String cdtFile = genelist[6];
				String gtrFile = null;
//		    	if (cdtFile != null)
		    		gtrFile = genelist[7];
		    		
	  	     	allItemsMap.put(id, new GenelistTreeNode(id, genelist[2], genelist[3], genelist[4], genelist[5], cdtFile, gtrFile, genelist[8]));
			}
*/			
		}
		
		ArrayList<GenelistInfo> allGenelists = AllComponentsGenelistAssembler.retrieveAllAnalysisGenelists();
		for (GenelistInfo genelist : allGenelists) {
			String id = genelist.getGenelistId();
			String filename = genelist.getFilename();
			ArrayList<String> parentFolders = genelist.getFolderIds();
			for (String parentId : parentFolders) {
				HashSet<String> children = childrenMap.get(parentId);
				if (children.contains("dumyNode"))
					children.remove("dumyNode");
				children.add(id);
				allItemsMap.get(parentId).containsGenelists = true;
				if (genelist.isClustered()) {
					ArrayList<GenelistInfo> allClusters = AllComponentsGenelistAssembler.retrieveAllClusters(id);
					for (GenelistInfo cluster : allClusters)
					{				
						String clusterId = 	AllComponentsGenelistAssembler.createClusterId(id, cluster.getGenelistId());
						children.add(clusterId);
						cluster.setSubmitter(genelist.getSubmitter());
						cluster.setPlatformId(genelist.getPlatformId());
						String path = filename.substring(0, filename.lastIndexOf('/')+1);
						cluster.setFilename(path+cluster.getFilename());
						cluster.setCdtFileName("");
						cluster.setGtrFileName("");
						allItemsMap.put(clusterId, new GenelistTreeNode(clusterId, cluster));
						
					}						
				}
			}
			
  	     	allItemsMap.put(id, new GenelistTreeNode(id, genelist));
		}
		allItemsMap.put("dumyNode", new GenelistTreeNode("dumyNode"));
		String rootId = genelistsData[0][0];
		DefaultMutableTreeNode root = addTreeNode(rootId);
		highlightNonemptyNodes(root);
		childrenMap.clear();
		allItemsMap.clear();
		
		return root;
	}

	private boolean highlightNonemptyNodes(DefaultMutableTreeNode root) {
		boolean notEmpty = false;
		Iterator  children = root.children();
		while (children.hasNext()) {
			DefaultMutableTreeNode child = (DefaultMutableTreeNode)children.next();
			GenelistTreeNode childObject = (GenelistTreeNode)child.getUserObject();
			if (childObject.containsGenelists || childObject.isGenelistNode())
				notEmpty = true;
			else {
				childObject.containsGenelists = highlightNonemptyNodes(child);
				if (!notEmpty && childObject.containsGenelists) 
					notEmpty = true;
			}
		}
		return notEmpty;
	}
	
	private DefaultMutableTreeNode addTreeNode(String nodeId) {
		HashSet<String> children = childrenMap.get(nodeId);
		GenelistTreeNode node = allItemsMap.get(nodeId);
		DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode(node);
		if (children != null)
			for (String childId:children) 
				treeNode.insert(addTreeNode(childId));
		return treeNode;
	}

	private class NodeComparator implements Comparator {
		boolean ascending;
		public NodeComparator() {
			ascending = true;
		}  
		public NodeComparator(boolean ascending) {
			this.ascending = ascending;
		}  
//		public int compare(Object o1,Object o2) {
//			DefaultMutableTreeNode node1 = (DefaultMutableTreeNode)o1;
//			DefaultMutableTreeNode node2 = (DefaultMutableTreeNode)o2;
//			String value1 = ((GenelistTreeNode)node1.getUserObject()).getTitle();
//			String value2 = ((GenelistTreeNode)node2.getUserObject()).getTitle();
//			int compare;
//			if (node1.isLeaf())
//				if (node2.isLeaf())
//					compare = value1.compareTo(value2);
//				else
//					return 1;
//			else
//				if (node2.isLeaf())
//					return -1;
//				else
//					compare = value1.compareTo(value2);
//				
//			if (ascending) 
//				return compare;
//			else
//				return -compare;
//		}

		public int compare(Object o1,Object o2) {
			DefaultMutableTreeNode node1 = (DefaultMutableTreeNode)o1;
			DefaultMutableTreeNode node2 = (DefaultMutableTreeNode)o2;
			int value1 = ((GenelistTreeNode)node1.getUserObject()).getDisplayOrder();
			int value2 = ((GenelistTreeNode)node2.getUserObject()).getDisplayOrder();
			int compare = 0;
			if (node1.isLeaf())
				if (node2.isLeaf()) {
					if (value1 > value2) compare = 1;
					if (value1 == value2) compare = 0;
					if (value1 < value2) compare = -1;
				}
				else
					return 1;
			else
				if (node2.isLeaf())
					return -1;
				else {
					if (value1 > value2) compare = 1;
					if (value1 == value2) compare = 0;
					if (value1 < value2) compare = -1;
				}
				
			if (ascending) 
				return compare;
			else
				return -compare;
		}
	}
	
    private void sortTree(DefaultMutableTreeNode root, Comparator comparator) {
		if (root.isLeaf())
			return;

		Iterator childrenItrator = root.children();

		List<DefaultMutableTreeNode> childrenList = new ArrayList<DefaultMutableTreeNode>();
		while (childrenItrator.hasNext()) {
			DefaultMutableTreeNode nextChild = (DefaultMutableTreeNode) childrenItrator.next();
			childrenList.add(nextChild);
			root.remove(nextChild);
			childrenItrator = root.children(); // removing a node from root invalidates the iterator (it not possible to call the iterator remove method aslo since this collection in not modifiable)
		}

		Collections.sort(childrenList, comparator);

		for (DefaultMutableTreeNode nextChild : childrenList) {
			root.insert(nextChild);
			sortTree(nextChild, comparator);
		}
	}
    
	// ********************************************************************************
	// Action Methods
	// ********************************************************************************
/*    
	public String launchTreeView() {
		HashMap<String, String> params = TableUtil.getClickedLinkParams();		// had to use a separate link to click because direct link did not work (didn't fire action)
		genelistId = params.get("genelistId");
		cdtFile = params.get("cdtFile");
		gtrFile = params.get("gtrFile");
		if ((genelistId!=null && !genelistId.equals("")) || (cdtFile!=null && !cdtFile.equals(""))) 
			displayTreeView = true;

		return null;
	}
*/
    
	public String sortTreeAction() {
		String treeSortOrder = Visit.getRequestParam("treeSortOrder", "ascending");
		
		if (treeSortOrder==null || "descending".equals(treeSortOrder))
			treeSortOrder = "ascending";
		else
			treeSortOrder = "descending";
		Visit.setStatusParam("treeSortOrder", treeSortOrder);
		DefaultMutableTreeNode root = (DefaultMutableTreeNode)treeModel.getRoot();
		sortTree(root, new NodeComparator(treeSortOrder.equals("ascending")));
		treeModel.nodeStructureChanged(root);

/*		
        System.out.println("######>>"+treeModel.getClass());
        for (Iterator iterator = treeModel.getTreeModelListeners().iterator(); iterator.hasNext();)
        {
//            TreeModelListener listener = (TreeModelListener)iterator.next();
           System.out.println(iterator.next().getClass());
//           (HtmlTree.ModelListener)iterator.next()
//                event = new TreeModelEvent(source, path, childIndices, children);
//            listener.expandAll();
        }
*/		
		
		return null;
	}
	
	// ********************************************************************************
	// Getters & Setter
	// ********************************************************************************
/*	
public String getJavaTreeViewAppletCode() {
	HashMap<String, String> treeViewAppletParams = new HashMap<String, String>();
	treeViewAppletParams.put("genelistId", genelistId);
	treeViewAppletParams.put("cdtFile", cdtFile);
	treeViewAppletParams.put("gtrFile", gtrFile);
	treeViewAppletParams.put("dataSource", "genelist");
	return Utility.javaTreeViewAppletCode(treeViewAppletParams);
}
*/

    /**
	 * @return Returns the treeModel.
	 */
    public DefaultTreeModel getTreeModel() {
        return treeModel;
    }

    /**
     * @param treeModel The treeModel to set.
     */
    public void setTreeModel(DefaultTreeModel treeModel) {
        this.treeModel = treeModel;
    }

    public boolean isDisplayTreeView() {
		return displayTreeView;
	}

	public void setDisplayTreeView(boolean displayTreeView) {
		this.displayTreeView = displayTreeView;
	}

	public String getCdtFile() {
		return cdtFile;
	}

	public String getGtrFile() {
		return gtrFile;
	}

	public String getGenelistId() {
		return genelistId;
	}
	
}

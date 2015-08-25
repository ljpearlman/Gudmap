

package gmerg.beans;

import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException; 
import javax.faces.event.ActionEvent;
import javax.servlet.ServletContext;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import gmerg.entities.GenelistRnaSeqTreeInfo;

import gmerg.utils.DbUtility;

import gmerg.utils.Visit;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;



public class GeneListRnaSeqTreeBean implements Serializable
{
    protected boolean debug = false;

	private static final long serialVersionUID = 1L;
 

	private String genelistId;
	private String htmlFile;

	
    public GeneListRnaSeqTreeBean(){
    	createJSONFile();
    }

	public String getSelectedItem() {
		return genelistId;
	}

	public void setSelectedItem(String item) {
		genelistId = item;
	}

	public String getHtmlFile() {
		return htmlFile;
	}

	public void setHtmlFile(String htmlFile) {
		this.htmlFile = htmlFile;
	}
	
    public void processAction(ActionEvent event) throws AbortProcessingException,Exception {    
		if (debug){
			System.out.println("GeneListTreeRnaSeqBean:processAction");
			System.out.println("GeneListTreeRnaSeqBean:selecteditrm = " + genelistId);
			System.out.println("GeneListTreeRnaSeqBean:htmlFile = " + htmlFile);
		}
		
		
    }

    public String findNode()
    {   
    	if (debug)
    		System.out.println("GeneListTreeRnaSeqBean:findNode");
		
		if (genelistId.equalsIgnoreCase("0"))
			return null;

		return "leaf";
    }
    
	public String getSearchParams(){

		String urlParams = null;
		HashMap<String, String> params = new HashMap<String, String>();		
    	params.put("genelistId", genelistId);
    	params.put("htmlFile", htmlFile);
    	params.put("cleartabs", "true");
    	urlParams = Visit.packParams(params);	
    	
		if(debug)
			System.out.println("getSearchParams:urlParams: " + urlParams);
				
		if (urlParams==null)
			return "";
		return "?" + urlParams;

	}

    public String getResultURL () {
//    	String result = "mastertable_browse.html?genelistId="+ genelistId + "&masterTableId="+ htmlFile + "&cleartabs=true";
    	String result = "htmlFile";
    	
    	if (debug)
    		System.out.println("getResultURL:result: " + result);
    	
        return result;
    }

	public String getTitle() {


		return "RNASEQ from Microarray Analysis";
	}
	
	private void createJSONFile(){
		ArrayList<GenelistRnaSeqTreeInfo> genelist = DbUtility.getRefGenelistsRnaSeq();
		createJSONObject(genelist);
	}

	private void createJSONObject(ArrayList<GenelistRnaSeqTreeInfo> genelist){
		
		JSONObject obj = new JSONObject();
		
		
//		obj.put("children", createIsPublished(genelist));
		
		JSONArray  outerlist = new JSONArray();

		outerlist.add(createUnpublished(genelist));		

		try{
			ServletContext ctx = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
			String realPath = ctx.getRealPath("/");
			String path = realPath + "/scripts/genelistrnaseq.json";
			
			if (debug)
			System.out.println("path = "+path);
			
			FileWriter file = new FileWriter(path);
			file.write(outerlist.toJSONString());
			file.flush();
			file.close();
		}
		catch(IOException e){
			e.printStackTrace();
		}
				
	
	}
	
	private JSONArray createIsPublished(ArrayList<GenelistRnaSeqTreeInfo> genelist){

		JSONArray  plist = new JSONArray();

		// published datasets
//		JSONObject obj = new JSONObject();

//		JSONObject attr = new JSONObject();

		ArrayList<GenelistRnaSeqTreeInfo> unpublishedGenelist = new ArrayList<GenelistRnaSeqTreeInfo>();
		for (GenelistRnaSeqTreeInfo inf : genelist){
			if (inf.getPublished().equalsIgnoreCase("0"))
				unpublishedGenelist.add(inf);				
		}
						
//		JSONArray  list = new JSONArray();

		
		// unpublished datasets
		JSONObject obj = new JSONObject();
		obj.put("data", "Unpublished");
		obj.put("state", "open");

		JSONObject attr = new JSONObject();
		attr.put("id", -2000);
		obj.put("attr", attr);
		

		ArrayList<String> unpublishedDatasets = new ArrayList<String>();
		for(GenelistRnaSeqTreeInfo inf : genelist){
			if(inf.getPublished().equalsIgnoreCase("0")){
				if(!unpublishedDatasets.contains(inf.getGlsClass())){ 
					unpublishedDatasets.add(inf.getGlsClass());
				}
			}
		}
		Collections.sort(unpublishedDatasets);

		JSONArray list = new JSONArray();
		int id = -20000;
		for(String dataset : unpublishedDatasets){
			id = id - 1;
			list.add(createUnpublishedDatasets(unpublishedGenelist, dataset, id));
		}				
		obj.put("children", list);
		
		plist.add(obj);
		
		return plist;		
	
	}

	private JSONObject createUnpublished(ArrayList<GenelistRnaSeqTreeInfo> glslist){

		ArrayList<GenelistRnaSeqTreeInfo> genelist = new ArrayList<GenelistRnaSeqTreeInfo>();
		for (GenelistRnaSeqTreeInfo inf : glslist){
			if (inf.getPublished().equalsIgnoreCase("0"))
				genelist.add(inf);				
		}

		
		// unpublished datasets
		JSONObject obj = new JSONObject();
		obj.put("data", "Unpublished");
		obj.put("state", "closed");

		JSONObject attr = new JSONObject();
		attr.put("id", -2000);
		obj.put("attr", attr);
		

		ArrayList<String> glsclasses = new ArrayList<String>();
		for(GenelistRnaSeqTreeInfo inf : genelist){
			if(inf.getPublished().equalsIgnoreCase("0")){
				String glsclass = inf.getGlsClass();
				if(!glsclasses.contains(glsclass)){ 
					glsclasses.add(glsclass);
				}
			}
		}
		Collections.sort(glsclasses);

		JSONArray list = new JSONArray();
		int id = -20000;
		for(String item : glsclasses){
			id = id - 100;
			list.add(createUnpublishedDatasets2(genelist, item, id));
		}				
		obj.put("children", list);
		
		return obj;		
	
	}

	private JSONObject createUnpublishedDatasets(ArrayList<GenelistRnaSeqTreeInfo> genelist, String glsclass, int id){
	    System.out.println("createUnpublishedDatasets dataset = "+ glsclass);
		
		JSONObject obj = new JSONObject();		
		obj.put("data", glsclass);
		obj.put("state", "closed");
		
		JSONObject attr = new JSONObject();
		attr.put("id", id);
		attr.put("title", "Dataset = "+glsclass);
		obj.put("attr", attr);

		ArrayList<String> subclasses = new ArrayList<String>();
		ArrayList<GenelistRnaSeqTreeInfo> unnamedsamples = new ArrayList<GenelistRnaSeqTreeInfo>();
		for(GenelistRnaSeqTreeInfo inf : genelist){
			if(glsclass.equalsIgnoreCase(inf.getGlsClass())){
				String subclass = inf.getSubClass();
				if (!subclasses.contains(subclass)){
				    System.out.println("createUnpublishedDatasets subclass = "+ subclass);
					subclasses.add(subclass);
				}
			}
		}
		Collections.sort(subclasses);

		
		
		JSONArray  list = new JSONArray();
		

		for(String subclass : subclasses){
			int id2 = id - 10;
			list.add(createSubclass(genelist, glsclass, subclass, id2));	
		}				
		obj.put("children", list);
				
		return obj;
	}	

	private JSONObject createUnpublishedDatasets2(ArrayList<GenelistRnaSeqTreeInfo> genelist, String glsclass, int id){
	    System.out.println("createUnpublishedDatasets dataset = "+ glsclass);
		
		JSONObject obj = new JSONObject();		
		obj.put("data", glsclass);
		obj.put("state", "closed");
		
		JSONObject attr = new JSONObject();
		attr.put("id", id);
		attr.put("title", "Dataset = "+glsclass);
		obj.put("attr", attr);

		ArrayList<String> clusters = new ArrayList<String>();
		for(GenelistRnaSeqTreeInfo inf : genelist){
			if(glsclass.equalsIgnoreCase(inf.getGlsClass())){
				String cluster = inf.getCluster();
			    System.out.println("createClusters cluster = "+ cluster);
			    clusters.add(cluster);
			}
		}

		
		
		JSONArray  list = new JSONArray();
		

		for(String cluster : clusters){
			int id2 = id - 10;
			list.add(createClusters2(genelist, glsclass, cluster, id2));	
		}				
		obj.put("children", list);
				
		return obj;
	}	
	
	private JSONObject createSubclass(ArrayList<GenelistRnaSeqTreeInfo> genelist, String glsclass, String subclass, int id){

		JSONObject obj = new JSONObject();		
		obj.put("data", subclass);
		obj.put("state", "closed");
		
		JSONObject attr = new JSONObject();
		attr.put("id", id);
		attr.put("title", "Dataset = "+subclass);
		obj.put("attr", attr);
		
		ArrayList<String> clusters = new ArrayList<String>();
		for(GenelistRnaSeqTreeInfo inf : genelist){
			if(glsclass.equalsIgnoreCase(inf.getGlsClass()) && subclass.equalsIgnoreCase(inf.getSubClass())){
				String cluster = inf.getCluster();
			    System.out.println("createClusters cluster = "+ cluster);
			    clusters.add(cluster);
			}
		}
		
		JSONArray  list = new JSONArray();
		
		int id2 = -12;
		for(String cluster : clusters){
			id2 = id2-1;
			list.add(createClusters(genelist, glsclass, subclass, cluster, id2));	
		}				
		obj.put("children", list);
				
		return obj;
	}

	private JSONObject createClusters(ArrayList<GenelistRnaSeqTreeInfo> genelist, String glsclass, String subclass, String cluster, int id){
		System.out.println("glsclass = "+ glsclass );
		System.out.println("subclass = "+ subclass );
		System.out.println("cluster = "+ cluster );

		JSONObject obj = new JSONObject();		
		obj.put("data", cluster);
		obj.put("state", "closed");
		
		JSONObject attr = new JSONObject();
		attr.put("id", id);
		attr.put("title", "Dataset = "+cluster);
		obj.put("attr", attr);
		
		
		JSONArray  list = new JSONArray();
		ArrayList<GenelistRnaSeqTreeInfo> ids = new ArrayList<GenelistRnaSeqTreeInfo>();
		for(GenelistRnaSeqTreeInfo inf : genelist){
			if(glsclass.equalsIgnoreCase(inf.getGlsClass()) && subclass.equalsIgnoreCase(inf.getSubClass()) && cluster.equalsIgnoreCase(inf.getCluster())){
				if(!ids.contains(inf.getGenelistOID()))
					ids.add(inf);
			}
		}
		
		list.add(createLeaf(ids));	
			
		obj.put("children", list);
				
		return obj;
	}

	private JSONObject createClusters2(ArrayList<GenelistRnaSeqTreeInfo> genelist, String glsclass, String cluster, int id){
		System.out.println("glsclass = "+ glsclass );
		System.out.println("cluster = "+ cluster );

		JSONObject obj = new JSONObject();		
//		obj.put("data", cluster);
//		obj.put("state", "closed");
//		
//		JSONObject attr = new JSONObject();
//		attr.put("id", id);
//		attr.put("title", "Dataset = "+cluster);
//		obj.put("attr", attr);
		
		
		JSONArray  list = new JSONArray();
		ArrayList<GenelistRnaSeqTreeInfo> ids = new ArrayList<GenelistRnaSeqTreeInfo>();
		for(GenelistRnaSeqTreeInfo inf : genelist){
			if(glsclass.equalsIgnoreCase(inf.getGlsClass()) && cluster.equalsIgnoreCase(inf.getCluster())){
				if(!ids.contains(inf.getGenelistOID()))
					ids.add(inf);
			}
		}
		
//		list.add(createLeaf(ids));	
			
//		obj.put("children", list);
				
		return createLeaf(ids);//;obj;
	}
	
	private JSONObject createLeaf(ArrayList<GenelistRnaSeqTreeInfo> ids){
		
		
//		JSONArray  list = new JSONArray();
		JSONObject obj = new JSONObject();

		for(GenelistRnaSeqTreeInfo inf : ids){
			obj.put("data", inf.getShortName() + "(" + inf.getGeneCount()  + " genes)");
			String file = inf.getShortName() + "_Full.html";
				
			String path = "http://www.gudmap.org/CellData/203Cells_V10_BA/";
			
			JSONObject attr = new JSONObject();
			attr.put("id", inf.getGenelistOID());
			attr.put("title", "Genelist = "+inf.getShortName());
			attr.put("rel", "Role");
			attr.put("file", path + file);
			obj.put("attr", attr);
			
//			list.add(obj);					
		}
		
				
		return obj;
	}
	
}



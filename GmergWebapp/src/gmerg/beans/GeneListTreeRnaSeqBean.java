

package gmerg.beans;

import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException; 
import javax.faces.event.ActionEvent;
import javax.servlet.ServletContext;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import gmerg.entities.GenelistRnaSeqTreeInfo;
import gmerg.entities.GenelistRnaSeqTreeInfo;
import gmerg.utils.DbUtility;
import gmerg.utils.FacesUtil;
import gmerg.utils.Visit;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;



public class GeneListTreeRnaSeqBean implements Serializable
{
    protected boolean debug = false;

	private static final long serialVersionUID = 1L;
 

	private String genelistId;
	private String masterTableId;

	
    public GeneListTreeRnaSeqBean(){
    	createJSONFile();
    }

	public String getSelectedItem() {
		return genelistId;
	}

	public void setSelectedItem(String item) {
		genelistId = item;
	}

	public String getMasterTableId() {
		return masterTableId;
	}

	public void setMasterTableId(String masterTableId) {
		this.masterTableId = masterTableId;
	}
	
    public void processAction(ActionEvent event) throws AbortProcessingException,Exception {    
		if (debug)
			System.out.println("GeneListTreeRnaSeqBean:processAction");
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
    	params.put("masterTableId", masterTableId);
    	params.put("cleartabs", "true");
    	urlParams = Visit.packParams(params);	
    	
		if(debug)
			System.out.println("getSearchParams:urlParams: " + urlParams);
				
		if (urlParams==null)
			return "";
		return "?" + urlParams;

	}

    public String getResultURL () {
    	String result = "mastertable_browse.html?genelistId="+ genelistId + "&masterTableId="+ masterTableId + "&cleartabs=true";
    	
    	if (debug)
    		System.out.println("getResultURL:result: " + result);
    	
        return result;
    }

	public String getTitle() {
//    	createJSONFile();

//		return "Microarray Analysis (gene list)";  
		return "RNASEQ from Microarray Analysis";
	}
	
	private void createJSONFile(){
		ArrayList<GenelistRnaSeqTreeInfo> genelist = DbUtility.getRefGenelistsRnaSeq();
		createJSONObject(genelist);
	}

	private void createJSONObject(ArrayList<GenelistRnaSeqTreeInfo> genelist){
		
		JSONObject obj = new JSONObject();
//		obj.put("data", "Datasets");
//		obj.put("state", "open");

//		JSONObject attr = new JSONObject();
//		attr.put("id", 0);
//		obj.put("attr", attr);
		
		
		obj.put("children", createIsPublished(genelist));
		
		JSONArray  outerlist = new JSONArray();
//		outerlist.add(obj);
		outerlist.add(createPublished(genelist));
		outerlist.add(createUnpublished(genelist));
		
//		System.out.println(outerlist);

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
		JSONObject obj = new JSONObject();
		obj.put("data", "Published");
		obj.put("state", "open");

		JSONObject attr = new JSONObject();
		attr.put("id", -1000);
		obj.put("attr", attr);

		ArrayList<GenelistRnaSeqTreeInfo> publishedGenelist = new ArrayList<GenelistRnaSeqTreeInfo>();
		ArrayList<GenelistRnaSeqTreeInfo> unpublishedGenelist = new ArrayList<GenelistRnaSeqTreeInfo>();
		for (GenelistRnaSeqTreeInfo inf : genelist){
			if (inf.getPublished().equalsIgnoreCase("1"))
				publishedGenelist.add(inf);
			else
				unpublishedGenelist.add(inf);				
		}
				
		ArrayList<String> publishedDatasets = new ArrayList<String>();
		for(GenelistRnaSeqTreeInfo inf : genelist){
			if(inf.getPublished().equalsIgnoreCase("1")){
				if(!publishedDatasets.contains(inf.getGlsClass())){ 
					publishedDatasets.add(inf.getGlsClass());
				}
			}
		}
		
		JSONArray  list = new JSONArray();
		int id = -10000;
		for(String dataset : publishedDatasets){
			id = id - 1;
			list.add(createPublishedDatasets(publishedGenelist, dataset, id));
		}				
		obj.put("children", list);
		
		plist.add(obj);
		
		// unpublished datasets
		obj = new JSONObject();
		obj.put("data", "Unpublished");
		obj.put("state", "open");

		attr = new JSONObject();
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

		list = new JSONArray();
		id = -20000;
		for(String dataset : unpublishedDatasets){
			id = id - 1;
			list.add(createUnpublishedDatasets(unpublishedGenelist, dataset, id));
		}				
		obj.put("children", list);
		
		plist.add(obj);
		
		return plist;		
	
	}
	private JSONObject createPublished(ArrayList<GenelistRnaSeqTreeInfo> genelist){

		// published datasets
		JSONObject obj = new JSONObject();
		obj.put("data", "Published");
		obj.put("state", "open");

		JSONObject attr = new JSONObject();
		attr.put("id", -1000);
		obj.put("attr", attr);

		ArrayList<GenelistRnaSeqTreeInfo> publishedGenelist = new ArrayList<GenelistRnaSeqTreeInfo>();
		for (GenelistRnaSeqTreeInfo inf : genelist){
			if (inf.getPublished().equalsIgnoreCase("1"))
				publishedGenelist.add(inf);
		}
				
		ArrayList<String> publishedDatasets = new ArrayList<String>();
		for(GenelistRnaSeqTreeInfo inf : genelist){
			if(inf.getPublished().equalsIgnoreCase("1")){
				if(!publishedDatasets.contains(inf.getGlsClass())){ 
					publishedDatasets.add(inf.getGlsClass());
				}
			}
		}
		
		JSONArray  list = new JSONArray();
		int id = -10000;
		for(String dataset : publishedDatasets){
			id = id - 1;
			list.add(createPublishedDatasets(publishedGenelist, dataset, id));
		}				
		obj.put("children", list);
		
		return obj;
		
	
	}
	private JSONObject createUnpublished(ArrayList<GenelistRnaSeqTreeInfo> genelist){

		ArrayList<GenelistRnaSeqTreeInfo> unpublishedGenelist = new ArrayList<GenelistRnaSeqTreeInfo>();
		for (GenelistRnaSeqTreeInfo inf : genelist){
			if (inf.getPublished().equalsIgnoreCase("0"))
				unpublishedGenelist.add(inf);				
		}

		
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
		
		return obj;		
	
	}

	private JSONObject createUnpublishedDatasets(ArrayList<GenelistRnaSeqTreeInfo> genelist, String dataset, int id){
		
		JSONObject obj = new JSONObject();		
		obj.put("data", dataset);
		obj.put("state", "closed");
		
		JSONObject attr = new JSONObject();
		attr.put("id", id);
		attr.put("title", "Dataset = "+dataset);
		obj.put("attr", attr);

		ArrayList<String> subclasses = new ArrayList<String>();
		ArrayList<GenelistRnaSeqTreeInfo> unnamedsamples = new ArrayList<GenelistRnaSeqTreeInfo>();
		for(GenelistRnaSeqTreeInfo inf : genelist){
			if(dataset.equalsIgnoreCase(inf.getGlsClass())){
				String subclass = inf.getSubClass();
//			    System.out.println("createUnpublishedDatasets sample = "+ sample + " , dataset = "+ dataset);
				subclasses.add(subclass);
			}
		}
		Collections.sort(subclasses);

		
		
		JSONArray  list = new JSONArray();
		
		int id2 = -10;
		for(String subclass : subclasses){
			id2 = id2-1;
			list.add(createClusters(genelist, dataset, subclass, id2));	
		}				
		obj.put("children", list);
				
		return obj;
	}	


	private JSONObject createClusters(ArrayList<GenelistRnaSeqTreeInfo> genelist, String glsclass, String subclass, int id){

		JSONObject obj = new JSONObject();
		for(GenelistRnaSeqTreeInfo inf : genelist){
			if(glsclass.equalsIgnoreCase(inf.getGlsClass()) && subclass.equalsIgnoreCase(inf.getSubClass())){
				if (debug)
					System.out.println("sample for "+ glsclass + ","+ stage + " = " + inf.getSample());
				obj = new JSONObject();
				obj.put("data", inf.getName());
				JSONObject attr1 = new JSONObject();
				attr1.put("id", inf.getGenelistOID());
				String table = "";
				if (inf.getDatasetId().contentEquals("5") || inf.getDatasetId().contentEquals("6") || inf.getDatasetId().contentEquals("7"))
					table = "4_" + inf.getDatasetId();
				else
					table = "3_" + inf.getDatasetId();
				attr1.put("table", table);
				attr1.put("rel", "Role");							
				attr1.put("title", "Dataset = "+ dataset +  ", " + stage);
				obj.put("attr", attr1);
				return obj;
			}
		}	

		
		obj.put("data", sample);
		obj.put("state", "closed");
		
		JSONObject attr = new JSONObject();
		attr.put("id",id);
		attr.put("title", "Dataset = "+ dataset + ", Sample = " +sample);
		obj.put("attr", attr);


		ArrayList<GenelistRnaSeqTreeInfo> allList = new ArrayList<GenelistRnaSeqTreeInfo>();
		for(GenelistRnaSeqTreeInfo inf : genelist){
//			if(dataset.equalsIgnoreCase(inf.getDataset()) && stage.equalsIgnoreCase(inf.getStage()) && sample.equalsIgnoreCase(inf.getSample())){
			if(dataset.equalsIgnoreCase(inf.getGlsClass()) && sample.equalsIgnoreCase(inf.getSample())){
				if(!allList.contains(inf.getSubset2()))
					allList.add(inf);
			}
		}

		JSONArray  list = new JSONArray();
		list = createLevelAll(allList);
		obj.put("children", list);
				
		return obj;
	}
	
	private JSONArray createLevelAll(ArrayList<GenelistRnaSeqTreeInfo> genelist){

		JSONArray  list = new JSONArray();
		
		ArrayList<GenelistRnaSeqTreeInfo> allList = new ArrayList<GenelistRnaSeqTreeInfo>();
		for(GenelistRnaSeqTreeInfo inf : genelist){
//			System.out.println("Subset2 = " + inf.getSubset2() + " , " + inf.getGenelistOID());
			if (inf.getSubset2() != null){
				if(!allList.contains(inf.getSubset2()) && inf.getSubset2().equalsIgnoreCase("All"))
					allList.add(inf);
			}
			else{
				JSONObject obj = new JSONObject();		
				obj.put("data", inf.getName() + "(" + inf.getEntityCount() + " probes, " + inf.getGeneCount() + " genes)");
				String table = "";
				if (inf.getDatasetId().contentEquals("5") || inf.getDatasetId().contentEquals("6") || inf.getDatasetId().contentEquals("7"))
					table = "4_" + inf.getDatasetId();
				else
					table = "3_" + inf.getDatasetId();
				JSONObject attr = new JSONObject();
				
				attr.put("id", inf.getGenelistOID());
				attr.put("table", table);
				attr.put("rel", "Role");
				attr.put("title", "Dataset = "+ inf.getDataset() + ", " + inf.getGenelistType() + ", " + inf.getAuthor());
				obj.put("attr", attr);
				System.out.println("object = "+ obj);
				list.add(obj);

			}
		}
		
		for (GenelistRnaSeqTreeInfo all : allList){
			JSONObject obj = new JSONObject();		
			obj.put("data", all.getName() + "(" + all.getEntityCount() + " probes, " + all.getGeneCount() + " genes)");
			obj.put("state", "closed");
			String table = "";
			if (all.getDatasetId().contentEquals("5") || all.getDatasetId().contentEquals("6") || all.getDatasetId().contentEquals("7"))
				table = "4_" + all.getDatasetId();
			else
				table = "3_" + all.getDatasetId();
			
			JSONObject attr = new JSONObject();
			attr.put("id", all.getGenelistOID());
			attr.put("table", table);
			attr.put("rel", "Role");
			attr.put("title", "Dataset = "+ all.getGlsClass() + ", Sample = " + all.getSample() + ", " + all.getStage() + ", " + all.getGenelistType() + ", " + all.getAuthor());
			obj.put("attr", attr);

			JSONArray  clusterlist = new JSONArray();
			clusterlist = createLevelCluster(genelist, all.getEntityCount());
			obj.put("children", clusterlist);
					
			list.add(obj);
		}
		return list;
	}

	private JSONArray createLevelCluster(ArrayList<GenelistRnaSeqTreeInfo> genelist, String count){

		ArrayList<GenelistRnaSeqTreeInfo> kList = new ArrayList<GenelistRnaSeqTreeInfo>();
		for(GenelistRnaSeqTreeInfo inf : genelist){
			if (inf.getSubset1() != null){
				if(!kList.contains(inf.getSubset1()) && inf.getSubset1().equalsIgnoreCase(count) && !inf.getSubset2().equalsIgnoreCase("All"))
					kList.add(inf);
			}
		}
		JSONArray  list = new JSONArray();
		
		for (GenelistRnaSeqTreeInfo k : kList){
			JSONObject obj = new JSONObject();		
			obj.put("data", k.getName() + "(" + k.getEntityCount() + " probes, " + k.getGeneCount() + " genes)");
			String table = "";
			if (k.getDatasetId().contentEquals("5") || k.getDatasetId().contentEquals("6") || k.getDatasetId().contentEquals("7"))
				table = "4_" + k.getDatasetId();
			else
				table = "3_" + k.getDatasetId();
			
//					System.out.println("data = " + k.getName() + ", tableid = "+ table);
			
			JSONObject attr = new JSONObject();
			attr.put("id", k.getGenelistOID());
			attr.put("table", table);
			attr.put("title", "Dataset = "+ k.getGlsClass() + ", Sample = " + k.getSample() + ", " + k.getStage() + ", " + k.getGenelistType() + ", " + k.getAuthor());
			obj.put("attr", attr);
					
			list.add(obj);
		}
		return list;
	}

	
	
	private JSONObject createPublishedDatasets(ArrayList<GenelistRnaSeqTreeInfo> genelist, String dataset, int id){
		
		JSONObject obj = new JSONObject();		
		obj.put("data", dataset);
		obj.put("state", "open");
		
		JSONObject attr = new JSONObject();
		attr.put("id", id);
		attr.put("title", "Dataset = "+dataset);
		obj.put("attr", attr);

		ArrayList<String> publishers = new ArrayList<String>();
		for(GenelistRnaSeqTreeInfo inf : genelist){
			if(dataset.equalsIgnoreCase(inf.getGlsClass())){
				if(!publishers.contains(inf.getLpuRef()) && inf.getLpuRef() != null)
					publishers.add(inf.getLpuRef());
			}
		}
		Collections.sort(publishers);
						
		JSONArray  list = new JSONArray();

		for(String publisher : publishers){
//			System.out.println("publisher = " + publisher);
			id = id-100;
			list.add(createPublisher(genelist, dataset, publisher, id));					
		}				
		obj.put("children", list);
				
		return obj;
	}

	private JSONObject createPublisher(ArrayList<GenelistRnaSeqTreeInfo> genelist, String dataset, String publisher, int id){
		
		JSONObject obj = new JSONObject();		
		obj.put("data", publisher);
		obj.put("state", "closed");
		
		JSONObject attr = new JSONObject();
		attr.put("id", id);
//		attr.put("title", "Dataset = "+dataset);
		obj.put("attr", attr);

		ArrayList<GenelistRnaSeqTreeInfo> names = new ArrayList<GenelistRnaSeqTreeInfo>();
		for(GenelistRnaSeqTreeInfo inf : genelist){
			if(dataset.equalsIgnoreCase(inf.getGlsClass()) && publisher.equalsIgnoreCase(inf.getLpuRef())){
				if(!names.contains(inf.getName()))
					names.add(inf);
			}
		}

		JSONArray  list = new JSONArray();
		
		list = createPublishedNames(names);
		obj.put("children", list);
				
		return obj;
	}

	private JSONObject createPublisher2(ArrayList<GenelistRnaSeqTreeInfo> genelist, String dataset, String publisher, int id){
		
		JSONObject obj = new JSONObject();		
		obj.put("data", publisher);
		obj.put("state", "closed");
		
		JSONObject attr = new JSONObject();
		attr.put("id", id);
//		attr.put("title", "Dataset = "+dataset);
		obj.put("attr", attr);

		ArrayList<String> stages = new ArrayList<String>();
		for(GenelistRnaSeqTreeInfo inf : genelist){
			if(dataset.equalsIgnoreCase(inf.getGlsClass()) && publisher.equalsIgnoreCase(inf.getLpuRef())){
				if(!stages.contains(inf.getStage()) && !inf.getStage().trim().contentEquals("0"))
					stages.add(inf.getStage());
			}
		}
		Collections.sort(stages);

		JSONArray  list = new JSONArray();
		
		// put multiple stages here outside the stage node
		String redundantStage = "";
		for(String stage1 : stages){
			if (stage1.length() > 4){
				for(GenelistRnaSeqTreeInfo inf : genelist){
					if(dataset.equalsIgnoreCase(inf.getGlsClass()) && publisher.equalsIgnoreCase(inf.getLpuRef()) && stage1.equalsIgnoreCase(inf.getStage())){						
						JSONObject obj1 = new JSONObject();
						obj1.put("data", inf.getName());
						JSONObject attr1 = new JSONObject();
						attr1.put("id", inf.getGenelistOID());
						String table = "";
						if (inf.getDatasetId().contentEquals("5") || inf.getDatasetId().contentEquals("6") || inf.getDatasetId().contentEquals("7"))
							table = "4_" + inf.getDatasetId();
						else
							table = "3_" + inf.getDatasetId();
						attr1.put("table", table);
						attr1.put("rel", "Role");
						attr1.put("title", "Dataset = "+ inf.getGlsClass() + ", Sample = " + inf.getName() + ", " + inf.getStage() + ", " + inf.getLpuRef() + ", " + inf.getAuthor());
						obj1.put("attr", attr1);
						list.add(obj1);
						redundantStage = stage1;
					}
					
				}
			}
		}
		if (redundantStage.length() > 0)
			stages.remove(redundantStage);
			
		for(String stage : stages){
//			System.out.println("stage = " + stage);
			id = id-1;
			list.add(createPublishedStages(genelist, dataset, publisher, stage, id));					
		}				
		obj.put("children", list);
				
		return obj;
	}
	
	private JSONObject createPublishedStages(ArrayList<GenelistRnaSeqTreeInfo> genelist, String dataset, String publisher, String stage, int id){

		JSONObject obj = new JSONObject();
		
		obj.put("data", stage);
		obj.put("state", "closed");
		
		JSONObject attr = new JSONObject();
		attr.put("id",id);				
		attr.put("title", "Dataset = "+ dataset + ", " + stage);
		obj.put("attr", attr);

		ArrayList<GenelistRnaSeqTreeInfo> names = new ArrayList<GenelistRnaSeqTreeInfo>();
		for(GenelistRnaSeqTreeInfo inf : genelist){
			if(dataset.equalsIgnoreCase(inf.getGlsClass()) && publisher.equalsIgnoreCase(inf.getLpuRef()) && stage.equalsIgnoreCase(inf.getStage())){
				if(!names.contains(inf.getName()))
					names.add(inf);
			}
		}
		
		JSONArray  list = new JSONArray();
		list = createPublishedNames(names);
		obj.put("children", list);
				
		return obj;
	}

	private JSONArray createPublishedNames(ArrayList<GenelistRnaSeqTreeInfo> genelist){
	
		JSONArray  list = new JSONArray();
		
		for (GenelistRnaSeqTreeInfo inf : genelist){
			JSONObject obj = new JSONObject();		
			//obj.put("data", inf.getName());
			obj.put("data", inf.getName() + "(" + inf.getEntityCount() + " probes, " + inf.getGeneCount() + " genes)");
			
			JSONObject attr = new JSONObject();
			attr.put("id", inf.getGenelistOID());
			String table = "";
			if (inf.getDatasetId().contentEquals("5") || inf.getDatasetId().contentEquals("6") || inf.getDatasetId().contentEquals("7"))
				table = "4_" + inf.getDatasetId();
			else
				table = "3_" + inf.getDatasetId();
			attr.put("table", table);
			attr.put("rel", "Role");
			attr.put("title", "Dataset = "+ inf.getGlsClass() + ", Sample = " + inf.getName() + ", " + inf.getStage() + ", " + inf.getLpuRef() + ", " + inf.getAuthor());
			obj.put("attr", attr);
					
			list.add(obj);
		}
		return list;
	}
	
}



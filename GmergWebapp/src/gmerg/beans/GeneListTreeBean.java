

package gmerg.beans;

import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException; 
import javax.faces.event.ActionEvent;
import javax.servlet.ServletContext;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import gmerg.entities.GenelistTreeInfo;
import gmerg.utils.DbUtility;
import gmerg.utils.FacesUtil;
import gmerg.utils.Visit;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;



public class GeneListTreeBean implements Serializable
{

	private static final long serialVersionUID = 1L;
 

	private String genelistId;
	private String masterTableId;

	
    public GeneListTreeBean(){
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
		
		System.out.println("GeneListTreeBean:processAction");
    }

    public String findNode()
    {    	
    	System.out.println("GeneListTreeBean:findNode");
		
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
    	
				
		if (urlParams==null)
			return "";
		System.out.println("getSearchParams:urlParams: " + urlParams);
		return "?" + urlParams;

	}

    public String getResultURL () {
    	String result = "mastertable_browse.html?genelistId="+ genelistId + "&masterTableId="+ masterTableId + "&cleartabs=true";
    	System.out.println("getSearchParams:result: " + result);
        return result;
    }

	public String getTitle() {
//    	createJSONFile();

//		return "Microarray Analysis (gene list)";  
		return "Genelists from Microarray Analysis";
	}
	
	private void createJSONFile(){
		ArrayList<GenelistTreeInfo> genelist = DbUtility.getRefGenelists();
		createJSONObject(genelist);
	}

	private void createJSONObject(ArrayList<GenelistTreeInfo> genelist){
		
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
			String path = realPath + "/scripts/genelist.json";
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
	
	private JSONArray createIsPublished(ArrayList<GenelistTreeInfo> genelist){

		JSONArray  plist = new JSONArray();

		// published datasets
		JSONObject obj = new JSONObject();
		obj.put("data", "Published");
		obj.put("state", "open");

		JSONObject attr = new JSONObject();
		attr.put("id", -1000);
		obj.put("attr", attr);

		ArrayList<GenelistTreeInfo> publishedGenelist = new ArrayList<GenelistTreeInfo>();
		ArrayList<GenelistTreeInfo> unpublishedGenelist = new ArrayList<GenelistTreeInfo>();
		for (GenelistTreeInfo inf : genelist){
			if (inf.getPublished().equalsIgnoreCase("1"))
				publishedGenelist.add(inf);
			else
				unpublishedGenelist.add(inf);				
		}
				
		ArrayList<String> publishedDatasets = new ArrayList<String>();
		for(GenelistTreeInfo inf : genelist){
			if(inf.getPublished().equalsIgnoreCase("1")){
				if(!publishedDatasets.contains(inf.getDataset())){ 
					publishedDatasets.add(inf.getDataset());
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
		for(GenelistTreeInfo inf : genelist){
			if(inf.getPublished().equalsIgnoreCase("0")){
				if(!unpublishedDatasets.contains(inf.getDataset())){ 
					unpublishedDatasets.add(inf.getDataset());
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
	private JSONObject createPublished(ArrayList<GenelistTreeInfo> genelist){

		// published datasets
		JSONObject obj = new JSONObject();
		obj.put("data", "Published");
		obj.put("state", "open");

		JSONObject attr = new JSONObject();
		attr.put("id", -1000);
		obj.put("attr", attr);

		ArrayList<GenelistTreeInfo> publishedGenelist = new ArrayList<GenelistTreeInfo>();
		for (GenelistTreeInfo inf : genelist){
			if (inf.getPublished().equalsIgnoreCase("1"))
				publishedGenelist.add(inf);
		}
				
		ArrayList<String> publishedDatasets = new ArrayList<String>();
		for(GenelistTreeInfo inf : genelist){
			if(inf.getPublished().equalsIgnoreCase("1")){
				if(!publishedDatasets.contains(inf.getDataset())){ 
					publishedDatasets.add(inf.getDataset());
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
	private JSONObject createUnpublished(ArrayList<GenelistTreeInfo> genelist){

		ArrayList<GenelistTreeInfo> unpublishedGenelist = new ArrayList<GenelistTreeInfo>();
		for (GenelistTreeInfo inf : genelist){
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
		for(GenelistTreeInfo inf : genelist){
			if(inf.getPublished().equalsIgnoreCase("0")){
				if(!unpublishedDatasets.contains(inf.getDataset())){ 
					unpublishedDatasets.add(inf.getDataset());
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

	private JSONObject createUnpublishedDatasets(ArrayList<GenelistTreeInfo> genelist, String dataset, int id){
		
		JSONObject obj = new JSONObject();		
		obj.put("data", dataset);
		obj.put("state", "closed");
		
		JSONObject attr = new JSONObject();
		attr.put("id", id);
		attr.put("title", "Dataset = "+dataset);
		obj.put("attr", attr);

		ArrayList<String> samples = new ArrayList<String>();
		ArrayList<GenelistTreeInfo> unnamedsamples = new ArrayList<GenelistTreeInfo>();
		for(GenelistTreeInfo inf : genelist){
			if(dataset.equalsIgnoreCase(inf.getDataset())){
				String sample = inf.getSample();
				if(sample.isEmpty()){
					unnamedsamples.add(inf);
				}
				else if(!samples.contains(sample)){
//					System.out.println("createUnpublishedDatasets sample = "+ sample + " , dataset = "+ dataset);
						samples.add(sample);
				}
			}
		}
		Collections.sort(samples);

		
		
		JSONArray  list = new JSONArray();
		
		JSONArray l1 = new JSONArray();
		list.addAll(createLevelAll(unnamedsamples));
		
		int id2 = -10;
		for(String sample : samples){
			id2 = id2-1;
			list.add(createSamples(genelist, dataset, "", sample, id2));	
//			}
		}				
		obj.put("children", list);
				
		return obj;
	}	
	private JSONObject createUnpublishedDatasets2(ArrayList<GenelistTreeInfo> genelist, String dataset, int id){
		
		JSONObject obj = new JSONObject();		
		obj.put("data", dataset);
		obj.put("state", "closed");
		
		JSONObject attr = new JSONObject();
		attr.put("id", id);
		attr.put("title", "Dataset = "+dataset);
		obj.put("attr", attr);

		ArrayList<String> stages = new ArrayList<String>();
		ArrayList<String> multistages = new ArrayList<String>();
		for(GenelistTreeInfo inf : genelist){
			if(dataset.equalsIgnoreCase(inf.getDataset())){
				String st = inf.getStage();
				if(!stages.contains(inf.getStage()) && !inf.getStage().trim().contentEquals("0")){
					if (inf.getStage().length() == 4)
						stages.add(inf.getStage());
					else if(!multistages.contains(inf.getStage()))
						multistages.add(inf.getStage());
				}
			}
		}
		Collections.sort(stages);
		Collections.sort(multistages);
		
		JSONArray  list = new JSONArray();
		
		

//		// put multiple stages here outside the stage node
//		String redundantStage = "";
//		for(String stage1 : stages){
//			if (stage1.length() > 4){
//				for(GenelistTreeInfo inf : genelist){
//					if(dataset.equalsIgnoreCase(inf.getDataset()) && stage1.equalsIgnoreCase(inf.getStage())){
//						System.out.println("sample for "+ dataset + ","+ stage1 + " = " + inf.getSample());
//						if (inf.getSample().contains("GSM") || inf.getSample().isEmpty()){
//							JSONObject obj1 = new JSONObject();
//							obj1.put("data", inf.getName());
//							JSONObject attr1 = new JSONObject();
//							attr1.put("id", inf.getGenelistOID());
//							String table = "";
//							if (inf.getDatasetId().contentEquals("5") || inf.getDatasetId().contentEquals("6") || inf.getDatasetId().contentEquals("7"))
//								table = "4_" + inf.getDatasetId();
//							else
//								table = "3_" + inf.getDatasetId();
//							attr1.put("table", table);
//							attr1.put("rel", "Role");							
//							attr1.put("title", "Dataset = "+ dataset +  ", " + stage1);
//							obj1.put("attr", attr1);
//							list.add(obj1);
//						}
//						else{
////							JSONObject obj1 = new JSONObject();
////							obj1.put("data", inf.getSample());
////							obj1.put("state", "closed");
////							JSONObject attr1 = new JSONObject();
////							attr1.put("id", id-1);
////							attr.put("title", "Dataset = "+ dataset + ", Sample = " +inf.getSample() + ", " + stage1);
////							obj1.put("attr", attr1);
////							JSONArray  list1 = new JSONArray();
////							list1.add(createSamples(genelist, dataset, stage1, inf.getSample(), id));
////							obj1.put("children", list1);
////							list.add(obj1);
//							list.add(createSamples(genelist, dataset, stage1, inf.getSample(), id));
//						}
//						redundantStage = stage1;
//					}					
//				}
//			}
//		}
//		if (redundantStage.length() > 0)
//			stages.remove(redundantStage);
		
		
		
		int id2 = -10;
		for(String multistage : multistages){
//			System.out.println("stage = " + stage);
			id2 = id2-1;
			list.add(createMultiStages(genelist, dataset, multistage, id2));					
		}				
		
		for(String stage : stages){
//			System.out.println("stage = " + stage);
			id2 = id2-1;
			list.add(createStages(genelist, dataset, stage, id2));					
		}				
		obj.put("children", list);
				
		return obj;
	}

	private JSONObject createStages(ArrayList<GenelistTreeInfo> genelist, String dataset, String stage, int id){

		JSONObject obj = new JSONObject();		
		obj.put("data", stage);
		obj.put("state", "closed");
		
		JSONObject attr = new JSONObject();
		attr.put("id",id);				
		attr.put("title", "Dataset = "+ dataset + ", " + stage);
		obj.put("attr", attr);

		ArrayList<String> samples = new ArrayList<String>();
		for(GenelistTreeInfo inf : genelist){
			if(dataset.equalsIgnoreCase(inf.getDataset()) && stage.equalsIgnoreCase(inf.getStage())){
				if(!samples.contains(inf.getSample()) && !inf.getSample().trim().equalsIgnoreCase(""))
					samples.add(inf.getSample());
			}
		}
		Collections.sort(samples);
		
		
		JSONArray  list = new JSONArray();
		int id2 = -100;
		for(String sample : samples){
			id2 = id2 - 1;
			list.add(createSamples(genelist, dataset, stage, sample, id2));					
		}				
		obj.put("children", list);
				
		return obj;
	}

	private JSONObject createMultiStages(ArrayList<GenelistTreeInfo> genelist, String dataset, String stage, int id){

		System.out.println("dataset = "+ dataset + " stage = "+ stage);

		ArrayList<String> samples = new ArrayList<String>();
		for(GenelistTreeInfo inf : genelist){
			if(dataset.equalsIgnoreCase(inf.getDataset()) && stage.equalsIgnoreCase(inf.getStage())){
				if(!samples.contains(inf.getSample()) && !inf.getSample().trim().equalsIgnoreCase(""))
					samples.add(inf.getSample());
			}
		}
		Collections.sort(samples);

		JSONObject obj = new JSONObject();
		for(GenelistTreeInfo inf : genelist){
			if(dataset.equalsIgnoreCase(inf.getDataset()) && stage.equalsIgnoreCase(inf.getStage())){
				if (inf.getSample().contains("GSM") || inf.getSample().isEmpty()){
					System.out.println("sample for "+ dataset + ","+ stage + " = " + inf.getSample());
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
		}

		obj = new JSONObject();		
		int id2 = -100;
		for(String sample : samples){
			id2 = id2 - 1;
			obj = createSamples(genelist, dataset, stage, sample, id2);					
		}				
				
		return obj;
	}
	
	private JSONObject createUnnamedSamples(ArrayList<GenelistTreeInfo> genelist, String dataset, String stage, String sample, int id){

		JSONObject obj = new JSONObject();
		if(sample.contains("GSM") || sample.isEmpty()){
		for(GenelistTreeInfo inf : genelist){
			if(dataset.equalsIgnoreCase(inf.getDataset()) && sample.equalsIgnoreCase(inf.getSample())){
				if (inf.getSample().contains("GSM") || inf.getSample().isEmpty()){
					System.out.println("sample for "+ dataset + ","+ stage + " = " + inf.getSample());
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
		}
		}
		
		
		



		obj.put("data", sample);
		obj.put("state", "closed");
		
		JSONObject attr = new JSONObject();
		attr.put("id",id);
		attr.put("title", "Dataset = "+ dataset + ", Sample = " +sample);
		obj.put("attr", attr);


		ArrayList<GenelistTreeInfo> allList = new ArrayList<GenelistTreeInfo>();
		for(GenelistTreeInfo inf : genelist){
//			if(dataset.equalsIgnoreCase(inf.getDataset()) && stage.equalsIgnoreCase(inf.getStage()) && sample.equalsIgnoreCase(inf.getSample())){
			if(dataset.equalsIgnoreCase(inf.getDataset()) && sample.equalsIgnoreCase(inf.getSample())){
				if(!allList.contains(inf.getSubset2()))
					allList.add(inf);
			}
		}

		JSONArray  list = new JSONArray();
		list = createLevelAll(allList);
		obj.put("children", list);
				
		return obj;
	}

	private JSONObject createSamples(ArrayList<GenelistTreeInfo> genelist, String dataset, String stage, String sample, int id){

		JSONObject obj = new JSONObject();
		if(sample.contains("GSM") || sample.isEmpty()){
		for(GenelistTreeInfo inf : genelist){
			if(dataset.equalsIgnoreCase(inf.getDataset()) && sample.equalsIgnoreCase(inf.getSample())){
				if (inf.getSample().contains("GSM") || inf.getSample().isEmpty()){
					System.out.println("sample for "+ dataset + ","+ stage + " = " + inf.getSample());
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
		}
		}
		
		
		



		obj.put("data", sample);
		obj.put("state", "closed");
		
		JSONObject attr = new JSONObject();
		attr.put("id",id);
		attr.put("title", "Dataset = "+ dataset + ", Sample = " +sample);
		obj.put("attr", attr);


		ArrayList<GenelistTreeInfo> allList = new ArrayList<GenelistTreeInfo>();
		for(GenelistTreeInfo inf : genelist){
//			if(dataset.equalsIgnoreCase(inf.getDataset()) && stage.equalsIgnoreCase(inf.getStage()) && sample.equalsIgnoreCase(inf.getSample())){
			if(dataset.equalsIgnoreCase(inf.getDataset()) && sample.equalsIgnoreCase(inf.getSample())){
				if(!allList.contains(inf.getSubset2()))
					allList.add(inf);
			}
		}

		JSONArray  list = new JSONArray();
		list = createLevelAll(allList);
		obj.put("children", list);
				
		return obj;
	}
	
	private JSONArray createLevelAll(ArrayList<GenelistTreeInfo> genelist){

		JSONArray  list = new JSONArray();
		
		ArrayList<GenelistTreeInfo> allList = new ArrayList<GenelistTreeInfo>();
		for(GenelistTreeInfo inf : genelist){
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
		
		for (GenelistTreeInfo all : allList){
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
			attr.put("title", "Dataset = "+ all.getDataset() + ", Sample = " + all.getSample() + ", " + all.getStage() + ", " + all.getGenelistType() + ", " + all.getAuthor());
			obj.put("attr", attr);

			JSONArray  clusterlist = new JSONArray();
			clusterlist = createLevelCluster(genelist, all.getEntityCount());
			obj.put("children", clusterlist);
					
			list.add(obj);
		}
		return list;
	}

	private JSONArray createLevelCluster(ArrayList<GenelistTreeInfo> genelist, String count){

		ArrayList<GenelistTreeInfo> kList = new ArrayList<GenelistTreeInfo>();
		for(GenelistTreeInfo inf : genelist){
			if (inf.getSubset1() != null){
				if(!kList.contains(inf.getSubset1()) && inf.getSubset1().equalsIgnoreCase(count) && !inf.getSubset2().equalsIgnoreCase("All"))
					kList.add(inf);
			}
		}
		JSONArray  list = new JSONArray();
		
		for (GenelistTreeInfo k : kList){
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
			attr.put("title", "Dataset = "+ k.getDataset() + ", Sample = " + k.getSample() + ", " + k.getStage() + ", " + k.getGenelistType() + ", " + k.getAuthor());
			obj.put("attr", attr);
					
			list.add(obj);
		}
		return list;
	}

	
	
	private JSONObject createPublishedDatasets(ArrayList<GenelistTreeInfo> genelist, String dataset, int id){
		
		JSONObject obj = new JSONObject();		
		obj.put("data", dataset);
		obj.put("state", "open");
		
		JSONObject attr = new JSONObject();
		attr.put("id", id);
		attr.put("title", "Dataset = "+dataset);
		obj.put("attr", attr);

		ArrayList<String> publishers = new ArrayList<String>();
		for(GenelistTreeInfo inf : genelist){
			if(dataset.equalsIgnoreCase(inf.getDataset())){
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

	private JSONObject createPublisher(ArrayList<GenelistTreeInfo> genelist, String dataset, String publisher, int id){
		
		JSONObject obj = new JSONObject();		
		obj.put("data", publisher);
		obj.put("state", "closed");
		
		JSONObject attr = new JSONObject();
		attr.put("id", id);
//		attr.put("title", "Dataset = "+dataset);
		obj.put("attr", attr);

		ArrayList<GenelistTreeInfo> names = new ArrayList<GenelistTreeInfo>();
		for(GenelistTreeInfo inf : genelist){
			if(dataset.equalsIgnoreCase(inf.getDataset()) && publisher.equalsIgnoreCase(inf.getLpuRef())){
				if(!names.contains(inf.getName()))
					names.add(inf);
			}
		}

		JSONArray  list = new JSONArray();
		
		list = createPublishedNames(names);
		obj.put("children", list);
				
		return obj;
	}

	private JSONObject createPublisher2(ArrayList<GenelistTreeInfo> genelist, String dataset, String publisher, int id){
		
		JSONObject obj = new JSONObject();		
		obj.put("data", publisher);
		obj.put("state", "closed");
		
		JSONObject attr = new JSONObject();
		attr.put("id", id);
//		attr.put("title", "Dataset = "+dataset);
		obj.put("attr", attr);

		ArrayList<String> stages = new ArrayList<String>();
		for(GenelistTreeInfo inf : genelist){
			if(dataset.equalsIgnoreCase(inf.getDataset()) && publisher.equalsIgnoreCase(inf.getLpuRef())){
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
				for(GenelistTreeInfo inf : genelist){
					if(dataset.equalsIgnoreCase(inf.getDataset()) && publisher.equalsIgnoreCase(inf.getLpuRef()) && stage1.equalsIgnoreCase(inf.getStage())){						
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
						attr1.put("title", "Dataset = "+ inf.getDataset() + ", Sample = " + inf.getName() + ", " + inf.getStage() + ", " + inf.getLpuRef() + ", " + inf.getAuthor());
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
	
	private JSONObject createPublishedStages(ArrayList<GenelistTreeInfo> genelist, String dataset, String publisher, String stage, int id){

		JSONObject obj = new JSONObject();
		
		obj.put("data", stage);
		obj.put("state", "closed");
		
		JSONObject attr = new JSONObject();
		attr.put("id",id);				
		attr.put("title", "Dataset = "+ dataset + ", " + stage);
		obj.put("attr", attr);

		ArrayList<GenelistTreeInfo> names = new ArrayList<GenelistTreeInfo>();
		for(GenelistTreeInfo inf : genelist){
			if(dataset.equalsIgnoreCase(inf.getDataset()) && publisher.equalsIgnoreCase(inf.getLpuRef()) && stage.equalsIgnoreCase(inf.getStage())){
				if(!names.contains(inf.getName()))
					names.add(inf);
			}
		}
		
		JSONArray  list = new JSONArray();
		list = createPublishedNames(names);
		obj.put("children", list);
				
		return obj;
	}

	private JSONArray createPublishedNames(ArrayList<GenelistTreeInfo> genelist){
	
		JSONArray  list = new JSONArray();
		
		for (GenelistTreeInfo inf : genelist){
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
			attr.put("title", "Dataset = "+ inf.getDataset() + ", Sample = " + inf.getName() + ", " + inf.getStage() + ", " + inf.getLpuRef() + ", " + inf.getAuthor());
			obj.put("attr", attr);
					
			list.add(obj);
		}
		return list;
	}
	
}



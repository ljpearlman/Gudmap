package gmerg.beans;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import gmerg.assemblers.PredictiveTextSearchAssembler;
import gmerg.utils.FacesUtil;






public class CellAnalysisBean {
	
	private String gene;
	private String file;
	
	public CellAnalysisBean() {

		

	}
	
	public void setGeneInput(String gene){
		this.gene = gene;
	}
	
	public String getGeneInput(){
		return gene;
	}
	
	
	public void setFile(String file){
		this.file = file;
	}
	
	public String getFile(){
		return file;
	}

    public List autocomplete (Object input) {

		if (null == input)
		    return null;
		
		String pattern =  ((String)input).trim();
		if (pattern.equals(""))
		    return null;
			
		PredictiveTextSearchAssembler assembler = new PredictiveTextSearchAssembler();
		List ret = null;
		int limit = 20;

	    ret = assembler.getGenes(pattern, 1, limit);

	    return ret;
    }

	public String search() {


		return "AdvancedQuery";
	}
	
	public String runPython(String gene, String file){
		String result = null;
		
		String pythonScriptPath = "/home/norbert/python/helloPython.py";
		
		String[] cmd = new String[2];
		cmd[0] = "python3";
		cmd[1] = pythonScriptPath;
		
		try{
			// create runtime to execute external command
			Runtime rt = Runtime.getRuntime();
			Process pr = rt.exec(cmd);
			
			
			// retrieve output from python script
			BufferedReader bfr = new BufferedReader(new InputStreamReader(pr.getInputStream()));
			String line = "";
			while((line = bfr.readLine()) != null) {
			// display each output line form python script
			System.out.println(line);
			}
		} catch(IOException e){
			
		}
		
		return result;
	}
    
}

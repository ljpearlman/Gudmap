package gmerg.control;

import gmerg.assemblers.GenelistExpressionAssembler;
import gmerg.beans.ArrayAnalysisBean;
import gmerg.beans.UserBean;
import gmerg.model.DataSetLoader;
import gmerg.utils.FacesUtil;
import gmerg.utils.FileHandler;
import gmerg.utils.Utility;

import javax.faces.event.PhaseListener;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.ObjectOutputStream;
//import java.io.OutputStream;
//import java.util.ArrayList;

import analysis.DataSet;


/**
 * Phase listener to capture requests from TreeView Applet and redirect to ArrayAnalysis
 *  
 * @author Mehran Sharghi
 * 
 */

public class GudmapPhaseTracker implements PhaseListener {
	
	public PhaseId getPhaseId(){
		return PhaseId.RESTORE_VIEW;
	}

	public void beforePhase(PhaseEvent event) {
		FacesContext context = FacesContext.getCurrentInstance();
		//************************* process requests from TreeView applet ***************************
		String appletRequest = FacesUtil.getRequestParamValue("appletRequest");
		if ("dataSet".equals(appletRequest)){
			HttpServletResponse response = (HttpServletResponse)context.getExternalContext().getResponse();
			String dataSource = FacesUtil.getRequestParamValue("dataSource");
			if ("series".equals(dataSource)) {
//				ArrayAnalysisBean analysisBean = (ArrayAnalysisBean) context.getApplication().getVariableResolver().resolveVariable(context, "AnalysisBean");
				ArrayAnalysisBean analysisBean = (ArrayAnalysisBean) FacesUtil.getSessionValue("AnalysisBean");
				analysisBean.setVisualiseResult(false);
				analysisBean.getArrayAnalysisDelegate().sendDataSet(response);
			}
			if ("genelist".equals(dataSource)) {
				String genelistId = FacesUtil.getRequestParamValue("genelistId");
				DataSet expressionDataSet = null;
				if (genelistId != null && !genelistId.equals("")) {
					String cdtFile = Utility.getValue(FacesUtil.getRequestParamValue("cdtFile"), null);
					if (cdtFile == null) {
						GenelistExpressionAssembler expressionAssembler = new GenelistExpressionAssembler(Integer.parseInt(genelistId));
					    expressionDataSet = expressionAssembler.retrieveExpressionData();
					    expressionAssembler.adjustExpressionsForDisplay(expressionDataSet.getData(), 2.5, 10.0, 1.0, 1.0, 1.0);
					}
					else {
						String gtrFile = Utility.getValue(FacesUtil.getRequestParamValue("gtrFile"), null);
						DataSetLoader dataSetLoader = new DataSetLoader(cdtFile, gtrFile);
						dataSetLoader.loadDataSet();
						expressionDataSet = dataSetLoader.getDataSet();
					}
				}
				   
				sendObject(response, expressionDataSet);
			}
			context.responseComplete();
			return;
		}
		
		//******************************** process request from Image Viewer ***************************
		String imageViewerRequest = FacesUtil.getRequestParamValue("imageViewerRequest");
//		System.out.println("imageViewerRequest==================="+imageViewerRequest);		
		if ("saveImage".equals(imageViewerRequest)){
			
			String path = FacesUtil.getRequestParamValue("path");
			String fileName = FacesUtil.getRequestParamValue("filename");
			String fileType = FacesUtil.getRequestParamValue("type");
			String downloadName = FacesUtil.getRequestParamValue("downloadName");
			if (downloadName == null || "".equals(downloadName))
				downloadName = fileName;
			String fullName = path + "/" + fileName;
//			System.out.println("path=="+path+"  fileName="+fileName+"    fileType="+fileType+ "   fullName="+fullName);		
			
			if (fullName!=null && fullName.indexOf("..")!=0) {	// Make sure we can't download files above the current directory location. 
				fullName.replace("..", "");
				if (fullName.indexOf(".ht.+")!=0) { // Make sure we can't download .ht control files.
//					get rid of "project/gudmap/" at the begining of path
		            String contentType = "image/" + fileType;
		            HttpServletResponse response = ( HttpServletResponse ) context.getExternalContext().getResponse();
//		            System.out.println("path=="+path+"  fileName="+fileName+"    contentType="+contentType+ "   downloadName="+downloadName);
	            	FileHandler.saveFileToDesktop(response, path, fileName, contentType, downloadName);
					context.responseComplete();
					return;
				}
			}
		}
		
		//*** To avoid caching of secure pages when user is logged in. When logged out and click
		//*** browser's back button user is prevented from being able to see previous contents.
		UserBean userBean = (UserBean)FacesUtil.getSessionValue("UserBean");
		if (userBean != null && userBean.isUserLoggedIn() && GudmapSessionTimeoutFilter.isSessionControlRequired(context)) {
			HttpServletResponse response = (HttpServletResponse)context.getExternalContext().getResponse();
			try{
//System.out.println("**********************************888 **");
if(false) { // it is disabled until find a better way 		
				response.setHeader("Cache-Control", "no-store");
				response.addHeader("Pragma", "no-cache");
}				
			}catch(NullPointerException npe){	
//				System.out.println("Response is Null - No Action on cache-control");
			}
		}
	}
	
	public void afterPhase(PhaseEvent e) {
	}
    
	/*****************************************************************************
	* Private methods
	* 
	*/
	public void sendObject(HttpServletResponse response, Object data) {
		if (data == null) {
			System.out.println("Error!  there is no genelist data to send to the applet.");
			return;
		}

		ObjectOutputStream outputToApplet;
		try {
			response.setContentType("application/octet-stream");
			// response.setContentType("application/x-java-serialized-object");
			ServletOutputStream servletOutput = response.getOutputStream();
			outputToApplet = new ObjectOutputStream(servletOutput);
			outputToApplet.writeObject(data);
			outputToApplet.flush();
			outputToApplet.close();
			System.out.println("Data transmission complete.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
}


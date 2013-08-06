package gmerg.beans;

import gmerg.assemblers.ImageDetailAssembler;

import gmerg.entities.submission.ImageDetail;
import gmerg.utils.FacesUtil;
import gmerg.utils.Utility;

import java.util.*;
import java.net.URLEncoder;
import java.io.UnsupportedEncodingException;
import java.lang.NumberFormatException;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;


public class ImageDetailBean {
    private boolean debug = false;
    
    private ImageDetail imageDetail;  //object containing details of a submitted image
    private ImageDetailAssembler imgAssembler;
    ArrayList notesList;
    ArrayList publicImagesList;
    String serialNo;
    String id;
    
    public ImageDetailBean() {
        this (FacesUtil.getRequestParamValue("id"), FacesUtil.getRequestParamValue("serialNo"));
    }
    
    public ImageDetailBean(String id, String serialNo) {
	if (debug)
	    System.out.println(" ImageDetailBean.constructor with argument id = "+id+" serialNo = "+serialNo);
	
    	this.id = id;
    	this.serialNo = serialNo;
        imgAssembler = new ImageDetailAssembler();
        imageDetail = imgAssembler.getData(id, serialNo);
        if (imageDetail != null) {
	    notesList = imageDetail.getAllImageNotesInSameSubmission();
            publicImagesList = imageDetail.getAllPublicImagesInSameSubmission();
        }
        else {
	    System.out.println("!!!! Image Detail is NULL for id=" + id + "  SerialNo=" + serialNo);
	    notesList = null;
	    publicImagesList = null;
    	}
    }
    
    public ImageDetail getImageDetail(){
        return imageDetail;
    }
    
    public void setImageDetail(ImageDetail detail) {
        imageDetail = detail;
    }
    
    public String getSubmissionName () {
        if(Utility.getProject().equalsIgnoreCase("gudmap")){
            return "gudmap";
        }
        else if(Utility.getProject().equalsIgnoreCase("euregene")) {
            if(Utility.getSpecies().equalsIgnoreCase("mouse")) {
                return "euregene_mge";
            }
            else {
                return "euregene_xge";
            }
        }
        else {
            return "";
        }
	
	
    }

    public String getImageDir () {
	if (null == imageDetail)
	    return "";
	
    	String path = imageDetail.getFilePath();
    	/*if (path.startsWith("/", path.length()-1));
	//path = path.substring(0, path.length()-2);
    	//String[] s = imageDetail.getFilePath().split("/");
	
	
    	return (s[s.length-2]+"/"+s[s.length-1]);*/
	
    	return path.substring(0,path.length()-1);
    }
    
    public String getThumbnail() {
	if (notesList == null)
	    return "";
	try {
	    int i = Integer.parseInt(serialNo);
	    if (i <= notesList.size())
		return ((String[])notesList.get(i-1))[0];
	    else
		return "";
	}
	catch (NumberFormatException e) {
	    return "";
	}
    }	
    
    public String getAllNotes() {
	String allNotes = null;
	if (notesList == null)
	    return allNotes;
	
	for(int i=0; i<notesList.size(); i++) {
	    String[] note = (String[])notesList.get(i);
	    if(i<notesList.size()-1)
		allNotes += note[0] + "!$!" + note[1] + "!%!";
	    else
		allNotes += note[0] + "!$!" + note[1];
	}
	
	try {
	    allNotes = URLEncoder.encode(allNotes, "UTF-8");
	} catch (UnsupportedEncodingException e) {
	    e.printStackTrace();
	    allNotes = null;
	}
	
	if (null != allNotes) {
	    allNotes = allNotes.trim();
	    if (allNotes.equals("") || allNotes.equalsIgnoreCase("null"))
		allNotes = null;
	}

	return allNotes;
    }
    
    public String getPublicImages() {
	StringBuffer publicImages = new StringBuffer("");
	if(publicImagesList == null){
	    return publicImages.toString();
	}
	for(int i=0;i<publicImagesList.size();i++){
	    if(i < publicImagesList.size()-1){
		publicImages.append(publicImagesList.get(i)+"|");
	    }
	    else {
		publicImages.append(publicImagesList.get(i));
	    }
	}
	
	return publicImages.toString();
    }
    
    public String getViewerFrameSourceName() {
	if (debug)
	    System.out.println(" ImageDetailBean.getViewerFrameSourceName");
	
	return Utility.domainUrl+"mrciip/mrciip_gudmap.html";
    }
}

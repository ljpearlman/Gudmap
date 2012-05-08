package gmerg.beans;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.faces.validator.ValidatorException;
import javax.mail.internet.InternetAddress;

import gmerg.utils.Utility;

public class FeedbackBean {
	
	private String name;
	private String email;
	private String comment;
	private String referringURL;
	private String operatingSystem;
	private String browser;
	private String behindFirewall;
	private String message = "";
	boolean submitted = false;
	
	public FeedbackBean() {
		
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String value){
		name = value;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String value){
		email = value;
	}
	
	public String getComment() {
		return comment;
	}
	
	public void setComment(String value){
		comment = value;
	}
	
	public String getReferringURL() {
		return referringURL;
	}
	
	public void setReferringURL(String value){
		referringURL = value;
		if (referringURL == null || referringURL.equals("")){
			referringURL = "n/a";
		}
	}
	
	public String getOperatingSystem() {
		return operatingSystem;
	}
	
	public void setOperatingSystem(String value){
		operatingSystem = value;
	}
	
	public String getBrowser() {
		return browser;
	}
	
	public void setBrowser(String value){
		browser = value;
	}
	
	public String getBehindFirewall() {
		return behindFirewall;
	}
	
	public void setBehindFirewall(String value){
		behindFirewall = value;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String value){
		message = value;
	}
	
	public boolean isSubmitted() {
		return submitted;
	}
	
	public SelectItem [] getBrowserOptions() {
		return new SelectItem [] {
				new SelectItem("Firefox/Mozilla", "Firefox/Mozilla"), 
                new SelectItem("Safari","Safari"), 
                new SelectItem("Internet Explorer (v 6.0)", "Internet Explorer (v 6.0)"),
                new SelectItem("Internet Explorer (v 7.0)", "Internet Explorer (v 7.0)"),   
                new SelectItem("Other/Don't know", "Other/Don't know")
		};
	}
	
	public SelectItem [] getOperatingSystemOptions() {
		return new SelectItem [] { 
                new SelectItem("Linux (i686)", "Linux (i686)"), 
                new SelectItem("Macintosh PPC 10.4.11","Macintosh PPC 10.4.11"), 
                new SelectItem("Macintosh Intel 10.4.11", "Macintosh Intel 10.4.11"),
                new SelectItem("Windows XP","Windows XP"), 
                new SelectItem("Windows Vista","Windows Vista"), 
                new SelectItem("Other/Don't know", "Other/Don't know")
            };
	}
	
	public void emailComment(ActionEvent ae){
		String content = "Name: " +name +"\n\r" +
				"EmailAddress: " +email+"\n\r" +
						"Operating System: " + operatingSystem +"\n\r" +
								"Behind Firewall: " +behindFirewall +"\n\r" +
										"Problem URL: " +referringURL+"\n\r" +
												"Comments: "+comment+"\n\r";
		try {
			Utility.sendEmail(email, "gudmap-db@hgu.mrc.ac.uk", "User comment on "+Utility.getProject()+" web site", content);
			message = "Your comments were successfully submitted.";
			submitted = true;
		}
		catch(Exception e){
			message = "Your comment could not be submitted. Please check your input and try again. If the problem persists, please contact the " +
					"<a class='plaintext' href='mailto:gudmap-db@hgu.mrc.ac.uk'>system administrator</a>.";
			e.printStackTrace();
			submitted = false;
			
		}
	}
	
	public void validateName(FacesContext context, UIComponent component, Object value) {
		String name = ((String)value).trim();
		FacesMessage message = null;
		if (name== null || name.equals(""))
			message = new FacesMessage("Please enter you name.", 
									   "Please enter your name.");
		
		if (message!=null) {
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			throw new ValidatorException(message);
		}
	}
	
	public void validateEmail(FacesContext context, UIComponent component, Object value) {
		String emailAddress = ((String)value).trim();
		FacesMessage message = null;
		if (emailAddress== null || emailAddress.equals(""))
			message = new FacesMessage("Please supply a valid email address", 
									   "Please supply a valid email address");
		else {
			try {
				InternetAddress.parse(emailAddress, true);
			}
			catch (Exception e) {
				message = new FacesMessage("Please supply a valid email address", "Please supply a valid email address");
			}
		}
		if (message!=null) {
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			throw new ValidatorException(message);
		}
	}
	
	public void validateComment(FacesContext context, UIComponent component, Object value) {
		String comment = ((String)value).trim();
		FacesMessage message = null;
		if (comment== null || comment.equals(""))
			message = new FacesMessage("Please supply a comment.", 
									   "Please supply a comment.");
		
		if (message!=null) {
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			throw new ValidatorException(message);
		}
	}
	
	
	
	
}

package gmerg.beans;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.mail.internet.InternetAddress;

import gmerg.assemblers.CollectionAssembler;
import gmerg.entities.CollectionInfo;
import gmerg.entities.Globals;
import gmerg.entities.User;
import gmerg.model.ClipboardDelegateCookieImp;
import gmerg.utils.FacesUtil;
import gmerg.utils.Utility;

public class CollectionShareBean {
    private boolean debug = false;

	private String emailMessage;
	private String toEmailAddress;
	private String collectionId;
	private int collectionType;
	private String sendErrorMessage;
	final static String fixedMessagePart = "Please find attached the collection file in text format.\n";
	
	// ********************************************************************************
	// Constructors & Initializers
	// ********************************************************************************
	public CollectionShareBean() {
	    if (debug)
		System.out.println("CollectionShareBean.constructor");

		collectionId = Utility.getValue(FacesUtil.getAnyRequestParamValue("collectionId"), "clipboard");
		collectionType = Integer.parseInt(Utility.getValue(FacesUtil.getRequestParamValue("collectionType"), "0"));
		sendErrorMessage = "";
	}
	
	// ********************************************************************************
	// Action methods & validators
	// ********************************************************************************
	public String shareCollection () {
		User user = Utility.getUser();
		String fromEmailAddress = "";
		String userName = "";
		if(user != null) {
			fromEmailAddress = user.getEmailAddress();
			userName = user.getUserName();
		}
		else {
			fromEmailAddress = "gudmapdb@hgu.mrc.ac.uk";
			userName = "Anonymous user";
		}
		if(fromEmailAddress==null || fromEmailAddress.equals(""))
			fromEmailAddress = "gudmapdb@hgu.mrc.ac.uk";
		String subject = userName+" would like to share a collection from "+Utility.getProject()+" with you";
		try {
			if (collectionId.equals("clipboard")) {
				String clipboardInfo = "name = clipboard\n";
				clipboardInfo += "type = " + Globals.getCollectionCategoryName(collectionType) + "\n";
				clipboardInfo += ClipboardDelegateCookieImp.getClicpboardIds(collectionType);
				Utility.sendEmail(fromEmailAddress, toEmailAddress, subject, emailMessage+"\n\n"+fixedMessagePart, 
						  clipboardInfo, "Clipboard");
			}
			else {
				CollectionInfo collectionInfo = CollectionAssembler.instance().getCollectionInfo(Integer.parseInt(collectionId));
				Utility.sendEmail(fromEmailAddress, toEmailAddress, subject, emailMessage+"\n\n"+fixedMessagePart, 
						  collectionInfo.getAllInfo(), collectionInfo.getName());
			}
			return "collectionBrowse";
		}
		catch (Exception e) {
			sendErrorMessage = "Collection could not be sent. Check the email address and try again.";
			return null;
		}
	}

	public void validateEmailAddress(FacesContext context, UIComponent component, Object value) {
		String emailAddress = ((String)value).trim();
		FacesMessage message = null;
		if (emailAddress.equals(""))
			message = new FacesMessage("An email address must be specified", 
									   "An email address must be specified");
		else {
			try {
				InternetAddress.parse(emailAddress, true);
			}
			catch (Exception e) {
				message = new FacesMessage("Email address is not valid", "Email address is not valid");
			}
		}
		if (message!=null) {
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			throw new ValidatorException(message);
		}
	}
	
	// ********************************************************************************
	// Getters & Setter
	// ********************************************************************************
	public String getCollectionId() {
		return collectionId;
	}

	public void setCollectionId(String collectionId) {
		this.collectionId = collectionId;
	}

	public String getEmailMessage() {
		return emailMessage;
	}

	public void setEmailMessage(String emailMessage) {
		this.emailMessage = emailMessage;
	}

	public String getToEmailAddress() {
		return toEmailAddress;
	}

	public void setToEmailAddress(String toEmailAddress) {
		this.toEmailAddress = toEmailAddress;
	}

	public String getSendErrorMessage() {
		return sendErrorMessage;
	}

	public int getCollectionType() {
		return collectionType;
	}

	public void setCollectionType(int collectionType) {
		this.collectionType = collectionType;
	}
	
}

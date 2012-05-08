<!-- Author: Mehran Sharghi				-->
<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<f:view>

	<jsp:include page="/includes/header.jsp" />
	
	<h:form id="shareCollectionForm">
		<h:inputHidden id="collectionId" value="#{CollectionShareBean.collectionId}"/>
		<h:inputHidden id="collectionType" value="#{CollectionShareBean.collectionType}"/>
		<h:panelGrid columns="2" width="100%" cellspacing="2px" cellpadding="10px" 
					 columnClasses="rightAlign,leftAlign"  styleClass="header-stripey" >
			<h:outputText value="To (email address): " styleClass="plaintextbold" />
			<h:panelGrid>
				<h:inputText id="emailAddress" value="#{CollectionShareBean.toEmailAddress}" size="50" 
							 validator="#{CollectionShareBean.validateEmailAddress}" 
							 required="true" requiredMessage="An email address must be specified" />
				<h:message for="emailAddress" styleClass="errorMessage" />
			</h:panelGrid>
			
			<h:outputText value="Your message (optional): " styleClass="plaintextbold" />
			<h:inputTextarea value="#{CollectionShareBean.emailMessage}" rows="8" cols="60"/>
		</h:panelGrid>
		
		<h:panelGrid width="100%" cellpadding="10px" columnClasses="centreAlign" styleClass="header-stripey" >
			<h:outputText value="#{CollectionShareBean.sendErrorMessage}" styleClass="errorMessage" />
		</h:panelGrid>
		<h:panelGrid columns="2" width="100%" cellpadding="10px" columnClasses="rightAlign,leftAlign" styleClass="table-stripey" >
			<h:commandButton value="Send" action="#{CollectionShareBean.shareCollection}" />
			<h:commandButton value="Cancel" onclick="clickLink('shareCollectionForm:toCollectionBrowse'); return false;" />
		</h:panelGrid>
		
		<h:outputLink id="toCollectionBrowse" value="collection_browse.html?collectionId=#{CollectionShareBean.collectionId}&collectionType=#{CollectionShareBean.collectionType}" />
	</h:form> 
	
	<jsp:include page="/includes/footer.jsp" />
	
</f:view>
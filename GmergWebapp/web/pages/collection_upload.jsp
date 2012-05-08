<!-- Author: Mehran Sharghi				-->
<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<f:view>

	<jsp:include page="/includes/header.jsp" />
	
	<h:form enctype="multipart/form-data" id="uploadCollectionForm">
		<h:panelGrid styleClass="defaultWidth" style="position:absolute;z-index:100;top:250px; visibility:#{CollectionUploadBean.overwriteVisibility}" >
			<h:panelGrid bgcolor="#FFFFBB" columnClasses="centreAlign" style="border:1px solid black;" styleClass="centreAlign" cellpadding="5px">
				<h:outputText value='A collection with the name "#{CollectionUploadBean.collectionName}" already exist, do you want to overwrite it?' styleClass="plaintext"/>
				<h:panelGrid columns="2"  cellspacing="15px" styleClass="centreAlign" columnClasses="rightAlign, leftAlign">
					<h:commandButton value="Overwrite" action="#{CollectionUploadBean.overwriteCollection}" />
					<h:commandButton value="Cancel" action="#{CollectionUploadBean.cancelOverwrite}" />
				</h:panelGrid>	
			</h:panelGrid>
		</h:panelGrid>
		
		<h:panelGrid id="geneTableOuterFrame2" cellspacing="2" cellpadding="2" bgcolor="white">
			<h:panelGroup>
				<h:outputText value="Upload collection from a " styleClass="datatext" />
				<h:outputLink value="batch_query.html" styleClass="plaintext" >
					<h:outputText value="text file" />
				</h:outputLink>
				<h:outputText value=":" styleClass="plaintext" />
			</h:panelGroup>
			<t:inputFileUpload id="myFileId" value="#{CollectionUploadBean.uploadedFile}" storage="file"  accept="txt" required="false" 
							   disabled="#{CollectionUploadBean.renderOverwrite}" binding="#{CollectionUploadBean.uploadedFileComponent}"/> 
			<h:panelGroup>
				<h:commandButton value="Upload" action="#{CollectionUploadBean.uploadCollection}" />
				<h:outputText value="&nbsp;&nbsp;" escape="false"/>
				<h:commandButton value="Cancel" action="#{CollectionUploadBean.cancelUpload}" />
			</h:panelGroup>							   
		</h:panelGrid>
	</h:form> 
	
	<jsp:include page="/includes/footer.jsp" />
	
</f:view>
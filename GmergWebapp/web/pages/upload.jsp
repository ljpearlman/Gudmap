<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<f:view>

	<jsp:include page="/includes/header.jsp" />
	
	<h:form enctype="multipart/form-data" id="uploadbutton">
		<h:panelGrid id="geneTableOuterFrame2" cellspacing="2" cellpadding="2" bgcolor="white">
			<h:panelGroup>
				<h:outputText value="Upload a collection from " styleClass="datatext" />
				<h:outputLink value="batch_query.html" styleClass="plaintext" >
					<h:outputText value="text file" />
				</h:outputLink>
				<h:outputText value=":" styleClass="plaintext" />
			</h:panelGroup>
			<t:inputFileUpload id="myFileId" value="#{CollectionListBrowseBean.uploadFile}" storage="file"  accept="txt" required="false"/> 
				<h:commandButton value="Upload" action="#{CollectionListBrowseBean.uploadCollection}">
			</h:commandButton>          
		</h:panelGrid>
	</h:form> 
	
	<jsp:include page="/includes/footer.jsp" />
	
</f:view>
<!-- Author: Mehran Sharghi				-->
<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>

<body onload="getById('CollectionInformationForm:collectionName').focus()" >

<f:view>
	<jsp:include page="/includes/header.jsp" />

	<h:form id="CollectionInformationForm" >
		<h:inputHidden id="collectionId" value="#{CollectionInfoBean.collectionInfo.id}" 
						binding="#{CollectionInfoBean.collectionIdComponent}"/>
		<h:inputHidden id="collectionType" value="#{CollectionInfoBean.collectionInfo.type}" 
						binding="#{CollectionInfoBean.collectionTypeComponent}"/>
		<h:panelGrid styleClass="defaultWidth" style="position:absolute;z-index:100;top:250px; visibility:#{CollectionInfoBean.overwriteVisibility}" >
			<h:panelGrid bgcolor="#FFFFBB" columnClasses="centreAlign" style="border:1px solid black;" styleClass="centreAlign" cellpadding="5px">
				<h:outputText value='A collection with the name "#{CollectionInfoBean.collectionInfo.name}" already exist, do you want to overwrite it?' styleClass="plaintext"/>
				<h:panelGrid columns="2"  cellspacing="15px" styleClass="centreAlign" columnClasses="rightAlign, leftAlign">
					<h:commandButton value="Overwrite" action="#{CollectionInfoBean.overwriteCollection}" 
									 onclick="enableInputComponents(document.forms['CollectionInformationForm'])"/>
					<h:commandButton value="Cancel" action="#{CollectionInfoBean.cancelOverwrite}" 
									 onclick="enableInputComponents(document.forms['CollectionInformationForm'])" />
				</h:panelGrid>	
			</h:panelGrid>
		</h:panelGrid>

		<h:outputText value="Please complete the following information:<br/>" escape="false" />
		<h:panelGrid columns="2" cellspacing="4" cellpadding="12" width="100%"
					 rowClasses="header-stripey,header-nostripe" columnClasses="plaintextbold, plaintext" >
			<h:outputText value="Collection Name:" />
			<h:panelGrid>
				<h:inputText id="collectionName" value="#{CollectionInfoBean.collectionInfo.name}" size="35"
							 required="true" requiredMessage="A name must be specified for collection" 
							 validator="#{CollectionInfoBean.validateCollectionName}" 
							 disabled="#{CollectionInfoBean.renderOverwrite}"/>
				<h:message for="collectionName" styleClass="errorMessage" />
			</h:panelGrid>
			
			<h:outputText value="Description:" />
			<h:inputText id="description" value="#{CollectionInfoBean.collectionInfo.description}" size="60" 
						 style="cursor:text" disabled="#{CollectionInfoBean.renderOverwrite}" />
			
			<h:outputText value="Status:" />
			<h:selectOneRadio id="statusSelectionRadio" value="#{CollectionInfoBean.collectionInfo.status}" 
							  layout="LineDirection" disabled="#{CollectionInfoBean.renderOverwrite}" >
				<f:selectItem itemValue="0" itemLabel="private"/>
				<f:selectItem itemValue="1" itemLabel="public"/>
			</h:selectOneRadio>
			
			<h:outputText value="Type:" />
			<h:outputText value="#{CollectionInfoBean.type}" />
			
			<h:outputText value="Focus Group:" />
			<h:selectOneRadio id="focusGroupSelectionRadio" value="#{CollectionInfoBean.collectionInfo.focusGroup}" 
							  layout="pageDirection" disabled="#{CollectionInfoBean.renderOverwrite}" >
				<f:selectItems value="#{CollectionInfoBean.groupItems}" />
			</h:selectOneRadio>
		</h:panelGrid>	

		<h:outputText value="<br/>" escape="false" />		
		<h:panelGrid columns="3" width="100%" columnClasses="centreAlign, centreAlign, centreAlign" rendered="#{!CollectionInfoBean.renderOverwrite}" >
			<h:commandButton value="Store" action="#{CollectionInfoBean.storeCollection}" />
<%--
			<h:commandButton value="Reset" onclick="getById('CollectionInformationForm:resetLink').onclick();return false;" />
			<h:commandButton value="Cancel" action="#{CollectionInfoBean.cancel}" immediate="true"/>
--%>			
			<h:commandButton value="Reset" action="#{CollectionInfoBean.reset}" immediate="true"/>
			<h:commandButton value="Cancel" action="#{CollectionInfoBean.cancel}" immediate="true"/>
		</h:panelGrid>
		
		<h:commandLink id="resetLink" value="" action="#{CollectionInfoBean.reset}" immediate="true" >
			<f:param name="collectionId" value="#{CollectionInfoBean.collectionInfo.id}" />
		</h:commandLink>
				
	</h:form>
	
	<jsp:include page="/includes/footer.jsp" />
</f:view>

</body>
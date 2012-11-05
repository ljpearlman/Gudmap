<!-- Author: Mehran Sharghi				-->
<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>

<body onload="getById('CollectionInformationForm:collectionName').focus()" >

<f:view>
	<jsp:include page="/includes/header.jsp" />

	<h:form id="CollectionInformationForm" >
		<h:inputHidden id="collectionId" value="#{CollectionInformationBean.collectionInfo.id}" 
						binding="#{CollectionInformationBean.collectionIdComponent}"/>
		<h:inputHidden id="collectionType" value="#{CollectionInformationBean.collectionInfo.type}" 
						binding="#{CollectionInformationBean.collectionTypeComponent}"/>
		<h:panelGrid styleClass="defaultWidth" style="position:absolute;z-index:100;top:250px; visibility:#{CollectionInformationBean.overwriteVisibility}" >
			<h:panelGrid bgcolor="#FFFFBB" columnClasses="centreAlign" style="border:1px solid black;" styleClass="centreAlign" cellpadding="5px">
				<h:outputText value='A collection with the name "#{CollectionInformationBean.collectionInfo.name}" already exist, do you want to overwrite it?' styleClass="plaintext"/>
				<h:panelGrid columns="2"  cellspacing="15px" styleClass="centreAlign" columnClasses="rightAlign, leftAlign">
					<h:commandButton value="Overwrite" action="#{CollectionInformationBean.overwriteCollection}" 
									 onclick="enableInputComponents(document.forms['CollectionInformationForm'])"/>
					<h:commandButton value="Cancel" action="#{CollectionInformationBean.cancelOverwrite}" 
									 onclick="enableInputComponents(document.forms['CollectionInformationForm'])" />
				</h:panelGrid>	
			</h:panelGrid>
		</h:panelGrid>

		<h:outputText value="Please complete the following information:<br/>" escape="false" />
		<h:panelGrid columns="2" cellspacing="4" cellpadding="12" width="100%"
					 rowClasses="header-stripey,header-nostripe" columnClasses="plaintextbold, plaintext" >
			<h:outputText value="Collection Name:" />
			<h:panelGrid>
				<h:inputText id="collectionName" value="#{CollectionInformationBean.collectionInfo.name}" size="35"
							 required="true" requiredMessage="A name must be specified for collection" 
							 validator="#{CollectionInformationBean.validateCollectionName}" 
							 disabled="#{CollectionInformationBean.renderOverwrite}"/>
				<h:message for="collectionName" styleClass="errorMessage" />
			</h:panelGrid>
			
			<h:outputText value="Description:" />
			<h:inputText id="description" value="#{CollectionInformationBean.collectionInfo.description}" size="60" 
						 style="cursor:text" disabled="#{CollectionInformationBean.renderOverwrite}" />
			
			<h:outputText value="Status:" />
			<h:selectOneRadio id="statusSelectionRadio" value="#{CollectionInformationBean.collectionInfo.status}" 
							  layout="LineDirection" disabled="#{CollectionInformationBean.renderOverwrite}" >
				<f:selectItem itemValue="0" itemLabel="private"/>
				<f:selectItem itemValue="1" itemLabel="public"/>
			</h:selectOneRadio>
			
			<h:outputText value="Type:" />
			<h:outputText value="#{CollectionInformationBean.type}" />
			
			<h:outputText value="Focus Group:" />
			<h:selectOneRadio id="focusGroupSelectionRadio" value="#{CollectionInformationBean.collectionInfo.focusGroup}" 
							  layout="pageDirection" disabled="#{CollectionInformationBean.renderOverwrite}" >
				<f:selectItems value="#{CollectionInformationBean.groupItems}" />
			</h:selectOneRadio>
		</h:panelGrid>	

		<h:outputText value="<br/>" escape="false" />		
		<h:panelGrid columns="3" width="100%" columnClasses="centreAlign, centreAlign, centreAlign" rendered="#{!CollectionInformationBean.renderOverwrite}" >
			<h:commandButton value="Store" action="#{CollectionInformationBean.storeCollection}" />
<%--
			<h:commandButton value="Reset" onclick="getById('CollectionInformationForm:resetLink').onclick();return false;" />
			<h:commandButton value="Cancel" action="#{CollectionInformationBean.cancel}" immediate="true"/>
--%>			
			<h:commandButton value="Reset" action="#{CollectionInformationBean.reset}" immediate="true"/>
			<h:commandButton value="Cancel" action="#{CollectionInformationBean.cancel}" immediate="true"/>
		</h:panelGrid>
		
		<h:commandLink id="resetLink" value="" action="#{CollectionInformationBean.reset}" immediate="true" >
			<f:param name="collectionId" value="#{CollectionInformationBean.collectionInfo.id}" />
		</h:commandLink>
				
	</h:form>
	
	<jsp:include page="/includes/footer.jsp" />
</f:view>

</body>
<!-- Author: Mehran Sharghi				-->
<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>

<body onload="getById('CollectionInformationForm:collectionName').focus()" >

<f:view>
	<jsp:include page="/includes/header.jsp" />

	<h:form id="CollectionInformationForm" >
		<h:inputHidden id="collectionId" value="#{CollectionInformationBeancollectionInfo.id}" 
						binding="#{CollectionInformationBeancollectionIdComponent}"/>
		<h:inputHidden id="collectionType" value="#{CollectionInformationBeancollectionInfo.type}" 
						binding="#{CollectionInformationBeancollectionTypeComponent}"/>
		<h:panelGrid styleClass="defaultWidth" style="position:absolute;z-index:100;top:250px; visibility:#{CollectionInformationBeanoverwriteVisibility}" >
			<h:panelGrid bgcolor="#FFFFBB" columnClasses="centreAlign" style="border:1px solid black;" styleClass="centreAlign" cellpadding="5px">
				<h:outputText value='A collection with the name "#{CollectionInformationBeancollectionInfo.name}" already exist, do you want to overwrite it?' styleClass="plaintext"/>
				<h:panelGrid columns="2"  cellspacing="15px" styleClass="centreAlign" columnClasses="rightAlign, leftAlign">
					<h:commandButton value="Overwrite" action="#{CollectionInformationBeanoverwriteCollection}" 
									 onclick="enableInputComponents(document.forms['CollectionInformationForm'])"/>
					<h:commandButton value="Cancel" action="#{CollectionInformationBeancancelOverwrite}" 
									 onclick="enableInputComponents(document.forms['CollectionInformationForm'])" />
				</h:panelGrid>	
			</h:panelGrid>
		</h:panelGrid>

		<h:outputText value="Please complete the following information:<br/>" escape="false" />
		<h:panelGrid columns="2" cellspacing="4" cellpadding="12" width="100%"
					 rowClasses="header-stripey,header-nostripe" columnClasses="plaintextbold, plaintext" >
			<h:outputText value="Collection Name:" />
			<h:panelGrid>
				<h:inputText id="collectionName" value="#{CollectionInformationBeancollectionInfo.name}" size="35"
							 required="true" requiredMessage="A name must be specified for collection" 
							 validator="#{CollectionInformationBeanvalidateCollectionName}" 
							 disabled="#{CollectionInformationBeanrenderOverwrite}"/>
				<h:message for="collectionName" styleClass="errorMessage" />
			</h:panelGrid>
			
			<h:outputText value="Description:" />
			<h:inputText id="description" value="#{CollectionInformationBeancollectionInfo.description}" size="60" 
						 style="cursor:text" disabled="#{CollectionInformationBeanrenderOverwrite}" />
			
			<h:outputText value="Status:" />
			<h:selectOneRadio id="statusSelectionRadio" value="#{CollectionInformationBeancollectionInfo.status}" 
							  layout="LineDirection" disabled="#{CollectionInformationBeanrenderOverwrite}" >
				<f:selectItem itemValue="0" itemLabel="private"/>
				<f:selectItem itemValue="1" itemLabel="public"/>
			</h:selectOneRadio>
			
			<h:outputText value="Type:" />
			<h:outputText value="#{CollectionInformationBeantype}" />
			
			<h:outputText value="Focus Group:" />
			<h:selectOneRadio id="focusGroupSelectionRadio" value="#{CollectionInformationBeancollectionInfo.focusGroup}" 
							  layout="pageDirection" disabled="#{CollectionInformationBeanrenderOverwrite}" >
				<f:selectItems value="#{CollectionInformationBeangroupItems}" />
			</h:selectOneRadio>
		</h:panelGrid>	

		<h:outputText value="<br/>" escape="false" />		
		<h:panelGrid columns="3" width="100%" columnClasses="centreAlign, centreAlign, centreAlign" rendered="#{!CollectionInformationBeanrenderOverwrite}" >
			<h:commandButton value="Store" action="#{CollectionInformationBeanstoreCollection}" />
<%--
			<h:commandButton value="Reset" onclick="getById('CollectionInformationForm:resetLink').onclick();return false;" />
			<h:commandButton value="Cancel" action="#{CollectionInformationBeancancel}" immediate="true"/>
--%>			
			<h:commandButton value="Reset" action="#{CollectionInformationBeanreset}" immediate="true"/>
			<h:commandButton value="Cancel" action="#{CollectionInformationBeancancel}" immediate="true"/>
		</h:panelGrid>
		
		<h:commandLink id="resetLink" value="" action="#{CollectionInformationBeanreset}" immediate="true" >
			<f:param name="collectionId" value="#{CollectionInformationBeancollectionInfo.id}" />
		</h:commandLink>
				
	</h:form>
	
	<jsp:include page="/includes/footer.jsp" />
</f:view>

</body>
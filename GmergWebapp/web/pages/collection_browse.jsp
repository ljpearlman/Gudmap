<!-- Author: Mehran Sharghi				-->
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<f:view>
	<jsp:include page="/includes/header.jsp" />

	
	<h:form id="collectionBrowseForm" >
		<h:panelGroup rendered="#{!CollectionBrowseBean.clipboard}" >
			<h:panelGrid columns="2" width="100%" cellspacing="5px"  styleClass="header-stripey" > 
				<h:panelGrid columns="2" columnClasses="plaintextbold, plaintext" > 
					<h:outputText value="Collection:" />
					<h:outputText value="#{CollectionBrowseBean.collectionInfo.name} (#{CollectionBrowseBean.clipboardName})" />
	
					<h:outputText value="Owner:" />
					<h:outputText value="#{CollectionBrowseBean.collectionInfo.ownerName}" />
					
					<h:outputText value="Last modified:" />
					<h:outputText value="#{CollectionBrowseBean.collectionInfo.lastUpdate}" />
					
					<h:outputText value="Status:" />
					<h:outputText value="#{CollectionBrowseBean.status}&nbsp;" escape="false" />
				</h:panelGrid>
				
				<h:panelGrid columns="2" columnClasses="plaintextbold, plaintext" > 
					<h:outputText value="Description:" />
					<h:outputText value="#{CollectionBrowseBean.collectionInfo.description}" />
					
					<h:outputText value="Focus group:" />
					<h:outputText value="#{CollectionBrowseBean.focusGroup}" />
			
					<h:outputText value="Entries:" />
					<h:outputText value="#{CollectionBrowseBean.collectionInfo.entries}" />
					
					<h:commandLink id="modifyCollection" action="#{CollectionBrowseBean.modifyCollection}" styleClass="plaintextbold" 
									rendered="#{CollectionBrowseBean.userOwnedCollection}" >
						<h:graphicImage value="/images/Modify_btn.png" title="Click to modify details of this collection" alt="Modify Details" styleClass="icon" />
						<f:param name="actionMethodCalled" value="true" />
						<f:param name="collectionId" value="#{CollectionBrowseBean.collectionId}" />
					</h:commandLink>
					<h:outputText value="&nbsp;" escape="false"/>
				</h:panelGrid>
			</h:panelGrid>
			<h:outputText value="&nbsp;" escape="false"/>
		</h:panelGroup>
<%--  	
		<h:panelGrid columns="2" columnClasses="plaintextbold, plaintext" rendered="#{CollectionBrowseBean.clipboard}" >
			<h:outputText value="Display my: " />
			<h:selectOneMenu value="#{CollectionBrowseBean.collectionType}"
							 onchange="getById('collectionBrowseForm:reload').onclick()">
				<f:selectItems value="#{CollectionBrowseBean.collectionCategories}" />
			</h:selectOneMenu>
		</h:panelGrid>
	
		<h:commandLink id="reload" value="" action="#{CollectionBrowseBean.reload}" >
			<f:param name="actionMethodCalled" value="true" />
		</h:commandLink>
--%>		
	</h:form>

	<%-- only shown when the collection is viewed in heat map - ie MOE430 or ST1 --%>
	<h:panelGroup rendered="#{CollectionBrowseBean.collectionType=='3' || CollectionBrowseBean.collectionType=='4' && MasterTableBrowseBean.viewMode=='0'}" >
		<h:outputText value=" Microarray Expression Profile for: " styleClass="plaintextbold" />
		<h:outputText value="#{MasterTableBrowseBean.title}" styleClass="plaintext" />
		
		<h:outputLink id="topGene" target="_blank" 
					value ="http://toppgene.cchmc.org/ToppGene/CheckInput.action?training_set=#{MasterTableBrowseBean.geneList}&type=HGNC&query=TOPPFUN">
<%-- 			<h:outputText value="Send To ToppFun" /> --%>
			<h:graphicImage value="../images/ToppFun_Button.png" width="60"/>
		</h:outputLink>
		
		<h:outputLink styleClass="plaintextbold" value ="http://www.gudmap.org/Help/Microarray_Help.html">
			<h:outputText value= "  (View Microarray Analysis Help)" />
		</h:outputLink>			
		
		<rich:tabPanel  switchType="client" >
			<rich:tab label="#{MasterTableBrowseBean.allMasterTables[0].info.title}" rendered="#{MasterTableBrowseBean.allMasterTables[0].selected}" >
				<f:subview id="masterTableBrowse_t0">
					<h:outputText value="" rendered="#{MasterTableBrowseBean.allMasterTables[0].setTableViewName}" />
					<jsp:include page="../includes/browse_table.jsp" />
				</f:subview>
			</rich:tab>

			<rich:tab label="#{MasterTableBrowseBean.allMasterTables[1].info.title}" rendered="#{MasterTableBrowseBean.allMasterTables[1].selected}">
				<f:subview id="masterTableBrowse_t1">
					<h:outputText value="" rendered="#{MasterTableBrowseBean.allMasterTables[1].setTableViewName}" />
					<jsp:include page="../includes/browse_table.jsp" />
				</f:subview>
			</rich:tab>

			<rich:tab label="#{MasterTableBrowseBean.allMasterTables[2].info.title}" rendered="#{MasterTableBrowseBean.allMasterTables[2].selected}">
				<f:subview id="masterTableBrowse_t2">
					<h:outputText value="" rendered="#{MasterTableBrowseBean.allMasterTables[2].setTableViewName}" />
					<jsp:include page="../includes/browse_table.jsp" />
				</f:subview>
			</rich:tab>

			<rich:tab label="#{MasterTableBrowseBean.allMasterTables[3].info.title}" rendered="#{MasterTableBrowseBean.allMasterTables[3].selected}">
				<f:subview id="masterTableBrowse_t3">
					<h:outputText value="" rendered="#{MasterTableBrowseBean.allMasterTables[3].setTableViewName}" />
					<jsp:include page="../includes/browse_table.jsp" />
				</f:subview>
			</rich:tab>

			<rich:tab label="#{MasterTableBrowseBean.allMasterTables[4].info.title}" rendered="#{MasterTableBrowseBean.allMasterTables[4].selected}">
				<f:subview id="masterTableBrowse_t4">
					<h:outputText value="" rendered="#{MasterTableBrowseBean.allMasterTables[4].setTableViewName}" />
					<jsp:include page="../includes/browse_table.jsp" />
				</f:subview>
			</rich:tab>

			<rich:tab label="#{MasterTableBrowseBean.allMasterTables[5].info.title}" rendered="#{MasterTableBrowseBean.allMasterTables[5].selected}">
				<f:subview id="masterTableBrowse_t5">
					<h:outputText value="" rendered="#{MasterTableBrowseBean.allMasterTables[5].setTableViewName}" />
					<jsp:include page="../includes/browse_table.jsp" />
				</f:subview>
			</rich:tab>

			<rich:tab label="#{MasterTableBrowseBean.allMasterTables[6].info.title}" rendered="#{MasterTableBrowseBean.allMasterTables[6].selected}">
				<f:subview id="masterTableBrowse_t6">
					<h:outputText value="" rendered="#{MasterTableBrowseBean.allMasterTables[6].setTableViewName}" />
					<jsp:include page="../includes/browse_table.jsp" />
				</f:subview>
			</rich:tab>

		</rich:tabPanel>
	</h:panelGroup>	


	<%-- only shown when the collection is not viewed in heat map - ie Entities, Genes or Images --%>
	<h:panelGroup rendered="#{CollectionBrowseBean.collectionType=='0' || CollectionBrowseBean.collectionType=='1' || CollectionBrowseBean.collectionType=='2'}" >
		<f:subview id="collectionBrowse">
			<jsp:include page="../includes/browse_table.jsp" />
		</f:subview>
	</h:panelGroup>


	<%-- ==================================== Collection Buttons Section ==================================== --%>
	<h:form id="CollectionBrowseButtonsForm" style="margin:0px" rendered="#{!TableBean.tableEmpty}">
		<h:panelGrid columns="2" styleClass="header-stripey" width="100%" columnClasses="align-left,align-right" rendered="#{CollectionBrowseBean.clipboard}" >
		
<%-- this should replace the following if using jsf1.1
			<h:commandLink id="deleteFromCollection" styleClass="plaintextbold" action="#{CollectionBrowseBean.removeSelectedItems}" 
						   onmousedown="return processSelectionsForAction(event, 'collectionBrowse')">
				<h:outputText value="Remove selected items from #{CollectionBrowseBean.collectionName}" />
				<f:param name="actionMethodCalled" value="true" />
			</h:commandLink>
--%>			
			<h:commandLink id="deleteFromCollection" styleClass="plaintextbold" action="#{CollectionBrowseBean.removeSelectedItems}" 
						   onclick="return processSelectionsForAction(event, 'collectionBrowse')">
				<h:outputText value="Remove selected items from #{CollectionBrowseBean.collectionName}" />
				<f:param name="actionMethodCalled" value="true" />
			</h:commandLink>
<%-- this should replace the following if using jsf1.1
			<h:panelGrid styleClass="rightAlign" >
				<h:commandLink id="emptyCollection" styleClass="plaintextbold" action="#{CollectionBrowseBean.emptyCollection}"  
							   onmousedown="passTableViewName(event, 'collectionBrowse')" >
					<h:outputText value="Remove all items from #{CollectionBrowseBean.collectionName}" />
					<f:param name="actionMethodCalled" value="true" />
				</h:commandLink>
			</h:panelGrid>
--%>			
			<h:panelGrid styleClass="rightAlign" >
				<h:commandLink id="emptyCollection" styleClass="plaintextbold" action="#{CollectionBrowseBean.emptyCollection}"  
							   onclick="if (confirm('Are you sure you want to remove all items?')) passTableViewName(event, 'collectionBrowse'); else return false;" >
					<h:outputText value="Remove all items from #{CollectionBrowseBean.collectionName}" />
					<f:param name="actionMethodCalled" value="true" />
				</h:commandLink>
			</h:panelGrid>
		</h:panelGrid>
		
		<h:panelGrid columns="2" styleClass="header-stripey" width="100%" columnClasses="align-left,align-right" >
			<h:commandLink id="downloadCollection" action="#{CollectionBrowseBean.downloadCollection}" styleClass="plaintextbold" >
				<h:outputText value="Download this collection" rendered="#{!CollectionBrowseBean.clipboard}"/>
				<h:outputText value="Download #{CollectionBrowseBean.collectionName}" rendered="#{CollectionBrowseBean.clipboard}"/>
				<f:param name="actionMethodCalled" value="true" />
			</h:commandLink>
			<h:panelGrid styleClass="rightAlign" >
				<h:outputLink id="shareCollection" styleClass="plaintextbold"
							value="collection_share.html?collectionId=#{CollectionBrowseBean.collectionId}&collectionType=#{CollectionBrowseBean.collectionType}" >
					<h:outputText value="Share this collection with another user" rendered="#{!CollectionBrowseBean.clipboard}"/>
					<h:outputText value="Share #{CollectionBrowseBean.collectionName} with another user" rendered="#{CollectionBrowseBean.clipboard}" />
				</h:outputLink>
			</h:panelGrid>
		</h:panelGrid>
	
		<h:panelGrid columns="2" styleClass="header-stripey" width="100%" columnClasses="align-left,align-right" 
					 rendered="#{UserBean.userLoggedIn && CollectionBrowseBean.clipboard}">
			<h:commandLink id="saveCollection" action="#{CollectionBrowseBean.saveCollection}" styleClass="plaintextbold">
				<h:outputText value="Store #{CollectionBrowseBean.collectionName} in database" />
				<f:param name="actionMethodCalled" value="true" />
			</h:commandLink>
			<h:panelGrid styleClass="rightAlign" >
				<h:outputLink value="collectionList_browse.html" styleClass="plaintextbold" >
					<h:outputText value="View stored collections" />
				</h:outputLink>
			</h:panelGrid>
		</h:panelGrid>	

	</h:form>

	<jsp:include page="/includes/footer.jsp" />
</f:view>


<!-- Author: Mehran Sharghi				-->
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>

<f:view>
	<jsp:include page="/includes/header.jsp" />
	
	<h:form id="collectionListBrowseForm" >
		<h:outputText value="List of " />
		<h:selectOneMenu value="#{CollectionListBrowseBean.collectionType}"
						 onchange="getById('collectionListBrowseForm:reload').onclick()">
			<f:selectItems value="#{CollectionListBrowseBean.collectionCategories}" />
		</h:selectOneMenu>
		<h:outputText value=" collections" />
		
		<h:panelGrid columns="2">
			<h:selectBooleanCheckbox value="#{CollectionListBrowseBean.displayOwns}" onclick="getById('collectionListBrowseForm:reload').onclick()" />
			<h:outputText value="Owned collections" styleClass="plaintext" />
			<h:selectBooleanCheckbox value="#{CollectionListBrowseBean.displayPublics}" onclick="getById('collectionListBrowseForm:reload').onclick()" />
			<h:outputText value="Public collections" styleClass="plaintext" />
		</h:panelGrid>
		
		<h:commandLink id="reload" value="" action="#{CollectionListBrowseBean.reload}" >
			<f:param name="actionMethodCalled" value="true" />
		</h:commandLink>		
		
	</h:form>
	
	<f:subview id="collectionListBrowse">
		<jsp:include page="../includes/browse_table.jsp" />
	</f:subview>

	<%-- ==================================== Collection Buttons Section ==================================== --%>
	<h:form id="collectionListButtonsForm" >
		<h:panelGrid columns="2" styleClass="header-stripey" width="100%" columnClasses="align-left,align-right" >
<%-- this should replace the following if using jsf1.1
			<h:commandLink id="RemoveCollections" styleClass="plaintextbold" action="#{CollectionListBrowseBean.removeCollections}" 
						   onmousedown="return processSelectionsForAction(event, 'collectionListBrowse')" >
				<h:outputText value="Remove Collections" />
				<f:param name="actionMethodCalled" value="true" />
			</h:commandLink>
--%>
			<h:commandLink id="removeCollections" styleClass="plaintextbold" action="#{CollectionListBrowseBean.removeCollections}" 
						   onclick="if (processSelectionsForAction(event, 'collectionListBrowse')) return confirm('Are you sure you want to remove selected collection(s)?'); else return false"  >
				<h:outputText value="Remove Collections" />
				<f:param name="actionMethodCalled" value="true" />
			</h:commandLink>
			<h:panelGroup>
				<h:outputLink id="getUnion" styleClass="plaintextbold" 
							  onclick="return collectionListOperationClicked(event, 'collectionListBrowse', 'collectionListButtonsForm:collectionAttribute1')"
							  value="collectionOperation_browse.html?collectionOperation=union&collectionType=#{CollectionListBrowseBean.collectionType}">
					<h:outputText value="Get Union" />
					<h:outputText value=" over " rendered="#{CollectionListBrowseBean.attributes}" />
				</h:outputLink>
				<h:selectOneMenu id="collectionAttribute1" value="#{CollectionListBrowseBean.collectionAttribute}" rendered="#{CollectionListBrowseBean.attributes}"
								 onchange="getById('collectionListButtonsForm:getUnion').onclick()" >
					<f:selectItems value="#{CollectionListBrowseBean.collectionAttributes}" />
				</h:selectOneMenu>
			</h:panelGroup>

			<h:outputLink id="uploadCollection" value="collection_upload.html" styleClass="plaintextbold">
				<h:outputText value="Upload Collection" />
			</h:outputLink>
			<h:panelGroup>
				<h:outputLink id="getIntersection" styleClass="plaintextbold" 
							  onclick="return collectionListOperationClicked(event, 'collectionListBrowse', 'collectionListButtonsForm:collectionAttribute2')"
							  value="collectionOperation_browse.html?collectionOperation=intersection&collectionType=#{CollectionListBrowseBean.collectionType}">
					<h:outputText value="Get Intersection " />
						<h:outputText value=" over " rendered="#{CollectionListBrowseBean.attributes}" />
				</h:outputLink>
				<h:selectOneMenu id="collectionAttribute2" value="#{CollectionListBrowseBean.collectionAttribute}" rendered="#{CollectionListBrowseBean.attributes}" >
					<f:selectItems value="#{CollectionListBrowseBean.collectionAttributes}" />
				</h:selectOneMenu>
			</h:panelGroup>

			<h:outputLink id="viewClipboard" styleClass="plaintextbold" 
						  value="collection_browse.html?collectionId=clipboard&collectionType=#{CollectionListBrowseBean.collectionType}">
				<h:outputText value="View My selections" />
			</h:outputLink>
			<h:panelGroup>
				<h:outputLink id="getDifference" styleClass="plaintextbold" 
							  onclick="return collectionListOperationClicked(event, 'collectionListBrowse', 'collectionListButtonsForm:collectionAttribute3')"
							  value="collectionDifference_browse.html?collectionOperation=difference&collectionType=#{CollectionListBrowseBean.collectionType}">
					<h:outputText value="Compare two collections " />
						<h:outputText value=" over " rendered="#{CollectionListBrowseBean.attributes}" />
				</h:outputLink>
				<h:selectOneMenu id="collectionAttribute3" value="#{CollectionListBrowseBean.collectionAttribute}" rendered="#{CollectionListBrowseBean.attributes}" >
					<f:selectItems value="#{CollectionListBrowseBean.collectionAttributes}" />
				</h:selectOneMenu>
			</h:panelGroup>
			
		</h:panelGrid>
		
		<%-- hidden link for download; clicked by links within template section of the page --%>
		<h:commandLink id="downloadCollectionLink" action="#{CollectionListBrowseBean.downloadCollection}" >
			<f:param name="actionMethodCalled" value="true" />
		</h:commandLink>
		
	</h:form>
	
	<jsp:include page="/includes/footer.jsp" />
</f:view>


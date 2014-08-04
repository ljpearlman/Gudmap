<!-- Author: Mehran Sharghi				-->
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>


<f:view>
	<jsp:include page="/includes/header.jsp" />

	<h:outputText value=" Note: Changing the column selection will default back to the first tab (A-B)" style="font-style:italic;font-size:0.8em;"/>
	<h:panelGrid columns="2" columnClasses="plaintextbold, plaintext" > 
		<h:outputText value="Compare two collections:" />
		<h:outputText value="#{CollectionOperationBrowseBean.collectionNames}" escape="false"/>
	</h:panelGrid>
	<rich:tabPanel switchType="client" activeTabClass="header-stripey" tabClass="header-stripey">
		<rich:tab label="A - B" >
			<f:subview id="collectionDifferenceBrowse1">
				<h:outputText value="" rendered="#{CollectionOperationBrowseBean.setViewNameDifference1}"  />
				<jsp:include page="../includes/browse_table.jsp" />
			</f:subview>
		</rich:tab>
		<rich:tab label="B - A" >
			<f:subview id="collectionDifferenceBrowse2">
				<h:outputText value="" rendered="#{CollectionOperationBrowseBean.setViewNameDifference2}" />
				<jsp:include page="../includes/browse_table.jsp" />
			</f:subview>
		</rich:tab>
		<rich:tab label="A #{CollectionOperationBrowseBean.intersectionSymbol} B" >
			<f:subview id="collectionDifferenceBrowse3">
				<h:outputText value="" rendered="#{CollectionOperationBrowseBean.setViewNameIntersection}" />
				<jsp:include page="../includes/browse_table.jsp" />
			</f:subview>
		</rich:tab>
		<rich:tab label="A" >
			<f:subview id="collectionDifferenceBrowse4">
			<h:outputText value="" rendered="#{CollectionOperationBrowseBean.setViewNameFirstSet}" />
				<jsp:include page="../includes/browse_table.jsp" />
			</f:subview>
		</rich:tab>
		<rich:tab label="B" >
			<f:subview id="collectionDifferenceBrowse5">
				<h:outputText value="" rendered="#{CollectionOperationBrowseBean.setViewNameSecondSet}" />
				<jsp:include page="../includes/browse_table.jsp" />
			</f:subview>
		</rich:tab>
	</rich:tabPanel>
	<h:outputText value="" rendered="#{UtilityBean.setActiveTableViewName}" />
	
	<jsp:include page="/includes/footer.jsp" />
</f:view>


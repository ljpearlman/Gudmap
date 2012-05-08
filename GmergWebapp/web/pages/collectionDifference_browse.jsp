<!-- Author: Mehran Sharghi				-->
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://gudmap.org/jsf/core" prefix="g"%>


<f:view>
	<jsp:include page="/includes/header.jsp" />
	<h:outputText value="" />
	<h:panelGrid columns="2" columnClasses="plaintextbold, plaintext" > 
		<h:outputText value="Compare two collections:" />
		<h:outputText value="#{CollectionOperationBrowseBean.collectionNames}" escape="false"/>
	</h:panelGrid>
	<g:tabbedPanel id="mytabbedpane" value="0"  activeTabStyleClass="header-stripey" activeSubStyleClass="header-stripey" tabContentStyleClass="header-stripey">
		<g:tabPane id="A-B" label="A - B" >
			<f:subview id="collectionDifferenceBrowse1">
				<h:outputText value="" rendered="#{CollectionOperationBrowseBean.setViewNameDifference1}" />
				<jsp:include page="../includes/browse_table.jsp" />
			</f:subview>
		</g:tabPane>
		<g:tabPane id="B-A" label="B - A" >
			<f:subview id="collectionDifferenceBrowse2">
				<h:outputText value="" rendered="#{CollectionOperationBrowseBean.setViewNameDifference2}" />
				<jsp:include page="../includes/browse_table.jsp" />
			</f:subview>
		</g:tabPane>
		<g:tabPane id="intersect" label="A #{CollectionOperationBrowseBean.intersectionSymbol} B" >
			<f:subview id="collectionDifferenceBrowse3">
				<h:outputText value="" rendered="#{CollectionOperationBrowseBean.setViewNameIntersection}" />
				<jsp:include page="../includes/browse_table.jsp" />
			</f:subview>
		</g:tabPane>
		<g:tabPane id="A" label="A" >
			<f:subview id="collectionDifferenceBrowse4">
			<h:outputText value="" rendered="#{CollectionOperationBrowseBean.setViewNameFirstSet}" />
				<jsp:include page="../includes/browse_table.jsp" />
			</f:subview>
		</g:tabPane>
		<g:tabPane id="B" label="B" >
			<f:subview id="collectionDifferenceBrowse5">
				<h:outputText value="" rendered="#{CollectionOperationBrowseBean.setViewNameSecondSet}" />
				<jsp:include page="../includes/browse_table.jsp" />
			</f:subview>
		</g:tabPane>
<%--
		<g:tabPane id="tab2" >
			<h:outputText styleClass="plaintext" value="#{arrayFocusBrowseBean.title}" escape="false" />
			<f:subview id="focusGeneTable">
				<h:outputText value="" rendered="#{arrayFocusBrowseBean.setTableViewName}" />
				<jsp:include page="../includes/browse_table.jsp" />
			</f:subview>
		</g:tabPane>
		<g:tabPane id="tab4" >
			<h:outputText styleClass="plaintext" value="#{SeriesBrowseBean.title}" escape="false" />
			<f:subview id="SeriesBrowseTable">
				<h:outputText value="" rendered="#{SeriesBrowseBean.setTableViewName}" />
				<jsp:include page="../includes/browse_table.jsp" />
			</f:subview>
		 </g:tabPane>
--%>		
	</g:tabbedPanel>
	<h:outputText value="" rendered="#{UtilityBean.setActiveTableViewName}" />
	
	<jsp:include page="/includes/footer.jsp" />
</f:view>


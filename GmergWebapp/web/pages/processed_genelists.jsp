<!-- Microarray analysis & Visualisation jsp interface				-->
<!-- Author: Mehran Sharghi																	 -->

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<f:view>
	<jsp:include page="/includes/header.jsp" />

	<h:outputText value="List of Gene Lists" styleClass="plaintextbold" />
	<h:outputText value="" rendered="#{ProcessedGenelistsBean.dumy}" />
	<f:subview id="processedGenelists">
		<jsp:include page="../includes/browse_table.jsp" />
	</f:subview>

<%--
	<h:form id="geneListDataForm">
		<h:outputText value="List of Gene Lists" styleClass="plaintextbold" />
		<f:verbatim><iframe src="../includes/browse_wide_table.jsp?</f:verbatim><h:outputText value='#{ProcessedGenelistsBean.tableView.requestParam}" height="#{ProcessedGenelistsBean.tableView.height}"' />
		<f:verbatim> width="100%" align="RIGHT" frameborder="no"> </iframe></f:verbatim>
	</h:form>		
--%>

	<jsp:include page="/includes/footer.jsp" />
</f:view>



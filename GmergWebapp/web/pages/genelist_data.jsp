<!-- Author: Mehran Sharghi																	 -->

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>

<f:view>
	<jsp:include page="/includes/header.jsp" />
	
	<h:outputText value="Gene List Table for:" styleClass="plaintextbold" />
	<h:outputText value="#{ProcessedGenelistDataBean.genelistName}" styleClass="plaintext" />
	<f:subview id="processedGenelistData">
		<jsp:include page="../includes/browse_table.jsp" />
	</f:subview>

<%--
	<h:form id="geneListDataForm">
		<h:outputText value="Gene List Table for:" styleClass="plaintextbold" />
		<h:outputText value="#{ProcessedGenelistDataBean.genelistName}" styleClass="plaintext" />
		<f:verbatim><iframe src="../includes/browse_wide_table.jsf?</f:verbatim><h:outputText value='#{ProcessedGenelistDataBean.tableView.requestParam}" height="#{ProcessedGenelistDataBean.tableView.height}"' />
		<f:verbatim> width="100%" align="RIGHT" frameborder="no"> </iframe></f:verbatim>
	</h:form>		
--%>

	<jsp:include page="/includes/footer.jsp" />
</f:view>

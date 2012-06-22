<!-- Author: Mehran Sharghi	-->
<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<f:view>
	<jsp:include page="/includes/header.jsp" />
<%-- 
	<h:outputText styleClass="plaintextbold" value="Gene Strip" />
--%>
	<h:outputText value="" rendered="#{GeneStripBrowseBean.dummy}" />
	<f:subview id="geneStripTable">
		<jsp:include page="../includes/browse_table.jsp" />
	</f:subview>

	<jsp:include page="/includes/footer.jsp" />
</f:view>
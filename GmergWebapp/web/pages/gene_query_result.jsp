<!-- Author: Xingjun Pi -->
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>

<f:view>
	<jsp:include page="/includes/header.jsp" />
<%--
	<h:outputText value="Gudmap #{GeneQueryResultBean.title} results"  />
	<h:outputText styleClass="plaintext" value="#{GeneQueryResultBean.title}" escape="false" rendered="#{GeneQueryResultBean.dumy}" />
 --%>
	<h:outputText styleClass="plaintext" value="#{GeneQueryResultBean.title}" escape="false" />
	<f:subview id="geneTable">
		<jsp:include page="../includes/browse_table.jsp" />
	</f:subview>
	
	<jsp:include page="/includes/footer.jsp" />
</f:view>
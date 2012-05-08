<!-- Author: Mehran Sharghi																	 -->
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>

<f:view>
	<jsp:include page="/includes/header.jsp" />
	
	<h:outputText value="Gudmap #{booleanQueryResultBean.title} results"  />
	<h:outputText value="" rendered="#{booleanQueryResultBean.dumy}" />
	<f:subview id="focusGeneTable">
		<jsp:include page="../includes/browse_table.jsp" />
	</f:subview>
	
	<jsp:include page="/includes/footer.jsp" />
</f:view>
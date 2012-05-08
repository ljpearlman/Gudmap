<!-- Author: Xingjun Pi - 26/08/2008																	 -->
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>

<f:view>
	<jsp:include page="/includes/header.jsp" />
	
	<h:outputText value="Transgenic submissions #{transgenicBrowseBean.organTitle}" escape="false" />
	<h:outputText value="--Gene: #{transgenicBrowseBean.gene}" rendered="#{transgenicBrowseBean.gene != ''}" escape="false" />
	<f:subview id="focusGeneTable">
		<jsp:include page="../includes/browse_table.jsp" />
	</f:subview>
	
	<jsp:include page="/includes/footer.jsp" />
</f:view>
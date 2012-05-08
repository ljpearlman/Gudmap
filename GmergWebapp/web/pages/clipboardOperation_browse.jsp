<!-- Author: Mehran Sharghi				-->
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>

<f:view>
	<jsp:include page="/includes/header.jsp" />

	<h:outputText value="#{ClipboardOperationBrowseBean.operation} with my #{ClipboardOperationBrowseBean.clipboardName}" styleClass="plaintextbold" />
	
	<f:subview id="clipboardOperationBrowse">
		<jsp:include page="../includes/browse_table.jsp" />
	</f:subview>
	
	<jsp:include page="/includes/footer.jsp" />
</f:view>

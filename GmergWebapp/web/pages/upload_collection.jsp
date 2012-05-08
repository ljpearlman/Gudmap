<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<f:view>

	<jsp:include page="/includes/header.jsp" />
  
	<h:outputText value="" rendered="#{uploadCollectionBean.dumy}" />
	<h:outputText value="Your Uploaded Collection:" styleClass="plaintextbold" />
	<f:subview id="CollectionTable">
		<jsp:include page="../includes/browse_table.jsp" />
	</f:subview >
	
	<jsp:include page="/includes/footer.jsp" />
	
</f:view>
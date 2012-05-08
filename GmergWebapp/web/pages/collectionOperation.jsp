<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>

<f:view>
	<jsp:include page="/includes/header.jsp" />

	<h:outputText value="Collection Operation Result:" styleClass="plaintextbold" />
	
	<f:subview id="CollectionOperationTable" >
		<h:outputText value="" rendered="#{CollectionBean.setViewNameCollectionOperation}" />
		<jsp:include page="../includes/browse_table.jsp" flush="false"/>
	</f:subview>
  
	<h:outputText value="Your Collection: " styleClass="plaintextbold" />
	
	<f:subview id="CollectionTable">
		<h:outputText value="" rendered="#{CollectionBean.setViewNameCollection}" />
		<jsp:include page="../includes/browse_table.jsp" flush="false"/>
	</f:subview>
	
	<jsp:include page="/includes/footer.jsp" />
</f:view>
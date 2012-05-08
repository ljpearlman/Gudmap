<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>

<%-- xingjun - 30/09/2010 - moved below style into gudmap_css.css
<head>
	<style>
		.centreAlign {
			text-align: center;
			align: center;
		}
	</style>
</head>
--%>
<f:view>
	<jsp:include page="/includes/header.jsp">
		<jsp:param name="headerParam" value="onlyHeader" />
	</jsp:include>
	
	<h:form id="timeoutForm" >
		<h:panelGrid cellpadding="10" styleClass="centreAlign" style="margin:auto" >
			<h:outputText value="&nbsp;" escape="false" />
			<h:outputText styleClass="plaintext" value="Your session has expired! You are now being redirected to the database home page." />
			<h:commandButton value="Continue" action="#{DatabaseHomepageBean.redirectToDatabaseHomepage}" />
		</h:panelGrid>
	</h:form>
	
	<jsp:include page="/includes/footer.jsp" />
</f:view>

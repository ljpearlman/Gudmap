<!-- Author: Mehran Sharghi																	 -->
<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>

<f:view>
	<jsp:include page="/includes/header.jsp">
		<jsp:param name="headerParam" value="onlyHeader" />
	</jsp:include>
	
	<h:form id="restrictedAccessForm" >
		<h:panelGrid styleClass="centreAlign" >
			<h:panelGrid  style="margin:auto" cellspacing="10" styleClass="centreAlign" >
				<h:outputText value="&nbsp;" escape="false" />
				<h:outputText value="To access this page you are required to log in" />
			</h:panelGrid>
			<h:panelGrid columns="2" cellspacing="20" styleClass="centreAlign" >
				<h:outputLink value="secure/login.html" styleClass="plaintextbold" >
					<h:outputText value="<Login>"/>
				</h:outputLink>
				<h:outputLink value="#{UtilityBean.refererURI}" styleClass="plaintextbold" >
					<h:outputText value="<Cancel>"/>
				</h:outputLink>
			</h:panelGrid>
		</h:panelGrid>
	</h:form>
	
	<jsp:include page="/includes/footer.jsp" />
</f:view>

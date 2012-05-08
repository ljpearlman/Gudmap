<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<f:view>
	<jsp:include page="/includes/header.jsp" />
	
	<h:form id="messageForm" >
		<h:inputHidden id="targetPage" value="#{MessageBean.targetPage}" />
		<h:panelGrid width="100%" styleClass="centreAlign" >
			<h:dataTable value="#{MessageBean.messages}" var="message" width="100%" columnClasses="leftAlign">
				<h:column>
					<h:outputText value="#{message}" styleClass="plaintext" escape="#{MessageBean.escape}"/>
				</h:column>
			</h:dataTable>
			<h:commandButton id="continue" value="Continue" action="#{MessageBean.navigateTarget}"/>
		</h:panelGrid>	
	</h:form>
	
	<jsp:include page="/includes/footer.jsp" />
</f:view>
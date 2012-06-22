<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<html>
<head>
	<title>Gudmap Annotate Edit Component Note Window</title>
	<link href="${pageContext.request.contextPath}/css/gudmap_css.css" type="text/css" rel="stylesheet">
	<style type="text/css">@import("${pageContext.request.contextPath}/css/ie51.css");</style>
</head>
<body style="overflow-x:scroll; width:500px;overflow: -moz-scrollbars-horizontal;"  onbeforeunload="window.opener.getById('annotationForm').submit();">
	<f:view>
	
		<h:form id="editExpressionNoteForm">
			<h:inputHidden id="hiddenSubId" value="#{EditExpressionSupportBean.expressionDetail.submissionId}" />
<%-- 
			<h:inputHidden id="originalStrength" value="#{EditExpressionBean.primaryStrength}" />
			<h:inputHidden id="tempStrength" value="#{EditExpressionBean.primaryStrength}" />
--%>
			<h:outputText styleClass="titletext" value="Expression<br/>" escape="false" />
			<h:panelGrid width="100%" columns="1" rowClasses="header-stripey,header-nostripe" >
				<h:outputText styleClass="plaintextbold" value="#{EditExpressionBean.expressionDetail.submissionId}"/>
			</h:panelGrid>
			<f:verbatim><br/></f:verbatim>

			<h:panelGrid width="100%" columns="2" rowClasses="header-stripey,header-nostripe" columnClasses="leftCol,rightCol">
				<h:outputText value="Stage" />
				<h:outputText value="#{EditExpressionBean.expressionDetail.stage}" />

				<f:verbatim>&nbsp;</f:verbatim>
				<f:verbatim>&nbsp;</f:verbatim>

				<h:outputText value="Component:" />
				<h:panelGrid columns="2" columnClasses="text-top,data-textCol">
					<h:outputText styleClass="plaintext" value="Name:" />
					<h:outputText styleClass="datatext" value="#{EditExpressionBean.expressionDetail.componentName}" />
					<h:outputText styleClass="plaintext" value="ID:" />
					<h:outputText styleClass="datatext" value="#{EditExpressionBean.expressionDetail.componentId}" />
					<h:outputText styleClass="plaintext" value="Main Path:" />
					<h:dataTable style="vertical-align:bottom" value="#{EditExpressionBean.expressionDetail.componentDescription}" var="component">
						<h:column>
							<h:outputText styleClass="datatext" value="#{component}" />
						</h:column>
					</h:dataTable>
				</h:panelGrid>

				<f:verbatim>&nbsp;</f:verbatim>
				<f:verbatim>&nbsp;</f:verbatim>
			</h:panelGrid>
				<h:panelGrid columns="2" columnClasses="text-top,data-textCol">
					<h:outputText value="Annotation Note:"/>
					<h:inputTextarea id="note" cols="40" rows="5" styleClass="datatext" value="#{EditExpressionBean.expressionDetail.expressionNote}" onchange="copyNote()"
								disabled="#{UserBean.user.userType=='EXAMINER' 
									|| UserBean.user.userPrivilege==3 && (EditExpressionBean.expressionDetail.submissionDbStatus==3 || EditExpressionBean.expressionDetail.submissionDbStatus==4 || EditExpressionBean.expressionDetail.submissionDbStatus>19) 
									|| UserBean.user.userPrivilege==4 && (EditExpressionBean.expressionDetail.submissionDbStatus==3 || EditExpressionBean.expressionDetail.submissionDbStatus==4 || EditExpressionBean.expressionDetail.submissionDbStatus>21) 
									|| UserBean.user.userPrivilege==5 && (EditExpressionBean.expressionDetail.submissionDbStatus==3 || EditExpressionBean.expressionDetail.submissionDbStatus>23)
									}"/>
				</h:panelGrid>
			<f:verbatim><br/></f:verbatim>
			<h:panelGrid width="100%" columns="2" styleClass="header-stripey" >
				<h:commandButton id="closeButton" value="Close" onclick="window.opener.getById('annotationForm').submit(); self.close()" type="button" />
				<h:commandLink id="updateButton" value="Save Modification" action="#{EditExpressionSupportBean.updateExpressionNote}" 
							rendered="#{UserBean.userLoggedIn && UserBean.user.userType!='EXAMINER' 
								&& (UserBean.user.userPrivilege>5 
									|| EditExpressionBean.expressionDetail.submissionDbStatus==2
									|| UserBean.user.userPrivilege==3 && EditExpressionBean.expressionDetail.submissionDbStatus>4 && EditExpressionBean.expressionDetail.submissionDbStatus<=19 
									|| UserBean.user.userPrivilege==4 && EditExpressionBean.expressionDetail.submissionDbStatus>4 && EditExpressionBean.expressionDetail.submissionDbStatus<=21 
									|| UserBean.user.userPrivilege==5 && EditExpressionBean.expressionDetail.submissionDbStatus>=4 && EditExpressionBean.expressionDetail.submissionDbStatus<=23)}">
<%-- 
					<f:param name="submissionId" value="#{EditExpressionBean.expressionDetail.submissionId}" />
					<f:param name="componentId" value="#{EditExpressionBean.expressionDetail.componentId}" />				
--%>
				</h:commandLink>
			</h:panelGrid>
		</h:form>
	</f:view>
</body>
</html>			
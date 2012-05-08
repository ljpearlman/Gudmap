<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<html>
<head>
	<title>Gudmap Annotate Edit Pattern Window</title>
	<link href="${pageContext.request.contextPath}/css/gudmap_css.css" type="text/css" rel="stylesheet">
	<style type="text/css">@import("${pageContext.request.contextPath}/css/ie51.css");</style>

</head>
<body style="overflow-x:scroll; width:500px;overflow: -moz-scrollbars-horizontal;"  onbeforeunload="window.opener.getById('annotationForm').submit();">
	<f:view>
	
		<h:form id="editExpressionPatternForm">
			<h:inputHidden id="hiddenSubId" value="#{editExpressionSupportBean.expressionDetail.submissionId}" />
			<h:inputHidden id="originalStrength" value="#{editExpressionBean.primaryStrength}" />
			<h:inputHidden id="tempStrength" value="#{editExpressionBean.primaryStrength}" />
			<h:outputText styleClass="titletext" value="Expression<br/>" escape="false" />
			<h:panelGrid width="100%" columns="1" rowClasses="header-stripey,header-nostripe" >
				<h:outputText styleClass="plaintextbold" value="#{editExpressionBean.expressionDetail.submissionId}"/>
			</h:panelGrid>
			<f:verbatim><br/></f:verbatim>

			<h:panelGrid width="100%" columns="2" rowClasses="header-stripey,header-nostripe" columnClasses="leftCol,rightCol">
				<h:outputText value="Stage" />
				<h:outputText value="#{editExpressionBean.expressionDetail.stage}" />

				<f:verbatim>&nbsp;</f:verbatim>
				<f:verbatim>&nbsp;</f:verbatim>

				<h:outputText value="Component:" />
				<h:panelGrid columns="2" columnClasses="text-top,data-textCol">
					<h:outputText styleClass="plaintext" value="Name:" />
					<h:outputText styleClass="datatext" value="#{editExpressionBean.expressionDetail.componentName}" />
					<h:outputText styleClass="plaintext" value="ID:" />
					<h:outputText styleClass="datatext" value="#{editExpressionBean.expressionDetail.componentId}" />
					<h:outputText styleClass="plaintext" value="Main Path:" />
					<h:dataTable style="vertical-align:bottom" value="#{editExpressionBean.expressionDetail.componentDescription}" var="component">
						<h:column>
							<h:outputText styleClass="datatext" value="#{component}" />
						</h:column>
					</h:dataTable>
				</h:panelGrid>

				<f:verbatim>&nbsp;</f:verbatim>
				<f:verbatim>&nbsp;</f:verbatim>
			</h:panelGrid>

			<h:panelGrid columns="1" styleClass="stripey">
				<f:verbatim>&nbsp;</f:verbatim>
				

				<h:dataTable border="1" cellpadding="2" cellspacing="0" id="patternTable" value="#{editExpressionBean.patterns}" var="pattern">
					<h:column>
						<f:facet name="header">
							<h:outputText id="select" value="Select" styleClass="plaintext"/>
						</f:facet>
						<h:selectBooleanCheckbox value="#{pattern.selected}"
							disabled="#{userBean.user.userType=='EXAMINER' 
									|| userBean.user.userPrivilege==3 && (editExpressionBean.expressionDetail.submissionDbStatus==3 || editExpressionBean.expressionDetail.submissionDbStatus==4 || editExpressionBean.expressionDetail.submissionDbStatus>19) 
									|| userBean.user.userPrivilege==4 && (editExpressionBean.expressionDetail.submissionDbStatus==3 || editExpressionBean.expressionDetail.submissionDbStatus==4 || editExpressionBean.expressionDetail.submissionDbStatus>21) 
									|| userBean.user.userPrivilege==5 && (editExpressionBean.expressionDetail.submissionDbStatus==3 || editExpressionBean.expressionDetail.submissionDbStatus>23)
									}"/>
					</h:column>
					<h:column>
						<f:facet name="header">
							<h:outputText value="Pattern" styleClass="plaintext"/>
						</f:facet>
<%-- 
						<h:selectOneMenu id="pattern" value="#{pattern.pattern}" >
--%>
						<h:selectOneMenu id="pattern" value="#{pattern.pattern}" styleClass="datatext" 
							immediate="" onchange="submit()" 
							valueChangeListener="#{editExpressionSupportBean.changePattern}"
							disabled="#{userBean.user.userType=='EXAMINER' 
									|| userBean.user.userPrivilege==3 && (editExpressionBean.expressionDetail.submissionDbStatus==3 || editExpressionBean.expressionDetail.submissionDbStatus==4 || editExpressionBean.expressionDetail.submissionDbStatus>19) 
									|| userBean.user.userPrivilege==4 && (editExpressionBean.expressionDetail.submissionDbStatus==3 || editExpressionBean.expressionDetail.submissionDbStatus==4 || editExpressionBean.expressionDetail.submissionDbStatus>21) 
									|| userBean.user.userPrivilege==5 && (editExpressionBean.expressionDetail.submissionDbStatus==3 || editExpressionBean.expressionDetail.submissionDbStatus>23)
									}">
							<f:selectItems value="#{editExpressionBean.patternItems}" />
						</h:selectOneMenu>
					</h:column>
					<h:column>
						<f:facet name="header">
							<h:outputText value="Location" styleClass="plaintext"/>
						</f:facet>
<%-- 
						<h:inputText id="location" styleClass="datatext" value="#{pattern.locations}" disabled="#{pattern.pattern == ''}"/>
--%>
                        <h:inputHidden id ="location" value="#{pattern.locations}" />
					<h:selectOneMenu id="locationAPart" styleClass="datatext" 
						value="#{pattern.locationAPart}" immediate="true" onchange="submit()" 
						valueChangeListener="#{editExpressionSupportBean.changeLocation}"
						rendered="#{pattern.pattern != null && pattern.pattern != ''}"
						disabled="#{userBean.user.userType=='EXAMINER' 
									|| userBean.user.userPrivilege==3 && (editExpressionBean.expressionDetail.submissionDbStatus==3 || editExpressionBean.expressionDetail.submissionDbStatus==4 || editExpressionBean.expressionDetail.submissionDbStatus>19) 
									|| userBean.user.userPrivilege==4 && (editExpressionBean.expressionDetail.submissionDbStatus==3 || editExpressionBean.expressionDetail.submissionDbStatus==4 || editExpressionBean.expressionDetail.submissionDbStatus>21) 
									|| userBean.user.userPrivilege==5 && (editExpressionBean.expressionDetail.submissionDbStatus==3 || editExpressionBean.expressionDetail.submissionDbStatus>23)
									}">
						<f:selectItems value="#{editExpressionBean.locationItems}" />
					</h:selectOneMenu>
					<h:selectOneMenu id="locationNPart" styleClass="datatext" 
					    value="#{pattern.locationNPart}"
						rendered="#{pattern.pattern != null && pattern.pattern != '' && pattern.locationAPart == 'adjacent to'}"
						disabled="#{userBean.user.userType=='EXAMINER' 
									|| userBean.user.userPrivilege==3 && (editExpressionBean.expressionDetail.submissionDbStatus==3 || editExpressionBean.expressionDetail.submissionDbStatus==4 || editExpressionBean.expressionDetail.submissionDbStatus>19) 
									|| userBean.user.userPrivilege==4 && (editExpressionBean.expressionDetail.submissionDbStatus==3 || editExpressionBean.expressionDetail.submissionDbStatus==4 || editExpressionBean.expressionDetail.submissionDbStatus>21) 
									|| userBean.user.userPrivilege==5 && (editExpressionBean.expressionDetail.submissionDbStatus==3 || editExpressionBean.expressionDetail.submissionDbStatus>23)
									}">
						<f:selectItems value="#{editExpressionBean.componentItems}"/>
					</h:selectOneMenu>
				</h:column>              
				</h:dataTable>
<%-- 
          <h:message for="patternSelectedCheck" styleClass="plainred"/>
          <br/>
          <h:inputHidden id="patternSelectedCheck" validator="#{editExpressionBean.validatePatternSelection}" value="required"/>
--%>
				<h:panelGrid columns="1" rendered="#{editExpressionBean.noPatternCheckedForDeletion}">
					<h:outputText value="Please make a selection before clicking on the Delete button." styleClass="plainred"/>
				</h:panelGrid>
				<h:panelGrid columns="1" rendered="#{editExpressionBean.errorCode==1}">
					<h:outputText value="Confliction exists! The expression cannot be PRESENT. At least one of its ancestors' expression is NOT DETECTED." styleClass="plainred"/>
				</h:panelGrid>
				<h:panelGrid columns="1" rendered="#{editExpressionBean.errorCode==2}">
					<h:outputText value="Confliction exists! The expression cannot be NOT DETECTED. At least one of its decendents' expression is PRESENT." styleClass="plainred"/>
				</h:panelGrid>
				<h:panelGrid columns="1" rendered="#{editExpressionBean.errorCode==3}">
					<h:outputText value="Confliction exists! The expression cannot be UNCERTAIN. At least one of its ancestors' expression is NOT DETECTED." styleClass="plainred"/>
				</h:panelGrid>
				<h:panelGrid columns="1" rendered="#{editExpressionBean.errorCode==7}">
					<h:outputText value="The annotation cannot be updated" styleClass="plainred"/>
				</h:panelGrid>

<%-- 
				<h:panelGrid columns="1" rendered="#{editExpressionBean.updateButtonClicked && editExpressionBean.errorCode==0}">
					<h:outputText value="Annotation updated" styleClass="plaingreen"/>
				</h:panelGrid>
--%>

				<h:panelGrid columns="3" cellspacing="5">
					<h:commandLink id="deletePatternButton" value="Delete Pattern" action="#{editExpressionSupportBean.deletePattern}" 
							rendered="#{userBean.userLoggedIn && userBean.user.userType!='EXAMINER' 
								&& (userBean.user.userPrivilege>5 
									|| editExpressionBean.expressionDetail.submissionDbStatus==2
									|| userBean.user.userPrivilege==3 && editExpressionBean.expressionDetail.submissionDbStatus>4 && editExpressionBean.expressionDetail.submissionDbStatus<=19 
									|| userBean.user.userPrivilege==4 && editExpressionBean.expressionDetail.submissionDbStatus>4 && editExpressionBean.expressionDetail.submissionDbStatus<=21 
									|| userBean.user.userPrivilege==5 && editExpressionBean.expressionDetail.submissionDbStatus>=4 && editExpressionBean.expressionDetail.submissionDbStatus<=23)}">
<%-- 
						<f:param name="submissionId" value="#{editExpressionBean.expressionDetail.submissionId}" />
						<f:param name="componentId" value="#{editExpressionBean.expressionDetail.componentId}" />
--%>
						<f:param name="primaryStrength" value="#{editExpressionBean.expressionDetail.primaryStrength}" />
					</h:commandLink>
					<h:commandLink id="addPatternButton" value="Add Pattern" action="#{editExpressionSupportBean.addPattern}" 
							rendered="#{userBean.userLoggedIn && userBean.user.userType!='EXAMINER' 
								&& (userBean.user.userPrivilege>5 
									|| editExpressionBean.expressionDetail.submissionDbStatus==2
									|| userBean.user.userPrivilege==3 && editExpressionBean.expressionDetail.submissionDbStatus>4 && editExpressionBean.expressionDetail.submissionDbStatus<=19 
									|| userBean.user.userPrivilege==4 && editExpressionBean.expressionDetail.submissionDbStatus>4 && editExpressionBean.expressionDetail.submissionDbStatus<=21 
									|| userBean.user.userPrivilege==5 && editExpressionBean.expressionDetail.submissionDbStatus>=4 && editExpressionBean.expressionDetail.submissionDbStatus<=23)}">
<%-- 
						<f:param name="submissionId" value="#{editExpressionBean.expressionDetail.submissionId}" />
						<f:param name="componentId" value="#{editExpressionBean.expressionDetail.componentId}" />
--%>
						<f:param name="primaryStrength" value="#{editExpressionBean.expressionDetail.primaryStrength}" />
					</h:commandLink>
					<h:commandLink id="cancelButton" value="Cancel Modification" action="#{editExpressionSupportBean.cancelModification}" 
							rendered="#{userBean.userLoggedIn && userBean.user.userType!='EXAMINER' 
								&& (userBean.user.userPrivilege>5 
									|| editExpressionBean.expressionDetail.submissionDbStatus==2
									|| userBean.user.userPrivilege==3 && editExpressionBean.expressionDetail.submissionDbStatus>4 && editExpressionBean.expressionDetail.submissionDbStatus<=19 
									|| userBean.user.userPrivilege==4 && editExpressionBean.expressionDetail.submissionDbStatus>4 && editExpressionBean.expressionDetail.submissionDbStatus<=21 
									|| userBean.user.userPrivilege==5 && editExpressionBean.expressionDetail.submissionDbStatus>=4 && editExpressionBean.expressionDetail.submissionDbStatus<=23)}">
<%-- 
						<f:param name="submissionId" value="#{editExpressionBean.expressionDetail.submissionId}" />
						<f:param name="componentId" value="#{editExpressionBean.expressionDetail.componentId}" />
--%>
					</h:commandLink>
				</h:panelGrid>
					
				<f:verbatim>&nbsp;</f:verbatim>
			</h:panelGrid>
			<f:verbatim><br/></f:verbatim>
			<h:panelGrid width="100%" columns="2" styleClass="header-stripey" >
				<h:commandButton id="closeButton" value="Close" onclick="window.opener.getById('annotationForm').submit(); self.close()" type="button" />
				<h:commandLink id="updateButton" value="Save Modification" action="#{editExpressionSupportBean.updateExpression}" 
							rendered="#{userBean.userLoggedIn && userBean.user.userType!='EXAMINER' 
								&& (userBean.user.userPrivilege>5 
									|| editExpressionBean.expressionDetail.submissionDbStatus==2
									|| userBean.user.userPrivilege==3 && editExpressionBean.expressionDetail.submissionDbStatus>4 && editExpressionBean.expressionDetail.submissionDbStatus<=19 
									|| userBean.user.userPrivilege==4 && editExpressionBean.expressionDetail.submissionDbStatus>4 && editExpressionBean.expressionDetail.submissionDbStatus<=21 
									|| userBean.user.userPrivilege==5 && editExpressionBean.expressionDetail.submissionDbStatus>=4 && editExpressionBean.expressionDetail.submissionDbStatus<=23)}">
<%-- 
					<f:param name="submissionId" value="#{editExpressionBean.expressionDetail.submissionId}" />
					<f:param name="componentId" value="#{editExpressionBean.expressionDetail.componentId}" />				
--%>
				</h:commandLink>
			</h:panelGrid>
		</h:form>
	</f:view>
</body>
</html>
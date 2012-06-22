<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<html>
<head>
	<title>Gudmap Edit Expression Window</title>
	<link href="${pageContext.request.contextPath}/css/gudmap_css.css" type="text/css" rel="stylesheet">
	<style type="text/css">@import("${pageContext.request.contextPath}/css/ie51.css");</style>
	<script type="text/javascript">
		function validateStrength() {
			orgstrength = document.forms['editExpressionForm'].elements['editExpressionForm:originalStrength'].value
			strength = document.forms['editExpressionForm'].elements['editExpressionForm:strength'].value
			// tmpsth = document.forms['editExpressionForm'].elements['editExpressionForm:tempStrength'].value
			if (orgstrength == "not examined") { // has no annotation
				var confirmAdd = confirm("You are trying to add a new annotation with strength value as " + strength + ". Are you sure?")
				if (confirmAdd == false) {
					document.forms['editExpressionForm'].elements['editExpressionForm:strength'].value =
					document.forms['editExpressionForm'].elements['editExpressionForm:originalStrength'].value
				}
			} else { // annotated
				if (strength == "not examined") {
					var confirmDelete = confirm("You are trying to delete the current annotation. Are you sure?")
					if (confirmDelete == false) {
						document.forms['editExpressionForm'].elements['editExpressionForm:strength'].value = 
						document.forms['editExpressionForm'].elements['editExpressionForm:originalStrength'].value
					} else {
						document.forms['editExpressionForm'].elements['editExpressionForm:patternTable'].style.display = 'block'
//						document.getElementById('editExpressionForm:patternTable').style.display = 'block'
					}
				}
			}
		}
	</script>
</head>

<body style="width:500px;overflow:auto;" onbeforeunload="window.opener.getById('mainForm').submit();">
	<f:view>
		<h:form id="editExpressionForm">
			<h:inputHidden id="hiddenSubId" value="#{EditExpressionSupportBean.expressionDetail.submissionId}" />
			<h:inputHidden id="originalStrength" value="#{EditExpressionBean.primaryStrength}" />
			<h:inputHidden id="tempStrength" value="#{EditExpressionBean.primaryStrength}" />
			<h:outputText styleClass="titletext" value="Expression<br/>" escape="false" />
			<h:panelGrid width="100%" columns="2" rowClasses="header-stripey,header-nostripe" >
				<h:outputText styleClass="plaintextbold" value="#{EditExpressionBean.expressionDetail.submissionId}"/>
				<h:outputText styleClass="#{DisplayLockTextInTreeBean.lockingTextDisplayStyle}" 
							value="#{DisplayLockTextInTreeBean.lockingText}"/>
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

			<h:panelGrid columns="1" styleClass="stripey">
				<f:verbatim>&nbsp;</f:verbatim>
				<h:panelGrid columns="2" columnClasses="text-top,data-textCol">
					<h:outputText value="Expression:" />
					<h:selectOneMenu id="strength" styleClass="datatext" 
						value="#{EditExpressionBean.primaryStrength}" immediate="true" onchange="submit()" 
						valueChangeListener="#{EditExpressionSupportBean.changeStrength}"
						disabled="#{!EditExpressionBean.userAccessPermitted}">
						<f:selectItems value="#{EditExpressionBean.strengthItems}"/>
					</h:selectOneMenu>
				
					<h:outputText value="Strength:" rendered="#{EditExpressionBean.strengthSelectionRendered}"/>
					<h:selectOneMenu id="additionalStrength" styleClass="datatext" 
						value="#{EditExpressionBean.secondaryStrength}" 
						rendered="#{EditExpressionBean.strengthSelectionRendered}" 
						disabled="#{EditExpressonBean.strengthSelectionDisabled}">
						<f:selectItems value="#{EditExpressionBean.additionalStrengthItems}" />
					</h:selectOneMenu>
				</h:panelGrid>

				<f:verbatim>&nbsp;</f:verbatim>

				<h:dataTable border="1" cellpadding="2" cellspacing="0" id="patternTable" value="#{EditExpressionBean.patterns}" var="pattern">
					<h:column>
						<f:facet name="header">
							<h:outputText id="select" value="Select" styleClass="plaintext"/>
						</f:facet>
						<h:selectBooleanCheckbox value="#{pattern.selected}" 
										rendered="#{EditExpressionBean.primaryStrength == 'present' || EditExpressionBean.primaryStrength == 'uncertain'}"
										disabled="#{!EditExpressionBean.userAccessPermitted}"/>
					</h:column>
					<h:column>
						<f:facet name="header">
							<h:outputText value="Pattern" styleClass="plaintext"/>
						</f:facet>
<%-- 
						<h:selectOneMenu id="pattern" value="#{pattern.pattern}" >
--%>
						<h:selectOneMenu id="pattern" styleClass="datatext" 
							value="#{pattern.pattern}" immediate="" onchange="submit()" 
							valueChangeListener="#{EditExpressionSupportBean.changePattern}"
							rendered="#{EditExpressionBean.patternSelectionRendered}"
							disabled="#{!EditExpressionBean.userAccessPermitted}">
							<f:selectItems value="#{EditExpressionBean.patternItems}" />
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
                        		valueChangeListener="#{EditExpressionSupportBean.changeLocation}" 
                        		rendered="#{(EditExpressionBean.primaryStrength == 'present' || EditExpressionBean.primaryStrength == 'uncertain') && pattern.pattern != null && pattern.pattern != ''}"
                        		disabled="#{!EditExpressionBean.userAccessPermitted}">
                        	<f:selectItems value="#{EditExpressionBean.locationItems}" />
                        </h:selectOneMenu>
                        <h:selectOneMenu id="locationNPart"	styleClass="datatext" 
                        		value="#{pattern.locationNPart}"
                        		rendered="#{pattern.pattern != null && pattern.pattern != '' && pattern.locationAPart == 'adjacent to'}"
                        		disabled="#{!EditExpressionBean.userAccessPermitted}">
                        	<f:selectItems value="#{EditExpressionBean.componentItems}"/>
                        </h:selectOneMenu>
					</h:column>              
				</h:dataTable>
<%-- 
          <h:message for="patternSelectedCheck" styleClass="plainred"/>
          <br/>
          <h:inputHidden id="patternSelectedCheck" validator="#{EditExpressionBean.validatePatternSelection}" value="required"/>
--%>
				<h:panelGrid columns="1" rendered="#{EditExpressionBean.noPatternCheckedForDeletion}">
					<h:outputText value="Please make a selection before clicking on the Delete button." styleClass="plainred"/>
				</h:panelGrid>
				<h:panelGrid columns="1" rendered="#{EditExpressionBean.errorCode==1}">
					<h:outputText value="Confliction exists! The expression cannot be PRESENT. At least one of its ancestors' expression is NOT DETECTED." styleClass="plainred"/>
				</h:panelGrid>
				<h:panelGrid columns="1" rendered="#{EditExpressionBean.errorCode==2}">
					<h:outputText value="Confliction exists! The expression cannot be NOT DETECTED. At least one of its decendents' expression is PRESENT." styleClass="plainred"/>
				</h:panelGrid>
				<h:panelGrid columns="1" rendered="#{EditExpressionBean.errorCode==3}">
					<h:outputText value="Confliction exists! The expression cannot be UNCERTAIN. At least one of its ancestors' expression is NOT DETECTED." styleClass="plainred"/>
				</h:panelGrid>
				<h:panelGrid columns="1" rendered="#{EditExpressionBean.errorCode==7}">
					<h:outputText value="The submission/annotation cannot be updated at moment" styleClass="plainred"/>
				</h:panelGrid>

<%-- 
				<h:panelGrid columns="1" rendered="#{EditExpressionBean.updateButtonClicked && EditExpressionBean.errorCode==0}">
					<h:outputText value="Annotation updated" styleClass="plaingreen"/>
				</h:panelGrid>
--%>
				<h:panelGrid columns="3" cellspacing="5">
					<h:commandLink id="deletePatternButton" value="Delete Pattern" action="#{EditExpressionSupportBean.deletePattern}" 
									rendered="#{EditExpressionBean.expressionEditButtonRendered}">
<%-- 
						<f:param name="submissionId" value="#{EditExpressionBean.expressionDetail.submissionId}" />
						<f:param name="componentId" value="#{EditExpressionBean.expressionDetail.componentId}" />
--%>
						<f:param name="primaryStrength" value="#{EditExpressionBean.expressionDetail.primaryStrength}" />
					</h:commandLink>
					<h:commandLink id="addPatternButton" value="Add Pattern" action="#{EditExpressionSupportBean.addPattern}" 
									rendered="#{EditExpressionBean.expressionEditButtonRendered}">
<%-- 
						<f:param name="submissionId" value="#{EditExpressionBean.expressionDetail.submissionId}" />
						<f:param name="componentId" value="#{EditExpressionBean.expressionDetail.componentId}" />
--%>
						<f:param name="primaryStrength" value="#{EditExpressionBean.expressionDetail.primaryStrength}" />
					</h:commandLink>
					<h:commandLink id="cancelButton" value="Cancel Modification" action="#{EditExpressionSupportBean.cancelModification}"
									rendered="#{EditExpressionBean.expressionEditButtonRendered}">
<%-- 
						<f:param name="submissionId" value="#{EditExpressionBean.expressionDetail.submissionId}" />
						<f:param name="componentId" value="#{EditExpressionBean.expressionDetail.componentId}" />
--%>
					</h:commandLink>
				</h:panelGrid>
					
				<f:verbatim>&nbsp;</f:verbatim>

				<h:panelGrid columns="2" columnClasses="text-top,data-textCol">
					<h:outputText value="Annotation Note:"/>
					<h:inputTextarea id="note" cols="40" rows="5" 
								value="#{EditExpressionBean.expressionDetail.expressionNote}" onchange="copyNote()" 
								disabled="#{EditExpressionBean.expressionNoteDisabled}"/>
				</h:panelGrid>
			</h:panelGrid>
			<f:verbatim><br/></f:verbatim>
			<h:panelGrid width="100%" columns="2" styleClass="header-stripey" >
				<h:commandLink id="unlockButton" value="Unlock Submission" action="#{EditExpressionSupportBean.unlockSubmission}" 
							rendered="#{EditExpressionBean.unlockButtonRendered}">
					<f:param name="submissionId" value="#{EditExpressionBean.expressionDetail.submissionId}" />
				</h:commandLink>
				<h:commandLink id="updateButton" value="Save Modification" action="#{EditExpressionSupportBean.updateExpression}" 
									rendered="#{EditExpressionBean.expressionEditButtonRendered}">
<%-- 
					<f:param name="submissionId" value="#{EditExpressionBean.expressionDetail.submissionId}" />
					<f:param name="componentId" value="#{EditExpressionBean.expressionDetail.componentId}" />				
--%>
				</h:commandLink>
			</h:panelGrid>
		</h:form>
		<f:verbatim><br/></f:verbatim>
		<f:verbatim><br/></f:verbatim>
        <h:panelGroup>
		  <h:commandButton id="closeButton" value="Close" onclick="window.opener.getById('mainForm').submit(); self.close()" type="button" />
		</h:panelGroup>
	</f:view>
</body>
</html>

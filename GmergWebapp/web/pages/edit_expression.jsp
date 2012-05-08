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
			<h:inputHidden id="hiddenSubId" value="#{editExpressionSupportBean.expressionDetail.submissionId}" />
			<h:inputHidden id="originalStrength" value="#{editExpressionBean.primaryStrength}" />
			<h:inputHidden id="tempStrength" value="#{editExpressionBean.primaryStrength}" />
			<h:outputText styleClass="titletext" value="Expression<br/>" escape="false" />
			<h:panelGrid width="100%" columns="2" rowClasses="header-stripey,header-nostripe" >
				<h:outputText styleClass="plaintextbold" value="#{editExpressionBean.expressionDetail.submissionId}"/>
				<h:outputText styleClass="#{displayLockTextInTreeBean.lockingTextDisplayStyle}" 
							value="#{displayLockTextInTreeBean.lockingText}"/>
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
				<h:panelGrid columns="2" columnClasses="text-top,data-textCol">
					<h:outputText value="Expression:" />
					<h:selectOneMenu id="strength" styleClass="datatext" 
						value="#{editExpressionBean.primaryStrength}" immediate="true" onchange="submit()" 
						valueChangeListener="#{editExpressionSupportBean.changeStrength}"
						disabled="#{!editExpressionBean.userAccessPermitted}">
						<f:selectItems value="#{editExpressionBean.strengthItems}"/>
					</h:selectOneMenu>
				
					<h:outputText value="Strength:" rendered="#{editExpressionBean.strengthSelectionRendered}"/>
					<h:selectOneMenu id="additionalStrength" styleClass="datatext" 
						value="#{editExpressionBean.secondaryStrength}" 
						rendered="#{editExpressionBean.strengthSelectionRendered}" 
						disabled="#{editExpressonBean.strengthSelectionDisabled}">
						<f:selectItems value="#{editExpressionBean.additionalStrengthItems}" />
					</h:selectOneMenu>
				</h:panelGrid>

				<f:verbatim>&nbsp;</f:verbatim>

				<h:dataTable border="1" cellpadding="2" cellspacing="0" id="patternTable" value="#{editExpressionBean.patterns}" var="pattern">
					<h:column>
						<f:facet name="header">
							<h:outputText id="select" value="Select" styleClass="plaintext"/>
						</f:facet>
						<h:selectBooleanCheckbox value="#{pattern.selected}" 
										rendered="#{editExpressionBean.primaryStrength == 'present' || editExpressionBean.primaryStrength == 'uncertain'}"
										disabled="#{!editExpressionBean.userAccessPermitted}"/>
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
							valueChangeListener="#{editExpressionSupportBean.changePattern}"
							rendered="#{editExpressionBean.patternSelectionRendered}"
							disabled="#{!editExpressionBean.userAccessPermitted}">
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
                        		rendered="#{(editExpressionBean.primaryStrength == 'present' || editExpressionBean.primaryStrength == 'uncertain') && pattern.pattern != null && pattern.pattern != ''}"
                        		disabled="#{!editExpressionBean.userAccessPermitted}">
                        	<f:selectItems value="#{editExpressionBean.locationItems}" />
                        </h:selectOneMenu>
                        <h:selectOneMenu id="locationNPart"	styleClass="datatext" 
                        		value="#{pattern.locationNPart}"
                        		rendered="#{pattern.pattern != null && pattern.pattern != '' && pattern.locationAPart == 'adjacent to'}"
                        		disabled="#{!editExpressionBean.userAccessPermitted}">
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
					<h:outputText value="The submission/annotation cannot be updated at moment" styleClass="plainred"/>
				</h:panelGrid>

<%-- 
				<h:panelGrid columns="1" rendered="#{editExpressionBean.updateButtonClicked && editExpressionBean.errorCode==0}">
					<h:outputText value="Annotation updated" styleClass="plaingreen"/>
				</h:panelGrid>
--%>
				<h:panelGrid columns="3" cellspacing="5">
					<h:commandLink id="deletePatternButton" value="Delete Pattern" action="#{editExpressionSupportBean.deletePattern}" 
									rendered="#{editExpressionBean.expressionEditButtonRendered}">
<%-- 
						<f:param name="submissionId" value="#{editExpressionBean.expressionDetail.submissionId}" />
						<f:param name="componentId" value="#{editExpressionBean.expressionDetail.componentId}" />
--%>
						<f:param name="primaryStrength" value="#{editExpressionBean.expressionDetail.primaryStrength}" />
					</h:commandLink>
					<h:commandLink id="addPatternButton" value="Add Pattern" action="#{editExpressionSupportBean.addPattern}" 
									rendered="#{editExpressionBean.expressionEditButtonRendered}">
<%-- 
						<f:param name="submissionId" value="#{editExpressionBean.expressionDetail.submissionId}" />
						<f:param name="componentId" value="#{editExpressionBean.expressionDetail.componentId}" />
--%>
						<f:param name="primaryStrength" value="#{editExpressionBean.expressionDetail.primaryStrength}" />
					</h:commandLink>
					<h:commandLink id="cancelButton" value="Cancel Modification" action="#{editExpressionSupportBean.cancelModification}"
									rendered="#{editExpressionBean.expressionEditButtonRendered}">
<%-- 
						<f:param name="submissionId" value="#{editExpressionBean.expressionDetail.submissionId}" />
						<f:param name="componentId" value="#{editExpressionBean.expressionDetail.componentId}" />
--%>
					</h:commandLink>
				</h:panelGrid>
					
				<f:verbatim>&nbsp;</f:verbatim>

				<h:panelGrid columns="2" columnClasses="text-top,data-textCol">
					<h:outputText value="Annotation Note:"/>
					<h:inputTextarea id="note" cols="40" rows="5" 
								value="#{editExpressionBean.expressionDetail.expressionNote}" onchange="copyNote()" 
								disabled="#{editExpressionBean.expressionNoteDisabled}"/>
				</h:panelGrid>
			</h:panelGrid>
			<f:verbatim><br/></f:verbatim>
			<h:panelGrid width="100%" columns="2" styleClass="header-stripey" >
				<h:commandLink id="unlockButton" value="Unlock Submission" action="#{editExpressionSupportBean.unlockSubmission}" 
							rendered="#{editExpressionBean.unlockButtonRendered}">
					<f:param name="submissionId" value="#{editExpressionBean.expressionDetail.submissionId}" />
				</h:commandLink>
				<h:commandLink id="updateButton" value="Save Modification" action="#{editExpressionSupportBean.updateExpression}" 
									rendered="#{editExpressionBean.expressionEditButtonRendered}">
<%-- 
					<f:param name="submissionId" value="#{editExpressionBean.expressionDetail.submissionId}" />
					<f:param name="componentId" value="#{editExpressionBean.expressionDetail.componentId}" />				
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

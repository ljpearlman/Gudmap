<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<fmt:setBundle basename="configuration" />

<fmt:message var="proj" scope="application" key="project" />
<fmt:message var="perspective" scope="application" key="perspective" />
<fmt:message var="stageSeriesShort" scope="application" key="stage_series_short" />
<fmt:message var="stageSeriesMedium" scope="application" key="stage_series_medium" />
<fmt:message var="stageSeriesLong" scope="application" key="stage_series_long" />
<fmt:message var="siteSpecies" scope="application" key="species"/>
<c:set var="pageName" value="${pageContext.request.servletPath}" />
<head>
	<style>
		.width95 {
			width:95%;
		}
		.width5 {
			width:5%;
		}
		td.width5 {
			text-align:right;
			vertical-align:bottom;
		}		
	</style>

	<title>GUDMAP Edit Annotation Tree Window</title>
		<link href="${pageContext.request.contextPath}/css/gudmap_css.css" type="text/css" rel="stylesheet">
	<link href="${pageContext.request.contextPath}/css/gudmapmain_css.css" type="text/css" rel="stylesheet">
	<link href="${pageContext.request.contextPath}/css/help.css" type="text/css" rel="stylesheet">
	<style type="text/css">
		@import("${pageContext.request.contextPath}/css/ie51.css");
	</style>
	<script src="${pageContext.request.contextPath}/scripts/formfunctions.js" type="text/javascript"></script>
	<script src="${pageContext.request.contextPath}/scripts/navbar.js" type="text/javascript"></script>
	<script src="${pageContext.request.contextPath}/scripts/navfunctions.js" type="text/javascript"></script>
	<script src="${pageContext.request.contextPath}/scripts/ua.js" type="text/javascript"></script>
	<script src="${pageContext.request.contextPath}/scripts/ftiens4.js" type="text/javascript"></script>
	<script src="${pageContext.request.contextPath}/scripts/header.js" type="text/javascript"></script>
	<%--
	<c:set var="userLoggedIn" value="<%= (session.getAttribute("UserBean")==null)?false:((gmerg.beans.UserBean)session.getAttribute("UserBean")).isUserLoggedIn() %>" />
	--%>
	<c:set var="refreshRequired" value="<%= gmerg.control.GudmapSessionTimeoutFilter.isRefreshRequired(request.getRequestURI()) %>" />
	<c:set var="maxInactiveInterval" value="<%= session.getMaxInactiveInterval() %>" />
	<c:if test="${refreshRequired}" >
		<meta http-equiv="refresh" content="${maxInactiveInterval}">
	</c:if>
	<c:set var="statusParams" value="<%= gmerg.utils.Visit.getStatus() %>" />
	<script type="text/javascript">
	
		function addStatusParam() {
			for (var i=0; i<document.forms.length; i++ ){
				var input = document.createElement('input');
				if (input) {
					input.type = 'hidden';
					input.name = 'statusParams';
					input.value = '${statusParams}';
				}
				document.forms[i].appendChild(input);
			}
		}
		
	</script>
	<c:set var="headerParam" value="${param['headerParam']}" />
</head>


<f:view>

	<h:outputText styleClass="plaintextbold" value="There are no entries in the database matching the specified submission id" rendered="#{!ISHSingleSubmissionBean.renderPage}"/>
	
	<h:form id="mainForm" rendered="#{ISHSingleSubmissionBean.renderPage}">
<%-- 
		<h:inputHidden id="hiddenSubmissionId" value="#{DisplayLockTextInTreeBean.submissionId}"/>
--%>
	    <h:panelGrid width="100%" columns="2" rowClasses="header-stripey,header-nostripe" >
			<h:outputText styleClass="plaintextbold" value="#{ISHSingleSubmissionBean.submission.accID}" />
			<h:outputText styleClass="#{DisplayLockTextInTreeBean.lockingTextDisplayStyle}" 
						value="#{DisplayLockTextInTreeBean.lockingText}" />
		</h:panelGrid>
		<h:panelGrid width="100%" columns="2" rowClasses="header-stripey,header-nostripe" columnClasses="leftCol,rightCol">
			<h:panelGroup>
				<h:outputText value="Expression Mapping <br/><br/>" escape="false"/>
				<h:outputText value="Expression Strengths Key:" styleClass="plaintextbold"/>
				<h:panelGrid columns="2">
					<h:graphicImage value="/images/tree/DetectedRoundPlus20x20.gif" styleClass="icon" />
					<h:outputText value="Present (unspecified strength)" styleClass="plaintext" />
			
					<h:graphicImage value="/images/tree/StrongRoundPlus20x20.gif" styleClass="icon" />
					<h:outputText value="Present (strong)" styleClass="plaintext" />
			
					<h:graphicImage value="/images/tree/ModerateRoundPlus20x20.gif" styleClass="icon" />
					<h:outputText value="Present (moderate)" styleClass="plaintext" />
					
					<h:graphicImage value="/images/tree/WeakRoundPlus20x20.gif" styleClass="icon" />
					<h:outputText value="Present (weak)" styleClass="plaintext" />
					
					<h:graphicImage value="/images/tree/PossibleRound20x20.gif" styleClass="icon" />
					<h:outputText value="Uncertain" styleClass="plaintext" rendered="#{proj == 'GUDMAP'}" />
					<h:outputText value="Possible" styleClass="plaintext" rendered="#{proj != 'GUDMAP'}" />
					
					<h:graphicImage value="/images/tree/NotDetectedRoundMinus20x20.gif" styleClass="icon" />
					<h:outputText value="Not Detected" styleClass="plaintext" />
					<f:verbatim>&nbsp;</f:verbatim><f:verbatim>&nbsp;</f:verbatim>
					</h:panelGrid>
					
					<h:outputText value="Expression Patterns Key:" styleClass="plaintextbold"/>
					<h:panelGrid columns="2">
					<h:graphicImage value="/images/tree/HomogeneousRound20x20.png" styleClass="icon" rendered="#{proj != 'GUDMAP'}" />
					<h:outputText value="Homogeneous" styleClass="plaintext" rendered="#{proj != 'GUDMAP'}" />
					
					<h:graphicImage value="/images/tree/GradedRound20x20.png" styleClass="icon" />
					<h:outputText value="Graded" styleClass="plaintext" />
					
					<h:graphicImage value="/images/tree/RegionalRound20x20.png" styleClass="icon" />
					<h:outputText value="Regional" styleClass="plaintext" />
					
					<h:graphicImage value="/images/tree/SpottedRound20x20.png" styleClass="icon" />
					<h:outputText value="Spotted" styleClass="plaintext" />
					
					<h:graphicImage value="/images/tree/UbiquitousRound20x20.png" styleClass="icon" />
					<h:outputText value="Ubiquitous" styleClass="plaintext" />
					
					<h:graphicImage value="/images/tree/OtherRound20x20.png" styleClass="icon" rendered="#{proj != 'GUDMAP'}" />
					<h:outputText value="Other" styleClass="plaintext" rendered="#{proj != 'GUDMAP'}" />
					
					<h:graphicImage value="/images/tree/RestrictedRound20x20.png" styleClass="icon" rendered="#{proj == 'GUDMAP'}" />
					<h:outputText value="Restricted" styleClass="plaintext" rendered="#{proj == 'GUDMAP'}" />
					
					<h:graphicImage value="/images/tree/SingleCellRound20x20.png" styleClass="icon" rendered="#{proj == 'GUDMAP'}" />
					<h:outputText value="Single cell" styleClass="plaintext" rendered="#{proj == 'GUDMAP'}" />
					
					<h:graphicImage value="/images/tree/note.gif" styleClass="icon" />
					<h:outputText value="Contains note" styleClass="plaintext" />
				</h:panelGrid>
			</h:panelGroup>
			<h:panelGroup>
		<h:panelGrid columns="2" columnClasses="table-stripey" bgcolor="white" cellspacing="2" cellpadding="4">

					<h:commandLink action="#{ISHSingleSubmissionBean.displayOfAnnotatedGps}" styleClass="plaintext">
						<h:outputText styleClass="plaintext" value="#{ISHSingleSubmissionBean.displayOfAnnotatedGpsTxt}" rendered="#{siteSpecies != 'Xenopus laevis'}" />
					</h:commandLink>
				</h:panelGrid>								
				<h:inputHidden value="#{ISHSingleSubmissionBean.submissionID}" />
				
				<h:panelGroup>
					<h:outputLink style="font-size:7pt;text-decoration:none;color:silver" value="http://www.treemenu.net/" target="_blank">
						<h:outputText value="Javascript Tree Menu" />
					</h:outputLink>
					<f:verbatim>
						<script type="text/javascript">
							<c:forEach items="${ISHSingleSubmissionBean.submission.annotationTree}" var="row">
								<c:out value="${row}" escapeXml="false"/>
							</c:forEach>
						</script>
						<script type="text/javascript">initializeDocument()</script>
						<noscript>
							<span class="plaintext">A tree of annotated anatomical components will open here if you enable JavaScript in your browser.</span>
						</noscript>
						&nbsp;
					</f:verbatim>
					<h:panelGroup rendered="#{siteSpecies != 'Xenopus laevis'}">
						<h:outputText styleClass="plaintextbold" value="G " />
						<h:outputText styleClass="plaintext" value="Group or group descendent. Groups provide alternative groupings of terms." />
					</h:panelGroup>
				</h:panelGroup>
			</h:panelGroup>
			</h:panelGrid>
<%--			
			<h:panelGroup rendered="#{UserBean.userLoggedIn && UserBean.user.userPrivilege>=3}">
						<h:commandButton value="Complete/Approve" actionListener="#{EditExpressionBean.annotationSignOff}">
						</h:commandButton>
			</h:panelGroup>
			<h:panelGroup rendered="#{UserBean.userLoggedIn && UserBean.user.userPrivilege>=4}">
						<h:commandButton value="Return to annotator" actionListener="#{EditExpressionBean.annotationReturnToAnnotator}">
						</h:commandButton>
			</h:panelGroup>
			<h:panelGroup rendered="#{UserBean.userLoggedIn && UserBean.user.userPrivilege>=6}">
						<h:commandButton value="Return to editor" actionListener="#{EditExpressionBean.annotationReturnToEditor}">
						</h:commandButton>
			</h:panelGroup>
			<h:panelGroup>
						<h:commandButton id="closeButton" value="Close" onclick="window.opener.closeImageViewer()" type="button"/>
			</h:panelGroup>
--%>			
	</h:form>	
<%--	<h:form id="buttonsForm" target="lab_ish_edit_window" >--%>
		<h:form id="buttonsForm">
		<h:panelGrid columns="4" cellspacing="5" styleClass="centreAlign">
			<h:commandButton value="Complete/Approve" action="#{EditExpressionSupportBean.annotationSignOff}" 
							onclick="setTimeout('window.opener.closeImageViewer()',100)"
							rendered="#{UserBean.userLoggedIn && UserBean.user.userPrivilege>=3 && UserBean.user.userType!='EXAMINER'}">
			</h:commandButton>
			<h:commandButton value="Return to annotator" action="#{EditExpressionSupportBean.annotationReturnToAnnotator}"
							onclick="setTimeout('window.opener.closeImageViewer()',100)"
							rendered="#{UserBean.userLoggedIn && UserBean.user.userPrivilege>=4 && UserBean.user.userType!='EXAMINER'}">
			</h:commandButton>
			<h:commandButton value="Return to editor" action="#{EditExpressionSupportBean.annotationReturnToEditor}"
							onclick="setTimeout('window.opener.closeImageViewer()',100)"
							rendered="#{UserBean.userLoggedIn && UserBean.user.userPrivilege>=6}">
			</h:commandButton>
			<h:commandButton id="closeButton" value="Close" onclick="window.opener.closeImageViewer();return false;" type="button"/>
		</h:panelGrid>
	</h:form>			
	
</f:view>
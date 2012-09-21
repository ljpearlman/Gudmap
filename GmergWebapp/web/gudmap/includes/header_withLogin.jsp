<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setBundle basename="configuration" />

<fmt:message var="proj" scope="application" key="project" />
<fmt:message var="perspective" scope="application" key="perspective" />
<fmt:message var="stageSeriesShort" scope="application" key="stage_series_short" />
<fmt:message var="stageSeriesMedium" scope="application" key="stage_series_medium" />
<fmt:message var="stageSeriesLong" scope="application" key="stage_series_long" />
<fmt:message var="siteSpecies" scope="application" key="species"/>
<c:set var="pageName" value="${pageContext.request.servletPath}" />

<f:subview id="headersubview">
<f:verbatim>

<html>
<head>
	<title>Gudmap Gene Expression Database</title>
	<link href="${pageContext.request.contextPath}/css/gudmap_css.css" type="text/css" rel="stylesheet">
	<link href="${pageContext.request.contextPath}/css/gudmapmain_css.css" type="text/css" rel="stylesheet">
	<link href="${pageContext.request.contextPath}/css/help.css" type="text/css" rel="stylesheet">
	<style type="text/css">
		@import("${pageContext.request.contextPath}/css/ie51.css");
	</style>
	<script src="${pageContext.request.contextPath}/scripts/formfunctions.js" type="text/javascript"></script>
	<script src="${pageContext.request.contextPath}/scripts/navbar.js" type="text/javascript"></script>
	<script src="${pageContext.request.contextPath}/scripts/navfunctions.js" type="text/javascript"></script>
	<script type="text/javascript" src="http://www.gudmap.org/javascripts/main.js"></script>
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
			for (var i=0; i<document.forms.length; i++ )
				appendHiddenInput(document.forms[i], 'statusParams', '${statusParams}');
		}
	</script>
	<c:set var="headerParam" value="${param['headerParam']}" />
</head>
	
<body class="database" onload="addStatusParam();<c:if test="${headerParam!='onlyHeader'}">adjustLoginPanelPosition()</c:if>" onunload="clearActionMethodsHistory()">

<c:out value="${statusParams}" />

<%-- this is a warning 3 minutes before session expiration. 
note that confirm box is not a good implementation because if the user doesn't answer it remains on the screen and the next one will display on top of it. should be implemented as a custom dialog and removed when the page is reloaded automatically.
	<c:if test="${refreshRequired}" >
		<script type="text/javascript">

/*
//		if (${refreshRequired})
			setTimeout("sessionExpirationWarning()", ${(maxInactiveInterval-180)*1000} )
			function sessionExpirationWarning() {
				setTimeout("window.location='http://www.google.com'",5000);
				var answer = confirm('Your session is going to expire soon!, Do you want to extend yor session?');
				if (answer)
					alert('should reload the page');
			}
*/		
			</script>
	</c:if>
--%>
	<div id="wrapper">
		<div id="header">
			<h1><a href="http://www.gudmap.org/index.html">GUDMAP</a></h1>
			<div id="menu">
				<ul id="nav">
					<li id="L_About"><a href="http://www.gudmap.org/About/">About&nbsp;GUDMAP</a>
						<ul>
							<li><a href="http://www.gudmap.org/About/Goal.html">Goal</a></li>
							<li><a href="http://www.gudmap.org/About/Projects/">Projects</a></li>
							<li><a href="http://www.gudmap.org/About/Consortium.html">Consortium</a></li>
							<li><a href="http://www.gudmap.org/About/Tutorial/">Tutorial</a></li>
							<li><a href="http://www.gudmap.org/About/News/">News</a></li>
							<li><a href="http://www.gudmap.org/About/Positions.html">Positions</a></li>
							<li><a href="http://www.gudmap.org/About/Usage.html">Website Content Usage</a></li>
						</ul>
					</li>
					<li id="L_Research"><a href="http://www.gudmap.org/Research/">Research</a>
						<ul>
							<li><a href="http://www.gudmap.org/Research/MAGD.html">Molecular Anatomy of Genitourinary Development</a></li>
							<li><a href="http://www.gudmap.org/Research/GGEM.html">Generation of Genetically Engineered Mice</a></li>
							<li><a href="http://www.gudmap.org/Research/Protocols/">Project Protocols</a></li>
							<li><a href="http://www.gudmap.org/Research/Pubs/">Publications</a></li>
						</ul>
					</li>
					<li id="L_Database"><a href="http://www.gudmap.org/gudmap/">Database</a>
						<%--
						<ul>
							<li><a href="<c:out value="${pageContext.request.contextPath}" />/pages/ish_browse_all.html">Browse All</a></li>
							<li><a href="<c:out value="${pageContext.request.contextPath}" />/pages/focus.html">Focus</a></li>
							<li><a href="<c:out value="${pageContext.request.contextPath}" />/pages/query_db.html">Query</a></li>
							<li><a href="<c:out value="${pageContext.request.contextPath}" />/pages/lab_summaries.html">Lab Summary</a></li>
							<li><a href="<c:out value="${pageContext.request.contextPath}" />/pages/help/index.htm">Help</a></li>
						</ul>--%>
					</li>
					<li id="L_Resources"><a href="http://www.gudmap.org/Resources/">Resources</a>
						<ul>
							<li><a href="http://www.gudmap.org/Resources/Ontologies.html">Ontologies</a></li>
		 					<li><a href="http://www.gudmap.org/Resources/MouseStrains/">Mouse Strains</a></li>
						</ul>
					</li>
					<li id="L_Links"><a href="http://www.gudmap.org/Links/">Links</a>
						<ul>
							<li><a href="http://www.gudmap.org/Links/Gen_Disease.html">Genitourinary Disease</a></li>
							<li><a href="http://www.gudmap.org/Links/Gen_Development.html">Genitourinary Development</a></li>
							<li><a href="http://www.gudmap.org/Links/Gene_Expression.html">Gene Expression</a></li>
							<li><a href="http://www.gudmap.org/Links/Funding.html">Funding</a></li>
							<li><a href="http://www.gudmap.org/Links/Events.html">Events</a></li>
							<li><a href="http://www.gudmap.org/Links/Message_Board.html">Message Board</a></li>
							<li><a href="http://www.gudmap.org/Links/qc.html">ISH Quality Control</a></li>
						</ul>
					</li>
					<li id="L_Internal"><a href="http://www.gudmap.org/Internal/">Internal</a>
						<ul>
							<li><a href="http://www.gudmap.org/Internal/Calendars/">Calendars</a></li>
							<li><a href="http://www.gudmap.org/Internal/Agendas/">Agendas</a></li>
							<li><a href="http://www.gudmap.org/Internal/Directory.html">Directory</a></li>
						</ul>
					</li>
					<li id="L_Contact"><a href="http://www.gudmap.org/Contact/">Contact</a>
						<ul>
							<li><a href="http://www.gudmap.org/Contact/ISS.html">In Situ Screening</a></li>
							<li><a href="http://www.gudmap.org/Contact/MP.html">MicroArray Profiling</a></li>
							<li><a href="http://www.gudmap.org/Contact/Mice.html">Genetically Modified Mice</a></li>
							<li><a href="http://www.gudmap.org/Contact/DB.html">Database</a></li>
							<li><a href="http://www.gudmap.org/Contact/NIH.html">NIH</a></li>
						</ul>
					</li>
				</ul>
			</div>
		</div>
		<div id="content">
			<c:if test="${headerParam!='onlyHeader'" >
			</f:verbatim>
				<h:panelGroup><f:verbatim>
					<div style="float:right"></f:verbatim>
						<h:form id="loginLinkForm" >
							<h:panelGroup rendered="#{UserBean.userLoggedIn}">
								<h:outputText value="User: #{UserBean.userName}, " styleClass="plaintext" />
								<h:outputLink styleClass="plaintext" onclick="logoutClicked(); return false;" value="#">
									<h:outputText value="LOGOUT" />
								</h:outputLink>
								<h:commandLink id="logoutLink" action="#{UserBean.logout}" />
							</h:panelGroup>				
							<h:outputLink id="loginLink" styleClass="plaintext" value="#" onclick="onLoginClicked(); return false;" rendered="#{!UserBean.userLoggedIn}">
								<h:outputText value="LOGIN" />
							</h:outputLink>
							<h:inputHidden id="displayLoginPanel" value="#{UserBean.displayLoginPanel}" />		
						</h:form><f:verbatim>
					</div>
		
					<div id="loginPanel" class="loginPanel" style="width:305">
						<iframe name="loginPanel" src="${pageContext.request.contextPath}/pages/secure/login_panel.html?complete=0&amp;hl=en" width="100%" height="195" frameborder="1" scrolling="auto">
						</iframe>
						
					</div>
					
					<c:set var="focusedOrgan" value="<%= gmerg.utils.Visit.getRequestParam("focusedOrgan") %>" />
					<c:set var="organName" value="<%= gmerg.beans.DatabaseHomepageBean.getOrganName(gmerg.utils.Visit.getRequestParam("focusedOrgan")) %>" />
					<span class="bigplaintext">
						
						
						<c:if test="${(focusedOrgan==null || focusedOrgan=='') && (headerParam==null || headerParam=='')}" >
							<a href="${pageContext.request.contextPath}/pages/database_homepage.html" class="bigplaintext" >GUDMAP Gene Expression</a>
						</c:if>
						<c:if test="${!(focusedOrgan==null || focusedOrgan=='') && (headerParam==null || headerParam=='')}" >
							<a href="database_homepage.html" class="bigplaintext" >GUDMAP Gene expression</a>
							&nbsp;>&nbsp;
							<a href="${pageContext.request.contextPath}/pages/database_homepage.html?focusedOrgan=${focusedOrgan}" class="bigplaintext" >${organName}</a>
						</c:if>
						<c:if test="${headerParam=='databaseHomepage' && !(focusedOrgan==null || focusedOrgan=='')}" >
							<a href="database_homepage.html" class="bigplaintext" >GUDMAP Gene expression</a>
							&nbsp;>&nbsp;${organName}
						</c:if>
						
						<c:if test="${(headerParam=='databaseHomepage') && (focusedOrgan==null || focusedOrgan=='')}" >
							GUDMAP Gene Expression
						</c:if>
						<c:if test="${pageName=='/pages/edit_entry_page_per_lab.jsp' || pageName=='/pages/lab_ish_edit.jsp'}" >
							
							&nbsp;>&nbsp;
							<a href="${pageContext.request.contextPath}/pages/edit_entry_page_per_lab.html" class="bigplaintext" >Annotation Tool</a>
						</c:if>
					</span>
					</f:verbatim>
				</h:panelGroup><f:verbatim>
			</c:if>
			<table border="0" class="defaultWidth" cellpadding="0" cellspacing="0">
				<tr> <td> </td> </tr>
			</table>
			<br/>
			<div id="mainclass" class="mainclass">
				<table border="0"  class="defaultWidth" cellpadding="0" cellspacing="0">
					<tr><td></f:verbatim>
</f:subview>

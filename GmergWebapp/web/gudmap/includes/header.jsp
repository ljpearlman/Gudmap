<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="https://ajax4jsf.dev.java.net/ajax" prefix="a4j"%>

<fmt:setBundle basename="configuration" />

<fmt:message var="proj" scope="application" key="project" />
<fmt:message var="perspective" scope="application" key="perspective" />
<fmt:message var="stageSeriesShort" scope="application" key="stage_series_short" />
<fmt:message var="stageSeriesMedium" scope="application" key="stage_series_medium" />
<fmt:message var="stageSeriesLong" scope="application" key="stage_series_long" />
<fmt:message var="siteSpecies" scope="application" key="species"/>
<c:set var="pageName" value="${pageContext.request.servletPath}" />

<f:subview id="headersubview">
  <a4j:keepAlive beanName="UserBean" />
  <a4j:keepAlive beanName="HeaderQuickSearchBean" />
<f:verbatim>

<html>
<head>
	<title>Gudmap Gene Expression Database</title>
	
	<!--[if IE]>
	<style type="text/css">
		.twoColHybLtHdr #sidebar1 { padding-top: 30px; }
		.twoColHybLtHdr #mainContent { zoom: 1; padding-top: 15px; }
	</style>
	<![endif]-->

	<script src="http://www.gudmap.org/Scripts/AC_RunActiveContent.js" type="text/javascript"></script>
	<script src="http://www.gudmap.org/Scripts/swfobject_modified.js" type="text/javascript"></script>

	<script src="http://www.gudmap.org/SpryAssets/SpryValidationTextField.js" type="text/javascript"></script>
	<script src="http://www.gudmap.org/SpryAssets/SpryValidationSelect.js" type="text/javascript"></script>

	<link href="http://www.gudmap.org/SpryAssets/SpryValidationTextField.css" rel="stylesheet" type="text/css" />
	<link href="http://www.gudmap.org/SpryAssets/SpryValidationSelect.css" rel="stylesheet" type="text/css" />
	<link href="http://www.gudmap.org/SpryAssets/SpryMenuBarHorizontal.css" rel="stylesheet" type="text/css" />
	<link href="${pageContext.request.contextPath}/css/gudmap_css.css" type="text/css" rel="stylesheet" />
	<link href="${pageContext.request.contextPath}/css/gudmap_customcomponents_css.css" type="text/css" rel="stylesheet" />
	<link href="${pageContext.request.contextPath}/css/help.css" type="text/css" rel="stylesheet" />
	<link href="${pageContext.request.contextPath}/css/gudmapmain_css.css" type="text/css" rel="stylesheet" />
	<link href="${pageContext.request.contextPath}/css/TSContainer.css" type="text/css" rel="stylesheet" />
	<link href="${pageContext.request.contextPath}/css/TSGlossary.css" type="text/css" rel="stylesheet" />
	<link href="http://www.gudmap.org/Styles/main.css" type="text/css" rel="stylesheet" />
	<link href="${pageContext.request.contextPath}/css/table_css.css" type="text/css" rel="stylesheet" />

	<style type="text/css">
		@import url("${pageContext.request.contextPath}/css/ie51.css");
	</style>
	<script src="${pageContext.request.contextPath}/SpryAssets/SpryMenuBar.js" type="text/javascript"></script>
	<script src="${pageContext.request.contextPath}/scripts/yahoo.js" type="text/javascript"></script>
	<script src="${pageContext.request.contextPath}/scripts/event.js" type="text/javascript"></script>
	<script src="${pageContext.request.contextPath}/scripts/dom.js" type="text/javascript"></script>
	<script src="${pageContext.request.contextPath}/scripts/dragdrop.js" type="text/javascript"></script>
	<script src="${pageContext.request.contextPath}/scripts/animation.js" type="text/javascript"></script>
	<script src="${pageContext.request.contextPath}/scripts/container.js" type="text/javascript"></script>
	<script src="${pageContext.request.contextPath}/scripts/TSGlossary.js" type="text/javascript"></script>

	<script src="${pageContext.request.contextPath}/scripts/navfunctions.js" type="text/javascript"></script>
	<script src="${pageContext.request.contextPath}/scripts/ftiens4.js" type="text/javascript"></script>
	<script src="${pageContext.request.contextPath}/scripts/formfunctions.js" type="text/javascript"></script>
	<script src="${pageContext.request.contextPath}/scripts/header.js" type="text/javascript"></script>
	<script src="${pageContext.request.contextPath}/scripts/browse_table.js" type="text/javascript"></script>
	<script src="${pageContext.request.contextPath}/scripts/navbar.js" type="text/javascript"></script>
	<script src="${pageContext.request.contextPath}/scripts/ua.js" type="text/javascript"></script>
	
	
	<%-- moved here from database_homepage.jsp - xingjun - 22/07/2010 - start --%>
	<%-- avoid using another <header> in page database_homepage.jsp --%>
	<script language="JavaScript">
		document.onkeydown = function (event){ processEnterKey(event) };
		var searchLinkId=null;
	</script>
  
	<script type="text/javascript" src="../scripts/rico.js"></script>
<!-- Do not know why it needs to be refreshed, comment it out to see whether it is needed
	<c:set var="refreshRequired" value="<%= gmerg.control.GudmapSessionTimeoutFilter.isRefreshRequired(request.getRequestURI()) %>" />
	<c:set var="maxInactiveInterval" value="<%= session.getMaxInactiveInterval() %>" />
	<c:if test="${refreshRequired}" >
		<meta http-equiv="refresh" content="${maxInactiveInterval}">
	</c:if>
-->
	<c:set var="statusParams" value="<%= gmerg.utils.Visit.getStatus() %>" />
	<script type="text/javascript">
		function setCookie(c_name,value,expiredays)
		{
			var exdate=new Date();
			exdate.setDate(exdate.getDate()+expiredays);
			document.cookie=c_name+ "=" +escape(value)+
			((expiredays==null) ? "" : ";expires="+exdate.toGMTString());
		}
		function writeCookie() {
			setCookie("appName",navigator.appName, 360);
			setCookie("appVersion",navigator.appVersion, 360);
			setCookie("userAgent",navigator.userAgent, 360);
			setCookie("platform",navigator.platform, 360);
			//alert(navigator.appName+':'+navigator.appVersion+':'+navigator.userAgent+':'+navigator.platform);
		}
		writeCookie();
	</script>
	<%-- feedback popup window --%>
	<script type="text/javascript">
		var addressBar = window.location.href;
		function popupCenter(pageURL, windowName, w, h) {
			var left = (screen.width/2)-(w/2);
			var top = (screen.height/2)-(h/2);
			var targetWindow = window.open(pageURL, windowName, 'resizable=1,toolbar=0,scrollbars=1,' + 'width='+w+', height='+h+', top='+top+', left='+left);
		}
	</script>
	<%-- quick search URL processing --%>
	<link href="${pageContext.request.contextPath}/css/main.css" rel="stylesheet" type="text/css" />
	
	<c:set var="headerParam" value="${param['headerParam']}" />

</head>
<body class="twoColHybLtHdr" onload="initialisePage('${statusParams}'); ${param['headerOnloadParam']}" onunload="clearActionMethodsHistory()" onresize="adjustTableBodyPanels();" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
	<div id="container">
		<div align="center" id="headernew">
			<table class="banner" width="100%" height="100%" cellpadding="0px" cellspacing="0px">
				<tr><!-- main table tr -->
					<!-- First td is fixed, contains logo and blue curve -->
					<td height="85px" valign="top" width="300px" style="padding:0px;">
					<!-- First td contains a table, new table contains 2 tds -->
						<div class="fixedContainer">
						<table width="100%" cellpadding="0px" cellspacing="0px" border="0px" class="fixedWidth">
							<tr>
								<td valign="top">
								<table width="100%" cellpadding="0px" cellspacing="0px" border="0px">
									<tr><td class="top_blue_left3"></td></tr>
									<tr>
										<td>&nbsp;&nbsp;&nbsp;
											<a href="http://www.gudmap.org/index.html">
												<img src="http://www.gudmap.org/Images/Banner/GUDMAP_Banner2.png" width="215" height="41" alt="GUDMAP Logo" style="border:0px;padding-top:20px;" />
											</a>
										</td>
									</tr>
								</table>
								</td><!-- SPACE LEFT BECAUSE OTHERWISE THE FIRST TD IN MAIN TABLE WON'T BE DISPLAYED -->
								<td width="60px" height="85px" class="blue_curve" valign="top"></td>
							</tr>
						</table>
						</div>
					</td>
					<!-- Second td is fixed, contains Search options -->
					<td height="85px" valign="top" width="">
					<table width="100%" cellpadding="0px" cellspacing="0px" border="0px">
						<tr>
							<td class="blue_1px_top" valign="top" align="right">
							<table cellpadding="2" align="right" valign="top">
							<tr>
							<td>
	</f:verbatim>
							<h:form id="quickSearchForm" >
							<!-- dummy button to allow search on hitting RETURN -->
							<h:commandButton action="#{HeaderQuickSearchBean.quickSearch}" style="display:none;width:2px;" value="submit"/>
							<h:panelGrid columns="5" cellpadding="2" cellspacing="0" border="0" columnClasses="width:115px;width:130px;width:20px;width:210px;width:75px" style="margin-left:auto; margin-right:0px; border-color:#FFFFFF;width:550px">
								<h:outputText value="Quick search: " styleClass="search_text"/>
								<h:selectOneMenu id="quickSearchType" styleClass="selectBanner" immediate="true" value="#{HeaderQuickSearchBean.quickSearchType}" 
												valueChangeListener="#{HeaderQuickSearchBean.changeSearchType}"  style="color:#000; font-family:Verdana, Geneva, sans-serif">
									<f:selectItems value="#{HeaderQuickSearchBean.quickSearchTypeItems}"/>
					                                                                <a4j:support event="onchange" reRender="quickSearchType" />
								</h:selectOneMenu>
								<h:outputText value=" for " styleClass="search_text"/>
								<h:inputText id="quickSearchInput" value="#{HeaderQuickSearchBean.quickSearchInput}" styleClass="textfield"/>
								<h:commandLink id="quickSearchButton" action="#{HeaderQuickSearchBean.quickSearch}" styleClass="quick_search">
									<h:graphicImage value="/images/search.png" title="Click to search" alt="search" styleClass="icon" style="border:0"/>
									<f:param name="input" value="#{HeaderQuickSearchBean.quickSearchInput}" />
								</h:commandLink>
							</h:panelGrid>
							</h:form>
	<f:verbatim>
							</td>
							</tr>
							</table>
							</td>
						</tr>
						<tr>
							<td width="100%" valign="bottom" height="54px">
							<table cellpadding="2" align="right" valign="top">
								<tr>
									<td><h:graphicImage value="/images/bottom_bar.png"/></td>
									<td><h:graphicImage value="/images/login_icon.png"/></td>
									<td>
	</f:verbatim>
									<h:form id="loginLinkForm" >
									<h:panelGrid columns="1">
										<h:panelGroup rendered="#{UserBean.userLoggedIn}">
											<h:outputText value="User: #{UserBean.userName} " styleClass="italictext" />
											<f:verbatim>&nbsp;&nbsp;</f:verbatim>
											<h:commandLink styleClass="text_bottom" action="#{UserBean.logout}" immediate="true">
												<h:outputText value="Logout" />
											</h:commandLink>
										</h:panelGroup>
										<h:panelGroup rendered="#{!UserBean.userLoggedIn}" >
											<h:outputLink id="loginLink" styleClass="text_bottom" value="#{HeaderQuickSearchBean.applicationRoot}pages/secure/login.html?#{Visit.statusParam}" >
												<h:outputText value="Login" />
											</h:outputLink>
										</h:panelGroup>
									</h:panelGrid>
									</h:form>
	<f:verbatim>
									</td>
									<td><h:graphicImage value="/images/bottom_bar.png"/></td>
									<td><h:graphicImage value="/images/feedback_icon.png" alt="feedback_button" styleClass="icon" style="border:0" /></td>
									<td>
	</f:verbatim>
									<h:panelGroup>
										<h:outputLink styleClass="text_bottom" value="javascript:popupCenter('http://www.gudmap.org/Feedback/popupgudmap.php', 'feedbackGudmap', 570, 610);" >
											Feedback
										</h:outputLink>
									</h:panelGroup>
	<f:verbatim>
									</td>
									<td><h:graphicImage value="/images/bottom_bar.png"/></td>
								</tr>
							</table>
							</td>
						</tr>
					</table>
					</td>
				</tr><!-- main table, closing tr -->
				<tr bgcolor="#CCCCCC">
					<td style="margin-top:-1px; vertical-align:middle; padding: 0.3em;" bgcolor="#CCCCCC" colspan="3" align="center">
					<ul id="MenuBar1" class="MenuBarHorizontal" >
<!-- do not know the reason fonts become smaller without changing any css. ==> individual tag setting -->
						<li><a class="bignavtext" href="http://www.gudmap.org/index.html">Home</a></li>
						<li>
							<a href="http://www.gudmap.org/About/index.html" class="MenuBarItemSubmenu bignavtext">About GUDMAP</a>
							<ul>
								<li><a class="mediumnavtext" href="http://www.gudmap.org/About/Goal.html">Goals</a></li>
								<li><a  class="mediumnavtext" href="http://www.gudmap.org/About/Projects/index.html">Projects</a></li>
								<li><a  class="mediumnavtext" href="http://www.gudmap.org/Research/Pubs/index.html">Publications</a></li>
								<li><a  class="mediumnavtext" href="http://www.gudmap.org/Contact/index.html">Contacts</a></li>
								<li><a  class="mediumnavtext" href="http://www.gudmap.org/About/Positions.html">Positions</a></li>
								<li><a  class="mediumnavtext" href="http://www.gudmap.org/Links/Funding.html">Funding</a></li>
								<li><a  class="mediumnavtext" href="http://www.gudmap.org/Links/Events.html">Events</a></li>
								<li><a  class="mediumnavtext" href="http://www.gudmap.org/Links/index.html">Links</a></li>
								<li><a  class="mediumnavtext" href="http://www.biomedatlas.org/smf/index.php?page=gudmap">Message Board</a></li>
								<li><a  class="mediumnavtext" href="http://www.gudmap.org/About/Usage.html">Citing GUDMAP</a></li>
							</ul>
						</li>
						<li>
							<a class="MenuBarItemSubmenu bignavtext" href="http://www.gudmap.org/Menu_Index/Gene_Expression.html">Gene Expression</a>
							<ul>
								<li><a  class="mediumnavtext" href="/gudmap/">Query/Browse Database</a>                </li>
								<li><a  class="mediumnavtext" href="http://www.gudmap.org/Organ_Summaries/index.php">Tissue Summaries</a></li>
								<li><a  class="mediumnavtext" href="http://www.gudmap.org/Quality_Control/ISH/index.html">ISH Quality Control</a></li>
							</ul>
						</li>
						<li>
							<a  href="http://www.gudmap.org/Resources/index.html" class="MenuBarItemSubmenu bignavtext">Resources</a>
							<ul>
		                                                                                                <li><a  class="mediumnavtext" href="http://www.gudmap.org/Submission_Archive/index.html">Submission Archive</a></li>
                                                                                                                                <li><a  class="mediumnavtext" href="http://www.gudmap.org/Research/Protocols/index.html">Project Protocols</a></li>
                                                                                                                                <li><a  class="mediumnavtext" href="http://www.gudmap.org/Resources/MouseStrains/index.html">Mice/Cell Lines</a></li>
                                                                                                                                <li><a  class="mediumnavtext" href="http://www.gudmap.org/Resources/OPT/index.html">3D Atlas</a></li>
                                                                                                                                <li><a  class="mediumnavtext" href="http://www.gudmap.org/Resources/Ontology/index.php">Ontology</a></li>
                                                                                                                                <li><a  class="mediumnavtext" href="http://www.gudmap.org/Schematics/index.php">Schematics</a></li>
                                                                                                                                <li><a  class="mediumnavtext" href="http://www.gudmap.org/biomart/">Biomart</a></li>
                                                                                                                                <li><a  class="mediumnavtext" href="http://uqgudmap.imb.uq.edu.au" target="_blank">UQ GUDMAP - Probe Design</a></li>
                                                                                                                                <li><a  class="mediumnavtext" href="http://www.gudmap.org/Website_Reports/index.html">Web Stats</a></li>
                                                                                                                                <li><a  class="mediumnavtext" href="http://www.gudmap.org/Internal/index.html">Internal</a></li>
							</ul>
						</li>
						<li>
							<a  href="http://www.gudmap.org/About/Tutorial/index.html" class="MenuBarItemSubmenu bignavtext">Tutorials</a>
							<ul>
								<li><a class="mediumnavtext" href="http://www.gudmap.org/About/Tutorial/Overview.html">Overview</a></li>
								<li><a class="mediumnavtext" href="http://www.gudmap.org/About/Tutorial/DevMUS.html">Urinary Development</a></li>
								<li><a class="mediumnavtext" href="http://www.gudmap.org/About/Tutorial/DevMRS.html">Reproductive Development</a></li>
							</ul>
						</li>
						<li>
							<a class="bignavtext" href="http://www.gudmap.org/gudmap_dis/index.jsp">Disease</a>
						</li>
						<li>
							<a  href="http://www.gudmap.org/Menu_Index/Help.html" class="MenuBarItemSubmenu bignavtext">Help</a>
							<ul>
								<li><a  class="mediumnavtext" href="http://www.gudmap.org/Help/index.html">Using Database</a></li>
								<li><a  class="mediumnavtext" href="http://www.gudmap.org/Help/Website_Help.html">Website</a></li>
								<li><a  class="mediumnavtext" href="http://www.gudmap.org/Help/Glossary.html">Glossary</a></li>
								<li><a  class="mediumnavtext" href="http://www.gudmap.org/Welcome/For_Clinicians.html">Clinicians</a></li>
								<li><a  class="mediumnavtext" href="http://www.gudmap.org/Welcome/For_Biologists.html">Biologists</a></li>
								<li><a  class="mediumnavtext" href="http://www.gudmap.org/Welcome/For_Bioinformaticians.html">Bioinformaticians</a></li>
								<li><a  class="mediumnavtext" href="http://www.gudmap.org/Welcome/For_Public.html">Public</a></li>
								<li><a  class="mediumnavtext" href="http://www.gudmap.org/Help/Sys_Requirements.html">System Requirements</a></li>
							</ul>
						</li>
					</ul>
				</td>
				</tr>
			</table><!-- end banner -->

<!-- now start menu -->
		</div><!-- end #headernew -->
		<div id="sidebar1"><!-- TemplateBeginEditable name="sidebarRegion" -->
			<div id="sidebarinner" ><!-- #BeginLibraryItem "/Library/db_nav.lbi" -->
				<br/>
	                                                <p class="db_expression_database sidebar_first_item"><a class="bigboldnavtext" href="${pageContext.request.contextPath}/pages/database_homepage.html">Expression Database</a></p>
				<p><a  class="mediumnavtext" href="http://www.gudmap.org/Organ_Summaries/index.php">Tissue Summaries</a></p>
				<p><a  class="mediumnavtext" href="${pageContext.request.contextPath}/pages/genelist_tree.html">Analysis</a></p>
				<%-- <p><a  class="mediumnavtext" href="${pageContext.request.contextPath}/pages/edit_entry_page_per_lab.html">Annotate</a></p> --%>
				<p><a  class="mediumnavtext" href="http://www.gudmap.org/Submission_Archive/index.html">Downloads</a></p>
				<p><a  class="mediumnavtext" href="${pageContext.request.contextPath}/pages/lab_summaries.html">Data Source</a></p>
				<p><a  class="mediumnavtext" href="${pageContext.request.contextPath}/pages/collectionList_browse.html">Collections</a></p>
			</div><!-- #EndLibraryItem -->
			<h3>&nbsp; </h3>
			<p>&nbsp;</p>
		</div><!-- TemplateEndEditable --><!-- TemplateBeginEditable name="mainEdit" -->
		<div id="mainContent"></f:verbatim>
			<table id="mainContentInnerTable" border="0" width="100%" cellspacing="0" cellpadding="0" class="mainContentInnerTable">
			<tr>
				<td style="width:2px">
					<div id="sidebar2"></div>
				</td>
				<td valign="top" >
					<c:if test="${headerParam!='onlyHeader'}">
						<h:panelGrid width="100%" columns="2" columnClasses="headerTitle, rightAlign" >
							<f:verbatim>
								<c:set var="focusedOrgan" value="<%= gmerg.utils.Visit.getRequestParam(\"focusedOrgan\") %>" />
								<c:set var="organName" value="<%= gmerg.beans.DatabaseHomepageBean.getOrganName(gmerg.utils.Visit.getRequestParam(\"focusedOrgan\")) %>" />
								<span><h1>
									<c:if test="${(focusedOrgan==null || focusedOrgan=='') && (headerParam==null || headerParam=='')}" >Expression Database
									</c:if>
									<c:if test="${!(focusedOrgan==null || focusedOrgan=='') && (headerParam==null || headerParam=='')}" >Expression Database
										&nbsp;>&nbsp;
										<a href="${pageContext.request.contextPath}/pages/database_homepage.html?focusedOrgan=${focusedOrgan}">${organName}</a>
									</c:if>
									<c:if test="${headerParam=='databaseHomepage' && !(focusedOrgan==null || focusedOrgan=='')}" >Expression Database
										&nbsp;>&nbsp;${organName}
									</c:if>
									
									<c:if test="${(headerParam=='databaseHomepage') && (focusedOrgan==null || focusedOrgan=='')}" >
 										Expression Database
<!-- 										Expression Database &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
										<input type="button" value="Try GUDMAP BETA search" onclick="var w=window.open('gudmap_beta.jsf','wholemountPopup','resizable=1,toolbar=0,scrollbars=1,width=600,height=500');w.focus();return false;"/>
-->										
									</c:if>
									<c:if test="${pageName=='/pages/edit_entry_page_per_lab.jsp' || pageName=='/pages/lab_ish_edit.jsp'}" >
										&nbsp;>&nbsp;Annotate
										<a href="#Link233865Context" name="Link233865Context" id="Link233865Context" style="cursor:help" onclick="javascript:createGlossary('TSGlossaryPanelID233865', 'Annotate', 'This page allows the user to submit gene expression annotations using the online annotation tool. This page requires a login.  &lt;a href=\&quot;http://www.gudmap.org/Help/Annotation_Tool_Help.html\&quot;&gt;More... &lt;/a&gt;', 'Link233865Context')"><img src="../images/focus/information_big.gif" width="42" height="44" border="0" /></a>
									</c:if>
									<c:if test="${pageName=='/pages/all_components_genelists.jsp'}" >
										&nbsp;>&nbsp;Analyses
										<a href="#Link703766Context" name="Link703766Context" id="Link703766Context" style="cursor:help" onclick="javascript:createGlossary('TSGlossaryPanelID703766', 'Analyses', 'This page returns a list of analyses on the GUDMAP microarray data. Each row in the table represents a gene list. To view a gene list as a heatmap table or table of expression values, click on the gene list name. To view as an interactive heatmap display, click Treeview.', 'Link703766Context')"><img src="../images/focus/information_big.gif" width="42" height="44" border="0" /></a>
									</c:if>
									<c:if test="${pageName=='/pages/lab_summaries.jsp'}" >
										&nbsp;>&nbsp;Data Source Summary
										<a href="#Link790484Context" name="Link790484Context" id="Link790484Context" style="cursor:help" onclick="javascript:createGlossary('TSGlossaryPanelID790484', 'Lab Summary', 'This page returns a list of In situ and microarray data submitted to the database, organized by Lab and submission date.  &lt;br&gt;Clicking on the Number of Submissions column where the status is Available will return that submission set.', 'Link790484Context')"><img src="../images/focus/information_big.gif" width="42" height="44" border="0" /></a>
									</c:if>
									<%-- xingjun - 22/04/2010 - start --%>
									<c:if test="${pageName=='/pages/genelist_folder.jsp'}" >
										&nbsp;>&nbsp;Microarray Analysis
									</c:if>
									<c:if test="${pageName=='/pages/mastertable_browse.jsp'}" >
										&nbsp;>&nbsp;Microarray Expression
									</c:if>
									<c:if test="${pageName=='/pages/gene_query_result.jsp'}" >
										&nbsp;>&nbsp;Gene Strips
									</c:if>
									<%-- xingjun - 22/04/2010 - end --%>
									<c:if test="${pageName=='/pages/secure/login.jsp'}" >
										&nbsp;>&nbsp;Login
										<a href="#Link735755Context" name="Link735755Context" id="Link735755Context" style="cursor:help" onclick="javascript:createGlossary('TSGlossaryPanelID735755', 'Login', 'Login is optional. &lt;br&gt;When logged-in you can save personal collections of genes, images and database entries and submit new gene expression annotations using the online annotation tool. Please contact &lt;a href=\&quot;mailto:GUDMAP-EDITORS@gudmap.org\&quot;&gt; GUDMAP-EDITORS@gudmap.org &lt;/a&gt; for a login. &lt;br&gt;', 'Link735755Context')"><img src="../images/focus/information_big.gif" width="42" height="44" border="0" /></a>
									</c:if>
									<c:if test="${pageName=='/pages/collectionList_browse.jsp'}" >
										&nbsp;>&nbsp;Collections
										<a href="#Link59415Context" name="Link59415Context" id="Link59415Context" style="cursor:help" onclick="javascript:createGlossary('TSGlossaryPanelID59415', 'Collections', 'From this page you can manage your list of collections of genes, images or database entries.   &lt;a href=\&quot;http://www.gudmap.org/Help/Collection_Help.html\&quot; &gt;More... &lt;/a&gt;&lt;br&gt;', 'Link59415Context')"><img src="../images/focus/information_big.gif" width="42" height="44" border="0" /></a>
									</c:if>
									<c:if test="${pageName=='/pages/boolean_test.jsp'}" >
										&nbsp;>&nbsp;Boolean Anatomy Search
									</c:if>
									<c:if test="${pageName=='/pages/genelist_tree.jsp'}" >
										&nbsp;>&nbsp;Gene Lists from Microarray Analysis
									</c:if>								</h1></span>
							</f:verbatim>
						</h:panelGrid>
					</c:if>
					
</f:subview>

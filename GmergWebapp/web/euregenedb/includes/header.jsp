<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
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


<html>
  <head>
    <title>EuReGene Gene Expression Database</title>
    <link href="<c:out value="${pageContext.request.contextPath}" />/css/euregene_css.css" type="text/css" rel="stylesheet">
    <link href="<c:out value="${pageContext.request.contextPath}" />/css/help.css" type="text/css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/table_css.css" type="text/css" rel="stylesheet" />
    <link rel="icon" href="/favicon.ico" type="image/ico">
	
	<script src="${pageContext.request.contextPath}/SpryAssets/SpryMenuBar.js" type="text/javascript"></script>
    <script src="<c:out value="${pageContext.request.contextPath}" />/scripts/formfunctions.js" type="text/javascript"></script>
    <script src="<c:out value="${pageContext.request.contextPath}" />/scripts/ua.js" type="text/javascript"></script>
    <script src="<c:out value="${pageContext.request.contextPath}" />/scripts/ftiens4.js" type="text/javascript"></script>
    <script src="<c:out value="${pageContext.request.contextPath}" />/scripts/header.js" type="text/javascript"></script>
    <script src="<c:out value="${pageContext.request.contextPath}" />/scripts/autosuggest.js" type="text/javascript"></script>
	<script src="${pageContext.request.contextPath}/scripts/navfunctions.js" type="text/javascript"></script>
	<script src="${pageContext.request.contextPath}/scripts/browse_table.js" type="text/javascript"></script>
	<script src="${pageContext.request.contextPath}/scripts/navbar.js" type="text/javascript"></script>
    
    <c:set var="refreshRequired" value="<%= gmerg.control.GudmapSessionTimeoutFilter.isRefreshRequired(request.getRequestURI()) %>" />
	<c:set var="maxInactiveInterval" value="<%= session.getMaxInactiveInterval() %>" />
	<c:if test="${refreshRequired}" >
		<meta http-equiv="refresh" content="${maxInactiveInterval}">
	</c:if>
	<c:set var="statusParams" value="<%= gmerg.utils.Visit.getStatus() %>" />
	<c:set var="headerParam" value="${param['headerParam']}" />
    
  </head>

 <body class="twoColHybLtHdr" onload="noSmartScroll=true;initialisePage('${statusParams}'); ${param['headerOnloadParam']}" onunload="clearActionMethodsHistory()">
    <table width="955" border="0" align="center" cellpadding="0" cellspacing="0">
      <tr>
        <td><a name="top"></a><a href="/euregene/pages/news.htm"><img src="<c:out value="${pageContext.request.contextPath}" />/images/EuReGene_header.jpg" alt="" width="955" height="141" border="0"></a></td>
      </tr>
      <tr>
        <td>
	      <table width="100%" border="0" cellpadding="0" cellspacing="0">
	        <tr>
              <td align="left" class="stripey nav3">
	            <table width="50%" border="0" cellspacing="0" cellpadding="0">
                  <tr class="stripey">
                    <td width="20" align="left" valign="top"><img src="<c:out value="${pageContext.request.contextPath}" />/images/spacet.gif" alt="" width="20" height="25"></td>
            	    <td width="294" class="plaintextbold"><a class="plaintextbold" href="<c:out value="${pageContext.request.contextPath}" />/">Gene Expression Database</a></td>
		            <td width="164"><img src="<c:out value="${pageContext.request.contextPath}" />/images/spacet.gif" alt="" width="20" height="25"></td>
          	      </tr>
                </table>
	          </td>
            </tr>
        <tr>
              <td>&nbsp;</td>
            </tr>
            <tr>
              <td>
              <c:if test="${pageName != '/pages/db_home.jsp'}">
                <div class="navTree">
                  <a href="/portal/index.html" class="plaintext">Kidney Atlas Data Portal</a>
                  <span class="plaintext">&nbsp;&nbsp;&gt;&nbsp;&nbsp;</span>
                  <a href="/euregenedb/" class="plaintext">Gene Expression Home</a>
                </div>
              </c:if>